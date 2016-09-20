package com.j256.simplemagic.types;

import com.j256.simplemagic.endian.EndianType;

/**
 * A two-byte value.
 * 
 * @author graywatson
 */
public class ShortType extends LongType {

	private static final int BYTES_PER_SHORT = 2;

	public ShortType(EndianType endianType) {
		super(endianType);
	}

	@Override
	protected int getBytesPerType() {
		return BYTES_PER_SHORT;
	}
}
