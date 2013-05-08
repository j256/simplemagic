package com.j256.simplemagic.types;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.j256.simplemagic.endian.EndianType;

/**
 * A 8-byte value interpreted as a UNIX date in UTC timezone.
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
		return new Date(val);
	}

	@Override
	protected void assisgnTimeZone(SimpleDateFormat format) {
		format.setTimeZone(UTC_TIME_ZONE);
	}
}
