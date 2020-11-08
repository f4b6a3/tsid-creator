package com.github.f4b6a3.tsid.util.internal;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.f4b6a3.tsid.util.internal.TsidCreatorSettings;

public class TsidCreatorSettingsTest {

	Random random = new Random();

	@BeforeClass
	public static void beforeClass() {
		TsidCreatorSettings.clearProperty(TsidCreatorSettings.PROPERTY_NODE);
	}

	@AfterClass
	public static void afterClass() {
		TsidCreatorSettings.clearProperty(TsidCreatorSettings.PROPERTY_NODE);
	}

	@Test
	public void testSetNodeIdentifier() {
		for (int i = 0; i < 100; i++) {
			int number = this.random.nextInt();
			TsidCreatorSettings.setNodeIdentifier(number);
			long result = TsidCreatorSettings.getNodeIdentifier();
			assertEquals(number, result);
		}
	}

	@Test
	public void testSetProperty() {
		for (int i = 0; i < 100; i++) {
			long number = random.nextInt();
			String string = Long.toString(number);
			TsidCreatorSettings.setProperty(TsidCreatorSettings.PROPERTY_NODE, string);
			long result = TsidCreatorSettings.getNodeIdentifier();
			assertEquals(number, result);
		}
	}

	@Test
	public void testSetPropertyInvalid() {
		String string = "0xx11223344"; // typo
		TsidCreatorSettings.setProperty(TsidCreatorSettings.PROPERTY_NODE, string);
		Integer result = TsidCreatorSettings.getNodeIdentifier();
		assertNull(result);

		string = " 0x11223344"; // space
		TsidCreatorSettings.setProperty(TsidCreatorSettings.PROPERTY_NODE, string);
		result = TsidCreatorSettings.getNodeIdentifier();
		assertNull(result);

		string = " 0x112233zz"; // non hexadecimal
		TsidCreatorSettings.setProperty(TsidCreatorSettings.PROPERTY_NODE, string);
		result = TsidCreatorSettings.getNodeIdentifier();
		assertNull(result);

		string = ""; // empty
		TsidCreatorSettings.setProperty(TsidCreatorSettings.PROPERTY_NODE, string);
		result = TsidCreatorSettings.getNodeIdentifier();
		assertNull(result);

		string = " "; // blank
		TsidCreatorSettings.setProperty(TsidCreatorSettings.PROPERTY_NODE, string);
		result = TsidCreatorSettings.getNodeIdentifier();
		assertNull(result);
	}
}
