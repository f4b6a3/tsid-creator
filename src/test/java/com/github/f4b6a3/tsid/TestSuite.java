package com.github.f4b6a3.tsid;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.github.f4b6a3.tsid.creator.TimeIdCreator00001Test;
import com.github.f4b6a3.tsid.creator.TimeIdCreator00064Test;
import com.github.f4b6a3.tsid.creator.TimeIdCreator00256Test;
import com.github.f4b6a3.tsid.creator.TimeIdCreator01024Test;
import com.github.f4b6a3.tsid.creator.TimeIdCreator04096Test;
import com.github.f4b6a3.tsid.creator.TimeIdCreator16384Test;
import com.github.f4b6a3.tsid.strategy.timestamp.DefaultTimestampStrategyTest;
import com.github.f4b6a3.tsid.util.TsidTimeTest;
import com.github.f4b6a3.tsid.util.TsidUtilTest;
import com.github.f4b6a3.tsid.util.TsidValidatorTest;
import com.github.f4b6a3.tsid.util.internal.TsidCreatorSettingsTest;
import com.github.f4b6a3.tsid.util.TsidConverterTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	TimeIdCreator00001Test.class,
	TimeIdCreator00064Test.class,
	TimeIdCreator00256Test.class,
	TimeIdCreator01024Test.class,
	TimeIdCreator04096Test.class,
	TimeIdCreator16384Test.class,
	DefaultTimestampStrategyTest.class,
	TsidConverterTest.class,
	TsidTimeTest.class,
	TsidUtilTest.class,
	TsidValidatorTest.class,
	TsidCreatorSettingsTest.class,
})

/**
 * 
 * It bundles all JUnit test cases.
 * 
 * Also see {@link UniquenesTest}. 
 *
 */
public class TestSuite {
}