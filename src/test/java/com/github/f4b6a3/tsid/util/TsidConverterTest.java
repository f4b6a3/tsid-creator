package com.github.f4b6a3.tsid.util;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.f4b6a3.tsid.TsidCreator;

public class TsidConverterTest {

	@Test
	public void testToStringIsValid() {
		long tsid = TsidCreator.getTsid();
		String string = TsidConverter.toString(tsid);
		assertTrue("TSID string is invalid", TsidValidator.isValid(string));
	}

	@Test
	public void testToString() {
		long tsid1 = 0x7fffffffffffffffL; // 2^31
		String string1 = "7ZZZZZZZZZZZZ";
		String result1 = TsidConverter.toString(tsid1);
		assertEquals(string1, result1);

		long tsid2 = 0x000000000000000aL; // 10
		String string2 = "000000000000A";
		String result2 = TsidConverter.toString(tsid2);
		assertEquals(string2, result2);
	}

	@Test
	public void testFromString() {

		long tsid1 = 0x7fffffffffffffffL; // 2^31
		String string1 = "7ZZZZZZZZZZZZ";
		long result1 = TsidConverter.fromString(string1);
		assertEquals(tsid1, result1);

		long tsid2 = 0x000000000000000aL; // 10
		String string2 = "000000000000A";
		long result2 = TsidConverter.fromString(string2);
		assertEquals(tsid2, result2);

	}
}
