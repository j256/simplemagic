package com.j256.simplemagic.operators;

/**
 * Operators for tests. If no operator character then equals is assumed.
 * 
 * @author graywatson
 */
public enum TestOperator {

	EQUALS('='),
	NOT_EQUALS('!'),
	GREATER_THAN('>'),
	LESS_THAN('<'),
	AND_ALL_SET('&'),
	AND_ALL_CLEARED('^'),
	NEGATE('~'),
	// end
	;

	/**
	 * Default operator which should be used if {@link #fromTest(String)} returns null;
	 */
	public static final TestOperator DEFAULT_OPERATOR = EQUALS;

	private final char prefixChar;

	private TestOperator(char prefixChar) {
		this.prefixChar = prefixChar;
	}

	public char getPrefixChar() {
		return prefixChar;
	}

	/**
	 * Returns the operator if the first character is an operator. Otherwise this returns null and you should use the
	 * {@link #DEFAULT_OPERATOR}.
	 */
	public static TestOperator fromTest(String testStr) {
		if (testStr.length() == 0) {
			return null;
		}
		char first = testStr.charAt(0);
		for (TestOperator operator : values()) {
			if (operator.prefixChar == first) {
				return operator;
			}
		}
		return null;
	}
}
