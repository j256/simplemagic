package com.j256.simplemagic.types;

/**
 * A Pascal-style string where the first byte is interpreted as the an unsigned length. The string is not '\0'
 * terminated.
 * 
 * @author graywatson
 */
public class PStringType extends StringType {

	@Override
	public Object extractValueFromBytes(int offset, byte[] bytes) {
		int len;
		if (offset >= bytes.length) {
			len = 0;
		} else {
			len = bytes[offset];
			int left = bytes.length - offset;
			if (len > left) {
				len = left;
			}
		}
		char[] chars = new char[len];
		for (int i = 0; i < chars.length; i++) {
			chars[i] = (char) bytes[offset + i];
		}
		return chars;
	}
}
