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
 * A regular expression match in extended POSIX regular expression syntax (like egrep). Regular expressions can take
 * exponential time to process, and their performance is hard to predict, so their use is discouraged. When used in
 * production environments, their performance should be carefully checked. The type specification can be optionally
 * followed by /[c][s]. The 'c' flag makes the match case insensitive, while the 's' flag update the offset to the start
 * offset of the match, rather than the end. The regular expression is tested against line N + 1 onwards, where N is the
 * given offset. Line endings are assumed to be in the machine's native format. ^ and $ match the beginning and end of
 * individual lines, respectively, not beginning and end of file.
 * 
 * @author graywatson
 */
public class RegexType implements MagicMatcher {

	public Object convertTestString(String test) {
		int index = test.lastIndexOf('/');
		PatternInfo patternInfo = new PatternInfo();
		if (index >= 0 && (index == test.length() - 2 || index == test.length() - 3)) {
			boolean valid = handleFlag(patternInfo, test.charAt(index + 1));
			if (valid && index == test.length() - 3) {
				valid = handleFlag(patternInfo, test.charAt(index + 2));
			}
			if (valid) {
				// strip off the flag / suffix
				test = test.substring(0, index);
			}
		}
		patternInfo.pattern = Pattern.compile(".*(" + test + ").*", patternInfo.patternFlags);
		return patternInfo;
	}

	public Object extractValueFromBytes(int offset, byte[] bytes) {
		return new ExtractedValue();
	}

	public boolean isMatch(Object testValue, Long andValue, boolean unsignedType, Object extractedValue, int offset,
			byte[] bytes) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes)));
		String line = null;
		for (int i = 0; i <= offset; i++) {
			try {
				line = reader.readLine();
				// if eof then no match
				if (line == null) {
					return false;
				}
			} catch (IOException e) {
				// probably won't get here
				return false;
			}
		}
		if (line == null) {
			// may never get here
			return false;
		}
		PatternInfo patternInfo = (PatternInfo) testValue;
		Matcher matcher = patternInfo.pattern.matcher(line);
		if (matcher.matches()) {
			ExtractedValue extracted = (ExtractedValue) extractedValue;
			extracted.matched = matcher.group(1);
			return true;
		} else {
			return false;
		}
	}

	public void renderValue(StringBuilder sb, Object extractedValue, Formatter formatter) {
		ExtractedValue extracted = (ExtractedValue) extractedValue;
		sb.append(extracted.matched);
	}

	private boolean handleFlag(PatternInfo patternInfo, char flag) {
		if (flag == 'c') {
			patternInfo.patternFlags |= Pattern.CASE_INSENSITIVE;
			return true;
		} else if (flag == 's') {
			patternInfo.updateOffsetStart = true;
			return true;
		} else {
			return false;
		}
	}

	private static class ExtractedValue {
		String matched;
	}

	private static class PatternInfo {
		int patternFlags;
		// TODO: need to implement this flag
		@SuppressWarnings("unused")
		boolean updateOffsetStart;
		Pattern pattern;
	}
}
