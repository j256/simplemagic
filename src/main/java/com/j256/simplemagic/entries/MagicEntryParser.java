package com.j256.simplemagic.entries;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.j256.simplemagic.ContentInfoUtil.ErrorCallBack;
import com.j256.simplemagic.endian.EndianConverter;
import com.j256.simplemagic.endian.EndianType;
import com.j256.simplemagic.entries.MagicEntry.OffsetInfo;

/**
 * Class which parses a line from the magic (5) format and produces a {@link MagicEntry} class.
 * 
 * @author graywatson
 */
public class MagicEntryParser {

	public static final String UNKNOWN_NAME = "unknown";
	// special lines, others are put into the extensionMap
	private static final String MIME_TYPE_LINE = "!:mime";
	private static final String OPTIONAL_LINE = "!:optional";

	private final static Pattern OFFSET_PATTERN =
			Pattern.compile("\\(([0-9a-fA-Fx]+)\\.?([bsilBSILm]?)([\\*\\+\\-]?)([0-9a-fA-Fx]*)\\)");

	/**
	 * Carries the endian converter, byte size, and id3 flag for a single offset type specifier character.
	 * Used by the lookup map below in place of a large switch block.
	 */
	private static class EndianSpec {
		final EndianConverter converter;
		final int size;
		final boolean isId3;

		EndianSpec(EndianConverter converter, int size, boolean isId3) {
			this.converter = converter;
			this.size = size;
			this.isId3 = isId3;
		}
	}

	/**
	 * Maps each offset type specifier character (from the magic(5) indirect offset syntax) to its
	 * endian converter, byte size, and id3 flag. Replaces the switch block in parseOffset().
	 *
	 * Lower-case letters are little-endian; upper-case letters are big-endian; 'm' is middle-endian.
	 * The character 'i'/'I' uses id3-length encoding (4 bytes, lower 7 bits of each byte).
	 */
	private static final Map<Character, EndianSpec> ENDIAN_SPEC_MAP = new HashMap<Character, EndianSpec>();
	static {
		// little-endian specifiers (endian doesn't really matter for 1-byte 'b')
		ENDIAN_SPEC_MAP.put('b', new EndianSpec(EndianType.LITTLE.getConverter(), 1, false));
		ENDIAN_SPEC_MAP.put('s', new EndianSpec(EndianType.LITTLE.getConverter(), 2, false));
		ENDIAN_SPEC_MAP.put('i', new EndianSpec(EndianType.LITTLE.getConverter(), 4, true));
		ENDIAN_SPEC_MAP.put('l', new EndianSpec(EndianType.LITTLE.getConverter(), 4, false));
		// big-endian specifiers (endian doesn't really matter for 1-byte 'B')
		ENDIAN_SPEC_MAP.put('B', new EndianSpec(EndianType.BIG.getConverter(), 1, false));
		ENDIAN_SPEC_MAP.put('S', new EndianSpec(EndianType.BIG.getConverter(), 2, false));
		ENDIAN_SPEC_MAP.put('I', new EndianSpec(EndianType.BIG.getConverter(), 4, true));
		ENDIAN_SPEC_MAP.put('L', new EndianSpec(EndianType.BIG.getConverter(), 4, false));
		// middle-endian (PDP-11)
		ENDIAN_SPEC_MAP.put('m', new EndianSpec(EndianType.MIDDLE.getConverter(), 4, false));
	}

	/** Default endian spec used when no type specifier character is present in the offset expression. */
	private static final EndianSpec DEFAULT_ENDIAN_SPEC =
			new EndianSpec(EndianType.LITTLE.getConverter(), 4, false);
	/**
	 * Parse a line from the magic configuration file into an entry.
	 */
	public static MagicEntry parseLine(MagicEntry previous, String line, ErrorCallBack errorCallBack) {
		if (line.startsWith("!:")) {
			if (previous != null) {
				handleSpecial(previous, line, errorCallBack);
			}
			return null;
		}

		String[] parts = splitLine(line, errorCallBack);
		if (parts == null) {
			return null;
		}

		// ── ① level + offset ─────────────────────────────────────────────
		int sindex = parts[0].lastIndexOf('>');
		int level;
		String offsetString;
		if (sindex < 0) {
			level = 0;
			offsetString = parts[0];
		} else {
			level = sindex + 1;
			offsetString = parts[0].substring(sindex + 1);
		}

		int offset;
		OffsetInfo offsetInfo;
		boolean addOffset = false;
		String work = offsetString;

		if (work.isEmpty()) {
			if (errorCallBack != null) {
				errorCallBack.error(line, "invalid offset number:" + offsetString, null);
			}
			return null;
		}
		if (work.charAt(0) == '&') {
			if (work.length() == 1) {
				if (errorCallBack != null) {
					errorCallBack.error(line, "invalid offset number:" + offsetString, null);
				}
				return null;
			}
			addOffset = true;
			work = work.substring(1);
		}
		if (work.charAt(0) == '(') {
			offset = -1;
			offsetInfo = parseOffset(work, line, errorCallBack);
			if (offsetInfo == null) {
				return null;
			}
		} else {
			try {
				offset = Integer.decode(work);
				offsetInfo = null;
			} catch (NumberFormatException e) {
				if (errorCallBack != null) {
					errorCallBack.error(line, "invalid offset number:" + offsetString, e);
				}
				return null;
			}
		}

		// ── ② type string (AND value + matcher) ──────────────────────────
		String typeStr = parts[1];
		Long andValue = null;
		int andIndex = typeStr.indexOf('&');
		if (andIndex >= 0) {
			String andStr = typeStr.substring(andIndex + 1);
			try {
				andValue = Long.decode(andStr);
			} catch (NumberFormatException e) {
				if (errorCallBack != null) {
					errorCallBack.error(line, "invalid type AND-number: " + andStr, e);
				}
				return null;
			}
			typeStr = typeStr.substring(0, andIndex);
		}
		if (typeStr.isEmpty()) {
			if (errorCallBack != null) {
				errorCallBack.error(line, "blank type string", null);
			}
			return null;
		}

		boolean unsignedType = false;
		MagicMatcher matcher = MagicType.matcherfromString(typeStr);
		if (matcher == null) {
			if (typeStr.charAt(0) == 'u') {
				matcher = MagicType.matcherfromString(typeStr.substring(1));
				unsignedType = true;
			} else {
				int slashIndex = typeStr.indexOf('/');
				if (slashIndex > 0) {
					matcher = MagicType.matcherfromString(typeStr.substring(0, slashIndex));
				}
			}
			if (matcher == null) {
				if (errorCallBack != null) {
					errorCallBack.error(line, "unknown magic type string: " + typeStr, null);
				}
				return null;
			}
		}

		// ── ③ test value ─────────────────────────────────────────────────
		String testStr = parts[2];
		Object testValue;
		if (testStr.equals("x")) {
			testValue = null;
		} else {
			try {
				testValue = matcher.convertTestString(typeStr, testStr);
			} catch (Exception e) {
				if (errorCallBack != null) {
					errorCallBack.error(line, "could not convert magic test string: " + testStr, e);
				}
				return null;
			}
		}

		// ── ④ format string + name ────────────────────────────────────────
		MagicFormatter formatter;
		String name;
		boolean formatSpacePrefix = true;
		boolean clearFormat = false;

		if (parts.length == 3) {
			formatter = null;
			name = UNKNOWN_NAME;
		} else {
			String format = parts[3];
			if (format.startsWith("\\b")) {
				format = format.substring(2);
				formatSpacePrefix = false;
			} else if (format.startsWith("\010")) {
				format = format.substring(1);
				formatSpacePrefix = false;
			} else if (format.startsWith("\\r")) {
				format = format.substring(2);
				clearFormat = true;
			}
			formatter = new MagicFormatter(format);

			String trimmedFormat = format.trim();
			int spaceIndex = trimmedFormat.indexOf(' ');
			if (spaceIndex < 0) {
				spaceIndex = trimmedFormat.indexOf('\t');
			}
			if (spaceIndex > 0) {
				name = trimmedFormat.substring(0, spaceIndex);
			} else if (trimmedFormat.isEmpty()) {
				name = UNKNOWN_NAME;
			} else {
				name = trimmedFormat;
			}
		}

		return new MagicEntry(name, level, addOffset, offset, offsetInfo, matcher, andValue, unsignedType,
				testValue, formatSpacePrefix, clearFormat, formatter);
	}

	private static String[] splitLine(String line, ErrorCallBack errorCallBack) {
		// skip opening whitespace if any
		int startPos = findNonWhitespace(line, 0);
		if (startPos < 0) {
			return null;
		}

		// find the level info
		int endPos = findWhitespaceWithoutEscape(line, startPos);
		if (endPos < 0) {
			if (errorCallBack != null) {
				errorCallBack.error(line, "invalid number of whitespace separated fields, must be >= 4", null);
			}
			return null;
		}
		String levelStr = line.substring(startPos, endPos);

		// skip whitespace
		startPos = findNonWhitespace(line, endPos + 1);
		if (startPos < 0) {
			if (errorCallBack != null) {
				errorCallBack.error(line, "invalid number of whitespace separated fields, must be >= 4", null);
			}
			return null;
		}
		// find the type string
		endPos = findWhitespaceWithoutEscape(line, startPos);
		if (endPos < 0) {
			if (errorCallBack != null) {
				errorCallBack.error(line, "invalid number of whitespace separated fields, must be >= 4", null);
			}
			return null;
		}
		String typeStr = line.substring(startPos, endPos);

		// skip whitespace
		startPos = findNonWhitespace(line, endPos + 1);
		if (startPos < 0) {
			if (errorCallBack != null) {
				errorCallBack.error(line, "invalid number of whitespace separated fields, must be >= 4", null);
			}
			return null;
		}
		// find the test string
		endPos = findWhitespaceWithoutEscape(line, startPos);
		if (endPos < 0) {
			endPos = line.length();
		}
		String testStr = line.substring(startPos, endPos);

		// skip any whitespace
		startPos = findNonWhitespace(line, endPos + 1);
		// format is optional, this could return length
		if (startPos < 0) {
			return new String[] { levelStr, typeStr, testStr };
		} else {
			// format is the rest of the line
			return new String[] { levelStr, typeStr, testStr, line.substring(startPos) };
		}
	}

	private static void handleSpecial(MagicEntry previous, String line, ErrorCallBack errorCallBack) {
		if (line.equals(OPTIONAL_LINE)) {
			previous.setOptional(true);
			return;
		}
		int startPos = findNonWhitespace(line, 0);
		int index = findWhitespaceWithoutEscape(line, startPos);
		if (index < 0) {
			if (errorCallBack != null) {
				errorCallBack.error(line, "invalid extension line has less than 2 whitespace separated fields", null);
			}
			return;
		}
		String key = line.substring(startPos, index);
		startPos = findNonWhitespace(line, index);
		if (startPos < 0) {
			if (errorCallBack != null) {
				errorCallBack.error(line, "invalid extension line has less than 2 whitespace separated fields", null);
			}
			return;
		}
		// find whitespace after value, if any
		index = findWhitespaceWithoutEscape(line, startPos);
		if (index < 0) {
			index = line.length();
		}
		String value = line.substring(startPos, index);

		if (key.equals(MIME_TYPE_LINE)) {
			previous.setMimeType(value);
		} else {
		if (errorCallBack != null) {
			errorCallBack.error(line, "unknown special extension key: " + key, null);
		}
	}
	}

	private static int findNonWhitespace(String line, int startPos) {
		for (int pos = startPos; pos < line.length(); pos++) {
			if (!Character.isWhitespace(line.charAt(pos))) {
				return pos;
			}
		}
		return -1;
	}

	private static int findWhitespaceWithoutEscape(String line, int startPos) {
		boolean lastEscape = false;
		for (int pos = startPos; pos < line.length(); pos++) {
			char ch = line.charAt(pos);
			if (ch == ' ') {
				if (!lastEscape) {
					return pos;
				}
				lastEscape = false;
			} else if (Character.isWhitespace(line.charAt(pos))) {
				return pos;
			} else lastEscape = ch == '\\';
		}
		return -1;
	}

	/**
	 * Copied from the magic(5) man page:
	 * 
	 * <p>
	 * Offsets do not need to be constant, but can also be read from the file being examined. If the first character
	 * following the last '>' is a '(' then the string after the parenthesis is interpreted as an indirect offset. That
	 * means that the number after the parenthesis is used as an offset in the file. The value at that offset is read,
	 * and is used again as an offset in the file. Indirect offsets are of the form: ((x[.[bsilBSILm]][+-]y). The value
	 * of x is used as an offset in the file. A byte, id3 length, short or long is read at that offset depending on the
	 * [bislBISLm] type specifier. The capitalized types interpret the number as a big-endian value, whereas the small
	 * letter versions interpret the number as a little-endian value; the 'm' type interprets the number as a
	 * middle-endian (PDP-11) value. To that number the value of y is added and the result is used as an offset in the
	 * file. The default type if one is not specified is 4-byte long.
	 * </p>
	 */
	private static OffsetInfo parseOffset(String offsetString, String line, ErrorCallBack errorCallBack) {
		// (9.b+19)
		// (0x3c.l)
		// (8.s*16)
		Matcher matcher = OFFSET_PATTERN.matcher(offsetString);
		if (!matcher.matches()) {
			if (errorCallBack != null) {
				errorCallBack.error(line, "invalid offset pattern: " + offsetString, null);
			}
			return null;
		}
		int offset;
		try {
			offset = Integer.decode(matcher.group(1));
		} catch (NumberFormatException e) {
			if (errorCallBack != null) {
				errorCallBack.error(line, "invalid long offset number: " + offsetString, e);
			}
			return null;
		}
		if (matcher.group(2) == null) {
			if (errorCallBack != null) {
				errorCallBack.error(line, "invalid long offset type: " + offsetString, null);
			}
			return null;
		}
		// resolve endian converter, size, and id3 flag from the type specifier character via lookup map
		EndianSpec spec;
		if (matcher.group(2).length() == 1) {
			char ch = matcher.group(2).charAt(0);
			// look up the specifier; fall back to default (little-endian 4-byte) if not found
			spec = ENDIAN_SPEC_MAP.get(ch);
			if (spec == null) {
				spec = DEFAULT_ENDIAN_SPEC;
			}
		} else {
			// no type specifier present — use the default (little-endian 4-byte long)
			spec = DEFAULT_ENDIAN_SPEC;
		}
		EndianConverter converter = spec.converter;
		boolean isId3 = spec.isId3;
		int size = spec.size;
		int add = 0;
		// the +# section is optional
		if (matcher.group(4) != null && !matcher.group(4).isEmpty()) {
			try {
				add = Integer.decode(matcher.group(4));
			} catch (NumberFormatException e) {
				if (errorCallBack != null) {
					errorCallBack.error(line, "invalid long add value: " + matcher.group(4), e);
				}
				return null;
			}
			// decode doesn't work with leading '+', grumble
			String offsetOperator = matcher.group(3);
			if ("-".equals(offsetOperator)) {
				/*
				 * From manual: An offset operator of the form (l-R) specifies an offset that is calculated by
				 * subtracting the value R from the value of memory location specified by l.
				 */
				add = -add;
			} else if ("*".equals(offsetOperator)) {
				/*
				 * From manual: '*' offset operator specifies that the value located at the memory location following
				 * the operator be used as the offset. Thus, *0x3C indicates that the value contained in 0x3C should be
				 * used as the offset.
				 */
				offset = add;
				add = 0;
			} else {
				/*
				 * From manual: The + offset operator specifies an incremental offset, based upon the value of the last
				 * offset. Thus, +15 indicates that the offset value is 15 bytes from the last specified offset.
				 * 
				 * From manual: An offset operator of the form (l+R) specifies an offset that is the total of the value
				 * of memory location specified by l and the value R.
				 */
			}
		}
		return new OffsetInfo(offset, converter, isId3, size, add);
	}
}
