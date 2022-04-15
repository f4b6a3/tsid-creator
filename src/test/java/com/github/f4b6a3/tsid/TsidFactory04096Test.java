package com.github.f4b6a3.tsid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.github.f4b6a3.tsid.TsidFactory;

public class TsidFactory04096Test extends TsidFactory00000Test {

	private static final int NODE_BITS = 12;
	private static final int COUNTER_BITS = 10;
	private static final int COUNTER_MAX = (int) Math.pow(2, COUNTER_BITS);

	@Test
	public void testGetTsid4096() {

		long startTime = System.currentTimeMillis();

		TsidFactory factory = TsidFactory.builder().withNodeBits(NODE_BITS).withRandom(random).build();

		long[] list = new long[LOOP_MAX];
		for (int i = 0; i < LOOP_MAX; i++) {
			list[i] = factory.create().toLong();
		}

		long endTime = System.currentTimeMillis();

		assertTrue(checkNullOrInvalid(list));
		assertTrue(checkUniqueness(list));
		assertTrue(checkOrdering(list));
		assertTrue(checkMaximumPerMs(list, COUNTER_MAX));
		assertTrue(checkCreationTime(list, startTime, endTime));
	}

	@Test
	public void testGetTsid4096WithNode() {

		long startTime = System.currentTimeMillis();

		int node = random.nextInt();
		TsidFactory factory = TsidFactory.builder().withNode(node).withNodeBits(NODE_BITS).withRandom(random).build();

		long[] list = new long[LOOP_MAX];
		for (int i = 0; i < LOOP_MAX; i++) {
			list[i] = factory.create().toLong();
		}

		long endTime = System.currentTimeMillis();

		assertTrue(checkNullOrInvalid(list));
		assertTrue(checkUniqueness(list));
		assertTrue(checkOrdering(list));
		assertTrue(checkMaximumPerMs(list, COUNTER_MAX));
		assertTrue(checkCreationTime(list, startTime, endTime));
	}

	@Test
	public void testGetTsidString4096() {

		long startTime = System.currentTimeMillis();

		TsidFactory factory = TsidFactory.builder().withNodeBits(NODE_BITS).withRandom(random).build();

		String[] list = new String[LOOP_MAX];
		for (int i = 0; i < LOOP_MAX; i++) {
			list[i] = factory.create().toString();
		}

		long endTime = System.currentTimeMillis();

		assertTrue(checkNullOrInvalid(list));
		assertTrue(checkUniqueness(list));
		assertTrue(checkOrdering(list));
		assertTrue(checkMaximumPerMs(list, COUNTER_MAX));
		assertTrue(checkCreationTime(list, startTime, endTime));
	}

	@Test
	public void testGetTsidString4096WithNode() {

		long startTime = System.currentTimeMillis();

		int node = random.nextInt();
		TsidFactory factory = TsidFactory.builder().withNode(node).withNodeBits(NODE_BITS).withRandom(random).build();

		String[] list = new String[LOOP_MAX];
		for (int i = 0; i < LOOP_MAX; i++) {
			list[i] = factory.create().toString();
		}

		long endTime = System.currentTimeMillis();

		assertTrue(checkNullOrInvalid(list));
		assertTrue(checkUniqueness(list));
		assertTrue(checkOrdering(list));
		assertTrue(checkMaximumPerMs(list, COUNTER_MAX));
		assertTrue(checkCreationTime(list, startTime, endTime));
	}

	@Test
	public void testGetTsid4096Parallel() throws InterruptedException {

		TestThread.clearHashSet();
		Thread[] threads = new Thread[THREAD_TOTAL];

		// Instantiate and start many threads
		for (int i = 0; i < THREAD_TOTAL; i++) {
			TsidFactory factory = TsidFactory.builder().withNode(i).withNodeBits(NODE_BITS).withRandom(random).build();
			threads[i] = new TestThread(factory, COUNTER_MAX);
			threads[i].start();
		}

		// Wait all the threads to finish
		for (Thread thread : threads) {
			thread.join();
		}

		// Check if the quantity of unique UUIDs is correct
		assertEquals(DUPLICATE_UUID_MSG, (COUNTER_MAX * THREAD_TOTAL), TestThread.hashSet.size());
	}
}
