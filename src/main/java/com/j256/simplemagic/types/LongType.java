package com.j256.simplemagic.types;

import com.j256.simplemagic.endian.EndianConverter;
import com.j256.simplemagic.endian.EndianType;
import com.j256.simplemagic.entries.MagicFormatter;
import com.j256.simplemagic.entries.MagicMatcher;
import com.j256.simplemagic.entries.NumberOperator;

/**
 * An eight-byte value.
 * 
 * @author graywatson
 */
public class LongType implements MagicMatcher {

	protected final EndianConverter endianConverter;

	public LongType(EndianType endianType) {
		this.endianConverter = endianType.getConverter();
	}

	public Object convertTestString(String typeStr, String testStr, int offset) {
		return new NumberOperator(testStr);
	}

	public Object extractValueFromBytes(int offset, byte[] bytes) {
		return endianConverter.convertNumber(offset, bytes, 8);
	}

	public Object isMatch(Object testValue, Long andValue, boolean unsignedType, Object extractedValue, int offset,
			byte[] bytes) {
		if (((NumberOperator) testValue).isMatch(andValue, unsignedType, (Long) extractedValue, offset, bytes)) {
			return extractedValue;
		} else {
			return null;
		}
	}

	public void renderValue(StringBuilder sb, Object extractedValue, MagicFormatter formatter) {
		formatter.format(sb, extractedValue);
	}

	public byte[] getStartingBytes(Object testValue) {
		return endianConverter.convertToByteArray(((NumberOperator) testValue).getValue(), 8);
	}
}
