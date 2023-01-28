package com.github.f4b6a3.tsid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public abstract class TsidFactory00000Test {

	protected static final int TSID_LENGTH = 13;

	protected static final int LOOP_MAX = 10_000;

	protected static Random random = new Random();

	protected static final String DUPLICATE_UUID_MSG = "A duplicate TSID was created";

	protected static final int THREAD_TOTAL = availableProcessors();

	protected static class TestThread extends Thread {

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

	protected boolean checkNullOrInvalid(long[] list) {
		for (long tsid : list) {
			assertNotEquals("TSID is zero", tsid, 0);
		}
		return true; // success
	}

	protected boolean checkNullOrInvalid(String[] list) {
		for (String tsid : list) {
			assertNotNull("TSID is null", tsid);
			assertFalse("TSID is empty", tsid.isEmpty());
			assertFalse("TSID is blank", tsid.replace(" ", "").isEmpty());
			assertEquals("TSID length is wrong " + tsid.length(), TSID_LENGTH, tsid.length());
			assertTrue("TSID is not valid", Tsid.isValid(tsid));
		}
		return true; // success
	}

	protected boolean checkUniqueness(long[] list) {

		HashSet<Long> set = new HashSet<>();

		for (Long tsid : list) {
			assertTrue(String.format("TSID is duplicated %s", tsid), set.add(tsid));
		}

		assertEquals("There are duplicated TSIDs", set.size(), list.length);
		return true; // success
	}

	protected boolean checkUniqueness(String[] list) {

		HashSet<String> set = new HashSet<>();

		for (String tsid : list) {
			assertTrue(String.format("TSID is duplicated %s", tsid), set.add(tsid));
		}

		assertEquals("There are duplicated TSIDs", set.size(), list.length);
		return true; // success
	}

	protected boolean checkOrdering(long[] list) {
		long[] other = Arrays.copyOf(list, list.length);
		Arrays.sort(other);

		for (int i = 0; i < list.length; i++) {
			assertEquals("The TSID list is not ordered", list[i], other[i]);
		}
		return true; // success
	}

	protected boolean checkOrdering(String[] list) {
		String[] other = Arrays.copyOf(list, list.length);
		Arrays.sort(other);

		for (int i = 0; i < list.length; i++) {
			assertEquals("The TSID list is not ordered", list[i], other[i]);
		}
		return true; // success
	}

	protected boolean checkMaximumPerMs(long[] list, int max) {
		HashMap<Long, ArrayList<Long>> map = new HashMap<>();

		for (Long tsid : list) {
			Long key = Tsid.from(tsid).getTime();
			if (map.get(key) == null) {
				map.put(key, new ArrayList<>());
			}
			map.get(key).add(tsid);
			int size = map.get(key).size();
			assertTrue(String.format("Too many TSIDs per milliecond %s", size), size <= max);
		}

		return true; // success
	}

	protected boolean checkMaximumPerMs(String[] list, int max) {
		HashMap<Long, HashSet<String>> map = new HashMap<>();

		for (String tsid : list) {
			Long key = Tsid.from(tsid).getTime();
			if (map.get(key) == null) {
				map.put(key, new HashSet<>());
			}
			map.get(key).add(tsid);
			int size = map.get(key).size();
			assertTrue(String.format("Too many TSIDs per milliecond %s", size), size <= max);
		}

		return true; // success
	}

	protected boolean checkCreationTime(long[] list, long startTime, long endTime) {

		assertTrue("Start time was after end time", startTime <= endTime);

		for (Long tsid : list) {
			long creationTime = Tsid.from(tsid).getInstant().toEpochMilli();
			assertTrue("Creation time was before start time", creationTime >= startTime);
			assertTrue("Creation time was after end time", creationTime <= endTime + LOOP_MAX);
		}
		return true; // success
	}

	protected boolean checkCreationTime(String[] list, long startTime, long endTime) {

		assertTrue("Start time was after end time", startTime <= endTime);

		for (String tsid : list) {
			long creationTime = Tsid.from(tsid).getInstant().toEpochMilli();
			assertTrue("Creation time was before start time ", creationTime >= startTime);
			assertTrue("Creation time was after end time", creationTime <= endTime + LOOP_MAX);
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
