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

import java.nio.ByteBuffer;

/**
 * Utility that converts TSIDs to and from strings and byte arrays.
 */
public class TsidConverter {

	protected static final long TIMESTAMP_LENGTH = 42;
	protected static final long RANDOMNESS_LENGTH = 22;

	protected static final int BASE_32 = 32;

	protected static final char[] ALPHABET_DEFAULT = "0123456789abcdefghijklmnopqrstuv".toCharArray();
	protected static final char[] ALPHABET_CROCKFORD = "0123456789ABCDEFGHJKMNPQRSTVWXYZ".toCharArray();

	private TsidConverter() {
	}

	/**
	 * Returns a TSID number from a TSID string.
	 * 
	 * @param tsid a TSID string
	 * @return a TSID number
	 */
	public static long fromString(String tsid) {
		TsidValidator.validate(tsid);
		String string = transliterate(tsid.toUpperCase(), ALPHABET_CROCKFORD, ALPHABET_DEFAULT);
		return Long.parseLong(string, BASE_32);
	}

	/**
	 * Returns a TSID string from a TSID number.
	 * 
	 * @param tsid a TSID number
	 * @return a TSID string
	 */
	public static String toString(long tsid) {
		String string = Long.toString(tsid, BASE_32);
		return leftPad(transliterate(string, ALPHABET_DEFAULT, ALPHABET_CROCKFORD));
	}

	/**
	 * Returns a TSID number from a TSID byte array.
	 * 
	 * @param tsid a TSID byte array
	 * @return a TSID number
	 */
	public static long fromBytes(byte[] tsid) {
		return ByteBuffer.wrap(tsid).getLong();
	}

	/**
	 * Returns a TSID byte array from a TSID number.
	 * 
	 * @param tsid a TSID number
	 * @return a TSID byte array
	 */
	public static byte[] toBytes(long tsid) {
		return ByteBuffer.allocate(8).putLong(tsid).array();
	}

	private static String leftPad(String unpadded) {
		return "0000000000000".substring(unpadded.length()) + unpadded;
	}

	private static String transliterate(String string, char[] alphabet1, char[] alphabet2) {
		char[] chars = string.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			for (int j = 0; j < alphabet1.length; j++) {
				if (chars[i] == alphabet1[j]) {
					chars[i] = alphabet2[j];
				}
			}
		}
		return new String(chars);
	}
}
