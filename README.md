
# TSID Creator

A Java library for generating time sortable IDs.

It brings together some ideas from [Twitter's Snowflake](https://github.com/twitter-archive/snowflake) and [ULID Spec](https://github.com/ulid/spec).

How to Use
------------------------------------------------------

Create a TSID:

```java
long tsid = TsidCreator.getTsid();
```

Create a TSID with node identifier:

```java
int node = 255;
long tsid = TsidCreator.getTsid(node);
```

### Maven dependency

Add these lines to your `pom.xml`:

```xml
<!-- https://search.maven.org/artifact/com.github.f4b6a3/tsid-creator -->
<dependency>
  <groupId>com.github.f4b6a3</groupId>
  <artifactId>tsid-creator</artifactId>
  <version>2.1.1</version>
</dependency>
```
See more options in [maven.org](https://search.maven.org/artifact/com.github.f4b6a3/tsid-creator).

Implementation
------------------------------------------------------

### TSID as number

The term TSID stands for (rougthly) Time Sortable ID. A TSID is a number that is formed by a creation time followed by random bits.

The TSID has 2 components:

- Time component (42 bits)
- Random component (22 bits)

The time component is the number of milliseconds since 2020-01-01 00:00:00 UTC.

The random component may have 2 sub-parts:

- Node ID (0 to 20 bits)
- Counter (2 to 22 bits)

The counter bit length depends on the node identifier bit length. If the node identifier bit length is 10, the counter bit length is limited to 12. In this example, the maximum node identifier value is 2^10 = 1024 and the maximum counter value is 2^12 = 4096. So the maximum TSIDs that can be generated per millisecond is about 4 thousand.

When the node ID is not used, all the 22 bits of the random component are dedicated to a counter that is started with a random value. In this case maximum counter value is 2^22 = 4,194,304. So the maximum number of TSIDs that can be generated within the same millisecond is about 4 million.

##### TSID structure WITHOUT node identifier

This is the structure without node identifier:

```
|------------------------------------------|----------------------|
        millisecs since 2020-01-01                  counter
                42 bits                             22 bits

- timestamp: 2^42 = ~69 years (with adjustable epoch)
- counter: 2^22 = 4,194,304 (initially random)
```

The node identifier not used. All bits from the random component are dedicated to the counter.

##### TSID structure WITH node identifier

This is the structure with node identifier:

```
                                            adjustable
                                           <---------->
|------------------------------------------|----------|------------|
       millisecs since 2020-01-01             nodeid      counter
                42 bits                       10 bits     12 bits

- timestamp: 2^42 = ~69 years (with adjustable epoch)
- nodeid: 2^10 = 1,024 (with adjustable bit length)
- counter: 2^12 = 4,096 (initially random)

Note:
The node Id is adjustable from 0 to 20 bits. 
The node id bit length affects the counter bit length.
```

The node identifier uses 10 bits of the random component by default. It's possible to adjust the node bit length to a value between 0 and 20. The counter bit length is affected by the node bit length.

##### Examples

```java
// Create a TSID
long tsid = TsidCreator.getTsid();
```

```java
// Create a TSID with node identifier
int node = 42;
long tsid = TsidCreator.getTsid(node);
```

Examples of TSIDs:

```text
38352658567418867
38352658567418868
38352658567418869
38352658567418870
38352658567418871
38352658567418872
38352658567418873
38352658567418874
38352658573940759 < millisecond changed
38352658573940760
38352658573940761
38352658573940762
38352658573940763
38352658573940764
38352658573940765
38352658573940766
         ^      ^ look
                                   
|--------|------|
   time   random
```

### TSID as string

The TSID string is a TSID number encoded to Crockford's base 32. It is a sequence of 13 characters.

##### Examples

```java
// Create a TSID string
long tsid = TsidCreator.getTsidString();
```

```java
// Create a TSID string with node identifier
int node = 753;
long tsid = TsidCreator.getTsidString(node);
```

Examples of TSID strings:

```text
01226N0640J7K
01226N0640J7M
01226N0640J7N
01226N0640J7P
01226N0640J7Q
01226N0640J7R
01226N0640J7S
01226N0640J7T
01226N0693HDA < millisecond changed
01226N0693HDB
01226N0693HDC
01226N0693HDD
01226N0693HDE
01226N0693HDF
01226N0693HDG
01226N0693HDH
        ^   ^ look
                                   
|-------|---|
   time random
```

#### How use the `TimeIdCreator` directly

These are some examples of using the `TimeIdCreator` to create TSIDs:

```java
// with a CUSTOM timestamp strategy
TimestampStrategy customStrategy = new CustomTimestampStrategy();
long tsid = TsidCreator.getTimeIdCreator()
	.withTimestampStrategy(customStrategy)
	.create();
```
```java
// with a CUSTOM epoch (fall of the Berlin Wall)
Instant customEpoch = Instant.parse("1989-11-09T00:00:00Z");
long tsid = TsidCreator.getTimeIdCreator()
	.withCustomEpoch(customEpoch)
	.create();
```
```java
// with a FIXED node identifier
int node = 256; // max: 2^10
long tsid = TsidCreator.getTimeIdCreator(node)
	.create();
```
```java
// with a FIXED node identifier and a CUSTOM node bit length.
int length = 16;   // max: 20
int node = 32768;  // max: 2^length
long tsid = TsidCreator.getTimeIdCreator(node, length)
	.create();
```

Benchmark
------------------------------------------------------

This section shows benchmarks comparing `TsidCreator` to `java.util.UUID`.

```
---------------------------------------------------------------------------
THROUGHPUT                         Mode  Cnt      Score     Error   Units
---------------------------------------------------------------------------
Throughput.Java_RandomBased        thrpt    5   2234,199 ±   2,844  ops/ms
Throughput.TsidCreator_Tsid        thrpt    5  32864,576 ± 129,848  ops/ms
Throughput.TsidCreator_TsidString  thrpt    5  11543,968 ± 103,472  ops/ms
---------------------------------------------------------------------------
Total time: 00:06:41
---------------------------------------------------------------------------
```

```
----------------------------------------------------------------------
AVERAGE TIME                        Mode  Cnt    Score   Error  Units
----------------------------------------------------------------------
AverageTime.Java_RandomBased        avgt    5  449,641 ± 0,994  ns/op
AverageTime.TsidCreator_Tsid        avgt    5   30,478 ± 0,260  ns/op
AverageTime.TsidCreator_TsidString  avgt    5   85,799 ± 0,289  ns/op
----------------------------------------------------------------------
Total time: 00:06:41
----------------------------------------------------------------------
```

System: CPU i5-3330, 8G RAM, Ubuntu 20.04.

See: [uuid-creator-benchmark](https://github.com/fabiolimace/uuid-creator-benchmark)

Links for generators
-------------------------------------------
* [UUID Creator](https://github.com/f4b6a3/uuid-creator)
* [ULID Creator](https://github.com/f4b6a3/ulid-creator)
* [TSID Creator](https://github.com/f4b6a3/tsid-creator)
