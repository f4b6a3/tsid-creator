/*
 * MIT License
 * 
 * Copyright (c) 2020-2021 Fabio Lima
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

import com.github.f4b6a3.tsid.factory.TsidFactory;

/**
 * A facade that generates time sortable IDs (TSIDs).
 * 
 * The term TSID stands for (roughly) Time Sortable ID. A TSID is a number that
 * is formed by the creation time followed by random bits.
 */
public final class TsidCreator {

	public static final int NODE_LENGTH_256 = 8; // S
	public static final int NODE_LENGTH_1024 = 10; // M
	public static final int NODE_LENGTH_4096 = 12; // L

	private TsidCreator() {
	}

	/**
	 * Returns a TSID.
	 * 
	 * The node identifier is RANDOM.
	 * 
	 * It supports up to 256 nodes.
	 * 
	 * It can generate up to 16,384 TSIDs per millisecond per node.
	 * 
	 * The system property `tsidcreator.node` and environment variable
	 * `TSIDCREATOR_NODE` can be used to replace the random node identifier.
	 * 
	 * ## Random component settings:
	 * 
	 * - Node bit length: 8
	 * 
	 * - Counter bit length: 14
	 * 
	 * - Maximum node: 256 (2^8)
	 * 
	 * - Maximum counter: 16,384 (2^14)
	 * 
	 * @return a TSID
	 */
	public static Tsid getTsid256() {
		return TsidFactory256Holder.INSTANCE.create();
	}

	/**
	 * Returns a TSID.
	 * 
	 * The node identifier is RANDOM.
	 * 
	 * It supports up to 1,024 nodes.
	 * 
	 * It can generate up to 4,096 TSIDs per millisecond per node.
	 * 
	 * The system property `tsidcreator.node` and environment variable
	 * `TSIDCREATOR_NODE` can be used to replace the random node identifier.
	 * 
	 * ## Random component settings:
	 * 
	 * - Node bit length: 10
	 * 
	 * - Counter bit length: 12
	 * 
	 * - Maximum node: 1,024 (2^10)
	 * 
	 * - Maximum counter: 4,096 (2^12)
	 * 
	 * @return a TSID
	 */
	public static Tsid getTsid1024() {
		return TsidFactory1024Holder.INSTANCE.create();
	}

	/**
	 * Returns a TSID.
	 * 
	 * The node identifier is RANDOM.
	 * 
	 * It supports up to 4,096 nodes.
	 * 
	 * It can generate up to 1,024 TSIDs per millisecond per node.
	 * 
	 * The system property `tsidcreator.node` and environment variable
	 * `TSIDCREATOR_NODE` can be used to replace the random node identifier.
	 * 
	 * ## Random component settings:
	 * 
	 * - Node bit length: 12
	 * 
	 * - Counter bit length: 10
	 * 
	 * - Maximum node: 4,096 (2^12)
	 * 
	 * - Maximum counter: 1,024 (2^10)
	 * 
	 * @return a TSID number
	 */
	public static Tsid getTsid4096() {
		return TsidFactory4096Holder.INSTANCE.create();
	}

	/**
	 * Returns a {@link TsidFactory}.
	 * 
	 * The node identifier is defined by parameter. A null argument means RANDOM
	 * node identifier.
	 * 
	 * It supports up to 256 nodes.
	 * 
	 * It can generate up to 16,384 TSIDs per millisecond per node.
	 * 
	 * ## Random component settings:
	 * 
	 * - Node bit length: 8
	 * 
	 * - Counter bit length: 14
	 * 
	 * - Maximum node: 256 (2^8)
	 * 
	 * - Maximum counter: 16,384 (2^14)
	 * 
	 * @param node a node identifier
	 * @return a {@link TsidFactory}
	 */
	public static TsidFactory getTsidFactory256(Integer node) {
		return new TsidFactory(node, NODE_LENGTH_256);
	}

	/**
	 * Returns a {@link TsidFactory}.
	 * 
	 * The node identifier is defined by parameter. A null argument means RANDOM
	 * node identifier.
	 * 
	 * It supports up to 1,024 nodes.
	 * 
	 * It can generate up to 4,096 TSIDs per millisecond per node.
	 * 
	 * ## Random component settings:
	 * 
	 * - Node bit length: 10
	 * 
	 * - Counter bit length: 12
	 * 
	 * - Maximum node: 1,024 (2^10)
	 * 
	 * - Maximum counter: 4,096 (2^12)
	 * 
	 * @param node a node identifier
	 * @return a {@link TsidFactory}
	 */
	public static TsidFactory getTsidFactory1024(Integer node) {
		return new TsidFactory(node, NODE_LENGTH_1024);
	}

	/**
	 * Returns a {@link TsidFactory}.
	 * 
	 * The node identifier is defined by parameter. A null argument means RANDOM
	 * node identifier.
	 * 
	 * It supports up to 4,096 nodes.
	 * 
	 * It can generate up to 1,024 TSIDs per millisecond per node.
	 * 
	 * ## Random component settings:
	 * 
	 * - Node bit length: 12
	 * 
	 * - Counter bit length: 10
	 * 
	 * - Maximum node: 4,096 (2^12)
	 * 
	 * - Maximum counter: 1,024 (2^10)
	 * 
	 * @param node a node identifier
	 * @return a {@link TsidFactory}
	 */
	public static TsidFactory getTsidFactory4096(Integer node) {
		return new TsidFactory(node, NODE_LENGTH_4096);
	}

	private static class TsidFactory256Holder {
		static final TsidFactory INSTANCE = getTsidFactory256(null);
	}

	private static class TsidFactory1024Holder {
		static final TsidFactory INSTANCE = getTsidFactory1024(null);
	}

	private static class TsidFactory4096Holder {
		static final TsidFactory INSTANCE = getTsidFactory4096(null);
	}
}
