package com.j256.simplemagic.entries;

/**
 * Classes which are able to match content according to operations and output description.
 * 
 * @author graywatson
 */
public interface MagicMatcher {

	/**
	 * Converts the test-string from the magic line to be an object to be passed into
	 * {@link #isMatch(Object, int, byte[])}.
	 */
	public Object convertTestString(String test);

	/**
	 * Extract the value from the bytes.
	 */
	public Object extractValueFromBytes(int offset, byte[] bytes);

	/**
	 * Matches if the bytes match at a certain offset.
	 */
	public boolean isMatch(Object testValue, Long andValue, boolean unsignedType, Object extractedValue, int offset,
			byte[] bytes);

	/**
	 * Returns the string version of the extracted value.
	 */
	public void renderValue(StringBuilder sb, Object extractedValue, Formatter formatter);
}
