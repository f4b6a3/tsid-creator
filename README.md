
# TSID Creator

A Java library for generating Time Sortable IDs.

It brings together some ideas from [Twitter's Snowflake](https://github.com/twitter-archive/snowflake) and [ULID Spec](https://github.com/ulid/spec).

How to Use
------------------------------------------------------

Create a TSID:

```java
long tsid = TsidCreator.getTsid();
```

Create a TSID string:

```java
long tsid = TsidCreator.getTsidString();
```

### Maven dependency

Add these lines to your `pom.xml`:

```xml
<!-- https://search.maven.org/artifact/com.github.f4b6a3/tsid-creator -->
<dependency>
  <groupId>com.github.f4b6a3</groupId>
  <artifactId>tsid-creator</artifactId>
  <version>2.2.0</version>
</dependency>
```
See more options in [maven.org](https://search.maven.org/artifact/com.github.f4b6a3/tsid-creator).

Implementation
------------------------------------------------------

### TSID as number

The term TSID stands for (roughly) Time Sortable ID. A TSID is a number that is formed by the creation time followed by random bits.

The TSID has 2 components:

- Time component (42 bits)
- Random component (22 bits)

The time component is the number of milliseconds since 2020-01-01 00:00:00 UTC.

The random component has 2 sub-parts:

- Node ID (0 to 20 bits)
- Counter (2 to 22 bits)

The counter bit length depends on the node identifier bit length. If the node identifier bit length is 10, the counter bit length is limited to 12. In this example, the maximum node identifier value is 2^10 = 1024 and the maximum counter value is 2^12 = 4096. So the maximum TSIDs that can be generated per millisecond is about 4 thousand. This is the default.

The node identifier uses 10 bits of the random component by default. It's possible to adjust the node bit length to a value between 0 and 20. The counter bit length is affected by the node bit length.

##### TSID structure

This is the default TSID structure:

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
The node id is adjustable from 0 to 20 bits.
The node id bit length affects the counter bit length.
```

The node identifier is a random number from 0 to 1023 (default). It can be replaced by a value passed to the `TimeIdCreator` constructor or method factory. Example:

```java
int node = 842;
long tsid = TsdiCreator.getTimeIdCreator(node).create();
```

Another way to replace the random value is using a system property `tsidcreator.node` or a environment variable `TSIDCREATOR_NODE`.

##### Examples

```java
// Create a TSID (up to 1024 nodes)
long tsid = TsidCreator.getTsid();
```

```java
// Create a TSID (up to 256 nodes)
long tsid = TsidCreator.getTsid256();
```

```java
// Create a TSID (up to 4096 nodes)
long tsid = TsidCreator.getTsid4096();
```

Sequence of TSIDs:

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

The TSID string is a number encoded to [Crockford's base 32](https://www.crockford.com/base32.html). It is a string of 13 characters.

##### Examples

```java
// Create a TSID string (up to 1024 nodes)
long tsid = TsidCreator.getTsidString();
```

```java
// Create a TSID (up to 256 nodes)
long tsid = TsidCreator.getTsidString256();
```

```java
// Create a TSID (up to 4096 nodes)
long tsid = TsidCreator.getTsidString4096();
```

Sequence of TSID strings:

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

### System properties and environment variables

##### Node identifier

The `tsidcreator.node` property is used by the `TimeIdCreator`. If this property or variable exists, its value is returned. Otherwise, a random value is returned. It may be useful if you want to identify each single machine by yourself, instead of allowing the PRNG do it for you.

The simplest way to avoid collisions is to ensure that each generator has its own node identifier.

* Using system property:

```bash
// append to VM arguments
-Dtsidcreator.node="755"
```

* Using environment variable:

```bash
// append to /etc/environment or ~/.profile
export TSIDCREATOR_NODE="492"
```

### How use the `TimeIdCreator` directly

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
THROUGHPUT                          Mode  Cnt      Score    Error   Units
---------------------------------------------------------------------------
Throughput.Java_RandomBased        thrpt    5   2232,765 ±  7,125  ops/ms
Throughput.TsidCreator_Tsid        thrpt    5  20782,016 ± 15,113  ops/ms
Throughput.TsidCreator_TsidString  thrpt    5   9769,603 ± 35,345  ops/ms
---------------------------------------------------------------------------
Total time: 00:04:01
---------------------------------------------------------------------------
```

```
----------------------------------------------------------------------
AVERAGE TIME                        Mode  Cnt    Score   Error  Units
----------------------------------------------------------------------
AverageTime.Java_RandomBased        avgt    5  446,341 ± 3,033  ns/op
AverageTime.TsidCreator_Tsid        avgt    5   49,830 ± 1,104  ns/op
AverageTime.TsidCreator_TsidString  avgt    5  104,378 ± 1,922  ns/op
----------------------------------------------------------------------
Total time: 00:04:01
----------------------------------------------------------------------
```

System: CPU i5-3330, 8G RAM, Ubuntu 20.04.

See: [uuid-creator-benchmark](https://github.com/fabiolimace/uuid-creator-benchmark)

Links for generators
-------------------------------------------
* [UUID Creator](https://github.com/f4b6a3/uuid-creator)
* [ULID Creator](https://github.com/f4b6a3/ulid-creator)
* [TSID Creator](https://github.com/f4b6a3/tsid-creator)
