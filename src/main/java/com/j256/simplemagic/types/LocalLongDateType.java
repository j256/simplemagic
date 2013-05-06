package com.j256.simplemagic.types;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.j256.simplemagic.endian.EndianType;
import com.j256.simplemagic.entries.Formatter;

/**
 * An eight-byte value interpreted as a UNIX-style date, but interpreted as local time rather than UTC.
 * 
 * @author graywatson
 */
public class LocalLongDateType extends LongType {

	private final ThreadLocal<SimpleDateFormat> dateFormat = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("");
		}
	};

	public LocalLongDateType(EndianType endianType) {
		super(endianType);
	}

	@Override
	public void renderValue(StringBuilder sb, Object extractedValue, Formatter formatter) {
		long val = (Long) extractedValue;
		// XXX: is this in millis or seconds?
		// val *= 1000;
		// local timezone
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(val);
		sb.append(dateFormat.get().format(calendar));
	}
}
