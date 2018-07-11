package com.j256.simplemagic.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.j256.simplemagic.entries.MagicFormatter;
import com.j256.simplemagic.entries.MagicMatcher.MutableOffset;

public class RegexTypeTest {

	@Test
	public void testBasic() {
		RegexType type = new RegexType();
		Object test = type.convertTestString("regex", "hello[abc]");
		byte[] bytes = "some line with helloa in it".getBytes();
		Object extracted = type.isMatch(test, null, false, null, new MutableOffset(0), bytes);
		assertNotNull(extracted);
		assertEquals("helloa", renderValue(extracted, type, new MagicFormatter("%s")));
	}

	@Test
	public void testCaseInsensitive() {
		RegexType type = new RegexType();
		Object test = type.convertTestString("regex/c", "hello[ABC]");
		byte[] bytes = "some line with helloa in it".getBytes();
		Object extracted = type.isMatch(test, null, false, null, new MutableOffset(0), bytes);
		assertNotNull(extracted);
		assertEquals("helloa", renderValue(extracted, type, new MagicFormatter("%s")));
	}

	@Test
	public void testSlashes() {
		RegexType type = new RegexType();
		/*
		 * \\xB is decimal 11 which is octal 013. The 8 backslashes doesn't seem right but if the Java string does one
		 * level of \, magic file does another, then you need 2 x 2 x 2 == 8.
		 */
		Object test = type.convertTestString("regex", "hrm\\t\\0\\xB\\\\\\\\wow");
		byte[] bytes = "some line with hrm\t\0\13\\wow in it".getBytes();
		Object extracted = type.isMatch(test, null, false, null, new MutableOffset(0), bytes);
		assertNotNull(extracted);
		assertEquals("hrm\t\0\13\\wow", renderValue(extracted, type, new MagicFormatter("%s")));
	}

	@Test
	public void testExtractValueFromBytes() {
		new RegexType().extractValueFromBytes(0, null, true);
	}

	private String renderValue(Object extracted, RegexType type, MagicFormatter formatter) {
		StringBuilder sb = new StringBuilder();
		type.renderValue(sb, extracted, formatter);
		return sb.toString();
	}
}
