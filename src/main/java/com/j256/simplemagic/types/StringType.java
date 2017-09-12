package com.j256.simplemagic.types;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.j256.simplemagic.entries.MagicFormatter;
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
	protected static final String EMPTY = "";

	@Override
	public Object convertTestString(String typeStr, String testStr) {
		Matcher matcher = TYPE_PATTERN.matcher(typeStr);
		if (!matcher.matches()) {
			// may not be able to get here
			return new TestInfo(StringOperator.DEFAULT_OPERATOR, (testStr), false, false, false, 0);
		}
		// max-offset is ignored by the string type
		int maxOffset = 0;
		String lengthStr = matcher.group(1);
		if (lengthStr != null && lengthStr.length() > 1) {
			try {
				// skip the '/'
				maxOffset = Integer.decode(lengthStr.substring(1));
			} catch (NumberFormatException e) {
				// may not be able to get here
				throw new IllegalArgumentException("Invalid format for search length: " + testStr);
			}
		}
		boolean compactWhiteSpace = false;
		boolean optionalWhiteSpace = false;
		boolean caseInsensitive = false;
		String flagsStr = matcher.group(2);
		if (flagsStr != null) {
			// look at flags/modifiers
			for (char ch : flagsStr.toCharArray()) {
				switch (ch) {
					case 'B':
						compactWhiteSpace = true;
						break;
					case 'b':
						optionalWhiteSpace = true;
						break;
					case 'c':
						caseInsensitive = true;
						break;
					case 't':
					case 'w':
					case 'W':
						// XXX: no idea what these do
						break;
				}
			}
		}
		StringOperator operator = StringOperator.fromTest(testStr);
		if (operator == null) {
			operator = StringOperator.DEFAULT_OPERATOR;
		} else {
			testStr = testStr.substring(1);
		}
		String processedPattern = preProcessPattern(testStr);
		return new TestInfo(operator, processedPattern, compactWhiteSpace, optionalWhiteSpace, caseInsensitive,
				maxOffset);
	}

	@Override
	public Object extractValueFromBytes(int offset, byte[] bytes, boolean required) {
		return EMPTY;
	}

	@Override
	public Object isMatch(Object testValue, Long andValue, boolean unsignedType, Object extractedValue,
			MutableOffset mutableOffset, byte[] bytes) {
		return findOffsetMatch((TestInfo) testValue, mutableOffset.offset, mutableOffset, bytes, null, bytes.length);
	}

	@Override
	public void renderValue(StringBuilder sb, Object extractedValue, MagicFormatter formatter) {
		formatter.format(sb, extractedValue);
	}

	@Override
	public byte[] getStartingBytes(Object testValue) {
		if (testValue == null) {
			return null;
		} else {
			return ((TestInfo) testValue).getStartingBytes();
		}
	}

	/**
	 * Find offset match either in an array of bytes or chars, which ever is not null.
	 */
	protected String findOffsetMatch(TestInfo info, int startOffset, MutableOffset mutableOffset, byte[] bytes,
			char[] chars, int maxPos) {

		int targetPos = startOffset;
		boolean lastMagicCompactWhitespace = false;
		for (int magicPos = 0; magicPos < info.pattern.length(); magicPos++) {
			char magicCh = info.pattern.charAt(magicPos);
			boolean lastChar = (magicPos == info.pattern.length() - 1);
			// did we reach the end?
			if (targetPos >= maxPos) {
				return null;
			}
			char targetCh;
			if (bytes == null) {
				targetCh = chars[targetPos];
			} else {
				targetCh = charFromByte(bytes, targetPos);
			}
			targetPos++;

			// if it matches, we can continue
			if (info.operator.doTest(targetCh, magicCh, lastChar)) {
				if (info.compactWhiteSpace) {
					lastMagicCompactWhitespace = Character.isWhitespace(magicCh);
				}
				continue;
			}

			// if it doesn't match, maybe the target is a whitespace
			if ((lastMagicCompactWhitespace || info.optionalWhiteSpace) && Character.isWhitespace(targetCh)) {
				do {
					if (targetPos >= maxPos) {
						break;
					}
					if (bytes == null) {
						targetCh = chars[targetPos];
					} else {
						targetCh = charFromByte(bytes, targetPos);
					}
					targetPos++;
				} while (Character.isWhitespace(targetCh));
				// now that we get to the first non-whitespace, it must match
				if (info.operator.doTest(targetCh, magicCh, lastChar)) {
					if (info.compactWhiteSpace) {
						lastMagicCompactWhitespace = Character.isWhitespace(magicCh);
					}
					continue;
				}
				// if it doesn't match, check the case insensitive
			}

			// maybe it doesn't match because of case insensitive handling and magic-char is lowercase
			if (info.caseInsensitive && Character.isLowerCase(magicCh)) {
				if (info.operator.doTest(Character.toLowerCase(targetCh), magicCh, lastChar)) {
					// matches
					continue;
				}
				// upper-case characters must match
			}

			return null;
		}

		if (bytes == null) {
			chars = Arrays.copyOfRange(chars, startOffset, targetPos);
		} else {
			chars = new char[targetPos - startOffset];
			for (int i = 0; i < chars.length; i++) {
				chars[i] = charFromByte(bytes, startOffset + i);
			}
		}
		mutableOffset.offset = targetPos;
		return new String(chars);
	}

	private char charFromByte(byte[] bytes, int index) {
		return (char) (bytes[index] & 0xFF);
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
				case 'b':
					sb.append('\b');
					break;
				case 'f':
					sb.append('\f');
					break;
				case 'n':
					sb.append('\n');
					break;
				case '0':
				case '1':
				case '2':
				case '3': 
				case '4':
				case '5':
				case '6':
				case '7': {
					// \o or \oo or \ooo ... where o is an octal digit
					int octal = Character.digit(ch, 8);
					for (int i = 1; i <= 2 && pos + 1 < pattern.length(); i++) {
						ch = pattern.charAt(pos + 1);
						int digit = Character.digit(ch, 8);
						if (digit >= 0) {
							octal = octal * 8 + digit;
							pos++;
						} else {
							break;
						}
					}
					sb.append((char) (octal & 0xff));
					break;
				}
				case 'r':
					sb.append('\r');
					break;
				case 't':
					sb.append('\t');
					break;
				case 'a':
					sb.append('\007');
					break;
				case 'v':
					sb.append('\013');
					break;
				case 'x': {
					// \xh or \xhh ... where h is a hexadecimal digit
					int hex = Character.digit(pattern.charAt(pos + 1), 16);
					if (hex >= 0) {
						pos++;
						if (pos + 1 < pattern.length()) {
							int digit = Character.digit(pattern.charAt(pos + 1), 16);
							if (digit >= 0) {
								hex = hex * 16 + digit;
								pos++;
							}
						}
						sb.append((char) (hex & 0xff));
					} else {
						// if there is no valid hex digit treat \x as x
						sb.append(ch);
					}
					break;
				}
				case ' ':
				case '\\':
				default:
					sb.append(ch);
					break;
			}
		}
		return sb.toString();
	}

	/**
	 * Internal holder for test information about strings.
	 */
	protected static class TestInfo {
		final StringOperator operator;
		final String pattern;
		final boolean compactWhiteSpace;
		final boolean optionalWhiteSpace;
		final boolean caseInsensitive;
		// ignored by the string type
		final int maxOffset;

		public TestInfo(StringOperator operator, String pattern, boolean compactWhiteSpace, boolean optionalWhiteSpace,
				boolean caseInsensitive, int maxOffset) {
			this.operator = operator;
			this.pattern = pattern;
			this.compactWhiteSpace = compactWhiteSpace;
			this.optionalWhiteSpace = optionalWhiteSpace;
			this.caseInsensitive = caseInsensitive;
			this.maxOffset = maxOffset;
		}

		/**
		 * Get the bytes that start the pattern from an optimization standpoint.
		 */
		public byte[] getStartingBytes() {
			if (pattern == null || pattern.length() < 4) {
				return null;
			} else {
				return new byte[] { (byte) pattern.charAt(0), (byte) pattern.charAt(1), (byte) pattern.charAt(2),
						(byte) pattern.charAt(3) };
			}
		}

		@Override
		public String toString() {
			return pattern;
		}
	}
}
