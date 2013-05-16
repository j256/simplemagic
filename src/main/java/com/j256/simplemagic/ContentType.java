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

	AIFF("audio/x-aiff", "aiff"),
	APPLE_QUICKTIME_IMAGE("image/x-quicktime", null),
	APPLE_QUICKTIME_MOVIE("video/quicktime", "quicktime"),
	ARC("application/x-arc", "arc"),
	AUDIO_MPEG("audio/mpeg", "mpeg"),
	AVI("video/x-msvideo", "avi"),
	AWK("text/x-awk", "awk"),
	BINHEX("application/mac-binhex40", "binhex"),
	BITTORRENT("application/x-bittorrent", "bittorrent"),
	BMP("image/x-ms-bmp", "bmp"),
	BZIP2("application/x-bzip2", "bzip2"),
	COMPRESS("application/x-compress", "compress"),
	COREL_DRAW("image/x-coreldraw", "corel-draw"),
	CORE_DUMP("application/x-coredump", "core-dump"),
	CPIO("application/x-cpio", "cpio"),
	DBM("application/x-dbm", "dbm"),
	DEBIAN_PACKAGE("application/x-debian-package", "pkg"),
	DIFF("text/x-diff", "diff"),
	DVI("application/x-dvi", "dvi"),
	FLASH("application/x-shockwave-flash", "flash"),
	FLASH_VIDEO("video/x-flv", "flash-video"),
	FORTRAN("text/x-fortran", "fortran"),
	FRAMEMAKER("application/x-mif", "framemaker"),
	GAWK("text/x-gawk", "gawk"),
	GDBM("application/x-gdbm", "gdbm"),
	GIF("image/gif", "gif"),
	GNUMERIC("application/x-gnumeric", "gnumeric"),
	GNUPG_KEYRING("application/x-gnupg-keyring", "gnupg-keyring"),
	GNU_INFO("text/x-info", "gnu-info"),
	GRAPHVIZ("text/vnd.graphviz", "graphviz"),
	GZIP("application/x-gzip", "gzip"),
	H264("video/h264", "h264"),
	HTML("text/html", "html"),
	ICO("image/x-ico", "ico"),
	ISO_9660("application/x-iso9660-image", "iso9660"),
	JAVA_APPLET("application/x-java-applet", "applet"),
	JAVA_KEYSTORE("application/x-java-keystore", "java-keystore"),
	JPEG("image/jpeg", "jpeg"),
	JPEG_2000("image/jp2", "jp2"),
	LHA("application/x-lha", "lha"),
	LISP("text/x-lisp", "lisp"),
	LOTUS_123("application/x-123", "lotus-123"),
	MICROSOFT_ACCESS("application/x-msaccess", "access"),
	MICROSOFT_EXCEL("application/vnd.ms-excel", "excel"),
	MICROSOFT_WORD("application/msword", "word"),
	MIDI("audio/midi", "midi"),
	MNG("video/x-mng", "mng"),
	MP4("video/mp4", "mp4"),
	MP4V("video/mp4v-es", "mp4v"),
	NAWK("text/x-nawk", "nawk"),
	NEWS("message/news", "news"),
	OGG("application/ogg", "ogg"),
	PBM("image/x-portable-bitmap", "pbm"),
	PDF("application/pdf", "pdf"),
	PERL("text/x-perl", "perl"),
	PGM("image/x-portable-greymap", "pgm"),
	PGP("text/PGP", "pgp"),
	PGP_KEYRING("application/x-pgp-keyring", "pgp-keyring"),
	PGP_SIGNATURE("application/pgp-signature", "pgp-signature"),
	PHOTOSHOP("image/vnd.adobe.photoshop", "photoshop"),
	PHP("text/x-php", "php"),
	PNG("image/png", "png"),
	POSTSCRIPT("application/postscript", "postscript"),
	PPM("image/x-portable-pixmap", "ppm"),
	RAR("application/x-rar", "rar"),
	REAL_AUDIO("audio/x-pn-realaudio", "real-audio"),
	REAL_MEDIA("application/vnd.rn-realmedia", "real-media"),
	RFC822("message/rfc822", "rfc822"),
	RPC("application/x-rpm", "rpm"),
	RTF("text/rtf", "rtf"),
	SHARED_LIBRARY("application/x-sharedlib", "shared-lib"),
	SHELL_SCRIPT("text/x-shellscript", "shell-script"),
	STUFFIT("application/x-stuffit", "stuffit"),
	SVG("image/svg+xml", "svg"),
	TAR("application/x-tar", "tar"),
	TEX("text/x-tex", "tex"),
	TEXINFO("text/x-texinfo", "texinfo"),
	TIFF("image/tiff", "tiff"),
	TROFF("text/troff", "troff"),
	VCARD("text/x-vcard", "vcard"),
	VIDEO_MPEG("video/mpeg", "mpeg"),
	VRML("model/vrml", "vrml"),
	WAV("audio/x-wav", "wav"),
	X3D("model/x3d", "x3d"),
	XML("application/xml", "xml"),
	ZIP("application/zip", "zip"),
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
