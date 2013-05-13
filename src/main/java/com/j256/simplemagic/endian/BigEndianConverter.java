package com.j256.simplemagic.endian;

/**
 * Converts values in "big" endian-ness where the high-order bytes come before the low-order (ABCD). Also called network
 * byte order. Big is better. Motorola 68000 processors.
 * 
 * @author graywatson
 */
public class BigEndianConverter implements EndianConverter {

	BigEndianConverter() {
		// only EndiaType should construct this
	}

	public Long convertNumber(int offset, byte[] bytes, int size) {
		return convertNumber(offset, bytes, size, 8, 0xFF);
	}

	public Long convertId3(int offset, byte[] bytes, int size) {
		return convertNumber(offset, bytes, size, 7, 0x7F);
	}

	private Long convertNumber(int offset, byte[] bytes, int size, int shift, int mask) {
		if (offset + size > bytes.length) {
			return null;
		}
		long value = 0;
		for (int i = offset; i < offset + size; i++) {
			value = value << shift | (bytes[i] & mask);
		}
		return value;
	}
}
