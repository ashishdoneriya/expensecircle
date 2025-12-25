package com.csetutorials.expensecircle.utilities;

public final class Base62 {

	private static final char[] ALPHABET =
		"0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

	private static final int BASE = ALPHABET.length;

	public static String encode(long value) {
		if (value < 0) {
			throw new IllegalArgumentException("negative value");
		}

		char[] buf = new char[11];
		int pos = buf.length;

		do {
			buf[--pos] = ALPHABET[(int) (value % BASE)];
			value /= BASE;
		} while (value > 0);

		return new String(buf, pos, buf.length - pos);
	}
}
