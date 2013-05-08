package com.j256.simplemagic.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.j256.simplemagic.entries.Formatter;

public class DefaultTypeTest {

	@Test
	public void testConverage() {
		DefaultType type = new DefaultType();
		type.convertTestString(null, null, 0);
		type.extractValueFromBytes(0, null);
		assertTrue(type.isMatch(null, null, false, null, 0, null) != null);
		String str = "weofjwepfj";
		StringBuilder sb = new StringBuilder();
		type.renderValue(sb, null, new Formatter(str));
		assertEquals(str, sb.toString());
	}
}
