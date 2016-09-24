package com.j256.simplemagic.types;

/**
 * Internal class that provides information about a particular test.
 */
public class NumberOperator {

	private final NumberType numberType;
	private final TestOperator operator;
	private final long value;

	/**
	 * Pre-process the test string into an operator and a value class.
	 */
	public NumberOperator(NumberType numberType, String test) {
		this.numberType = numberType;
		TestOperator op = TestOperator.fromTest(test);
		if (op == null) {
			op = TestOperator.DEFAULT_OPERATOR;
		} else {
			test = test.substring(1).trim();
		}
		this.operator = op;
		try {
			this.value = Long.decode(test);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Could not parse long from: '" + test + "'");
		}
	}

	public boolean isMatch(Long andValue, boolean unsignedType, long extractedValue) {
		if (andValue != null) {
			extractedValue &= andValue;
		}
		return operator.doTest(unsignedType, extractedValue, value, numberType);
	}

	public long getValue() {
		return value;
	}

	@Override
	public String toString() {
		return operator + ", value " + value;
	}

	/**
	 * Operators for tests. If no operator character then equals is assumed.
	 */
	private enum TestOperator {

		EQUALS('=') {
			@Override
			public boolean doTest(boolean unsignedType, long extractedValue, long testValue, NumberType numberType) {
				if (unsignedType) {
					return (extractedValue == testValue);
				} else {
					return (numberType.compare(extractedValue, testValue) == 0);
				}
			}
		},
		NOT_EQUALS('!') {
			@Override
			public boolean doTest(boolean unsignedType, long extractedValue, long testValue, NumberType numberType) {
				if (unsignedType) {
					return (extractedValue != testValue);
				} else {
					return (numberType.compare(extractedValue, testValue) != 0);
				}
			}
		},
		GREATER_THAN('>') {
			@Override
			public boolean doTest(boolean unsignedType, long extractedValue, long testValue, NumberType numberType) {
				if (unsignedType) {
					return (extractedValue > testValue);
				} else {
					return (numberType.compare(extractedValue, testValue) > 0);
				}
			}
		},
		LESS_THAN('<') {
			@Override
			public boolean doTest(boolean unsignedType, long extractedValue, long testValue, NumberType numberType) {
				if (unsignedType) {
					return (extractedValue < testValue);
				} else {
					return (numberType.compare(extractedValue, testValue) < 0);
				}
			}
		},
		AND_ALL_SET('&') {
			@Override
			public boolean doTest(boolean unsignedType, long extractedValue, long testValue, NumberType numberType) {
				return ((extractedValue & testValue) == testValue);
			}
		},
		AND_ALL_CLEARED('^') {
			@Override
			public boolean doTest(boolean unsignedType, long extractedValue, long testValue, NumberType numberType) {
				return ((extractedValue & testValue) == 0);
			}
		},
		NEGATE('~') {
			@Override
			public boolean doTest(boolean unsignedType, long extractedValue, long testValue, NumberType numberType) {
				// we need the mask because we are using bit negation but testing only a portion of the long
				long negatedValue = numberType.maskValue(~testValue);
				return (extractedValue == negatedValue);
			}
		},
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

		public abstract boolean doTest(boolean unsignedType, long extractedValue, long testValue,
				NumberType numberType);

		/**
		 * Returns the operator if the first character is an operator. Otherwise this returns null and you should use
		 * the {@link #DEFAULT_OPERATOR}.
		 * 
		 * <p>
		 * <b>NOTE:</b> We _don't_ return the default operator here because the caller needs to know if the prefix was
		 * supplied or not.
		 * </p>
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
}
