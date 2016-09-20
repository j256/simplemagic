package com.j256.simplemagic.types;

import com.j256.simplemagic.endian.EndianType;

/**
 * A one-byte value.
 * 
 * @author graywatson
 */
public class ByteType extends LongType {

	public ByteType() {
		// we don't care about byte order since we only process 1 byte at a time
		super(EndianType.NATIVE);
	}

	@Override
	protected int getBytesPerType() {
		return 1;
	}
}
