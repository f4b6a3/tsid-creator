package com.github.f4b6a3;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.github.f4b6a3.tsid.TsidCreatorStringTest;
import com.github.f4b6a3.tsid.TsidCreatorTest;
import com.github.f4b6a3.tsid.random.NaiveRandomTest;
import com.github.f4b6a3.tsid.timestamp.DefaultTimestampStrategyTest;
import com.github.f4b6a3.tsid.util.Base32UtilTest;
import com.github.f4b6a3.tsid.util.TsidTimeUtilTest;
import com.github.f4b6a3.tsid.util.TsidUtilTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
   DefaultTimestampStrategyTest.class,
   NaiveRandomTest.class,
   Base32UtilTest.class,
   TsidUtilTest.class,
   TsidCreatorTest.class,
   TsidCreatorStringTest.class,
   TsidTimeUtilTest.class
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