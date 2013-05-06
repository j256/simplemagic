package com.j256.simplemagic.types;

import com.j256.simplemagic.endian.EndianConverter;
import com.j256.simplemagic.endian.EndianType;
import com.j256.simplemagic.entries.Formatter;
import com.j256.simplemagic.entries.MagicMatcher;
import com.j256.simplemagic.entries.NumberOperator;

/**
 * A four-byte integer value.
 * 
 * @author graywatson
 */
public class IntegerType implements MagicMatcher {

	private final EndianConverter endianConverter;

	public IntegerType(EndianType endianType) {
		this.endianConverter = endianType.getConverter();
	}

	public Object convertTestString(String test) {
		return new NumberOperator(test);
	}

	public Long extractValueFromBytes(int offset, byte[] bytes) {
		// we use a long here because we don't want to overflow
		return endianConverter.convertNumber(offset, bytes, 4);
	}

	public boolean isMatch(Object testValue, Long andValue, boolean unsignedType, Object extractedValue, int offset,
			byte[] bytes) {
		return ((NumberOperator) testValue).isMatch(andValue, unsignedType, (Long) extractedValue, offset, bytes);
	}

	public void renderValue(StringBuilder sb, Object extractedValue, Formatter formatter) {
		formatter.format(sb, extractedValue);
	}
}
