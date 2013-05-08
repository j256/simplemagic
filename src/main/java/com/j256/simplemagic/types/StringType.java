package com.j256.simplemagic.types;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.j256.simplemagic.entries.Formatter;
import com.j256.simplemagic.entries.MagicMatcher;

/**
 * From the magic(5) man page: A string of bytes. The string type specification can be optionally followed by /[Bbc]*.
 * The ``B'' flag compacts whitespace in the target, which must contain at least one whitespace character. If the magic
 * has n consecutive blanks, the target needs at least n consecutive blanks to match. The ``b'' flag treats every blank
 * in the target as an optional blank. Finally the ``c'' flag, specifies case insensitive matching: lower-case
 * characters in the magic match both lower and upper case characters in the target, whereas upper case characters in
 * the magic only match upper-case characters in the target.
 * 
 * @author graywatson
 */
public class StringType implements MagicMatcher {

	private final static Pattern TYPE_PATTERN = Pattern.compile("[^/]+(/\\d+)?(/[BbcwWt]*)?");
	private static final String EMPTY = "";
	private static final int MAX_NUM_LINES = 20;

	public Object convertTestString(String typeStr, String testStr, int offset) {
		Matcher matcher = TYPE_PATTERN.matcher(typeStr);
		if (!matcher.matches()) {
			// may not be able to get here
			return new StringTestInfo(preProcessPattern(testStr), false, false, false, 0);
		}
		// max-offset is ignored by the search type
		int maxOffset = 1;
		String lengthStr = matcher.group(1);
		if (lengthStr != null && lengthStr.length() > 1) {
			// default is 1 line
			int numLines = 1;
			try {
				// skip the '/'
				numLines = Integer.decode(lengthStr.substring(1));
			} catch (NumberFormatException e) {
				// may not be able to get here
				throw new IllegalArgumentException("Invalid format for search length: " + testStr);
			}
			if (numLines > MAX_NUM_LINES) {
				numLines = MAX_NUM_LINES;
			}
			// numLines gets added to offset to get max-offset
			maxOffset = offset + numLines;
		}
		boolean compactWhiteSpace = false;
		boolean optionalWhiteSpace = false;
		boolean caseInsensitive = false;
		String flagsStr = matcher.group(2);
		if (flagsStr != null) {
			for (char ch : flagsStr.toCharArray()) {
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
					case 't' :
					case 'w' :
					case 'W' :
						// XXX: no idea what these do
						break;
				}
			}
		}
		String processedPattern = preProcessPattern(testStr);
		return new StringTestInfo(processedPattern, compactWhiteSpace, optionalWhiteSpace, caseInsensitive, maxOffset);
	}

	public Object extractValueFromBytes(int offset, byte[] bytes) {
		return EMPTY;
	}

	public Object isMatch(Object testValue, Long andValue, boolean unsignedType, Object extractedValue, int offset,
			byte[] bytes) {
		// find the match in the array of bytes
		return findOffsetMatch((StringTestInfo) testValue, offset, bytes, null);
	}

	public void renderValue(StringBuilder sb, Object extractedValue, Formatter formatter) {
		formatter.format(sb, extractedValue);
	}

	/**
	 * Called from the string and search types to see if a string or byte array matches our pattern.
	 */
	protected String findOffsetMatch(StringTestInfo info, int offset, byte[] bytes, String line) {
		int targetPos = offset;
		int length;
		if (bytes == null) {
			length = line.length();
		} else {
			length = bytes.length;
		}
		boolean lastMagicCompactWhitespace = false;
		for (int magicPos = 0; magicPos < info.pattern.length(); magicPos++) {
			char magicCh = info.pattern.charAt(magicPos);
			// did we reach the end?
			if (targetPos >= length) {
				return null;
			}
			char targetCh;
			if (bytes == null) {
				targetCh = line.charAt(targetPos);
			} else {
				targetCh = (char) (bytes[targetPos] & 0xFF);
			}
			targetPos++;

			// if it matches, we can continue
			if (magicCh == targetCh) {
				if (info.compactWhiteSpace) {
					lastMagicCompactWhitespace = Character.isWhitespace(magicCh);
				}
				continue;
			}

			// if it doesn't match, maybe the target is a whitespace
			if ((lastMagicCompactWhitespace || info.optionalWhiteSpace) && Character.isWhitespace(targetCh)) {
				do {
					if (targetPos >= length) {
						break;
					}
					if (bytes == null) {
						targetCh = line.charAt(targetPos);
					} else {
						targetCh = (char) (bytes[targetPos] & 0xFF);
					}
					targetPos++;
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

			return null;
		}

		if (bytes == null) {
			return line.substring(offset, targetPos);
		} else {
			char[] chars = new char[targetPos - offset];
			for (int i = 0; i < chars.length; i++) {
				chars[i] = (char) (bytes[offset + i] & 0xFF);
			}
			return new String(chars);
		}
	}

	/**
	 * Pre-processes the pattern by handling \007 type of escapes and others.
	 */
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
				// we'll end the pattern with a '\\' char
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
				case ' ' :
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
	 * Internal holder for test information about strings.
	 */
	protected static class StringTestInfo {
		final String pattern;
		final boolean compactWhiteSpace;
		final boolean optionalWhiteSpace;
		final boolean caseInsensitive;
		// ignored by the search type
		final int maxOffset;

		public StringTestInfo(String pattern, boolean compactWhiteSpace, boolean optionalWhiteSpace,
				boolean caseInsensitive, int maxOffset) {
			this.pattern = pattern;
			this.compactWhiteSpace = compactWhiteSpace;
			this.optionalWhiteSpace = optionalWhiteSpace;
			this.caseInsensitive = caseInsensitive;
			this.maxOffset = maxOffset;
		}

		@Override
		public String toString() {
			return pattern;
		}
	}
}
