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

	@Test
	public void testIsValidLoose() {

		String tsid = null; // Null
		assertFalse("Null tsid should be invalid.", TsidUtil.isValid(tsid));

		tsid = ""; // length: 0
		assertFalse("tsid with empty string should be invalid.", TsidUtil.isValid(tsid));

		tsid = "0123456789ABC"; // All upper case
		assertTrue("tsid in upper case should valid.", TsidUtil.isValid(tsid));

		tsid = "0123456789abc"; // All lower case
		assertTrue("tsid in lower case should be valid.", TsidUtil.isValid(tsid));

		tsid = "0123456789AbC"; // Mixed case
		assertTrue("tsid in upper and lower case should valid.", TsidUtil.isValid(tsid));

		tsid = "0123456789AB"; // length: 12
		assertFalse("tsid length lower than 13 should be invalid.", TsidUtil.isValid(tsid));

		tsid = "0123456789ABCC"; // length: 14
		assertFalse("tsid length greater than 13 should be invalid.", TsidUtil.isValid(tsid));

		tsid = "u123456789ABC"; // Letter u
		assertFalse("tsid with 'u' or 'U' should be invalid.", TsidUtil.isValid(tsid));

		tsid = "#123456789ABC"; // Special char
		assertFalse("tsid with special chars should be invalid.", TsidUtil.isValid(tsid));

		tsid = "01234-56789-ABC"; // Hiphens
		assertTrue("tsid with hiphens should be valid.", TsidUtil.isValid(tsid));
	}

	@Test
	public void testIsValidStrict() {
		boolean strict = true;

		String tsid = null; // Null
		assertFalse("Null tsid should be invalid in strict mode.", TsidUtil.isValid(tsid, strict));

		tsid = ""; // length: 0
		assertFalse("tsid with empty string should be invalid  in strict mode.", TsidUtil.isValid(tsid, strict));

		tsid = "0123456789ABC"; // All upper case
		assertTrue("tsid in upper case should valid in strict mode.", TsidUtil.isValid(tsid, strict));

		tsid = "0123456789abc"; // All lower case
		assertTrue("tsid in lower case should be valid in strict mode.", TsidUtil.isValid(tsid, strict));

		tsid = "0123456789AbC"; // Mixed case
		assertTrue("tsid in upper and lower case should valid in strict mode.", TsidUtil.isValid(tsid, strict));

		tsid = "0123456789AB"; // length: 12
		assertFalse("tsid length lower than 13 should be invalid in strict mode.", TsidUtil.isValid(tsid, strict));

		tsid = "0123456789ABCC"; // length: 14
		assertFalse("tsid length greater than 13 should be invalid in strict mode.", TsidUtil.isValid(tsid, strict));

		tsid = "i123456789ABC"; // Letter i
		assertFalse("tsid with 'i' or 'I' should be invalid in strict mode.", TsidUtil.isValid(tsid, strict));

		tsid = "L123456789ABC"; // letter L
		assertFalse("tsid with 'l' or 'L' should be invalid in strict mode.", TsidUtil.isValid(tsid, strict));

		tsid = "o123456789ABC"; // letter o
		assertFalse("tsid with 'o' or 'O' should be invalid in strict mode.", TsidUtil.isValid(tsid, strict));

		tsid = "u123456789ABC"; // letter u
		assertFalse("tsid with 'u' or 'U' should be invalid in strict mode.", TsidUtil.isValid(tsid, strict));

		tsid = "#123456789ABC"; // Special char
		assertFalse("tsid with special chars should be invalid in strict mode.", TsidUtil.isValid(tsid, strict));

		tsid = "01234-56789-ABC"; // Hyphens
		assertFalse("tsid with hiphens should be invalid in strict mode.", TsidUtil.isValid(tsid, strict));
	}
}
