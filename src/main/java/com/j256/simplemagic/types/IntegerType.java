package com.j256.simplemagic.types;

import com.j256.simplemagic.endian.EndianType;
import com.j256.simplemagic.entries.NumberOperator;

/**
 * A four-byte integer value.
 * 
 * @author graywatson
 */
public class IntegerType extends LongType {

	public IntegerType(EndianType endianType) {
		super(endianType);
	}

	@Override
	public Object extractValueFromBytes(int offset, byte[] bytes) {
		// we use a long here because we don't want to overflow
		return endianConverter.convertNumber(offset, bytes, 4);
	}

	@Override
	public byte[] getStartingBytes(Object testValue) {
		return endianConverter.convertToByteArray(((NumberOperator) testValue).getValue(), 4);
	}
}
