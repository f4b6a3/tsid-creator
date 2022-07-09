package com.github.f4b6a3.tsid;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Random;
import java.util.SplittableRandom;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntFunction;
import java.util.function.IntSupplier;

import org.junit.Test;

public class TsidFactoryTest {

	private static final int LOOP_MAX = 1_000;

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
		long middle = tsid.getUnixMilliseconds();
		long end = System.currentTimeMillis();

		assertTrue(start <= middle);
		assertTrue(middle <= end);
	}

	@Test
	public void testGetInstantWithClock() {

		final long bound = (long) Math.pow(2, 42);

		for (int i = 0; i < LOOP_MAX; i++) {

			// instantiate a factory with a Clock that returns a fixed value
			final long random = ThreadLocalRandom.current().nextLong(bound);
			final long millis = random + Tsid.TSID_EPOCH; // avoid dates before 2020
			Clock clock = Clock.fixed(Instant.ofEpochMilli(millis), ZoneOffset.UTC); // simulate a frozen clock
			IntFunction<byte[]> randomFunction = x -> new byte[x]; // force to reinitialize the counter to ZERO
			TsidFactory factory = TsidFactory.builder().withClock(clock).withRandomFunction(randomFunction).build();

			long result = factory.create().getInstant().toEpochMilli();
			assertEquals("The current instant is incorrect", millis, result);
		}
	}

	@Test
	public void testGetUnixMillisecondsWithClock() {

		final long bound = (long) Math.pow(2, 42);

		for (int i = 0; i < LOOP_MAX; i++) {

			// instantiate a factory with a Clock that returns a fixed value
			final long random = ThreadLocalRandom.current().nextLong(bound);
			final long millis = random + Tsid.TSID_EPOCH; // avoid dates before 2020
			Clock clock = Clock.fixed(Instant.ofEpochMilli(millis), ZoneOffset.UTC); // simulate a frozen clock
			IntFunction<byte[]> randomFunction = x -> new byte[x]; // force to reinitialize the counter to ZERO
			TsidFactory factory = TsidFactory.builder().withClock(clock).withRandomFunction(randomFunction).build();

			long result = factory.create().getUnixMilliseconds();
			assertEquals("The current millisecond is incorrect", millis, result);
		}
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
	public void testWithNode() {
		{
			for (int i = 0; i <= 20; i++) {
				int bits = TsidFactory.NODE_BITS_1024;
				int shif = Tsid.RANDOM_BITS - bits;
				int mask = (1 << bits - 1);
				int node = ThreadLocalRandom.current().nextInt() & mask;
				TsidFactory factory = new TsidFactory(node);
				assertEquals(node, (factory.create().getRandom() >>> shif) & mask);
			}
		}
		{
			for (int i = 0; i <= 20; i++) {
				int bits = TsidFactory.NODE_BITS_1024;
				int shif = Tsid.RANDOM_BITS - bits;
				int mask = (1 << bits - 1);
				int node = ThreadLocalRandom.current().nextInt() & mask;
				TsidFactory factory = TsidFactory.builder().withNode(node).build();
				assertEquals(node, (factory.create().getRandom() >>> shif) & mask);
			}
		}
		{
			for (int i = 0; i <= 20; i++) {
				int bits = TsidFactory.NODE_BITS_256;
				int shif = Tsid.RANDOM_BITS - bits;
				int mask = (1 << bits - 1);
				int node = ThreadLocalRandom.current().nextInt() & mask;
				TsidFactory factory = TsidFactory.newInstance256(node);
				assertEquals(node, (factory.create().getRandom() >>> shif) & mask);
			}
		}
		{
			for (int i = 0; i <= 20; i++) {
				int bits = TsidFactory.NODE_BITS_1024;
				int shif = Tsid.RANDOM_BITS - bits;
				int mask = (1 << bits - 1);
				int node = ThreadLocalRandom.current().nextInt() & mask;
				TsidFactory factory = TsidFactory.newInstance1024(node);
				assertEquals(node, (factory.create().getRandom() >>> shif) & mask);
			}
		}
		{
			for (int i = 0; i <= 20; i++) {
				int bits = TsidFactory.NODE_BITS_4096;
				int shif = Tsid.RANDOM_BITS - bits;
				int mask = (1 << bits - 1);
				int node = ThreadLocalRandom.current().nextInt() & mask;
				TsidFactory factory = TsidFactory.newInstance4096(node);
				assertEquals(node, (factory.create().getRandom() >>> shif) & mask);
			}
		}
	}

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
	public void testWithRandom() {
		Random random = new Random();
		TsidFactory factory = TsidFactory.builder().withRandom(random).build();
		assertNotNull(factory.create());
	}

	@Test
	public void testWithRandomNull() {
		TsidFactory factory = TsidFactory.builder().withRandom(null).build();
		assertNotNull(factory.create());
	}

	@Test
	public void testWithRandomFunction() {
		{
			SplittableRandom random = new SplittableRandom();
			IntSupplier function = () -> random.nextInt();
			TsidFactory factory = TsidFactory.builder().withRandomFunction(function).build();
			assertNotNull(factory.create());
		}
		{
			IntFunction<byte[]> function = (length) -> {
				byte[] bytes = new byte[length];
				ThreadLocalRandom.current().nextBytes(bytes);
				return bytes;
			};
			TsidFactory factory = TsidFactory.builder().withRandomFunction(function).build();
			assertNotNull(factory.create());
		}
	}

	@Test
	public void testWithRandomFunctionNull() {
		{
			TsidFactory factory = TsidFactory.builder().withRandomFunction((IntSupplier) null).build();
			assertNotNull(factory.create());
		}
		{
			TsidFactory factory = TsidFactory.builder().withRandomFunction((IntFunction<byte[]>) null).build();
			assertNotNull(factory.create());
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
			Thread.sleep(1); // wait 1ms
			long expected = 0;
			long counter = factory.create().getRandom() & mask;
			assertEquals("The counter should be equal to ZERO when the ms changes", expected, counter);
		}
	}

	@Test
	public void testWithRandomFunctionReturningNonZero() throws InterruptedException {

		// a random function that returns a fixed array
		byte[] fixed = { 0, 0, 0, 0, 0, 0, 0, 127 };
		IntFunction<byte[]> randomFunction = (x) -> fixed;

		TsidFactory factory = TsidFactory.builder().withRandomFunction(randomFunction).build();

		final long mask = 0b111111111111; // counter bits: 12

		// test it 5 times, waiting 1ms each time
		for (int i = 0; i < 5; i++) {
			Thread.sleep(1); // wait 1ms
			long expected = fixed[2];
			long counter = factory.create().getRandom() & mask;
			assertEquals("The counter should be equal to a fixed value when the ms changes", expected, counter);
		}
	}

	@Test
	public void testMonotonicityAfterClockDrift() throws InterruptedException {

		long diff = TsidFactory.CLOCK_DRIFT_TOLERANCE;
		long time = Instant.parse("2021-12-31T23:59:59.000Z").toEpochMilli();
		long times[] = { -1, time, time + 0, time + 1, time + 2, time + 3 - diff, time + 4 - diff, time + 5 };

		Clock clock = new Clock() {
			private int i;

			@Override
			public long millis() {
				return times[i++ % times.length];
			}

			@Override
			public ZoneId getZone() {
				return null;
			}

			@Override
			public Clock withZone(ZoneId zone) {
				return null;
			}

			@Override
			public Instant instant() {
				return null;
			}
		};

		// a function that forces the clock to restart to ZERO
		IntFunction<byte[]> randomFunction = x -> new byte[x];

		TsidFactory factory = TsidFactory.builder().withClock(clock).withRandomFunction(randomFunction).build();

		long ms1 = factory.create().getUnixMilliseconds(); // time
		long ms2 = factory.create().getUnixMilliseconds(); // time + 0
		long ms3 = factory.create().getUnixMilliseconds(); // time + 1
		long ms4 = factory.create().getUnixMilliseconds(); // time + 2
		long ms5 = factory.create().getUnixMilliseconds(); // time + 3 - 10000 (CLOCK DRIFT)
		long ms6 = factory.create().getUnixMilliseconds(); // time + 4 - 10000 (CLOCK DRIFT)
		long ms7 = factory.create().getUnixMilliseconds(); // time + 5
		assertEquals(ms1 + 0, ms2); // clock repeats.
		assertEquals(ms1 + 1, ms3); // clock advanced.
		assertEquals(ms1 + 2, ms4); // clock advanced.
		assertEquals(ms1 + 2, ms5); // CLOCK DRIFT! DON'T MOVE BACKWARDS!
		assertEquals(ms1 + 2, ms6); // CLOCK DRIFT! DON'T MOVE BACKWARDS!
		assertEquals(ms1 + 5, ms7); // clock advanced.
	}

	@Test
	public void testMonotonicityAfterLeapSecond() throws InterruptedException {

		long second = Instant.parse("2021-12-31T23:59:59.000Z").getEpochSecond();
		long leapSecond = second - 1; // simulate a leap second
		long times[] = { second, leapSecond };

		Clock clock = new Clock() {
			private int i;

			@Override
			public long millis() {
				return times[i++ % times.length] * 1000;
			}

			@Override
			public ZoneId getZone() {
				return null;
			}

			@Override
			public Clock withZone(ZoneId zone) {
				return null;
			}

			@Override
			public Instant instant() {
				return null;
			}
		};

		// a function that forces the clock to restart to ZERO
		IntFunction<byte[]> randomFunction = x -> new byte[x];

		TsidFactory factory = TsidFactory.builder().withClock(clock).withRandomFunction(randomFunction).build();

		long ms1 = factory.create().getUnixMilliseconds(); // second
		long ms2 = factory.create().getUnixMilliseconds(); // leap second

		assertEquals(ms1, ms2); // LEAP SECOND! DON'T MOVE BACKWARDS!
	}

	@Test
	public void testByteRandomNextInt() {

		for (int i = 0; i < 10; i++) {
			byte[] bytes = new byte[Integer.BYTES];
			(new Random()).nextBytes(bytes);
			int number = ByteBuffer.wrap(bytes).getInt();
			TsidFactory.IRandom random = new TsidFactory.ByteRandom((x) -> bytes);
			assertEquals(number, random.nextInt());
		}

		for (int i = 0; i < 10; i++) {

			int ints = 10;
			int size = Integer.BYTES * ints;

			byte[] bytes = new byte[size];
			(new Random()).nextBytes(bytes);
			ByteBuffer buffer1 = ByteBuffer.wrap(bytes);
			ByteBuffer buffer2 = ByteBuffer.wrap(bytes);

			TsidFactory.IRandom random = new TsidFactory.ByteRandom((x) -> {
				byte[] octects = new byte[x];
				buffer1.get(octects);
				return octects;
			});

			for (int j = 0; j < ints; j++) {
				assertEquals(buffer2.getInt(), random.nextInt());
			}
		}
	}

	@Test
	public void testByteRandomNextBytes() {

		for (int i = 0; i < 10; i++) {
			byte[] bytes = new byte[Integer.BYTES];
			(new Random()).nextBytes(bytes);
			TsidFactory.IRandom random = new TsidFactory.ByteRandom((x) -> bytes);
			assertEquals(Arrays.toString(bytes), Arrays.toString(random.nextBytes(Integer.BYTES)));
		}

		for (int i = 0; i < 10; i++) {

			int ints = 10;
			int size = Integer.BYTES * ints;

			byte[] bytes = new byte[size];
			(new Random()).nextBytes(bytes);
			ByteBuffer buffer1 = ByteBuffer.wrap(bytes);
			ByteBuffer buffer2 = ByteBuffer.wrap(bytes);

			TsidFactory.IRandom random = new TsidFactory.ByteRandom((x) -> {
				byte[] octects = new byte[x];
				buffer1.get(octects);
				return octects;
			});

			for (int j = 0; j < ints; j++) {
				byte[] octects = new byte[Integer.BYTES];
				buffer2.get(octects);
				assertEquals(Arrays.toString(octects), Arrays.toString(random.nextBytes(Integer.BYTES)));
			}
		}
	}

	@Test
	public void testLogRandomNextInt() {

		for (int i = 0; i < 10; i++) {
			byte[] bytes = new byte[Integer.BYTES];
			(new Random()).nextBytes(bytes);
			int number = ByteBuffer.wrap(bytes).getInt();
			TsidFactory.IRandom random = new TsidFactory.IntRandom(() -> number);
			assertEquals(number, random.nextInt());
		}

		for (int i = 0; i < 10; i++) {

			int ints = 10;
			int size = Integer.BYTES * ints;

			byte[] bytes = new byte[size];
			(new Random()).nextBytes(bytes);
			ByteBuffer buffer1 = ByteBuffer.wrap(bytes);
			ByteBuffer buffer2 = ByteBuffer.wrap(bytes);

			TsidFactory.IRandom random = new TsidFactory.IntRandom(() -> buffer1.getInt());

			for (int j = 0; j < ints; j++) {
				assertEquals(buffer2.getInt(), random.nextInt());
			}
		}
	}

	@Test
	public void testLogRandomNextBytes() {

		for (int i = 0; i < 10; i++) {
			byte[] bytes = new byte[Integer.BYTES];
			(new Random()).nextBytes(bytes);
			int number = ByteBuffer.wrap(bytes).getInt();
			TsidFactory.IRandom random = new TsidFactory.IntRandom(() -> number);
			assertEquals(Arrays.toString(bytes), Arrays.toString(random.nextBytes(Integer.BYTES)));
		}

		for (int i = 0; i < 10; i++) {

			int ints = 10;
			int size = Integer.BYTES * ints;

			byte[] bytes = new byte[size];
			(new Random()).nextBytes(bytes);
			ByteBuffer buffer1 = ByteBuffer.wrap(bytes);
			ByteBuffer buffer2 = ByteBuffer.wrap(bytes);

			TsidFactory.IRandom random = new TsidFactory.IntRandom(() -> buffer1.getInt());

			for (int j = 0; j < ints; j++) {
				byte[] octects = new byte[Integer.BYTES];
				buffer2.get(octects);
				assertEquals(Arrays.toString(octects), Arrays.toString(random.nextBytes(Integer.BYTES)));
			}
		}
	}
}
