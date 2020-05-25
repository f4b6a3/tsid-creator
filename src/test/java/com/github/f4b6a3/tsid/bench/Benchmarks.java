package com.github.f4b6a3.tsid.bench;

// Add these dependencies to pom.xml:
//
//		<dependency>
//			<groupId>org.openjdk.jmh</groupId>
//			<artifactId>jmh-core</artifactId>
//			<version>1.23</version>
//		</dependency>
//		<dependency>
//			<groupId>org.openjdk.jmh</groupId>
//			<artifactId>jmh-generator-annprocess</artifactId>
//			<version>1.23</version>
//		</dependency>

/*

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import com.github.f4b6a3.tsid.TsidCreator;

@Threads(1)
@State(Scope.Thread)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
public class Benchmarks {

	@Benchmark
	@BenchmarkMode(Mode.Throughput)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	public long getTsidThroughput() {
		return TsidCreator.getTsid();
	}

//	@Benchmark
//	@BenchmarkMode(Mode.AverageTime)
//	@OutputTimeUnit(TimeUnit.NANOSECONDS)
//	public long getTsidAverage() {
//		return TsidCreator.getTsid();
//	}

	@Benchmark
	@BenchmarkMode(Mode.Throughput)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	public String getTsidStringThroughput() {
		return TsidCreator.getTsidString();
	}

//	@Benchmark
//	@BenchmarkMode(Mode.AverageTime)
//	@OutputTimeUnit(TimeUnit.NANOSECONDS)
//	public String getTsidStringAverage() {
//		return TsidCreator.getTsidString();
//	}

	@Benchmark
	@BenchmarkMode(Mode.Throughput)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	public UUID getRandomUUIDThroughput() {
		return UUID.randomUUID();
	}

//	@Benchmark
//	@BenchmarkMode(Mode.AverageTime)
//	@OutputTimeUnit(TimeUnit.NANOSECONDS)
//	public UUID getRandomUUIDAverage() {
//		return UUID.randomUUID();
//	}

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder().include(Benchmarks.class.getSimpleName()).forks(1).build();
		new Runner(opt).run();
	}
}

*/


