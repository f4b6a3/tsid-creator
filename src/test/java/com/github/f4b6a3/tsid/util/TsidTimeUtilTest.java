package com.github.f4b6a3.tsid.util;

import static org.junit.Assert.*;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import org.junit.Test;

import com.github.f4b6a3.tsid.TsidCreator;

public class TsidTimeUtilTest {

	private Instant unixEpoch = Instant.parse("1970-01-01T00:00:00.000Z");
	private Instant custEpoch = Instant.parse("2020-01-01T00:00:00.000Z");

	private long unixMilli = unixEpoch.toEpochMilli();
	private long custMilli = custEpoch.toEpochMilli();

	@Test
	public void testUnixEpoch() {
		long timestamp = TsidTimeUtil.toTimestamp(unixMilli);
		long milliseconds = TsidTimeUtil.toUnixMilliseconds(timestamp);
		assertEquals(unixMilli, milliseconds);
	}

	@Test
	public void testCustomEpoch() {
		long timestamp = TsidTimeUtil.toTimestamp(custMilli);
		long milliseconds = TsidTimeUtil.toUnixMilliseconds(timestamp);
		assertEquals(custMilli, milliseconds);
	}

	@Test
	public void testCurrentTimestamp() {

		long start = System.currentTimeMillis();
		long tsid = TsidCreator.getTsid();
		long middle = TsidUtil.extractUnixMilliseconds(tsid);
		long end = System.currentTimeMillis();

		assertTrue(start <= middle);
		assertTrue(middle <= end);
	}

	@Test
	public void testToInstantFromInstant() {
		Clock test = Clock.tickSeconds(ZoneId.of("UTC"));
		Instant instant1 = Instant.now(test);
		long timestamp1 = TsidTimeUtil.toTimestamp(instant1);
		Instant instant2 = TsidTimeUtil.toInstant(timestamp1);
		assertEquals(instant1, instant2);
	}

	@Test
	public void testFromUnixMillisecondsToTimestamp() {
		long milliseconds = System.currentTimeMillis();
		long timestamp = TsidTimeUtil.toTimestamp(milliseconds);
		Instant instant = TsidTimeUtil.toInstant(timestamp);
		assertEquals(milliseconds, instant.toEpochMilli());
	}

	@Test
	public void testFromCurrentTimestampToUnixMilliseconds() {
		long timestamp = TsidTimeUtil.getCurrentTimestamp();
		long milliseconds = TsidTimeUtil.toUnixMilliseconds(timestamp);
		Instant instant = TsidTimeUtil.toInstant(timestamp);
		assertEquals(milliseconds, instant.toEpochMilli());
	}

	@Test
	public void testFromTimestampToUnixMilliseconds() {
		long milliseconds1 = System.currentTimeMillis();
		long timestamp = TsidTimeUtil.toTimestamp(milliseconds1);
		long milliseconds2 = TsidTimeUtil.toUnixMilliseconds(timestamp);
		assertEquals(milliseconds1, milliseconds2);
	}
}
