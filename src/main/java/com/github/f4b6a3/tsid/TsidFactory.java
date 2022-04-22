/*
 * MIT License
 * 
 * Copyright (c) 2020-2022 Fabio Lima
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

import java.security.SecureRandom;
import java.time.Clock;
import java.time.Instant;
import java.util.Random;
import java.util.function.IntFunction;

import com.github.f4b6a3.tsid.internal.SettingsUtil;

import static com.github.f4b6a3.tsid.Tsid.RANDOM_BITS;
import static com.github.f4b6a3.tsid.Tsid.RANDOM_MASK;

/**
 * Factory that creates Time Sortable IDs (TSIDs).
 * 
 * A TSID is a number that is formed by a creation time followed by random bits.
 * 
 * The TSID has 2 components:
 * 
 * - Time component (42 bits)
 * 
 * - Random component (22 bits)
 * 
 * The Random component has 2 sub-parts:
 * 
 * - Node (0 to 20 bits)
 * 
 * - Counter (2 to 22 bits)
 * 
 * These are the DEFAULT Random component settings:
 * 
 * - Node bits: 10;
 * 
 * - Maximum node value: 2^10-1 = 1023;
 * 
 * - Counter bits: 12;
 * 
 * - Maximum counter value: 2^12-1 = 4095.
 * 
 * The random component settings depend on the node bits. If the node bits are
 * 10, the counter bits are limited to 12. In this example, the maximum node
 * value is 2^10-1 = 1023 and the maximum counter value is 2^12-1 = 4093. So the
 * maximum TSIDs that can be generated per millisecond per node is 4096.
 * 
 * If a system property `tsidcreator.node` or environment variable
 * `TSIDCREATOR_NODE` is defined, it's value is used as node identifier.
 * 
 * The default random generator uses {@link SecureRandom}.
 */
public final class TsidFactory {

	private int counter;
	private long lastTime;

	private final int node;

	private final int nodeBits;
	private final int counterBits;

	private final int nodeMask;
	private final int counterMask;

	private final long customEpoch;

	private final int randomBytes;
	private final IntFunction<byte[]> randomFunction;

	public static final int NODE_BITS_256 = 8;
	public static final int NODE_BITS_1024 = 10;
	public static final int NODE_BITS_4096 = 12;

	// Used to preserve monotonicity when the system clock is
	// adjusted by NTP after a small clock drift or when the
	// system clock jumps back by 1 second due to leap second.
	protected static final int CLOCK_DRIFT_TOLERANCE = 10_000;

	protected final Clock clock; // for tests
	protected static final Clock DEFAULT_CLOCK = Clock.systemUTC();

	/**
	 * It builds a generator with a RANDOM node identifier from 0 to 1,023.
	 * 
	 * If a system property `tsidcreator.node` or environment variable
	 * `TSIDCREATOR_NODE` is defined, it's value is used as node identifier.
	 *
	 * Instances built by this constructor can generate up to 4,096 TSIDs per
	 * millisecond per node.
	 */
	public TsidFactory() {
		this(builder());
	}

	/**
	 * It builds a generator with a given node identifier from 0 to 1,023.
	 * 
	 * Instances built by this constructor can generate up to 4,096 TSIDs per
	 * millisecond per node.
	 * 
	 * @param node the node identifier
	 */
	public TsidFactory(int node) {
		this(builder().withNode(node));
	}

	/**
	 * It builds a generator with the given builder.
	 * 
	 * @param builder a builder instance
	 */
	private TsidFactory(Builder builder) {

		// setup node bits, custom epoch and random function
		this.nodeBits = builder.getNodeBits();
		this.customEpoch = builder.getCustomEpoch();
		this.randomFunction = builder.getRandomFunction();
		this.clock = builder.getClock();

		// setup constants that depend on node bits
		this.counterBits = RANDOM_BITS - nodeBits;
		this.counterMask = RANDOM_MASK >>> nodeBits;
		this.nodeMask = RANDOM_MASK >>> counterBits;

		// setup how many bytes to get from the random function
		this.randomBytes = ((this.counterBits - 1) / 8) + 1;

		// setup the node identifier
		this.node = builder.getNode() & nodeMask;

		// finally, initialize internal state
		this.counter = getRandomCounter();
		this.lastTime = DEFAULT_CLOCK.millis();
	}

	/**
	 * Returns a new factory for up to 256 nodes and 16384 ID/ms.
	 * 
	 * @return {@link TsidFactory}
	 */
	public static TsidFactory newInstance256() {
		return TsidFactory.builder().withNodeBits(NODE_BITS_256).build();
	}

	/**
	 * Returns a new factory for up to 256 nodes and 16384 ID/ms.
	 * 
	 * @param node the node identifier
	 * @return {@link TsidFactory}
	 */
	public static TsidFactory newInstance256(int node) {
		return TsidFactory.builder().withNodeBits(NODE_BITS_256).withNode(node).build();
	}

	/**
	 * Returns a new factory for up to 1024 nodes and 4096 ID/ms.
	 * 
	 * It is equivalent to {@code new TsidFactory()}.
	 * 
	 * @return {@link TsidFactory}
	 */
	public static TsidFactory newInstance1024() {
		return TsidFactory.builder().withNodeBits(NODE_BITS_1024).build();
	}

	/**
	 * Returns a new factory for up to 1024 nodes and 4096 ID/ms.
	 * 
	 * It is equivalent to {@code new TsidFactory(int)}.
	 * 
	 * @param node the node identifier
	 * @return {@link TsidFactory}
	 */
	public static TsidFactory newInstance1024(int node) {
		return TsidFactory.builder().withNodeBits(NODE_BITS_1024).withNode(node).build();
	}

	/**
	 * Returns a new factory for up to 4096 nodes and 1024 ID/ms.
	 * 
	 * @return {@link TsidFactory}
	 */
	public static TsidFactory newInstance4096() {
		return TsidFactory.builder().withNodeBits(NODE_BITS_4096).build();
	}

	/**
	 * Returns a new factory for up to 4096 nodes and 1024 ID/ms.
	 * 
	 * @param node the node identifier
	 * @return {@link TsidFactory}
	 */
	public static TsidFactory newInstance4096(int node) {
		return TsidFactory.builder().withNodeBits(NODE_BITS_4096).withNode(node).build();
	}

	/**
	 * Returns a TSID.
	 * 
	 * @return a TSID.
	 */
	public synchronized Tsid create() {

		final long _time = getTime() << RANDOM_BITS;
		final long _node = (long) this.node << this.counterBits;
		final long _counter = (long) this.counter & this.counterMask;

		return new Tsid(_time | _node | _counter);
	}

	/**
	 * Returns the current time.
	 * 
	 * If the current time is equal to the previous time, the counter is incremented
	 * by one. Otherwise the counter is reset to a random value.
	 * 
	 * The maximum number of increment operations depend on the counter bits. For
	 * example, if the counter bits is 12, the maximum number of increment
	 * operations is 2^12 = 4096.
	 * 
	 * @return the current time
	 */
	private synchronized long getTime() {

		long time = clock.millis();

		// Check if the current time is the same as the previous time or has moved
		// backwards after a small system clock adjustment or after a leap second.
		// Drift tolerance = (previous_time - 10s) < current_time <= previous_time
		if ((time > this.lastTime - CLOCK_DRIFT_TOLERANCE) && (time <= this.lastTime)) {
			this.counter++;
			// Carry is 1 if an overflow occurs after ++.
			int carry = this.counter >>> this.counterBits;
			this.counter = this.counter & this.counterMask;
			time = this.lastTime + carry; // increment time
		} else {
			// If the system clock has advanced as expected,
			// simply reset the counter to a new random value.
			this.counter = this.getRandomCounter();
		}

		// save current time
		this.lastTime = time;

		// adjust to the custom epoch
		return time - this.customEpoch;
	}

	/**
	 * Returns a random counter value from 0 to 0x3fffff (2^22-1 = 4,194,303).
	 * 
	 * The counter maximum value depends on the node identifier bits. For example,
	 * if the node identifier has 10 bits, the counter has 12 bits.
	 * 
	 * If the random function returns NULL or EMPTY, the counter is simply
	 * incremented and returned.
	 * 
	 * @return a number
	 */
	private synchronized int getRandomCounter() {

		final byte[] bytes = randomFunction.apply(this.randomBytes);

		if (bytes == null || bytes.length == 0) {
			// if the function return is NULL or EMPTY:
			// simply increment and return the counter
			return ++this.counter & this.counterMask;
		}

		switch (bytes.length) {
		case 1:
			return (bytes[0] & 0xff) & this.counterMask;
		case 2:
			return (((bytes[0] & 0xff) << 8) | (bytes[1] & 0xff)) & this.counterMask;
		default:
			return (((bytes[0] & 0xff) << 16) | ((bytes[1] & 0xff) << 8) | (bytes[2] & 0xff)) & this.counterMask;
		}
	}

	/**
	 * Returns a builder object.
	 * 
	 * It is used to build a custom {@link TsidFactory}.
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * A builder class.
	 * 
	 * It is used to setup a custom {@link TsidFactory}.
	 */
	public static class Builder {

		private Integer node;
		private Integer nodeBits;
		private Long customEpoch;
		private IntFunction<byte[]> randomFunction;
		private Clock clock;

		/**
		 * Set the node identifier.
		 * 
		 * The range is 0 to 2^nodeBits-1.
		 * 
		 * @param node a number between 0 and 2^nodeBits-1.
		 * @return {@link Builder}
		 */
		public Builder withNode(Integer node) {
			this.node = node;
			return this;
		}

		/**
		 * Set the node identifier bits within the range 0 to 20.
		 * 
		 * @param nodeBits a number between 0 and 20.
		 * @return {@link Builder}
		 */
		public Builder withNodeBits(Integer nodeBits) {
			if (nodeBits < 0 || nodeBits > 20) {
				throw new IllegalArgumentException("Node bits out of range: [0, 20]");
			}
			this.nodeBits = nodeBits;
			return this;
		}

		/**
		 * Set the custom epoch.
		 * 
		 * @param customEpoch an instant that represents the custom epoch.
		 * @return {@link Builder}
		 */
		public Builder withCustomEpoch(Instant customEpoch) {
			this.customEpoch = customEpoch.toEpochMilli();
			return this;
		}

		/**
		 * Set the random generator.
		 * 
		 * The random generator is used to create a random function that is used to
		 * reset the counter when the millisecond changes.
		 * 
		 * @param random a {@link Random} generator
		 * @return {@link Builder}
		 */
		public Builder withRandom(Random random) {
			this.randomFunction = (final int length) -> {
				final byte[] bytes = new byte[length];
				random.nextBytes(bytes);
				return bytes;
			};
			return this;
		}

		/**
		 * Set the random function.
		 * 
		 * The random function must return a byte array of a given length.
		 * 
		 * The random function is used to reset the counter when the millisecond
		 * changes.
		 * 
		 * Despite its name, the random function MAY return a fixed value, for example,
		 * if your app requires the counter to be reset to ZERO whenever the millisecond
		 * changes, like Twitter Snowflakes, this function should return an array filled
		 * with ZEROS.
		 * 
		 * If the returned value is NULL or EMPTY, the factory ignores it and just
		 * increments the counter when the millisecond changes, for example, when your
		 * app requires the counter to always be incremented, no matter if the
		 * millisecond has changed or not, like Discord Snowflakes.
		 * 
		 * @param randomFunction a random function that returns a byte array
		 * @return {@link Builder}
		 */
		public Builder withRandomFunction(IntFunction<byte[]> randomFunction) {
			this.randomFunction = randomFunction;
			return this;
		}

		/**
		 * Set the clock to be used in tests.
		 * 
		 * @param clock a clock
		 * @return {@link Builder}
		 */
		public Builder withClock(Clock clock) {
			this.clock = clock;
			return this;
		}

		public Integer getNode() {

			// 2^22 - 1 = 4,194,303
			final int mask = 0x3fffff;

			if (this.node == null) {
				if (SettingsUtil.getNode() != null) {
					// use system property and environment variable
					this.node = SettingsUtil.getNode();
				} else {
					// use random node id from 0 to 4,194,303
					final byte[] bytes = this.getRandomFunction().apply(3);
					if (bytes != null && bytes.length == 3) {
						this.node = ((bytes[0] & 0xff) << 16) | ((bytes[1] & 0xff) << 8) | (bytes[2] & 0xff);
					} else {
						// use fallback random generator
						this.node = (new SecureRandom()).nextInt();
					}
				}
			}

			return this.node & mask;
		}

		public Integer getNodeBits() {
			if (this.nodeBits == null) {
				this.nodeBits = TsidFactory.NODE_BITS_1024; // 10 bits
			}
			return this.nodeBits;
		}

		public Long getCustomEpoch() {
			if (this.customEpoch == null) {
				this.customEpoch = Tsid.TSID_EPOCH; // 2020-01-01
			}
			return this.customEpoch;
		}

		public IntFunction<byte[]> getRandomFunction() {
			if (this.randomFunction == null) {
				this.withRandom(new SecureRandom());
			}
			return this.randomFunction;
		}

		public Clock getClock() {
			if (this.clock == null) {
				this.withClock(DEFAULT_CLOCK);
			}
			return this.clock;
		}

		/**
		 * Returns a custom TSID factory with the builder settings.
		 * 
		 * @return {@link TsidFactory}
		 */
		public TsidFactory build() {
			return new TsidFactory(this);
		}
	}
}
