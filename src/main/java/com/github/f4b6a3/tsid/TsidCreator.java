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
 * A utility that generates Time-Sorted Unique Identifiers (TSID).
 * <p>
 * It is <b>highly recommended</b> to use this class in conjunction with the
 * "tsidcreator.node" system property or the "TSIDCREATOR_NODE" environment
 * variable. This is a simple way to avoid collisions between identifiers
 * produced by more than one machine or application instance.
 * <p>
 * A "node" as we call it in this library can be a physical machine, a virtual
 * machine, a container, a k8s pod, a running process, etc.
 */
public final class TsidCreator {

	private TsidCreator() {
	}

	/**
	 * Returns a new TSID.
	 * <p>
	 * The node ID is is set by defining the system property "tsidcreator.node" or
	 * the environment variable "TSIDCREATOR_NODE". One of them <b>should</b> be
	 * used to embed a machine ID in the generated TSID in order to avoid TSID
	 * collisions. If that property or variable is not defined, the node ID is
	 * chosen randomly.
	 * <p>
	 * The amount of nodes can be set by defining the system property
	 * "tsidcreator.node.count" or the environment variable
	 * "TSIDCREATOR_NODE_COUNT". That property or variable is used to adjust the
	 * minimum amount of bits to accommodate the node ID. If that property or
	 * variable is not defined, the default amount of nodes is 1024, which takes 10
	 * bits.
	 * <p>
	 * The amount of bits needed to accommodate the node ID is calculated by this
	 * pseudo-code formula: {@code node_bits = ceil(log(node_count)/log(2))}.
	 * <p>
	 * Random component settings:
	 * <ul>
	 * <li>Node bits: node_bits
	 * <li>Counter bits: 22-node_bits
	 * <li>Maximum node: 2^node_bits
	 * <li>Maximum counter: 2^(22-node_bits)
	 * </ul>
	 * <p>
	 * The time component can be 1 ms or more ahead of the system time when
	 * necessary to maintain monotonicity and generation speed.
	 * 
	 * @return a TSID
	 * @since 5.1.0
	 */
	public static Tsid getTsid() {
		return FactoryHolder.INSTANCE.create();
	}

	/**
	 * Returns a new TSID.
	 * <p>
	 * It supports up to 256 nodes.
	 * <p>
	 * It can generate up to 16,384 TSIDs per millisecond per node.
	 * <p>
	 * The node ID is is set by defining the system property "tsidcreator.node" or
	 * the environment variable "TSIDCREATOR_NODE". One of them <b>should</b> be
	 * used to embed a machine ID in the generated TSID in order to avoid TSID
	 * collisions. If that property or variable is not defined, the node ID is
	 * chosen randomly.
	 * 
	 * <p>
	 * Random component settings:
	 * <ul>
	 * <li>Node bits: 8
	 * <li>Counter bits: 14
	 * <li>Maximum node: 256 (2^8)
	 * <li>Maximum counter: 16,384 (2^14)
	 * </ul>
	 * <p>
	 * The time component can be 1 ms or more ahead of the system time when
	 * necessary to maintain monotonicity and generation speed.
	 * 
	 * @return a TSID
	 */
	public static Tsid getTsid256() {
		return Factory256Holder.INSTANCE.create();
	}

	/**
	 * Returns a new TSID.
	 * <p>
	 * It supports up to 1,024 nodes.
	 * <p>
	 * It can generate up to 4,096 TSIDs per millisecond per node.
	 * <p>
	 * The node ID is is set by defining the system property "tsidcreator.node" or
	 * the environment variable "TSIDCREATOR_NODE". One of them <b>should</b> be
	 * used to embed a machine ID in the generated TSID in order to avoid TSID
	 * collisions. If that property or variable is not defined, the node ID is
	 * chosen randomly.
	 * <p>
	 * Random component settings:
	 * <ul>
	 * <li>Node bits: 10
	 * <li>Counter bits: 12
	 * <li>Maximum node: 1,024 (2^10)
	 * <li>Maximum counter: 4,096 (2^12)
	 * </ul>
	 * <p>
	 * The time component can be 1 ms or more ahead of the system time when
	 * necessary to maintain monotonicity and generation speed.
	 * 
	 * @return a TSID
	 */
	public static Tsid getTsid1024() {
		return Factory1024Holder.INSTANCE.create();
	}

	/**
	 * Returns a new TSID.
	 * <p>
	 * It supports up to 4,096 nodes.
	 * <p>
	 * It can generate up to 1,024 TSIDs per millisecond per node.
	 * <p>
	 * The node ID is is set by defining the system property "tsidcreator.node" or
	 * the environment variable "TSIDCREATOR_NODE". One of them <b>should</b> be
	 * used to embed a machine ID in the generated TSID in order to avoid TSID
	 * collisions. If that property or variable is not defined, the node ID is
	 * chosen randomly.
	 * <p>
	 * Random component settings:
	 * <ul>
	 * <li>Node bits: 12
	 * <li>Counter bits: 10
	 * <li>Maximum node: 4,096 (2^12)
	 * <li>Maximum counter: 1,024 (2^10)
	 * </ul>
	 * <p>
	 * The time component can be 1 ms or more ahead of the system time when
	 * necessary to maintain monotonicity and generation speed.
	 * 
	 * @return a TSID number
	 */
	public static Tsid getTsid4096() {
		return Factory4096Holder.INSTANCE.create();
	}

	private static class FactoryHolder {
		static final TsidFactory INSTANCE = new TsidFactory();
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
