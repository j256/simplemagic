package com.j256.simplemagic.types;

import com.j256.simplemagic.entries.Formatter;
import com.j256.simplemagic.entries.MagicMatcher;
import com.j256.simplemagic.operators.NumberOperatorUtil;
import com.j256.simplemagic.operators.NumberOperatorUtil.NumberTest;

/**
 * A one-byte value.
 * 
 * @author graywatson
 */
public class ByteType implements MagicMatcher {

	public Object convertTestString(String test) {
		return NumberOperatorUtil.convertTestString(test);
	}

	public Byte extractValueFromBytes(int offset, byte[] bytes) {
		if (offset >= bytes.length) {
			return null;
		} else {
			return bytes[offset];
		}
	}

	public boolean isMatch(Object testValue, Long andValue, boolean unsignedType, Object extractedValue, int offset,
			byte[] bytes) {
		return NumberOperatorUtil.isMatch((NumberTest) testValue, andValue, unsignedType, (Byte) extractedValue,
				offset, bytes);
	}

	public void renderValue(StringBuilder sb, Object extractedValue, Formatter formatter) {
		formatter.format(sb, extractedValue);
	}
}
