package com.github.f4b6a3.tsid.util;

import static org.junit.Assert.*;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import org.junit.Test;

import com.github.f4b6a3.tsid.TsidCreator;

public class TsidTimeTest {

	private Instant unixEpoch = Instant.parse("1970-01-01T00:00:00.000Z");
	private Instant custEpoch = Instant.parse("2020-01-01T00:00:00.000Z");

	private long unixMilli = unixEpoch.toEpochMilli();
	private long custMilli = custEpoch.toEpochMilli();

	@Test
	public void testUnixEpoch() {
		long timestamp = TsidTime.toTimestamp(unixMilli);
		long milliseconds = TsidTime.toUnixMilliseconds(timestamp);
		assertEquals(unixMilli, milliseconds);
	}

	@Test
	public void testCustomEpoch() {
		long timestamp = TsidTime.toTimestamp(custMilli);
		long milliseconds = TsidTime.toUnixMilliseconds(timestamp);
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
		long timestamp1 = TsidTime.toTimestamp(instant1);
		Instant instant2 = TsidTime.toInstant(timestamp1);
		assertEquals(instant1, instant2);
	}

	@Test
	public void testFromUnixMillisecondsToTimestamp() {
		long milliseconds = System.currentTimeMillis();
		long timestamp = TsidTime.toTimestamp(milliseconds);
		Instant instant = TsidTime.toInstant(timestamp);
		assertEquals(milliseconds, instant.toEpochMilli());
	}

	@Test
	public void testFromCurrentTimestampToUnixMilliseconds() {
		long timestamp = TsidTime.getCurrentTimestamp();
		long milliseconds = TsidTime.toUnixMilliseconds(timestamp);
		Instant instant = TsidTime.toInstant(timestamp);
		assertEquals(milliseconds, instant.toEpochMilli());
	}

	@Test
	public void testFromTimestampToUnixMilliseconds() {
		long milliseconds1 = System.currentTimeMillis();
		long timestamp = TsidTime.toTimestamp(milliseconds1);
		long milliseconds2 = TsidTime.toUnixMilliseconds(timestamp);
		assertEquals(milliseconds1, milliseconds2);
	}
}
