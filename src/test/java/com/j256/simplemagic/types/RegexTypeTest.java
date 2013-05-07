package com.j256.simplemagic.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.j256.simplemagic.entries.Formatter;

public class RegexTypeTest {

	@Test
	public void testBasic() {
		RegexType type = new RegexType();
		Object test = type.convertTestString("hello[abc]", 0);
		byte[] bytes = "some line with helloa in it".getBytes();
		Object extracted = type.isMatch(test, null, false, null, 0, bytes);
		assertNotNull(extracted);
		assertEquals("helloa", renderValue(extracted, type, new Formatter("%s")));
	}

	@Test
	public void testCaseInsensitive() {
		RegexType type = new RegexType();
		Object test = type.convertTestString("hello[ABC]/c", 0);
		byte[] bytes = "some line with helloa in it".getBytes();
		Object extracted = type.isMatch(test, null, false, null, 0, bytes);
		assertNotNull(extracted);
		assertEquals("helloa", renderValue(extracted, type, new Formatter("%s")));
	}

	@Test
	public void testExtractValueFromBytes() {
		new RegexType().extractValueFromBytes(0, null);
	}

	private String renderValue(Object extracted, RegexType type, Formatter formatter) {
		StringBuilder sb = new StringBuilder();
		type.renderValue(sb, extracted, formatter);
		return sb.toString();
	}
}
