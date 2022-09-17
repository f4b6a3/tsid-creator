/*
 * MIT License
 * 
 * Copyright (c) 2020-2022 Fabio Lima
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.f4b6a3.tsid.internal;

/**
 * Reads system properties and environment variables.
 * <p>
 * The system properties has prevalence over environment variables.
 * <p>
 * Available properties and variables:
 * <ul>
 * <li>tsidcreator.node
 * <li>TSIDCREATOR_NODE
 * </ul>
 */
public final class SettingsUtil {

	static final String PROPERTY_PREFIX = "tsidcreator";
	static final String PROPERTY_NODE = "node";

	protected SettingsUtil() {
	}

	public static Integer getNode() {
		String value = getProperty(PROPERTY_NODE);

		if (value == null) {
			return null;
		}

		try {
			return Integer.decode(value);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static void setNode(Integer node) {
		String value = Integer.toString(node);
		setProperty(PROPERTY_NODE, value);
	}

	static String getProperty(String name) {

		String fullName = getPropertyName(name);
		String value = System.getProperty(fullName);
		if (!isEmpty(value)) {
			return value;
		}

		fullName = getEnvinronmentName(name);
		value = System.getenv(fullName);
		if (!isEmpty(value)) {
			return value;
		}

		return null;
	}

	static void setProperty(String key, String value) {
		System.setProperty(getPropertyName(key), value);
	}

	static void clearProperty(String key) {
		System.clearProperty(getPropertyName(key));
	}

	static String getPropertyName(String key) {
		return String.join(".", PROPERTY_PREFIX, key);
	}

	static String getEnvinronmentName(String key) {
		return String.join("_", PROPERTY_PREFIX, key).toUpperCase().replace(".", "_");
	}

	private static boolean isEmpty(String value) {
		return value == null || value.isEmpty();
	}
}
