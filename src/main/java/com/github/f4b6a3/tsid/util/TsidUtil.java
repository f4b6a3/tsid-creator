/*
 * MIT License
 * 
 * Copyright (c) 2020 Fabio Lima
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.f4b6a3.tsid.util;

import java.time.Instant;

/**
 * Utility that provides methods for for extracting creation time and node
 * identifier from TSIDs.
 */
public final class TsidUtil {

	private static final long RANDOMNESS_LENGTH = 22;
	private static final int DEFAULT_NODEID_LENGTH = 10;

	private static final int RANDOMNESS_MASK = 0x003fffff;

	protected static final int BASE_32 = 32;

	protected static final int TSID_CHAR_LENGTH = 13;

	// Include 'O'->ZERO, 'I'->ONE and 'L'->ONE
	protected static final char[] ALPHABET_CROCKFORD = "0123456789ABCDEFGHJKMNPQRSTVWXYZOIL".toCharArray();

	private TsidUtil() {
	}

	/**
	 * Returns the Unix milliseconds from a TSID.
	 * 
	 * @param tsid a TSID
	 * @return the Unix milliseconds
	 */
	public static long extractUnixMilliseconds(String tsid) {
		return extractUnixMilliseconds(TsidConverter.fromString(tsid));
	}

	/**
	 * Returns the Unix milliseconds from a TSID.
	 * 
	 * @param tsid a TSID
	 * @return the Unix milliseconds
	 */
	public static long extractUnixMilliseconds(long tsid) {
		final long timestamp = extractTimestamp(tsid);
		return TsidTime.toUnixMilliseconds(timestamp);
	}

	/**
	 * Returns the Unix milliseconds from a TSID, using a custom epoch.
	 * 
	 * The default epoch is 2020-01-01 00:00:00Z (TSID epoch).
	 * 
	 * @param tsid        a TSID
	 * @param customEpoch a custom epoch
	 * @return the Unix milliseconds
	 */
	public static long extractUnixMilliseconds(String tsid, long customEpoch) {
		return extractUnixMilliseconds(TsidConverter.fromString(tsid), customEpoch);
	}

	/**
	 * Returns the Unix milliseconds from a TSID, using a custom epoch.
	 * 
	 * The default epoch is 2020-01-01 00:00:00Z (TSID epoch).
	 * 
	 * @param tsid        a TSID
	 * @param customEpoch a custom epoch
	 * @return the Unix milliseconds
	 */
	public static long extractUnixMilliseconds(long tsid, long customEpoch) {
		final long timestamp = extractTimestamp(tsid);
		return TsidTime.toUnixMilliseconds(timestamp, customEpoch);
	}

	/**
	 * Returns the {@link Instant} from a TSID.
	 * 
	 * @param tsid a TSID
	 * @return the {@link Instant}.
	 */
	public static Instant extractInstant(String tsid) {
		return extractInstant(TsidConverter.fromString(tsid));
	}

	/**
	 * Returns the {@link Instant} from a TSID.
	 * 
	 * @param tsid a TSID
	 * @return the {@link Instant}.
	 */
	public static Instant extractInstant(long tsid) {
		final long unixMilliseconds = extractUnixMilliseconds(tsid);
		return Instant.ofEpochMilli(unixMilliseconds);
	}

	/**
	 * Returns the {@link Instant} from a TSID, using a custom epoch.
	 * 
	 * The default epoch is 2020-01-01 00:00:00Z (TSID epoch).
	 * 
	 * @param tsid        a TSID
	 * @param customEpoch a custom epoch
	 * @return the {@link Instant}
	 */
	public static Instant extractInstant(String tsid, Instant customEpoch) {
		return extractInstant(TsidConverter.fromString(tsid), customEpoch);
	}

	/**
	 * Returns the {@link Instant} from a TSID, using a custom epoch.
	 * 
	 * The default epoch is 2020-01-01 00:00:00Z (TSID epoch).
	 * 
	 * @param tsid        a TSID
	 * @param customEpoch a custom epoch
	 * @return the {@link Instant}
	 */
	public static Instant extractInstant(long tsid, Instant customEpoch) {
		final long unixMilliseconds = extractUnixMilliseconds(tsid, customEpoch.toEpochMilli());
		return Instant.ofEpochMilli(unixMilliseconds);
	}

	private static long extractTimestamp(long tsid) {
		return tsid >>> RANDOMNESS_LENGTH;
	}

	/**
	 * Returns the node identifier.
	 * 
	 * The DEFAULT bit length for node identifiers is 10. The maximum number of
	 * nodes using this default bit length is 2^10 = 1,024.
	 * 
	 * This method shouldn't be used for TSIDs created with different node
	 * identifier bit lengths.
	 * 
	 * @param tsid the TSID
	 * @return the node identifier
	 */
	public static int extractNodeIdentifier(long tsid) {
		return extractNodeIdentifier(tsid, DEFAULT_NODEID_LENGTH);
	}

	/**
	 * Returns the node identifier.
	 * 
	 * A bit length for node identifiers is needed.
	 * 
	 * The bit length must be in the range [0, 20].
	 * 
	 * @param tsid         the TSID
	 * @param nodeidLength the node identifier bit length
	 * @return the node identifier
	 * @throws IllegalArgumentException if the bit length is out of range [0, 20]
	 */
	public static int extractNodeIdentifier(long tsid, int nodeidLength) {

		// Check if the node identifier bit length between 0 and 20 (inclusive)
		if (nodeidLength < 0 || nodeidLength > 20) {
			throw new IllegalArgumentException("The node identifier bit length is out of the permited range: [0, 20]");
		}

		return (int) (tsid & RANDOMNESS_MASK) >>> (RANDOMNESS_LENGTH - nodeidLength);
	}

	protected static char[] removeHyphens(final char[] input) {

		int count = 0;
		char[] buffer = new char[input.length];

		for (int i = 0; i < input.length; i++) {
			if ((input[i] != '-')) {
				buffer[count++] = input[i];
			}
		}

		char[] output = new char[count];
		System.arraycopy(buffer, 0, output, 0, count);

		return output;
	}

	protected static boolean isCrockfordBase32(final char[] chars) {
		char[] input = toUpperCase(chars);
		for (int i = 0; i < input.length; i++) {
			if (!isCrockfordBase32(input[i])) {
				return false;
			}
		}
		return true;
	}

	protected static boolean isCrockfordBase32(char c) {
		for (int j = 0; j < ALPHABET_CROCKFORD.length; j++) {
			if (c == ALPHABET_CROCKFORD[j]) {
				return true;
			}
		}
		return false;
	}

	public static char[] toBase32Crockford(long number) {
		return encode(number, TsidUtil.ALPHABET_CROCKFORD);
	}

	public static long fromBase32Crockford(char[] chars) {
		return decode(chars, TsidUtil.ALPHABET_CROCKFORD);
	}

	protected static char[] zerofill(char[] chars, int length) {
		return lpad(chars, length, '0');
	}

	protected static char[] lpad(char[] chars, int length, char fill) {

		int delta = 0;
		int limit = 0;

		if (length > chars.length) {
			delta = length - chars.length;
			limit = length;
		} else {
			delta = 0;
			limit = chars.length;
		}

		char[] output = new char[chars.length + delta];
		for (int i = 0; i < limit; i++) {
			if (i < delta) {
				output[i] = fill;
			} else {
				output[i] = chars[i - delta];
			}
		}
		return output;
	}

	protected static char[] transliterate(char[] chars, char[] alphabet1, char[] alphabet2) {
		char[] output = chars.clone();
		for (int i = 0; i < output.length; i++) {
			for (int j = 0; j < alphabet1.length; j++) {
				if (output[i] == alphabet1[j]) {
					output[i] = alphabet2[j];
					break;
				}
			}
		}
		return output;
	}

	protected static char[] toUpperCase(final char[] chars) {
		char[] output = new char[chars.length];
		for (int i = 0; i < output.length; i++) {
			if (chars[i] >= 0x61 && chars[i] <= 0x7a) {
				output[i] = (char) ((int) chars[i] & 0xffffffdf);
			} else {
				output[i] = chars[i];
			}
		}
		return output;
	}

	/**
	 * Encode a long number to base 32 char array.
	 * 
	 * @param number   a long number
	 * @param alphabet an alphabet
	 * @return a base32 encoded char array
	 */
	protected static char[] encode(long number, char[] alphabet) {

		final int CHARS_MAX = 13; // 13 * 5 = 65

		if (number < 0) {
			throw new IllegalArgumentException(String.format("Number '%d' is not a positive integer.", number));
		}

		long n = number;
		char[] buffer = new char[CHARS_MAX];
		char[] output;

		int count = CHARS_MAX;
		while (n > 0) {
			buffer[--count] = alphabet[(int) (n % BASE_32)];
			n = n / BASE_32;
		}

		output = new char[buffer.length - count];
		System.arraycopy(buffer, count, output, 0, output.length);

		return output;
	}

	/**
	 * Decode a base 32 char array to a long number.
	 * 
	 * @param chars   a base 32 encoded char array
	 * @param alphabet an alphabet
	 * @return a long number
	 */
	protected static long decode(char[] chars, char[] alphabet) {

		long n = 0;

		for (int i = 0; i < chars.length; i++) {
			int d = chr(chars[i], alphabet);
			n = BASE_32 * n + d;
		}

		return n;
	}

	private static int chr(char c, char[] alphabet) {
		for (int i = 0; i < alphabet.length; i++) {
			if (alphabet[i] == c) {
				return (byte) i;
			}
		}
		return (byte) '0';
	}
}
