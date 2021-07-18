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

package com.github.f4b6a3.tsid.util;

import java.time.Instant;

public final class TsidTime {

	public static final long TSID_EPOCH_MILLISECONDS = Instant.parse("2020-01-01T00:00:00.000Z").toEpochMilli();

	private TsidTime() {
	}

	/**
	 * Get the number of milliseconds since 2020-01-01 00:00:00Z (TSID epoch).
	 * 
	 * @return the current timestamp
	 */
	public static long getCurrentTimestamp() {
		return System.currentTimeMillis() - TSID_EPOCH_MILLISECONDS;
	}

	/**
	 * Get the number of milliseconds since a custom epoch.
	 * 
	 * @param customEpoch the custom epoch milliseconds
	 * @return the current timestamp
	 */
	public static long getCurrentTimestamp(final long customEpoch) {
		return System.currentTimeMillis() - customEpoch;
	}

	/**
	 * Get the timestamp of given Unix Epoch milliseconds.
	 * 
	 * The value returned by this method is the number of milliseconds since
	 * 2020-01-01 00:00:00Z (TSID epoch).
	 * 
	 * @param unixMilliseconds the Unix Epoch milliseconds
	 * @return the timestamp
	 */
	public static long toTimestamp(final long unixMilliseconds) {
		return unixMilliseconds - TSID_EPOCH_MILLISECONDS;
	}

	/**
	 * Get the timestamp of a given instant with milliseconds precision.
	 *
	 * The value returned by this method is the number of milliseconds since
	 * 1920-01-01 00:00:00Z (TSID epoch).
	 * 
	 * @param instant an instant
	 * @return the timestamp
	 */
	public static long toTimestamp(final Instant instant) {
		return toTimestamp(instant.toEpochMilli());
	}

	/**
	 * Get the timestamp of given Unix Epoch milliseconds.
	 * 
	 * The value returned by this method is the number of milliseconds since a
	 * custom epoch.
	 * 
	 * @param unixMilliseconds the Unix Epoch milliseconds
	 * @param customEpoch      the custom epoch milliseconds
	 * @return
	 */
	public static long toTimestamp(final long unixMilliseconds, final long customEpoch) {
		return unixMilliseconds - customEpoch;
	}

	/**
	 * Get the timestamp of a given instant with milliseconds precision.
	 *
	 * The value returned by this method is the number of milliseconds since
	 * 1920-01-01 00:00:00Z (TSID epoch).
	 * 
	 * @param instant     an instant
	 * @param customEpoch the custom epoch instant
	 * @return the timestamp
	 */
	public static long toTimestamp(final Instant instant, final Instant customEpoch) {
		return toTimestamp(instant.toEpochMilli(), customEpoch.toEpochMilli());
	}

	/**
	 * Get the Unix Epoch milliseconds of a given timestmap.
	 * 
	 * The value returned by this method is the number of milliseconds since
	 * 1970-01-01 00:00:00Z (Unix epoch).
	 * 
	 * @param timestamp a timestamp
	 * @return the Unix milliseconds
	 */
	public static long toUnixMilliseconds(final long timestamp) {
		return timestamp + TSID_EPOCH_MILLISECONDS;
	}

	/**
	 * Get the Unix Epoch milliseconds of a given timestmap.
	 * 
	 * The value returned by this method is the number of milliseconds since
	 * 1970-01-01 00:00:00Z (Unix epoch).
	 * 
	 * @param timestamp   a timestamp
	 * @param customEpoch the custom epoch milliseconds
	 * @return
	 */
	public static long toUnixMilliseconds(final long timestamp, final long customEpoch) {
		return timestamp + customEpoch;
	}

	/**
	 * Get the instant of the given timestamp with milliseconds precision.
	 *
	 * @param timestamp a timestamp
	 * @return the instant
	 */
	public static Instant toInstant(final long timestamp) {
		return Instant.ofEpochMilli(toUnixMilliseconds(timestamp));
	}

	/**
	 * Get the instant of the given timestamp with milliseconds precision.
	 *
	 * @param timestamp   a timestamp
	 * @param customEpoch the custom epoch instant
	 * @return the instant
	 */
	public static Instant toInstant(final long timestamp, final Instant customEpoch) {
		return Instant.ofEpochMilli(toUnixMilliseconds(timestamp, customEpoch.toEpochMilli()));
	}
}
