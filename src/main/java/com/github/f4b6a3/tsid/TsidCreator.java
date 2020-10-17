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
import com.github.f4b6a3.tsid.exception.InvalidTsidException;
import com.github.f4b6a3.tsid.util.TsidConverter;

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
	 * Returns a TSID number from a TSID string.
	 * 
	 * @param tsid a TSID string
	 * @return a TSID number
	 * @throws InvalidTsidException if invalid
	 */
	public static long fromString(String tsid) {
		return TsidConverter.fromString(tsid);
	}

	/**
	 * Returns a TSID string from a TSID number.
	 * 
	 * @param tsid a TSID number
	 * @return a TSID string
	 */
	public static String toString(long tsid) {
		return TsidConverter.toString(tsid);
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
	 * @return a TSID number
	 */
	public static long getTsid256() {
		return TimeIdCreatorHolder256.INSTANCE.create();
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
	 * @return a TSID number
	 */
	public static long getTsid1024() {
		return TimeIdCreatorHolder1024.INSTANCE.create();
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
	public static long getTsid4096() {
		return TimeIdCreatorHolder4096.INSTANCE.create();
	}

	/**
	 * Returns a TSID string.
	 * 
	 * The returning string is encoded to Crockford's base32.
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
	 * @return a TSID string
	 */
	public static String getTsidString256() {
		return TimeIdCreatorHolder256.INSTANCE.createString();
	}

	/**
	 * Returns a TSID string.
	 * 
	 * The returning string is encoded to Crockford's base32.
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
	 * @return a TSID string
	 */
	public static String getTsidString1024() {
		return TimeIdCreatorHolder1024.INSTANCE.createString();
	}

	/**
	 * Returns a TSID string.
	 * 
	 * The returning string is encoded to Crockford's base32.
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
	 * @return a TSID string
	 */
	public static String getTsidString4096() {
		return TimeIdCreatorHolder4096.INSTANCE.createString();
	}

	/**
	 * Returns a {@link TimeIdCreator}.
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
	 * @return a {@link TimeIdCreator}
	 */
	public static TimeIdCreator getTimeIdCreator256() {
		return new TimeIdCreator(null, NODE_LENGTH_256);
	}

	/**
	 * Returns a {@link TimeIdCreator}.
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
	 * @return a {@link TimeIdCreator}
	 */
	public static TimeIdCreator getTimeIdCreator1024() {
		return new TimeIdCreator(null, NODE_LENGTH_1024);
	}

	/**
	 * Returns a {@link TimeIdCreator}.
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
	 * @return a {@link TimeIdCreator}
	 */
	public static TimeIdCreator getTimeIdCreator4096() {
		return new TimeIdCreator(null, NODE_LENGTH_4096);
	}

	/**
	 * Returns a {@link TimeIdCreator}.
	 * 
	 * The node identifier is defined by parameter.
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
	 * @return a {@link TimeIdCreator}
	 */
	public static TimeIdCreator getTimeIdCreator256(int node) {
		return new TimeIdCreator(node, NODE_LENGTH_256);
	}

	/**
	 * Returns a {@link TimeIdCreator}.
	 * 
	 * The node identifier is defined by parameter.
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
	 * @return a {@link TimeIdCreator}
	 */
	public static TimeIdCreator getTimeIdCreator1024(int node) {
		return new TimeIdCreator(node, NODE_LENGTH_1024);
	}

	/**
	 * Returns a {@link TimeIdCreator}.
	 * 
	 * The node identifier is defined by parameter.
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
	 * @return a {@link TimeIdCreator}
	 */
	public static TimeIdCreator getTimeIdCreator4096(int node) {
		return new TimeIdCreator(node, NODE_LENGTH_4096);
	}

	/**
	 * Returns a {@link TimeIdCreator}.
	 * 
	 * If the node id passed by parameter is null, a random value will be used.
	 * 
	 * If the node bit length is null, the default value of 10 bits is used.
	 * 
	 * The node identifier bit length can be between 0 and 20.
	 * 
	 * The counter bit length depends on the node identifier bit length.
	 * 
	 * The maximum node identifier depends on it's bit length.
	 * 
	 * The maximum counter value also depends on it's bit length.
	 * 
	 * @param node       the node identifier (optional)
	 * @param nodeLength the node bit length (optional)
	 * @return a {@link TimeIdCreator}
	 */
	public static TimeIdCreator getTimeIdCreator(Integer node, Integer nodeLength) {
		return new TimeIdCreator(node, nodeLength);
	}

	private static class TimeIdCreatorHolder256 {
		static final TimeIdCreator INSTANCE = getTimeIdCreator256();
	}

	private static class TimeIdCreatorHolder1024 {
		static final TimeIdCreator INSTANCE = getTimeIdCreator1024();
	}

	private static class TimeIdCreatorHolder4096 {
		static final TimeIdCreator INSTANCE = getTimeIdCreator4096();
	}
}
