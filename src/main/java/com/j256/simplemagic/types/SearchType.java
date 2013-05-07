package com.j256.simplemagic.types;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.j256.simplemagic.entries.Formatter;
import com.j256.simplemagic.entries.MagicMatcher;

/**
 * A literal string search starting at the given offset. The same modifier flags can be used as for string patterns. The
 * modifier flags (if any) must be followed by /number the range, that is, the number of positions at which the match
 * will be attempted, starting from the start offset. This is suitable for searching larger binary expressions with
 * variable offsets, using \ escapes for special characters. The offset works as for regex. *
 * 
 * @author graywatson
 */
public class SearchType implements MagicMatcher {

	private final static Pattern TARGET_PATTERN = Pattern.compile("(.*)(/[Bbc]*)?(/(\\d+))?");
	private static final String EMPTY = "";

	public Object convertTestString(String test, int offset) {
		Matcher matcher = TARGET_PATTERN.matcher(test);
		if (!matcher.matches()) {
			return new SearchTestInfo(preProcessPattern(test), false, false, false, 0);
		}
		boolean compactWhiteSpace = false;
		boolean optionalWhiteSpace = false;
		boolean caseInsensitive = false;
		if (matcher.group(2) != null && matcher.group(2).length() > 0) {
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
		}
		int numLines = 1;
		if (matcher.group(4) != null && matcher.group(4).length() > 0) {
			try {
				numLines = Integer.decode(matcher.group(4));
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Invalid format for search length: " + test);
			}
		}
		// numLines gets added to offset to get max-offset
		return new SearchTestInfo(preProcessPattern(matcher.group(1)), compactWhiteSpace, optionalWhiteSpace,
				caseInsensitive, offset + numLines);
	}

	public Object extractValueFromBytes(int offset, byte[] bytes) {
		return EMPTY;
	}

	public Object isMatch(Object testValue, Long andValue, boolean unsignedType, Object extractedValue, int offset,
			byte[] bytes) {
		SearchTestInfo info = (SearchTestInfo) testValue;
		BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes)));
		try {
			int lineCount;
			// if offset is 1 then we need to pre-read 1 line
			for (lineCount = 0; lineCount < offset; lineCount++) {
				// if eof then no match
				if (reader.readLine() == null) {
					return null;
				}
			}

			for (; lineCount < info.maxOffset; lineCount++) {
				String line = reader.readLine();
				// if eof then no match
				if (line == null) {
					break;
				}
				for (int i = 0; i < line.length(); i++) {
					String match = checkLine(info, line, i);
					if (match != null) {
						return match;
					}
				}
			}
			return null;
		} catch (IOException e) {
			// probably won't get here
			return null;
		}
	}

	private String checkLine(SearchTestInfo info, String line, int startOffset) {

		boolean lastMagicCompactWhitespace = false;
		int targetPos = startOffset;
		for (int magicPos = 0; magicPos < info.pattern.length(); magicPos++) {
			char magicCh = info.pattern.charAt(magicPos);
			// did we reach the end?
			if (targetPos >= line.length()) {
				return null;
			}
			char targetCh = line.charAt(targetPos++);

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
					targetCh = line.charAt(targetPos++);
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

		return line.substring(startOffset, targetPos);
	}

	public void renderValue(StringBuilder sb, Object extractedValue, Formatter formatter) {
		formatter.format(sb, extractedValue);
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

	private static class SearchTestInfo {
		final String pattern;
		final boolean compactWhiteSpace;
		final boolean optionalWhiteSpace;
		final boolean caseInsensitive;
		final int maxOffset;

		public SearchTestInfo(String pattern, boolean compactWhiteSpace, boolean optionalWhiteSpace,
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
