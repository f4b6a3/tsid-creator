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

import static com.github.f4b6a3.tsid.util.TsidUtil.*;

/**
 * Utility that converts TSIDs to and from strings and byte arrays.
 */
public final class TsidConverter {

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
		return fromBase32Crockford(tsid.toCharArray());
	}

	/**
	 * Returns a TSID string from a TSID number.
	 * 
	 * @param tsid a TSID number
	 * @return a TSID string
	 */
	public static String toString(long tsid) {
		return new String(zerofill(toBase32Crockford(tsid), TSID_CHAR_LENGTH));
	}

}
