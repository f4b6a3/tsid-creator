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

/**
 * Utility that provides methods for for extracting creation time and node
 * identifier from TSIDs.
 */
public class TsidUtil {

	protected static final long TIMESTAMP_LENGTH = 42;
	protected static final long RANDOMNESS_LENGTH = 22;
	public static final int DEFAULT_NODEID_LENGTH = 10;

	protected static final int RANDOMNESS_MASK = 0x003fffff;

	private TsidUtil() {
	}

	/**
	 * Returns the Unix milliseconds from a TSID.
	 * 
	 * @param tsid a TSID
	 * @return the Unix milliseconds
	 */
	public static long extractUnixMilliseconds(String tsid) {
		return extractUnixMilliseconds(TsidConverter.fromString(tsid));
	}

	/**
	 * Returns the Unix milliseconds from a TSID.
	 * 
	 * @param tsid a TSID
	 * @return the Unix milliseconds
	 */
	public static long extractUnixMilliseconds(long tsid) {
		final long timestamp = extractTimestamp(tsid);
		return TsidTimeUtil.toUnixMilliseconds(timestamp);
	}

	/**
	 * Returns the Unix milliseconds from a TSID, using a custom epoch.
	 * 
	 * The default epoch is 2020-01-01 00:00:00Z (TSID epoch).
	 * 
	 * @param tsid        a TSID
	 * @param customEpoch a custom epoch
	 * @return the Unix milliseconds
	 */
	public static long extractUnixMilliseconds(String tsid, long customEpoch) {
		return extractUnixMilliseconds(TsidConverter.fromString(tsid), customEpoch);
	}

	/**
	 * Returns the Unix milliseconds from a TSID, using a custom epoch.
	 * 
	 * The default epoch is 2020-01-01 00:00:00Z (TSID epoch).
	 * 
	 * @param tsid        a TSID
	 * @param customEpoch a custom epoch
	 * @return the Unix milliseconds
	 */
	public static long extractUnixMilliseconds(long tsid, long customEpoch) {
		final long timestamp = extractTimestamp(tsid);
		return TsidTimeUtil.toUnixMilliseconds(timestamp, customEpoch);
	}

	/**
	 * Returns the {@link Instant} from a TSID.
	 * 
	 * @param tsid a TSID
	 * @return the {@link Instant}.
	 */
	public static Instant extractInstant(String tsid) {
		return extractInstant(TsidConverter.fromString(tsid));
	}

	/**
	 * Returns the {@link Instant} from a TSID.
	 * 
	 * @param tsid a TSID
	 * @return the {@link Instant}.
	 */
	public static Instant extractInstant(long tsid) {
		final long unixMilliseconds = extractUnixMilliseconds(tsid);
		return Instant.ofEpochMilli(unixMilliseconds);
	}

	/**
	 * Returns the {@link Instant} from a TSID, using a custom epoch.
	 * 
	 * The default epoch is 2020-01-01 00:00:00Z (TSID epoch).
	 * 
	 * @param tsid        a TSID
	 * @param customEpoch a custom epoch
	 * @return the {@link Instant}
	 */
	public static Instant extractInstant(String tsid, Instant customEpoch) {
		return extractInstant(TsidConverter.fromString(tsid), customEpoch);
	}

	/**
	 * Returns the {@link Instant} from a TSID, using a custom epoch.
	 * 
	 * The default epoch is 2020-01-01 00:00:00Z (TSID epoch).
	 * 
	 * @param tsid        a TSID
	 * @param customEpoch a custom epoch
	 * @return the {@link Instant}
	 */
	public static Instant extractInstant(long tsid, Instant customEpoch) {
		final long unixMilliseconds = extractUnixMilliseconds(tsid, customEpoch.toEpochMilli());
		return Instant.ofEpochMilli(unixMilliseconds);
	}

	private static long extractTimestamp(long tsid) {
		return tsid >>> RANDOMNESS_LENGTH;
	}

	/**
	 * Returns the node identifier.
	 * 
	 * The DEFAULT bit length for node identifiers is 10. The maximum number of
	 * nodes using this default bit length is 2^10 = 1,024.
	 * 
	 * This method shouldn't be used for TSIDs created with different node
	 * identifier bit lengths.
	 * 
	 * @param tsid the TSID
	 * @return the node identifier
	 */
	public static int extractNodeIdentifier(long tsid) {
		return (int) extractNodeIdentifier(tsid, DEFAULT_NODEID_LENGTH);
	}

	/**
	 * Returns the node identifier.
	 * 
	 * A bit length for node identifiers is needed.
	 * 
	 * The bit length must be in the range [0, 20].
	 * 
	 * @param tsid         the TSID
	 * @param nodeidLength the node identifier bit length
	 * @return the node identifier
	 * @throws IllegalArgumentException if the bit length is out of range [0, 20]
	 */
	public static int extractNodeIdentifier(long tsid, int nodeidLength) {

		// Check if the node identifier bit length between 0 and 20 (inclusive)
		if (nodeidLength < 0 || nodeidLength > 20) {
			throw new IllegalArgumentException("The node identifier bit length is out of the permited range: [0, 20]");
		}

		return (int) (tsid & RANDOMNESS_MASK) >>> (RANDOMNESS_LENGTH - nodeidLength);
	}
}
