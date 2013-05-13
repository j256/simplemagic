package com.j256.simplemagic.endian;

/**
 * A four-byte value in middle-endian (god help us) PDP-11 byte order.
 * 
 * @author graywatson
 */
public class MiddleEndianConverter implements EndianConverter {

	MiddleEndianConverter() {
		// only EndiaType should construct this
	}

	public Long convertNumber(int offset, byte[] bytes, int size) {
		return convertNumber(offset, bytes, size, 8, 0xFF);
	}

	public Long convertId3(int offset, byte[] bytes, int size) {
		return convertNumber(offset, bytes, size, 7, 0x7F);
	}

	private Long convertNumber(int offset, byte[] bytes, int size, int shift, int mask) {
		if (size != 4) {
			throw new UnsupportedOperationException("Middle-endian only supports 4-byte integers");
		}
		if (offset + size > bytes.length) {
			return null;
		}
		long value = 0;
		// BADC
		value = value << shift | (bytes[1] & mask);
		value = value << shift | (bytes[0] & mask);
		value = value << shift | (bytes[3] & mask);
		value = value << shift | (bytes[2] & mask);
		return value;
	}
}
