package com.j256.simplemagic.types;

import com.j256.simplemagic.endian.EndianType;

/**
 * A four-byte integer value where the high bit of each byte is ignored.
 * 
 * @author graywatson
 */
public class Id3LengthType extends LongType {

	public Id3LengthType(EndianType endianType) {
		super(endianType);
	}

	@Override
	public Object extractValueFromBytes(int offset, byte[] bytes) {
		Long val = endianConverter.convertNumber(offset, bytes, 8);
		if (val == null) {
			return null;
		}

		long result = 0;
		for (int i = 0; i < 4; i++) {
			result |= (val & 0x7F);
		}
		return result;
	}
}
