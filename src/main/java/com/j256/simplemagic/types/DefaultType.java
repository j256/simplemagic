package com.j256.simplemagic.types;

import com.j256.simplemagic.entries.MagicFormatter;
import com.j256.simplemagic.entries.MagicMatcher;

/**
 * This is intended to be used with the test @code{x} (which is always true) and a message that is to be used if there
 * are no other matches.
 * 
 * <p>
 * <b>WARNING:</b> This type _is_ used in the magic files.
 * </p>
 * 
 * @author graywatson
 */
public class DefaultType implements MagicMatcher {

	private static final String EMPTY = "";

	public Object convertTestString(String typeStr, String testStr) {
		// null is an error so we just return junk
		return EMPTY;
	}

	public Object extractValueFromBytes(int offset, byte[] bytes) {
		return EMPTY;
	}

	public Object isMatch(Object testValue, Long andValue, boolean unsignedType, Object extractedValue,
			MutableOffset offset, byte[] bytes) {
		// always matches
		return EMPTY;
	}

	public void renderValue(StringBuilder sb, Object extractedValue, MagicFormatter formatter) {
		formatter.format(sb, extractedValue);
	}

	public byte[] getStartingBytes(Object testValue) {
		return null;
	}
}
