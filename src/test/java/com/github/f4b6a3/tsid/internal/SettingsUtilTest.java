package com.github.f4b6a3.tsid.internal;

import static org.junit.Assert.*;

import java.util.concurrent.ThreadLocalRandom;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.f4b6a3.tsid.internal.SettingsUtil;

public class SettingsUtilTest {

	@BeforeClass
	public static void beforeClass() {
		SettingsUtil.clearProperty(SettingsUtil.PROPERTY_NODE);
	}

	@AfterClass
	public static void afterClass() {
		SettingsUtil.clearProperty(SettingsUtil.PROPERTY_NODE);
	}

	@Test
	public void testSetNodeIdentifier() {
		for (int i = 0; i < 100; i++) {
			int number = ThreadLocalRandom.current().nextInt();
			SettingsUtil.setNode(number);
			long result = SettingsUtil.getNode();
			assertEquals(number, result);
		}
	}

	@Test
	public void testSetProperty() {
		for (int i = 0; i < 100; i++) {
			long number = ThreadLocalRandom.current().nextInt();
			String string = Long.toString(number);
			SettingsUtil.setProperty(SettingsUtil.PROPERTY_NODE, string);
			long result = SettingsUtil.getNode();
			assertEquals(number, result);
		}
	}

	@Test
	public void testSetPropertyInvalid() {
		String string = "0xx11223344"; // typo
		SettingsUtil.setProperty(SettingsUtil.PROPERTY_NODE, string);
		Integer result = SettingsUtil.getNode();
		assertNull(result);

		string = " 0x11223344"; // space
		SettingsUtil.setProperty(SettingsUtil.PROPERTY_NODE, string);
		result = SettingsUtil.getNode();
		assertNull(result);

		string = " 0x112233zz"; // non hexadecimal
		SettingsUtil.setProperty(SettingsUtil.PROPERTY_NODE, string);
		result = SettingsUtil.getNode();
		assertNull(result);

		string = ""; // empty
		SettingsUtil.setProperty(SettingsUtil.PROPERTY_NODE, string);
		result = SettingsUtil.getNode();
		assertNull(result);

		string = " "; // blank
		SettingsUtil.setProperty(SettingsUtil.PROPERTY_NODE, string);
		result = SettingsUtil.getNode();
		assertNull(result);
	}
}
