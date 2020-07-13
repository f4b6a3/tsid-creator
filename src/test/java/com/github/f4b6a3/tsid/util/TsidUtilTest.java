package com.github.f4b6a3.tsid.util;

import static org.junit.Assert.*;

import java.time.Instant;

import org.junit.Test;

import com.github.f4b6a3.tsid.TsidCreator;
import com.github.f4b6a3.tsid.creator.TimeIdCreator;
import com.github.f4b6a3.tsid.strategy.TimestampStrategy;
import com.github.f4b6a3.tsid.strategy.timestamp.FixedTimestampStretegy;

public class TsidUtilTest {

	private static final int[] NUMBERS = { 102685630, 725393777, 573697669, 614668535, 790665079, 728958755, 966150230,
			410015018, 605266173, 946077566, 214051168, 775737014, 723003700, 391609366, 147844737, 514081413,
			488279622, 550860813, 611087782, 223492126, 706308515, 158990768, 549042286, 26926303, 775714134, 602886016,
			27282100, 675097356, 641101167, 515280699, 454184468, 371424784, 633917378, 887459583, 792903202, 168552040,
			824806922, 696445335, 653338746, 357696553, 353677217, 972662902, 400738139, 537701151, 202077579,
			110209145, 356152341, 168702810, 684185451, 419840003, 480132486, 308833881, 997154252, 918202260,
			103304091, 328467776, 648729690, 733655121, 645189051, 342500864, 560919543, 509761384, 626871960,
			429248550, 319025067, 507317265, 348303729, 256009160, 660250872, 85224414, 414490625, 355994979, 318005886,
			326093128, 492813589, 569014099, 503350412, 168303553, 801566586, 800368918, 742601973, 395588591,
			257341245, 722366808, 501878988, 200718306, 184948029, 149469829, 992401543, 240364551, 976817281,
			161998068, 515579566, 275182272, 376045488, 899163436, 941443452, 974372015, 934795357, 958806784 };

	private static final String[] NUMBERS_BASE_32_CROCKFORD = { "31XPXY", "NKS8BH", "H33VM5", "JA667Q", "QJ15VQ",
			"NQ61S3", "WSCJ2P", "C70N9A", "J1787X", "W67ZVY", "6C4AB0", "Q3SKNP", "NHGA9M", "BNEZ0P", "4CZVM1",
			"FA8GM5", "EHN3J6", "GDAY0D", "J6RXD6", "6N4E0Y", "N1JTD3", "4QM0DG", "GBKE3E", "SNQ6Z", "Q3RXAP", "HYYKW0",
			"T0JNM", "M3TARC", "K3CVBF", "FBD3SV", "DH4KGM", "B26ZGG", "JWHKY2", "TEB3QZ", "QM5FH2", "50QSK8", "RJK3GA",
			"MR5TCQ", "KF2A3T", "AN4119", "AH9BX1", "WZKA3P", "BY5HTV", "G0SARZ", "60PXCB", "393A3S", "AKMX0N",
			"50WCTT", "MCFNVB", "CGCG03", "E9WFC6", "96GVJS", "XPYQEC", "VBN9WM", "32GJWV", "9S81A0", "KANN2T",
			"NVNC2H", "K79KDV", "A6M9G0", "GPXWZQ", "F64NV8", "JNTKMR", "CSBM16", "9G7VXB", "F3T30H", "AC5CBH",
			"7M4RY8", "KNN87R", "2H8TYY", "CB9801", "AKG3B3", "9F8RKY", "9PZJA8", "ENZF8N", "GYMXTK", "F0114C",
			"50G6Y1", "QWDVVT", "QV9A8P", "P46D7N", "BS8CZF", "7NDDSX", "NGWWAR", "EYM46C", "5ZDDZ2", "5GC59X",
			"4EHEM5", "XJDP47", "757B07", "X3J341", "4TFS7M", "FBP7NE", "86DWP0", "B6KZXG", "TSG99C", "W1TJBW",
			"X17F5F", "VVFP2X", "WJCER0" };

	@Test
	public void testExtractUnixMilliseconds() {

		long start = System.currentTimeMillis();
		long tsid = TsidCreator.getTsid1024();
		long middle = TsidUtil.extractUnixMilliseconds(tsid);
		long end = System.currentTimeMillis();

		assertTrue(start <= middle);
		assertTrue(middle <= end);
	}

	@Test
	public void testExtractUnixMillisecondsWithCustomEpoch() {

		Instant customEpoch = Instant.parse("1984-01-01T00:00:00Z");

		long start = System.currentTimeMillis();
		long tsid = TsidCreator.getTimeIdCreator1024().withCustomEpoch(customEpoch).create();
		long middle = TsidUtil.extractUnixMilliseconds(tsid, customEpoch.toEpochMilli());
		long end = System.currentTimeMillis();

		assertTrue(start <= middle);
		assertTrue(middle <= end);
	}

	@Test
	public void testExtractInstant() {

		Instant start = Instant.now();
		long tsid = TsidCreator.getTsid1024();
		Instant middle = TsidUtil.extractInstant(tsid);
		Instant end = Instant.now();

		assertTrue(start.toEpochMilli() <= middle.toEpochMilli());
		assertTrue(middle.toEpochMilli() <= end.toEpochMilli());
	}

	@Test
	public void testExtractInstantWithCustomEpoch() {

		Instant customEpoch = Instant.parse("2015-10-23T00:00:00Z");

		Instant start = Instant.now();
		long tsid = TsidCreator.getTimeIdCreator1024().withCustomEpoch(customEpoch).create();
		Instant middle = TsidUtil.extractInstant(tsid, customEpoch);
		Instant end = Instant.now();

		assertTrue(start.toEpochMilli() <= middle.toEpochMilli());
		assertTrue(middle.toEpochMilli() <= end.toEpochMilli());
	}

	@Test
	public void testExtractNodeIdentifierDefaultBitLength() {

		final int nodeidLength = 10; // default bit length
		final int nodeidMax = (int) Math.pow(2, nodeidLength);

		long timestamp = TsidTime.getCurrentTimestamp();
		TimestampStrategy strategy = new FixedTimestampStretegy(timestamp);

		// The default node identifier bit length is 10 (2^10 = 1024)
		for (int nodeid = 0; nodeid < nodeidMax; nodeid++) {
			TimeIdCreator creator = TsidCreator.getTimeIdCreator1024(nodeid).withTimestampStrategy(strategy);
			long tsid = creator.create();
			int result = TsidUtil.extractNodeIdentifier(tsid);
			assertEquals(nodeid, result);
		}
	}

	@Test
	public void testExtractNodeIdentifierCustomtBitLength() {

		final int nodeidLength = 12;
		final int nodeidMax = (int) Math.pow(2, nodeidLength);

		long timestamp = TsidTime.getCurrentTimestamp();
		TimestampStrategy strategy = new FixedTimestampStretegy(timestamp);

		for (int nodeid = 0; nodeid < nodeidMax; nodeid++) {
			TimeIdCreator creator = TsidCreator.getTimeIdCreator(nodeid, nodeidLength).withTimestampStrategy(strategy);
			long tsid = creator.create();

			int result = TsidUtil.extractNodeIdentifier(tsid, nodeidLength);
			assertEquals(nodeid, result);
		}
	}

	@Test
	public void testToUpperCase() {
		String string = "Aq7zmxKxPc61QKiGRu8Y3PdYMer64lrRxfb9A5JAJuDeEhXSrbsxsaUoHrFzmEJUYBKJPgV+1rAd";
		char[] chars1 = string.toCharArray();
		char[] chars2 = TsidUtil.toUpperCase(chars1);
		assertEquals(new String(string).toUpperCase(), new String(chars2));

		string = "kL9zTzZfzlwKYCEmWKPxFYxINf6JZCSmSqykyG5ONWZcFkJG2WGc7gq71YCEzt2hYcsTvfQqEmn0";
		chars1 = string.toCharArray();
		chars2 = TsidUtil.toUpperCase(chars1);
		assertEquals(new String(string).toUpperCase(), new String(chars2));

		string = "XXEOUV3jJb3f+wpRPDVke9NgWwEgdkzChnKnpZZWS/mCSqTi757GmmqYdzuDGOa5ftqHI3/zqKrS";
		chars1 = string.toCharArray();
		chars2 = TsidUtil.toUpperCase(chars1);
		assertEquals(new String(string).toUpperCase(), new String(chars2));

		string = "t9LVRQZbCxTQgaxlajNE/VYpLpKiHtKt7jHrtxSDIJ2hrHaJI2UPF1zA7I35m9cKz01lHYD1IXlM";
		chars1 = string.toCharArray();
		chars2 = TsidUtil.toUpperCase(chars1);
		assertEquals(new String(string).toUpperCase(), new String(chars2));

		string = "jyS52J42LLT6GY+Zywo1R4tQv4bTfAqpFB6aiKEuA3yDxFkuXzuKe8PaGlUTaXD5WgRFMnO9nRLU";
		chars1 = string.toCharArray();
		chars2 = TsidUtil.toUpperCase(chars1);
		assertEquals(new String(string).toUpperCase(), new String(chars2));
	}

	@Test
	public void testZerofill() {
		assertEquals("001", new String(TsidUtil.zerofill("1".toCharArray(), 3)));
		assertEquals("000123", new String(TsidUtil.zerofill("123".toCharArray(), 6)));
		assertEquals("0000000000", new String(TsidUtil.zerofill("".toCharArray(), 10)));
		assertEquals("9876543210", new String(TsidUtil.zerofill("9876543210".toCharArray(), 10)));
		assertEquals("0000000000123456", new String(TsidUtil.zerofill("123456".toCharArray(), 16)));
	}

	@Test
	public void testLpad() {

		String string = "";
		char[] chars1 = string.toCharArray();
		char[] chars2 = TsidUtil.lpad(chars1, 8, 'x');
		assertEquals("xxxxxxxx", new String(chars2));

		string = "";
		chars1 = string.toCharArray();
		chars2 = TsidUtil.lpad(chars1, 12, 'W');
		assertEquals("WWWWWWWWWWWW", new String(chars2));

		string = "TCgpYATMlK9BmSzX";
		chars1 = string.toCharArray();
		chars2 = TsidUtil.lpad(chars1, 13, '0');
		assertEquals(string, new String(chars2));

		string = "2kgy3m9U646L6TJ5";
		chars1 = string.toCharArray();
		chars2 = TsidUtil.lpad(chars1, 16, '0');
		assertEquals(string, new String(chars2));

		string = "2kgy3m9U646L6TJ5";
		chars1 = string.toCharArray();
		chars2 = TsidUtil.lpad(chars1, 17, '0');
		assertEquals("0" + string, new String(chars2));

		string = "LH6hfYcGJu06xSNF";
		chars1 = string.toCharArray();
		chars2 = TsidUtil.lpad(chars1, 25, '0');
		assertEquals("000000000" + string, new String(chars2));

		string = "t9LVRQZbCxTQgaxlajNE/VYpLpKiHtKt7jHrtxSDIJ2hrHaJI2UPF1zA7I35m9cKz01lHYD1IXlM";
		chars1 = string.toCharArray();
		chars2 = TsidUtil.lpad(chars1, 80, '0');
		assertEquals("0000" + string, new String(chars2));
	}

	@Test
	public void testRemoveHyphens() {
		String string = "-ZGQ8yCsza-RFxlYyA-FaXa4wd-k4Owa-/ITDvqWOl4-Do3/NwW--Lawx6GcO-LmSRDsd3af3Zt-VMNnvLgIw9-";
		char[] chars1 = string.toCharArray();
		char[] chars2 = TsidUtil.removeHyphens(chars1);
		assertEquals(string.replace("-", ""), new String(chars2));

		string = "qi3q-EMvc1-Kk7XzYMj----SUnwf-lp0K7-Ucj-W-cDplP-2dG-3x+5y-r9JBc-ZT-0e--cRHoMbU/lBzZsJ6rcJ5zT/J";
		chars1 = string.toCharArray();
		chars2 = TsidUtil.removeHyphens(chars1);
		assertEquals(string.replace("-", ""), new String(chars2));

		string = "RXMD0DJV---Zf3Jqcv39uGjzBuiLkLNL-IvPnTyfMteEet-I7u-Z8oyE+BIUBf/OPi30iICP1TnQpMve4j";
		chars1 = string.toCharArray();
		chars2 = TsidUtil.removeHyphens(chars1);
		assertEquals(string.replace("-", ""), new String(chars2));

		string = "---dFH-b-ylQPA60-kuRxZ9-6q5MLd1-qLTKdma-rF2yEABt-t6mJg0U-ibIYcVnt-Guqdn-z-G-43Ob-W/-Gxah1+53a-";
		chars1 = string.toCharArray();
		chars2 = TsidUtil.removeHyphens(chars1);
		assertEquals(string.replace("-", ""), new String(chars2));

		string = "-------------------------------";
		chars1 = string.toCharArray();
		chars2 = TsidUtil.removeHyphens(chars1);
		assertEquals(string.replace("-", ""), new String(chars2));
	}

	@Test
	public void testTransliterate() {

		char[] alphabetCrockford = "0123456789ABCDEFGHJKMNPQRSTVWXYZ".toCharArray();
		char[] alphabetDefault = "0123456789abcdefghijklmnopqrstuv".toCharArray();

		char[] chars2 = TsidUtil.transliterate(alphabetCrockford, alphabetCrockford, alphabetDefault);
		assertEquals(new String(alphabetDefault), new String(chars2));

		chars2 = TsidUtil.transliterate(alphabetDefault, alphabetDefault, alphabetCrockford);
		assertEquals(new String(alphabetCrockford), new String(chars2));
	}

	@Test
	public void testIsBase32Crockford() {
		assertTrue(TsidUtil.isCrockfordBase32("16JD".toCharArray()));
		assertTrue(TsidUtil.isCrockfordBase32("01BX5ZZKBKACTAV9WEVGEMMVRY".toCharArray()));
		assertTrue(TsidUtil.isCrockfordBase32(TsidUtil.ALPHABET_CROCKFORD));
		assertFalse(TsidUtil.isCrockfordBase32("U6JD".toCharArray()));
		assertFalse(TsidUtil.isCrockfordBase32("*1BX5ZZKBKACTAV9WEVGEMMVRY".toCharArray()));
		assertFalse(TsidUtil.isCrockfordBase32("u".toCharArray()));
		assertFalse(TsidUtil.isCrockfordBase32("U".toCharArray()));
	}

	@Test
	public void testToBase32Crockford() {
		assertEquals("7ZZZZZZZZZ", new String(TsidUtil.toBase32Crockford(281474976710655L)));
		// Encode from long to base 32
		for (int i = 0; i < NUMBERS.length; i++) {
			String result = new String(TsidUtil.toBase32Crockford(NUMBERS[i]));
			assertEquals(NUMBERS_BASE_32_CROCKFORD[i].length(), result.length());
			assertEquals(NUMBERS_BASE_32_CROCKFORD[i], result);
		}
	}

	@Test
	public void testFromBase32Crockford() {
		assertEquals(281474976710655L, TsidUtil.fromBase32Crockford("7ZZZZZZZZZ".toCharArray()));
		// Decode from base 32 to long
		long number = 0;
		for (int i = 0; i < NUMBERS.length; i++) {
			number = TsidUtil.fromBase32Crockford((NUMBERS_BASE_32_CROCKFORD[i]).toCharArray());
			assertEquals(NUMBERS[i], number);
		}
	}
}
