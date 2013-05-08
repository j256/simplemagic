package com.j256.simplemagic.entries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.j256.simplemagic.ContentType;

/**
 * Representation of a line of information from the magic (5) format.
 * 
 * @author graywatson
 */
public class MagicEntry {

	private static final String UNKNOWN_NAME = "unknown";
	// special lines, others are put into the extensionMap
	private static final String MIME_TYPE_LINE = "!:mime";
	private static final String STRENGTH_LINE = "!:stength";

	private List<MagicEntry> children;

	private final MagicEntry parent;
	private final String name;
	private final int level;
	private final int offset;
	private final MagicMatcher matcher;
	private final Long andValue;
	private final boolean unsignedType;
	// the testValue object is defined by the particular matcher
	private final Object testValue;
	private final boolean formatSpacePrefix;
	private final Formatter formatter;

	private int strength;
	private String mimeType;
	private Map<String, String> extensionMap;

	private MagicEntry(MagicEntry parent, String name, int level, int offset, MagicMatcher matcher, Long andValue,
			boolean unsignedType, Object testValue, boolean formatSpacePrefix, String format) {
		this.parent = parent;
		this.name = name;
		this.level = level;
		this.offset = offset;
		this.matcher = matcher;
		this.andValue = andValue;
		this.unsignedType = unsignedType;
		this.testValue = testValue;
		this.formatSpacePrefix = formatSpacePrefix;
		if (format == null) {
			this.formatter = null;
		} else {
			this.formatter = new Formatter(format);
		}
		this.strength = 1;
	}

	/**
	 * Parse a line from the magic configuration file into an entry.
	 */
	public static MagicEntry parseLine(MagicEntry previous, String line) {
		if (line.startsWith("!:")) {
			if (previous != null) {
				// we ignore it if there is no previous entry to add it to
				handleSpecial(previous, line);
			}
			return null;
		}

		// 0[ ]string[ ]%PDF-[ ]PDF document
		// !:mime[ ]application/pdf
		// >5[ ]byte[ ]x[ ]\b, version %c
		// >7[ ]byte[ ]x[ ]\b.%c

		// unfortunately, tab is not a reliable line splitter, even though patterns and formats have spaces (sigh)

		// either a tab and other spaces or two spaces in a row -- we hope that other spaces a escaped with \
		String[] parts = line.split("\\t\\s*|\\s\\s+", 4);
		if (parts.length < 3) {
			throw new IllegalArgumentException("Invalid magic line: " + line);
		}

		// level and offset
		int level;
		int sindex = parts[0].lastIndexOf('>');
		String offsetString;
		if (sindex < 0) {
			level = 0;
			offsetString = parts[0];
		} else {
			level = sindex + 1;
			offsetString = parts[0].substring(sindex + 1);
		}
		int offset;
		try {
			offset = Integer.decode(offsetString);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid offset number: " + line);
		}

		// type
		String typeStr = parts[1];
		sindex = typeStr.indexOf('&');
		// we use long because of overlaps
		Long andValue = null;
		if (sindex >= 0) {
			try {
				andValue = Long.decode(typeStr.substring(sindex + 1));
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Invalid type and-number: " + line);
			}
			typeStr = typeStr.substring(0, sindex);
		}
		if (typeStr.length() == 0) {
			throw new IllegalArgumentException("Blank type string: " + line);
		}

		boolean unsignedType = false;
		MagicMatcher matcher = MagicType.matcherfromString(typeStr);
		if (matcher == null) {
			if (typeStr.charAt(0) == 'u') {
				matcher = MagicType.matcherfromString(typeStr.substring(1));
				unsignedType = true;
			} else {
				int index = typeStr.indexOf('/');
				if (index > 0) {
					matcher = MagicType.matcherfromString(typeStr.substring(0, index));
				}
			}
			if (matcher == null) {
				throw new IllegalArgumentException("Unknown magic type '" + typeStr + "' on line<: " + line);
			}
		}

		Object testValue;
		String testStr = parts[2];
		if (testStr.equals("x")) {
			testValue = null;
		} else {
			testValue = matcher.convertTestString(typeStr, testStr, offset);
		}

		String format;
		boolean formatSpacePrefix = true;
		if (parts.length == 3) {
			format = null;
		} else {
			format = parts[3];
			if (format.startsWith("\\b")) {
				format = format.substring(2);
				formatSpacePrefix = false;
			}
		}

		MagicEntry parent = null;
		if (level > 0) {
			if (previous == null) {
				// if no previous then we have to drop this
				return null;
			}
			// use the previous and then go up until the parent is a level lower than ours
			for (parent = previous; parent.level >= level; parent = parent.parent) {
				// none
			}
		}
		String name;
		if (format == null) {
			name = UNKNOWN_NAME;
		} else {
			parts = format.split("[ \t]");
			if (parts.length == 0) {
				name = UNKNOWN_NAME;
			} else {
				name = parts[0];
			}
		}
		MagicEntry entry =
				new MagicEntry(parent, name, level, offset, matcher, andValue, unsignedType, testValue,
						formatSpacePrefix, format);
		if (level > 0) {
			if (previous == null) {
				// if no previous then we have to drop this
				return null;
			}
			if (parent != null) {
				parent.addChild(entry);
			}
		}
		return entry;
	}
	/**
	 * Returns the content type associated with the bytes or null if it does not match.
	 */
	public ContentType processBytes(byte[] bytes) {
		ContentInfo info = processBytes(bytes, null);
		if (info == null || info.name == UNKNOWN_NAME) {
			return null;
		} else {
			return new ContentType(info.name, info.mimeType, info.sb.toString());
		}
	}

	/**
	 * Return the "level" of the rule. Level-0 rules start the matching process. Level-1 and above rules are processed
	 * only when the level-0 matches.
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Get the strength of the rule. Not well supported right now.
	 */
	public int getStrength() {
		return strength;
	}

	@Override
	public String toString() {
		return matcher.toString();
	}

	private void addChild(MagicEntry child) {
		if (children == null) {
			children = new ArrayList<MagicEntry>();
		}
		children.add(child);
	}

	private ContentInfo processBytes(byte[] bytes, ContentInfo contentInfo) {
		Object val = matcher.extractValueFromBytes(offset, bytes);
		if (val == null) {
			return null;
		}
		if (testValue != null) {
			val = matcher.isMatch(testValue, andValue, unsignedType, val, offset, bytes);
			if (val == null) {
				return null;
			}
		}

		if (contentInfo == null) {
			contentInfo = new ContentInfo(name, mimeType);
		}
		if (formatter != null) {
			// if we are appending and need a space then preprend one
			if (formatSpacePrefix && contentInfo.sb.length() > 0) {
				contentInfo.sb.append(' ');
			}
			matcher.renderValue(contentInfo.sb, val, formatter);
		}
		if (children != null) {
			/*
			 * If there are children then one of them has to match otherwise the whole thing doesn't match. This is
			 * necessary for formats that are XML but we don't want to dominate plain old XML documents.
			 */
			boolean matched = false;
			// we have to do this because the children's children set the name first otherwise
			boolean assignName = (contentInfo.name == UNKNOWN_NAME);
			for (MagicEntry child : children) {
				if (child.processBytes(bytes, contentInfo) != null) {
					matched = true;
					if (assignName && child.name != null && child.name != UNKNOWN_NAME) {
						contentInfo.name = child.name;
					}
					if (contentInfo.mimeType == null && child.mimeType != null) {
						contentInfo.mimeType = child.mimeType;
					}
				}
			}
			if (!matched) {
				return null;
			}
		}
		return contentInfo;
	}

	private static void handleSpecial(MagicEntry parent, String line) {
		String[] parts = line.split("[ \\t]+", 2);
		if (parts.length < 2) {
			throw new IllegalArgumentException("Invalid extension line: " + line);
		}
		String key = parts[0];
		if (key.equals(MIME_TYPE_LINE)) {
			parent.mimeType = parts[1];
			return;
		}
		if (key.equals(STRENGTH_LINE)) {
			handleStrength(parent, parts[1]);
			return;
		}
		key = key.substring(2);
		if (key.length() == 0) {
			throw new IllegalArgumentException("Blank extension key: " + line);
		}

		parent.addExtension(key, parts[1]);
	}

	private static void handleStrength(MagicEntry parent, String strengthValue) {
		String[] parts = strengthValue.split("[ \\t]+", 2);
		if (parts.length == 0) {
			throw new IllegalArgumentException("Invalid strength value: " + strengthValue);
		}
		if (parts[0].length() == 0) {
			throw new IllegalArgumentException("Invalid strength operator: " + strengthValue);
		}
		char operator = parts[0].charAt(0);
		String valueStr;
		if (parts.length == 1) {
			valueStr = parts[0].substring(1);
		} else {
			valueStr = parts[1];
		}
		int value;
		try {
			value = Integer.parseInt(valueStr);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid strength value: " + strengthValue);
		}

		int strength = parent.strength;
		switch (operator) {
			case '+' :
				strength += value;
				break;
			case '-' :
				strength -= value;
				break;
			case '*' :
				strength *= value;
				break;
			case '/' :
				strength /= value;
				break;
			default :
				throw new IllegalArgumentException("Invalid strength operator: " + strengthValue);
		}
		parent.strength = strength;
	}

	private void addExtension(String key, String value) {
		if (extensionMap == null) {
			extensionMap = new HashMap<String, String>();
		}
		extensionMap.put(key, value);
	}

	private static class ContentInfo {
		String name;
		String mimeType;
		final StringBuilder sb = new StringBuilder();
		private ContentInfo(String name, String mimeType) {
			this.name = name;
			this.mimeType = mimeType;
		}
	}
}
