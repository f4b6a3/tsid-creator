package com.github.f4b6a3.tsid;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	TsidTest.class,
	TsidFactoryTest.class,
	TsidFactory00001Test.class,
	TsidFactory00064Test.class,
	TsidFactory00256Test.class,
	TsidFactory01024Test.class,
	TsidFactory04096Test.class,
	TsidFactory16384Test.class,
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