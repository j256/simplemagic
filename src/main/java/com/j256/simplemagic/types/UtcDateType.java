package com.j256.simplemagic.types;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.j256.simplemagic.endian.EndianType;

/**
 * A 4-byte value interpreted as a UNIX date in UTC timezone.
 * 
 * @author graywatson
 */
public class UtcDateType extends LocalDateType {

	private static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("UTC");

	public UtcDateType(EndianType endianType) {
		super(endianType);
	}

	@Override
	protected Date dateFromExtractedValue(long val) {
		val *= 1000;
		Calendar calendar = Calendar.getInstance(UTC_TIME_ZONE);
		calendar.setTimeInMillis(val);
		return calendar.getTime();
	}
}
