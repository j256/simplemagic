package com.j256.simplemagic.types;

import com.j256.simplemagic.endian.EndianConverter;
import com.j256.simplemagic.endian.EndianType;
import com.j256.simplemagic.entries.MagicFormatter;
import com.j256.simplemagic.entries.MagicMatcher;

/**
 * A 64-bit double precision IEEE floating point number in this machine's native byte order.
 * 
 * @author graywatson
 */
public class DoubleType implements MagicMatcher {

	private final EndianConverter endianConverter;

	public DoubleType(EndianType endianType) {
		this.endianConverter = endianType.getConverter();
	}

	@Override
	public Object convertTestString(String typeStr, String testStr) {
		try {
			return Double.parseDouble(testStr);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Could not parse double from: " + testStr);
		}
	}

	@Override
	public Object extractValueFromBytes(int offset, byte[] bytes) {
		Long val = endianConverter.convertNumber(offset, bytes, 8);
		if (val == null) {
			return null;
		} else {
			return Double.longBitsToDouble(val);
		}
	}

	@Override
	public Object isMatch(Object testValue, Long andValue, boolean unsignedType, Object extractedValue,
			MutableOffset mutableOffset, byte[] bytes) {
		// not sure how to do the & here
		if (testValue.equals(extractedValue)) {
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
		return null;
	}
}
