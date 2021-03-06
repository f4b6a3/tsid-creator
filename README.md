

# TSID Creator

A Java library for generating TSIDs (Time Sortable Identifier).

It brings together some ideas from [Twitter's Snowflake](https://github.com/twitter-archive/snowflake/tree/snowflake-2010) and [ULID Spec](https://github.com/ulid/spec).

* Generated in lexicographical order;
* Can be stored as an integer of 64 bits;
* Can be stored as a string of 13 chars;
* String format is encoded to [Crockford's base32](https://www.crockford.com/base32.html);
* String format is URL safe and case insensitive.

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
  <version>3.0.3</version>
</dependency>
```
See more options in [maven.org](https://search.maven.org/artifact/com.github.f4b6a3/tsid-creator).

Module name: `com.github.f4b6a3.tsid`.

### TSID as number

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

### TSID as string

This section shows how to create TSID strings.

The TSID string is a 13 characters long string encoded to [Crockford's base 32](https://www.crockford.com/base32.html).

There are 3 methods to generate TSID strings: `Tsid.toString()`, `Tsid.toUpperCase()` and `Tsid.toLowerCase()`.

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

### Other usage examples

Create a TSID from a canonical string (13 chars):

```java
Tsid tsid = Tsid.from("0123456789ABC");
```

Convert a TSID into a canonical string in upper case:

```java
String string = tsid.toUpperCase(); // 0123456789ABC
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

Tsid tsid = factory.create();
```

Use a `TsidFactory` instance with a FIXED node identifier and a CUSTOM node bit length to generate TSIDs:

```java
int length = 16;   // max: 20
int node = 32768;  // max: 2^length
TsidFactory factory = new TsidFactory(node, length);

Tsid tsid = factory.create();
```

Use a `TsidFactory` instance with a CUSTOM epoch (fall of the Berlin Wall) to generate TSIDs:

```java
Instant customEpoch = Instant.parse("1989-11-09T00:00:00Z");
TsidFactory factory = TsidCreator.getTsidFactory1024(null).withCustomEpoch(customEpoch);
	
Tsid tsid = factory.create();
```

### TSID structure

The term TSID stands for (roughly) Time Sortable ID. A TSID is a number that is formed by the creation time along with random bits.

The TSID has 2 components:

- Time component (42 bits)
- Random component (22 bits)

The time component is the count of milliseconds since 2020-01-01 00:00:00 UTC.

The random component has 2 sub-parts:

- Node ID (0 to 20 bits)
- Counter (2 to 22 bits)

The counter bit length depends on the node identifier bit length. If the node identifier bit length is 10, the counter bit length is limited to 12. In this example, the maximum node identifier value is 2^10 = 1024 and the maximum counter value is 2^12 = 4096. So the maximum TSIDs that can be generated per millisecond is about 4 thousand.

In the generator `TsidFactory` the node identifier uses 10 bits of the random component by default. It's possible to adjust the node bit length to a value between 0 and 20. The counter bit length is affected by the node bit length.

This is the default TSID structure:

```
                                            adjustable
                                           <---------->
|------------------------------------------|----------|------------|
       time (msecs since 2020-01-01)           node       counter
                42 bits                       10 bits     12 bits

- time:    2^42 = ~139 years (with adjustable epoch)
- node:    2^10 = 1,024 (with adjustable bit length)
- counter: 2^12 = 4,096 (initially random)

Note:
The node id is adjustable from 0 to 20 bits.
The node id bit length affects the counter bit length.
```

The node identifier is a random number from 0 to 1023 (default). It can be replaced by a value passed to the `TsidFactory` constructor or method factory.

Another way to replace the random node is by using a system property `tsidcreator.node` or a environment variable `TSIDCREATOR_NODE`.


### System properties and environment variables

##### Node identifier

The `tsidcreator.node` property is used by the `TsidFactory`. If this property or variable exists, its value is returned. Otherwise, a random value is returned. It may be useful if you want to identify each single machine by yourself, instead of allowing the PRNG do it for you.

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

Benchmark
------------------------------------------------------

This section shows benchmarks comparing `TsidCreator` to `java.util.UUID`.

```
=================================================================================
THROUGHPUT (operations/millis)            Mode  Cnt      Score     Error   Units
=================================================================================
Throughput.Uuid03_RandomBased            thrpt    5   2022,604 ±  23,528  ops/ms
---------------------------------------------------------------------------------
Throughput.TsidCreator01_Tsid256         thrpt    5  16075,867 ± 274,681  ops/ms
Throughput.TsidCreator02_Tsid1024        thrpt    5   4093,735 ±   0,272  ops/ms
Throughput.TsidCreator03_Tsid4096        thrpt    5   1024,009 ±   0,252  ops/ms
Throughput.TsidCreator04_Tsid256String   thrpt    5  15379,666 ± 167,178  ops/ms
Throughput.TsidCreator05_Tsid1024String  thrpt    5   4076,510 ±   2,437  ops/ms
Throughput.TsidCreator06_Tsid4096String  thrpt    5   1023,653 ±   0,379  ops/ms
=================================================================================
Total time: 00:09:20
=================================================================================
```

System: JVM 8, Ubuntu 20.04, CPU i5-3330, 8G RAM.

See: [uuid-creator-benchmark](https://github.com/fabiolimace/uuid-creator-benchmark)

Links for generators
-------------------------------------------
* [UUID Creator](https://github.com/f4b6a3/uuid-creator): for generating UUIDs
* [ULID Creator](https://github.com/f4b6a3/ulid-creator): for generating ULIDs
* [KSUID Creator](https://github.com/f4b6a3/ksuid-creator): for generating KSUIDs

