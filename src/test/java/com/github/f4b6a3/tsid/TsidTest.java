package com.github.f4b6a3.tsid;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Test;

import com.github.f4b6a3.tsid.Tsid;

public class TsidTest {

	private static final int TIME_BITS = 42;
	private static final int RANDOM_BITS = 22;
	private static final int LOOP_MAX = 1_000;

	private static final char[] ALPHABET_CROCKFORD = "0123456789ABCDEFGHJKMNPQRSTVWXYZ".toCharArray();
	private static final char[] ALPHABET_JAVA = "0123456789abcdefghijklmnopqrstuv".toCharArray(); // Long.parseUnsignedLong()

	@Test
	public void testFromBytes() {
		for (int i = 0; i < LOOP_MAX; i++) {

			final long number0 = ThreadLocalRandom.current().nextLong();
			final ByteBuffer buffer = ByteBuffer.allocate(8);
			buffer.putLong(number0);
			byte[] bytes = buffer.array();

			final long number1 = Tsid.from(bytes).toLong();
			assertEquals(number0, number1);
		}
	}

	@Test
	public void testToBytes() {
		for (int i = 0; i < LOOP_MAX; i++) {

			final long number = ThreadLocalRandom.current().nextLong();
			final ByteBuffer buffer = ByteBuffer.allocate(8);
			buffer.putLong(number);
			byte[] bytes0 = buffer.array();

			final String string0 = toString(number);
			byte[] bytes1 = Tsid.from(string0).toBytes();
			assertEquals(Arrays.toString(bytes0), Arrays.toString(bytes1));
		}
	}

	@Test
	public void testFromString() {
		for (int i = 0; i < LOOP_MAX; i++) {
			final long number0 = ThreadLocalRandom.current().nextLong();
			final String string0 = toString(number0);
			final long number1 = Tsid.from(string0).toLong();
			assertEquals(number0, number1);
		}
	}

	@Test
	public void testToString() {
		for (int i = 0; i < LOOP_MAX; i++) {
			final long number = ThreadLocalRandom.current().nextLong();
			final String string0 = toString(number);
			final String string1 = Tsid.from(number).toString();
			assertEquals(string0, string1);
		}
	}

	@Test
	public void testToLowerCase() {
		for (int i = 0; i < LOOP_MAX; i++) {
			final long number = ThreadLocalRandom.current().nextLong();
			final String string0 = toString(number).toLowerCase();
			final String string1 = Tsid.from(number).toLowerCase();
			assertEquals(string0, string1);
		}
	}

	@Test
	public void testFromString2() {

		long tsid1 = 0xffffffffffffffffL;
		String string1 = "FZZZZZZZZZZZZ";
		long result1 = Tsid.from(string1).toLong();
		assertEquals(tsid1, result1);

		long tsid2 = 0x000000000000000aL; // 10
		String string2 = "000000000000A";
		long result2 = Tsid.from(string2).toLong();
		assertEquals(tsid2, result2);

		try {
			// Test the first extra bit added by the base32 encoding
			String string3 = "G000000000000";
			Tsid.from(string3);
			fail("Should throw an InvalidTsidException");
		} catch (IllegalArgumentException e) {
			// success
		}
	}

	@Test
	public void testToString2() {
		long tsid1 = 0xffffffffffffffffL;
		String string1 = "FZZZZZZZZZZZZ";
		String result1 = Tsid.from(tsid1).toString();
		assertEquals(string1, result1);

		long tsid2 = 0x000000000000000aL; // 10
		String string2 = "000000000000A";
		String result2 = Tsid.from(tsid2).toString();
		assertEquals(string2, result2);
	}

	@Test
	public void testToLowerCase2() {
		long tsid1 = 0xffffffffffffffffL;
		String string1 = "FZZZZZZZZZZZZ".toLowerCase();
		String result1 = Tsid.from(tsid1).toLowerCase();
		assertEquals(string1, result1);

		long tsid2 = 0x000000000000000aL; // 10
		String string2 = "000000000000A".toLowerCase();
		String result2 = Tsid.from(tsid2).toLowerCase();
		assertEquals(string2, result2);
	}

	@Test
	public void testGetUnixMilliseconds() {

		long start = System.currentTimeMillis();
		Tsid tsid = TsidCreator.getTsid1024();
		long middle = tsid.getUnixMilliseconds();
		long end = System.currentTimeMillis();

		assertTrue(start <= middle);
		assertTrue(middle <= end + TsidFactory.CLOCK_DRIFT_TOLERANCE);
	}

	@Test
	public void testGetUnixMillisecondsMinimum() {

		// 2020-01-01T00:00:00.000Z
		long expected = Tsid.TSID_EPOCH;

		long time1 = 0; // the same as 2^42
		long tsid1 = time1 << TsidTest.RANDOM_BITS;
		assertEquals(expected, Tsid.from(tsid1).getUnixMilliseconds());

		long time2 = (long) Math.pow(2, 42);
		long tsid2 = time2 << TsidTest.RANDOM_BITS;
		assertEquals(expected, Tsid.from(tsid2).getUnixMilliseconds());
	}

	@Test
	public void testGetUnixMillisecondsMaximum() {

		// 2159-05-15T07:35:11.103Z
		long expected = Tsid.TSID_EPOCH + (long) Math.pow(2, 42) - 1;

		long time1 = (long) Math.pow(2, 42) - 1;
		long tsid1 = time1 << TsidTest.RANDOM_BITS;
		assertEquals(expected, Tsid.from(tsid1).getUnixMilliseconds());

		long time2 = -1; // the same as 2^42-1
		long tsid2 = time2 << TsidTest.RANDOM_BITS;
		assertEquals(expected, Tsid.from(tsid2).getUnixMilliseconds());
	}

	@Test
	public void testGetInstant() {

		Instant start = Instant.now();
		Tsid tsid = TsidCreator.getTsid1024();
		Instant middle = tsid.getInstant();
		Instant end = Instant.now();

		assertTrue(start.toEpochMilli() <= middle.toEpochMilli());
		assertTrue(middle.toEpochMilli() <= end.toEpochMilli() + TsidFactory.CLOCK_DRIFT_TOLERANCE);
	}

	@Test
	public void testGetInstantMinimum() {

		Instant expected = Instant.parse("2020-01-01T00:00:00.000Z");

		long time1 = 0; // the same as 2^42
		long tsid1 = time1 << TsidTest.RANDOM_BITS;
		assertEquals(expected, Tsid.from(tsid1).getInstant());

		long time2 = (long) Math.pow(2, 42);
		long tsid2 = time2 << TsidTest.RANDOM_BITS;
		assertEquals(expected, Tsid.from(tsid2).getInstant());
	}

	@Test
	public void testGetInstantMaximum() {

		Instant expected = Instant.parse("2159-05-15T07:35:11.103Z");

		long time1 = (long) Math.pow(2, 42) - 1;
		long tsid1 = time1 << TsidTest.RANDOM_BITS;
		assertEquals(expected, Tsid.from(tsid1).getInstant());

		long time2 = -1; // the same as 2^42-1
		long tsid2 = time2 << TsidTest.RANDOM_BITS;
		assertEquals(expected, Tsid.from(tsid2).getInstant());
	}

	@Test
	public void testGetTime() {
		for (int i = 0; i < LOOP_MAX; i++) {
			final long number = ThreadLocalRandom.current().nextLong();
			Tsid tsid = Tsid.from(number);

			long time0 = number >>> RANDOM_BITS;
			long time1 = tsid.getTime();

			assertEquals(time0, time1);
		}
	}

	@Test
	public void testGetRandom() {
		for (int i = 0; i < LOOP_MAX; i++) {
			final long number = ThreadLocalRandom.current().nextLong();
			Tsid tsid = Tsid.from(number);

			long random0 = number << TIME_BITS >>> TIME_BITS;
			long random1 = tsid.getRandom();

			assertEquals(random0, random1);
		}
	}

	@Test
	public void testIsValid() {

		String tsid = null; // Null
		assertFalse("Null tsid should be invalid.", Tsid.isValid(tsid));

		tsid = ""; // length: 0
		assertFalse("tsid with empty string should be invalid.", Tsid.isValid(tsid));

		tsid = "0123456789ABC"; // All upper case
		assertTrue("tsid in upper case should valid.", Tsid.isValid(tsid));

		tsid = "0123456789abc"; // All lower case
		assertTrue("tsid in lower case should be valid.", Tsid.isValid(tsid));

		tsid = "0123456789AbC"; // Mixed case
		assertTrue("tsid in upper and lower case should valid.", Tsid.isValid(tsid));

		tsid = "0123456789AB"; // length: 12
		assertFalse("tsid length lower than 13 should be invalid.", Tsid.isValid(tsid));

		tsid = "0123456789ABCC"; // length: 14
		assertFalse("tsid length greater than 13 should be invalid.", Tsid.isValid(tsid));

		tsid = "0123456789ABi"; // Letter I
		assertTrue("tsid with 'i' or 'I' should be valid.", Tsid.isValid(tsid));

		tsid = "0123456789ABl"; // Letter L
		assertTrue("tsid with 'i' or 'L' should be valid.", Tsid.isValid(tsid));

		tsid = "0123456789ABo"; // Letter O
		assertTrue("tsid with 'o' or 'O' should be valid.", Tsid.isValid(tsid));

		tsid = "0123456789ABu"; // Letter U
		assertFalse("tsid with 'u' or 'U' should be invalid.", Tsid.isValid(tsid));

		tsid = "0123456789AB#"; // Special char
		assertFalse("tsid with special chars should be invalid.", Tsid.isValid(tsid));

		try {
			tsid = null;
			Tsid.from(tsid);
			fail();
		} catch (IllegalArgumentException e) {
			// Success
		}

		for (int i = 0; i < LOOP_MAX; i++) {
			String string = TsidCreator.getTsid1024().toString();
			assertTrue(Tsid.isValid(string));
		}
	}

	@Test
	public void testTsidMax256() {

		final int maxTsid = 16384;
		final int maxLoop = 20000;

		Tsid[] list = new Tsid[maxLoop];

		for (int i = 0; i < maxLoop; i++) {
			// can generate up to 16384 per msec
			list[i] = TsidCreator.getTsid256();
		}

		int n = 0;
		long prevTime = 0;
		for (int i = 0; i < maxLoop; i++) {
			long time = list[i].getTime();
			if (time != prevTime) {
				n = 0;
			}
			n++;
			prevTime = time;
			assertFalse("Too many TSIDs: " + n, n > maxTsid);
		}
	}

	@Test
	public void testTsidMax1024() {

		final int maxTsid = 4096;
		final int maxLoop = 10000;

		Tsid[] list = new Tsid[maxLoop];

		for (int i = 0; i < maxLoop; i++) {
			// can generate up to 4096 per msec
			list[i] = TsidCreator.getTsid1024();
		}

		int n = 0;
		long prevTime = 0;
		for (int i = 0; i < maxLoop; i++) {
			long time = list[i].getTime();
			if (time != prevTime) {
				n = 0;
			}
			n++;
			prevTime = time;
			assertFalse("Too many TSIDs: " + n, n > maxTsid);
		}
	}

	@Test
	public void testEquals() {

		byte[] bytes = new byte[Tsid.TSID_BYTES];

		for (int i = 0; i < LOOP_MAX; i++) {

			ThreadLocalRandom.current().nextBytes(bytes);
			Tsid tsid1 = Tsid.from(bytes);
			Tsid tsid2 = Tsid.from(bytes);
			assertEquals(tsid1, tsid2);
			assertEquals(tsid1.toString(), tsid2.toString());
			assertEquals(Arrays.toString(tsid1.toBytes()), Arrays.toString(tsid2.toBytes()));

			// change all bytes
			for (int j = 0; j < bytes.length; j++) {
				bytes[j]++;
			}
			Tsid tsid3 = Tsid.from(bytes);
			assertNotEquals(tsid1, tsid3);
			assertNotEquals(tsid1.toString(), tsid3.toString());
			assertNotEquals(Arrays.toString(tsid1.toBytes()), Arrays.toString(tsid3.toBytes()));
		}
	}

	@Test
	public void testCompareTo() {
		byte[] bytes = new byte[Tsid.TSID_BYTES];

		for (int i = 0; i < LOOP_MAX; i++) {
			ThreadLocalRandom.current().nextBytes(bytes);
			Tsid tsid1 = Tsid.from(bytes);
			BigInteger number1 = new BigInteger(1, bytes);

			ThreadLocalRandom.current().nextBytes(bytes);
			Tsid tsid2 = Tsid.from(bytes);
			Tsid tsid3 = Tsid.from(bytes);
			BigInteger number2 = new BigInteger(1, bytes);
			BigInteger number3 = new BigInteger(1, bytes);

			// compare numerically
			assertEquals(number1.compareTo(number2) > 0, tsid1.compareTo(tsid2) > 0);
			assertEquals(number1.compareTo(number2) < 0, tsid1.compareTo(tsid2) < 0);
			assertEquals(number2.compareTo(number3) == 0, tsid2.compareTo(tsid3) == 0);

			// compare lexicographically
			assertEquals(number1.compareTo(number2) > 0, tsid1.toString().compareTo(tsid2.toString()) > 0);
			assertEquals(number1.compareTo(number2) < 0, tsid1.toString().compareTo(tsid2.toString()) < 0);
			assertEquals(number2.compareTo(number3) == 0, tsid2.toString().compareTo(tsid3.toString()) == 0);
		}
	}

	@Test
	public void testHashCode() {
		for (int i = 0; i < LOOP_MAX; i++) {

			long number = ThreadLocalRandom.current().nextLong();
			Tsid tsid1 = Tsid.from(number);
			Tsid tsid2 = Tsid.from(number);

			assertEquals(tsid1.hashCode(), tsid2.hashCode());
		}
	}

	@Test
	public void testTsidMax4096() {

		final int maxTsid = 1024;
		final int maxLoop = 10000;

		Tsid[] list = new Tsid[maxLoop];

		for (int i = 0; i < maxLoop; i++) {
			// can generate up to 1024 per msec
			list[i] = TsidCreator.getTsid4096();
		}

		int n = 0;
		long prevTime = 0;
		for (int i = 0; i < maxLoop; i++) {
			long time = list[i].getTime();
			if (time != prevTime) {
				n = 0;
			}
			n++;
			prevTime = time;
			assertFalse("Too many TSIDs: " + n, n > maxTsid);
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
