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

import com.github.f4b6a3.tsid.exception.InvalidTsidException;

import static com.github.f4b6a3.tsid.util.TsidUtil.*;

public final class TsidValidator {

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

		char[] chars = removeHyphens(tsid.toCharArray());
		return (chars.length == TSID_CHAR_LENGTH && isCrockfordBase32(chars));
	}

	/**
	 * Checks if the TSID string is a valid.
	 * 
	 * See {@link TsidValidator#isValid(String)}.
	 * 
	 * @param tsid a TSID
	 * @throws InvalidTsidException if invalid
	 */
	protected static void validate(String tsid) {
		if (!isValid(tsid)) {
			throw new InvalidTsidException(String.format("Invalid TSID string: %s.", tsid));
		}
	}
}
