package com.j256.simplemagic.types;

import com.j256.simplemagic.endian.EndianType;

/**
 * A one-byte value.
 * 
 * @author graywatson
 */
public class ByteType extends NumberType {

	public ByteType() {
		// we don't care about byte order since we only process 1 byte at a time
		super(EndianType.NATIVE);
	}

	@Override
	public int getBytesPerType() {
		return 1;
	}

	@Override
	public long maskValue(long value) {
		return value & 0xFFL;
	}

	@Override
	public int compare(long extractedValue, long testValue) {
		byte extractedByte = (byte)extractedValue;
		byte testByte = (byte)testValue;
		if (extractedByte > testByte) {
			return 1;
		} else if (extractedByte < testByte) {
			return -1;
		} else {
			return 0;
		}
	}
}
