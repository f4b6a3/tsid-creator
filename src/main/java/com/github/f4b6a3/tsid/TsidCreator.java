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

/**
 * A class that generates Time Sortable Identifiers (TSID).
 */
public final class TsidCreator {

	private TsidCreator() {
	}

	/**
	 * Returns a TSID.
	 * <p>
	 * It supports up to 256 nodes.
	 * <p>
	 * It can generate up to 16,384 TSIDs per millisecond per node.
	 * <p>
	 * The time component can be 1 ms or more ahead of the system time when
	 * necessary to maintain monotonicity and generation speed.
	 * <p>
	 * The node identifier is <b>random</b>, unless there's a system property
	 * `tsidcreator.node` or an environment variable `TSIDCREATOR_NODE` is defined.
	 * One of them <b>should</b> be used to embed a machine ID in the generated TSID
	 * in order to avoid TSID collisions.
	 * <p>
	 * Random component settings:
	 * <ul>
	 * <li>Node bits: 8
	 * <li>Counter bits: 14
	 * <li>Maximum node: 256 (2^8)
	 * <li>Maximum counter: 16,384 (2^14)
	 * </ul>
	 * 
	 * @return a TSID
	 */
	public static Tsid getTsid256() {
		return Factory256Holder.INSTANCE.create();
	}

	/**
	 * Returns a TSID.
	 * <p>
	 * It supports up to 1,024 nodes.
	 * <p>
	 * It can generate up to 4,096 TSIDs per millisecond per node.
	 * <p>
	 * The time component can be 1 ms or more ahead of the system time when
	 * necessary to maintain monotonicity and generation speed.
	 * <p>
	 * The node identifier is <b>random</b>, unless there's a system property
	 * `tsidcreator.node` or an environment variable `TSIDCREATOR_NODE` is defined.
	 * One of them <b>should</b> be used to embed a machine ID in the generated TSID
	 * in order to avoid TSID collisions.
	 * <p>
	 * Random component settings:
	 * <ul>
	 * <li>Node bits: 10
	 * <li>Counter bits: 12
	 * <li>Maximum node: 1,024 (2^10)
	 * <li>Maximum counter: 4,096 (2^12)
	 * </ul>
	 * 
	 * @return a TSID
	 */
	public static Tsid getTsid1024() {
		return Factory1024Holder.INSTANCE.create();
	}

	/**
	 * Returns a TSID.
	 * <p>
	 * It supports up to 4,096 nodes.
	 * <p>
	 * It can generate up to 1,024 TSIDs per millisecond per node.
	 * <p>
	 * The time component can be 1 ms or more ahead of the system time when
	 * necessary to maintain monotonicity and generation speed.
	 * <p>
	 * The node identifier is <b>random</b>, unless there's a system property
	 * `tsidcreator.node` or an environment variable `TSIDCREATOR_NODE` is defined.
	 * One of them <b>should</b> be used to embed a machine ID in the generated TSID
	 * in order to avoid TSID collisions.
	 * <p>
	 * Random component settings:
	 * <ul>
	 * <li>Node bits: 12
	 * <li>Counter bits: 10
	 * <li>Maximum node: 4,096 (2^12)
	 * <li>Maximum counter: 1,024 (2^10)
	 * </ul>
	 * 
	 * @return a TSID number
	 */
	public static Tsid getTsid4096() {
		return Factory4096Holder.INSTANCE.create();
	}

	private static class Factory256Holder {
		static final TsidFactory INSTANCE = TsidFactory.newInstance256();
	}

	private static class Factory1024Holder {
		static final TsidFactory INSTANCE = TsidFactory.newInstance1024();
	}

	private static class Factory4096Holder {
		static final TsidFactory INSTANCE = TsidFactory.newInstance4096();
	}
}
