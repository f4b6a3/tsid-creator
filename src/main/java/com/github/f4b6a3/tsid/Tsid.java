/*
 * MIT License
 * 
 * Copyright (c) 2020-2021 Fabio Lima
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

package com.github.f4b6a3.tsid;

import java.io.Serializable;
import java.time.Instant;

/**
 * Class that represents a TSID (Time Sortable Identifier).
 * 
 * A TSID is formed by a creation time followed by random bits.
 * 
 * The TSID has 2 components:
 * 
 * - Time component (42 bits)
 * 
 * - Random component (22 bits)
 * 
 * The Random component has 2 sub-parts:
 * 
 * - Node ID (0 to 20 bits)
 * 
 * - Counter (2 to 22 bits)
 * 
 * The random component settings depend on the node identifier bit length. If
 * the node identifier bit length is 10, the counter bit length is limited to
 * 12. In this example, the maximum node identifier value is 2^10-1 = 1023 and
 * the maximum counter value is 2^12-1 = 4093. So the maximum TSIDs that can be
 * generated per millisecond per node is 4096.
 * 
 * Instances of this class are immutable.
 */
public final class Tsid implements Serializable, Comparable<Tsid> {

	private static final long serialVersionUID = -5446820982139116297L;

	private final long number;

	public static final int TSID_CHARS = 13;
	public static final int TSID_BYTES = 8;

	private static final int RANDOM_BITS_LENGTH = 22;
	private static final int RANDOM_BITS_MASK = 0x003fffff;

	public static final long TSID_EPOCH_MILLISECONDS = Instant.parse("2020-01-01T00:00:00.000Z").toEpochMilli();

	private static final char[] ALPHABET_UPPERCASE = //
			{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', //
					'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', //
					'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'X', 'Y', 'Z' };

	private static final char[] ALPHABET_LOWERCASE = //
			{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', //
					'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', //
					'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'x', 'y', 'z' };

	private static final long[] ALPHABET_VALUES = new long[128];
	static {
		for (int i = 0; i < ALPHABET_VALUES.length; i++) {
			ALPHABET_VALUES[i] = -1;
		}
		// Numbers
		ALPHABET_VALUES['0'] = 0x00;
		ALPHABET_VALUES['1'] = 0x01;
		ALPHABET_VALUES['2'] = 0x02;
		ALPHABET_VALUES['3'] = 0x03;
		ALPHABET_VALUES['4'] = 0x04;
		ALPHABET_VALUES['5'] = 0x05;
		ALPHABET_VALUES['6'] = 0x06;
		ALPHABET_VALUES['7'] = 0x07;
		ALPHABET_VALUES['8'] = 0x08;
		ALPHABET_VALUES['9'] = 0x09;
		// Lower case
		ALPHABET_VALUES['a'] = 0x0a;
		ALPHABET_VALUES['b'] = 0x0b;
		ALPHABET_VALUES['c'] = 0x0c;
		ALPHABET_VALUES['d'] = 0x0d;
		ALPHABET_VALUES['e'] = 0x0e;
		ALPHABET_VALUES['f'] = 0x0f;
		ALPHABET_VALUES['g'] = 0x10;
		ALPHABET_VALUES['h'] = 0x11;
		ALPHABET_VALUES['j'] = 0x12;
		ALPHABET_VALUES['k'] = 0x13;
		ALPHABET_VALUES['m'] = 0x14;
		ALPHABET_VALUES['n'] = 0x15;
		ALPHABET_VALUES['p'] = 0x16;
		ALPHABET_VALUES['q'] = 0x17;
		ALPHABET_VALUES['r'] = 0x18;
		ALPHABET_VALUES['s'] = 0x19;
		ALPHABET_VALUES['t'] = 0x1a;
		ALPHABET_VALUES['v'] = 0x1b;
		ALPHABET_VALUES['w'] = 0x1c;
		ALPHABET_VALUES['x'] = 0x1d;
		ALPHABET_VALUES['y'] = 0x1e;
		ALPHABET_VALUES['z'] = 0x1f;
		// Lower case OIL
		ALPHABET_VALUES['o'] = 0x00;
		ALPHABET_VALUES['i'] = 0x01;
		ALPHABET_VALUES['l'] = 0x01;
		// Upper case
		ALPHABET_VALUES['A'] = 0x0a;
		ALPHABET_VALUES['B'] = 0x0b;
		ALPHABET_VALUES['C'] = 0x0c;
		ALPHABET_VALUES['D'] = 0x0d;
		ALPHABET_VALUES['E'] = 0x0e;
		ALPHABET_VALUES['F'] = 0x0f;
		ALPHABET_VALUES['G'] = 0x10;
		ALPHABET_VALUES['H'] = 0x11;
		ALPHABET_VALUES['J'] = 0x12;
		ALPHABET_VALUES['K'] = 0x13;
		ALPHABET_VALUES['M'] = 0x14;
		ALPHABET_VALUES['N'] = 0x15;
		ALPHABET_VALUES['P'] = 0x16;
		ALPHABET_VALUES['Q'] = 0x17;
		ALPHABET_VALUES['R'] = 0x18;
		ALPHABET_VALUES['S'] = 0x19;
		ALPHABET_VALUES['T'] = 0x1a;
		ALPHABET_VALUES['V'] = 0x1b;
		ALPHABET_VALUES['W'] = 0x1c;
		ALPHABET_VALUES['X'] = 0x1d;
		ALPHABET_VALUES['Y'] = 0x1e;
		ALPHABET_VALUES['Z'] = 0x1f;
		// Upper case OIL
		ALPHABET_VALUES['O'] = 0x00;
		ALPHABET_VALUES['I'] = 0x01;
		ALPHABET_VALUES['L'] = 0x01;
	}

	/**
	 * Create a new TSID.
	 * 
	 * This constructor wraps the input value in an immutable object.
	 * 
	 * @param number a number
	 */
	public Tsid(final long number) {
		this.number = number;
	}

	/**
	 * Converts a number into a TSID.
	 * 
	 * This method wraps the input value in an immutable object.
	 * 
	 * @param number a number
	 * @return a TSID
	 */
	public static Tsid from(final long number) {
		return new Tsid(number);
	}

	/**
	 * Converts a byte array into a TSID.
	 * 
	 * @param bytes a byte array
	 * @return a TSID
	 */
	public static Tsid from(final byte[] bytes) {

		if (bytes == null || bytes.length != TSID_BYTES) {
			throw new IllegalArgumentException("Invalid TSID bytes"); // null or wrong length!
		}

		long number = 0;

		number |= (bytes[0x0] & 0xffL) << 56;
		number |= (bytes[0x1] & 0xffL) << 48;
		number |= (bytes[0x2] & 0xffL) << 40;
		number |= (bytes[0x3] & 0xffL) << 32;
		number |= (bytes[0x4] & 0xffL) << 24;
		number |= (bytes[0x5] & 0xffL) << 16;
		number |= (bytes[0x6] & 0xffL) << 8;
		number |= (bytes[0x7] & 0xffL);

		return new Tsid(number);
	}

	/**
	 * Converts a canonical string into a TSID.
	 * 
	 * The input string must be 13 characters long and must contain only characters
	 * from Crockford's base 32 alphabet.
	 * 
	 * The first character of the input string must be between 0 and F.
	 * 
	 * @param string a canonical string
	 * @return a TSID
	 */
	public static Tsid from(final String string) {

		final char[] chars = toCharArray(string);

		long number = 0;

		number |= ALPHABET_VALUES[chars[0x00]] << 60;
		number |= ALPHABET_VALUES[chars[0x01]] << 55;
		number |= ALPHABET_VALUES[chars[0x02]] << 50;
		number |= ALPHABET_VALUES[chars[0x03]] << 45;
		number |= ALPHABET_VALUES[chars[0x04]] << 40;
		number |= ALPHABET_VALUES[chars[0x05]] << 35;
		number |= ALPHABET_VALUES[chars[0x06]] << 30;
		number |= ALPHABET_VALUES[chars[0x07]] << 25;
		number |= ALPHABET_VALUES[chars[0x08]] << 20;
		number |= ALPHABET_VALUES[chars[0x09]] << 15;
		number |= ALPHABET_VALUES[chars[0x0a]] << 10;
		number |= ALPHABET_VALUES[chars[0x0b]] << 5;
		number |= ALPHABET_VALUES[chars[0x0c]];

		return new Tsid(number);
	}

	/**
	 * Convert the TSID into a number.
	 * 
	 * This method simply unwraps the internal value.
	 * 
	 * @return an number.
	 */
	public long toLong() {
		return this.number;
	}

	/**
	 * Convert the TSID into a byte array.
	 * 
	 * @return an byte array.
	 */
	public byte[] toBytes() {

		final byte[] bytes = new byte[TSID_BYTES];

		bytes[0x0] = (byte) (number >>> 56);
		bytes[0x1] = (byte) (number >>> 48);
		bytes[0x2] = (byte) (number >>> 40);
		bytes[0x3] = (byte) (number >>> 32);
		bytes[0x4] = (byte) (number >>> 24);
		bytes[0x5] = (byte) (number >>> 16);
		bytes[0x6] = (byte) (number >>> 8);
		bytes[0x7] = (byte) (number);

		return bytes;
	}

	/**
	 * Converts the TSID into a canonical string in upper case.
	 * 
	 * The output string is 13 characters long and contains only characters from
	 * Crockford's base 32 alphabet.
	 * 
	 * For lower case string, use the shorthand {@code Tsid#toLowerCase()}, instead
	 * of {@code Tsid#toString()#toLowerCase()}.
	 * 
	 * See: https://www.crockford.com/base32.html
	 * 
	 * @return a TSID string
	 */
	@Override
	public String toString() {
		return toString(ALPHABET_UPPERCASE);
	}

	/**
	 * Converts the TSID into a canonical string in lower case.
	 * 
	 * The output string is 13 characters long and contains only characters from
	 * Crockford's base 32 alphabet.
	 * 
	 * It is faster shorthand for {@code Tsid#toString()#toLowerCase()}.
	 * 
	 * See: https://www.crockford.com/base32.html
	 * 
	 * @return a string
	 */
	public String toLowerCase() {
		return toString(ALPHABET_LOWERCASE);
	}

	/**
	 * Returns the instant of creation.
	 * 
	 * The instant of creation is extracted from the time component.
	 * 
	 * @return {@link Instant}
	 */
	public Instant getInstant() {
		return Instant.ofEpochMilli(this.getTime() + TSID_EPOCH_MILLISECONDS);
	}

	/**
	 * Returns the instant of creation.
	 * 
	 * The instant of creation is extracted from the time component.
	 * 
	 * @param customEpoch the custom epoch instant
	 * @return {@link Instant}
	 */
	public Instant getInstant(final Instant customEpoch) {
		return Instant.ofEpochMilli(this.getTime() + customEpoch.toEpochMilli());
	}

	/**
	 * Returns the time component as a number.
	 * 
	 * The time component is a number between 0 and 2^42-1.
	 * 
	 * @return a number of milliseconds.
	 */
	public long getTime() {
		return this.number >>> RANDOM_BITS_LENGTH;
	}

	/**
	 * Returns the random component as a number.
	 * 
	 * The time component is a number between 0 and 2^22-1.
	 * 
	 * @return a number
	 */
	public long getRandom() {
		return this.number & RANDOM_BITS_MASK;
	}

	/**
	 * Checks if the input string is valid.
	 * 
	 * The input string must be 13 characters long and must contain only characters
	 * from Crockford's base 32 alphabet.
	 * 
	 * The first character of the input string must be between 0 and F.
	 * 
	 * @param string a string
	 * @return true if valid
	 */
	public static boolean isValid(final String string) {
		return string != null && isValidCharArray(string.toCharArray());
	}

	@Override
	public int compareTo(Tsid other) {
		if (this.number < other.number)
			return -1;
		if (this.number > other.number)
			return 1;
		return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (number ^ (number >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tsid other = (Tsid) obj;
		if (number != other.number)
			return false;
		return true;
	}

	protected String toString(final char[] alphabet) {

		final char[] chars = new char[TSID_CHARS];

		chars[0x00] = alphabet[(int) ((number >>> 60) & 0b11111)];
		chars[0x01] = alphabet[(int) ((number >>> 55) & 0b11111)];
		chars[0x02] = alphabet[(int) ((number >>> 50) & 0b11111)];
		chars[0x03] = alphabet[(int) ((number >>> 45) & 0b11111)];
		chars[0x04] = alphabet[(int) ((number >>> 40) & 0b11111)];
		chars[0x05] = alphabet[(int) ((number >>> 35) & 0b11111)];
		chars[0x06] = alphabet[(int) ((number >>> 30) & 0b11111)];
		chars[0x07] = alphabet[(int) ((number >>> 25) & 0b11111)];
		chars[0x08] = alphabet[(int) ((number >>> 20) & 0b11111)];
		chars[0x09] = alphabet[(int) ((number >>> 15) & 0b11111)];
		chars[0x0a] = alphabet[(int) ((number >>> 10) & 0b11111)];
		chars[0x0b] = alphabet[(int) ((number >>> 5) & 0b11111)];
		chars[0x0c] = alphabet[(int) (number & 0b11111)];

		return new String(chars);
	}

	protected static char[] toCharArray(final String string) {
		char[] chars = string == null ? null : string.toCharArray();
		if (!isValidCharArray(chars)) {
			throw new IllegalArgumentException(String.format("Invalid TSID: \"%s\"", string));
		}
		return chars;
	}

	/**
	 * Checks if the string is a valid TSID.
	 * 
	 * A valid TSID string is a sequence of 13 characters from Crockford's base 32
	 * alphabet.
	 * 
	 * The first character of the input string must be between 0 and F.
	 * 
	 * @param chars a char array
	 * @return boolean true if valid
	 */
	protected static boolean isValidCharArray(final char[] chars) {

		if (chars == null || chars.length != TSID_CHARS) {
			return false; // null or wrong size!
		}

		// The extra bit added by base-32 encoding must be zero
		// As a consequence, the 1st char of the input string must be between 0 and F.
		if ((ALPHABET_VALUES[chars[0]] & 0b10000) != 0) {
			return false; // overflow!
		}

		for (int i = 0; i < chars.length; i++) {
			if (ALPHABET_VALUES[chars[i]] == -1) {
				return false; // invalid character!
			}
		}
		return true; // It seems to be OK.
	}
}
