package com.github.f4b6a3.tsid.creator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.junit.Test;

import com.github.f4b6a3.tsid.TsidCreator;
import com.github.f4b6a3.tsid.exception.TsidCreatorException;
import com.github.f4b6a3.tsid.strategy.TimestampStrategy;
import com.github.f4b6a3.tsid.strategy.timestamp.FixedTimestampStretegy;
import com.github.f4b6a3.tsid.util.TsidTime;
import com.github.f4b6a3.tsid.util.TsidUtil;
import com.github.f4b6a3.tsid.util.TsidValidator;

public class TimeIdCreatorTest {

	private static final int TSID_LENGTH = 13;

	public static final int TIMESTAMP_LENGTH = 42;
	public static final int RANDOMNESS_LENGTH = 22;
	public static final int DEFAULT_NODEID_LENGTH = 10;

	private static final int DEFAULT_LOOP_MAX = 4096; // 2^12 (counter bit length)

	private static Random random = new Random();

	protected static final String DUPLICATE_UUID_MSG = "A duplicate TSID was created";

	protected static final int THREAD_TOTAL = availableProcessors();

	private static int availableProcessors() {
		int processors = Runtime.getRuntime().availableProcessors();
		if (processors < 4) {
			processors = 4;
		}
		return processors;
	}

	@Test
	public void testGetTsid() {
		long[] list = new long[DEFAULT_LOOP_MAX];

		long startTime = System.currentTimeMillis();

		TimeIdCreator creator = TsidCreator.getTimeIdCreator1024();

		for (int i = 0; i < DEFAULT_LOOP_MAX; i++) {
			list[i] = creator.create();
		}

		long endTime = System.currentTimeMillis();

		checkNullOrInvalid(list);
		checkUniqueness(list);
		checkCreationTime(list, startTime, endTime);
	}

	@Test
	public void testGetTsidWithNode() {

		final int nodeidLength = DEFAULT_NODEID_LENGTH;
		final int counterLength = RANDOMNESS_LENGTH - nodeidLength;
		final int counterMax = (int) Math.pow(2, counterLength);

		long[] list = new long[counterMax];

		int nodeid = random.nextInt();

		long startTime = System.currentTimeMillis();

		TimeIdCreator creator = TsidCreator.getTimeIdCreator1024(nodeid);

		for (int i = 0; i < counterMax; i++) {
			list[i] = creator.create();
		}

		long endTime = System.currentTimeMillis();

		checkNullOrInvalid(list);
		checkUniqueness(list);
		checkCreationTime(list, startTime, endTime);
	}

	@Test
	public void testGetTsidString() {
		String[] list = new String[DEFAULT_LOOP_MAX];

		long startTime = System.currentTimeMillis();

		for (int i = 0; i < DEFAULT_LOOP_MAX; i++) {
			list[i] = TsidCreator.getTsidString1024();
		}

		long endTime = System.currentTimeMillis();

		checkNullOrInvalid(list);
		checkUniqueness(list);
		checkCreationTime(list, startTime, endTime);
	}

	@Test
	public void testGetTsidStringWithNode() {

		final int nodeidLength = DEFAULT_NODEID_LENGTH;
		final int counterLength = RANDOMNESS_LENGTH - nodeidLength;
		final int counterMax = (int) Math.pow(2, counterLength);

		String[] list = new String[counterMax];

		int nodeid = random.nextInt();

		long startTime = System.currentTimeMillis();

		TimeIdCreator creator = TsidCreator.getTimeIdCreator1024(nodeid);

		for (int i = 0; i < counterMax; i++) {
			list[i] = creator.createString();
		}

		long endTime = System.currentTimeMillis();

		checkNullOrInvalid(list);
		checkUniqueness(list);
		checkCreationTime(list, startTime, endTime);
	}

	private void checkNullOrInvalid(long[] list) {
		for (long tsid : list) {
			assertNotEquals("TSID is zero", tsid, 0);
		}
	}

	@Test
	public void testOverrunExceptionZeroBitLength() {

		final int nodeidLength = 0; // zero bit length
		final int counterLength = RANDOMNESS_LENGTH - nodeidLength;
		final int counterMax = (int) Math.pow(2, counterLength);

		int nodeid = random.nextInt();
		long timestamp = TsidTime.getCurrentTimestamp();
		TimestampStrategy strategy = new FixedTimestampStretegy(timestamp);
		TimeIdCreator creator = TsidCreator.getTimeIdCreator(nodeid, nodeidLength).withTimestampStrategy(strategy);

		for (int i = 0; i < counterMax; i++) {
			creator.create();
		}

		try {
			creator.create();
			fail();
		} catch (TsidCreatorException e) {
			// success
		}
	}

	@Test
	public void testOverrunExceptionDefaultBitLength() {

		final int nodeidLength = DEFAULT_NODEID_LENGTH;
		final int counterLength = RANDOMNESS_LENGTH - nodeidLength;
		final int counterMax = (int) Math.pow(2, counterLength);

		int nodeid = random.nextInt();
		long timestamp = TsidTime.getCurrentTimestamp();
		TimestampStrategy strategy = new FixedTimestampStretegy(timestamp);
		TimeIdCreator creator = TsidCreator.getTimeIdCreator1024(nodeid).withTimestampStrategy(strategy);

		for (int i = 0; i < counterMax; i++) {
			creator.create();
		}

		try {
			creator.create();
			fail();
		} catch (TsidCreatorException e) {
			// success
		}
	}

	@Test
	public void testOverrunExceptionCustomBitLength() {

		final int nodeidLength = 16; // custom bit length
		final int counterLength = RANDOMNESS_LENGTH - nodeidLength;
		final int counterMax = (int) Math.pow(2, counterLength);

		int nodeid = random.nextInt();
		long timestamp = TsidTime.getCurrentTimestamp();
		TimestampStrategy strategy = new FixedTimestampStretegy(timestamp);
		TimeIdCreator creator = TsidCreator.getTimeIdCreator(nodeid, nodeidLength).withTimestampStrategy(strategy);

		for (int i = 0; i < counterMax; i++) {
			creator.create();
		}

		try {
			creator.create();
			fail();
		} catch (TsidCreatorException e) {
			// success
		}
	}

	private void checkNullOrInvalid(String[] list) {
		for (String tsid : list) {
			assertNotNull("TSID is null", tsid);
			assertTrue("TSID is empty", !tsid.isEmpty());
			assertTrue("TSID is blank", !tsid.replace(" ", "").isEmpty());
			assertEquals("TSID length is wrong " + tsid.length(), TSID_LENGTH, tsid.length());
			assertTrue("TSID is not valid", TsidValidator.isValid(tsid));
		}
	}

	private void checkUniqueness(long[] list) {

		HashSet<Long> set = new HashSet<>();

		for (Long tsid : list) {
			assertTrue(String.format("TSID is duplicated %s", tsid), set.add(tsid));
		}

		assertEquals("There are duplicated TSIDs", set.size(), list.length);
	}

	private void checkUniqueness(String[] list) {

		HashSet<String> set = new HashSet<>();

		for (String tsid : list) {
			assertTrue(String.format("TSID is duplicated %s", tsid), set.add(tsid));
		}

		assertEquals("There are duplicated TSIDs", set.size(), list.length);
	}

	private void checkCreationTime(long[] list, long startTime, long endTime) {

		assertTrue("Start time was after end time", startTime <= endTime);

		for (Long tsid : list) {
			long creationTime = TsidUtil.extractUnixMilliseconds(tsid);
			assertTrue("Creation time was before start time", creationTime >= startTime);
			assertTrue("Creation time was after end time", creationTime <= endTime);
		}
	}

	private void checkCreationTime(String[] list, long startTime, long endTime) {

		assertTrue("Start time was after end time", startTime <= endTime);

		for (String tsid : list) {
			long creationTime = TsidUtil.extractUnixMilliseconds(tsid);
			assertTrue("Creation time was before start time ", creationTime >= startTime);
			assertTrue("Creation time was after end time", creationTime <= endTime);
		}
	}

	@Test
	public void testGetTsidParallelGeneratorsShouldCreateUniqueTsids() throws InterruptedException {

		Thread[] threads = new Thread[THREAD_TOTAL];

		TestThread.clearHashSet();

		TimestampStrategy strategy = new FixedTimestampStretegy(System.currentTimeMillis());

		// Instantiate and start many threads
		for (int i = 0; i < THREAD_TOTAL; i++) {
			TimeIdCreator parallelCreator = TsidCreator.getTimeIdCreator1024(i).withTimestampStrategy(strategy);
			threads[i] = new TestThread(parallelCreator, DEFAULT_LOOP_MAX);
			threads[i].start();
		}

		// Wait all the threads to finish
		for (Thread thread : threads) {
			thread.join();
		}

		// Check if the quantity of unique UUIDs is correct
		assertEquals(DUPLICATE_UUID_MSG, (DEFAULT_LOOP_MAX * THREAD_TOTAL), TestThread.hashSet.size());
	}

	public static class TestThread extends Thread {

		private TimeIdCreator creator;
		private int loopLimit;

		protected static final Set<Long> hashSet = new HashSet<>();

		public TestThread(TimeIdCreator creator, int loopLimit) {
			this.creator = creator;
			this.loopLimit = loopLimit;
		}

		public static void clearHashSet() {
			synchronized (TestThread.hashSet) {
				TestThread.hashSet.clear();
			}
		}

		@Override
		public void run() {
			for (int i = 0; i < loopLimit; i++) {
				synchronized (hashSet) {
					hashSet.add(creator.create());
				}
			}
		}
	}
}
