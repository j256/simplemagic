package com.j256.simplemagic.endian;

/**
 * Converts values in "little" endian-ness where the high-order bytes come _after_ the low-order (DCBA). x86 processors.
 * 
 * @author graywatson
 */
public class LittleEndianConverter implements EndianConverter {

	LittleEndianConverter() {
		// only EndiaType should construct this
	}

	public Long convertNumber(int offset, byte[] bytes, int size) {
		if (offset + size > bytes.length) {
			return null;
		}
		long value = 0;
		for (int i = offset + (size - 1); i >= offset; i--) {
			value = value << 8 | (bytes[i] & 0xFF);
		}
		return value;
	}
}
