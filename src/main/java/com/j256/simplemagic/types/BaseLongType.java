package com.j256.simplemagic.types;

import com.j256.simplemagic.endian.EndianType;

import java.math.BigInteger;
import java.util.regex.Matcher;

/**
 * Base class for those types which use long types to compare.
 *
 * @author graywatson
 */
public abstract class BaseLongType extends NumberType {

	public BaseLongType(EndianType endianType) {
		super(endianType);
	}

	@Override
	public Number decodeValueString(String valueStr) throws NumberFormatException {
		Matcher matcher = HEX_PATTERN.matcher(valueStr);
		return matcher.matches() ? new BigInteger(matcher.group(1), 16) : new BigInteger(valueStr);
	}

	@Override
	public byte[] getStartingBytes(Object testValue) {
		return endianConverter.convertToByteArray(((NumberComparison) testValue).getValue().longValue(),
				getBytesPerType());
	}
}
