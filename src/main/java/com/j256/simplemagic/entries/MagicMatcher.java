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
	 */
	Object convertTestString(String typeStr, String testStr);

	/**
	 * Extract the value from the bytes either for doing the match or rendering it in the format.
	 * 
	 * @param offset
	 *            Number of bytes into the bytes array that we are extracting from.
	 * @param bytes
	 *            Array of bytes we are extracting from.
	 * @param required
	 *            Whether or not the extracted value is required for later. If it is not then the type may opt to not
	 *            extract the value and to do the matching directly.
	 * @return The object to be passed to {@link #isMatch(Object, Long, boolean, Object, MutableOffset, byte[])} or null
	 *         if not enough bytes.
	 */
	Object extractValueFromBytes(int offset, byte[] bytes, boolean required);

	/**
	 * Matches if the bytes match at a certain offset.
	 * 
	 * @return The extracted-value object, or null if no match.
	 */
	Object isMatch(Object testValue, Long andValue, boolean unsignedType, Object extractedValue,
				   MutableOffset offset, byte[] bytes);

	/**
	 * Returns the string version of the extracted value.
	 */
	void renderValue(StringBuilder sb, Object extractedValue, MagicFormatter formatter);

	/**
	 * Return the starting bytes of the pattern or null if none.
	 */
	byte[] getStartingBytes(Object testValue);

	/**
	 * Offset which we can update.
	 */
	class MutableOffset {
		public int offset;

		public MutableOffset(int offset) {
			this.offset = offset;
		}

		@Override
		public String toString() {
			return Integer.toString(offset);
		}
	}
}
