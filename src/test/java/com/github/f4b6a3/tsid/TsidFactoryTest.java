package com.github.f4b6a3.tsid;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.function.IntFunction;

import org.junit.Test;

import com.github.f4b6a3.tsid.Tsid;

public class TsidFactoryTest {

	@Test
	public void testWithNodeBits() {
		final int randomBits = 22;
		// test all allowed values of node bits
		for (int i = 0; i <= 20; i++) {
			final int nodeBits = i;
			final int counterBits = randomBits - nodeBits;
			final int node = (1 << nodeBits) - 1; // max: 2^nodeBits - 1
			Tsid tsid = TsidFactory.builder().withNodeBits(nodeBits).withNode(node).build().create();
			int actual = (int) tsid.getRandom() >>> counterBits;
			assertEquals(node, actual);
		}
	}

	@Test
	public void testGetInstant() {

		Instant start = Instant.now();
		Tsid tsid = (new TsidFactory()).create();
		Instant middle = tsid.getInstant();
		Instant end = Instant.now();

		assertTrue(start.toEpochMilli() <= middle.toEpochMilli());
		assertTrue(middle.toEpochMilli() <= end.toEpochMilli());
	}

	@Test
	public void testGetUnixMilliseconds() {

		long start = System.currentTimeMillis();
		Tsid tsid = (new TsidFactory()).create();
		long middle = tsid.getInstant().toEpochMilli();
		long end = System.currentTimeMillis();

		assertTrue(start <= middle);
		assertTrue(middle <= end);
	}

	@Test
	public void testGetInstantWithCustomEpoch() {

		Instant customEpoch = Instant.parse("2015-10-23T00:00:00Z");

		Instant start = Instant.now();
		Tsid tsid = TsidFactory.builder().withCustomEpoch(customEpoch).build().create();
		Instant middle = tsid.getInstant(customEpoch);
		Instant end = Instant.now();

		assertTrue(start.toEpochMilli() <= middle.toEpochMilli());
		assertTrue(middle.toEpochMilli() <= end.toEpochMilli());
	}

	@Test
	public void testGetUnixMillisecondsWithCustomEpoch() {

		Instant customEpoch = Instant.parse("1984-01-01T00:00:00Z");

		long start = System.currentTimeMillis();
		Tsid tsid = TsidFactory.builder().withCustomEpoch(customEpoch).build().create();
		long middle = tsid.getInstant(customEpoch).toEpochMilli();
		long end = System.currentTimeMillis();

		assertTrue(start <= middle);
		assertTrue(middle <= end);
	}

	@Test
	public void testWithRandomFunctionReturningNull() throws InterruptedException {

		// a random function that returns NULL
		IntFunction<byte[]> randomFunction = (x) -> null;

		TsidFactory factory = TsidFactory.builder().withRandomFunction(randomFunction).build();

		final long mask = 0b111111111111; // counter bits: 12
		long lastCounter = factory.create().getRandom() & mask;

		// test it 5 times, waiting 1ms each time
		for (int i = 0; i < 5; i++) {
			long expected = (lastCounter + 1) & mask;
			long counter = factory.create().getRandom() & mask;
			assertEquals("The counter should be incremented when the ms changes", expected, counter);
			lastCounter = counter;
			Thread.sleep(1); // wait 1ms
		}
	}

	@Test
	public void testWithRandomFunctionReturningEmpty() throws InterruptedException {

		// a random function that returns EMPTY (length = 0)
		IntFunction<byte[]> randomFunction = (x) -> new byte[0];

		TsidFactory factory = TsidFactory.builder().withRandomFunction(randomFunction).build();

		final long mask = 0b111111111111; // counter bits: 12
		long lastCounter = factory.create().getRandom() & mask;

		// test it 5 times, waiting 1ms each time
		for (int i = 0; i < 5; i++) {
			long expected = (lastCounter + 1) & mask;
			long counter = factory.create().getRandom() & mask;
			assertEquals("The counter should be incremented when the ms changes", expected, counter);
			lastCounter = counter;
			Thread.sleep(1); // wait 1ms
		}
	}

	@Test
	public void testWithRandomFunctionReturningZero() throws InterruptedException {

		// a random function that returns a fixed array filled with ZEROS
		IntFunction<byte[]> randomFunction = (x) -> new byte[x];

		TsidFactory factory = TsidFactory.builder().withRandomFunction(randomFunction).build();

		final long mask = 0b111111111111; // counter bits: 12

		// test it 5 times, waiting 1ms each time
		for (int i = 0; i < 5; i++) {
			long expected = 0;
			long counter = factory.create().getRandom() & mask;
			assertEquals("The counter should be equal to ZERO when the ms changes", expected, counter);
			Thread.sleep(1); // wait 1ms
		}
	}

	@Test
	public void testWithRandomFunctionReturningNonZero() throws InterruptedException {

		// a random function that returns a fixed array
		byte[] fixed = { 0, 0, 127 };
		IntFunction<byte[]> randomFunction = (x) -> fixed;

		TsidFactory factory = TsidFactory.builder().withRandomFunction(randomFunction).build();

		final long mask = 0b111111111111; // counter bits: 12

		// test it 5 times, waiting 1ms each time
		for (int i = 0; i < 5; i++) {
			long expected = fixed[2];
			long counter = factory.create().getRandom() & mask;
			assertEquals("The counter should be equal to a fixed value when the ms changes", expected, counter);
			Thread.sleep(1); // wait 1ms
		}
	}
}
