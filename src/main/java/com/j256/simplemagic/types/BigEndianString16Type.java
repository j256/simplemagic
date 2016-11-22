package com.j256.simplemagic.types;

/**
 * A two-byte unicode (UCS16) string in big-endian byte order.
 * 
 * @author graywatson
 */
public class BigEndianString16Type extends StringType {

	@Override
	public Object extractValueFromBytes(int offset, byte[] bytes) {
		int len;
		// find the 2 (I guess) '\0' chars, we do the -1 to make sure we don't have odd number of bytes
		for (len = offset; len < bytes.length - 1; len += 2) {
			if (bytes[len] == 0 && bytes[len + 1] == 0) {
				break;
			}
		}
		int charsLength = len / 2;
		char[] chars = new char[charsLength];
		for (int i = 0; i < charsLength; i++) {
			chars[i] = bytesToChar(bytes[i * 2], bytes[i * 2 + 1]);
		}
		return chars;
	}

    @Override
    public Object isMatch(Object testValue, Long andValue, boolean unsignedType, Object extractedValue,
            MutableOffset mutableOffset, byte[] bytes) {
        return findOffsetMatch((TestInfo) testValue, mutableOffset.offset, mutableOffset, (char[]) extractedValue);
    }
    
	protected char bytesToChar(int firstByte, int secondByte) {
		return (char) ((firstByte << 8) + secondByte);
	}
}
