package com.j256.simplemagic.entries;

import java.util.HashMap;
import java.util.Map;

import com.j256.simplemagic.endian.EndianType;
import com.j256.simplemagic.types.BigEndianString16Type;
import com.j256.simplemagic.types.ByteType;
import com.j256.simplemagic.types.DefaultType;
import com.j256.simplemagic.types.DoubleType;
import com.j256.simplemagic.types.FloatType;
import com.j256.simplemagic.types.IntegerType;
import com.j256.simplemagic.types.LittleEndianString16Type;
import com.j256.simplemagic.types.LocalDateType;
import com.j256.simplemagic.types.LocalLongDateType;
import com.j256.simplemagic.types.LongType;
import com.j256.simplemagic.types.PStringType;
import com.j256.simplemagic.types.RegexType;
import com.j256.simplemagic.types.SearchType;
import com.j256.simplemagic.types.ShortType;
import com.j256.simplemagic.types.StringType;

/**
 * The various types which correspond to the "type" part of the magic (5) format.
 * 
 * @author graywatson
 */
public enum MagicType {

	BYTE("byte", new ByteType()),
	SHORT("short", new ShortType(EndianType.NATIVE)),
	// yes this is long in C magic lang (shudder)
	INTEGER("long", new IntegerType(EndianType.NATIVE)),
	QUAD("quad", new LongType(EndianType.NATIVE)),
	FLOAT("float", new FloatType(EndianType.NATIVE)),
	DOUBLE("double", new DoubleType(EndianType.NATIVE)),
	STRING("string", new StringType()),
	PSTRING("pstring", new PStringType()),
	DATE("date", new LocalDateType(EndianType.NATIVE)),
	LONG_DATE("qdate", new LocalLongDateType(EndianType.NATIVE)),
	LOCAL_DATE("ldate", new LocalDateType(EndianType.NATIVE)),
	LONG_LOCAL_DATE("qldate", new LocalDateType(EndianType.NATIVE)),

	// beid3
	BIG_ENDIAN_SHORT("beshort", new ShortType(EndianType.BIG)),
	// yes this is long in C long so 4 bytes (shudder)
	BIG_ENDIAN_INTEGER("belong", new IntegerType(EndianType.BIG)),
	BIG_ENDIAN_QUAD("bequad", new LongType(EndianType.BIG)),
	BIG_ENDIAN_FLOAT("befloat", new FloatType(EndianType.BIG)),
	BIG_ENDIAN_DOUBLE("bedouble", new DoubleType(EndianType.BIG)),
	BIG_ENDIAN_DATE("bedate", new LocalDateType(EndianType.BIG)),
	BIG_ENDIAN_LONG_DATE("beqdate", new LocalLongDateType(EndianType.BIG)),
	BIG_ENDIAN_LOCAL_DATE("beldate", new LocalDateType(EndianType.BIG)),
	BIG_ENDIAN_LONG_LOCAL_DATE("beqldate", new LocalLongDateType(EndianType.BIG)),
	BIG_ENDIAN_TWO_BYTE_STRING("bestring16", new BigEndianString16Type()),

	// leid3
	LITTLE_ENDIAN_SHORT("leshort", new ShortType(EndianType.LITTLE)),
	// yes this is long in C long so 4 bytes (shudder)
	LITTLE_ENDIAN_INTEGER("lelong", new IntegerType(EndianType.LITTLE)),
	LITTLE_ENDIAN_QUAD("lequad", new LongType(EndianType.LITTLE)),
	LITTLE_ENDIAN_FLOAT("lefloat", new FloatType(EndianType.LITTLE)),
	LITTLE_ENDIAN_DOUBLE("ledouble", new DoubleType(EndianType.LITTLE)),
	LITTLE_ENDIAN_DATE("ledate", new LocalDateType(EndianType.LITTLE)),
	LITTLE_ENDIAN_LONG_DATE("leqdate", new LocalLongDateType(EndianType.LITTLE)),
	LITTLE_ENDIAN_LOCAL_DATE("leldate", new LocalDateType(EndianType.LITTLE)),
	LITTLE_ENDIAN_LONG_LOCAL_DATE("leqldate", new LocalLongDateType(EndianType.LITTLE)),
	LITTLE_ENDIAN_TWO_BYTE_STRING("lestring16", new LittleEndianString16Type()),

	// indirect -- special
	REGEX("regex", new RegexType()),
	SEARCH("search", new SearchType()),

	// yes this is long in C magic lang (shudder)
	MIDDLE_ENDIAN_INTEGER("melong", new IntegerType(EndianType.MIDDLE)),
	MIDDLE_ENDIAN_DATE("medate", new LocalDateType(EndianType.MIDDLE)),
	MIDDLE_ENDIAN_LOCAL_DATE("meldate", new LocalDateType(EndianType.MIDDLE)),

	DEFAULT("default", new DefaultType()),
	// end
	;

	private final String name;
	private final MagicMatcher matcher;
	private static final Map<String, MagicMatcher> typeMap = new HashMap<String, MagicMatcher>();

	static {
		for (MagicType type : values()) {
			typeMap.put(type.name, type.matcher);
		}
	}

	private MagicType(String name, MagicMatcher matcher) {
		this.name = name;
		this.matcher = matcher;
	}

	/**
	 * Find the associated matcher to the string.
	 */
	public static MagicMatcher matcherfromString(String typeString) {
		MagicMatcher matcher = typeMap.get(typeString);
		if (matcher == null) {
			return null;
		} else {
			return matcher;
		}
	}
}
