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

import com.github.f4b6a3.tsid.creator.TimeIdCreator;
import com.github.f4b6a3.tsid.creator.impl.AdjustableNodeTimeIdCreator;
import com.github.f4b6a3.tsid.creator.impl.NodeTimeIdCreator;
import com.github.f4b6a3.tsid.creator.impl.DefaultTimeIdCreator;

/**
 * A utility class that creates time sortable IDs (TSIDs).
 * 
 * A TSID is a number that is formed by a creation time followed by random bits.
 */
public class TsidCreator {

	private TsidCreator() {
	}

	/**
	 * Returns a TSID.
	 * 
	 * The default counter bit length is 22.
	 * 
	 * The maximum counter value is 2^22 = 4,194,304.
	 * 
	 * @return a TSID
	 */
	public static long getTsid() {
		return DefaultTimeIdCreatorHolder.INSTANCE.create();
	}

	/**
	 * Returns a TSID with node identifier.
	 * 
	 * The default node identifier bit length is 10.
	 * 
	 * The default counter bit length is 12.
	 * 
	 * The maximum node identifier is 2^10 = 1,024.
	 * 
	 * The maximum counter value is 2^12 = 4,096.
	 * 
	 * @param node a node identifier
	 * @return a TSID
	 */
	public static long getTsid(int node) {
		return NodeTimeIdCreatorHolder.INSTANCE.create(node);
	}

	/**
	 * Returns a TSID as string.
	 * 
	 * The returning string is encoded to Crockford's base32.
	 * 
	 * The default counter bit length is 22.
	 * 
	 * The maximum counter value is 2^22 = 4,194,304.
	 * 
	 * @return a TSID
	 */
	public static String getTsidString() {
		return DefaultTimeIdCreatorHolder.INSTANCE.createString();
	}

	/**
	 * Returns a TSID with node identifier.
	 * 
	 * The returning string is encoded to Crockford's base32.
	 * 
	 * The default node identifier bit length is 10.
	 * 
	 * The default counter bit length is 12.
	 * 
	 * The maximum node identifier is 2^10 = 1,024.
	 * 
	 * The maximum counter value is 2^12 = 4,096.
	 * 
	 * @param node a node identifier
	 * @return a TSID
	 */
	public static String getTsidString(int node) {
		return NodeTimeIdCreatorHolder.INSTANCE.createString(node);
	}

	/**
	 * Returns a {@link TimeIdCreator}.
	 * 
	 * The default counter bit length is 22.
	 * 
	 * The maximum counter value is 2^22 = 4,194,304.
	 * 
	 * @return a {@link TimeIdCreator}
	 */
	public static TimeIdCreator getTimeIdCreator() {
		return new DefaultTimeIdCreator();
	}

	/**
	 * Returns a {@link TimeIdCreator}.
	 * 
	 * The default node identifier bit length is 10.
	 * 
	 * The default counter bit length is 12.
	 * 
	 * The maximum node identifier is 2^10 = 1,024.
	 * 
	 * The maximum counter value is 2^12 = 4,096.
	 * 
	 * @param node the node identifier
	 * @return a {@link TimeIdCreator}
	 */
	public static TimeIdCreator getTimeIdCreator(int node) {
		return new NodeTimeIdCreator(node);
	}

	/**
	 * Returns a {@link TimeIdCreator}.
	 * 
	 * The node identifier bit length can be between 0 and 20.
	 * 
	 * The counter bit length depends on the node identifier bit length.
	 * 
	 * The maximum node identifier depends on it's bit length.
	 * 
	 * The maximum counter value also depends on it's bit length.
	 * 
	 * @param node       the node identifier
	 * @param nodeLength the node identifier bit length
	 * @return a {@link TimeIdCreator}
	 */
	public static TimeIdCreator getTimeIdCreator(int node, int nodeLength) {
		return new AdjustableNodeTimeIdCreator(node, nodeLength);
	}

	private static class DefaultTimeIdCreatorHolder {
		static final DefaultTimeIdCreator INSTANCE = new DefaultTimeIdCreator();
	}

	private static class NodeTimeIdCreatorHolder {
		static final NodeTimeIdCreator INSTANCE = new NodeTimeIdCreator();
	}
}
