package com.j256.simplemagic.endian;

/**
 * A four-byte value in middle-endian (god help us) PDP-11 byte order.
 * 
 * @author graywatson
 */
public class MiddleEndianConverter extends BaseEndianConverter {

	MiddleEndianConverter() {
		// only EndiaType should construct this
	}

	@Override
	public Long convertNumber(int offset, byte[] bytes, int size) {
		if (size != 4) {
			throw new UnsupportedOperationException("Middle-endian only supports 4-byte integers");
		}
		if (offset + size > bytes.length) {
			return null;
		}
		long value = 0;
		// BADC
		value = value << 8 | (bytes[1] & 0xFF);
		value = value << 8 | (bytes[0] & 0xFF);
		value = value << 8 | (bytes[3] & 0xFF);
		value = value << 8 | (bytes[2] & 0xFF);
		return value;
	}
}
