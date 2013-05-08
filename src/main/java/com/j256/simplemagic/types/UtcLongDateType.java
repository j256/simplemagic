package com.j256.simplemagic.types;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.j256.simplemagic.endian.EndianType;

/**
 * A four-byte value interpreted as a UNIX date in UTC timezone.
 * 
 * @author graywatson
 */
public class UtcLongDateType extends LocalLongDateType {

	private static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("UTC");

	public UtcLongDateType(EndianType endianType) {
		super(endianType);
	}

	@Override
	protected Date dateFromExtractedValue(long val) {
		// XXX: is this in millis or seconds?
		// val *= 1000;
		Calendar calendar = Calendar.getInstance(UTC_TIME_ZONE);
		calendar.setTimeInMillis(val);
		return calendar.getTime();
	}
}
