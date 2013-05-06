package com.j256.simplemagic.types;

import com.j256.simplemagic.endian.EndianConverter;
import com.j256.simplemagic.endian.EndianType;
import com.j256.simplemagic.entries.Formatter;
import com.j256.simplemagic.entries.MagicMatcher;
import com.j256.simplemagic.operators.NumberOperatorUtil;
import com.j256.simplemagic.operators.NumberOperatorUtil.NumberTest;

/**
 * An eight-byte value.
 * 
 * @author graywatson
 */
public class LongType implements MagicMatcher {

	private final EndianConverter endianConverter;

	public LongType(EndianType endianType) {
		this.endianConverter = endianType.getConverter();
	}

	public Object convertTestString(String test) {
		return NumberOperatorUtil.convertTestString(test);
	}

	public Object extractValueFromBytes(int offset, byte[] bytes) {
		return endianConverter.convertNumber(offset, bytes, 8);
	}

	public boolean isMatch(Object testValue, Long andValue, boolean unsignedType, Object extractedValue, int offset,
			byte[] bytes) {
		return NumberOperatorUtil.isMatch((NumberTest) testValue, andValue, unsignedType, (Long) extractedValue,
				offset, bytes);
	}

	public void renderValue(StringBuilder sb, Object extractedValue, Formatter formatter) {
		formatter.format(sb, extractedValue);
	}
}
