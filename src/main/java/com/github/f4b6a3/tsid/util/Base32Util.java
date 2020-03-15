/*
 * MIT License
 * 
 * Copyright (c) 2020 Fabio Lima
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

package com.github.f4b6a3.tsid.util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

/**
 * 
 * This class contain static methods for encoding to and from Base 32.
 * 
 * Supported alphabets: base32, base32hex, zbase32 and crockford base32.
 * 
 * RFC-4648: https://tools.ietf.org/html/rfc4648
 * 
 * Wikipedia: https://en.wikipedia.org/wiki/Base32
 * 
 */
public class Base32Util {

	public static final String ALPHABET_BASE_32 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
	public static final String ALPHABET_BASE_32_HEX = "0123456789ABCDEFGHIJKLMNOPQRSTUV";
	public static final String ALPHABET_BASE_32_Z = "ybndrfg8ejkmcpqxot1uwisza345h769";
	public static final String ALPHABET_BASE_32_CROCKFORD = "0123456789ABCDEFGHJKMNPQRSTVWXYZ";

	private static final char[] _ALPHABET_BASE_32 = ALPHABET_BASE_32.toCharArray();
	private static final char[] _ALPHABET_BASE_32_HEX = ALPHABET_BASE_32_HEX.toCharArray();
	private static final char[] _ALPHABET_BASE_32_Z = ALPHABET_BASE_32_Z.toCharArray();
	private static final char[] _ALPHABET_BASE_32_CROCKFORD = ALPHABET_BASE_32_CROCKFORD.toCharArray();

	private static final int[] PADDING_ENCODE = { 0, 6, 4, 3, 1 };
	private static final int[] PADDING_DECODE = { 0, 4, 4, 3, 3, 2, 2, 1 };

	private static final int BASE = 32;

	private Base32Util() {
	}

	// ----------------------
	// Base 32
	// ----------------------

	public static String toBase32(long number) {
		return encodeLong(number, _ALPHABET_BASE_32);
	}

	public static String toBase32(BigInteger number) {
		return encodeBigInteger(number, _ALPHABET_BASE_32);
	}

	public static String toBase32(byte[] bytes) {
		return encode(bytes, _ALPHABET_BASE_32, '=');
	}

	public static String toBase32(String string) {
		return toBase32(toBytes(string));
	}

	public static byte[] fromBase32(String string) {
		return decode(normalize(string), _ALPHABET_BASE_32, '=');
	}

	public static String fromBase32AsString(String string) {
		return toString(fromBase32(string));
	}

	public static long fromBase32AsLong(String string) {
		return decodeLong(normalize(string), _ALPHABET_BASE_32);
	}

	public static BigInteger fromBase32AsBigInteger(String string) {
		return decodeBigInteger(normalize(string), _ALPHABET_BASE_32);
	}

	// ----------------------
	// Base 32 Hex
	// ----------------------

	public static String toBase32Hex(long number) {
		return encodeLong(number, _ALPHABET_BASE_32_HEX);
	}

	public static String toBase32Hex(BigInteger number) {
		return encodeBigInteger(number, _ALPHABET_BASE_32_HEX);
	}

	public static String toBase32Hex(byte[] bytes) {
		return encode(bytes, _ALPHABET_BASE_32_HEX, '=');
	}

	public static String toBase32Hex(String string) {
		return toBase32Hex(toBytes(string));
	}

	public static byte[] fromBase32Hex(String string) {
		return decode(normalize(string), _ALPHABET_BASE_32_HEX, '=');
	}

	public static String fromBase32HexAsString(String string) {
		return toString(fromBase32Hex(string));
	}

	public static long fromBase32HexAsLong(String string) {
		return decodeLong(normalize(string), _ALPHABET_BASE_32_HEX);
	}

	public static BigInteger fromBase32HexAsBigInteger(String string) {
		return decodeBigInteger(normalize(string), _ALPHABET_BASE_32_HEX);
	}

	// ----------------------
	// Base 32 Crockford
	// ----------------------

	public static String toBase32Crockford(long number) {
		return encodeLong(number, _ALPHABET_BASE_32_CROCKFORD);
	}

	public static String toBase32Crockford(BigInteger number) {
		return encodeBigInteger(number, _ALPHABET_BASE_32_CROCKFORD);
	}

	public static String toBase32Crockford(byte[] bytes) {
		return encode(bytes, _ALPHABET_BASE_32_CROCKFORD, null);
	}

	public static String toBase32Crockford(String string) {
		return toBase32Crockford(toBytes(string));
	}

	public static byte[] fromBase32Crockford(String string) {
		return decode(normalizeCrockford(string), _ALPHABET_BASE_32_CROCKFORD, null);
	}

	public static String fromBase32CrockfordAsString(String string) {
		return toString(fromBase32Crockford(string));
	}

	public static long fromBase32CrockfordAsLong(String string) {
		return decodeLong(normalizeCrockford(string), _ALPHABET_BASE_32_CROCKFORD);
	}

	public static BigInteger fromBase32CrockfordAsBigInteger(String string) {
		return decodeBigInteger(normalizeCrockford(string), _ALPHABET_BASE_32_CROCKFORD);
	}

	// ----------------------
	// Z Base 32 Z
	// ----------------------

	public static String toBase32Z(long number) {
		return encodeLong(number, _ALPHABET_BASE_32_Z);
	}

	public static String toBase32Z(BigInteger number) {
		return encodeBigInteger(number, _ALPHABET_BASE_32_Z);
	}

	public static String toBase32Z(byte[] bytes) {
		return encode(bytes, _ALPHABET_BASE_32_Z, null);
	}

	public static String toBase32Z(String string) {
		return toBase32Z(toBytes(string));
	}

	public static byte[] fromBase32Z(String string) {
		return decode(normalizeZ(string), _ALPHABET_BASE_32_Z, null);
	}

	public static String fromBase32ZAsString(String string) {
		return toString(fromBase32Z(string));
	}

	public static long fromBase32ZAsLong(String string) {
		return decodeLong(normalizeZ(string), _ALPHABET_BASE_32_Z);
	}

	public static BigInteger fromBase32ZAsBigInteger(String string) {
		return decodeBigInteger(normalizeZ(string), _ALPHABET_BASE_32_Z);
	}

	// ----------------------
	// Utils
	// ----------------------

	/**
	 * Convert a string to an array of bytes using UTF-8.
	 * 
	 * @param string a string
	 * @return a string
	 */
	public static byte[] toBytes(String string) {
		return string.getBytes(StandardCharsets.UTF_8);
	}

	/**
	 * Convert an array of bytes to a string using UTF-8.
	 * 
	 * @param bytes a byte sequence
	 * @return a string
	 */
	public static String toString(byte[] bytes) {
		return new String(bytes, StandardCharsets.UTF_8);
	}

	// ----------------------
	// Encoder and Decoder
	// ----------------------

	/**
	 * Encode a long number to base 32 string.
	 * 
	 * @param number
	 *            a long number
	 * @param alphabet
	 *            an alphabet
	 * @return a base32 encoded string
	 */
	public static String encodeLong(long number, char[] alphabet) {

		long n = number;
		StringBuilder builder = new StringBuilder();

		while (n > 0) {
			builder.append(alphabet[(int) (n % BASE)]);
			n = n / BASE;
		}

		return builder.reverse().toString();
	}

	/**
	 * Encode a BigInteger to base 32 string.
	 * 
	 * @param number
	 *            a BigInteger
	 * @param alphabet
	 *            an alphabet
	 * @return a base32 encoded string
	 */
	public static String encodeBigInteger(BigInteger number, char[] alphabet) {

		BigInteger n = number;
		BigInteger b = BigInteger.valueOf(BASE);

		StringBuilder builder = new StringBuilder();

		while (n.compareTo(BigInteger.ZERO) > 0) {
			builder.append(alphabet[n.remainder(b).intValue()]);
			n = n.divide(b);
		}

		return builder.reverse().toString();
	}

	/**
	 * Decode a base 32 string to a long number.
	 * 
	 * @param string
	 *            a base 32 encoded string
	 * @param alphabet
	 *            an alphabet
	 * @return a long number
	 */
	public static long decodeLong(String string, char[] alphabet) {

		long n = 0;

		for (char c : string.toCharArray()) {
			int d = map(c, alphabet);
			n = BASE * n + d;
		}

		return n;
	}

	/**
	 * Decode a base 32 string to a BigInteger.
	 * 
	 * @param string
	 *            a base 32 encoded string
	 * @param alphabet
	 *            an alphabet
	 * @return a BigInteger
	 */
	public static BigInteger decodeBigInteger(String string, char[] alphabet) {

		BigInteger n = BigInteger.ZERO;
		BigInteger b = BigInteger.valueOf(BASE);

		for (char c : string.toCharArray()) {
			int d = map(c, alphabet);
			n = b.multiply(n).add(BigInteger.valueOf(d));
		}

		return n;
	}

	/**
	 * Encode an array of bytes into a base 32 string.
	 * 
	 * @param bytes
	 *            an array of bytes
	 * @param alphabet
	 *            an alphabet
	 * @param padding
	 *            a padding char, if necessary
	 * @return a base 32 encoded string
	 */
	public static String encode(byte[] bytes, char[] alphabet, Character padding) {

		if (bytes == null || bytes.length == 0) {
			return "";
		}

		int div = bytes.length / 5;
		int mod = bytes.length % 5;

		int blocks = (div + (mod == 0 ? 0 : 1));

		byte[] input = new byte[5 * blocks];
		char[] output = new char[8 * blocks];

		System.arraycopy(bytes, 0, input, 0, bytes.length);

		for (int i = 0; i < blocks; i++) {
			byte[] blk = new byte[5];
			System.arraycopy(input, i * 5, blk, 0, 5);

			int[] out = new int[8];

			out[0] = ((blk[0] & 0b11111000) >>> 3);
			out[1] = ((blk[0] & 0b00000111) << 2) | ((blk[1] & 0b11000000) >>> 6);
			out[2] = ((blk[1] & 0b00111110) >>> 1);
			out[3] = ((blk[1] & 0b00000001) << 4) | ((blk[2] & 0b11110000) >>> 4);
			out[4] = ((blk[2] & 0b00001111) << 1) | ((blk[3] & 0b10000000) >>> 7);
			out[5] = ((blk[3] & 0b01111100) >>> 2);
			out[6] = ((blk[3] & 0b00000011) << 3) | ((blk[4] & 0b11100000) >>> 5);
			out[7] = (blk[4] & 0b00011111);

			char[] chars = new char[8];
			for (int j = 0; j < 8; j++) {
				chars[j] = alphabet[out[j]];
			}

			System.arraycopy(chars, 0, output, i * 8, 8);
		}

		int outputSize = output.length - PADDING_ENCODE[mod];
		if (padding != null) {
			// Add padding: '='
			for (int i = outputSize; i < output.length; i++) {
				output[i] = padding;
			}
		} else {
			// Remove padding
			char[] temp = new char[outputSize];
			System.arraycopy(output, 0, temp, 0, outputSize);
			output = temp;
		}

		return new String(output);
	}

	/**
	 * Decode a base 32 string into an byte array.
	 * 
	 * @param string
	 *            a base 32 encoded string
	 * @param alphabet
	 *            an alphabet
	 * @param padding
	 *            a padding char, if necessary
	 * @return a byte array
	 */
	public static byte[] decode(String string, char[] alphabet, Character padding) {

		if (string == null || string.length() == 0) {
			return new byte[0];
		}

		char[] chars = null;
		char[] alph = alphabet;

		if (padding != null) {
			chars = string.replaceAll(padding.toString(), "").toCharArray();
		} else {
			chars = string.toCharArray();
		}

		validate(chars, alph);

		int div = chars.length / 8;
		int mod = chars.length % 8;
		int size = (div + (mod == 0 ? 0 : 1));

		char[] input = new char[8 * size];
		byte[] output = new byte[5 * size];

		System.arraycopy(chars, 0, input, 0, chars.length);

		for (int i = 0; i < size; i++) {
			char[] blk = new char[8];
			System.arraycopy(input, i * 8, blk, 0, 8);

			byte[] out = new byte[5];

			out[0] = (byte) ((map(blk[0], alph) << 3) | (map(blk[1], alph) >>> 2));
			out[1] = (byte) ((map(blk[1], alph) << 6) | (map(blk[2], alph) << 1) | (map(blk[3], alph) >>> 4));
			out[2] = (byte) ((map(blk[3], alph) << 4) | (map(blk[4], alph) >>> 1));
			out[3] = (byte) ((map(blk[4], alph) << 7) | (map(blk[5], alph) << 2) | (map(blk[6], alph) >>> 3));
			out[4] = (byte) ((map(blk[6], alph) << 5) | (map(blk[7], alph)));

			System.arraycopy(out, 0, output, i * 5, 5);

		}

		// Remove padding
		int outputSize = output.length - PADDING_DECODE[mod];
		byte[] temp = new byte[outputSize];
		System.arraycopy(output, 0, temp, 0, outputSize);
		output = temp;

		return output;
	}

	protected static void validate(char[] chars, char[] alphabet) {
		for (int i = 0; i < chars.length; i++) {
			boolean found = false;
			for (int j = 0; j < alphabet.length; j++) {
				if (chars[i] == alphabet[j]) {
					found = true;
					break;
				}
			}
			if (!found) {
				throw new Base32UtilException("Invalid character");
			}
		}
	}

	private static int map(char c, char[] alphabet) {
		for (int i = 0; i < alphabet.length; i++) {
			if (alphabet[i] == c) {
				return (byte) i;
			}
		}
		return (byte) '0';
	}

	protected static String normalize(String string) {
		if (string == null) {
			return "";
		}
		return string.toUpperCase();
	}

	protected static String normalizeZ(String string) {
		if (string == null) {
			return "";
		}
		return string.toLowerCase();
	}

	protected static String normalizeCrockford(String string) {
		if (string == null) {
			return "";
		}
		String normalized = string.toUpperCase();
		normalized = normalized.replaceAll("-", "");
		normalized = normalized.replaceAll("I", "1");
		normalized = normalized.replaceAll("L", "1");
		normalized = normalized.replaceAll("O", "0");
		return normalized;
	}

	public static class Base32UtilException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public Base32UtilException(String message) {
			super(message);
		}
	}
}