package com.j256.simplemagic.types;

import com.j256.simplemagic.endian.EndianType;

/**
 * A four-byte integer value where the high bit of each byte is ignored.
 * 
 * @author graywatson
 */
public class Id3LengthType extends LongType {

	private static final int BYTES_PER_ID3 = 4;

	public Id3LengthType(EndianType endianType) {
		super(endianType);
	}

	@Override
	public Object extractValueFromBytes(int offset, byte[] bytes) {
		Long val = endianConverter.convertNumber(offset, bytes, BYTES_PER_ID3);
		if (val == null) {
			return null;
		}

		long result = 0;
		for (int i = 0; i < BYTES_PER_ID3; i++) {
			result |= (val & 0x7F);
		}
		return result;
	}

	@Override
	protected int getBytesPerType() {
		return BYTES_PER_ID3;
	}
}
