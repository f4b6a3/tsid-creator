package com.github.f4b6a3.tsid;

import org.junit.Test;

import com.github.f4b6a3.tsid.util.TsidUtil;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;

public class TsidCreatorTest {

	private static final int DEFAULT_LOOP_MAX = 100_000;

	@Test
	public void testGetTsid() {
		long[] list = new long[DEFAULT_LOOP_MAX];

		long startTime = System.currentTimeMillis();

		for (int i = 0; i < DEFAULT_LOOP_MAX; i++) {
			list[i] = TsidCreator.getTsid();
		}

		long endTime = System.currentTimeMillis();

		checkNullOrInvalid(list);
		checkUniqueness(list);
		checkOrdering(list);
		checkCreationTime(list, startTime, endTime);
	}

	private void checkNullOrInvalid(long[] list) {
		for (long tsid : list) {
			assertTrue("TSID is zero", tsid != 0);
		}
	}

	private void checkUniqueness(long[] list) {

		HashSet<Long> set = new HashSet<>();

		for (Long tsid : list) {
			assertTrue(String.format("TSID is duplicated %s", tsid), set.add(tsid));
		}

		assertTrue("There are duplicated TSIDs", set.size() == list.length);
	}

	private void checkCreationTime(long[] list, long startTime, long endTime) {

		assertTrue("Start time was after end time", startTime <= endTime);

		for (Long tsid : list) {
			long creationTime = TsidUtil.extractUnixMilliseconds(tsid);
			assertTrue("Creation time was before start time", creationTime >= startTime);
			assertTrue("Creation time was after end time", creationTime <= endTime);
		}
	}

	private void checkOrdering(long[] list) {
		long[] other = Arrays.copyOf(list, list.length);
		Arrays.sort(other);

		for (int i = 0; i < list.length; i++) {
			assertTrue("The TSID list is not ordered", list[i] == other[i]);
		}
	}

}
