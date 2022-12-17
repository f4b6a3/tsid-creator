
package benchmark;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

import com.github.f4b6a3.tsid.Tsid;
import com.github.f4b6a3.tsid.TsidCreator;

@Fork(1)
@Threads(4)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 3)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class Throughput {

	@Benchmark
	public UUID UUID_randomUUID() {
		return UUID.randomUUID();
	}

	@Benchmark
	public String UUID_randomUUID_toString() {
		return UUID.randomUUID().toString();
	}

	@Benchmark
	public Tsid Tsid_fast() {
		return Tsid.fast();
	}

	@Benchmark
	public String Tsid_fast_toString() {
		return Tsid.fast().toString();
	}

	@Benchmark
	public Tsid TsidCreator_getTsid256() {
		return TsidCreator.getTsid256();
	}

	@Benchmark
	public String TsidCreator_getTsid256_toString() {
		return TsidCreator.getTsid256().toString();
	}

	@Benchmark
	public Tsid TsidCreator_getTsid1024() {
		return TsidCreator.getTsid1024();
	}

	@Benchmark
	public String TsidCreator_getTsid1024_toString() {
		return TsidCreator.getTsid1024().toString();
	}

	@Benchmark
	public Tsid TsidCreator_getTsid4096() {
		return TsidCreator.getTsid4096();
	}

	@Benchmark
	public String TsidCreator_getTsid4096_toString() {
		return TsidCreator.getTsid4096().toString();
	}
}
