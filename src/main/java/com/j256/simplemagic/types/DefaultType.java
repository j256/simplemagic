package com.j256.simplemagic.types;

import com.j256.simplemagic.entries.Formatter;
import com.j256.simplemagic.entries.MagicMatcher;

/**
 * This is intended to be used with the test x (which is always true) and a message that is to be used if there are no
 * other matches.
 * 
 * @author graywatson
 */
public class DefaultType implements MagicMatcher {

	private final Object empty = new Object();

	public Object convertTestString(String test) {
		// null is an error so we just return junk
		return empty;
	}

	public Object extractValueFromBytes(int offset, byte[] bytes) {
		return empty;
	}

	public boolean isMatch(Object testValue, Long andValue, boolean unsignedType, Object extractedValue, int offset,
			byte[] bytes) {
		// always matches
		return true;
	}

	public void renderValue(StringBuilder sb, Object extractedValue, Formatter formatter) {
		formatter.format(sb, extractedValue);
	}
}
