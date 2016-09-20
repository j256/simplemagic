package com.j256.simplemagic.types;

import com.j256.simplemagic.endian.EndianType;

/**
 * A four-byte integer value which often handles the "long" types when the spec was written.
 * 
 * @author graywatson
 */
public class IntegerType extends LongType {

	private static final int BYTES_PER_INTEGER = 4;

	public IntegerType(EndianType endianType) {
		super(endianType);
	}

	@Override
	protected int getBytesPerType() {
		return BYTES_PER_INTEGER;
	}
}
