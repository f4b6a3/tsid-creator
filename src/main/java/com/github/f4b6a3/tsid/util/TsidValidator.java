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

public class TsidValidator {

	protected static final String TSID_PATTERN = "^[0-9a-tv-zA-TV-Z]{13}$";

	private TsidValidator() {
	}

	/**
	 * Checks if the UUID byte array is valid.
	 * 
	 * A valid TSID byte array is a not null sequence of 8 bytes.
	 * 
	 * @param tsid a UUID byte array
	 * @return true if valid, false if invalid
	 */
	protected static boolean isValid(byte[] tsid) {
		return tsid != null && tsid.length == 8;
	}

	/**
	 * Checks if the TSID byte array is valid.
	 * 
	 * See {@link TsidValidator#isValid(byte[])}
	 * 
	 * @param tsid a TSID
	 * @throws InvalidTsidException if invalid
	 */
	public static void validate(byte[] tsid) {
		if (!isValid(tsid)) {
			throw new InvalidTsidException("Invalid TSID byte array.");
		}
	}

	/**
	 * Checks if the string is a valid TSID.
	 * 
	 * A valid TSID string is a sequence of 13 characters from Crockford's base 32
	 * alphabet.
	 * 
	 * Dashes are ignored by this validator.
	 * 
	 * <pre>
	 * Examples of valid TSID strings:
	 * 
	 * - 0123456789ABC (13 alphanumeric, case insensitive, except iI, lL, oO and uU)
	 * - 0123456789ABC (13 alphanumeric, case insensitive, except uU)
	 * - 012-34567-89ABC (with dashes, 13 alphanumeric, case insensitive, except iI, lL, oO and uU)
	 * - 01234-56789-ABC (with dashes, 13 alphanumeric, case insensitive, except uU)
	 * </pre>
	 * 
	 * @param tsid   a TSID
	 * @param strict true for strict validation, false for loose validation
	 * @return boolean true if valid
	 */
	public static boolean isValid(String tsid) {

		if (tsid == null || tsid.isEmpty()) {
			return false;
		}

		String u = tsid.replace("-", "");
		return u.matches(TSID_PATTERN);
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
