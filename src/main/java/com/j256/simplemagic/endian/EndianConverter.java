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
	 * @param offset The offset to begin reading
	 * @param bytes  The bytes to be introspected
	 * @param size   The size of the value to read
	 * @return The long or null if not enough bytes.
	 */
	public Long convertNumber(int offset, byte[] bytes, int size);

	/**
	 * Convert a number of bytes starting at an offset into a long integer where the high-bit in each byte is always 0.
	 * 
	 * @param offset The offset to begin reading
	 * @param bytes  The bytes to be introspected
	 * @param size   The size of the value to read
	 * @return The long or null if not enough bytes.
	 */
	public Long convertId3(int offset, byte[] bytes, int size);

	/**
	 * Translate a number into an array of bytes.
	 * @param value The long value to be converted
	 * @param size   The size of the value in bytes
	 * @return The long or null if not enough bytes.
	 */
	public byte[] convertToByteArray(long value, int size);
}
