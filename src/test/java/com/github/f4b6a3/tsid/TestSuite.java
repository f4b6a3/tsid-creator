package com.github.f4b6a3.tsid;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.github.f4b6a3.tsid.util.TsidTimeTest;
import com.github.f4b6a3.tsid.util.internal.SettingsUtilTest;
import com.github.f4b6a3.tsid.factory.TsidFactory00001Test;
import com.github.f4b6a3.tsid.factory.TsidFactory00064Test;
import com.github.f4b6a3.tsid.factory.TsidFactory00256Test;
import com.github.f4b6a3.tsid.factory.TsidFactory01024Test;
import com.github.f4b6a3.tsid.factory.TsidFactory04096Test;
import com.github.f4b6a3.tsid.factory.TsidFactory16384Test;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	TsidFactory00001Test.class,
	TsidFactory00064Test.class,
	TsidFactory00256Test.class,
	TsidFactory01024Test.class,
	TsidFactory04096Test.class,
	TsidFactory16384Test.class,
	TsidTimeTest.class,
	TsidTest.class,
	SettingsUtilTest.class,
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