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

	private static final int BYTES_PER_DOUBLE = 8;

	protected final EndianConverter endianConverter;

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
		Long val = endianConverter.convertNumber(offset, bytes, getBytesPerType());
		if (val == null) {
			return null;
		} else {
			return longToObject(val);
		}
	}

	@Override
	public Object isMatch(Object testValue, Long andValue, boolean unsignedType, Object extractedValue,
			MutableOffset mutableOffset, byte[] bytes) {
		// not sure how to do the & here
		if (testValue.equals(extractedValue)) {
			mutableOffset.offset += getBytesPerType();
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

	/**
	 * Convert a long to the type.
	 */
	protected Object longToObject(Long value) {
		return Double.longBitsToDouble(value);
	}

	/**
	 * Return the number of bytes in this type.
	 */
	protected int getBytesPerType() {
		return BYTES_PER_DOUBLE;
	}
}
