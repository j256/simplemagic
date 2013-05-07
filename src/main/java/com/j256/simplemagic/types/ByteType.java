package com.j256.simplemagic.types;

import com.j256.simplemagic.endian.EndianType;

/**
 * A one-byte value.
 * 
 * @author graywatson
 */
public class ByteType extends LongType {

	public ByteType() {
		// we really don't care about byte order since we only process 1 byte at a time
		super(EndianType.NATIVE);
	}

	@Override
	public Long extractValueFromBytes(int offset, byte[] bytes) {
		// we use a long here because we don't want to overflow
		return endianConverter.convertNumber(offset, bytes, 1);
	}
}
