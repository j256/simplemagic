package com.j256.simplemagic.entries;

/**
 * Classes which are able to match content according to operations and output description.
 * 
 * @author graywatson
 */
public interface MagicMatcher {

	/**
	 * Converts the test-string from the magic line to be the testValue object to be passed into
	 * {@link #isMatch(Object, Long, boolean, Object, MutableOffset, byte[])} and {@link #getStartingBytes(Object)}.
	 * 
	 * @param typeStr The type the data is to be read as 
	 * @param testStr The string to be tested against
	 * 
	 * @return An object representing the result
	 */
	public Object convertTestString(String typeStr, String testStr);

	/**
	 * Extract the value from the bytes.
	 * 
	 * @param offset The offset to begin checking
	 * @param bytes  The bytes to introspect
	 * 
	 * @return The object to be passed to {@link #isMatch(Object, Long, boolean, Object, MutableOffset, byte[])} or null
	 *         if not enough bytes.
	 */
	public Object extractValueFromBytes(int offset, byte[] bytes);

	/**
	 * Matches if the bytes match at a certain offset.
	 * 
	 * @param testValue The value to be tested
	 * @param andValue  The optional mask applied
	 * @param unsignedType Whether signed or unsigned
	 * @param extractedValue The value extracted from the bytes
	 * @param offset     A changeable offset to be used throughout
	 * @param bytes      The bytes the be introspected
	 * 
	 * @return The extracted-value object, or null if no match.
	 */
	public Object isMatch(Object testValue, Long andValue, boolean unsignedType, Object extractedValue,
			MutableOffset offset, byte[] bytes);

	/**
	 * Returns the string version of the extracted value.
	 * 
	 * @param sb  A stringBuilder to accumulate the result
	 * @param extractedValue The value extracted from the bytes
	 * @param formatter A formatter used to format the output
	 */
	public void renderValue(StringBuilder sb, Object extractedValue, MagicFormatter formatter);

	/**
	 * Return the starting bytes of the patter or null if none.
	 * 
	 * @param testValue What value we're hoping to find
	 * @return A list of bytes that match, or null
	 */
	public byte[] getStartingBytes(Object testValue);

	/**
	 * Offset which we can update.
	 */
	public static class MutableOffset {
		public int offset;
		/**
		 * Construct a MutableOffset
		 * 
		 * @param offset The initial offset
		 */
		public MutableOffset(int offset) {
			this.offset = offset;
		}
	}
}
