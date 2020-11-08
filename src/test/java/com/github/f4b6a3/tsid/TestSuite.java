package com.github.f4b6a3.tsid;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.github.f4b6a3.tsid.creator.TimeIdCreator0001Test;
import com.github.f4b6a3.tsid.creator.TimeIdCreator0256Test;
import com.github.f4b6a3.tsid.creator.TimeIdCreator1024Test;
import com.github.f4b6a3.tsid.creator.TimeIdCreator4096Test;
import com.github.f4b6a3.tsid.strategy.timestamp.DefaultTimestampStrategyTest;
import com.github.f4b6a3.tsid.util.TsidTimeTest;
import com.github.f4b6a3.tsid.util.TsidUtilTest;
import com.github.f4b6a3.tsid.util.TsidValidatorTest;
import com.github.f4b6a3.tsid.util.internal.TsidCreatorSettingsTest;
import com.github.f4b6a3.tsid.util.TsidConverterTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	TimeIdCreator0001Test.class,
	TimeIdCreator0256Test.class,
	TimeIdCreator1024Test.class,
	TimeIdCreator4096Test.class,
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