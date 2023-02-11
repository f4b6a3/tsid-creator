package com.github.f4b6a3.tsid;

import static org.junit.Assert.*;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

public class CollisionTest {

	private TsidFactory newFactory(int nodeBits) {
		return TsidFactory.builder().withRandomFunction(() -> ThreadLocalRandom.current().nextInt())
				.withNodeBits(nodeBits) // 8 bits: 256 nodes; 10 bits: 1024 nodes...
				.build();
	}

	@Test
	public void testCollision() throws InterruptedException {

		int nodeBits = 8;
		int threadCount = 16;
		int iterationCount = 100_000;

		AtomicInteger clashes = new AtomicInteger();
		CountDownLatch endLatch = new CountDownLatch(threadCount);
		ConcurrentMap<Long, Integer> tsidMap = new ConcurrentHashMap<>();

		// one generator shared by ALL THREADS
		TsidFactory factory = newFactory(nodeBits);

		for (int i = 0; i < threadCount; i++) {

			final int threadId = i;

			new Thread(() -> {
				for (int j = 0; j < iterationCount; j++) {
					Long tsid = factory.create().toLong();
					if (Objects.nonNull(tsidMap.put(tsid, (threadId * iterationCount) + j))) {
						clashes.incrementAndGet();
						break;
					}
				}

				endLatch.countDown();
			}).start();
		}
		endLatch.await();

		assertFalse("Collisions detected!", clashes.intValue() != 0);
	}
}