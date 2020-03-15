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

package com.github.f4b6a3.tsid;

import com.github.f4b6a3.tsid.creator.TimeSortableIdCreator;
import com.github.f4b6a3.tsid.util.TsidUtil;

public class TsidCreator {

	private TsidCreator() {
	}

	/**
	 * Returns a TSID.
	 * 
	 * @return a TSID
	 */
	public static long getTsid() {
		return TsidCreatorLazyHolder.INSTANCE.create();
	}

	/**
	 * Returns a TSID as string.
	 * 
	 * The returning string is encoded to Crockford's base 32.
	 * 
	 * @return a TSID
	 */
	public static String getTsidString() {
		return TsidUtil.toTsidString(getTsid());
	}

	public static TimeSortableIdCreator getTimeSortableIdCreator() {
		return new TimeSortableIdCreator();
	}

	private static class TsidCreatorLazyHolder {
		static final TimeSortableIdCreator INSTANCE = getTimeSortableIdCreator();
	}
}
