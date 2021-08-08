package com.github.f4b6a3.tsid;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.github.f4b6a3.tsid.factory.TsidFactory00001Test;
import com.github.f4b6a3.tsid.factory.TsidFactory00064Test;
import com.github.f4b6a3.tsid.factory.TsidFactory00256Test;
import com.github.f4b6a3.tsid.factory.TsidFactory01024Test;
import com.github.f4b6a3.tsid.factory.TsidFactory04096Test;
import com.github.f4b6a3.tsid.factory.TsidFactory16384Test;
import com.github.f4b6a3.tsid.internal.SettingsUtilTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	TsidFactory00001Test.class,
	TsidFactory00064Test.class,
	TsidFactory00256Test.class,
	TsidFactory01024Test.class,
	TsidFactory04096Test.class,
	TsidFactory16384Test.class,
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