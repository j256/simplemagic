package com.j256.simplemagic.types;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * From the magic(5) man page: A literal string search starting at the given line offset. The same modifier flags can be
 * used as for string patterns. The modifier flags (if any) must be followed by /number the range, that is, the number
 * of positions at which the match will be attempted, starting from the start offset. This is suitable for searching
 * larger binary expressions with variable offsets, using \ escapes for special characters. The offset works as for
 * regex.
 * 
 * <p>
 * <b>NOTE:</b> in our experience, the /number is _before_ the flags in 99% of the lines so that is how we implemented
 * it.
 * </p>
 * 
 * @author graywatson
 */
public class SearchType extends StringType {

	private static final int MAX_NUM_LINES = 20;

	@Override
	public Object isMatch(Object testValue, Long andValue, boolean unsignedType, Object extractedValue, int offset,
			byte[] bytes) {
		if (offset > MAX_NUM_LINES) {
			return null;
		}
		StringTestInfo info = (StringTestInfo) testValue;
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
					String match = findOffsetMatch(info, i, null, line);
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
}
