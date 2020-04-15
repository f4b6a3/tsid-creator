package com.github.f4b6a3.tsid.util;

import static org.junit.Assert.*;

import java.time.Instant;

import org.junit.Test;

import com.github.f4b6a3.tsid.TsidCreator;

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
	public void testExtractInstant() {

		Instant start = Instant.now();
		long tsid = TsidCreator.getTsid();
		Instant middle = TsidUtil.extractInstant(tsid);
		Instant end = Instant.now();

		assertTrue(start.toEpochMilli() <= middle.toEpochMilli());
		assertTrue(middle.toEpochMilli() <= end.toEpochMilli());
	}
}
