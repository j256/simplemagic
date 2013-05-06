package com.j256.simplemagic.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.j256.simplemagic.entries.Formatter;

public class StringTypeTest {

	@Test
	public void testBasicMatch() {
		StringType type = new StringType();
		Object info = type.convertTestString("hello");
		byte[] bytes = new byte[] { 'h', 'e', 'l', 'l', 'o', '2' };
		Object extract = type.extractValueFromBytes(0, bytes);
		assertTrue(type.isMatch(info, null, false, extract, 0, bytes));
		bytes = new byte[] { ' ', 'e', 'l', 'l', 'o', '2' };
		assertFalse(type.isMatch(info, null, false, extract, 0, bytes));
	}

	@Test
	public void testBasicNoMatch() {
		StringType type = new StringType();
		Object info = type.convertTestString("hello");
		byte[] bytes = new byte[] { 'h', 'e', 'l', 'l' };
		Object extract = type.extractValueFromBytes(0, bytes);
		assertFalse(type.isMatch(info, null, false, extract, 0, bytes));
		bytes = new byte[] { 'h', 'e', 'l', 'l', 'p' };
		assertFalse(type.isMatch(info, null, false, extract, 0, bytes));
	}

	@Test
	public void testOffset() {
		StringType type = new StringType();
		Object info = type.convertTestString("hello");
		byte[] bytes = new byte[] { 'w', 'o', 'w', 'h', 'e', 'l', 'l', 'o', '2', '3' };
		Object extract = type.extractValueFromBytes(0, bytes);
		assertTrue(type.isMatch(info, null, false, extract, 3, bytes));
	}

	@Test
	public void testCaseInsensitive() {
		StringType type = new StringType();
		Object info = type.convertTestString("hello/c");
		byte[] bytes = new byte[] { 'h', 'e', 'l', 'l', 'o' };
		Object extract = type.extractValueFromBytes(0, bytes);
		assertTrue(type.isMatch(info, null, false, extract, 0, bytes));
		bytes = new byte[] { 'h', 'E', 'l', 'l', 'o' };
		assertTrue(type.isMatch(info, null, false, extract, 0, bytes));

		info = type.convertTestString("Hello/c");
		bytes = new byte[] { 'H', 'e', 'l', 'l', 'o' };
		assertTrue(type.isMatch(info, null, false, extract, 0, bytes));
		bytes = new byte[] { 'H', 'E', 'l', 'l', 'o' };
		assertTrue(type.isMatch(info, null, false, extract, 0, bytes));
	}

	@Test
	public void testOptionalWhitespace() {
		StringType type = new StringType();
		Object info = type.convertTestString("hello/b");
		byte[] bytes = new byte[] { 'h', ' ', 'e', ' ', 'l', ' ', 'l', ' ', 'o' };
		Object extract = type.extractValueFromBytes(0, bytes);
		assertTrue(type.isMatch(info, null, false, extract, 0, bytes));
		bytes = new byte[] { 'h', 'e', 'l', 'l', 'o' };
		assertTrue(type.isMatch(info, null, false, extract, 0, bytes));
		bytes = new byte[] { 'n', 'e', 'l', 'l', 'o' };
		assertFalse(type.isMatch(info, null, false, extract, 0, bytes));
	}

	@Test
	public void testCompactWhitespace() {
		StringType type = new StringType();
		Object info = type.convertTestString("h ello/B");
		byte[] bytes = new byte[] { 'h', ' ', 'e', 'l', 'l', 'o' };
		Object extract = type.extractValueFromBytes(0, bytes);
		assertTrue(type.isMatch(info, null, false, extract, 0, bytes));
		bytes = new byte[] { 'h', ' ', 'e', ' ', 'l', ' ', 'l', ' ', 'o' };
		assertFalse(type.isMatch(info, null, false, extract, 0, bytes));
		bytes = new byte[] { 'h', ' ', ' ', 'e', 'l', 'l', 'o' };
		assertTrue(type.isMatch(info, null, false, extract, 0, bytes));
		info = type.convertTestString("h e llo/B");
		bytes = new byte[] { 'h', ' ', ' ', 'e', ' ', ' ', ' ', 'l', 'l', 'o' };
		assertTrue(type.isMatch(info, null, false, extract, 0, bytes));
		bytes = new byte[] { 'h', ' ', ' ', 'b', ' ', ' ', ' ', 'l', 'l', 'o' };
		assertFalse(type.isMatch(info, null, false, extract, 0, bytes));
	}

	@Test
	public void testCompactWhitespacePlusCaseInsensitive() {
		StringType type = new StringType();
		Object info = type.convertTestString("h ello/Bc");
		byte[] bytes = new byte[] { 'h', ' ', 'e', 'l', 'l', 'o' };
		Object extract = type.extractValueFromBytes(0, bytes);
		assertTrue(type.isMatch(info, null, false, extract, 0, bytes));
		bytes = new byte[] { 'h', ' ', ' ', 'e', 'l', 'l', 'o' };
		assertTrue(type.isMatch(info, null, false, extract, 0, bytes));
		bytes = new byte[] { 'H', ' ', 'e', 'l', 'l', 'o' };
		assertTrue(type.isMatch(info, null, false, extract, 0, bytes));
		bytes = new byte[] { 'H', ' ', ' ', 'E', 'L', 'L', 'O' };
		assertTrue(type.isMatch(info, null, false, extract, 0, bytes));
	}

	@Test
	public void testCompactPlusOptionalWhitespace() {
		StringType type = new StringType();
		Object info = type.convertTestString("h ello/Bb");
		byte[] bytes = new byte[] { 'h', ' ', 'e', 'l', 'l', 'o' };
		Object extract = type.extractValueFromBytes(0, bytes);
		assertTrue(type.isMatch(info, null, false, extract, 0, bytes));
		bytes = new byte[] { 'h', ' ', ' ', 'e', 'l', 'l', 'o' };
		assertTrue(type.isMatch(info, null, false, extract, 0, bytes));
		bytes = new byte[] { 'h', ' ', 'e', ' ', 'l', 'l', 'o' };
		assertTrue(type.isMatch(info, null, false, extract, 0, bytes));
		bytes = new byte[] { 'h', 'e', ' ', 'l', 'l', 'o' };
		assertFalse(type.isMatch(info, null, false, extract, 0, bytes));
		bytes = new byte[] { 'h', ' ', ' ', ' ', 'e', ' ', 'l', 'l', 'o' };
		assertTrue(type.isMatch(info, null, false, extract, 0, bytes));
	}

	@Test
	public void testRenderValue() {
		StringType type = new StringType();
		Object info = type.convertTestString("h ello/Bb");
		byte[] bytes = new byte[] { 'h', ' ', 'e', 'l', 'l', 'o' };
		Object extract = type.extractValueFromBytes(0, bytes);
		assertTrue(type.isMatch(info, null, false, extract, 0, bytes));
		StringBuilder sb = new StringBuilder();
		type.renderValue(sb, extract, new Formatter("%s"));
		assertEquals("h ello", sb.toString());

		bytes = new byte[] { 'h', ' ', ' ', ' ', 'e', 'l', 'l', 'o' };
		assertTrue(type.isMatch(info, null, false, extract, 0, bytes));
		sb.setLength(0);
		type.renderValue(sb, extract, new Formatter("%s"));
		assertEquals("h   ello", sb.toString());
	}
}
