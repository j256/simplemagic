package com.j256.simplemagic.types;

import com.j256.simplemagic.endian.EndianType;

/**
 * A 32-bit single precision IEEE floating point number in this machine's native byte order.
 * 
 * @author graywatson
 */
public class FloatType extends DoubleType {

	private static final int BYTES_PER_FLOAT = 4;

	public FloatType(EndianType endianType) {
		super(endianType);
	}

	@Override
	public Object convertTestString(String typeStr, String testStr) {
		try {
			return Float.parseFloat(testStr);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Could not parse float from: " + testStr);
		}
	}

	@Override
	protected Object longToObject(Long value) {
		return Float.intBitsToFloat(value.intValue());
	}

	@Override
	protected int getBytesPerType() {
		return BYTES_PER_FLOAT;
	}
}
