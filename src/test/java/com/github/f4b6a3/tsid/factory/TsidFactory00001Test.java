package com.github.f4b6a3.tsid.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.junit.Test;

import com.github.f4b6a3.tsid.Tsid;
import com.github.f4b6a3.tsid.TsidFactory;

public class TsidFactory00001Test {

	private static final int TSID_LENGTH = 13;

	private static final int NODE_BITS = 0;
	private static final int COUNTER_BITS = 22;
	private static final int COUNTER_MAX = (int) Math.pow(2, COUNTER_BITS);

	private static final int LOOP_MAX = 1_000;

	private static Random random = new Random();

	protected static final String DUPLICATE_UUID_MSG = "A duplicate TSID was created";

	protected static final int THREAD_TOTAL = availableProcessors();

	@Test
	public void testGetTsid1() {

		long startTime = System.currentTimeMillis();

		TsidFactory factory = TsidFactory.builder().withNodeBits(NODE_BITS).withRandom(random).build();

		long[] list = new long[LOOP_MAX];
		for (int i = 0; i < LOOP_MAX; i++) {
			list[i] = factory.create().toLong();
		}

		long endTime = System.currentTimeMillis();

		assertTrue(checkNullOrInvalid(list));
		assertTrue(checkUniqueness(list));
		assertTrue(checkCreationTime(list, startTime, endTime));
	}

	@Test
	public void testGetTsid1WithNode() {

		long startTime = System.currentTimeMillis();

		int node = random.nextInt();
		TsidFactory factory = TsidFactory.builder().withNode(node).withNodeBits(NODE_BITS).withRandom(random).build();

		long[] list = new long[LOOP_MAX];
		for (int i = 0; i < LOOP_MAX; i++) {
			list[i] = factory.create().toLong();
		}

		long endTime = System.currentTimeMillis();

		assertTrue(checkNullOrInvalid(list));
		assertTrue(checkUniqueness(list));
		assertTrue(checkCreationTime(list, startTime, endTime));
	}

	@Test
	public void testGetTsidString1() {

		long startTime = System.currentTimeMillis();

		TsidFactory factory = TsidFactory.builder().withNodeBits(NODE_BITS).withRandom(random).build();

		String[] list = new String[LOOP_MAX];
		for (int i = 0; i < LOOP_MAX; i++) {
			list[i] = factory.create().toString();
		}

		long endTime = System.currentTimeMillis();

		assertTrue(checkNullOrInvalid(list));
		assertTrue(checkUniqueness(list));
		assertTrue(checkCreationTime(list, startTime, endTime));
	}

	@Test
	public void testGetTsidString1WithNode() {

		long startTime = System.currentTimeMillis();

		int node = random.nextInt();
		TsidFactory factory = TsidFactory.builder().withNode(node).withNodeBits(NODE_BITS).withRandom(random).build();

		String[] list = new String[LOOP_MAX];
		for (int i = 0; i < LOOP_MAX; i++) {
			list[i] = factory.create().toString();
		}

		long endTime = System.currentTimeMillis();

		assertTrue(checkNullOrInvalid(list));
		assertTrue(checkUniqueness(list));
		assertTrue(checkCreationTime(list, startTime, endTime));
	}

	@Test
	public void testGetTsid1Parallel() throws InterruptedException {

		TestThread.clearHashSet();
		Thread[] threads = new Thread[THREAD_TOTAL];
		int counterMax = COUNTER_MAX / THREAD_TOTAL;

		TsidFactory factory = TsidFactory.builder().withNodeBits(NODE_BITS).withRandom(random).build();

		// Instantiate and start many threads
		for (int i = 0; i < THREAD_TOTAL; i++) {
			threads[i] = new TestThread(factory, counterMax);
			threads[i].start();
		}

		// Wait all the threads to finish
		for (Thread thread : threads) {
			thread.join();
		}

		// Check if the quantity of unique UUIDs is correct
		assertEquals(DUPLICATE_UUID_MSG, (counterMax * THREAD_TOTAL), TestThread.hashSet.size());
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

	private boolean checkNullOrInvalid(long[] list) {
		for (long tsid : list) {
			assertNotEquals("TSID is zero", tsid, 0);
		}
		return true; // success
	}

	private boolean checkNullOrInvalid(String[] list) {
		for (String tsid : list) {
			assertNotNull("TSID is null", tsid);
			assertFalse("TSID is empty", tsid.isEmpty());
			assertFalse("TSID is blank", tsid.replace(" ", "").isEmpty());
			assertEquals("TSID length is wrong " + tsid.length(), TSID_LENGTH, tsid.length());
			assertTrue("TSID is not valid", Tsid.isValid(tsid));
		}
		return true; // success
	}

	private boolean checkUniqueness(long[] list) {

		HashSet<Long> set = new HashSet<>();

		for (Long tsid : list) {
			assertTrue(String.format("TSID is duplicated %s", tsid), set.add(tsid));
		}

		assertEquals("There are duplicated TSIDs", set.size(), list.length);
		return true; // success
	}

	private boolean checkUniqueness(String[] list) {

		HashSet<String> set = new HashSet<>();

		for (String tsid : list) {
			assertTrue(String.format("TSID is duplicated %s", tsid), set.add(tsid));
		}

		assertEquals("There are duplicated TSIDs", set.size(), list.length);
		return true; // success
	}

	private boolean checkCreationTime(long[] list, long startTime, long endTime) {

		assertTrue("Start time was after end time", startTime <= endTime);

		for (Long tsid : list) {
			long creationTime = Tsid.from(tsid).getInstant().toEpochMilli();
			assertTrue("Creation time was before start time", creationTime >= startTime);
			assertTrue("Creation time was after end time", creationTime <= endTime);
		}
		return true; // success
	}

	private boolean checkCreationTime(String[] list, long startTime, long endTime) {

		assertTrue("Start time was after end time", startTime <= endTime);

		for (String tsid : list) {
			long creationTime = Tsid.from(tsid).getInstant().toEpochMilli();
			assertTrue("Creation time was before start time ", creationTime >= startTime);
			assertTrue("Creation time was after end time", creationTime <= endTime);
		}
		return true; // success
	}

	private static int availableProcessors() {
		int processors = Runtime.getRuntime().availableProcessors();
		if (processors < 4) {
			processors = 4;
		}
		return processors;
	}
}
