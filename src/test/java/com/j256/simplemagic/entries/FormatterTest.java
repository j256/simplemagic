package com.j256.simplemagic.entries;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

import com.j256.simplemagic.entries.Formatter;

public class FormatterTest {

	@Test
	public void testBasic() {
		String str = "hello there";
		Formatter formatter = new Formatter(str);
		assertEquals(str, formatToString(formatter, null));
	}

	@Test
	public void testInvalidc() {
		String str = "hello %z there";
		Formatter formatter = new Formatter(str);
		assertEquals(str, formatToString(formatter, null));
	}

	@Test
	public void testJustValue() {
		Formatter formatter = new Formatter("hello%##sthere");
		assertEquals("hellowowthere", formatToString(formatter, "wow"));
	}

	@Test
	public void testString() {
		String prefix = "hello ";
		String suffix = " there";
		Formatter formatter = new Formatter(prefix + "%s" + suffix);
		String val = "fwpfjwepfjwe";
		assertEquals(prefix + val + suffix, formatToString(formatter, val));

		formatter = new Formatter("a%3sb");
		assertEquals("a 12b", formatToString(formatter, "12"));
		formatter = new Formatter("a%-3sb");
		assertEquals("a12 b", formatToString(formatter, "12"));
		assertEquals("a12 b", formatToString(formatter, 12L));

		formatter = new Formatter("a%.3sb");
		assertEquals("a12b", formatToString(formatter, "12"));
		assertEquals("a123b", formatToString(formatter, "123"));
		assertEquals("a123b", formatToString(formatter, "12345"));

		formatter = new Formatter("a%5.3sb");
		assertEquals("a   12b", formatToString(formatter, "12"));
		assertEquals("a  123b", formatToString(formatter, "123"));
		assertEquals("a  123b", formatToString(formatter, "12345"));
	}

	@Test
	public void testNString() {
		String prefix = "hello ";
		String suffix = " there";
		Formatter formatter = new Formatter(prefix + "%b" + suffix);
		String val = "fwpfjwepfjwe";
		assertEquals(prefix + val + suffix, formatToString(formatter, val));

		formatter = new Formatter("a%3bb");
		assertEquals("a 12b", formatToString(formatter, "12"));
		formatter = new Formatter("a%-3bb");
		assertEquals("a12 b", formatToString(formatter, "12"));
		assertEquals("a12 b", formatToString(formatter, 12L));

		formatter = new Formatter("a%.3bb");
		assertEquals("a12b", formatToString(formatter, "12"));
		assertEquals("a123b", formatToString(formatter, "123"));
		assertEquals("a123b", formatToString(formatter, "12345"));

		formatter = new Formatter("a%5.3bb");
		assertEquals("a   12b", formatToString(formatter, "12"));
		assertEquals("a  123b", formatToString(formatter, "123"));
		assertEquals("a  123b", formatToString(formatter, "12345"));
	}

	@Test
	public void testPercentPercent() {
		Formatter formatter = new Formatter("1%%2");
		assertEquals("1%2", formatToString(formatter, null));

		formatter = new Formatter("%d%%");
		assertEquals("1%", formatToString(formatter, 1));

		formatter = new Formatter("%d%%%%");
		assertEquals("-12%%", formatToString(formatter, -12));

		formatter = new Formatter("%%%%%d%%%%");
		assertEquals("%%-12%%", formatToString(formatter, -12));

		formatter = new Formatter("%%%%d%%%%");
		assertEquals("%%d%%", formatToString(formatter, -12));
	}

	@Test
	public void testCharacgter() {
		Formatter formatter = new Formatter("a%cb");
		assertEquals("ab", formatToString(formatter, null));
		assertEquals("acb", formatToString(formatter, 'c'));
		assertEquals("aAb", formatToString(formatter, (int) 'A'));
		assertEquals("a1b", formatToString(formatter, "123"));
		assertEquals("ab", formatToString(formatter, ""));

		formatter = new Formatter("a%3cb");
		assertEquals("a  cb", formatToString(formatter, 'c'));
		assertEquals("a  ?b", formatToString(formatter, new Date()));
	}

	@Test
	public void testInteger() {
		Formatter formatter = new Formatter("a%3db");
		assertEquals("a  1b", formatToString(formatter, 1));
		assertEquals("a -1b", formatToString(formatter, -1));

		formatter = new Formatter("a%03db");
		assertEquals("a001b", formatToString(formatter, 1));
		assertEquals("a-01b", formatToString(formatter, -1));

		formatter = new Formatter("a%+3db");
		assertEquals("a +1b", formatToString(formatter, 1));
		assertEquals("a -1b", formatToString(formatter, -1));

		formatter = new Formatter("a%0+3db");
		assertEquals("a+01b", formatToString(formatter, 1));
		assertEquals("a-01b", formatToString(formatter, -1));

		formatter = new Formatter("a% db");
		assertEquals("a 1b", formatToString(formatter, 1));
		assertEquals("a-1b", formatToString(formatter, -1));

		formatter = new Formatter("a%-05db");
		assertEquals("a1    b", formatToString(formatter, 1));
		assertEquals("a-1   b", formatToString(formatter, -1));
	}

	@Test
	public void testOctal() {
		Formatter formatter = new Formatter("a%ob");
		assertEquals("a1b", formatToString(formatter, 1));
		assertEquals("a10b", formatToString(formatter, 8));
		assertEquals("a-10b", formatToString(formatter, -8));
		assertEquals("awowb", formatToString(formatter, "wow"));

		formatter = new Formatter("a%3ob");
		assertEquals("a  1b", formatToString(formatter, 1));
		assertEquals("a 10b", formatToString(formatter, 8));

		formatter = new Formatter("a%-3ob");
		assertEquals("a1  b", formatToString(formatter, 1));
		assertEquals("a10 b", formatToString(formatter, 8));
		assertEquals("a-10b", formatToString(formatter, -8));

		formatter = new Formatter("a%#-3ob");
		assertEquals("a01 b", formatToString(formatter, 1));
		assertEquals("a010b", formatToString(formatter, 8));
		assertEquals("a-010b", formatToString(formatter, -8));
	}

	@Test
	public void testHexLower() {
		Formatter formatter = new Formatter("a%xb");
		assertEquals("a1b", formatToString(formatter, 1));
		assertEquals("aab", formatToString(formatter, 10));
		assertEquals("a-ab", formatToString(formatter, -10));
		assertEquals("a10b", formatToString(formatter, 16));
		assertEquals("a-10b", formatToString(formatter, -16));
		assertEquals("awowb", formatToString(formatter, "wow"));

		formatter = new Formatter("a%3xb");
		assertEquals("a  1b", formatToString(formatter, 1));
		assertEquals("a  ab", formatToString(formatter, 10));
		assertEquals("a 10b", formatToString(formatter, 16));

		formatter = new Formatter("a%-3xb");
		assertEquals("a1  b", formatToString(formatter, 1));
		assertEquals("a10 b", formatToString(formatter, 16));
		assertEquals("a-10b", formatToString(formatter, -16));

		formatter = new Formatter("a%#-3xb");
		assertEquals("a0x1b", formatToString(formatter, 1));
		assertEquals("a0x10b", formatToString(formatter, 16));
		assertEquals("a-0x10b", formatToString(formatter, -16));
	}

	@Test
	public void testHexUpper() {
		Formatter formatter = new Formatter("a%Xb");
		assertEquals("a1b", formatToString(formatter, 1));
		assertEquals("aAb", formatToString(formatter, 10));
		assertEquals("a-Ab", formatToString(formatter, -10));
		assertEquals("a10b", formatToString(formatter, 16));
		assertEquals("a-10b", formatToString(formatter, -16));
		assertEquals("awowb", formatToString(formatter, "wow"));

		formatter = new Formatter("a%3Xb");
		assertEquals("a  1b", formatToString(formatter, 1));
		assertEquals("a  Ab", formatToString(formatter, 10));
		assertEquals("a 10b", formatToString(formatter, 16));

		formatter = new Formatter("a%-3Xb");
		assertEquals("a1  b", formatToString(formatter, 1));
		assertEquals("a10 b", formatToString(formatter, 16));
		assertEquals("a-10b", formatToString(formatter, -16));

		formatter = new Formatter("a%#-3Xb");
		assertEquals("a0X1b", formatToString(formatter, 1));
		assertEquals("a0XAb", formatToString(formatter, 10));
		assertEquals("a-0XAb", formatToString(formatter, -10));
		assertEquals("a0X10b", formatToString(formatter, 16));
		assertEquals("a-0X10b", formatToString(formatter, -16));
	}

	@Test
	public void testIntegerString() {
		Formatter formatter = new Formatter("%d");
		assertEquals("wow", formatToString(formatter, "wow"));
	}

	@Test
	public void testFloat() {
		Formatter formatter = new Formatter("%f");
		assertEquals("1.2", formatToString(formatter, 1.2F));
		assertEquals("-2.3", formatToString(formatter, -2.3F));
		assertEquals("3", formatToString(formatter, 3));
		assertEquals("300", formatToString(formatter, 300.0));
		assertEquals("wow", formatToString(formatter, "wow"));

		formatter = new Formatter("%.2f");
		assertEquals("1.20", formatToString(formatter, 1.2F));
		assertEquals("-2.30", formatToString(formatter, -2.3F));
		assertEquals("3.00", formatToString(formatter, 3));

		formatter = new Formatter("%.2f");
		assertEquals("nan", formatToString(formatter, Float.NaN));
		assertEquals("inf", formatToString(formatter, Float.POSITIVE_INFINITY));

		formatter = new Formatter("% 10.3f");
		assertEquals("     1.100", formatToString(formatter, 1.1F));

		formatter = new Formatter("%-10.3f");
		assertEquals("1.100     ", formatToString(formatter, 1.1F));
		assertEquals("-1.100    ", formatToString(formatter, -1.1F));

		formatter = new Formatter("%+10.3f");
		assertEquals("    +2.230", formatToString(formatter, 2.23F));
		assertEquals("    -2.230", formatToString(formatter, -2.23F));

		formatter = new Formatter("%-10.0f");
		assertEquals("12        ", formatToString(formatter, 12.34F));
	}

	@Test
	public void testFloatScientific() {
		Formatter formatter = new Formatter("%e");
		assertEquals("1.2E0", formatToString(formatter, 1.2F));
		assertEquals("1.234E3", formatToString(formatter, 1234F));
		assertEquals("-1.234E3", formatToString(formatter, -1234F));
		assertEquals("9.786E-1", formatToString(formatter, 0.9786F));

		formatter = new Formatter("%.1e");
		assertEquals("1.2E0", formatToString(formatter, 1.2F));
		assertEquals("1.2E3", formatToString(formatter, 1234F));
		assertEquals("-2.3E5", formatToString(formatter, -231234F));
		// rounded up
		assertEquals("9.8E-1", formatToString(formatter, 0.9786));

		formatter = new Formatter("%.0e");
		assertEquals("1E0", formatToString(formatter, 1.2F));
		assertEquals("1E3", formatToString(formatter, 1234F));
		assertEquals("-2E5", formatToString(formatter, -231234F));
		// rounded up
		assertEquals("1E0", formatToString(formatter, 0.9786));
	}

	@Test
	public void testFloatMixed() {
		Formatter formatter = new Formatter("%g");
		assertEquals("1.2", formatToString(formatter, 1.2F));
		assertEquals("12345", formatToString(formatter, 12345F));
		assertEquals("1E6", formatToString(formatter, 1000000F));
		assertEquals("-1E6", formatToString(formatter, -1000000F));
		assertEquals("1E6", formatToString(formatter, 1000001D));
		assertEquals("-1E6", formatToString(formatter, -1000001D));
	}

	@Test
	public void testCoverage() {
		formatToString(new Formatter("%1000s"), "wow");
	}

	private String formatToString(Formatter formatter, Object value) {
		StringBuilder sb = new StringBuilder();
		formatter.format(sb, value);
		return sb.toString();
	}
}
