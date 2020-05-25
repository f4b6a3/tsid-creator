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
 * 
 * All subclasses must implement these abstract methods:
 * 
 * - {@link @link AbstractTimeIdCreator#create()};
 * 
 * - {@link @link AbstractTimeIdCreator#reset()};
 */
public abstract class AbstractTimeIdCreator implements TimeIdCreator {

	protected int nodeid = 0;
	protected int counter = 0;
	protected int incrementLimit = 0;
	
	protected long previousTimestamp;

	protected TimestampStrategy timestampStrategy;

	public static final int TIMESTAMP_LENGTH = 42;
	public static final int RANDOMNESS_LENGTH = 22;

	public static final int DEFAULT_NODEID_LENGTH = 10;
	public static final int DEFAULT_COUNTER_LENGTH = 12;

	public static final int TIMESTAMP_TRUNC = 0xffc00000;
	public static final int RANDOMNESS_TRUNC = 0x003fffff;

	protected static final int DEFAULT_NODEID_TRUNC = 0x000003ff;
	protected static final int DEFAULT_COUNTER_TRUNC = 0x00000fff;

	protected static final ThreadLocal<Random> THREAD_LOCAL_RANDOM = ThreadLocal.withInitial(SecureRandom::new);

	protected static final String OVERRUN_MESSAGE = "The system overran the generator by requesting too many TSIDs.";

	/**
	 * Construct a {@link AbstractTimeIdCreator}.
	 */
	public AbstractTimeIdCreator() {
		this.timestampStrategy = new DefaultTimestampStrategy();
		this.reset();
	}

	/**
	 * Returns a TSID.
	 * 
	 * All subclasses must implement this method.
	 * 
	 * @return a TSID.
	 */
	public abstract long create();

	/**
	 * Returns a TSID string.
	 * 
	 * The returning string is encoded to Crockford's base32.
	 * 
	 * @return a TSID.
	 */
	public synchronized String createString() {
		return TsidConverter.toString(create());
	}
	
	/**
	 * Returns a TSID.
	 * 
	 * This method can be used by subclasses.
	 * 
	 * @param nodeid the node identifier
	 * @param counter the current counter value
	 * @param counterLength the counter bit length
	 * @return a TSID
	 */
	protected synchronized long create(int nodeid, int counter, int counterLength) {

		final long time = getTimestamp() << RANDOMNESS_LENGTH;
		final long node = nodeid << counterLength;

		return time | node | this.counter;
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
	 * 
	 * All subclasses must implement this method.
	 */
	protected abstract void reset();

	/**
	 * Set a custom epoch instead of the default.
	 *
	 * The default epoch is 2020-01-01 00:00:00Z (TSID epoch).
	 *
	 * @param customEpoch the custom epoch instant
	 * @return {@link AbstractTimeIdCreator}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public synchronized <T extends TimeIdCreator> T withCustomEpoch(Instant customEpoch) {
		this.timestampStrategy = new DefaultTimestampStrategy(customEpoch);
		return (T) this;
	}

	/**
	 * Replace the default strategy with another {@link TimestampStrategy}.
	 * 
	 * @param timestampStrategy a timestamp strategy
	 * @return {@link AbstractTimeIdCreator}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public synchronized <T extends TimeIdCreator> T withTimestampStrategy(TimestampStrategy timestampStrategy) {
		this.timestampStrategy = timestampStrategy;
		return (T) this;
	}
}
