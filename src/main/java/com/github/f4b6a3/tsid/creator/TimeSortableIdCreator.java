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
import com.github.f4b6a3.tsid.timestamp.DefaultTimestampStrategy;
import com.github.f4b6a3.tsid.timestamp.TimestampStrategy;

public class TimeSortableIdCreator {

	protected long previousTimestamp;

	protected int nodeid = 0;
	protected int nodeidLength = 0;

	protected int counter = 0;
	protected int counterLength = 0;

	protected int counterMax = 0;
	protected int counterTrunc = 0;

	protected Instant customEpoch = null;

	protected static final String OVERRUN_MESSAGE = "The system overran the generator by requesting too many TSIDs.";

	protected TimestampStrategy timestampStrategy;

	protected static final int TIMESTAMP_LENGH = 42;
	protected static final int RANDOMNESS_LENGTH = 22;
	protected static final int DEFAULT_NODEID_LENGTH = 10;

	public TimeSortableIdCreator() {
		this.timestampStrategy = new DefaultTimestampStrategy();
		this.setupNodeIdentifier(0, 0);
		this.reset();
	}

	public synchronized long create() {

		final long time = getTimestamp() << RANDOMNESS_LENGTH;
		final long node = this.nodeid << this.counterLength;
		final long count = this.counter & this.counterTrunc;

		return time | node | count;
	}

	/**
	 * Return the current timestamp and resets or increments the random part.
	 * 
	 * @return timestamp
	 */
	protected synchronized long getTimestamp() {

		final long timestamp;

		if (this.customEpoch == null) {
			timestamp = this.timestampStrategy.getTimestamp();
		} else {
			timestamp = this.timestampStrategy.getTimestamp(customEpoch.toEpochMilli());
		}

		if (timestamp == this.previousTimestamp) {
			this.increment();
		} else {
			this.reset();
		}

		this.previousTimestamp = timestamp;
		return timestamp;
	}

	/**
	 * Increment the random part of the TSID.
	 * 
	 * An exception is thrown when too many increment operations are made.
	 * 
	 * The maximum increment operation depends on the counter bit length.
	 * 
	 * For example, if the counter bit length is 12, the maximum number of
	 * increment operations 2^12 = 4096. It means an exception is thrown if the
	 * generator tries to generate more than 4096 TSIDs within the same
	 * milliseconds.
	 * 
	 * @throws TsidCreatorException
	 *             if an overrun happens.
	 */
	protected synchronized void increment() {
		if (++this.counter == this.counterMax) {
			this.reset();
			throw new TsidCreatorException(OVERRUN_MESSAGE);
		}
	}

	/**
	 * Reset the random part of the TSID.
	 */
	protected synchronized void reset() {
		this.counter = SecureRandomLazyHolder.INSTANCE.nextInt() & this.counterTrunc;
		this.counterMax = this.counter | (0x00000001 << this.counterLength);
	}

	/**
	 * Used for changing the timestamp strategy.
	 * 
	 * @param timestampStrategy
	 *            a timestamp strategy
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
	 * The value is truncated to fit the default bit length.
	 * 
	 * The default bit length is 10, so the maximum number of nodes is 2^10 =
	 * 1,024.
	 * 
	 * @param value
	 *            the fixed value
	 * @return {@link TimeSortableIdCreator}
	 */
	@SuppressWarnings("unchecked")
	public synchronized <T extends TimeSortableIdCreator> T withNodeIdentifier(int value) {
		this.setupNodeIdentifier(value, DEFAULT_NODEID_LENGTH);
		return (T) this;
	}

	/**
	 * Set a fixed node identifier and a node identifier bit length.
	 * 
	 * The value is truncated to fit the desired bit length.
	 * 
	 * The maximum bit length is 20, so the maximum number of nodes is 2^20 =
	 * 1,048,576.
	 * 
	 * If the desired bit length is invalid, the default value is assumed.
	 * 
	 * @param nodeId
	 *            the node identifier
	 * @param length
	 *            the fixed bit length between 0 and 20 (inclusive)
	 * @return {@link TimeSortableIdCreator}
	 */
	@SuppressWarnings("unchecked")
	public synchronized <T extends TimeSortableIdCreator> T withNodeIdentifier(int nodeId, int length) {
		this.setupNodeIdentifier(nodeId, length);
		return (T) this;
	}

	/**
	 * 
	 * @param nodeId
	 *            the node identifier
	 * @param length
	 *            the fixed bit length between 0 and 20 (inclusive)
	 * @return {@link TimeSortableIdCreator}
	 */
	@SuppressWarnings("unchecked")
	public synchronized <T extends TimeSortableIdCreator> T withCustomEpoch(Instant customEpoch) {
		this.customEpoch = customEpoch;
		return (T) this;
	}

	protected synchronized void setupNodeIdentifier(int nodeid, int length) {

		// Update the node id bit length between 0 and 20 (inclusive)
		this.nodeidLength = (length < 0 || length > 20) ? DEFAULT_NODEID_LENGTH : length;

		// Update the counter bit length to embed the node id
		this.counterLength = RANDOMNESS_LENGTH - nodeidLength;

		// Truncate node id to fit the bit length
		this.nodeid = nodeid & (0xffffffff >>> nodeidLength);

		// Update the counter truncation mask
		this.counterTrunc = ~(0xffffffff << counterLength);
	}

	private static class SecureRandomLazyHolder {
		static final Random INSTANCE = new SecureRandom();
	}
}
