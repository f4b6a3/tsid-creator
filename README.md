
TSID Creator
======================================================

A Java library for generating Time-Sorted Unique Identifiers (TSID).

It brings together ideas from [Twitter's Snowflake](https://github.com/twitter-archive/snowflake/tree/snowflake-2010) and [ULID Spec](https://github.com/ulid/spec).

In summary:

*   Sorted by generation time;
*   Can be stored as an integer of 64 bits;
*   Can be stored as a string of 13 chars;
*   String format is encoded to [Crockford's base32](https://www.crockford.com/base32.html);
*   String format is URL safe, is case insensitive, and has no hyphens;
*   Shorter than UUID, ULID and KSUID.

This project contains a [micro benchmark](https://github.com/f4b6a3/tsid-creator/tree/master/benchmark) and a good amount of [unit tests](https://github.com/f4b6a3/tsid-creator/tree/master/src/test/java/com/github/f4b6a3/tsid).

The jar file can be downloaded directly from [maven.org](https://repo1.maven.org/maven2/com/github/f4b6a3/tsid-creator/).

Recommended readings:

* [Javadocs](https://javadoc.io/doc/com.github.f4b6a3/tsid-creator)
* [FAQ wiki page](https://github.com/f4b6a3/tsid-creator/wiki)
* [How to not use TSID factories](https://fillumina.wordpress.com/2023/01/19/how-to-not-use-tsid-factories/)
* [Time Sorted IDs: Oracle implementation](https://connor-mcdonald.com/2023/07/11/time-sorted-ids/)
* [The best UUID type for a database Primary Key](https://vladmihalcea.com/uuid-database-primary-key/)
* [The primary key dilemma: ID vs UUID and some practical solutions](https://fillumina.wordpress.com/2023/02/06/the-primary-key-dilemma-id-vs-uuid-and-some-practical-solutions/)
* [The best way to generate a TSID entity identifier with JPA and Hibernate](https://vladmihalcea.com/tsid-identifier-jpa-hibernate/)
* [Primary keys in the DB - what to use? ID vs UUID or is there something else?](https://www.linkedin.com/pulse/primary-keys-db-what-use-id-vs-uuid-something-else-lucas-persson)
* [TSIDs strike the perfect balance between integers and UUIDs for most databases](https://www.foxhound.systems/blog/time-sorted-unique-identifiers/)

Usage
------------------------------------------------------

Create a TSID:

```java
Tsid tsid = TsidCreator.getTsid();
```

Create a TSID as `long`:

```java
long number = TsidCreator.getTsid().toLong(); // 38352658567418872
```

Create a TSID as `String`:

```java
String string = TsidCreator.getTsid().toString(); // 01226N0640J7Q
```

The TSID generator is [thread-safe](https://en.wikipedia.org/wiki/Thread_safety).

### Dependency

Add these lines to your `pom.xml`:

```xml
<!-- https://search.maven.org/artifact/com.github.f4b6a3/tsid-creator -->
<dependency>
  <groupId>com.github.f4b6a3</groupId>
  <artifactId>tsid-creator</artifactId>
  <version>5.2.6</version>
</dependency>
```
See more options in [maven.org](https://search.maven.org/artifact/com.github.f4b6a3/tsid-creator).

### Modularity

Module and bundle names are the same as the root package name.

*   JPMS module name: `com.github.f4b6a3.tsid`
*   OSGi symbolic name: `com.github.f4b6a3.tsid`

### TSID as Long

The `Tsid.toLong()` method simply unwraps the internal `long` value of a TSID.

```java
long tsid = TsidCreator.getTsid().toLong();
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

The `Tsid.toString()` method encodes a TSID to [Crockford's base 32](https://www.crockford.com/base32.html) encoding. The returned string is 13 characters long.

```java
String tsid = TsidCreator.getTsid().toString();
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

The string format can be useful for languages that store numbers in [double-precision 64-bit binary format IEEE 754](https://en.wikipedia.org/wiki/Double-precision_floating-point_format), such as [Javascript](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Number).

### TSID Structure

The term TSID stands for (roughly) Time-Sorted ID. A TSID is a number that is formed by the creation time along with random bits.

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

- time:    2^42 = ~69 years or ~139 years (with adjustable epoch)
- node:    2^10 = 1,024 (with adjustable bits)
- counter: 2^12 = 4,096 (initially random)

Notes:
The node is adjustable from 0 to 20 bits.
The node bits affect the counter bits.
The time component can be used for ~69 years if stored in a SIGNED 64 bits integer field.
The time component can be used for ~139 years if stored in a UNSIGNED 64 bits integer field.
```

The time component can be 1 ms or more ahead of the system time when necessary to maintain monotonicity and generation speed.

### Node identifier

A simple way to avoid collisions is to make sure that each generator has its exclusive node ID. A "node" as we call it in this library can be a physical machine, a virtual machine, a container, a k8s pod, a running process, a database instance number, etc.

The node ID can be given to `TsidFactory` by defining the `tsidcreator.node` system property or the `TSIDCREATOR_NODE` environment variable. Otherwise, the node identifier will be chosen randomly.

The total number of nodes can be given to `TsidFactory` by defining the `tsidcreator.node.count` system property or the `TSIDCREATOR_NODE_COUNT` environment variable. If this property or variable is set, `TsidFactory` will adjust the amount of bits needed to fit the given node count. For example, if the value 100 is given, the number of bits reserved for the node ID is set to 7, which is the minimum number of bits needed to fit 100 nodes. Otherwise, the default number of bits is set to 10, which can accommodate 1024 nodes.

System properties:

*   `tsidcreator.node`: the node identifier (machine-id).
*   `tsidcreator.node.count`: the total number of nodes.

Environment variables:

*   `TSIDCREATOR_NODE`: the node identifier (machine-id).
*   `TSIDCREATOR_NODE_COUNT`: the total number of nodes.

Using system properties:

```bash
// append to VM arguments
// node identifier: 1 of 1024
// default node count is 1024
-Dtsidcreator.node="1"
```

```bash
// append to VM arguments
// node identifier: 1 of 64
-Dtsidcreator.node="1" \
-Dtsidcreator.node.count="64"
```

Using environment variables:

```bash
# append to ~/.profile
# node identifier: 1 of 1024
# default node count is 1024
export TSIDCREATOR_NODE="1"
```

```bash
# append to ~/.profile
# node identifier: 1 of 64
export TSIDCREATOR_NODE="1"
export TSIDCREATOR_NODE_COUNT="64"
```

```bash
# append to ~/.profile
# node identifier: x of 256
# where x is the last part of the host's IPv4 (if it can be resolved)
# for example, if the host address is 192.168.0.42, the value of x is 42
export TSIDCREATOR_NODE="`hostname --ip-address | awk -F. '{print $4}'`"
export TSIDCREATOR_NODE_COUNT="256"
```

```bash
# append to ~/.profile
# node identifier: x of 1024
# remember that the default node count is 1024 and the maximum number of IDs is 4096K/second/node
# where x is the MODULO 1024 (2^10) of the first host's IPv4 (if there's 1 or more addresses)
# for example, if the first address of the host is 192.168.1.1, the value of x is 257 (1*256 + 1)
export TSIDCREATOR_NODE="`hostname -I | awk '{print $1}' | awk -F. '{print ($3*256 + $4) % 1024}'`"
```

```bash
# append to ~/.profile
# node identifier: x of 65536
# note that the maximum number of IDs per ms per node is reduced to 64, i.e., 64K/second/node
# where x is the MODULO 65536 (2^16) of the first host's IPv4 (if there's 1 or more addresses)
# for example, if the host address is 10.42.10.1 (e.g. k8s pod), the value of x is 2561 (10*256 + 1)
export TSIDCREATOR_NODE="`hostname -I | awk '{print $1}' | awk -F. '{print ($3*256 + $4) % 65536}'`"
export TSIDCREATOR_NODE_COUNT="65536"
```

**Notes:** 
1. As a reference, [6,000 tweets are posted on Twitter every second as of 2022](https://www.demandsage.com/twitter-statistics/);
1. According to the `hostname` manual:
    * `--ip-address` or `-i` (lowercase): _Display the network address(es) of the host name. Note that this works only if the host name can be resolved. Avoid using this option; use hostname --all-ip-addresses instead_.
    * `--all-ip-addresses` or `-I` (uppercase): _Display all network addresses of the host. This option enumerates all configured addresses on all network interfaces. The loopback interface and IPv6 link-local addresses are omitted. Contrary to option -i, this option does not depend on name resolution. Do not make any assumptions about the order of the output._

### More Examples

Create a quick TSID:

```java
Tsid tsid = Tsid.fast();
```

---

Create a TSID from a canonical string (13 chars):

```java
Tsid tsid = Tsid.from("0123456789ABC");
```

---

Convert a TSID into a canonical string in lower case:

```java
String string = tsid.toLowerCase(); // 0123456789abc
```

---

Get the creation instant of a TSID:

```java
Instant instant = tsid.getInstant(); // 2020-04-15T22:31:02.458Z
```

---

Encode a TSID to base-62:

```java
String string = tsid.encode(62); // 0T5jFDIkmmy
```

---

Format a TSID to a string starting with a letter, where "K" is the letter and "%S" is a placeholder:

```java
String string = tsid.format("K%S"); // K0AWE5HZP3SKTK
```

---

A key generator that makes substitution easy if necessary:

```java
package com.example;

import com.github.f4b6a3.tsid.TsidCreator;

public class KeyGenerator {
    public static String next() {
        return TsidCreator.getTsid().toString();
    }
}
```
```java
String key = KeyGenerator.next();
```

---

A `TsidFactory` with a FIXED node identifier:

```java
int node = 256; // max: 2^10
TsidFactory factory = new TsidFactory(node);

// use the factory
Tsid tsid = factory.create();
```

---

A `TsidFactory` with a FIXED node identifier and CUSTOM node bits:

```java
// setup a factory for up to 64 nodes and 65536 ID/ms.
TsidFactory factory = TsidFactory.builder()
    .withNodeBits(6)      // max: 20
    .withNode(63)         // max: 2^nodeBits
    .build();

// use the factory
Tsid tsid = factory.create();
```

---

A `TsidFactory` with a CUSTOM epoch:

```java
// use a CUSTOM epoch that starts from the fall of the Berlin Wall
Instant customEpoch = Instant.parse("1989-11-09T00:00:00Z");
TsidFactory factory = TsidFactory.builder().withCustomEpoch(customEpoch).build();

// use the factory
Tsid tsid = factory.create();
```

---

A `TsidFactory` with `java.util.Random`:

```java
// use a `java.util.Random` instance for fast generation
TsidFactory factory = TsidFactory.builder().withRandom(new Random()).build();

// use the factory
Tsid tsid = factory.create();
```

---

A `TsidFactory` with `RandomGenerator` (JDK 17+):

```java
// use a random function that returns an int value
RandomGenerator random = RandomGenerator.getDefault();
TsidFactory factory = TsidFactory.builder()
    .withRandomFunction(() -> random.nextInt())
    .build();

// use the factory
Tsid tsid = factory.create();
```

---

A `TsidFactory` with `ThreadLocalRandom`:

```java
// use a random function that returns an int value
TsidFactory factory = TsidFactory.builder()
    .withRandomFunction(() -> ThreadLocalRandom.current().nextInt())
    .build();

// use the factory
Tsid tsid = factory.create();
```

---

A `TsidFactory` that creates TSIDs similar to [Twitter Snowflakes](https://github.com/twitter-archive/snowflake):

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

---

A `TsidFactory` that creates TSIDs similar to [Discord Snowflakes](https://discord.com/developers/docs/reference#snowflakes):

```java
// Discord Snowflakes have 5 bits for worker ID and 5 bits for process ID
int worker = 1;  // max: 2^5-1 = 31
int process = 1; // max: 2^5-1 = 31
int node = (worker << 5 | process); // max: 2^10-1 = 1023

// Discord Epoch starts in the first millisecond of 2015
Instant customEpoch = Instant.parse("2015-01-01T00:00:00.000Z");

// a factory that returns TSIDs similar to Discord Snowflakes
TsidFactory factory = TsidFactory.builder()
		.withCustomEpoch(customEpoch)
		.withNode(node)
		.build();

// use the factory
Tsid tsid = factory.create();
```

---

Benchmark
------------------------------------------------------

This section shows benchmarks comparing `TsidCreator` to `java.util.UUID`.

```
---------------------------------------------------------------------------
THROUGHPUT (operations/msec)       Mode  Cnt      Score      Error   Units
---------------------------------------------------------------------------
UUID_randomUUID                   thrpt    5   1630,938 ±  183,581  ops/ms
UUID_randomUUID_toString          thrpt    5   1604,916 ±  189,711  ops/ms
-  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  - 
Tsid_fast                         thrpt    5  37397,739 ± 1128,756  ops/ms
Tsid_fast_toString                thrpt    5  21144,662 ±  673,939  ops/ms
-  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  - 
TsidCreator_getTsid256            thrpt    5  10727,236 ±  761,920  ops/ms
TsidCreator_getTsid256_toString   thrpt    5   6813,193 ±  867,041  ops/ms
-  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  - 
TsidCreator_getTsid1024           thrpt    5  12146,561 ± 1533,959  ops/ms
TsidCreator_getTsid1024_toString  thrpt    5   6507,373 ±  729,444  ops/ms
-  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  - 
TsidCreator_getTsid4096           thrpt    5  11589,976 ± 1757,076  ops/ms
TsidCreator_getTsid4096_toString  thrpt    5   6497,042 ± 1339,480  ops/ms
---------------------------------------------------------------------------
Total time: 00:03:22
---------------------------------------------------------------------------
```

Number of threads used in this the benchmark: 4.

System: CPU i7-8565U, 16G RAM, Ubuntu 22.04, JVM 11, rng-tools installed.

To execute the benchmark, run `./benchmark/run.sh`.

Ports, forks, implementations and other OSS
------------------------------------------------------

Ports, forks and implementations:

| Language | Name |
| -------- | ---- |
| Go       | [vishal-bihani/go-tsid](https://github.com/vishal-bihani/go-tsid) |
| Java     | [vladmihalcea/hypersistence-tsid](https://github.com/vladmihalcea/hypersistence-tsid) |
| Java     | [vincentdaogithub/tsid](https://github.com/vincentdaogithub/tsid) |
| .NET     | [kgkoutis/TSID.Creator.NET](https://github.com/kgkoutis/TSID.Creator.NET) |
| PHP      | [odan/tsid](https://github.com/odan/tsid) |
| Python   | [luismedel/tsid-python](https://github.com/luismedel/tsid-python) |
| Rust     | [jakudlaty/tsid](https://github.com/jakudlaty/tsid)
| TypeScript  | [yubintw/tsid-ts](https://github.com/yubinTW/tsid-ts) |

Other OSS:

| Language | Name |
| -------- | ---- |
| Java     | [fillumina/id-encryptor](https://github.com/fillumina/id-encryptor) |
| .NET     | [ullmark/hashids.net](https://github.com/ullmark/hashids.net) |

License
------------------------------------------------------

This library is Open Source software released under the [MIT license](https://opensource.org/licenses/MIT).
