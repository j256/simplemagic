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

	@Override
	public Object convertTestString(String typeStr, String testStr) {
		return new NumberOperator(testStr);
	}

	@Override
	public Object extractValueFromBytes(int offset, byte[] bytes) {
		return endianConverter.convertNumber(offset, bytes, 8);
	}

	@Override
	public Object isMatch(Object testValue, Long andValue, boolean unsignedType, Object extractedValue,
			MutableOffset mutableOffset, byte[] bytes) {
		if (((NumberOperator) testValue).isMatch(andValue, unsignedType, (Long) extractedValue)) {
			mutableOffset.offset += 8;
			return extractedValue;
		} else {
			return null;
		}
	}

	@Override
	public void renderValue(StringBuilder sb, Object extractedValue, MagicFormatter formatter) {
		formatter.format(sb, extractedValue);
	}

	@Override
	public byte[] getStartingBytes(Object testValue) {
		return endianConverter.convertToByteArray(((NumberOperator) testValue).getValue(), 8);
	}
}
