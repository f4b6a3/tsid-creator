
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
@Threads(1)
@State(Scope.Thread)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class Throughput {

	@Benchmark
	public UUID uuidRandomUUID() {
		return UUID.randomUUID();
	}

	@Benchmark
	public String uuidRandomUUIDToString() {
		return UUID.randomUUID().toString();
	}

	@Benchmark
	public Tsid tsidCreatorGetTsid256() {
		return TsidCreator.getTsid256();
	}

	@Benchmark
	public String tsidCreatorGetTsid256ToString() {
		return TsidCreator.getTsid256().toString();
	}

	@Benchmark
	public Tsid tsidCreatorGetTsid1024() {
		return TsidCreator.getTsid1024();
	}

	@Benchmark
	public String tsidCreatorGetTsid1024ToString() {
		return TsidCreator.getTsid1024().toString();
	}

	@Benchmark
	public Tsid tsidCreatorGetTsid4096() {
		return TsidCreator.getTsid4096();
	}

	@Benchmark
	public String tsidCreatorGetTsid4096ToString() {
		return TsidCreator.getTsid4096().toString();
	}
}
