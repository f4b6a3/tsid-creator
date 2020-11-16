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

/**
 * Utility that converts TSIDs to and from strings and byte arrays.
 */
public final class TsidConverter {

	protected static final char[] BASE32_CHARS = //
			{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', //
					'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', //
					'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'X', 'Y', 'Z' };

	protected static final long[] BASE32_VALUES = new long[128];
	static {

		// Numbers
		BASE32_VALUES['0'] = 0x00;
		BASE32_VALUES['1'] = 0x01;
		BASE32_VALUES['2'] = 0x02;
		BASE32_VALUES['3'] = 0x03;
		BASE32_VALUES['4'] = 0x04;
		BASE32_VALUES['5'] = 0x05;
		BASE32_VALUES['6'] = 0x06;
		BASE32_VALUES['7'] = 0x07;
		BASE32_VALUES['8'] = 0x08;
		BASE32_VALUES['9'] = 0x09;
		// Lower case
		BASE32_VALUES['a'] = 0x0a;
		BASE32_VALUES['b'] = 0x0b;
		BASE32_VALUES['c'] = 0x0c;
		BASE32_VALUES['d'] = 0x0d;
		BASE32_VALUES['e'] = 0x0e;
		BASE32_VALUES['f'] = 0x0f;
		BASE32_VALUES['g'] = 0x10;
		BASE32_VALUES['h'] = 0x11;
		BASE32_VALUES['j'] = 0x12;
		BASE32_VALUES['k'] = 0x13;
		BASE32_VALUES['m'] = 0x14;
		BASE32_VALUES['n'] = 0x15;
		BASE32_VALUES['p'] = 0x16;
		BASE32_VALUES['q'] = 0x17;
		BASE32_VALUES['r'] = 0x18;
		BASE32_VALUES['s'] = 0x19;
		BASE32_VALUES['t'] = 0x1a;
		BASE32_VALUES['v'] = 0x1b;
		BASE32_VALUES['w'] = 0x1c;
		BASE32_VALUES['x'] = 0x1d;
		BASE32_VALUES['y'] = 0x1e;
		BASE32_VALUES['z'] = 0x1f;
		// Lower case OIL
		BASE32_VALUES['o'] = 0x00;
		BASE32_VALUES['i'] = 0x01;
		BASE32_VALUES['l'] = 0x01;
		// Upper case
		BASE32_VALUES['A'] = 0x0a;
		BASE32_VALUES['B'] = 0x0b;
		BASE32_VALUES['C'] = 0x0c;
		BASE32_VALUES['D'] = 0x0d;
		BASE32_VALUES['E'] = 0x0e;
		BASE32_VALUES['F'] = 0x0f;
		BASE32_VALUES['G'] = 0x10;
		BASE32_VALUES['H'] = 0x11;
		BASE32_VALUES['J'] = 0x12;
		BASE32_VALUES['K'] = 0x13;
		BASE32_VALUES['M'] = 0x14;
		BASE32_VALUES['N'] = 0x15;
		BASE32_VALUES['P'] = 0x16;
		BASE32_VALUES['Q'] = 0x17;
		BASE32_VALUES['R'] = 0x18;
		BASE32_VALUES['S'] = 0x19;
		BASE32_VALUES['T'] = 0x1a;
		BASE32_VALUES['V'] = 0x1b;
		BASE32_VALUES['W'] = 0x1c;
		BASE32_VALUES['X'] = 0x1d;
		BASE32_VALUES['Y'] = 0x1e;
		BASE32_VALUES['Z'] = 0x1f;
		// Upper case OIL
		BASE32_VALUES['O'] = 0x00;
		BASE32_VALUES['I'] = 0x01;
		BASE32_VALUES['L'] = 0x01;
	}

	private TsidConverter() {
	}

	/**
	 * Returns a TSID number from a TSID string.
	 * 
	 * @param tsid a TSID string
	 * @return a TSID number
	 * @throws InvalidTsidException if invalid
	 */
	public static long fromString(String tsid) {

		final char[] chars = tsid == null ? new char[0] : tsid.toCharArray();
		TsidValidator.validate(chars);

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

		return number;
	}

	/**
	 * Returns a TSID string from a TSID number.
	 * 
	 * @param tsid a TSID number
	 * @return a TSID string
	 */
	public static String toString(long tsid) {

		final char[] chars = new char[13];

		chars[0x00] = BASE32_CHARS[(int) ((tsid >>> 60) & 0b11111)];
		chars[0x01] = BASE32_CHARS[(int) ((tsid >>> 55) & 0b11111)];
		chars[0x02] = BASE32_CHARS[(int) ((tsid >>> 50) & 0b11111)];
		chars[0x03] = BASE32_CHARS[(int) ((tsid >>> 45) & 0b11111)];
		chars[0x04] = BASE32_CHARS[(int) ((tsid >>> 40) & 0b11111)];
		chars[0x05] = BASE32_CHARS[(int) ((tsid >>> 35) & 0b11111)];
		chars[0x06] = BASE32_CHARS[(int) ((tsid >>> 30) & 0b11111)];
		chars[0x07] = BASE32_CHARS[(int) ((tsid >>> 25) & 0b11111)];
		chars[0x08] = BASE32_CHARS[(int) ((tsid >>> 20) & 0b11111)];
		chars[0x09] = BASE32_CHARS[(int) ((tsid >>> 15) & 0b11111)];
		chars[0x0a] = BASE32_CHARS[(int) ((tsid >>> 10) & 0b11111)];
		chars[0x0b] = BASE32_CHARS[(int) ((tsid >>> 5) & 0b11111)];
		chars[0x0c] = BASE32_CHARS[(int) (tsid & 0b11111)];

		return new String(chars);
	}
}
