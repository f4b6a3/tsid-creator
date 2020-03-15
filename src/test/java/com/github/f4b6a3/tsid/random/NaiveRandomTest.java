package com.github.f4b6a3.tsid.random;

import org.junit.Test;

import com.github.f4b6a3.tsid.random.Xorshift128PlusRandom;
import com.github.f4b6a3.tsid.random.XorshiftRandom;

import static org.junit.Assert.*;

public class NaiveRandomTest {

	private static final int DEFAULT_LOOP_LIMIT = 100_000;
	private static final String EXPECTED_BIT_COUNT_RANDOM_LONG = "The average bit count expected for random long values is 32";

	@Test
	public void testXorshiftNextLongNaiveAverageBitCount() {
		
		double accumulator = 0;
		
		XorshiftRandom random = new XorshiftRandom();
		
		for(int i = 0; i < DEFAULT_LOOP_LIMIT; i++) {
			long value = random.nextLong();
			accumulator += Long.bitCount(value);
		}
		
		double average = Math.round(accumulator / DEFAULT_LOOP_LIMIT);
		
		assertTrue(EXPECTED_BIT_COUNT_RANDOM_LONG, average == 32);
	}
	
	@Test
	public void testXorshift128PlusNextLongNaiveAverageBitCount() {

		double accumulator = 0;

		Xorshift128PlusRandom random = new Xorshift128PlusRandom();

		for (int i = 0; i < DEFAULT_LOOP_LIMIT; i++) {
			long value = random.nextLong();
			accumulator += Long.bitCount(value);
		}

		double average = Math.round(accumulator / DEFAULT_LOOP_LIMIT);

		assertTrue(EXPECTED_BIT_COUNT_RANDOM_LONG, average == 32);
	}
}
