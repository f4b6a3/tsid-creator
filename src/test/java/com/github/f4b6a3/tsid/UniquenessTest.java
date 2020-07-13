package com.github.f4b6a3.tsid;

import java.util.HashSet;

import com.github.f4b6a3.tsid.TsidCreator;
import com.github.f4b6a3.tsid.creator.TimeIdCreator;
import com.github.f4b6a3.tsid.exception.TsidCreatorException;
import com.github.f4b6a3.tsid.strategy.timestamp.FixedTimestampStretegy;
import com.github.f4b6a3.tsid.util.TsidTime;

/**
 * This is is not included in the {@link TestSuite} because it may take a long
 * time to finish.
 */
public class UniquenessTest {

	private int threadCount; // Number of threads to run
	private int requestCount; // Number of requests for thread

	private HashSet<Long> hashSet = new HashSet<>();

	private long now = TsidTime.getCurrentTimestamp();

	private boolean verbose; // Show progress or not

	/**
	 * Initialize the test.
	 * 
	 * @param threadCount
	 * @param requestCount
	 * @param creator
	 */
	public UniquenessTest(int threadCount, int requestCount, boolean progress) {
		this.threadCount = threadCount;
		this.requestCount = requestCount;
		this.verbose = progress;
	}

	/**
	 * Initialize and start the threads.
	 */
	public void start() {

		Thread[] threads = new Thread[this.threadCount];

		// Instantiate and start many threads
		for (int i = 0; i < this.threadCount; i++) {
			threads[i] = new Thread(new UniquenessTestThread(i, verbose));
			threads[i].start();
		}

		// Wait all the threads to finish
		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	public class UniquenessTestThread implements Runnable {

		private int id;
		private boolean verbose;

		private TimeIdCreator creator;

		public UniquenessTestThread(int id, boolean verbose) {
			this.id = id;
			this.verbose = verbose;
			this.creator = TsidCreator.getTimeIdCreator1024(id).withTimestampStrategy(new FixedTimestampStretegy(now));
		}

		/**
		 * Run the test.
		 */
		@Override
		public void run() {

			int progress = 0;
			int max = requestCount;

			for (int i = 0; i < max; i++) {
				// Request a TSID
				long tsid = 0;
				try {
					tsid = creator.create();
				} catch (TsidCreatorException e) {
					tsid = creator.create();
				}

				if (verbose) {
					// Calculate and show progress
					progress = (int) ((i * 1.0 / max) * 100);
					if ((progress) % 10 == 0) {
						System.out.println(String.format("[Thread %06d] %s %s %s%%", id, tsid, i, (int) progress));
					}
				}
				synchronized (hashSet) {
					// Insert the value in cache, if it does not exist in it.
					if (!hashSet.add(tsid)) {
						System.err.println(
								String.format("[Thread %06d] %s %s %s%% [DUPLICATE]", id, tsid, i, (int) progress));
					}
				}
			}

			if (verbose) {
				// Finished
				System.out.println(String.format("[Thread %06d] Done.", id));
			}
		}
	}

	public static void execute(int threadCount, int requestCount, boolean verbose) {
		UniquenessTest test = new UniquenessTest(threadCount, requestCount, verbose);
		test.start();
	}

	public static void main(String[] args) {
		int threadCount = 1024; // 2^10 (node bit length)
		int requestCount = 4096; // 2^12 (counter bit length)
		boolean verbose = true;
		execute(threadCount, requestCount, verbose);
	}
}
