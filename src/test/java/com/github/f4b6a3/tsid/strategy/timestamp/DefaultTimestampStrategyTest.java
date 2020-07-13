package com.github.f4b6a3.tsid.strategy.timestamp;

import static org.junit.Assert.assertTrue;

import java.time.Instant;

import org.junit.Test;

import com.github.f4b6a3.tsid.strategy.timestamp.DefaultTimestampStrategy;
import com.github.f4b6a3.tsid.util.TsidTime;

public class DefaultTimestampStrategyTest {

	@Test
	public void testGetTimestamp() {
		DefaultTimestampStrategy strategy = new DefaultTimestampStrategy();

		long start = System.currentTimeMillis();
		long timestamp = strategy.getTimestamp();
		long middle = TsidTime.toUnixMilliseconds(timestamp);
		long end = System.currentTimeMillis();

		assertTrue(start <= middle);
		assertTrue(middle <= end);
	}

	@Test
	public void testGetTimestampWithCustomEpoch() {

		Instant customEpoch = Instant.parse("1999-12-31T00:00:00.000Z");
		DefaultTimestampStrategy strategy = new DefaultTimestampStrategy(customEpoch);

		long start = System.currentTimeMillis();
		long timestamp = strategy.getTimestamp();
		long middle = TsidTime.toUnixMilliseconds(timestamp, customEpoch.toEpochMilli());
		long end = System.currentTimeMillis();

		assertTrue(start <= middle);
		assertTrue(middle <= end);
	}
}
