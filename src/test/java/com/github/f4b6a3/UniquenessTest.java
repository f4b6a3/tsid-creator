package com.github.f4b6a3;

import java.util.HashSet;

import com.github.f4b6a3.commons.util.Base32Util;
import com.github.f4b6a3.tsid.TsidCreator;
import com.github.f4b6a3.tsid.creator.TimeSortableIdCreator;
import com.github.f4b6a3.tsid.exception.TsidCreatorException;
import com.github.f4b6a3.tsid.strategy.timestamp.FixedTimestampStretegy;
import com.github.f4b6a3.tsid.util.TsidConverter;
import com.github.f4b6a3.tsid.util.TsidTimeUtil;

/**
 * 
 * This test starts many threads that keep requesting thousands of TSIDs to a
 * single generator.
 * 
 * This is is not included in the {@link TestSuite} because it takes a long time
 * to finish.
 */
public class UniquenessTest {

	private int threadCount; // Number of threads to run
	private int requestCount; // Number of requests for thread

	// private long[][] cacheLong; // Store values generated per thread
	private HashSet<Long> hashSet;

	private boolean verbose; // Show progress or not

	private TimeSortableIdCreator creator;

	/**
	 * Initialize the test.
	 * 
	 * @param threadCount
	 * @param requestCount
	 * @param creator
	 */
	public UniquenessTest(int threadCount, int requestCount, TimeSortableIdCreator creator, boolean progress) {
		this.threadCount = threadCount;
		this.requestCount = requestCount;
		this.creator = creator;
		this.verbose = progress;
		this.initCache();
	}

	private void initCache() {
		this.hashSet = new HashSet<>();
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

		public UniquenessTestThread(int id, boolean verbose) {
			this.id = id;
			this.verbose = verbose;
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
					// Ignore the overrun exception and try again
					tsid = creator.create();
				}

				if (verbose) {
					// Calculate and show progress
					progress = (int) ((i * 1.0 / max) * 100);
					if ((progress) % 10 == 0) {
						String str = TsidConverter.toString(tsid);
						System.out.println(String.format("[Thread %06d] %s %s %s%%", id, str, i, (int) progress));
					}
				}
				synchronized (hashSet) {
					// Insert the value in cache, if it does not exist in it.
					if (!hashSet.add(tsid)) {
						String str = TsidConverter.toString(tsid);
						System.err.println(
								String.format("[Thread %06d] %s %s %s%% [DUPLICATE]", id, str, i, (int) progress));
					}
				}
			}

			if (verbose) {
				// Finished
				System.out.println(String.format("[Thread %06d] Done.", id));
			}
		}
	}

	public static void execute(boolean verbose, int threadCount, int requestCount) {
		TimeSortableIdCreator creator = TsidCreator.getTimeSortableIdCreator()
				.withTimestampStrategy(new FixedTimestampStretegy(TsidTimeUtil.getCurrentTimestamp()));

		UniquenessTest test = new UniquenessTest(threadCount, requestCount, creator, verbose);
		test.start();
	}

	public static void main(String[] args) {
		boolean verbose = true;
		int threadCount = 16; // Number of threads to run
		int requestCount = (int) Math.pow(2, 22) / threadCount;
		execute(verbose, threadCount, requestCount);
	}
}
