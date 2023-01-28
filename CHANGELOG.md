# Changelog

All notable changes to this project will be documented in this file.

## [Unreleased]

Nothing unreleased.

## [5.2.1] - 2023-01-28

Fix incremental generation (by Francesco Illuminati) #24
Validate the node ID passed to the builder #23

## [5.2.0] - 2022-12-23

Add simple format methods #20

## [5.1.1] - 2022-12-18

Docs update

## [5.1.0] - 2022-12-17

-   Added the system property and environment variable to configure the node count. #19
-   Added a fast method to generate identifiers. #18

## [5.0.2] - 2022-09-17

Rewrite docs. #17

## [5.0.1] - 2022-08-21

Optimized comparison and hash. #16

## [5.0.0] - 2022-07-09

Add support for RandomGenerator in Java 17. #15

## [4.2.1] - 2022-04-21

Handle clock drift #14

## [4.2.0] - 2022-04-14

Handle clock drift #14

## [4.1.4] - 2021-11-28

Regular maintenance.

## [4.1.3] - 2021-11-20

Increment the counter when the random function returns null or empty. (undone in v5.0.0)

## [4.1.2] - 2021-11-19

Compare internal field as unsigned integers.

## [4.1.1] - 2021-10-03

Regular maintenance.

## [4.1.0] - 2021-09-04

Add OSGi entries to Manifest.MF #11

Module and bundle names are the same as the root package name.

The OSGi symbolic name is the same as the JPMS module name: `com.github.f4b6a3.tsid`.

## [4.0.0] - 2021-08-08

Now you call `TsidFactory.newInstance1024()` and it's variants to get a new `TsidFactory`.

### Added

-   Added `TsidFactory.Builder`
-   Added `TsidFactory.newInstance256()`
-   Added `TsidFactory.newInstance256(int)`
-   Added `TsidFactory.newInstance1024()`
-   Added `TsidFactory.newInstance1024(int)`
-   Added `TsidFactory.newInstance4096()`
-   Added `TsidFactory.newInstance4096(int)`
-   Added benchmark code to compare TSID with UUID

### Removed

-   Removed `TsidTime`
-   Removed `Tsid.toUpperCase()`

## [3.0.3] - 2021-07-18

Regular mantainence

## [3.0.2] - 2021-07-17

Module name for Java 9+ #7

-   Added

-   Added this line to MANIFEST.MF: `Automatic-Module-Name: com.github.f4b6a3.tsid`

### Upated

-   Updated README.md
-   Updated pom.xml

## [3.0.1] - 2021-01-31

Updates README.md and javadoc

## [3.0.0] - 2021-01-30

### Added

Simplifies the `tsid-creator`.

The version 3.0.0 breaks compatibility.

Teste coverage: 82,2%

### Added

-   Added `Tsid`
-   Added `TsidFactory`
-   Added test cases

### Changed

-   Updated README.md
-   Updated test cases
-   Updated javadoc

### Removed

-   Removed `TimeIdCreator`
-   Removed `InvalidTsidException`
-   Removed `TimestampStrategy`
-   Removed `DefaultTimestampStrategy`
-   Removed `FixedTimestampStrategy`
-   Removed `TsidConverter`
-   Removed `TsidUtil`
-   Remvoed `TsidValidator`
-   Removed test cases

## [2.4.4] - 2020-11-16

### Added

-   Added `TimeIdCreator00064Test` // test with up to 64 nodes
-   Added `TimeIdCreator16384Test` // test with up to 16k nodes

### Changed

-   Change `TsidUtil` // remove unused code
-   Change `TsidUtilTest` // remove unused code
-   Optimize `TimeIdCreator` // small optimization
-   Optimize `TsidConverter` // small optimization
-   Optimize `TsidValidator` // add methods for char[] args
-   Update README.md

## [2.4.3] - 2020-11-09

Changed `TsidValidator` to check the maximum value.

Optimization of the TSID validation.

## [2.4.2] - 2020-11-08

Changed `TsidValidator` to check the maximum value.

## [2.4.1] - 2020-11-08

Changed `validate()` access to public.

## [2.4.0] - 2020-11-08

Optimized the generation of TSID in string format.

Now the generation of TSID in string format is 27% faster than before.

Coverage 94%

### Added

-   Add test cases

### Changed

-   Rename `TsidSettings` to `TsidCreatorSettings`
-   Optimize `TimeIdCreator`
-   Optimize `TsidConverter`
-   Optimize `TsidValidator`
-   Update README.md

## [2.3.0] - 2020-10-17

### Added

-   Added `TsidCreator.toString()`

### Changed

-   Change `TimeIdCreator` // wait the next millisecond like Snowflake does
-   Update test cases
-   Update README.md
-   Update javadoc

### Removed

-   Remove `TsidCreatorException`

## [2.2.4] - 2020-07-15

### Changed

Change `TsidSetting`.

### Changed 

-   Chenge `TsidSettings.getNodeIdentifier()` // return null if not found
-   Chenge `TimeIdCreator.getNodeIdentifier()` // check if return is null
-   Update javadoc
-   Update test cases

## [2.2.3] - 2020-07-14

Add more test cases for 256, 1024 and 4096 generators

Coverage: 91.8%

### Added

-   Add `TimeIdCreator0001Test`
-   Add `TimeIdCreator0256Test`
-   Add `TimeIdCreator1024Test`
-   Add `TimeIdCreator4096Test`

### Changed

-   Update TestSuite
-   Update README.md

### Removed
-   Remove `TimeIdCreatorTest`

## [2.2.2] - 2020-07-13

Make TsidSettings.getNodeIdentifier() to return -1 when property is not
found, empty or invalid. ZERO is a valid node ID.

## Changed

-   Changed `TsidSettings.getNodeIdentifier()`

## [2.2.1] - 2020-07-13

### Renamed

-   Rename `TsidCreator.getTsid()` to `getTsid1024()`
-   Rename `TsidCreator.getTsidString()` to `getTsidString1024()`
-   Rename `TsidCreator.getTimeIdCreator()` to `getTimeIdCreator1024()`
-   Rename `TsidCreator.getTimeIdCreator(node)` to `getTimeIdCreator1024(node)`

## [2.2.0] - 2020-07-13

The TSID structure without node id was removed.

Now its possible to define the node id using a system property `tsidcreator.node` or an environment variable `TSIDCREATOR_NODE`.

### Added

-   Added `TsidCreator.getTsid256()` // up to 256 nodes
-   Added `TsidCreator.getTsid4096()` // up to 4096 nodes
-   Added `TsidCreator.getTsidString256()` // up to 256 nodes
-   Added `TsidCreator.getTsidString4096()` // up to 4096 nodes
-   Added `TsidCreator.getTimeIdCreator256()` // up to 256 nodes
-   Added `TsidCreator.getTimeIdCreator4096()` // up to 4096 nodes
-   Added `TsidSetings` // for node id definition via property or variable
-   Added `TimeIdCreator` // unique generator for all types of TSID

### Changed

-   Renamed `TsidTimeUtil` to `TsidTime`
-   Update README.md
-   Update test cases
-   Update pom.xml

### Removed

-   Removed `TsidCreator.getTsid(int node)` // use TimeIdCreator instead
-   Removed `TsidCreator.getTsidString(int node)` // use TimeIdCreator instead
-   Removed `TimeIdCreator` (interface) // there's generator hierarchy anymore
-   Removed `AbstractTimeIdCreator` // there's generator hierarchy anymore
-   Removed `SimpleTimeIdCreator` // there's generator hierarchy anymore
-   Removed `NodeTimeIdCreator` // there's generator hierarchy anymore
-   Removed `AjustableTimeIdCreator` // there's generator hierarchy anymore

## [2.1.1] - 2020-07-06

### Fixed

-   Fixed `AbstractTimeIdCrator.increment()`

## [2.1.0] - 2020-07-04

### Added

-   Added `TsidCrator.fromString()`
-   Add test cases

### Changed

-   Renamed `DefaultTimeIdCreator` to `SimpleTimeIdCreator`
-   Renamed `AdjustableNodeTimeIdCreator` to `AdjustableTimeIdCreator`
-   Optimized `AbstractTimeIdCreator`
-   Optimize `SimpleTimeIdCreator`
-   Optimized `AdjustableTimeIdCreator`
-   Optimized `NodeTimeIdCreator`
-   Optimized `TsidConverter.fromString()`
-   Optimize `TsidConverter.toString()`
-   Optimize `TsidValidator.isValid(String)`
-   Update test cases
-   Update README.md

### Removed
-   Removed `TsidConverter.fromBytes()`
-   Remove `TsidConverter.toBytes()`
-   Remove `TsidValidator.isValid(byte[])`

## [2.0.0] - 2020-05-25

### Added
-   Added `AbstractTimeIdCreator`
-   Added `DefaultTimeIdCreator`
-   Added `NodeTimeIdCreator`
-   Added `AdjustableNodeTimeIdCreator`

### Changed:
-   Renamed `TimeSortableIdCreator` to `TimeIdCreator`
-   Updated `TsidCreator`
-   Updated README.md
-   Updated test cases

## [1.0.2] - 2020-04-19

### Changed

-   Updated pom.xml

## [1.0.1] - 2020-04-18

### Fixed

-   Fixed package name in two test cases

## [1.0.0] - 2020-04-17

Project created as an alternative Java implementation for Twitter's Snowflake.

### Added

-   Added `TsidCreator`
-   Added `TimeSortableIdCreator`
-   Added `TimestampStrategy`
-   Added `DefaultTimestampStrategy`
-   Added `FixedTimestampStrategy`
-   Added `TsidUtil`
-   Added `TsidConverter`
-   Added `TsidTimeUtil`
-   Added `TsidValidator`
-   Added `TsidCreatorException`
-   Added `InvalidTsidException`
-   Added `README.md`
-   Added `pom.xml`
-   Added `LICENSE`
-   Added test cases

[unreleased]: https://github.com/f4b6a3/tsid-creator/compare/tsid-creator-5.2.1...HEAD
[5.2.1]: https://github.com/f4b6a3/tsid-creator/compare/tsid-creator-5.2.0...tsid-creator-5.2.1
[5.2.0]: https://github.com/f4b6a3/tsid-creator/compare/tsid-creator-5.1.1...tsid-creator-5.2.0
[5.1.1]: https://github.com/f4b6a3/tsid-creator/compare/tsid-creator-5.1.0...tsid-creator-5.1.1
[5.1.0]: https://github.com/f4b6a3/tsid-creator/compare/tsid-creator-5.0.2...tsid-creator-5.1.0
[5.0.2]: https://github.com/f4b6a3/tsid-creator/compare/tsid-creator-5.0.1...tsid-creator-5.0.2
[5.0.1]: https://github.com/f4b6a3/tsid-creator/compare/tsid-creator-5.0.0...tsid-creator-5.0.1
[5.0.0]: https://github.com/f4b6a3/tsid-creator/compare/tsid-creator-4.2.1...tsid-creator-5.0.0
[4.2.1]: https://github.com/f4b6a3/tsid-creator/compare/tsid-creator-4.2.0...tsid-creator-4.2.1
[4.2.0]: https://github.com/f4b6a3/tsid-creator/compare/tsid-creator-4.1.4...tsid-creator-4.2.0
[4.1.4]: https://github.com/f4b6a3/tsid-creator/compare/tsid-creator-4.1.3...tsid-creator-4.1.4
[4.1.3]: https://github.com/f4b6a3/tsid-creator/compare/tsid-creator-4.1.2...tsid-creator-4.1.3
[4.1.2]: https://github.com/f4b6a3/tsid-creator/compare/tsid-creator-4.1.1...tsid-creator-4.1.2
[4.1.1]: https://github.com/f4b6a3/tsid-creator/compare/tsid-creator-4.1.0...tsid-creator-4.1.1
[4.1.0]: https://github.com/f4b6a3/tsid-creator/compare/tsid-creator-4.0.0...tsid-creator-4.1.0
[4.0.0]: https://github.com/f4b6a3/tsid-creator/compare/tsid-creator-3.0.3...tsid-creator-4.0.0
[3.0.3]: https://github.com/f4b6a3/tsid-creator/compare/tsid-creator-3.0.2...tsid-creator-3.0.3
[3.0.2]: https://github.com/f4b6a3/tsid-creator/compare/tsid-creator-3.0.1...tsid-creator-3.0.2
[3.0.1]: https://github.com/f4b6a3/tsid-creator/compare/tsid-creator-3.0.0...tsid-creator-3.0.1
[3.0.0]: https://github.com/f4b6a3/tsid-creator/compare/tsid-creator-2.4.4...tsid-creator-3.0.0
[2.4.4]: https://github.com/f4b6a3/tsid-creator/compare/tsid-creator-2.4.3...tsid-creator-2.4.4
[2.4.3]: https://github.com/f4b6a3/tsid-creator/compare/tsid-creator-2.4.2...tsid-creator-2.4.3
[2.4.2]: https://github.com/f4b6a3/tsid-creator/compare/tsid-creator-2.4.1...tsid-creator-2.4.2
[2.4.1]: https://github.com/f4b6a3/tsid-creator/compare/tsid-creator-2.4.0...tsid-creator-2.4.1
[2.4.0]: https://github.com/f4b6a3/tsid-creator/compare/tsid-creator-2.3.0...tsid-creator-2.4.0
[2.3.0]: https://github.com/f4b6a3/tsid-creator/compare/tsid-creator-2.2.4...tsid-creator-2.3.0
[2.2.4]: https://github.com/f4b6a3/tsid-creator/compare/tsid-creator-2.2.3...tsid-creator-2.2.4
[2.2.3]: https://github.com/f4b6a3/tsid-creator/compare/tsid-creator-2.2.2...tsid-creator-2.2.3
[2.2.2]: https://github.com/f4b6a3/tsid-creator/compare/tsid-creator-2.2.1...tsid-creator-2.2.2
[2.2.1]: https://github.com/f4b6a3/tsid-creator/compare/tsid-creator-2.2.0...tsid-creator-2.2.1
[2.2.0]: https://github.com/f4b6a3/tsid-creator/compare/tsid-creator-2.1.1...tsid-creator-2.2.0
[2.1.1]: https://github.com/f4b6a3/tsid-creator/compare/tsid-creator-2.1.0...tsid-creator-2.1.1
[2.1.0]: https://github.com/f4b6a3/tsid-creator/compare/tsid-creator-2.0.0...tsid-creator-2.1.0
[2.0.0]: https://github.com/f4b6a3/tsid-creator/compare/tsid-creator-1.0.1...tsid-creator-2.0.0
[1.0.2]: https://github.com/f4b6a3/tsid-creator/compare/tsid-creator-1.0.1...tsid-creator-1.0.2
[1.0.1]: https://github.com/f4b6a3/tsid-creator/compare/tsid-creator-1.0.0...tsid-creator-1.0.1
[1.0.0]: https://github.com/f4b6a3/tsid-creator/releases/tag/tsid-creator-1.0.0
