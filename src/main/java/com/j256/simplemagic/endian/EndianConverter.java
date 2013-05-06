package com.j256.simplemagic.endian;

/**
 * Class which converts from a particular machine byte representation into values appropriate for Java.
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
