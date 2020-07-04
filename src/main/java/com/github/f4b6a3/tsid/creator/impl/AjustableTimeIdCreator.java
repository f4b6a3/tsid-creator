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

package com.github.f4b6a3.tsid.creator.impl;

import com.github.f4b6a3.tsid.creator.AbstractTimeIdCreator;
import com.github.f4b6a3.tsid.util.TsidConverter;

/**
 * Factory that creates time sortable IDs (TSIDs).
 * 
 * The node identifier bit length is adjustable.
 * 
 * The random component settings depend depends on the node identifier bit
 * length. If the node identifier bit length is 10, the counter bit length is
 * limited to 12. In this example, the maximum node identifier value is 2^10 =
 * 1024 and the maximum counter value is 2^12 = 4096. So the maximum TSIDs that
 * can be generated per millisecond is about 4 thousand.
 * 
 */
public final class AjustableTimeIdCreator extends AbstractTimeIdCreator {

	private int nodeidLength = 0;
	private int counterLength = 0;

	private int nodeidTrunc = 0x00000000;
	private int counterTrunc = 0x00000000;

	/**
	 * Construct an instance of {@link AjustableTimeIdCreator}.
	 * 
	 * Many other fields are setup according to these two arguments.
	 * 
	 * @param nodeid       the node identifier
	 * @param nodeidLength the node bit length
	 */
	public AjustableTimeIdCreator(int nodeid, int nodeidLength) {
		super();
		this.setupRandomComponent(nodeid, nodeidLength);
	}

	/**
	 * Construct an instance of {@link AjustableTimeIdCreator}.
	 * 
	 * The default node identifier is random.
	 * 
	 * The default node identifier bit length is 10.
	 */
	public AjustableTimeIdCreator() {
		this(THREAD_LOCAL_RANDOM.get().nextInt(), DEFAULT_NODEID_LENGTH);
	}

	@Override
	public synchronized long create() {
		return this.create(this.nodeid);
	}

	public synchronized long create(int nodeid) {
		return create(nodeid & this.nodeidTrunc, this.counterLength);
	}

	public synchronized String createString(int nodeid) {
		return TsidConverter.toString(create(nodeid));
	}

	@Override
	protected synchronized void reset() {
		this.reset(this.counterTrunc, this.counterLength);
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
	 * 4. Update the node identifier truncation mask;
	 * 
	 * 5. Update the counter truncation mask;
	 * 
	 * 6. And truncate the node identifier to fit the bit length.
	 * 
	 * @param nodeid       the node identifier
	 * @param nodeidLength the node identifier bit length
	 * @throws IllegalArgumentException if the bit length is out of range [0, 20]
	 */
	private synchronized void setupRandomComponent(final int nodeid, final int nodeidLength) {

		// Check if the node identifier bit length between 0 and 20 (inclusive)
		if (nodeidLength < 0 || nodeidLength > 20) {
			throw new IllegalArgumentException("The node identifier bit length is out of the permited range: [0, 20]");
		}

		// Update the node identifier bit length
		this.nodeidLength = nodeidLength;

		// Update the counter bit length
		this.counterLength = RANDOMNESS_LENGTH - this.nodeidLength;

		// Update the node identifier truncation mask
		this.nodeidTrunc = RANDOMNESS_TRUNC >>> this.counterLength;

		// Update the counter truncation mask
		this.counterTrunc = RANDOMNESS_TRUNC >>> this.nodeidLength;

		// Truncate the node identifier to fit the bit length
		this.nodeid = nodeid & (RANDOMNESS_TRUNC >>> this.counterLength);
	}
}
