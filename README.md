

# TSID Creator

A Java library for generating Time Sortable IDs.

It brings together some ideas from [Twitter's Snowflake](https://github.com/twitter-archive/snowflake/tree/snowflake-2010) and [ULID Spec](https://github.com/ulid/spec).

* Generated in lexicographical order;
* Can be stored as an integer of 64 bits;
* Can be stored as a string of 13 chars;
* String format is encoded to [Crockford's base32](https://www.crockford.com/base32.html);
* String format is URL safe, case insensitive and accepts hyphens.

How to Use
------------------------------------------------------

Create a TSID for up to 1024 nodes and 4096 ID/ms:

```java
long tsid = TsidCreator.getTsid1024(); // 38352658567418867
```

Create a TSID string for up to 1024 nodes and 4096 ID/ms:

```java
String tsid = TsidCreator.getTsidString1024(); // 01226N0640J7K
```

There are three predefined node ranges: 256, 1024 and 4096. If you need another node range, you can use the `TimeIdCreator` directly.

The TSID generator is [thread-safe](https://en.wikipedia.org/wiki/Thread_safety).

### Maven dependency

Add these lines to your `pom.xml`:

```xml
<!-- https://search.maven.org/artifact/com.github.f4b6a3/tsid-creator -->
<dependency>
  <groupId>com.github.f4b6a3</groupId>
  <artifactId>tsid-creator</artifactId>
  <version>2.4.4</version>
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

The counter bit length depends on the node identifier bit length. If the node identifier bit length is 10, the counter bit length is limited to 12. In this example, the maximum node identifier value is 2^10 = 1024 and the maximum counter value is 2^12 = 4096. So the maximum TSIDs that can be generated per millisecond is about 4 thousand.

In the generator `TimeIdCreator` the node identifier uses 10 bits of the random component by default. It's possible to adjust the node bit length to a value between 0 and 20. The counter bit length is affected by the node bit length.

##### TSID structure

This is the default TSID structure:

```
                                            adjustable
                                           <---------->
|------------------------------------------|----------|------------|
       time (msecs since 2020-01-01)           node      counter
                42 bits                       10 bits     12 bits

- time:    2^42 = ~139 years (with adjustable epoch)
- node:    2^10 = 1,024 (with adjustable bit length)
- counter: 2^12 = 4,096 (initially random)

Note:
The node id is adjustable from 0 to 20 bits.
The node id bit length affects the counter bit length.
```

The node identifier is a random number from 0 to 1023 (default). It can be replaced by a value passed to the `TimeIdCreator` constructor or method factory.

Another way to replace the random value is using a system property `tsidcreator.node` or a environment variable `TSIDCREATOR_NODE`.

##### Examples


```java
// Create a TSID for up to 256 nodes and 16384 ID/ms
long tsid = TsidCreator.getTsid256();
```

```java
// Create a TSID for up to 1024 nodes and 4096 ID/ms
long tsid = TsidCreator.getTsid1024();
```

```java
// Create a TSID for up to 4096 nodes and 1024 ID/ms
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
// Create a TSID string for up to 256 nodes and 16384 ID/ms
long tsid = TsidCreator.getTsidString256();
```

```java
// Create a TSID string for up to 1024 nodes and 4096 ID/ms
long tsid = TsidCreator.getTsidString1024();
```

```java
// Create a TSID string for up to 4096 nodes and 1024 ID/ms
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
# append to /etc/environment or ~/.profile
export TSIDCREATOR_NODE="492"
```

### How use the `TimeIdCreator` directly

These are some examples of using the `TimeIdCreator` to create TSIDs:

```java
// with a FIXED node identifier
int node = 256; // max: 2^10
TimeIdCreator creator = new TimeIdCreator(node); 
long tsid = creator.create();
```
```java
// with a FIXED node identifier and a CUSTOM node bit length.
int length = 16;   // max: 20
int node = 32768;  // max: 2^length
TimeIdCreator creator = new TimeIdCreator(node, length); 
long tsid = creator.create();
```
```java
// with a CUSTOM timestamp strategy
TimestampStrategy customStrategy = new CustomTimestampStrategy();
TimeIdCreator creator = TsidCreator.getTimeIdCreator1024()
	.withTimestampStrategy(customStrategy)
long tsid = creator.create();
```
```java
// with a CUSTOM epoch (fall of the Berlin Wall)
Instant customEpoch = Instant.parse("1989-11-09T00:00:00Z");
TimeIdCreator creator = TsidCreator.getTimeIdCreator1024()
	.withCustomEpoch(customEpoch);
long tsid = creator.create();
```

Benchmark
------------------------------------------------------

This section shows benchmarks comparing `TsidCreator` to `java.util.UUID`.

```
================================================================================
THROUGHPUT (operations/millis)           Mode  Cnt      Score     Error   Units
================================================================================
Throughput.JDK_RandomBased              thrpt    5   2050,995 ±  21,636  ops/ms
--------------------------------------------------------------------------------
Throughput.TsidCreator_Tsid256          thrpt    5  16383,417 ±   5,386  ops/ms
Throughput.TsidCreator_Tsid1024         thrpt    5   4096,246 ±   0,063  ops/ms
Throughput.TsidCreator_Tsid4096         thrpt    5   1024,052 ±   0,102  ops/ms
Throughput.TsidCreator_TsidString256    thrpt    5  14574,401 ± 202,471  ops/ms
Throughput.TsidCreator_TsidString1024   thrpt    5   4076,432 ±   3,552  ops/ms
Throughput.TsidCreator_TsidString4096   thrpt    5   1023,696 ±   0,445  ops/ms
================================================================================
Total time: 00:09:20
================================================================================
```

System: JVM 8, Ubuntu 20.04, CPU i5-3330, 8G RAM.

See: [uuid-creator-benchmark](https://github.com/fabiolimace/uuid-creator-benchmark)

Links for generators
-------------------------------------------
* [UUID Creator](https://github.com/f4b6a3/uuid-creator)
* [ULID Creator](https://github.com/f4b6a3/ulid-creator)
* [TSID Creator](https://github.com/f4b6a3/tsid-creator)
