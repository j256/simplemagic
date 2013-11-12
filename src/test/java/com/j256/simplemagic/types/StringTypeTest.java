package com.j256.simplemagic.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.j256.simplemagic.entries.MagicFormatter;

public class StringTypeTest {

	@Test
	public void testBasicMatch() {
		StringType type = new StringType();
		Object info = type.convertTestString("string", "hello", 0);
		byte[] bytes = new byte[] { 'h', 'e', 'l', 'l', 'o', '2' };
		Object extract = type.isMatch(info, null, false, null, 0, bytes);
		assertNotNull(extract);
		bytes = new byte[] { ' ', 'e', 'l', 'l', 'o', '2' };
		assertNull(type.isMatch(info, null, false, extract, 0, bytes));
	}

	@Test
	public void testBasicNoMatch() {
		StringType type = new StringType();
		Object info = type.convertTestString("string", "hello", 0);
		byte[] bytes = new byte[] { 'h', 'e', 'l', 'l' };
		Object extract = type.extractValueFromBytes(0, bytes);
		assertNull(type.isMatch(info, null, false, extract, 0, bytes));
		bytes = new byte[] { 'h', 'e', 'l', 'l', 'p' };
		assertNull(type.isMatch(info, null, false, extract, 0, bytes));
	}

	@Test
	public void testOffset() {
		StringType type = new StringType();
		Object info = type.convertTestString("string", "hello", 0);
		byte[] bytes = new byte[] { 'w', 'o', 'w', 'h', 'e', 'l', 'l', 'o', '2', '3' };
		Object extract = type.extractValueFromBytes(0, bytes);
		assertNotNull(type.isMatch(info, null, false, extract, 3, bytes));
	}

	@Test
	public void testCaseInsensitive() {
		StringType type = new StringType();
		Object info = type.convertTestString("string/c", "hello", 0);
		byte[] bytes = new byte[] { 'h', 'e', 'l', 'l', 'o' };
		Object extract = type.extractValueFromBytes(0, bytes);
		assertNotNull(type.isMatch(info, null, false, extract, 0, bytes));
		bytes = new byte[] { 'h', 'E', 'l', 'l', 'o' };
		assertNotNull(type.isMatch(info, null, false, extract, 0, bytes));

		info = type.convertTestString("string/c", "Hello", 0);
		bytes = new byte[] { 'H', 'e', 'l', 'l', 'o' };
		assertNotNull(type.isMatch(info, null, false, extract, 0, bytes));
		bytes = new byte[] { 'H', 'E', 'l', 'l', 'o' };
		assertNotNull(type.isMatch(info, null, false, extract, 0, bytes));
	}

	@Test
	public void testOptionalWhitespace() {
		StringType type = new StringType();
		Object info = type.convertTestString("string/b", "hello", 0);
		byte[] bytes = new byte[] { 'h', ' ', 'e', ' ', 'l', ' ', 'l', ' ', 'o' };
		Object extract = type.extractValueFromBytes(0, bytes);
		assertNotNull(type.isMatch(info, null, false, extract, 0, bytes));
		bytes = new byte[] { 'h', 'e', 'l', 'l', 'o' };
		assertNotNull(type.isMatch(info, null, false, extract, 0, bytes));
		bytes = new byte[] { 'n', 'e', 'l', 'l', 'o' };
		assertNull(type.isMatch(info, null, false, extract, 0, bytes));
	}

	@Test
	public void testCompactWhitespace() {
		StringType type = new StringType();
		Object info = type.convertTestString("string/B", "h ello", 0);
		byte[] bytes = new byte[] { 'h', ' ', 'e', 'l', 'l', 'o' };
		Object extract = type.extractValueFromBytes(0, bytes);
		assertNotNull(type.isMatch(info, null, false, extract, 0, bytes));
		bytes = new byte[] { 'h', ' ', 'e', ' ', 'l', ' ', 'l', ' ', 'o' };
		assertNull(type.isMatch(info, null, false, extract, 0, bytes));
		bytes = new byte[] { 'h', ' ', ' ', 'e', 'l', 'l', 'o' };
		assertNotNull(type.isMatch(info, null, false, extract, 0, bytes));
		info = type.convertTestString("string/B", "h e llo", 0);
		bytes = new byte[] { 'h', ' ', ' ', 'e', ' ', ' ', ' ', 'l', 'l', 'o' };
		assertNotNull(type.isMatch(info, null, false, extract, 0, bytes));
		bytes = new byte[] { 'h', ' ', ' ', 'b', ' ', ' ', ' ', 'l', 'l', 'o' };
		assertNull(type.isMatch(info, null, false, extract, 0, bytes));
	}

	@Test
	public void testCompactWhitespacePlusCaseInsensitive() {
		StringType type = new StringType();
		Object info = type.convertTestString("string/Bc", "h ello", 0);
		byte[] bytes = new byte[] { 'h', ' ', 'e', 'l', 'l', 'o' };
		Object extract = type.extractValueFromBytes(0, bytes);
		assertNotNull(type.isMatch(info, null, false, extract, 0, bytes));
		bytes = new byte[] { 'h', ' ', ' ', 'e', 'l', 'l', 'o' };
		assertNotNull(type.isMatch(info, null, false, extract, 0, bytes));
		bytes = new byte[] { 'H', ' ', 'e', 'l', 'l', 'o' };
		assertNotNull(type.isMatch(info, null, false, extract, 0, bytes));
		bytes = new byte[] { 'H', ' ', ' ', 'E', 'L', 'L', 'O' };
		assertNotNull(type.isMatch(info, null, false, extract, 0, bytes));
	}

	@Test
	public void testCompactPlusOptionalWhitespace() {
		StringType type = new StringType();
		Object info = type.convertTestString("string/Bb", "h ello", 0);
		byte[] bytes = new byte[] { 'h', ' ', 'e', 'l', 'l', 'o' };
		Object extract = type.extractValueFromBytes(0, bytes);
		assertNotNull(type.isMatch(info, null, false, extract, 0, bytes));
		bytes = new byte[] { 'h', ' ', ' ', 'e', 'l', 'l', 'o' };
		assertNotNull(type.isMatch(info, null, false, extract, 0, bytes));
		bytes = new byte[] { 'h', ' ', 'e', ' ', 'l', 'l', 'o' };
		assertNotNull(type.isMatch(info, null, false, extract, 0, bytes));
		bytes = new byte[] { 'h', 'e', ' ', 'l', 'l', 'o' };
		assertNull(type.isMatch(info, null, false, extract, 0, bytes));
		bytes = new byte[] { 'h', ' ', ' ', ' ', 'e', ' ', 'l', 'l', 'o' };
		assertNotNull(type.isMatch(info, null, false, extract, 0, bytes));
	}

	@Test
	public void testRenderValue() {
		StringType type = new StringType();
		Object info = type.convertTestString("string/Bb", "h ello", 0);
		byte[] bytes = new byte[] { 'h', ' ', 'e', 'l', 'l', 'o' };
		Object extract = type.isMatch(info, null, false, null, 0, bytes);
		assertNotNull(extract);
		StringBuilder sb = new StringBuilder();
		type.renderValue(sb, extract, new MagicFormatter("%s"));
		assertEquals("h ello", sb.toString());

		bytes = new byte[] { 'h', ' ', ' ', ' ', 'e', 'l', 'l', 'o' };
		extract = type.isMatch(info, null, false, null, 0, bytes);
		assertNotNull(extract);
		sb.setLength(0);
		type.renderValue(sb, extract, new MagicFormatter("%s"));
		assertEquals("h   ello", sb.toString());
	}
}
