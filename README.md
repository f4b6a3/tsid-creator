
TSID Creator
======================================================

A Java library for generating TSIDs (Time Sortable Identifier).

It brings together some ideas from [Twitter's Snowflake](https://github.com/twitter-archive/snowflake/tree/snowflake-2010) and [ULID Spec](https://github.com/ulid/spec).

*   Generated in lexicographical order;
*   Can be stored as an integer of 64 bits;
*   Can be stored as a string of 13 chars;
*   String format is encoded to [Crockford's base32](https://www.crockford.com/base32.html);
*   String format is URL safe and case insensitive;
*   It is shorter than UUID, ULID and KSUID.

Read [Snowflake ID](https://en.wikipedia.org/wiki/Snowflake_ID) on Wikipedia.

How to Use
------------------------------------------------------

Create a TSID:

```java
Tsid tsid = TsidCreator.getTsid1024();
```

Create a TSID number:

```java
long number = TsidCreator.getTsid1024().toLong(); // 38352658567418872
```

Create a TSID string:

```java
String string = TsidCreator.getTsid1024().toString(); // 01226N0640J7Q
```

There are three predefined node ranges: 256, 1024 and 4096.

The TSID generator is [thread-safe](https://en.wikipedia.org/wiki/Thread_safety).

### Maven dependency

Add these lines to your `pom.xml`:

```xml
<!-- https://search.maven.org/artifact/com.github.f4b6a3/tsid-creator -->
<dependency>
  <groupId>com.github.f4b6a3</groupId>
  <artifactId>tsid-creator</artifactId>
  <version>4.1.3</version>
</dependency>
```
See more options in [maven.org](https://search.maven.org/artifact/com.github.f4b6a3/tsid-creator).

### Modularity

Module and bundle names are the same as the root package name.

*   JPMS module name: `com.github.f4b6a3.tsid`
*   OSGi symbolic name: `com.github.f4b6a3.tsid`

### TSID as Long

This section shows how to create TSID numbers.

The method `Tsid.toLong()` simply unwraps the internal `long` value.

```java
// Create a TSID for up to 256 nodes and 16384 ID/ms
long tsid = TsidCreator.getTsid256().toLong();
```

```java
// Create a TSID for up to 1024 nodes and 4096 ID/ms
long tsid = TsidCreator.getTsid1024().toLong();
```

```java
// Create a TSID for up to 4096 nodes and 1024 ID/ms
long tsid = TsidCreator.getTsid4096().toLong();
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

### TSID as String

This section shows how to create TSID strings.

The TSID string is a 13 characters long string encoded to [Crockford's base 32](https://www.crockford.com/base32.html).

```java
// Create a TSID string for up to 256 nodes and 16384 ID/ms
String tsid = TsidCreator.getTsid256().toString();
```

```java
// Create a TSID string for up to 1024 nodes and 4096 ID/ms
String tsid = TsidCreator.getTsid1024().toString();
```

```java
// Create a TSID string for up to 4096 nodes and 1024 ID/ms
String tsid = TsidCreator.getTsid4096().toString();
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

### TSID Structure

The term TSID stands for (roughly) Time Sortable ID. A TSID is a number that is formed by the creation time along with random bits.

The TSID has 2 components:

*   Time component (42 bits)
*   Random component (22 bits)

The time component is the count of milliseconds since 2020-01-01 00:00:00 UTC.

The Random component has 2 sub-parts:

*   Node ID (0 to 20 bits)
*   Counter (2 to 22 bits)

The counter bits depend on the node bits. If the node bits are 10, the counter bits are limited to 12. In this example, the maximum node value is 2^10-1 = 1023 and the maximum counter value is 2^12-1 = 4095. So the maximum TSIDs that can be generated per millisecond is 4096.

The node identifier uses 10 bits of the random component by default in the `TsidFactory`. It's possible to adjust the node bits to a value between 0 and 20. The counter bits are affected by the node bits.

This is the default TSID structure:

```
                                            adjustable
                                           <---------->
|------------------------------------------|----------|------------|
       time (msecs since 2020-01-01)           node       counter
                42 bits                       10 bits     12 bits

- time:    2^42 = ~139 years (with adjustable epoch)
- node:    2^10 = 1,024 (with adjustable bits)
- counter: 2^12 = 4,096 (initially random)

Notes:
The node is adjustable from 0 to 20 bits.
The node bits affect the counter bits.
The time component can be used for ~69 years if stored in a SIGNED 64 bits integer field.
```

The node identifier is a random number from 0 to 1023 (default). It can be replaced by a value given to the `TsidFactory` constructor or method factory.

Another way to replace the random node is by using a system property `tsidcreator.node` or a environment variable `TSIDCREATOR_NODE`.

#### Node identifier

The node identifier can be given to the `TsidFactory` by defining a system property `tsidcreator.node` or a environment variable `TSIDCREATOR_NODE`. If this property or variable exists, the node identifier is its value. Otherwise, the node identifier is random number.

The simplest way to avoid collisions is to ensure that each generator has its exclusive node identifier.

*   Using system property:

```bash
// append to VM arguments
-Dtsidcreator.node="755"
```

*   Using environment variable:

```bash
# append to /etc/environment or ~/.profile
export TSIDCREATOR_NODE="492"
```
### Other usage examples

Create a TSID from a canonical string (13 chars):

```java
Tsid tsid = Tsid.from("0123456789ABC");
```

Convert a TSID into a canonical string in lower case:

```java
String string = tsid.toLowerCase(); // 0123456789abc
```

Convert a TSID into a byte array:

```java
byte[] bytes = tsid.toBytes(); // 8 bytes (64 bits)
```

Get the creation instant of a TSID:

```java
Instant instant = tsid.getInstant(); // 2020-04-15T22:31:02.458Z
```

Get the time component of a TSID:

```java
long time = tsid.getTime(); // 9153062458
```

Get the random component of a TSID:

```java
long random = tsid.getRandom(); // 305516
```

Use a `TsidFactory` instance with a FIXED node identifier to generate TSIDs:

```java
int node = 256; // max: 2^10
TsidFactory factory = new TsidFactory(node);

// use the factory
Tsid tsid = factory.create();
```

Use a `TsidFactory` instance with a FIXED node identifier and CUSTOM node bits to generate TSIDs:

```java
// setup a factory for up to 64 nodes and 65536 ID/ms.
TsidFactory factory = TsidFactory.builder()
    .withNodeBits(6)      // max: 20
    .withNode(63)         // max: 2^nodeBits
    .build();

// use the factory
Tsid tsid = factory.create();
```

Use a `TsidFactory` instance with a CUSTOM epoch to generate TSIDs:

```java
// use a CUSTOM epoch that starts from the fall of the Berlin Wall
Instant customEpoch = Instant.parse("1989-11-09T00:00:00Z");
TsidFactory factory = TsidFactory.builder().withCustomEpoch(customEpoch).build();

// use the factory
Tsid tsid = factory.create();
```

Use a `TsidFactory` with `java.util.Random`:

```java
// use a `java.util.Random` instance for fast generation
TsidFactory factory = TsidFactory.builder().withRandom(new Random()).build();

// use the factory
Tsid tsid = factory.create();
```

Use a `TsidFactory` with a random generator of your choice inside of an `IntFunction<byte[]>`:

```java
// use a random function that returns an array of bytes with a given length
AwesomeRandom awesomeRandom = new AwesomeRandom(); // a hypothetical RNG
TsidFactory factory = TsidFactory.builder()
    .withRandomFunction(length -> awesomeRandom.nextBytes(length))
    .build();

// use the factory
Tsid tsid = factory.create();
```

Use a `TsidFactory` with `ThreadLocalRandom` inside of an `IntFunction<byte[]>`:

```java
// use a random function that returns an array of bytes with a given length
TsidFactory factory = TsidFactory.builder()
    .withRandomFunction(length -> {
        final byte[] bytes = new byte[length];
        ThreadLocalRandom.current().nextBytes(bytes);
        return bytes;
    }).build();

// use the factory
Tsid tsid = factory.create();
```

Use a `TsidFactory` that creates TSIDs similar to [Twitter Snowflakes](https://github.com/twitter-archive/snowflake):

```java
// Twitter Snowflakes have 5 bits for datacenter ID and 5 bits for worker ID
int datacenter = 1; // max: 2^5-1 = 31
int worker = 1;     // max: 2^5-1 = 31
int node = (datacenter << 5 | worker); // max: 2^10-1 = 1023

// Twitter Epoch is fixed in 1288834974657 (2010-11-04T01:42:54.657Z)
Instant customEpoch = Instant.ofEpochMilli(1288834974657L);

// a function that returns an array with ZEROS, making the factory
// to RESET the counter to ZERO when the millisecond changes
IntFunction<byte[]> randomFunction = (x) -> new byte[x];

// a factory that returns TSIDs similar to Twitter Snowflakes
TsidFactory factory = TsidFactory.builder()
		.withRandomFunction(randomFunction)
		.withCustomEpoch(customEpoch)
		.withNode(node)
		.build();

// use the factory
Tsid tsid = factory.create();
```

Use a `TsidFactory` that creates TSIDs similar to [Discord Snowflakes](https://discord.com/developers/docs/reference#snowflakes):

```java
// Discord Snowflakes have 5 bits for worker ID and 5 bits for process ID
int worker = 1;  // max: 2^5-1 = 31
int process = 1; // max: 2^5-1 = 31
int node = (worker << 5 | process); // max: 2^10-1 = 1023

// Discord Epoch starts in the first millisecond of 2015
Instant customEpoch = Instant.parse("2015-01-01T00:00:00.000Z");

// a function that returns NULL, making the factory to
// INCREMENT the counter when the millisecond changes
IntFunction<byte[]> randomFunction = (x) -> null;

// a factory that returns TSIDs similar to Discord Snowflakes
TsidFactory factory = TsidFactory.builder()
		.withRandomFunction(randomFunction)
		.withCustomEpoch(customEpoch)
		.withNode(node)
		.build();

// use the factory
Tsid tsid = factory.create();
```

Benchmark
------------------------------------------------------

This section shows benchmarks comparing `TsidCreator` to `java.util.UUID`.

```
--------------------------------------------------------------------------------
THROUGHPUT (operations/msec)            Mode  Cnt      Score     Error   Units
--------------------------------------------------------------------------------
UUID_randomUUID                        thrpt    5   2062,642 ±  34,230  ops/ms
UUID_randomUUID_toString               thrpt    5   1166,183 ±  16,001  ops/ms
TsidCreator_getTsid256                 thrpt    5  16075,867 ± 274,681  ops/ms
TsidCreator_getTsid256_toString        thrpt    5  15379,666 ± 167,178  ops/ms
TsidCreator_getTsid1024                thrpt    5   4093,735 ±   0,272  ops/ms
TsidCreator_getTsid1024_toString       thrpt    5   4076,510 ±   2,437  ops/ms
TsidCreator_getTsid4096                thrpt    5   1024,009 ±   0,252  ops/ms
TsidCreator_getTsid4096_toString       thrpt    5   1023,653 ±   0,379  ops/ms
--------------------------------------------------------------------------------
Total time: 00:10:40
--------------------------------------------------------------------------------
```

System: JVM 8, Ubuntu 20.04, CPU i5-3330, 8G RAM.

To execute the benchmark, run `./benchmark/run.sh`.

Other identifier generators
------------------------------------------------------
*   [UUID Creator](https://github.com/f4b6a3/uuid-creator)
*   [ULID Creator](https://github.com/f4b6a3/ulid-creator)
*   [KSUID Creator](https://github.com/f4b6a3/ksuid-creator)
