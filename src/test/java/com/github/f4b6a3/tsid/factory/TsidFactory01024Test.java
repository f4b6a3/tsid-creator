package com.github.f4b6a3.tsid.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.junit.Test;

import com.github.f4b6a3.tsid.Tsid;
import com.github.f4b6a3.tsid.TsidCreator;
import com.github.f4b6a3.tsid.factory.TsidFactory;

public class TsidFactory01024Test {

	private static final int TSID_LENGTH = 13;

	private static final int COUNTER_LENGTH = 12;
	private static final int COUNTER_MAX = (int) Math.pow(2, COUNTER_LENGTH);

	private static Random random = new Random();

	protected static final String DUPLICATE_UUID_MSG = "A duplicate TSID was created";

	protected static final int THREAD_TOTAL = availableProcessors();

	@Test
	public void testGetTsid1024() {

		long startTime = System.currentTimeMillis();

		long[] list = new long[COUNTER_MAX];
		for (int i = 0; i < COUNTER_MAX; i++) {
			list[i] = TsidCreator.getTsid1024().toLong();
		}

		long endTime = System.currentTimeMillis();

		checkNullOrInvalid(list);
		checkUniqueness(list);
		checkCreationTime(list, startTime, endTime);
	}

	@Test
	public void testGetTsid1024WithNode() {

		long startTime = System.currentTimeMillis();

		int node = random.nextInt();
		TsidFactory creator = TsidCreator.getTsidFactory1024(node);

		long[] list = new long[COUNTER_MAX];
		for (int i = 0; i < COUNTER_MAX; i++) {
			list[i] = creator.create().toLong();
		}

		long endTime = System.currentTimeMillis();

		checkNullOrInvalid(list);
		checkUniqueness(list);
		checkCreationTime(list, startTime, endTime);
	}

	@Test
	public void testGetTsidString1024() {

		long startTime = System.currentTimeMillis();

		String[] list = new String[COUNTER_MAX];
		for (int i = 0; i < COUNTER_MAX; i++) {
			list[i] = TsidCreator.getTsid1024().toString();
		}

		long endTime = System.currentTimeMillis();

		checkNullOrInvalid(list);
		checkUniqueness(list);
		checkCreationTime(list, startTime, endTime);
	}

	@Test
	public void testGetTsidString1024WithNode() {

		long startTime = System.currentTimeMillis();

		int node = random.nextInt();
		TsidFactory creator = TsidCreator.getTsidFactory1024(node);

		String[] list = new String[COUNTER_MAX];
		for (int i = 0; i < COUNTER_MAX; i++) {
			list[i] = creator.create().toString();
		}

		long endTime = System.currentTimeMillis();

		checkNullOrInvalid(list);
		checkUniqueness(list);
		checkCreationTime(list, startTime, endTime);
	}

	@Test
	public void testGetTsid1024Parallel() throws InterruptedException {

		TestThread.clearHashSet();
		Thread[] threads = new Thread[THREAD_TOTAL];

		// Instantiate and start many threads
		for (int i = 0; i < THREAD_TOTAL; i++) {
			TsidFactory parallelCreator = TsidCreator.getTsidFactory1024(i);
			threads[i] = new TestThread(parallelCreator, COUNTER_MAX);
			threads[i].start();
		}

		// Wait all the threads to finish
		for (Thread thread : threads) {
			thread.join();
		}

		// Check if the quantity of unique UUIDs is correct
		assertEquals(DUPLICATE_UUID_MSG, (COUNTER_MAX * THREAD_TOTAL), TestThread.hashSet.size());
	}

	public static class TestThread extends Thread {

		private TsidFactory creator;
		private int loopLimit;

		protected static final Set<Long> hashSet = new HashSet<>();

		public TestThread(TsidFactory creator, int loopLimit) {
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
					hashSet.add(creator.create().toLong());
				}
			}
		}
	}

	private void checkNullOrInvalid(long[] list) {
		for (long tsid : list) {
			assertNotEquals("TSID is zero", tsid, 0);
		}
	}

	private void checkNullOrInvalid(String[] list) {
		for (String tsid : list) {
			assertNotNull("TSID is null", tsid);
			assertTrue("TSID is empty", !tsid.isEmpty());
			assertTrue("TSID is blank", !tsid.replace(" ", "").isEmpty());
			assertEquals("TSID length is wrong " + tsid.length(), TSID_LENGTH, tsid.length());
			assertTrue("TSID is not valid", Tsid.isValid(tsid));
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
			long creationTime = Tsid.from(tsid).getInstant().toEpochMilli();
			assertTrue("Creation time was before start time", creationTime >= startTime);
			assertTrue("Creation time was after end time", creationTime <= endTime);
		}
	}

	private void checkCreationTime(String[] list, long startTime, long endTime) {

		assertTrue("Start time was after end time", startTime <= endTime);

		for (String tsid : list) {
			long creationTime = Tsid.from(tsid).getInstant().toEpochMilli();
			assertTrue("Creation time was before start time ", creationTime >= startTime);
			assertTrue("Creation time was after end time", creationTime <= endTime);
		}
	}

	private static int availableProcessors() {
		int processors = Runtime.getRuntime().availableProcessors();
		if (processors < 4) {
			processors = 4;
		}
		return processors;
	}
}
