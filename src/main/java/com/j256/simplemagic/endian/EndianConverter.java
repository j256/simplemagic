package com.j256.simplemagic.endian;

/**
 * A four-byte value in big-endian byte order, interpreted as a Unix date.
 * 
 * @author graywatson
 */
public interface EndianConverter {

	/**
	 * Convert a number of bytes starting at an offset into a long integer.
	 * 
	 * @return The long or null if not enough bytes.
	 */
	public Long convertNumber(int offset, byte[] bytes, int size);
}
