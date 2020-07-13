package com.github.f4b6a3.tsid.util;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TsidSettingsTest {

	Random random = new Random();

	@BeforeClass
	public static void beforeClass() {
		TsidSettings.clearProperty(TsidSettings.PROPERTY_NODE);
	}

	@AfterClass
	public static void afterClass() {
		TsidSettings.clearProperty(TsidSettings.PROPERTY_NODE);
	}

	@Test
	public void testSetNodeIdentifier() {
		for (int i = 0; i < 100; i++) {
			int number = this.random.nextInt();
			TsidSettings.setNodeIdentifier(number);
			long result = TsidSettings.getNodeIdentifier();
			assertEquals(number, result);
		}
	}

	@Test
	public void testSetProperty() {
		for (int i = 0; i < 100; i++) {
			long number = random.nextInt();
			String string = Long.toString(number);
			TsidSettings.setProperty(TsidSettings.PROPERTY_NODE, string);
			long result = TsidSettings.getNodeIdentifier();
			assertEquals(number, result);
		}
	}

	@Test
	public void testSetPropertyInvalid() {
		String string = "0xx112233445566"; // typo
		TsidSettings.setProperty(TsidSettings.PROPERTY_NODE, string);
		long result = TsidSettings.getNodeIdentifier();
		assertEquals(-1, result);

		string = " 0x112233445566"; // space
		TsidSettings.setProperty(TsidSettings.PROPERTY_NODE, string);
		result = TsidSettings.getNodeIdentifier();
		assertEquals(-1, result);

		string = " 0x1122334455zz"; // non hexadecimal
		TsidSettings.setProperty(TsidSettings.PROPERTY_NODE, string);
		result = TsidSettings.getNodeIdentifier();
		assertEquals(-1, result);

		string = ""; // empty
		TsidSettings.setProperty(TsidSettings.PROPERTY_NODE, string);
		result = TsidSettings.getNodeIdentifier();
		assertEquals(-1, result);

		string = " "; // blank
		TsidSettings.setProperty(TsidSettings.PROPERTY_NODE, string);
		result = TsidSettings.getNodeIdentifier();
		assertEquals(-1, result);
	}
}
