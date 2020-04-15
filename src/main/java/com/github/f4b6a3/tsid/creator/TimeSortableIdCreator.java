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

import java.time.Instant;

import com.github.f4b6a3.commons.util.RandomUtil;
import com.github.f4b6a3.tsid.exception.TsidCreatorException;
import com.github.f4b6a3.tsid.strategy.TimestampStrategy;
import com.github.f4b6a3.tsid.strategy.timestamp.DefaultTimestampStrategy;

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
 * By default the node identifier bit length is 0 and the counter bit length is
 * 22. So the node ID is disabled by default. All the bits of the random
 * component are dedicated to a counter that is started with a random value. The
 * maximum counter value by default is 2^22 = 4,194,304. So the maximum number
 * of TSIDs that can be generated within the same millisecond is about 4
 * million.
 * 
 * The counter bit length depends on the node identifier bit length. If the node
 * identifier bit length is 10, the counter bit length is limited to 12. In this
 * example, the maximum node identifier value is 2^10 = 1024 and the maximum
 * counter value is 2^12 = 4096. So the maximum TSIDs that can be generated per
 * millisecond is about 4 thousand.
 * 
 */
public class TimeSortableIdCreator {

	protected long previousTimestamp;

	protected int nodeid = 0;
	protected int nodeidLength = 0;

	protected int counter = 0;
	protected int counterLength = 0;

	protected int incrementLimit = 0;
	protected int counterTrunc = 0;

	protected TimestampStrategy timestampStrategy;

	public static final int TIMESTAMP_LENGTH = 42;
	public static final int RANDOMNESS_LENGTH = 22;
	public static final int IMPLICIT_NODEID_LENGTH = 10;

	protected static final int RANDOMNESS_MASK = 0x003fffff;

	protected static final String OVERRUN_MESSAGE = "The system overran the generator by requesting too many TSIDs.";

	/**
	 * Construct a {@link TimeSortableIdCreator}.
	 * 
	 * By default all the 22 bits from the random component are dedicated for the
	 * counter sub-part.
	 * 
	 * The node identifier sub-part is disabled, i.e., it's bit length is ZERO.
	 * 
	 * The maximum TSIDs that can be generated per millisecond is about 4 million.
	 */
	public TimeSortableIdCreator() {
		this.timestampStrategy = new DefaultTimestampStrategy();

		int nodeid = 0;
		int nodeidLength = 0; // Node identifier disabled
		this.setupRandomComponent(nodeid, nodeidLength);

		this.reset();
	}

	/**
	 * Returns a TSID.
	 * 
	 * @return a TSID.
	 */
	public synchronized long create() {

		final long time = getTimestamp() << RANDOMNESS_LENGTH;
		final long node = this.nodeid << this.counterLength;
		final long count = this.counter & this.counterTrunc;

		return time | node | count;
	}

	/**
	 * Returns the current timestamp.
	 * 
	 * If the current timestamp is equal to the previous timestamp, the counter is
	 * incremented by one. Otherwise the counter is reset to a random value.
	 * 
	 * @return timestamp
	 */
	protected synchronized long getTimestamp() {

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
	 * @throws TsidCreatorException if an overrun happens.
	 */
	protected synchronized void increment() {
		if (++this.counter == this.incrementLimit) {
			this.reset();
			throw new TsidCreatorException(OVERRUN_MESSAGE);
		}
	}

	/**
	 * Reset the counter sub-part of the TSID with a random value.
	 * 
	 * The new value is truncated to fit the counter bit length.
	 */
	protected synchronized void reset() {

		// Update the counter with a random value
		this.counter = RandomUtil.get().nextInt() & this.counterTrunc;

		// Update the maximum incrementing value
		this.incrementLimit = this.counter | (0x00000001 << this.counterLength);
	}

	/**
	 * Replace the default strategy with another {@link TimestampStrategy}.
	 * 
	 * @param timestampStrategy a timestamp strategy
	 * @return {@link TimeSortableIdCreator}
	 */
	@SuppressWarnings("unchecked")
	public synchronized <T extends TimeSortableIdCreator> T withTimestampStrategy(TimestampStrategy timestampStrategy) {
		this.timestampStrategy = timestampStrategy;
		return (T) this;
	}

	/**
	 * Set a fixed node identifier.
	 * 
	 * The IMPLICIT bit length for node identifiers is 10. The maximum number of
	 * nodes using this implicit bit length is 2^10 = 1,024.
	 * 
	 * The value is truncated to fit the bit length assumed by this method.
	 * 
	 * @param nodeid a fixed node identifier
	 * @return {@link TimeSortableIdCreator}
	 */
	@SuppressWarnings("unchecked")
	public synchronized <T extends TimeSortableIdCreator> T withNodeIdentifier(int nodeid) {
		this.setupRandomComponent(nodeid, IMPLICIT_NODEID_LENGTH);
		return (T) this;
	}

	/**
	 * Set a fixed node identifier with a specific bit length.
	 * 
	 * The value is truncated to fit the desired bit length.
	 * 
	 * The maximum bit length is 20, so the maximum number of nodes is 2^20 =
	 * 1,048,576.
	 * 
	 * @param nodeid a fixed node identifier
	 * @param length the fixed bit length between 0 and 20 (inclusive)
	 * @return {@link TimeSortableIdCreator}
	 */
	@SuppressWarnings("unchecked")
	public synchronized <T extends TimeSortableIdCreator> T withNodeIdentifier(int nodeid, int length) {
		this.setupRandomComponent(nodeid, length);
		return (T) this;
	}

	/**
	 * Set a custom epoch instead of the default.
	 * 
	 * The default epoch is 2020-01-01 00:00:00Z (TSID epoch).
	 * 
	 * @param customEpoch the custom epoch in Unix milliseconds
	 * @return {@link TimeSortableIdCreator}
	 */
	@SuppressWarnings("unchecked")
	public synchronized <T extends TimeSortableIdCreator> T withCustomEpoch(long customEpoch) {
		this.timestampStrategy = new DefaultTimestampStrategy(customEpoch);
		return (T) this;
	}

	/**
	 * Set a custom epoch instead of the default.
	 *
	 * The default epoch is 2020-01-01 00:00:00Z (TSID epoch).
	 *
	 * @param customEpoch the custom epoch instant
	 * @return {@link TimeSortableIdCreator}
	 */
	@SuppressWarnings("unchecked")
	public synchronized <T extends TimeSortableIdCreator> T withCustomEpoch(Instant customEpoch) {
		this.timestampStrategy = new DefaultTimestampStrategy(customEpoch.toEpochMilli());
		return (T) this;
	}

	/**
	 * This method adjusts the node identifier and counter bit lengths.
	 * 
	 * Tasks of this method:
	 * 
	 * 1. Update the note identifier;
	 * 
	 * 2. Update the node identifier bit length;
	 * 
	 * 3. Update the counter bit length;
	 * 
	 * 4. Truncate the node identifier to fit the bit length;
	 * 
	 * 5. And update the counter truncation mask.
	 * 
	 * @param nodeid       the node identifier
	 * @param nodeidLength the node identifier bit length
	 * @throws IllegalArgumentException if the bit length is out of range [0, 20]
	 */
	protected synchronized void setupRandomComponent(final int nodeid, final int nodeidLength) {

		// Check if the node identifier bit length between 0 and 20 (inclusive)
		if (nodeidLength < 0 || nodeidLength > 20) {
			throw new IllegalArgumentException("The node identifier bit length is out of the permited range: [0, 20]");
		}

		// Update the node identifier bit length
		this.nodeidLength = nodeidLength;

		// Update the counter bit length
		this.counterLength = RANDOMNESS_LENGTH - this.nodeidLength;

		// Truncate the node identifier to fit the bit length
		this.nodeid = nodeid & (RANDOMNESS_MASK >>> this.counterLength);

		// Update the counter truncation mask
		this.counterTrunc = RANDOMNESS_MASK >>> this.nodeidLength;
	}
}
