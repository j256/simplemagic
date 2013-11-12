package com.j256.simplemagic.entries;

/**
 * Classes which are able to match content according to operations and output description.
 * 
 * @author graywatson
 */
public interface MagicMatcher {

	/**
	 * Converts the test-string from the magic line to be an object to be passed into
	 * {@link #isMatch(Object, Long, boolean, Object, int, byte[])}.
	 */
	public Object convertTestString(String typeStr, String testStr, int offset);

	/**
	 * Extract the value from the bytes.
	 * 
	 * @return The object to be passed to {@link #isMatch(Object, Long, boolean, Object, int, byte[])} or null if not
	 *         enough bytes.
	 */
	public Object extractValueFromBytes(int offset, byte[] bytes);

	/**
	 * Matches if the bytes match at a certain offset.
	 * 
	 * @return The extracted-value object, or null if no match.
	 */
	public Object isMatch(Object testValue, Long andValue, boolean unsignedType, Object extractedValue, int offset,
			byte[] bytes);

	/**
	 * Returns the string version of the extracted value.
	 */
	public void renderValue(StringBuilder sb, Object extractedValue, MagicFormatter formatter);

	/**
	 * Return the starting byte of the patter or null if none. 
	 */
	public Byte getStartingByte(Object testValue);
}
