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

	@Override
	public Long convertNumber(int offset, byte[] bytes, int size) {
		return convertNumber(offset, bytes, size, 8, 0xFF);
	}

	@Override
	public Long convertId3(int offset, byte[] bytes, int size) {
		return convertNumber(offset, bytes, size, 7, 0x7F);
	}

	@Override
	public byte[] convertToByteArray(long value, int size) {
		byte[] result = new byte[size];
		for (int i = 0; i < size; i++) {
			result[i] = (byte) (value & 0xFF);
			value >>= 8;
		}
		return result;
	}

	/**
	 * Convert the number found at the given offset
	 * 
	 * @param offset  The offset to begin searching
	 * @param bytes   The bytes to introspect
	 * @param size    The size / number of bytes to read
	 * @param shift   The shift (+/-) to be applied after
	 * @param mask    The mask to be applied
	 * @return A long representation of the read number
	 */
	public Long convertNumber(int offset, byte[] bytes, int size, int shift, int mask) {
		if (offset + size > bytes.length) {
			return null;
		}
		long value = 0;
		for (int i = offset + (size - 1); i >= offset; i--) {
			value = value << shift | (bytes[i] & mask);
		}
		return value;
	}
}
