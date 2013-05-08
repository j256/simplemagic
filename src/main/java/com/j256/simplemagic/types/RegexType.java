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
 * From the magic(5) man page: A regular expression match in extended POSIX regular expression syntax (like egrep).
 * Regular expressions can take exponential time to process, and their performance is hard to predict, so their use is
 * discouraged. When used in production environments, their performance should be carefully checked. The type
 * specification can be optionally followed by /[c][s]. The 'c' flag makes the match case insensitive, while the 's'
 * flag update the offset to the start offset of the match, rather than the end. The regular expression is tested
 * against line N + 1 onwards, where N is the given offset. Line endings are assumed to be in the machine's native
 * format. ^ and $ match the beginning and end of individual lines, respectively, not beginning and end of file.
 * 
 * @author graywatson
 */
public class RegexType implements MagicMatcher {

	private final static Pattern TYPE_PATTERN = Pattern.compile("[^/]+(/[cs]*)?");
	private static final String EMPTY = "";

	public Object convertTestString(String typeStr, String testStr, int offset) {
		Matcher matcher = TYPE_PATTERN.matcher(typeStr);
		PatternInfo patternInfo = new PatternInfo();
		if (matcher.matches()) {
			String flagsStr = matcher.group(1);
			if (flagsStr != null && flagsStr.length() > 1) {
				for (char ch : flagsStr.toCharArray()) {
					if (ch == 'c') {
						patternInfo.patternFlags |= Pattern.CASE_INSENSITIVE;
					} else if (ch == 's') {
						patternInfo.updateOffsetStart = true;
					}
				}
			}
		}
		patternInfo.pattern = Pattern.compile(".*(" + testStr + ").*", patternInfo.patternFlags);
		return patternInfo;
	}

	public Object extractValueFromBytes(int offset, byte[] bytes) {
		return EMPTY;
	}

	public Object isMatch(Object testValue, Long andValue, boolean unsignedType, Object extractedValue, int offset,
			byte[] bytes) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes)));
		String line = null;
		for (int i = 0; i <= offset; i++) {
			try {
				line = reader.readLine();
				// if eof then no match
				if (line == null) {
					return null;
				}
			} catch (IOException e) {
				// probably won't get here
				return null;
			}
		}
		if (line == null) {
			// may never get here
			return null;
		}
		PatternInfo patternInfo = (PatternInfo) testValue;
		Matcher matcher = patternInfo.pattern.matcher(line);
		if (matcher.matches()) {
			return matcher.group(1);
		} else {
			return null;
		}
	}

	public void renderValue(StringBuilder sb, Object extractedValue, Formatter formatter) {
		formatter.format(sb, extractedValue);
	}

	private static class PatternInfo {
		int patternFlags;
		// TODO: need to implement this flag
		@SuppressWarnings("unused")
		boolean updateOffsetStart;
		Pattern pattern;
	}
}
