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

import com.github.f4b6a3.commons.util.Base32Util;

public class TsidUtil {

	protected static final long TIMESTAMP_LENGTH = 42;
	protected static final long RANDOMNESS_LENGTH = 22;

	protected static final String TSID_PATTERN_STRICT = "^[0-9a-hjkmnp-tv-zA-HJKMNP-TV-Z]{13}$";
	protected static final String TSID_PATTERN_LOOSE = "^[0-9a-tv-zA-TV-Z]{13}$";

	private TsidUtil() {
	}

	public static String toTsidString(long tsid) {
		return leftPad(Base32Util.toBase32Crockford(tsid));
	}

	private static String leftPad(String unpadded) {
		return "0000000000000".substring(unpadded.length()) + unpadded;
	}

	/**
	 * Checks if the TSID string is a valid.
	 * 
	 * The validation mode is not strict.
	 * 
	 * See {@link TsidUtil#validate(String, boolean)}.
	 * 
	 * @param tsid
	 *            a TSID
	 */
	protected static void validate(String tsid) {
		validate(tsid, false);
	}

	/**
	 * Checks if the TSID string is a valid.
	 * 
	 * See {@link TsidUtil#validate(String, boolean)}.
	 * 
	 * @param tsid
	 *            a TSID
	 */
	protected static void validate(String tsid, boolean strict) {
		if (!isValid(tsid, strict)) {
			throw new TsidUtilException(String.format("Invalid TSID: %s.", tsid));
		}
	}

	/**
	 * Checks if the string is a valid TSID.
	 * 
	 * The validation mode is not strict.
	 * 
	 * See {@link TsidUtil#validate(String, boolean)}.
	 */
	public static boolean isValid(String tsid) {
		return isValid(tsid, false);
	}

	/**
	 * Checks if the string is a valid TSID.
	 * 
	 * <pre>
	 * Strict validation: checks if the string is in the TSID format:
	 * 
	 * - 0123456789ABC (13 alphanumeric, case insensitive, except iI, lL, oO and uU)
	 * 
	 * Loose validation: checks if the string is in one of these formats:
	 *
	 * - 0123456789ABC (13 alphanumeric, case insensitive, except uU)
	 * </pre>
	 * 
	 * @param tsid
	 *            a TSID
	 * @param strict
	 *            true for strict validation, false for loose validation
	 * @return boolean true if valid
	 */
	public static boolean isValid(String tsid, boolean strict) {

		if (tsid == null || tsid.isEmpty()) {
			return false;
		}

		boolean matches = false;

		if (strict) {
			matches = tsid.matches(TSID_PATTERN_STRICT);
		} else {
			String u = tsid.replaceAll("-", "");
			matches = u.matches(TSID_PATTERN_LOOSE);
		}

		return matches;
	}

	protected static long extractTimestamp(long tsid) {
		return tsid >>> RANDOMNESS_LENGTH;
	}

	protected static long extractTimestamp(String tsid) {
		return extractTimestamp(Base32Util.fromBase32CrockfordAsLong(tsid));
	}

	public static long extractUnixMilliseconds(String tsid) {
		validate(tsid);
		final long timestamp = extractTimestamp(tsid);
		return TsidTimeUtil.toUnixMilliseconds(timestamp);
	}

	public static long extractUnixMilliseconds(long tsid) {
		final long timestamp = extractTimestamp(tsid);
		return TsidTimeUtil.toUnixMilliseconds(timestamp);
	}

	public static Instant extractInstant(String tsid) {
		validate(tsid);
		final long unixMilliseconds = extractUnixMilliseconds(tsid);
		return Instant.ofEpochMilli(unixMilliseconds);
	}

	public static Instant extractInstant(long tsid) {
		final long unixMilliseconds = extractUnixMilliseconds(tsid);
		return Instant.ofEpochMilli(unixMilliseconds);
	}

	public static class TsidUtilException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public TsidUtilException(String message) {
			super(message);
		}
	}
}
