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

import static com.github.f4b6a3.tsid.util.TsidConverter.BASE32_VALUES;

import com.github.f4b6a3.tsid.exception.InvalidTsidException;

public final class TsidValidator {

	// 7ZZZZZZZZZZZZ: 9223372036854775807 (2^63 - 1)
	protected static final long TSID_MAX = 0x7fffffffffffffffL;

	protected static final int TSID_LENGTH = 13;

	private TsidValidator() {
	}

	/**
	 * Checks if the string is a valid TSID.
	 * 
	 * A valid TSID string is a sequence of 13 characters from Crockford's base 32
	 * alphabet.
	 * 
	 * <pre>
	 * Examples of valid TSID strings:
	 * 
	 * - 0123456789ABC (13 alphanumeric, case insensitive, except U)
	 * - 0123456789OIL (13 alphanumeric, case insensitive, including OIL, except U)
	 * - 0123-4567-89ABC (13 alphanumeric, case insensitive, except U, with hyphens)
	 * - 0123-4567-89OIL (13 alphanumeric, case insensitive, including OIL, except U, with hyphens)
	 * </pre>
	 * 
	 * @param tsid a TSID
	 * @return boolean true if valid
	 */
	public static boolean isValid(String tsid) {
		if (tsid == null) {
			return false;
		}
		final char[] chars = tsid.toCharArray();
		return isValidString(chars) && isValidNumber(chars);
	}

	/**
	 * Checks if the TSID string is a valid.
	 * 
	 * See {@link TsidValidator#isValid(String)}.
	 * 
	 * @param tsid a TSID
	 * @throws InvalidTsidException if invalid
	 */
	public static void validate(String tsid) {
		if (tsid != null) {
			final char[] chars = tsid.toCharArray();
			if (isValidString(chars) && isValidNumber(chars)) {
				return; // valid
			}
		}
		throw new InvalidTsidException(String.format("Invalid TSID: %s.", tsid));
	}

	/**
	 * Checks if the string is a valid TSID.
	 * 
	 * A valid TSID string is a sequence of 13 characters from Crockford's base 32
	 * alphabet.
	 * 
	 * <pre>
	 * Examples of valid TSID strings:
	 * 
	 * - 0123456789ABC (13 alphanumeric, case insensitive, except U)
	 * - 0123456789OIL (13 alphanumeric, case insensitive, including OIL, except U)
	 * - 0123-4567-89ABC (13 alphanumeric, case insensitive, except U, with hyphens)
	 * - 0123-4567-89OIL (13 alphanumeric, case insensitive, including OIL, except U, with hyphens)
	 * </pre>
	 * 
	 * @param c a char array
	 * @return boolean true if valid
	 */
	protected static boolean isValidString(final char[] c) {
		int hyphen = 0;
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '-') {
				hyphen++;
				continue;
			}
			if (c[i] == 'U' || c[i] == 'u') {
				return false;
			}
			// ASCII codes: A-Z, 0-9, a-z
			if (!((c[i] >= 0x41 && c[i] <= 0x5a) || (c[i] >= 0x30 && c[i] <= 0x39) || (c[i] >= 0x61 && c[i] <= 0x7a))) {
				return false;
			}
		}
		return (c.length - hyphen) == TSID_LENGTH;
	}

	/**
	 * Checks if the timestamp is between 0 and 2^48-1
	 * 
	 * @param chars a char array
	 * @return false if invalid.
	 */
	protected static boolean isValidNumber(char[] chars) {

		long number = 0;

		number |= BASE32_VALUES[chars[0x00]] << 60;
		number |= BASE32_VALUES[chars[0x01]] << 55;
		number |= BASE32_VALUES[chars[0x02]] << 50;
		number |= BASE32_VALUES[chars[0x03]] << 45;
		number |= BASE32_VALUES[chars[0x04]] << 40;
		number |= BASE32_VALUES[chars[0x05]] << 35;
		number |= BASE32_VALUES[chars[0x06]] << 30;
		number |= BASE32_VALUES[chars[0x07]] << 25;
		number |= BASE32_VALUES[chars[0x08]] << 20;
		number |= BASE32_VALUES[chars[0x09]] << 15;
		number |= BASE32_VALUES[chars[0x0a]] << 10;
		number |= BASE32_VALUES[chars[0x0b]] << 5;
		number |= BASE32_VALUES[chars[0x0c]];

		return number >= 0 && number <= TSID_MAX;
	}
}
