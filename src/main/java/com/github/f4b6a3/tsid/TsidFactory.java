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

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Random;
import java.util.function.IntFunction;

import com.github.f4b6a3.tsid.internal.SettingsUtil;

import static com.github.f4b6a3.tsid.Tsid.TSID_EPOCH_MILLISECONDS;

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
 * - Node ID (0 to 20 bits)
 * 
 * - Counter (2 to 22 bits)
 * 
 * These are the DEFAULT Random component settings:
 * 
 * - Node identifier bit length: 10;
 * 
 * - Maximum node value: 2^10-1 = 1023;
 * 
 * - Counter bit length: 12;
 * 
 * - Maximum counter value: 2^12-1 = 4095.
 * 
 * The random component settings depend on the node identifier bit length. If
 * the node identifier bit length is 10, the counter bit length is limited to
 * 12. In this example, the maximum node identifier value is 2^10-1 = 1023 and
 * the maximum counter value is 2^12-1 = 4093. So the maximum TSIDs that can be
 * generated per millisecond per node is 4096.
 * 
 * If a system property `tsidcreator.node` or environment variable
 * `TSIDCREATOR_NODE` is defined, it's value is used as node identifier.
 * 
 * The default random generator uses {@link SecureRandom}.
 */
public final class TsidFactory {

	private long lastTime = -1;

	private int counter;
	private int counterMax;

	private final int node;

	private final int nodeMask;
	private final int counterMask;

	private final int nodeBitLength;
	private final int counterBitLength;

	private final long customEpoch;
	private final IntFunction<byte[]> randomFunction;

	private static final int RANDOM_MASK = 0x003fffff;
	private static final int RANDOM_BIT_LENGTH = 22;

	public static final int NODE_BIT_LENGTH_256 = 8;
	public static final int NODE_BIT_LENGTH_1024 = 10;
	public static final int NODE_BIT_LENGTH_4096 = 12;

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
	 * Returns a new factory for up to 256 nodes and 16384 ID/ms.
	 * 
	 * @return {@link TsidFactory}
	 */
	public static TsidFactory newInstance256() {
		return TsidFactory.builder().withNodeBitLength(NODE_BIT_LENGTH_256).build();
	}

	/**
	 * Returns a new factory for up to 256 nodes and 16384 ID/ms.
	 * 
	 * @param node the node identifier
	 * @return {@link TsidFactory}
	 */
	public static TsidFactory newInstance256(int node) {
		return TsidFactory.builder().withNodeBitLength(NODE_BIT_LENGTH_256).withNode(node).build();
	}

	/**
	 * Returns a new factory for up to 1024 nodes and 4096 ID/ms.
	 * 
	 * It is equivalent to {@code new TsidFactory()}.
	 * 
	 * @return {@link TsidFactory}
	 */
	public static TsidFactory newInstance1024() {
		return TsidFactory.builder().withNodeBitLength(NODE_BIT_LENGTH_1024).build();
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
		return TsidFactory.builder().withNodeBitLength(NODE_BIT_LENGTH_1024).withNode(node).build();
	}

	/**
	 * Returns a new factory for up to 4096 nodes and 1024 ID/ms.
	 * 
	 * @return {@link TsidFactory}
	 */
	public static TsidFactory newInstance4096() {
		return TsidFactory.builder().withNodeBitLength(NODE_BIT_LENGTH_4096).build();
	}

	/**
	 * Returns a new factory for up to 4096 nodes and 1024 ID/ms.
	 * 
	 * @param node the node identifier
	 * @return {@link TsidFactory}
	 */
	public static TsidFactory newInstance4096(int node) {
		return TsidFactory.builder().withNodeBitLength(NODE_BIT_LENGTH_4096).withNode(node).build();
	}

	/**
	 * It builds a generator with the given builder settings.
	 * 
	 * @param builder a builder instance
	 */
	private TsidFactory(Builder builder) {

		// setup the random generator
		this.randomFunction = builder.randomFunction != null ? builder.randomFunction
				: getRandomFunction(new SecureRandom());

		// setup the random and custom epoch
		this.customEpoch = builder.customEpoch != null ? builder.customEpoch : TSID_EPOCH_MILLISECONDS;

		// setup the node bit length
		this.nodeBitLength = builder.nodeBitLength != null ? builder.nodeBitLength : NODE_BIT_LENGTH_1024;
		if (nodeBitLength < 0 || nodeBitLength > 20) {
			throw new IllegalArgumentException("The node identifier bit length is out of the permited range: [0, 20]");
		}

		// setup constants that depend on the bit length
		this.counterBitLength = RANDOM_BIT_LENGTH - nodeBitLength;
		this.counterMask = RANDOM_MASK >>> nodeBitLength;
		this.nodeMask = RANDOM_MASK >>> counterBitLength;

		// finally, setup the node identifier
		if (builder.node != null) {
			// use the node id given by builder
			this.node = builder.node & this.nodeMask;
		} else if (SettingsUtil.getNodeIdentifier() != null) {
			// use the node id given by system property or environment variable
			this.node = SettingsUtil.getNodeIdentifier() & this.nodeMask;
		} else {
			// use a random node id from 0 to 0x3fffff (2^22 = 4,194,304).
			final byte[] bytes = randomFunction.apply(3);
			this.node = (((bytes[0] & 0x3f) << 16) | ((bytes[1] & 0xff) << 8) | (bytes[2] & 0xff)) & this.nodeMask;
		}
	}

	/**
	 * Returns a TSID.
	 * 
	 * @return a TSID.
	 */
	public synchronized Tsid create() {

		final long _time = getTime() << RANDOM_BIT_LENGTH;
		final long _node = (long) this.node << this.counterBitLength;
		final long _counter = (long) this.counter & this.counterMask;

		return new Tsid(_time | _node | _counter);
	}

	/**
	 * Returns the current time.
	 * 
	 * If the current time is equal to the previous time, the counter is incremented
	 * by one. Otherwise the counter is reset to a random value.
	 * 
	 * The maximum increment operation depends on the counter bit length. For
	 * example, if the counter bit length is 12, the maximum number of increment
	 * operations is 2^12 = 4096.
	 * 
	 * @return the current time
	 */
	private synchronized long getTime() {

		long time = System.currentTimeMillis();

		if (time == this.lastTime) {
			if (++this.counter >= this.counterMax) {
				time = nextTime(time);
				this.reset();
			}
		} else {
			this.reset();
		}

		this.lastTime = time;
		return time - this.customEpoch;
	}

	/**
	 * Stall the creator until the system clock catches up.
	 */
	private synchronized long nextTime(long time) {
		while (time == this.lastTime) {
			time = System.currentTimeMillis();
		}
		return time;
	}

	/**
	 * Resets the internal counter.
	 */
	private synchronized void reset() {
		// update the counter with a random value
		this.counter = this.getRandomCounter() & this.counterMask;

		// update the maximum incrementing value
		this.counterMax = this.counter | (0x00000001 << this.counterBitLength);
	}

	/**
	 * Returns a random counter value from 0 to 0x3fffff (2^22 = 4,194,304).
	 * 
	 * The counter maximum value depends on the node identifier bit length.
	 * 
	 * @return a number
	 */
	private synchronized int getRandomCounter() {
		if (this.counterBitLength <= 8) {
			final byte[] bytes = randomFunction.apply(1);
			return (bytes[0] & 0xff) & this.counterMask;
		} else if (this.counterBitLength <= 16) {
			final byte[] bytes = randomFunction.apply(2);
			return (((bytes[0] & 0xff) << 8) | (bytes[1] & 0xff)) & this.counterMask;
		} else {
			final byte[] bytes = randomFunction.apply(3);
			return (((bytes[0] & 0x3f) << 16) | ((bytes[1] & 0xff) << 8) | (bytes[2] & 0xff)) & this.counterMask;
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
		private Integer nodeBitLength;
		private Long customEpoch;
		private IntFunction<byte[]> randomFunction;

		/**
		 * Set the node identifier.
		 * 
		 * The range is 0 to 2^nodeBitLength-1.
		 * 
		 * @param nodeBitLength a number between 0 and 2^nodeBitLength-1.
		 * @return {@link Builder}
		 */
		public Builder withNode(Integer node) {
			this.node = node;
			return this;
		}

		/**
		 * Set the node identifier bit length within the range 0 to 20.
		 * 
		 * @param nodeBitLength a number between 0 and 20.
		 * @return {@link Builder}
		 */
		public Builder withNodeBitLength(Integer nodeBitLength) {
			this.nodeBitLength = nodeBitLength;
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
		 * @param random a {@link Random} generator
		 * @return {@link Builder}
		 */
		public Builder withRandom(Random random) {
			this.randomFunction = getRandomFunction(random);
			return this;
		}

		/**
		 * Set the random function.
		 * 
		 * The random function must return a byte array of a given length.
		 * 
		 * @param randomFunction a random function that returns a byte array
		 * @return {@link Builder}
		 */
		public Builder withRandomFunction(IntFunction<byte[]> randomFunction) {
			this.randomFunction = randomFunction;
			return this;
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

	/**
	 * It instantiates a function that returns a byte array of a given length.
	 * 
	 * @param random a {@link Random} generator
	 * @return a random function that returns a byte array of a given length
	 */
	protected static IntFunction<byte[]> getRandomFunction(Random random) {
		return (final int length) -> {
			final byte[] bytes = new byte[length];
			random.nextBytes(bytes);
			return bytes;
		};
	}
}
