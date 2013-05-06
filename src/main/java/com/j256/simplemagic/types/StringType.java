package com.j256.simplemagic.types;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.j256.simplemagic.entries.Formatter;
import com.j256.simplemagic.entries.MagicMatcher;

/**
 * A string of bytes. The string type specification can be optionally followed by /[Bbc]*. The ``B'' flag compacts
 * whitespace in the target, which must contain at least one whitespace character. If the magic has n consecutive
 * blanks, the target needs at least n consecutive blanks to match. The ``b'' flag treats every blank in the target as
 * an optional blank. Finally the ``c'' flag, specifies case insensitive matching: lower-case characters in the magic
 * match both lower and upper case characters in the target, whereas upper case characters in the magic only match
 * upper-case characters in the target.
 * 
 * @author graywatson
 */
public class StringType implements MagicMatcher {

	private final static Pattern TARGET_PATTERN = Pattern.compile("(.*)/([Bbc]*)");

	public Object convertTestString(String test) {
		Matcher matcher = TARGET_PATTERN.matcher(test);
		if (!matcher.matches()) {
			return new StringTestInfo(preProcessPattern(test), false, false, false);
		}
		boolean compactWhiteSpace = false;
		boolean optionalWhiteSpace = false;
		boolean caseInsensitive = false;
		for (char ch : matcher.group(2).toCharArray()) {
			switch (ch) {
				case 'B' :
					compactWhiteSpace = true;
					break;
				case 'b' :
					optionalWhiteSpace = true;
					break;
				case 'c' :
					caseInsensitive = true;
					break;
			}
		}
		return new StringTestInfo(preProcessPattern(matcher.group(1)), compactWhiteSpace, optionalWhiteSpace,
				caseInsensitive);
	}

	public Object extractValueFromBytes(int offset, byte[] bytes) {
		return new ExtractedValue();
	}

	public boolean isMatch(Object testValue, Long andValue, boolean unsignedType, Object extractedValue, int offset,
			byte[] bytes) {
		StringTestInfo info = (StringTestInfo) testValue;
		int targetPos = offset;
		boolean lastMagicCompactWhitespace = false;
		for (int magicPos = 0; magicPos < info.pattern.length(); magicPos++) {
			char magicCh = info.pattern.charAt(magicPos);
			// did we reach the end?
			if (targetPos >= bytes.length) {
				return false;
			}
			char targetCh = (char) (bytes[targetPos++] & 0xFF);

			// if it matches, we are done
			if (magicCh == targetCh) {
				if (info.compactWhiteSpace) {
					lastMagicCompactWhitespace = Character.isWhitespace(magicCh);
				}
				continue;
			}

			// if it doesn't match, maybe the target is a whitespace
			if ((lastMagicCompactWhitespace || info.optionalWhiteSpace) && Character.isWhitespace(targetCh)) {
				do {
					targetCh = (char) (bytes[targetPos++] & 0xFF);
				} while (Character.isWhitespace(targetCh));
				// now that we get to the first non-whitespace, it must match
				if (magicCh == targetCh) {
					if (info.compactWhiteSpace) {
						lastMagicCompactWhitespace = Character.isWhitespace(magicCh);
					}
					continue;
				}
				// if it doesn't match, check the case insensitive
			}

			// maybe it doesn't match because of case insensitive handling and magic-char is lowercase
			if (info.caseInsensitive && Character.isLowerCase(magicCh)) {
				if (magicCh == Character.toLowerCase(targetCh)) {
					// matches
					continue;
				}
				// upper-case characters must match
			}

			return false;
		}

		ExtractedValue value = (ExtractedValue) extractedValue;
		char[] chars = new char[targetPos - offset];
		for (int i = 0; i < chars.length; i++) {
			chars[i] = (char) (bytes[offset + i] & 0xFF);
		}
		value.string = new String(chars);

		// now that we've matched, we construct our matching string
		return true;
	}

	public void renderValue(StringBuilder sb, Object extractedValue, Formatter formatter) {
		ExtractedValue value = (ExtractedValue) extractedValue;
		formatter.format(sb, value.string);
	}

	private String preProcessPattern(String pattern) {
		int index = pattern.indexOf('\\');
		if (index < 0) {
			return pattern;
		}

		StringBuilder sb = new StringBuilder();
		for (int pos = 0; pos < pattern.length(); pos++) {
			char ch = pattern.charAt(pos);
			if (ch != '\\') {
				sb.append(ch);
				continue;
			}
			if (pos + 1 >= pattern.length()) {
				sb.append(ch);
				break;
			}
			ch = pattern.charAt(++pos);
			switch (ch) {
				case 'b' :
					sb.append('\b');
					break;
				case 'f' :
					sb.append('\f');
					break;
				case 'n' :
					sb.append('\n');
					break;
				case '0' :
				case '1' :
				case '2' :
				case '3' : {
					// \017
					int len = 3;
					if (pos + len <= pattern.length()) {
						int octal = radixCharsToChar(pattern, pos, len, 8);
						if (octal >= 0) {
							sb.append((char) octal);
							pos += len - 1;
							break;
						}
					} else if (ch == '0') {
						sb.append('\0');
					} else {
						sb.append(ch);
					}
					break;
				}
				case 'r' :
					sb.append('\r');
					break;
				case 't' :
					sb.append('\t');
					break;
				case 'x' : {
					// \xD9
					int len = 2;
					if (pos + len < pattern.length()) {
						int hex = radixCharsToChar(pattern, pos + 1, len, 16);
						if (hex >= 0) {
							sb.append((char) hex);
							pos += len;
							break;
						}
					} else {
						sb.append(ch);
					}
					break;
				}
				case '\\' :
				default :
					sb.append(ch);
					break;
			}
		}
		return sb.toString();
	}

	private int radixCharsToChar(String pattern, int pos, int len, int radix) {
		if (pos + len > pattern.length()) {
			return -1;
		}
		int val = 0;
		for (int i = 0; i < len; i++) {
			int digit = Character.digit(pattern.charAt(pos + i), radix);
			if (digit < 0) {
				return -1;
			}
			val = val * radix + digit;
		}
		return val;
	}

	/**
	 * Extracted value. We have to do this because the match sets the extracted value from its length.
	 */
	private static class ExtractedValue {
		String string;
		@Override
		public String toString() {
			if (string == null) {
				return "not set";
			} else {
				return string;
			}
		}
	}

	private static class StringTestInfo {
		final String pattern;
		final boolean compactWhiteSpace;
		final boolean optionalWhiteSpace;
		final boolean caseInsensitive;

		public StringTestInfo(String pattern, boolean compactWhiteSpace, boolean optionalWhiteSpace,
				boolean caseInsensitive) {
			this.pattern = pattern;
			this.compactWhiteSpace = compactWhiteSpace;
			this.optionalWhiteSpace = optionalWhiteSpace;
			this.caseInsensitive = caseInsensitive;
		}

		@Override
		public String toString() {
			return pattern;
		}
	}
}
