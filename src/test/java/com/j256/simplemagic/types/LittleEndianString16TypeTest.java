package com.j256.simplemagic.types;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LittleEndianString16TypeTest {

	@Test
	public void testStuff() {
		LittleEndianString16Type type = new LittleEndianString16Type();
		char[] value = (char[]) type.extractValueFromBytes(0, new byte[] { 1, 'a', 2, 'b' });
		System.out.println((int) value[0]);
		assertEquals("愁戂", new String(value));
	}
}
