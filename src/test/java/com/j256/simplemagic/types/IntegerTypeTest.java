package com.j256.simplemagic.types;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.j256.simplemagic.endian.EndianType;
import com.j256.simplemagic.entries.MagicMatcher.MutableOffset;

public class IntegerTypeTest {

	@Test
	public void testLittleEndianNumber() {
		IntegerType longType = new IntegerType(EndianType.LITTLE);
		String hexBytes = "0x03cbc6c5";
		Object testString = longType.convertTestString("lelong", hexBytes);
		Object value = longType.extractValueFromBytes(0,
				new byte[] { hexToByte("0xc5"), hexToByte("0xc6"), hexToByte("0xcb"), hexToByte("0x03") });
		assertNotNull(longType.isMatch(testString, null, false, value, new MutableOffset(0), null /* unused */));
	}

	@Test
	public void testBigEndianNumber() {
		IntegerType longType = new IntegerType(EndianType.BIG);
		String hexBytes = "0x03c7b3a1";
		Object testString = longType.convertTestString("lelong", hexBytes);
		Object value = longType.extractValueFromBytes(0,
				new byte[] { hexToByte("0x03"), hexToByte("0xc7"), hexToByte("0xb3"), hexToByte("0xa1") });
		assertNotNull(longType.isMatch(testString, null, false, value, new MutableOffset(0), null /* unused */));
	}

	private byte hexToByte(String hex) {
		return Integer.decode(hex).byteValue();
	}
}
