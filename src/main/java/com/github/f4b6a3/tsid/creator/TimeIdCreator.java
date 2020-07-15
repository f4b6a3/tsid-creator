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

package com.github.f4b6a3.tsid.creator;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Random;

import com.github.f4b6a3.tsid.exception.TsidCreatorException;
import com.github.f4b6a3.tsid.strategy.TimestampStrategy;
import com.github.f4b6a3.tsid.strategy.timestamp.DefaultTimestampStrategy;
import com.github.f4b6a3.tsid.util.TsidSettings;
import com.github.f4b6a3.tsid.util.TsidConverter;

/**
 * Factory that creates time sortable IDs (TSIDs).
 * 
 * A TSID is a number that is formed by a creation time followed by random bits.
 * 
 * The TSID has 2 components:
 * 
 * - Time component (42 bits)
 * 
 * - Random component (22 bits)
 * 
 * The Random component may have 2 sub-parts:
 * 
 * - Node ID (0 to 20 bits)
 * 
 * - Counter (2 to 22 bits)
 * 
 * The counter bit length depends on the node identifier bit length. If the node
 * identifier bit length is 10, the counter bit length is limited to 12. In this
 * example, the maximum node identifier value is 2^10 = 1024 and the maximum
 * counter value is 2^12 = 4096. So the maximum TSIDs that can be generated per
 * millisecond is about 4 thousand.
 */
public final class TimeIdCreator {

	private int node = 0;
	private int counter = 0;
	private int counterMax = 0;
	private int counterLength = 0;
	private int counterMask = 0;

	private long previousTimestamp;

	private TimestampStrategy timestampStrategy;

	private static final int RANDOMNESS_LENGTH = 22;
	private static final int RANDOMNESS_MASK = 0x003fffff;

	private static final int DEFAULT_NODEID_LENGTH = 10;

	private static final String OVERRUN_MESSAGE = "The system overran the generator by requesting too many TSIDs.";

	private static final Random SECURE_RANDOM = new SecureRandom();

	/**
	 * It builds a generator with a RANDOM node identifier from 0 to 1,023.
	 * 
	 * Instances built by this constructor can generate up to 4,096 TSIDs per
	 * millisecond.
	 * 
	 * These are the random component settings:
	 * 
	 * - Node identifier bit length: 10;
	 * 
	 * - Maximum node value: 2^10 = 1024;
	 * 
	 * - Counter bit length: 12;
	 * 
	 * - Maximum counter value: 2^12 = 4096.
	 * 
	 */
	public TimeIdCreator() {
		this(null, null);
	}

	/**
	 * It builds a generator with a node identifier from 0 to 1,023 defined by input
	 * parameter.
	 * 
	 * Instances built by this constructor can generate up to 4,096 TSIDs per
	 * millisecond.
	 * 
	 * These are the random component settings:
	 * 
	 * - Node identifier bit length: 10;
	 * 
	 * - Maximum node value: 2^10 = 1024;
	 * 
	 * - Counter bit length: 12;
	 * 
	 * - Maximum counter value: 2^12 = 4096.
	 * 
	 * @param node the node identifier
	 */
	public TimeIdCreator(int node) {
		this(node, null);
	}

	/**
	 * It builds a generator with a node identifier and node identifier bit length
	 * defined by input parameters.
	 * 
	 * If the node id passed by parameter is null, a RANDOM value is be used. If a
	 * system property `tsidcreator.node` or environment variable `TSIDCREATOR_NODE`
	 * is defined, it's value is used instead of a random number.
	 * 
	 * If the node bit length is null, the default value of 10 bits is used.
	 * 
	 * The random component settings depend depends on the node identifier bit
	 * length. If the node identifier bit length is 10, the counter bit length is
	 * limited to 12. In this example, the maximum node identifier value is 2^10 =
	 * 1024 and the maximum counter value is 2^12 = 4096. So the maximum TSIDs that
	 * can be generated per millisecond is about 4 thousand.
	 * 
	 * @param node       the node identifier (optional)
	 * @param nodeLength the node bit length (optional)
	 */
	public TimeIdCreator(Integer node, Integer nodeLength) {
		final int _node = node != null ? node : getNodeIdentifier();
		final int _length = nodeLength != null ? nodeLength : DEFAULT_NODEID_LENGTH;
		this.setupRandomComponent(_node, _length);
		this.timestampStrategy = new DefaultTimestampStrategy();
	}

	private synchronized void setupRandomComponent(final int node, final int nodeLength) {
		// Check if the node identifier bit length between 0 and 20 (inclusive)
		if (nodeLength < 0 || nodeLength > 20) {
			throw new IllegalArgumentException("The node identifier bit length is out of the permited range: [0, 20]");
		}
		this.counterLength = RANDOMNESS_LENGTH - nodeLength;
		this.counterMask = RANDOMNESS_MASK >>> nodeLength;
		this.node = node & (RANDOMNESS_MASK >>> this.counterLength);
	}

	/**
	 * Returns a TSID.
	 * 
	 * @return a TSID.
	 * 
	 * @throws TsidCreatorException an overrun exception too many TSIDs are
	 *                              requested within the same millisecond.
	 */
	public synchronized long create() {
		final long _time = getTimestamp() << RANDOMNESS_LENGTH;
		final long _node = this.node << this.counterLength;
		final long _counter = this.counter & this.counterMask;

		return (_time | _node | _counter);
	}

	/**
	 * Returns a TSID string.
	 * 
	 * The returning string is encoded to Crockford's base32.
	 * 
	 * @return a TSID string.
	 * 
	 * @throws TsidCreatorException an overrun exception too many TSIDs are
	 *                              requested within the same millisecond.
	 */
	public synchronized String createString() {
		return TsidConverter.toString(create());
	}

	/**
	 * Returns the current timestamp.
	 * 
	 * If the current timestamp is equal to the previous timestamp, the counter is
	 * incremented by one. Otherwise the counter is reset to a random value.
	 * 
	 * @return timestamp
	 */
	private synchronized long getTimestamp() {

		final long timestamp = this.timestampStrategy.getTimestamp();

		if (timestamp == this.previousTimestamp) {
			this.increment();
		} else {
			this.reset();
		}

		this.previousTimestamp = timestamp;
		return timestamp;
	}

	/**
	 * Increment the counter sub-part of the TSID.
	 * 
	 * An exception is thrown when too many increment operations are made.
	 * 
	 * The maximum increment operation depends on the counter bit length.
	 * 
	 * For example, if the counter bit length is 12, the maximum number of increment
	 * operations is 2^12 = 4096. It means that an exception is thrown if the
	 * generator tries to generate more than 4096 TSIDs within the same millisecond.
	 * 
	 * @throws TsidCreatorException an overrun exception too many TSIDs are
	 *                              requested within the same millisecond.
	 */
	private synchronized void increment() {
		if (++this.counter >= this.counterMax) {
			this.reset();
			throw new TsidCreatorException(OVERRUN_MESSAGE);
		}
	}

	/**
	 * Resets the internal counter.
	 */
	private synchronized void reset() {
		// Update the counter with a random value
		this.counter = this.getRandomComponent() & this.counterMask;

		// Update the maximum incrementing value
		this.counterMax = this.counter | (0x00000001 << this.counterLength);
	}

	/**
	 * Returns a random number from 0 to 0x3fffff (2^22 = 4,194,304).
	 * 
	 * @return a number
	 */
	private synchronized int getRandomComponent() {
		byte[] bytes = new byte[3];
		SECURE_RANDOM.nextBytes(bytes);
		return ((bytes[0] & 0x0000003f) << 16) | ((bytes[1] & 0x000000ff) << 8) | (bytes[2] & 0x000000ff);
	}

	/**
	 * Get a node identifier from system property `tsidcreator.node` or environment
	 * variable `TSIDCREATOR_NODE`, in this order.
	 * 
	 * If no property or variable is found, it returns a random number.
	 * 
	 * @return a number
	 */
	private synchronized int getNodeIdentifier() {
		final Integer _node = TsidSettings.getNodeIdentifier();
		if (_node != null) {
			return _node;
		}
		return SECURE_RANDOM.nextInt();
	}

	/**
	 * Set a custom epoch instead of the default.
	 *
	 * The default epoch is 2020-01-01 00:00:00Z (TSID epoch).
	 *
	 * @param customEpoch the custom epoch instant
	 * @return {@link TimeIdCreator}
	 */
	public synchronized TimeIdCreator withCustomEpoch(Instant customEpoch) {
		this.timestampStrategy = new DefaultTimestampStrategy(customEpoch);
		return this;
	}

	/**
	 * Replace the default strategy with another {@link TimestampStrategy}.
	 * 
	 * @param timestampStrategy a timestamp strategy
	 * @return {@link TimeIdCreator}
	 */
	public synchronized TimeIdCreator withTimestampStrategy(TimestampStrategy timestampStrategy) {
		this.timestampStrategy = timestampStrategy;
		return this;
	}
}
