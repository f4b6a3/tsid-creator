package com.github.f4b6a3.tsid.creator;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import org.junit.Test;

import com.github.f4b6a3.tsid.TsidCreator;
import com.github.f4b6a3.tsid.exception.TsidCreatorException;
import com.github.f4b6a3.tsid.strategy.TimestampStrategy;
import com.github.f4b6a3.tsid.strategy.timestamp.FixedTimestampStretegy;
import com.github.f4b6a3.tsid.util.TsidTimeUtil;
import com.github.f4b6a3.tsid.util.TsidUtil;
import com.github.f4b6a3.tsid.util.TsidValidator;

public class TimeIdCreatorTest {

	private static final int TSID_LENGTH = 13;

	public static final int TIMESTAMP_LENGTH = 42;
	public static final int RANDOMNESS_LENGTH = 22;
	public static final int DEFAULT_NODEID_LENGTH = 10;

	private static final int DEFAULT_LOOP_MAX = 100_000;

	private static Random random = new Random();

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

	@Test
	public void testGetTsidWithNode() {

		final int nodeidLength = DEFAULT_NODEID_LENGTH;
		final int counterLength = RANDOMNESS_LENGTH - nodeidLength;
		final int counterMax = (int) Math.pow(2, counterLength);

		long[] list = new long[counterMax];

		int nodeid = random.nextInt();

		long startTime = System.currentTimeMillis();

		for (int i = 0; i < counterMax; i++) {
			list[i] = TsidCreator.getTsid(nodeid);
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
			list[i] = TsidCreator.getTsidString();
		}

		long endTime = System.currentTimeMillis();

		checkNullOrInvalid(list);
		checkUniqueness(list);
		checkOrdering(list);
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

		for (int i = 0; i < counterMax; i++) {
			list[i] = TsidCreator.getTsidString(nodeid);
		}

		long endTime = System.currentTimeMillis();

		checkNullOrInvalid(list);
		checkUniqueness(list);
		checkCreationTime(list, startTime, endTime);
	}

	private void checkNullOrInvalid(long[] list) {
		for (long tsid : list) {
			assertTrue("TSID is zero", tsid != 0);
		}
	}

	@Test
	public void testOverrunException() {

		final int nodeidLength = 0; // default bit length (node id disabled)
		final int counterLength = RANDOMNESS_LENGTH - nodeidLength;
		final int counterMax = (int) Math.pow(2, counterLength);

		long timestamp = TsidTimeUtil.getCurrentTimestamp();
		TimestampStrategy strategy = new FixedTimestampStretegy(timestamp);
		TimeIdCreator creator = TsidCreator.getTimeIdCreator().withTimestampStrategy(strategy);

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
		long timestamp = TsidTimeUtil.getCurrentTimestamp();
		TimestampStrategy strategy = new FixedTimestampStretegy(timestamp);
		TimeIdCreator creator = TsidCreator.getTimeIdCreator(nodeid).withTimestampStrategy(strategy);

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
		long timestamp = TsidTimeUtil.getCurrentTimestamp();
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
			assertTrue("TSID is null", tsid != null);
			assertTrue("TSID is empty", !tsid.isEmpty());
			assertTrue("TSID is blank", !tsid.replace(" ", "").isEmpty());
			assertTrue("TSID length is wrong " + tsid.length(), tsid.length() == TSID_LENGTH);
			assertTrue("TSID is not valid", TsidValidator.isValid(tsid));
		}
	}

	private void checkUniqueness(long[] list) {

		HashSet<Long> set = new HashSet<>();

		for (Long tsid : list) {
			assertTrue(String.format("TSID is duplicated %s", tsid), set.add(tsid));
		}

		assertTrue("There are duplicated TSIDs", set.size() == list.length);
	}

	private void checkUniqueness(String[] list) {

		HashSet<String> set = new HashSet<>();

		for (String tsid : list) {
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

	private void checkCreationTime(String[] list, long startTime, long endTime) {

		assertTrue("Start time was after end time", startTime <= endTime);

		for (String tsid : list) {
			long creationTime = TsidUtil.extractUnixMilliseconds(tsid);
			assertTrue("Creation time was before start time ", creationTime >= startTime);
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

	private void checkOrdering(String[] list) {
		String[] other = Arrays.copyOf(list, list.length);
		Arrays.sort(other);

		for (int i = 0; i < list.length; i++) {
			assertTrue("The TSID list is not ordered", list[i].equals(other[i]));
		}
	}
}
