package com.j256.simplemagic.types;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BigEndianString16TypeTest {

	@Test
	public void testStuff() {
		BigEndianString16Type type = new BigEndianString16Type();
		char[] value = (char[]) type.extractValueFromBytes(0, new byte[] { 1, 'a', 2, 'b' });
		assertEquals("šɢ", new String(value));
	}
}
