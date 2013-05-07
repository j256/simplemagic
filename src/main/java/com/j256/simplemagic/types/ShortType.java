package com.j256.simplemagic.types;

import com.j256.simplemagic.endian.EndianType;

/**
 * A two-byte value.
 * 
 * @author graywatson
 */
public class ShortType extends LongType {

	public ShortType(EndianType endianType) {
		super(endianType);
	}

	@Override
	public Long extractValueFromBytes(int offset, byte[] bytes) {
		// we use long here because we don't want to overflow
		return endianConverter.convertNumber(offset, bytes, 2);
	}
}
