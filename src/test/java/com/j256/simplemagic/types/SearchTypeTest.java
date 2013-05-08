package com.j256.simplemagic.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SearchTypeTest {

	@Test
	public void testBasicMatch() {
		SearchType type = new SearchType();
		String str = "hello";
		Object info = type.convertTestString("search", str, 0);
		byte[] bytes = new byte[] { 'h', 'e', 'l', 'l', 'o', '2' };
		assertEquals(str, type.isMatch(info, null, false, null, 0, bytes));
		bytes = new byte[] { ' ', 'e', 'l', 'l', 'o', '2' };
		assertNull(type.isMatch(info, null, false, null, 0, bytes));
	}

	@Test
	public void testSubLineMatch() {
		SearchType type = new SearchType();
		String str = "hello";
		Object info = type.convertTestString("search", str, 0);
		byte[] bytes = new byte[] { '1', '2', 'h', 'e', 'l', 'l', 'o', '2', '4' };
		assertEquals(str, type.isMatch(info, null, false, null, 0, bytes));
		bytes = new byte[] { ' ', 'e', 'l', 'l', 'o', '2' };
		assertNull(type.isMatch(info, null, false, null, 0, bytes));
	}

	@Test
	public void testNoMatch() {
		SearchType type = new SearchType();
		String str = "hello";
		Object info = type.convertTestString("search/10", str, 0);
		byte[] bytes = new byte[] { '1', '2', 'h', 'e', 'l', 'l', '2', '4' };
		assertNull(type.isMatch(info, null, false, null, 0, bytes));
		// no match after offset
		info = type.convertTestString("search/10", str, 1);
		bytes = new byte[] { '1', '2', '\n', 'h', 'e', '\n', 'l', 'l', '2', '4' };
		assertNull(type.isMatch(info, null, false, null, 1, bytes));
		// EOF before offset reached
		info = type.convertTestString("search", str, 10);
		bytes = new byte[] { '1', '2', '\n', 'h', 'e', '\n', 'l', 'l', '2', '4' };
		assertNull(type.isMatch(info, null, false, null, 10, bytes));
	}

	@Test
	public void testCoverage() {
		new SearchType().extractValueFromBytes(0, null);
	}

	@Test
	public void testMultipleLines() {
		SearchType type = new SearchType();
		String str = "hello";
		Object info = type.convertTestString("search/1", str, 0);
		byte[] bytes = new byte[] { '1', '2', '\n', '1', '2', 'h', 'e', 'l', 'l', 'o', '2', '4' };
		// no match on the first line
		assertNull(type.isMatch(info, null, false, null, 0, bytes));
		// match on the second line started at offset 1
		info = type.convertTestString("search/1", str, 1);
		assertEquals(str, type.isMatch(info, null, false, null, 1, bytes));
		// match on the second line with 2 lines looked at
		info = type.convertTestString("search/2", str, 0);
		assertEquals(str, type.isMatch(info, null, false, null, 0, bytes));
	}

	@Test
	public void testMultipleLinesOptionalWhitespace() {
		SearchType type = new SearchType();
		String str = "hello";
		Object info = type.convertTestString("search/1/b", str, 0);
		byte[] bytes = new byte[] { '1', '2', '\n', '1', '2', 'h', 'e', 'l', ' ', 'l', ' ', 'o', ' ', '2', '4' };
		// no match on the first line
		assertNull(type.isMatch(info, null, false, null, 0, bytes));
		// match on the second line started at offset 1
		info = type.convertTestString("search/1/b", str, 1);
		assertEquals("hel l o", type.isMatch(info, null, false, null, 1, bytes));
		// match on the second line with 2 lines looked at
		info = type.convertTestString("search/2/b", str, 0);
		assertEquals("hel l o", type.isMatch(info, null, false, null, 0, bytes));
	}
}
