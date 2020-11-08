package com.github.f4b6a3.tsid.util;

import static org.junit.Assert.assertTrue;

import java.util.Random;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.f4b6a3.tsid.TsidCreator;

public class TsidConverterTest {

	private static final int DEFAULT_LOOP_MAX = 100_000;
	
	protected static final char[] ALPHABET_CROCKFORD = "0123456789ABCDEFGHJKMNPQRSTVWXYZ".toCharArray();
	protected static final char[] ALPHABET_JAVA = "0123456789abcdefghijklmnopqrstuv".toCharArray(); // Long.parseUnsignedLong()
	
	@Test
	public void testToStringIsValid() {
		long tsid = TsidCreator.getTsid1024();
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
	
	@Test
	public void testFromString2() {
		for (int i = 0; i < DEFAULT_LOOP_MAX; i++) {
			Random random = new Random();
			final long number0 = random.nextLong();
			final String string0 = toString(number0);
			
			final long number1 = TsidConverter.fromString(string0);

			assertEquals(number0, number1);
		}
	}
	
	@Test
	public void testToString2() {
		for (int i = 0; i < DEFAULT_LOOP_MAX; i++) {
			Random random = new Random();
			final long number0 = random.nextLong();
			final String string0 = toString(number0);

			final String string1 = TsidConverter.toString(number0);

			assertEquals(string0, string1);
		}
	}
	
	public long fromString(String tsid) {

		String number = tsid.substring(0, 10);
		number = transliterate(number, ALPHABET_CROCKFORD, ALPHABET_JAVA);

		return Long.parseUnsignedLong(number, 32);
	}

	public String toString(long tsid) {

		final String zero = "0000000000000";

		String number = Long.toUnsignedString(tsid, 32);
		number = zero.substring(0, zero.length() - number.length()) + number;

		return transliterate(number, ALPHABET_JAVA, ALPHABET_CROCKFORD);
	}

	private static String transliterate(String string, char[] alphabet1, char[] alphabet2) {
		char[] output = string.toCharArray();
		for (int i = 0; i < output.length; i++) {
			for (int j = 0; j < alphabet1.length; j++) {
				if (output[i] == alphabet1[j]) {
					output[i] = alphabet2[j];
					break;
				}
			}
		}
		return new String(output);
	}
}
