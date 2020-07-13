package com.github.f4b6a3.tsid;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.github.f4b6a3.tsid.creator.TimeIdCreatorTest;
import com.github.f4b6a3.tsid.strategy.timestamp.DefaultTimestampStrategyTest;
import com.github.f4b6a3.tsid.util.TsidTimeTest;
import com.github.f4b6a3.tsid.util.TsidUtilTest;
import com.github.f4b6a3.tsid.util.TsidValidatorTest;
import com.github.f4b6a3.tsid.util.TsidSettingsTest;
import com.github.f4b6a3.tsid.util.TsidConverterTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	TimeIdCreatorTest.class,
	DefaultTimestampStrategyTest.class,
	TsidConverterTest.class,
	TsidTimeTest.class,
	TsidUtilTest.class,
	TsidValidatorTest.class,
	TsidSettingsTest.class,
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