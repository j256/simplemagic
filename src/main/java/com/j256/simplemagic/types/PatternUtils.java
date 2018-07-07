package com.j256.simplemagic.types;

/**
 * Some common pattern utilities used by multiple types.
 * 
 * @author graywatson
 */
public class PatternUtils {

	/**
	 * Pre-processes the pattern by handling backslash escapes such as \b and \007.
	 */
	public static String preProcessPattern(String pattern) {
		int index = pattern.indexOf('\\');
		if (index < 0) {
			return pattern;
		}

		StringBuilder sb = new StringBuilder();
		for (int pos = 0; pos < pattern.length();) {
			char ch = pattern.charAt(pos);
			if (ch != '\\') {
				sb.append(ch);
				pos++;
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
					pos++;
					break;
				case 'f':
					sb.append('\f');
					pos++;
					break;
				case 'n':
					sb.append('\n');
					pos++;
					break;
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7': {
					// 1-3 octal characters: \1 \01 or \017
					pos += radixCharsToChar(sb, pattern, pos, 3, 8);
					break;
				}
				case 'r':
					sb.append('\r');
					pos++;
					break;
				case 't':
					sb.append('\t');
					pos++;
					break;
				case 'x': {
					// 1-2 hex characters: \xD or \xD9
					int adjust = radixCharsToChar(sb, pattern, pos + 1, 2, 16);
					if (adjust > 0) {
						// adjust by 1 for the x
						pos += 1 + adjust;
					} else {
						sb.append(ch);
						pos++;
					}
					break;
				}
				case ' ':
				case '\\':
				default:
					sb.append(ch);
					pos++;
					break;
			}
		}
		return sb.toString();
	}

	private static int radixCharsToChar(StringBuilder sb, String pattern, int pos, int maxLen, int radix) {
		int val = 0;
		int i = 0;
		for (; i < maxLen; i++) {
			if (pos + i >= pattern.length()) {
				break;
			}
			int digit = Character.digit(pattern.charAt(pos + i), radix);
			if (digit < 0) {
				break;
			}
			val = val * radix + digit;
		}
		if (i > 0) {
			sb.append((char) val);
		}
		return i;
	}
}
