package com.j256.simplemagic.endian;

/**
 * Converts values in "big" endian-ness where the high-order bytes come before the low-order (ABCD). Also called network
 * byte order. Big is better. Motorola 68000 processors.
 * 
 * @author graywatson
 */
public class BigEndianConverter extends BaseEndianConverter {

	BigEndianConverter() {
		// only EndiaType should construct this
	}

	@Override
	public Long convertNumber(int offset, byte[] bytes, int size) {
		if (offset + size > bytes.length) {
			return null;
		}
		long value = 0;
		for (int i = offset; i < offset + size; i++) {
			value = value << 8 | (bytes[i] & 0xFF);
		}
		return value;
	}
}
