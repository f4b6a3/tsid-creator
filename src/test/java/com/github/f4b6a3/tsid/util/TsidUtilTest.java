package com.github.f4b6a3.tsid.util;

import static org.junit.Assert.*;

import java.time.Instant;

import org.junit.Test;

import com.github.f4b6a3.tsid.TsidCreator;
import com.github.f4b6a3.tsid.creator.TimeSortableIdCreator;
import com.github.f4b6a3.tsid.strategy.TimestampStrategy;
import com.github.f4b6a3.tsid.strategy.timestamp.FixedTimestampStretegy;

public class TsidUtilTest {

	@Test
	public void testExtractUnixMilliseconds() {

		long start = System.currentTimeMillis();
		long tsid = TsidCreator.getTsid();
		long middle = TsidUtil.extractUnixMilliseconds(tsid);
		long end = System.currentTimeMillis();

		assertTrue(start <= middle);
		assertTrue(middle <= end);
	}

	@Test
	public void testExtractUnixMillisecondsWithCustomEpoch() {

		Instant customEpoch = Instant.parse("1984-01-01T00:00:00Z");

		long start = System.currentTimeMillis();
		long tsid = TsidCreator.getTimeSortableIdCreator().withCustomEpoch(customEpoch.toEpochMilli()).create();
		long middle = TsidUtil.extractUnixMilliseconds(tsid, customEpoch.toEpochMilli());
		long end = System.currentTimeMillis();

		assertTrue(start <= middle);
		assertTrue(middle <= end);
	}

	@Test
	public void testExtractInstant() {

		Instant start = Instant.now();
		long tsid = TsidCreator.getTsid();
		Instant middle = TsidUtil.extractInstant(tsid);
		Instant end = Instant.now();

		assertTrue(start.toEpochMilli() <= middle.toEpochMilli());
		assertTrue(middle.toEpochMilli() <= end.toEpochMilli());
	}

	@Test
	public void testExtractInstantWithCustomEpoch() {

		Instant customEpoch = Instant.parse("2015-10-23T00:00:00Z");

		Instant start = Instant.now();
		long tsid = TsidCreator.getTimeSortableIdCreator().withCustomEpoch(customEpoch).create();
		Instant middle = TsidUtil.extractInstant(tsid, customEpoch);
		Instant end = Instant.now();

		assertTrue(start.toEpochMilli() <= middle.toEpochMilli());
		assertTrue(middle.toEpochMilli() <= end.toEpochMilli());
	}

	@Test
	public void testExtractNodeIdentifierImplicitBitLength() {

		final int nodeidLength = 10; // implicit bit length
		final int nodeidMax = (int) Math.pow(2, nodeidLength);

		long timestamp = TsidTimeUtil.getCurrentTimestamp();
		TimestampStrategy strategy = new FixedTimestampStretegy(timestamp);

		// The implicit node identifier bit length is 10 (2^10 = 1024)
		for (int nodeid = 0; nodeid < nodeidMax; nodeid++) {
			TimeSortableIdCreator creator = TsidCreator.getTimeSortableIdCreator().withTimestampStrategy(strategy)
					.withNodeIdentifier(nodeid);
			long tsid = creator.create();
			int result = TsidUtil.extractNodeIdentifier(tsid);
			assertEquals(nodeid, result);
		}
	}

	@Test
	public void testExtractNodeIdentifierCustomtBitLength() {

		final int nodeidLength = 12;
		final int nodeidMax = (int) Math.pow(2, nodeidLength);

		long timestamp = TsidTimeUtil.getCurrentTimestamp();
		TimestampStrategy strategy = new FixedTimestampStretegy(timestamp);

		for (int nodeid = 0; nodeid < nodeidMax; nodeid++) {
			TimeSortableIdCreator creator = TsidCreator.getTimeSortableIdCreator().withTimestampStrategy(strategy)
					.withNodeIdentifier(nodeid, nodeidLength);
			long tsid = creator.create();

			int result = TsidUtil.extractNodeIdentifier(tsid, nodeidLength);
			assertEquals(nodeid, result);
		}
	}

}
