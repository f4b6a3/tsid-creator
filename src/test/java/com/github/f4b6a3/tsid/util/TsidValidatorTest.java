package com.github.f4b6a3.tsid.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class TsidValidatorTest {

	@Test
	public void testIsValid() {

		String tsid = null; // Null
		assertFalse("Null tsid should be invalid.", TsidValidator.isValid(tsid));

		tsid = ""; // length: 0
		assertFalse("tsid with empty string should be invalid.", TsidValidator.isValid(tsid));

		tsid = "0123456789ABC"; // All upper case
		assertTrue("tsid in upper case should valid.", TsidValidator.isValid(tsid));

		tsid = "0123456789abc"; // All lower case
		assertTrue("tsid in lower case should be valid.", TsidValidator.isValid(tsid));

		tsid = "0123456789AbC"; // Mixed case
		assertTrue("tsid in upper and lower case should valid.", TsidValidator.isValid(tsid));

		tsid = "0123456789AB"; // length: 12
		assertFalse("tsid length lower than 13 should be invalid.", TsidValidator.isValid(tsid));

		tsid = "0123456789ABCC"; // length: 14
		assertFalse("tsid length greater than 13 should be invalid.", TsidValidator.isValid(tsid));

		tsid = "u123456789ABC"; // Letter u
		assertFalse("tsid with 'u' or 'U' should be invalid.", TsidValidator.isValid(tsid));

		tsid = "#123456789ABC"; // Special char
		assertFalse("tsid with special chars should be invalid.", TsidValidator.isValid(tsid));

		tsid = "01234-56789-ABC"; // Hiphens
		assertTrue("tsid with hiphens should be valid.", TsidValidator.isValid(tsid));
	}
}
