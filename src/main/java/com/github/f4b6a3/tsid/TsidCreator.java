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

public class TsidCreator {

	private TsidCreator() {
	}

	/**
	 * Returns a TSID.
	 * 
	 * @return a TSID
	 */
	public static long getTsid() {
		return TimeSortableIdCreatorHolder.INSTANCE.create();
	}

	/**
	 * Returns a TSID with node identifier.
	 * 
	 * The implicit node identifier bit length is 10.
	 * 
	 * The maximum node identifiers is 2^10 = 1024.
	 * 
	 * @return a TSID
	 */
	public static long getTsid(int node) {
		return TimeSortableIdWithNodeCreatorHolder.INSTANCE.create(node);
	}

	/**
	 * Returns a TSID as string.
	 * 
	 * The returning string is encoded to Crockford's base32.
	 * 
	 * @return a TSID
	 */
	public static String getTsidString() {
		return TimeSortableIdCreatorHolder.INSTANCE.createString();
	}

	/**
	 * Returns a TSID with node identifier.
	 * 
	 * The implicit node identifier bit length is 10.
	 * 
	 * The maximum node identifiers is 2^10 = 1024.
	 * 
	 * The returning string is encoded to Crockford's base32.
	 * 
	 * @return a TSID
	 */
	public static String getTsidString(int node) {
		return TimeSortableIdWithNodeCreatorHolder.INSTANCE.createString(node);
	}

	/**
	 * Returns a {@link TimeSortableIdCreator}.
	 * 
	 * @return a {@link TimeSortableIdCreator}
	 */
	public static TimeSortableIdCreator getTimeSortableIdCreator() {
		return new TimeSortableIdCreator();
	}

	private static class TimeSortableIdCreatorHolder {
		static final TimeSortableIdCreator INSTANCE = getTimeSortableIdCreator();
	}

	private static class TimeSortableIdWithNodeCreatorHolder {
		static final TimeSortableIdCreator INSTANCE = getTimeSortableIdCreator().withNodeIdentifier(0);
	}
}
