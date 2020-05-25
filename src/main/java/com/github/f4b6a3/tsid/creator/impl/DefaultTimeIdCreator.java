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

/**
 * Factory that creates time sortable IDs (TSIDs).
 * 
 * All 22 bits from the random component are dedicated for the counter sub-part.
 * 
 * The node identifier sub-part is disabled, i.e., it's bit length is ZERO.
 * 
 * The maximum TSIDs that can be generated per millisecond is about 4 million.
 * 
 * These are the random component settings:
 * 
 * - Node identifier bit length: N/A;
 * 
 * - Maximum node value: N/A;
 * 
 * - Counter bit length: 22;
 * 
 * - Maximum counter value: 2^22 = 4,194,304.
 */
public class DefaultTimeIdCreator extends AbstractTimeIdCreator {

	@Override
	public synchronized long create() {
		final long time = getTimestamp() << RANDOMNESS_LENGTH;
		return time | this.counter;
	}

	@Override
	protected synchronized void reset() {

		// Update the counter with a random value
		this.counter = THREAD_LOCAL_RANDOM.get().nextInt() & RANDOMNESS_TRUNC;

		// Update the maximum incrementing value
		this.incrementLimit = this.counter | (0x00000001 << RANDOMNESS_LENGTH);
	}
}
