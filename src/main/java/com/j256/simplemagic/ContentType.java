package com.j256.simplemagic;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumerated type of the content if it is known by SimpleMagic. This information is _not_ processed from the magic
 * files.
 * 
 * @author graywatson
 */
public enum ContentType {

	/** AIFF audio format */
	AIFF("audio/x-aiff", "aiff"),
	/** Apple Quicktime image */
	APPLE_QUICKTIME_IMAGE("image/x-quicktime", null),
	/** Apple Quicktime movie */
	APPLE_QUICKTIME_MOVIE("video/quicktime", "quicktime"),
	/** ARC archive data */
	ARC("application/x-arc", "arc"),
	/** MPEG audio file */
	AUDIO_MPEG("audio/mpeg", "mpeg"),
	/** Microsoft AVI video file */
	AVI("video/x-msvideo", "avi"),
	/** Unix AWK command script */
	AWK("text/x-awk", "awk"),
	/** Macintosh BinHex file */
	BINHEX("application/mac-binhex40", "binhex"),
	/** Bittorrent file */
	BITTORRENT("application/x-bittorrent", "bittorrent"),
	/** Microsoft PC bitmap image */
	BMP("image/x-ms-bmp", "bmp"),
	/** Bzip2 compressed file */
	BZIP2("application/x-bzip2", "bzip2"),
	/** Unix compress file */
	COMPRESS("application/x-compress", "compress"),
	/** Corel Draw image file */
	COREL_DRAW("image/x-coreldraw", "corel-draw"),
	/** Unix core dump output */
	CORE_DUMP("application/x-coredump", "core-dump"),
	/** Unix CPIO archive data */
	CPIO("application/x-cpio", "cpio"),
	/** Berkeley database file */
	DBM("application/x-dbm", "dbm"),
	/** Debian installation package */
	DEBIAN_PACKAGE("application/x-debian-package", "pkg"),
	/** Unix diff output */
	DIFF("text/x-diff", "diff"),
	/** TeX DVI output file */
	DVI("application/x-dvi", "dvi"),
	/** Macromedia Flash data */
	FLASH("application/x-shockwave-flash", "flash"),
	/** Macromedia Flash movie file */
	FLASH_VIDEO("video/x-flv", "flash-video"),
	/** FORTRAN program */
	FORTRAN("text/x-fortran", "fortran"),
	/** FrameMaker document */
	FRAMEMAKER("application/x-mif", "framemaker"),
	/** GNU awk script */
	GAWK("text/x-gawk", "gawk"),
	/** GNU database file */
	GDBM("application/x-gdbm", "gdbm"),
	/** GIF image file */
	GIF("image/gif", "gif"),
	/** GNU Numeric file */
	GNUMERIC("application/x-gnumeric", "gnumeric"),
	/** GPG keyring file */
	GNUPG_KEYRING("application/x-gnupg-keyring", "gnupg-keyring"),
	/** GNU Info file */
	GNU_INFO("text/x-info", "gnu-info"),
	/** Gzip compressed data */
	GZIP("application/x-gzip", "gzip"),
	/** H264 video encoded file */
	H264("video/h264", "h264"),
	/** HTML document */
	HTML("text/html", "html"),
	/** MS Windows icon resource */
	ICO("image/x-ico", "ico"),
	/** ISO 9660 CD-ROM filesystem data */
	ISO_9660("application/x-iso9660-image", "iso9660"),
	/** Java applet */
	JAVA_APPLET("application/x-java-applet", "applet"),
	/** Java keystore file */
	JAVA_KEYSTORE("application/x-java-keystore", "java-keystore"),
	/** JPEG image */
	JPEG("image/jpeg", "jpeg"),
	/** JPEG 2000 image */
	JPEG_2000("image/jp2", "jp2"),
	/** LHA archive data */
	LHA("application/x-lha", "lha"),
	/** Lisp program */
	LISP("text/x-lisp", "lisp"),
	/** Lotus 123 spreadsheet */
	LOTUS_123("application/x-123", "lotus-123"),
	/** Microsoft access database */
	MICROSOFT_ACCESS("application/x-msaccess", "access"),
	/** Microsoft excel spreadsheet */
	MICROSOFT_EXCEL("application/vnd.ms-excel", "excel"),
	/** Microsoft word document */
	MICROSOFT_WORD("application/msword", "word"),
	/** MIDI audio */
	MIDI("audio/midi", "midi"),
	/** MNG video */
	MNG("video/x-mng", "mng"),
	/** MP4 encoded video */
	MP4("video/mp4", "mp4"),
	/** MP4V encoded video */
	MP4V("video/mp4v-es", "mp4v"),
	/** New Awk script */
	NAWK("text/x-nawk", "nawk"),
	/** Network news message */
	NEWS("message/news", "news"),
	/** OGG file container */
	OGG("application/ogg", "ogg"),
	/** PBM image */
	PBM("image/x-portable-bitmap", "pbm"),
	/** PDF document */
	PDF("application/pdf", "pdf"),
	/** Perl script */
	PERL("text/x-perl", "perl"),
	/** PGM image */
	PGM("image/x-portable-greymap", "pgm"),
	/** PGP encrypted message */
	PGP("text/PGP", "pgp"),
	/** PGP keyring */
	PGP_KEYRING("application/x-pgp-keyring", "pgp-keyring"),
	/** PGP signature */
	PGP_SIGNATURE("application/pgp-signature", "pgp-signature"),
	/** Photoshop image */
	PHOTOSHOP("image/vnd.adobe.photoshop", "photoshop"),
	/** PHP script */
	PHP("text/x-php", "php"),
	/** PNG image */
	PNG("image/png", "png"),
	/** Postscript file */
	POSTSCRIPT("application/postscript", "postscript"),
	/** PPM image */
	PPM("image/x-portable-pixmap", "ppm"),
	/** RAR archive data */
	RAR("application/x-rar", "rar"),
	/** Real-audio file */
	REAL_AUDIO("audio/x-pn-realaudio", "real-audio"),
	/** Real-media file */
	REAL_MEDIA("application/vnd.rn-realmedia", "real-media"),
	/** RFC822 news message */
	RFC822("message/rfc822", "rfc822"),
	/** RedHat package file */
	RPM("application/x-rpm", "rpm"),
	/** Rich text format document */
	RTF("text/rtf", "rtf"),
	/** Shared library file */
	SHARED_LIBRARY("application/x-sharedlib", "shared-lib"),
	/** Unix shell script */
	SHELL_SCRIPT("text/x-shellscript", "shell-script"),
	/** Mac Stuffit archive data */
	STUFFIT("application/x-stuffit", "stuffit"),
	/** SVG image */
	SVG("image/svg+xml", "svg"),
	/** TAR archive data */
	TAR("application/x-tar", "tar"),
	/** TeX document */
	TEX("text/x-tex", "tex"),
	/** TeXinfo document */
	TEXINFO("text/x-texinfo", "texinfo"),
	/** TIFF image */
	TIFF("image/tiff", "tiff"),
	/** Troff document */
	TROFF("text/troff", "troff"),
	/** vCard visiting card */
	VCARD("text/x-vcard", "vcard"),
	/** Mpeg video */
	VIDEO_MPEG("video/mpeg", "mpeg"),
	/** VRML modeling file */
	VRML("model/vrml", "vrml"),
	/** WAV audio */
	WAV("audio/x-wav", "wav"),
	/** X3D modeling file */
	X3D("model/x3d", "x3d"),
	/** XML document */
	XML("application/xml", "xml"),
	/** Zip archive data */
	ZIP("application/zip", "zip"),
	/** Zoo archive data */
	ZOO("application/x-zoo", "zoo"),

	// default if no match
	UNKNOWN("unknown", "unknown"),
	// end
	;

	private final static Map<String, ContentType> mimeTypeMap = new HashMap<String, ContentType>();

	static {
		for (ContentType type : values()) {
			mimeTypeMap.put(type.mimeType, type);
		}
	}

	private final String mimeType;
	private final String simpleName;

	private ContentType(String mimeType, String simpleName) {
		this.mimeType = mimeType;
		this.simpleName = simpleName;
	}

	/**
	 * Get simple name of the type or null if none.
	 */
	public String getSimpleName() {
		return simpleName;
	}

	/**
	 * Return the type associated with the mime-type string or {@link #UNKNOWN} if not found.
	 */
	public static ContentType fromMimeType(String mimeType) {
		if (mimeType == null) {
			return UNKNOWN;
		}
		ContentType type = mimeTypeMap.get(mimeType);
		if (type == null) {
			return UNKNOWN;
		} else {
			return type;
		}

	}
}
