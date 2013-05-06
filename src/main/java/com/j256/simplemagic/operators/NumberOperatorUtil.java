package com.j256.simplemagic.operators;

/**
 * Some commonly used operations on integers.
 * 
 * @author graywatson
 */
public class NumberOperatorUtil {

	/**
	 * Preprocess the test string into an operator and a value class.
	 */
	public static NumberTest convertTestString(String test) {
		TestOperator operator = TestOperator.fromTest(test);
		if (operator == null) {
			operator = TestOperator.DEFAULT_OPERATOR;
		} else {
			test = test.substring(1);
		}
		long val;
		try {
			val = Long.decode(test);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Could not parse long from: '" + test + "'");
		}
		return new NumberTest(operator, val);
	}

	/**
	 * Return true if the extractedValue compared to the testValue matches.
	 */
	public static boolean isMatch(NumberTest testValue, Long andValue, boolean unsignedType, long extractedValue,
			int offset, byte[] bytes) {
		if (andValue != null) {
			extractedValue &= andValue;
		}
		boolean result = false;
		switch (testValue.operator) {
			case EQUALS :
				result = (extractedValue == testValue.value);
				break;
			case NOT_EQUALS :
				result = (extractedValue != testValue.value);
				break;
			case LESS_THAN :
				result = (extractedValue < testValue.value);
				break;
			case GREATER_THAN :
				result = (extractedValue > testValue.value);
				break;
			case AND_ALL_SET :
				result = ((extractedValue & testValue.value) == testValue.value);
				break;
			case AND_ALL_CLEARED :
				result = ((extractedValue & testValue.value) == 0);
				break;
			default :
				throw new IllegalStateException("Unknown operator: " + testValue.operator);
		}
		// if (result) {
		// System.out.println("true");
		// }
		return result;
	}

	/**
	 * Information about the particular test.
	 */
	public static class NumberTest {

		final TestOperator operator;
		final long value;

		private NumberTest(TestOperator operator, long value) {
			this.operator = operator;
			this.value = value;
		}
	}
}
