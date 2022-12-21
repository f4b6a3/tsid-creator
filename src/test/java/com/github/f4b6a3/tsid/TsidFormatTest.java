package com.github.f4b6a3.tsid;

import static org.junit.Assert.*;

import org.junit.Test;

import static com.github.f4b6a3.tsid.Tsid.BaseN.encode;

public class TsidFormatTest {

	@Test
	public void testFormat() {

		Tsid tsid = Tsid.fast();

		String[][] string = { //
				{ "HEAD", "TAIL" }, //
				{ "HEAD", "" }, //
				{ "", "TAIL" }, //
				{ "", "" } //
		};

		String format;
		String formatted;

		// '%S': upper case
		for (int i = 0; i < string.length; i++) {
			String head = string[i][0];
			String tail = string[i][1];

			// '%S': canonical string in upper case
			format = head + "%S" + tail;
			formatted = head + tsid.toString() + tail;
			assertEquals(formatted, tsid.format(format));

			// '%s': canonical string in lower case
			format = head + "%s" + tail;
			formatted = head + tsid.toLowerCase() + tail;
			assertEquals(formatted, tsid.format(format));

			// '%X': hexadecimal in upper case
			format = head + "%X" + tail;
			formatted = head + encode(tsid, 16) + tail;
			assertEquals(formatted, tsid.format(format));

			// '%x': hexadecimal in lower case
			format = head + "%x" + tail;
			formatted = head + encode(tsid, 16).toLowerCase() + tail;
			assertEquals(formatted, tsid.format(format));

			// '%d': base-10
			format = head + "%d" + tail;
			formatted = head + encode(tsid, 10) + tail;
			assertEquals(formatted, tsid.format(format));

			// '%z': base-62
			format = head + "%z" + tail;
			formatted = head + encode(tsid, 62) + tail;
			assertEquals(formatted, tsid.format(format));
		}

		try {
			tsid.format((String) null);
			fail();
		} catch (IllegalArgumentException e) {
			// success
		}

		try {
			tsid.format("");
			fail();
		} catch (IllegalArgumentException e) {
			// success
		}

		try {
			tsid.format("INVALID");
			fail();
		} catch (IllegalArgumentException e) {
			// success
		}

		try {
			tsid.format((String) null);
			fail();
		} catch (IllegalArgumentException e) {
			// success
		}

		try {
			tsid.format("");
			fail();
		} catch (IllegalArgumentException e) {
			// success
		}

		try {
			tsid.format("INVALID");
			fail();
		} catch (IllegalArgumentException e) {
			// success
		}
	}

	@Test
	public void testUnformat() {

		Tsid tsid = Tsid.fast();

		String[][] string = { //
				{ "HEAD", "TAIL" }, //
				{ "HEAD", "" }, //
				{ "", "TAIL" }, //
				{ "", "" } //
		};

		String format;
		String formatted;

		for (int i = 0; i < string.length; i++) {
			String head = string[i][0];
			String tail = string[i][1];

			// '%S': canonical string in upper case
			format = head + "%S" + tail;
			formatted = head + tsid.toString() + tail;
			assertEquals(tsid, Tsid.unformat(formatted, format));

			// '%s': canonical string in lower case
			format = head + "%s" + tail;
			formatted = head + tsid.toLowerCase() + tail;
			assertEquals(tsid, Tsid.unformat(formatted, format));

			// '%X': hexadecimal in upper case
			format = head + "%X" + tail;
			formatted = head + encode(tsid, 16) + tail;
			assertEquals(tsid, Tsid.unformat(formatted, format));

			// '%x': hexadecimal in lower case
			format = head + "%x" + tail;
			formatted = head + encode(tsid, 16).toLowerCase() + tail;
			assertEquals(tsid, Tsid.unformat(formatted, format));

			// '%d': base-10
			format = head + "%d" + tail;
			formatted = head + encode(tsid, 10) + tail;
			assertEquals(tsid, Tsid.unformat(formatted, format));

			// '%z': base-62
			format = head + "%z" + tail;
			formatted = head + encode(tsid, 62) + tail;
			assertEquals(tsid, Tsid.unformat(formatted, format));
		}

		try {
			Tsid.unformat(null, "%s");
			fail();
		} catch (IllegalArgumentException e) {
			// success
		}

		try {
			Tsid.unformat("", null);
			fail();
		} catch (IllegalArgumentException e) {
			// success
		}

		try {
			Tsid.unformat("", "");
			fail();
		} catch (IllegalArgumentException e) {
			// success
		}

		try {
			Tsid.unformat("", "%s");
			fail();
		} catch (IllegalArgumentException e) {
			// success
		}

		try {
			Tsid.unformat("INVALID", "%s");
			fail();
		} catch (IllegalArgumentException e) {
			// success
		}
	}

	@Test
	public void testFormatAndUnformat() {

		Tsid tsid = Tsid.fast();

		String[][] string = { //
				{ "HEAD", "TAIL" }, //
				{ "HEAD", "" }, //
				{ "", "TAIL" }, //
				{ "", "" } //
		};

		String format;
		String formatted;

		for (int i = 0; i < string.length; i++) {

			String head = string[i][0];
			String tail = string[i][1];

			// '%S': canonical string in upper case
			format = head + "%S" + tail;
			formatted = head + tsid.toString() + tail;
			assertEquals(formatted, Tsid.unformat(formatted, format).format(format));
			assertEquals(tsid, Tsid.unformat(tsid.format(format), format));

			// '%s': canonical string in lower case
			format = head + "%s" + tail;
			formatted = head + tsid.toLowerCase() + tail;
			assertEquals(formatted, Tsid.unformat(formatted, format).format(format));
			assertEquals(tsid, Tsid.unformat(tsid.format(format), format));

			// '%X': hexadecimal in upper case
			format = head + "%X" + tail;
			formatted = head + encode(tsid, 16) + tail;
			assertEquals(formatted, Tsid.unformat(formatted, format).format(format));
			assertEquals(tsid, Tsid.unformat(tsid.format(format), format));

			// '%x': hexadecimal in lower case
			format = head + "%x" + tail;
			formatted = head + encode(tsid, 16).toLowerCase() + tail;
			assertEquals(formatted, Tsid.unformat(formatted, format).format(format));
			assertEquals(tsid, Tsid.unformat(tsid.format(format), format));

			// '%z': base-62
			format = head + "%z" + tail;
			formatted = head + encode(tsid, 62) + tail;
			assertEquals(formatted, Tsid.unformat(formatted, format).format(format));
			assertEquals(tsid, Tsid.unformat(tsid.format(format), format));

			// '%d': base-10
			format = head + "%d" + tail;
			formatted = head + encode(tsid, 10) + tail;
			assertEquals(formatted, Tsid.unformat(formatted, format).format(format));
			assertEquals(tsid, Tsid.unformat(tsid.format(format), format));
		}
	}
}
