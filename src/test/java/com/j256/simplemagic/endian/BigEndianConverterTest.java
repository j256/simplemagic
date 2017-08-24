package com.j256.simplemagic.endian;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class BigEndianConverterTest {

	@Test
	public void testStuff() {
		EndianConverter converter = EndianType.BIG.getConverter();
		byte[] bytes = new byte[] { 10, 127, -100, 0, -128, 1, 62, -62 };
		Long result = converter.convertNumber(0, bytes, 8);
		byte[] outBytes = converter.convertToByteArray(result, 8);
		assertTrue(Arrays.equals(bytes, outBytes));
		assertNull(converter.convertNumber(0, bytes, bytes.length + 1));
		assertNull(converter.convertNumber(-1, bytes, bytes.length));
	}

	@Test
	public void testId3() {
		EndianConverter converter = EndianType.BIG.getConverter();
		long val = converter.convertId3(0, new byte[] { 1, 2, 3, 4 }, 4);
		// BADC: 1*2^21 + 2*2^14 + 3*2^7 + 4
		assertEquals(2130308, val);
	}
}
