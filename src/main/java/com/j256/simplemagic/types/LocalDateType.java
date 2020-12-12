package com.j256.simplemagic.types;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.j256.simplemagic.endian.EndianType;
import com.j256.simplemagic.entries.MagicFormatter;

/**
 * A 4-byte value interpreted as a UNIX-style date, but interpreted as local time rather than UTC.
 * 
 * @author graywatson
 */
public class LocalDateType extends IntegerType {

	public final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");

	public LocalDateType(EndianType endianType) {
		super(endianType);
	}

	@Override
	public void renderValue(StringBuilder sb, Object extractedValue, MagicFormatter formatter) {
		long val = (Long) extractedValue;
		Date date = dateFromExtractedValue(val);
		DateFormat format = (DateFormat) DATE_FORMAT.clone();
		assisgnTimeZone(format);
		formatter.format(sb, format.format(date));
	}

	protected Date dateFromExtractedValue(long val) {
		val *= 1000;
		return new Date(val);
	}

	protected void assisgnTimeZone(DateFormat format) {
		// noop for local time-zone
	}
}
