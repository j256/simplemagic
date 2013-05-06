package com.j256.simplemagic.types;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import com.j256.simplemagic.endian.EndianType;
import com.j256.simplemagic.entries.Formatter;

/**
 * A four-byte value interpreted as a UNIX date.
 * 
 * @author graywatson
 */
public class UtcDateType extends IntegerType {

	private static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("UTC");

	private final ThreadLocal<SimpleDateFormat> dateFormat = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("");
		}
	};

	public UtcDateType(EndianType endianType) {
		super(endianType);
	}

	@Override
	public void renderValue(StringBuilder sb, Object extractedValue, Formatter formatter) {
		long val = (Integer) extractedValue;
		val *= 1000;
		Calendar calendar = Calendar.getInstance(UTC_TIME_ZONE);
		calendar.setTimeInMillis(val);
		sb.append(dateFormat.get().format(calendar));
	}
}
