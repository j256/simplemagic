package com.j256.simplemagic.endian;

/**
 * Base class which handles the argument checking for the endian converters.
 * 
 * @author graywatson
 */
public abstract class BaseEndianConverter implements EndianConverter {

	BaseEndianConverter() {
		// only EndiaType should construct this
	}

	public abstract Long convertNumber(int offset, byte[] bytes, int size);
}
