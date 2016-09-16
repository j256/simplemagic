package com.j256.simplemagic.types;

import com.j256.simplemagic.endian.EndianConverter;
import com.j256.simplemagic.endian.EndianType;
import com.j256.simplemagic.entries.MagicFormatter;
import com.j256.simplemagic.entries.MagicMatcher;
import com.j256.simplemagic.entries.NumberOperator;

/**
 * An four-byte value constituted "long" then the magic file spec was written.
 * 
 * @author graywatson
 */
public class LongType implements MagicMatcher {

	static final int BYTES_PER_LONG = 8;
	
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
		return endianConverter.convertNumber(offset, bytes, BYTES_PER_LONG);
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
		return endianConverter.convertToByteArray(((NumberOperator) testValue).getValue(), BYTES_PER_LONG);
	}
}
