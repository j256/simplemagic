package com.j256.simplemagic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.j256.simplemagic.entries.IanaEntries;
import com.j256.simplemagic.entries.IanaEntry;

/**
 * Enumerated type of the content if it is known by SimpleMagic matched from the mime-type. This information is _not_
 * processed from the magic files.
 * 
 * @author graywatson
 */
public enum ContentType {

	/** Adobe Illustrator (may look like a PDF) */
	AI("application/vnd.adobe.illustrator", "ai"),
	/** AIFF audio format */
	AIFF("audio/x-aiff", "aiff", "aif", "aiff", "aifc"),
	/** Apple Quicktime image */
	APPLE_QUICKTIME_IMAGE("image/x-quicktime", null),
	/** Apple Quicktime movie */
	APPLE_QUICKTIME_MOVIE("video/quicktime", "quicktime", "qt", "mov"),
	/** ARC archive data */
	ARC("application/x-arc", "arc", "arc"),
	/** MPEG audio file */
	AUDIO_MPEG("audio/mpeg", "mpeg", "mpga", "mp2", "mp2a", "mp3", "m2a", "m3a"),
	/** Microsoft AVI video file */
	AVI("video/x-msvideo", "avi", "avi"),
	/** Unix AWK command script */
	AWK("text/x-awk", "awk", "awk"),
	/** Macintosh BinHex file */
	BINHEX("application/mac-binhex40", "binhex", "hqx"),
	/** Bittorrent file */
	BITTORRENT("application/x-bittorrent", "bittorrent", "torrent"),
	/** Microsoft PC bitmap image */
	BMP("image/x-ms-bmp", "bmp", "bmp"),
	/** Bzip2 compressed file */
	BZIP2("application/x-bzip2", "bzip2", "bz2", "boz"),
	/** Unix compress file */
	COMPRESS("application/x-compress", "compress", "Z"),
	/** Corel Draw image file */
	COREL_DRAW("image/x-coreldraw", "corel-draw"),
	/** Unix core dump output */
	CORE_DUMP("application/x-coredump", "core-dump"),
	/** Unix CPIO archive data */
	CPIO("application/x-cpio", "cpio", "cpio"),
	/** Berkeley database file */
	DBM("application/x-dbm", "dbm"),
	/** Debian installation package */
	DEBIAN_PACKAGE("application/x-debian-package", "debian-pkg", "pkg", "deb", "udeb"),
	/** Unix diff output */
	DIFF("text/x-diff", "diff", "diff"),
	/** TeX DVI output file */
	DVI("application/x-dvi", "dvi", "dvi"),
	/** FITS document */
	FITS("application/fits", "fits", "fits"),
	/** Macromedia Flash data */
	FLASH("application/x-shockwave-flash", "flash", "swf"),
	/** Macromedia Flash movie file */
	FLASH_VIDEO("video/x-flv", "flash-video", "flv"),
	/** FORTRAN program */
	FORTRAN("text/x-fortran", "fortran", "f", "for", "f77", "f90"),
	/** FrameMaker document */
	FRAMEMAKER("application/x-mif", "framemaker", "fm", "frame", "maker", "book"),
	/** GNU awk script */
	GAWK("text/x-gawk", "gawk", "gawk"),
	/** GNU database file */
	GDBM("application/x-gdbm", "gdbm"),
	/** GIF image file */
	GIF("image/gif", "gif", "gif"),
	/** GNU Numeric file */
	GNUMERIC("application/x-gnumeric", "gnumeric"),
	/** GPG keyring file */
	GNUPG_KEYRING("application/x-gnupg-keyring", "gnupg-keyring"),
	/** GNU Info file */
	GNU_INFO("text/x-info", "gnu-info", "info"),
	/** Gzip compressed data */
	GZIP("application/x-gzip", "gzip", "gz"),
	/** H264 video encoded file */
	H264("video/h264", "h264", "h264"),
	/** HTML document */
	HTML("text/html", "html", "html", "htm"),
	/** MS Windows icon resource */
	ICO("image/x-ico", "ico", "ico"),
	/** ISO 9660 CD-ROM filesystem data */
	ISO_9660("application/x-iso9660-image", "iso9660", "iso"),
	/** Java applet */
	JAVA_APPLET("application/x-java-applet", "applet"),
	/** Java keystore file */
	JAVA_KEYSTORE("application/x-java-keystore", "java-keystore"),
	/** JPEG image */
	JPEG("image/jpeg", "jpeg", "jpeg", "jpg", "jpe"),
	/** JPEG 2000 image */
	JPEG_2000("image/jp2", "jp2", "jp2"),
	/** LHA archive data */
	LHA("application/x-lha", "lha", "lha", "lzh"),
	/** Lisp program */
	LISP("text/x-lisp", "lisp", "lisp"),
	/** Lotus 123 spreadsheet */
	LOTUS_123("application/x-123", "lotus-123", "123"),
	/** Microsoft access database */
	MICROSOFT_ACCESS("application/x-msaccess", "access", "mdb"),
	/** Microsoft excel spreadsheet */
	MICROSOFT_EXCEL("application/vnd.ms-excel", "excel", "xls", "xlm", "xla", "xlc", "xlt", "xlw", "xlsx"),
	/** Microsoft word document */
	MICROSOFT_WORD("application/msword", "word", "doc", "dot", "docx"),
	/** Microsoft office document */
	MICROSOFT_OFFICE("application/vnd.openxmlformats-officedocument", "office"),
	/** MIDI audio */
	MIDI("audio/midi", "midi", "mid", "midi", "kar", "rmi"),
	/** MNG video */
	MNG("video/x-mng", "mng", "mng"),
	/** MP4 encoded video */
	MP4A("video/mp4", "mp4a", "mp4", "mp4a", "mpg4"),
	/** MP4V encoded video */
	MP4V("video/mp4v-es", "mp4v", "mp4v"),
	/** New Awk script */
	NAWK("text/x-nawk", "nawk", "nawk"),
	/** Network news message */
	NEWS("message/news", "news"),
	/** OGG file container */
	OGG("application/ogg", "ogg", "ogg", "oga", "spx"),
	/** PBM image */
	PBM("image/x-portable-bitmap", "pbm", "pbm"),
	/** PDF document */
	PDF("application/pdf", "pdf", "pdf"),
	/** Perl script */
	PERL("text/x-perl", "perl", "pl"),
	/** PGM image */
	PGM("image/x-portable-greymap", "pgm", "pgm"),
	/** PGP encrypted message */
	PGP("application/pgp-encrypted", "pgp", "pgp"),
	/** PGP keyring */
	PGP_KEYRING("application/x-pgp-keyring", "pgp-keyring"),
	/** PGP signature */
	PGP_SIGNATURE("application/pgp-signature", "pgp-signature"),
	/** Photoshop image */
	PHOTOSHOP("image/vnd.adobe.photoshop", "photoshop", "psd"),
	/** PHP script */
	PHP("text/x-php", "php", "php"),
	/** PNG image */
	PNG("image/png", "png", "png"),
	/** Postscript file */
	POSTSCRIPT("application/postscript", "postscript", "ps", "eps"),
	/** PPM image */
	PPM("image/x-portable-pixmap", "ppm", "ppm"),
	/** RAR archive data */
	RAR("application/x-rar", "rar", "rar"),
	/** Real-audio file */
	REAL_AUDIO("audio/x-pn-realaudio", "real-audio", "ram", "ra"),
	/** Real-media file */
	REAL_MEDIA("application/vnd.rn-realmedia", "real-media", "rm"),
	/** RFC822 news message */
	MIME("message/rfc822", "mime", "eml", "mime"),
	/** RedHat package file */
	RPM("application/x-rpm", "rpm", "rpm"),
	/** Rich text format document */
	RTF("text/rtf", "rtf", "rtf"),
	/** Shared library file */
	SHARED_LIBRARY("application/x-sharedlib", "shared-lib"),
	/** Unix shell script */
	SHELL_SCRIPT("text/x-shellscript", "shell-script", "sh"),
	/** Mac Stuffit archive data */
	STUFFIT("application/x-stuffit", "stuffit", "sit"),
	/** SVG image */
	SVG("image/svg+xml", "svg", "svg", "svgz"),
	/** TAR archive data */
	TAR("application/x-tar", "tar", "tar"),
	/** TeX document */
	TEX("text/x-tex", "tex", "tex"),
	/** TeXinfo document */
	TEXINFO("text/x-texinfo", "texinfo", "texinfo", "texi"),
	/** TIFF image */
	TIFF("image/tiff", "tiff", "tiff", "tif"),
	/** Troff document */
	TROFF("text/troff", "troff", "t", "tr", "roff", "man", "me", "ms"),
	/** vCard visiting card */
	VCARD("text/x-vcard", "vcard", "vcf"),
	/** Mpeg video */
	VIDEO_MPEG("video/mpeg", "mpeg", "mpeg", "mpg", "mpe", "m1v", "m2v"),
	/** VRML modeling file */
	VRML("model/vrml", "vrml", "wrl", "vrml"),
	/** WAV audio */
	WAV("audio/x-wav", "wav", "wav"),
	/** X3D modeling file */
	X3D("model/x3d", "x3d", "x3d", "x3dz"),
	/** XML document */
	XML("application/xml", "xml", "xml", "xsl"),
	/** Zip archive data */
	ZIP("application/zip", "zip", "zip"),
	/** Zoo archive data */
	ZOO("application/x-zoo", "zoo", "zoo"),

	/**
	 * Copied from http://svn.apache.org/viewvc/httpd/httpd/trunk/docs/conf/mime.types?view=co
	 * 
	 * FYI: many of these are not in the built in magic rules file.
	 */
	ANDREW_INSERT("application/andrew-inset", "andrew-insert", "ez"),
	APPLIXWARE("application/applixware", "applixware", "aw"),
	ATOM("application/atom+xml", "atom", "atom"),
	CU_SEEME("application/cu-seeme", "cu-seeme", "cu"),
	DOCBOOK("application/docbook+xml", "docbook", "dbk"),
	DSSC("application/dssc+der", "dssc", "dssc"),
	ECMA_SCRIPT("application/ecmascript", "ecma", "ecma"),
	EMMA("application/emma+xml", "emma", "emma"),
	EPUB("application/epub+zip", "epub", "epub"),
	EXI("application/exi", "exi", "exi"),
	FONT_TDPFR("application/font-tdpfr", "pfr", "pfr"),
	GML("application/gml+xml", "gml", "gml"),
	GPX("application/gpx+xml", "gpx", "gpx"),
	GXF("application/gxf", "gxf", "gxf"),
	HYPER_STUDIO("application/hyperstudio", "hyper-studio", "stk"),
	INKML("application/inkml+xml", "inkml", "ink", "inkml"),
	IPFIX("application/ipfix", "ipfix", "ipfix"),
	JAVA_ARCHIVE("application/java-archive", "java-archive", "jar"),
	JAVA_SERIALIZED("application/java-serialized-object", "java-serialized", "ser"),
	JAVA_CLASS("application/java-vm", "java-class", "class"),
	JAVASCRIPT("application/javascript", "javascript", "js"),
	JSON("application/json", "json", "json"),
	JSON_ML("application/jsonml+json", "jsonml", "jsonml"),
	LOST_XML("application/lost+xml", "lostxml", "lostxml"),
	COMPACT_PRO("application/mac-compactpro", "compact-pro", "cpt"),
	MADS("application/mads+xml", "mads", "mads"),
	MARC("application/marc", "marc", "mrc"),
	MARC_XML("application/marcxml+xml", "marc-xml", "mrcx"),
	MATHEMATICA("application/mathematica", "mathematica", "ma", "nb", "mb"),
	MATH_ML("application/mathml+xml", "mathml", "mathml"),
	MBOX("application/mbox", "mbox", "mbox"),
	META4("application/metalink4+xml", "meta4", "meta4"),
	METS("application/mets+xml", "mets", "mets"),
	MODS("application/mods+xml", "mods", "mods"),
	MP21("application/mp21", "mp21", "m21", "mp21"),
	MXF("application/mxf", "mxf", "mxf"),
	ODA("application/oda", "oda", "oda"),
	OEBPS_PACKAGE("application/oebps-package+xml", "oebps-package", "opf"),
	OMDOC("application/omdoc+xml", "omdoc", "omdoc"),
	ONE_NOTE("application/onenote", "one-note", "onetoc", "onetoc2", "onetmp", "onepkg"),
	OXPS("application/oxps", "oxps", "oxps"),
	PICS_RULES("application/pics-rules", "pics-rules", "prf"),
	PKCS10("application/pkcs10", "pkcs10", "p10"),
	PKCS7_MIME("application/pkcs7-mime", "pkcs7-mime", "p7m", "p7c"),
	PKCS7_SIGNATURE("application/pkcs7-signature", "pkcs7-signature", "p7s"),
	PKCS8("application/pkcs8", "pkcs8", "p8"),
	PKIX_ATTR_CERT("application/pkix-attr-cert", "ac", "ac"),
	PKIX_CERT("application/pkix-cert", "pkix-cert", "cer"),
	PKIX_CRL("application/pkix-crl", "pkix-crl", "crl"),
	PKIX_PKIPATH("application/pkix-pkipath", "pkix-pkipath", "pkipath"),
	PKIXCMP("application/pkixcmp", "pkixcmp", "pki"),
	PLS("application/pls+xml", "pls-xml", "pls"),
	PRS_CWW("application/prs.cww", "prs-cww", "cww"),
	PSKC("application/pskc+xml", "pskc", "pskcxml"),
	RDF("application/rdf+xml", "rdf", "rdf"),
	REGINFO("application/reginfo+xml", "reginfo", "rif"),
	RELAX_NG_COMPACT("application/relax-ng-compact-syntax", "relax-ng-compact", "rnc"),
	RESOURCE_LISTS("application/resource-lists+xml", "resource-lists", "rl"),
	RESOURCE_LISTS_DIFF("application/resource-lists-diff+xml", "resource-lists-diff", "rld"),
	RLS_SERVICES("application/rls-services+xml", "rls-services", "rs"),
	RPKI_GHOSTBUSTERS("application/rpki-ghostbusters", "rpki-ghostbusters", "gbr"),
	RPKI_MANIFEST("application/rpki-manifest", "rpki-manifest", "mft"),
	RPKI_ROA("application/rpki-roa", "rpki-roa", "roa"),
	RSD("application/rsd+xml", "rsd", "rsd"),
	RSS("application/rss+xml", "rss", "rss"),
	SBML("application/sbml+xml", "sbml", "sbml"),
	SCVP_CV_REQUEST("application/scvp-cv-request", "scvp-cv-request", "scq"),
	SCVP_CV_RESPONSE("application/scvp-cv-response", "scvp-cv-response", "scs"),
	SCVP_VP_REQUEST("application/scvp-vp-request", "scvp-vp-request", "spq"),
	SCVP_VP_RESPONSE("application/scvp-vp-response", "scvp-vp-response", "spp"),
	SDP("application/sdp", "sdp", "sdp"),
	SHF("application/shf+xml", "shf", "shf"),
	SMIL("application/smil+xml", "smil", "smi", "smil"),
	SPARQL_QUERY("application/sparql-query", "sparql-query", "rq"),
	SPARQL_RESULTS("application/sparql-results+xml", "sparql-results", "srx"),
	SRGS("application/srgs", "srgs", "gram"),
	SRGS_XML("application/srgs+xml", "srgs-xml", "grxml"),
	SRU("application/sru+xml", "sru", "sru"),
	SSDL("application/ssdl+xml", "ssdl", "ssdl"),
	SSML("application/ssml+xml", "ssml", "ssml"),
	TEI("application/tei+xml", "tei", "tei"),
	TFI("application/thraud+xml", "tfi", "tfi"),
	TIMESTAMPED_DATA("application/timestamped-data", "timestamped-data", "tsd"),
	PLB("application/vnd.3gpp.pic-bw-large", "plb", "plb"),
	PSB("application/vnd.3gpp.pic-bw-small", "psb", "psb"),
	PVB("application/vnd.3gpp.pic-bw-var", "pvb", "pvb"),
	TCAP("application/vnd.3gpp2.tcap", "tcap", "tcap"),
	PWN("application/vnd.3m.post-it-notes", "pwn", "pwn"),
	ASO("application/vnd.accpac.simply.aso", "aso", "aso"),
	IMP("application/vnd.accpac.simply.imp", "imp", "imp"),
	ACU("application/vnd.acucobol", "acu", "acu"),
	ACU_CORP("application/vnd.acucorp", "acu-corp", "atc", "acutc"),
	AIR("application/vnd.adobe.air-application-installer-package+zip", "air", "air"),
	FCDT("application/vnd.adobe.formscentral.fcdt", "fcdt", "fcdt"),
	ADOBE_FXP("application/vnd.adobe.fxp", "adobe-fxp", "fxp", "fxpl"),
	XDP("application/vnd.adobe.xdp+xml", "xdp", "xdp"),
	XFDF("application/vnd.adobe.xfdf", "xfdf", "xfdf"),
	AHEAD("application/vnd.ahead.space", "ahead", "ahead"),
	AZF("application/vnd.airzip.filesecure.azf", "azf", "azf"),
	AZS("application/vnd.airzip.filesecure.azs", "azs", "azs"),
	AZW("application/vnd.amazon.ebook", "azw", "azw"),
	ACC("application/vnd.americandynamics.acc", "acc", "acc"),
	AMI("application/vnd.amiga.ami", "ami", "ami"),
	APK("application/vnd.android.package-archive", "apk", "apk"),
	CII("application/vnd.anser-web-certificate-issue-initiation", "cii", "cii"),
	FTI("application/vnd.anser-web-funds-transfer-initiation", "fti", "fti"),
	ATX("application/vnd.antix.game-component", "atx", "atx"),
	MPKG("application/vnd.apple.installer+xml", "mpkg", "mpkg"),
	M3U8("application/vnd.apple.mpegurl", "m3u8", "m3u8"),
	SWI("application/vnd.aristanetworks.swi", "swi", "swi"),
	IOTA("application/vnd.astraea-software.iota", "iota", "iota"),
	AEP("application/vnd.audiograph", "aep", "aep"),
	MPM("application/vnd.blueice.multipass", "mpm", "mpm"),
	BMI("application/vnd.bmi", "bmi", "bmi"),
	REP("application/vnd.businessobjects", "rep", "rep"),
	CDXML("application/vnd.chemdraw+xml", "cdxml", "cdxml"),
	MMD("application/vnd.chipnuts.karaoke-mmd", "mmd", "mmd"),
	CDY("application/vnd.cinderella", "cdy", "cdy"),
	CLA("application/vnd.claymore", "cla", "cla"),
	RP9("application/vnd.cloanto.rp9", "rp9", "rp9"),
	CLONK_C4GROUP("application/vnd.clonk.c4group", "clonk-c4group", "c4g", "c4d", "c4f", "c4p", "c4u"),
	C11AMC("application/vnd.cluetrust.cartomobile-config", "c11amc", "c11amc"),
	C11AMZ("application/vnd.cluetrust.cartomobile-config-pkg", "c11amz", "c11amz"),
	CSP("application/vnd.commonspace", "csp", "csp"),
	CDBCMSG("application/vnd.contact.cmsg", "cdbcmsg", "cdbcmsg"),
	CMC("application/vnd.cosmocaller", "cmc", "cmc"),
	CLKX("application/vnd.crick.clicker", "clkx", "clkx"),
	CLKK("application/vnd.crick.clicker.keyboard", "clkk", "clkk"),
	CLKP("application/vnd.crick.clicker.palette", "clkp", "clkp"),
	CLKT("application/vnd.crick.clicker.template", "clkt", "clkt"),
	CLKW("application/vnd.crick.clicker.wordbank", "clkw", "clkw"),
	WBS("application/vnd.criticaltools.wbs+xml", "wbs", "wbs"),
	PML("application/vnd.ctc-posml", "pml", "pml"),
	PPD("application/vnd.cups-ppd", "ppd", "ppd"),
	CAR("application/vnd.curl.car", "car", "car"),
	PCURL("application/vnd.curl.pcurl", "pcurl", "pcurl"),
	DART("application/vnd.dart", "dart", "dart"),
	RDZ("application/vnd.data-vision.rdz", "rdz", "rdz"),
	DECE_DATA("application/vnd.dece.data", "dece-data", "uvf", "uvvf", "uvd", "uvvd"),
	DECE_TTML("application/vnd.dece.ttml+xml", "dece-ttml", "uvt uvvt"),
	DECE_UNSPECIFIED("application/vnd.dece.unspecified", "dece-unspecified", "uvx uvvx"),
	DECE_ZIP("application/vnd.dece.zip", "dece-zip", "uvz", "uvvz"),
	FE_LAUNCH("application/vnd.denovo.fcselayout-link", "fe_launch", "fe_launch"),
	DNA("application/vnd.dna", "dna", "dna"),
	MLP("application/vnd.dolby.mlp", "mlp", "mlp"),
	DPG("application/vnd.dpgraph", "dpg", "dpg"),
	DFAC("application/vnd.dreamfactory", "dfac", "dfac"),
	KPXX("application/vnd.ds-keypoint", "kpxx", "kpxx"),
	AIT("application/vnd.dvb.ait", "ait", "ait"),
	SVC("application/vnd.dvb.service", "svc", "svc"),
	GEO("application/vnd.dynageo", "geo", "geo"),
	MAG("application/vnd.ecowin.chart", "mag", "mag"),
	NML("application/vnd.enliven", "nml", "nml"),
	ESF("application/vnd.epson.esf", "esf", "esf"),
	MSF("application/vnd.epson.msf", "msf", "msf"),
	QAM("application/vnd.epson.quickanime", "qam", "qam"),
	SLT("application/vnd.epson.salt", "slt", "slt"),
	SSF("application/vnd.epson.ssf", "ssf", "ssf"),
	ESZIGNO3("application/vnd.eszigno3+xml", "eszigno3", "es3", "et3"),
	EZ2("application/vnd.ezpix-album", "ez2", "ez2"),
	EZ3("application/vnd.ezpix-package", "ez3", "ez3"),
	FDF("application/vnd.fdf", "fdf", "fdf"),
	MSEED("application/vnd.fdsn.mseed", "mseed", "mseed"),
	FDSN_SEED("application/vnd.fdsn.seed", "fdns-seed", "seed"),
	GPH("application/vnd.flographit", "gph", "gph"),
	FTC("application/vnd.fluxtime.clip", "ftc", "ftc"),
	FNC("application/vnd.frogans.fnc", "fnc", "fnc"),
	LTF("application/vnd.frogans.ltf", "ltf", "ltf"),
	FSC("application/vnd.fsc.weblaunch", "fsc", "fsc"),
	OAS("application/vnd.fujitsu.oasys", "oas", "oas"),
	OA2("application/vnd.fujitsu.oasys2", "oa2", "oa2"),
	OA3("application/vnd.fujitsu.oasys3", "oa3", "oa3"),
	FG5("application/vnd.fujitsu.oasysgp", "fg5", "fg5"),
	BH2("application/vnd.fujitsu.oasysprs", "bh2", "bh2"),
	DDD("application/vnd.fujixerox.ddd", "ddd", "ddd"),
	XDW("application/vnd.fujixerox.docuworks", "xdw", "xdw"),
	XBD("application/vnd.fujixerox.docuworks.binder", "xbd", "xbd"),
	FZS("application/vnd.fuzzysheet", "fzs", "fzs"),
	TXD("application/vnd.genomatix.tuxedo", "txd", "txd"),
	GGB("application/vnd.geogebra.file", "ggb", "ggb"),
	GGT("application/vnd.geogebra.tool", "ggt", "ggt"),
	GEOMETRY_EXPLORER("application/vnd.geometry-explorer", "geometry-explorer", "gex gre"),
	GXT("application/vnd.geonext", "gxt", "gxt"),
	G2W("application/vnd.geoplan", "g2w", "g2w"),
	G3W("application/vnd.geospace", "g3w", "g3w"),
	GMX("application/vnd.gmx", "gmx", "gmx"),
	KML("application/vnd.google-earth.kml+xml", "kml", "kml"),
	KMZ("application/vnd.google-earth.kmz", "kmz", "kmz"),
	GRAFEQ("application/vnd.grafeq", "grafeq", "gqf gqs"),
	GAC("application/vnd.groove-account", "gac", "gac"),
	GHF("application/vnd.groove-help", "ghf", "ghf"),
	GIM("application/vnd.groove-identity-message", "gim", "gim"),
	GRV("application/vnd.groove-injector", "grv", "grv"),
	GTM("application/vnd.groove-tool-message", "gtm", "gtm"),
	TPL("application/vnd.groove-tool-template", "tpl", "tpl"),
	VCG("application/vnd.groove-vcard", "vcg", "vcg"),
	HAL("application/vnd.hal+xml", "hal", "hal"),
	ZMM("application/vnd.handheld-entertainment+xml", "zmm", "zmm"),
	HBCI("application/vnd.hbci", "hbci", "hbci"),
	LES("application/vnd.hhe.lesson-player", "les", "les"),
	HPGL("application/vnd.hp-hpgl", "hpgl", "hpgl"),
	HPID("application/vnd.hp-hpid", "hpid", "hpid"),
	HPS("application/vnd.hp-hps", "hps", "hps"),
	JLT("application/vnd.hp-jlyt", "jlt", "jlt"),
	PCL("application/vnd.hp-pcl", "pcl", "pcl"),
	PCLXL("application/vnd.hp-pclxl", "pclxl", "pclxl"),
	HYDROSTATIX_SOF("application/vnd.hydrostatix.sof-data", "hydrostatix-sof", "sfd-hdstx"),
	MPY("application/vnd.ibm.minipay", "mpy", "mpy"),
	MODCAP("application/vnd.ibm.modcap", "modcap", "afp", "listafp", "list3820"),
	IRM("application/vnd.ibm.rights-management", "irm", "irm"),
	SC("application/vnd.ibm.secure-container", "sc", "sc"),
	ICC_PROFILE("application/vnd.iccprofile", "icc-profile", "icc", "icm"),
	IGL("application/vnd.igloader", "igl", "igl"),
	IVP("application/vnd.immervision-ivp", "ivp", "ivp"),
	IVU("application/vnd.immervision-ivu", "ivu", "ivu"),
	IGM("application/vnd.insors.igm", "igm", "igm"),
	INTERCON("application/vnd.intercon.formnet", "intercon", "xpw", "xpx"),
	I2G("application/vnd.intergeo", "i2g", "i2g"),
	QBO("application/vnd.intu.qbo", "qbo", "qbo"),
	QFX("application/vnd.intu.qfx", "qfx", "qfx"),
	RCPROFILE("application/vnd.ipunplugged.rcprofile", "rcprofile", "rcprofile"),
	IRP("application/vnd.irepository.package+xml", "irp", "irp"),
	XPR("application/vnd.is-xpr", "xpr", "xpr"),
	FCS("application/vnd.isac.fcs", "fcs", "fcs"),
	JAM("application/vnd.jam", "jam", "jam"),
	RMS("application/vnd.jcp.javame.midlet-rms", "rms", "rms"),
	JISP("application/vnd.jisp", "jisp", "jisp"),
	JODA("application/vnd.joost.joda-archive", "joda", "joda"),
	KAHOOTZ("application/vnd.kahootz", "kahootz", "ktz", "ktr"),
	KARBON("application/vnd.kde.karbon", "karbon", "karbon"),
	CHRT("application/vnd.kde.kchart", "chrt", "chrt"),
	KFO("application/vnd.kde.kformula", "kfo", "kfo"),
	FLW("application/vnd.kde.kivio", "flw", "flw"),
	KON("application/vnd.kde.kontour", "kon", "kon"),
	KDE_KPRESENTER("application/vnd.kde.kpresenter", "kde-kpresenter", "kpr", "kpt"),
	KSP("application/vnd.kde.kspread", "ksp", "ksp"),
	KDE_WORD("application/vnd.kde.kword", "kde-word", "kwd", "kwt"),
	HTKE("application/vnd.kenameaapp", "htke", "htke"),
	KIA("application/vnd.kidspiration", "kia", "kia"),
	KINAR("application/vnd.kinar", "kinar", "kne", "knp"),
	KOAN("application/vnd.koan", "koan", "skp skd skt skm"),
	SSE("application/vnd.kodak-descriptor", "sse", "sse"),
	LASXML("application/vnd.las.las+xml", "lasxml", "lasxml"),
	LBD("application/vnd.llamagraphics.life-balance.desktop", "lbd", "lbd"),
	LBE("application/vnd.llamagraphics.life-balance.exchange+xml", "lbe", "lbe"),
	APR("application/vnd.lotus-approach", "apr", "apr"),
	PRE("application/vnd.lotus-freelance", "pre", "pre"),
	NSF("application/vnd.lotus-notes", "nsf", "nsf"),
	ORG("application/vnd.lotus-organizer", "org", "org"),
	SCM("application/vnd.lotus-screencam", "scm", "scm"),
	LWP("application/vnd.lotus-wordpro", "lwp", "lwp"),
	PORTPKG("application/vnd.macports.portpkg", "portpkg", "portpkg"),
	MCD("application/vnd.mcd", "mcd", "mcd"),
	MC1("application/vnd.medcalcdata", "mc1", "mc1"),
	CDKEY("application/vnd.mediastation.cdkey", "cdkey", "cdkey"),
	MWF("application/vnd.mfer", "mwf", "mwf"),
	MFM("application/vnd.mfmp", "mfm", "mfm"),
	FLO("application/vnd.micrografx.flo", "flo", "flo"),
	IGX("application/vnd.micrografx.igx", "igx", "igx"),
	MIF("application/vnd.mif", "mif", "mif"),
	DAF("application/vnd.mobius.daf", "daf", "daf"),
	DIS("application/vnd.mobius.dis", "dis", "dis"),
	MBK("application/vnd.mobius.mbk", "mbk", "mbk"),
	MQY("application/vnd.mobius.mqy", "mqy", "mqy"),
	MSL("application/vnd.mobius.msl", "msl", "msl"),
	PLC("application/vnd.mobius.plc", "plc", "plc"),
	TXF("application/vnd.mobius.txf", "txf", "txf"),
	MPN("application/vnd.mophun.application", "mpn", "mpn"),
	MPC("application/vnd.mophun.certificate", "mpc", "mpc"),
	XUL("application/vnd.mozilla.xul+xml", "xul", "xul"),
	CIL("application/vnd.ms-artgalry", "cil", "cil"),
	CAB("application/vnd.ms-cab-compressed", "cab", "cab"),
	FONT_OBJECT("application/vnd.ms-fontobject", "font-object", "eot"),
	CHM("application/vnd.ms-htmlhelp", "chm", "chm"),
	IMS("application/vnd.ms-ims", "ims", "ims"),
	LRM("application/vnd.ms-lrm", "lrm", "lrm"),
	THMX("application/vnd.ms-officetheme", "thmx", "thmx"),
	CAT("application/vnd.ms-pki.seccat", "cat", "cat"),
	STL("application/vnd.ms-pki.stl", "stl", "stl"),
	MICROSOFT_POWERPOINT("application/vnd.ms-powerpoint", "powerpoint", "ppt", "pps", "pot"),
	PPAM("application/vnd.ms-powerpoint.addin.macroenabled.12", "ppam", "ppam"),
	PPTM("application/vnd.ms-powerpoint.presentation.macroenabled.12", "pptm", "pptm"),
	SLDM("application/vnd.ms-powerpoint.slide.macroenabled.12", "sldm", "sldm"),
	PPSM("application/vnd.ms-powerpoint.slideshow.macroenabled.12", "ppsm", "ppsm"),
	POTM("application/vnd.ms-powerpoint.template.macroenabled.12", "potm", "potm"),
	MICROSOFT_PROJECT("application/vnd.ms-project", "project", "mpp", "mpt"),
	DOCM("application/vnd.ms-word.document.macroenabled.12", "docm", "docm"),
	DOTM("application/vnd.ms-word.template.macroenabled.12", "dotm", "dotm"),
	MICROSOFT_WORKS("application/vnd.ms-works", "works", "wps", "wks", "wcm", "wdb"),
	WPL("application/vnd.ms-wpl", "wpl", "wpl"),
	XPS("application/vnd.ms-xpsdocument", "xps", "xps"),
	MSEQ("application/vnd.mseq", "mseq", "mseq"),
	MUS("application/vnd.musician", "mus", "mus"),
	MSTY("application/vnd.muvee.style", "msty", "msty"),
	TAGLET("application/vnd.mynfc", "taglet", "taglet"),
	NLU("application/vnd.neurolanguage.nlu", "nlu", "nlu"),
	NITF("application/vnd.nitf", "nitf", "ntf", "nitf"),
	NND("application/vnd.noblenet-directory", "nnd", "nnd"),
	NNS("application/vnd.noblenet-sealer", "nns", "nns"),
	NNW("application/vnd.noblenet-web", "nnw", "nnw"),
	NGDAT("application/vnd.nokia.n-gage.data", "ngdat", "ngdat"),
	N_GAGE("application/vnd.nokia.n-gage.symbian.install", "n-gage", "n-gage"),
	RPST("application/vnd.nokia.radio-preset", "rpst", "rpst"),
	RPSS("application/vnd.nokia.radio-presets", "rpss", "rpss"),
	EDM("application/vnd.novadigm.edm", "edm", "edm"),
	EDX("application/vnd.novadigm.edx", "edx", "edx"),
	EXT("application/vnd.novadigm.ext", "ext", "ext"),
	OPENDOCUMENT_CHART("application/vnd.oasis.opendocument.chart", "odc", "odc"),
	OPENDOCUMENT_CHART_TEMPLATE("application/vnd.oasis.opendocument.chart-template", "otc", "otc"),
	OPENDOCUMENT_DATABASE("application/vnd.oasis.opendocument.database", "odb", "odb"),
	OPENDOCUMENT_FORMULA("application/vnd.oasis.opendocument.formula", "odf", "odf"),
	OPENDOCUMENT_FORMULA_TEMPLATE("application/vnd.oasis.opendocument.formula-template", "odft", "odft"),
	OPENDOCUMENT_GRAPHICS("application/vnd.oasis.opendocument.graphics", "odg", "odg"),
	OPENDOCUMENT_GRAPHICS_TEMPLATE("application/vnd.oasis.opendocument.graphics-template", "otg", "otg"),
	OPENDOCUMENT_IMAGE("application/vnd.oasis.opendocument.image", "odi", "odi"),
	OPENDOCUMENT_IMAGE_TEMPLATE("application/vnd.oasis.opendocument.image-template", "oti", "oti"),
	OPENDOCUMENT_PRESENTATION("application/vnd.oasis.opendocument.presentation", "odp", "odp"),
	OPENDOCUMENT_PRESENTATION_TEMPLATE("application/vnd.oasis.opendocument.presentation-template", "otp", "otp"),
	OPENDOCUMENT_SPREADSHEET("application/vnd.oasis.opendocument.spreadsheet", "opendocument-spreadsheet", "ods"),
	OPENDOCUMENT_SPREADSHEET_TEMPLATE("application/vnd.oasis.opendocument.spreadsheet-template", "ots", "ots"),
	OPENDOCUMENT_TEXT("application/vnd.oasis.opendocument.text", "opendocument-text", "odt"),
	OPENDOCUMENT_TEXT_MASTER("application/vnd.oasis.opendocument.text-master", "opendocument-text-master", "odm"),
	OPENDOCUMENT_TEXT_TEMPLATE("application/vnd.oasis.opendocument.text-template", "opendocument-text-template", "ott"),
	OPENDOCUMENT_TEXT_WEB("application/vnd.oasis.opendocument.text-web", "opendocument-text-web", "oth"),
	XO("application/vnd.olpc-sugar", "xo", "xo"),
	DD2("application/vnd.oma.dd2+xml", "dd2", "dd2"),
	OXT("application/vnd.openofficeorg.extension", "oxt", "oxt"),
	MICROSOFT_POWERPOINT_XML("application/vnd.openxmlformats-officedocument.presentationml.presentation", "powerpoint",
			"pptx"),
	SLDX("application/vnd.openxmlformats-officedocument.presentationml.slide", "sldx", "sldx"),
	PPSX("application/vnd.openxmlformats-officedocument.presentationml.slideshow", "ppsx", "ppsx"),
	POTX("application/vnd.openxmlformats-officedocument.presentationml.template", "potx", "potx"),
	MICROSOFT_EXCEL_XML("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "excel", "xlsx"),
	XLTX("application/vnd.openxmlformats-officedocument.spreadsheetml.template", "xltx", "xltx"),
	MICROSOFT_WORD_XML("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "word", "docx"),
	DOTX("application/vnd.openxmlformats-officedocument.wordprocessingml.template", "dotx", "dotx"),
	MGP("application/vnd.osgeo.mapguide.package", "mgp", "mgp"),
	DP("application/vnd.osgi.dp", "dp", "dp"),
	ESA("application/vnd.osgi.subsystem", "esa", "esa"),
	PALM("application/vnd.palm", "palm", "pdb", "pqa", "oprc"),
	PAW("application/vnd.pawaafile", "paw", "paw"),
	STR("application/vnd.pg.format", "str", "str"),
	EI6("application/vnd.pg.osasli", "ei6", "ei6"),
	EFIF("application/vnd.picsel", "efif", "efif"),
	WG("application/vnd.pmi.widget", "wg", "wg"),
	PLF("application/vnd.pocketlearn", "plf", "plf"),
	PBD("application/vnd.powerbuilder6", "pbd", "pbd"),
	BOX("application/vnd.previewsystems.box", "box", "box"),
	MGZ("application/vnd.proteus.magazine", "mgz", "mgz"),
	QPS("application/vnd.publishare-delta-tree", "qps", "qps"),
	PTID("application/vnd.pvi.ptid1", "ptid", "ptid"),
	QUARK_XPRESS("application/vnd.quark.quarkxpress", "quark-xpress", "qxd", "qxt", "qwd", "qwt", "qxl", "qxb"),
	BED("application/vnd.realvnc.bed", "bed", "bed"),
	MXL("application/vnd.recordare.musicxml", "mxl", "mxl"),
	MUSICXML("application/vnd.recordare.musicxml+xml", "musicxml", "musicxml"),
	CRYPTONOTE("application/vnd.rig.cryptonote", "cryptonote", "cryptonote"),
	COD("application/vnd.rim.cod", "cod", "cod"),
	RMVB("application/vnd.rn-realmedia-vbr", "rmvb", "rmvb"),
	LINK66("application/vnd.route66.link66+xml", "link66", "link66"),
	ST("application/vnd.sailingtracker.track", "st", "st"),
	SEE("application/vnd.seemail", "see", "see"),
	SEMA("application/vnd.sema", "sema", "sema"),
	SEMD("application/vnd.semd", "semd", "semd"),
	SEMF("application/vnd.semf", "semf", "semf"),
	IFM("application/vnd.shana.informed.formdata", "ifm", "ifm"),
	ITP("application/vnd.shana.informed.formtemplate", "itp", "itp"),
	IIF("application/vnd.shana.informed.interchange", "iif", "iif"),
	IPK("application/vnd.shana.informed.package", "ipk", "ipk"),
	MIND_MAPPER("application/vnd.simtech-mindmapper", "mind-mapper", "twd", "twds"),
	MMF("application/vnd.smaf", "mmf", "mmf"),
	TEACHER("application/vnd.smart.teacher", "teacher", "teacher"),
	SOLENT("application/vnd.solent.sdkm+xml", "solent", "sdkm", "sdkd"),
	DXP("application/vnd.spotfire.dxp", "dxp", "dxp"),
	SFS("application/vnd.spotfire.sfs", "sfs", "sfs"),
	SDC("application/vnd.stardivision.calc", "sdc", "sdc"),
	SDA("application/vnd.stardivision.draw", "sda", "sda"),
	SDD("application/vnd.stardivision.impress", "sdd", "sdd"),
	SMF("application/vnd.stardivision.math", "smf", "smf"),
	STARDIVISION_WRITER("application/vnd.stardivision.writer", "stardivision-writer", "sdw", "vor"),
	STARDIVISION_WRITER_GLOBAL("application/vnd.stardivision.writer-global", "stardivision-writer-global", "sgl"),
	SMZIP("application/vnd.stepmania.package", "smzip", "smzip"),
	SM("application/vnd.stepmania.stepchart", "sm", "sm"),
	SXC("application/vnd.sun.xml.calc", "sxc", "sxc"),
	STC("application/vnd.sun.xml.calc.template", "stc", "stc"),
	SXD("application/vnd.sun.xml.draw", "sxd", "sxd"),
	STD("application/vnd.sun.xml.draw.template", "std", "std"),
	SXI("application/vnd.sun.xml.impress", "sxi", "sxi"),
	STI("application/vnd.sun.xml.impress.template", "sti", "sti"),
	SXM("application/vnd.sun.xml.math", "sxm", "sxm"),
	SXW("application/vnd.sun.xml.writer", "sxw", "sxw"),
	SXG("application/vnd.sun.xml.writer.global", "sxg", "sxg"),
	STW("application/vnd.sun.xml.writer.template", "stw", "stw"),
	SUS_CALENDAR("application/vnd.sus-calendar", "sus-calendar", "sus", "susp"),
	SVD("application/vnd.svd", "svd", "svd"),
	SYMBIAN("application/vnd.symbian.install", "symbian", "sis", "sisx"),
	XSM("application/vnd.syncml+xml", "xsm", "xsm"),
	BDM("application/vnd.syncml.dm+wbxml", "bdm", "bdm"),
	XDM("application/vnd.syncml.dm+xml", "xdm", "xdm"),
	TAO("application/vnd.tao.intent-module-archive", "tao", "tao"),
	TCPDUMP("application/vnd.tcpdump.pcap", "tcpdump", "pcap", "cap", "dmp"),
	TMO("application/vnd.tmobile-livetv", "tmo", "tmo"),
	TPT("application/vnd.trid.tpt", "tpt", "tpt"),
	MXS("application/vnd.triscape.mxs", "mxs", "mxs"),
	TRA("application/vnd.trueapp", "tra", "tra"),
	UFDL("application/vnd.ufdl", "ufdl", "ufd", "ufdl"),
	UTZ("application/vnd.uiq.theme", "utz", "utz"),
	UMJ("application/vnd.umajin", "umj", "umj"),
	UNITY("application/vnd.unity", "unity", "unityweb"),
	UOML("application/vnd.uoml+xml", "uoml", "uoml"),
	VCX("application/vnd.vcx", "vcx", "vcx"),
	VISIO("application/vnd.visio", "visio", "vsd", "vst", "vss", "vsw"),
	VIS("application/vnd.visionary", "vis", "vis"),
	VSF("application/vnd.vsf", "vsf", "vsf"),
	WBXML("application/vnd.wap.wbxml", "wbxml", "wbxml"),
	WMLC("application/vnd.wap.wmlc", "wmlc", "wmlc"),
	WMLSC("application/vnd.wap.wmlscriptc", "wmlsc", "wmlsc"),
	WTB("application/vnd.webturbo", "wtb", "wtb"),
	NBP("application/vnd.wolfram.player", "nbp", "nbp"),
	WPD("application/vnd.wordperfect", "wpd", "wpd"),
	WQD("application/vnd.wqd", "wqd", "wqd"),
	STF("application/vnd.wt.stf", "stf", "stf"),
	XAR("application/vnd.xara", "xar", "xar"),
	XFDL("application/vnd.xfdl", "xfdl", "xfdl"),
	HVD("application/vnd.yamaha.hv-dic", "hvd", "hvd"),
	HVS("application/vnd.yamaha.hv-script", "hvs", "hvs"),
	HVP("application/vnd.yamaha.hv-voice", "hvp", "hvp"),
	OSF("application/vnd.yamaha.openscoreformat", "osf", "osf"),
	OSFPVG("application/vnd.yamaha.openscoreformat.osfpvg+xml", "osfpvg", "osfpvg"),
	SAF("application/vnd.yamaha.smaf-audio", "saf", "saf"),
	SPF("application/vnd.yamaha.smaf-phrase", "spf", "spf"),
	CMP("application/vnd.yellowriver-custom-menu", "cmp", "cmp"),
	ZUL("application/vnd.zul", "zul", "zir", "zirz"),
	ZAZ("application/vnd.zzazz.deck+xml", "zaz", "zaz"),
	VXML("application/voicexml+xml", "vxml", "vxml"),
	WGT("application/widget", "wgt", "wgt"),
	HLP("application/winhlp", "hlp", "hlp"),
	WSDL("application/wsdl+xml", "wsdl", "wsdl"),
	WSPOLICY("application/wspolicy+xml", "wspolicy", "wspolicy"),
	SEVEN_Z("application/x-7z-compressed", "7zip", "7z"),
	ABIWORD("application/x-abiword", "abiword", "abw"),
	ACE("application/x-ace-compressed", "ace", "ace"),
	DMG("application/x-apple-diskimage", "dmg", "dmg"),
	AUTHORWARE("application/x-authorware-bin", "authorware", "aab", "x32", "u32", "vox"),
	AAM("application/x-authorware-map", "aam", "aam"),
	AAS("application/x-authorware-seg", "aas", "aas"),
	BCPIO("application/x-bcpio", "bcpio", "bcpio"),
	BLORB("application/x-blorb", "blb blorb", "blb blorb"),
	BZ("application/x-bzip", "bz", "bz"),
	CBR("application/x-cbr", "cbr", "cbr", "cba", "cbt", "cbz", "cb7"),
	VCD("application/x-cdlink", "vcd", "vcd"),
	CFS("application/x-cfs-compressed", "cfs", "cfs"),
	CHAT("application/x-chat", "chat", "chat"),
	PGN("application/x-chess-pgn", "pgn", "pgn"),
	NSC("application/x-conference", "nsc", "nsc"),
	CSH("application/x-csh", "csh", "csh"),
	DGC("application/x-dgc-compressed", "dgc", "dgc"),
	X_DIRCTOR("application/x-director", "x-director", "dir", "dcr", "dxr", "cst", "cct", "cxt", "w3d", "fgd", "swa"),
	WAD("application/x-doom", "wad", "wad"),
	NCX("application/x-dtbncx+xml", "ncx", "ncx"),
	DTB("application/x-dtbook+xml", "dtb", "dtb"),
	RES("application/x-dtbresource+xml", "res", "res"),
	EVY("application/x-envoy", "evy", "evy"),
	EVA("application/x-eva", "eva", "eva"),
	BDF("application/x-font-bdf", "bdf", "bdf"),
	GSF("application/x-font-ghostscript", "gsf", "gsf"),
	PSF("application/x-font-linux-psf", "psf", "psf"),
	OTF("application/x-font-otf", "otf", "otf"),
	PCF("application/x-font-pcf", "pcf", "pcf"),
	SNF("application/x-font-snf", "snf", "snf"),
	FONT_TTF("application/x-font-ttf", "font-ttf", "ttf", "ttc"),
	FONT_TYPE1("application/x-font-type1", "font-type1", "pfa", "pfb", "pfm", "afm"),
	WOFF("application/x-font-woff", "woff", "woff"),
	SPL("application/x-futuresplash", "spl", "spl"),
	GCA("application/x-gca-compressed", "gca", "gca"),
	ULX("application/x-glulx", "ulx", "ulx"),
	GRAMPS("application/x-gramps-xml", "gramps", "gramps"),
	GTAR("application/x-gtar", "gtar", "gtar"),
	HDF("application/x-hdf", "hdf", "hdf"),
	INSTALL("application/x-install-instructions", "install", "install"),
	JNLP("application/x-java-jnlp-file", "jnlp", "jnlp"),
	LATEX("application/x-latex", "latex", "latex"),
	MIE("application/x-mie", "mie", "mie"),
	MOBIPOCKET_EBOOK("application/x-mobipocket-ebook", "mobipocket-ebook", "prc", "mobi"),
	APPLICATION("application/x-ms-application", "application", "application"),
	LNK("application/x-ms-shortcut", "lnk", "lnk"),
	WMD("application/x-ms-wmd", "wmd", "wmd"),
	WMZ("application/x-ms-wmz", "wmz", "wmz"),
	XBAP("application/x-ms-xbap", "xbap", "xbap"),
	OBD("application/x-msbinder", "obd", "obd"),
	CRD("application/x-mscardfile", "crd", "crd"),
	CLP("application/x-msclip", "clp", "clp"),
	MICROSOFT_MONEY("application/x-msmoney", "money", "mny"),
	PUB("application/x-mspublisher", "pub", "pub"),
	SCD("application/x-msschedule", "scd", "scd"),
	TRM("application/x-msterminal", "trm", "trm"),
	MICROSOFT_WRITE("application/x-mswrite", "write", "wri"),
	NETCDF("application/x-netcdf", "netcdf", "nc", "cdf"),
	NZB("application/x-nzb", "nzb", "nzb"),
	PKCS12("application/x-pkcs12", "pkcs12", "p12", "pfx"),
	PKCS7_CERTIFICATES("application/x-pkcs7-certificates", "pkcs7-certificates", "p7b", "spc"),
	P7R("application/x-pkcs7-certreqresp", "p7r", "p7r"),
	RIS("application/x-research-info-systems", "ris", "ris"),
	// extensions defined in SHELL_SCRIPT
	SH("application/x-sh", "sh"),
	SHAR("application/x-shar", "shar", "shar"),
	XAP("application/x-silverlight-app", "xap", "xap"),
	SQL("application/x-sql", "sql", "sql"),
	SITX("application/x-stuffitx", "sitx", "sitx"),
	SRT("application/x-subrip", "srt", "srt"),
	SV4CPIO("application/x-sv4cpio", "sv4cpio", "sv4cpio"),
	SV4CRC("application/x-sv4crc", "sv4crc", "sv4crc"),
	T3("application/x-t3vm-image", "t3", "t3"),
	GAM("application/x-tads", "gam", "gam"),
	TCL("application/x-tcl", "tcl", "tcl"),
	TFM("application/x-tex-tfm", "tfm", "tfm"),
	OBJ("application/x-tgif", "obj", "obj"),
	USTAR("application/x-ustar", "ustar", "ustar"),
	SRC("application/x-wais-source", "src", "src"),
	X509_CA_CERT("application/x-x509-ca-cert", "x509-ca-cert", "der", "crt"),
	XFIG("application/x-xfig", "xfig", "fig"),
	XLIFF("application/x-xliff+xml", "xliff", "xlf"),
	XP_INSTALL("application/x-xpinstall", "xp-install", "xpi"),
	XZ("application/x-xz", "xz", "xz"),
	ZMACHINE("application/x-zmachine", "zmachine", "z1", "z2", "z3", "z4", "z5", "z6", "z7", "z8"),
	XAML("application/xaml+xml", "xaml", "xaml"),
	XDF("application/xcap-diff+xml", "xdf", "xdf"),
	XENC("application/xenc+xml", "xenc", "xenc"),
	XHTML("application/xhtml+xml", "xhtml", "xhtml", "xht"),
	DTD("application/xml-dtd", "dtd", "dtd"),
	XOP("application/xop+xml", "xop", "xop"),
	XPL("application/xproc+xml", "xpl", "xpl"),
	XSLT("application/xslt+xml", "xslt", "xslt"),
	XSPF("application/xspf+xml", "xspf", "xspf"),
	XV("application/xv+xml", "xv", "mxml", "xhvml", "xvml", "xvm"),
	YANG("application/yang", "yang", "yang"),
	YIN("application/yin+xml", "yin", "yin"),
	ADP("audio/adpcm", "adp", "adp"),
	SND("audio/basic", "audio-basic", "au", "snd"),
	S3M("audio/s3m", "s3m", "s3m"),
	SIL("audio/silk", "silk", "sil"),
	DECE_AUDIO("audio/vnd.dece.audio", "dece-audio", "uva", "uvva"),
	EOL("audio/vnd.digital-winds", "eol", "eol"),
	DRA("audio/vnd.dra", "dra", "dra"),
	DTS("audio/vnd.dts", "dts", "dts"),
	DTSHD("audio/vnd.dts.hd", "dtshd", "dtshd"),
	LVP("audio/vnd.lucent.voice", "lvp", "lvp"),
	PYA("audio/vnd.ms-playready.media.pya", "pya", "pya"),
	ECELP4800("audio/vnd.nuera.ecelp4800", "ecelp4800", "ecelp4800"),
	ECELP7470("audio/vnd.nuera.ecelp7470", "ecelp7470", "ecelp7470"),
	ECELP9600("audio/vnd.nuera.ecelp9600", "ecelp9600", "ecelp9600"),
	RIP("audio/vnd.rip", "rip", "rip"),
	WEBA("audio/webm", "weba", "weba"),
	AAC("audio/x-aac", "aac", "aac"),
	CAF("audio/x-caf", "caf", "caf"),
	FLAC("audio/x-flac", "flac", "flac"),
	MKA("audio/x-matroska", "mka", "mka"),
	M3U("audio/x-mpegurl", "m3u", "m3u"),
	WAX("audio/x-ms-wax", "wax", "wax"),
	WMA("audio/x-ms-wma", "wma", "wma"),
	RMP("audio/x-pn-realaudio-plugin", "rmp", "rmp"),
	XM("audio/xm", "xm", "xm"),
	CDX("chemical/x-cdx", "cdx", "cdx"),
	CIF("chemical/x-cif", "cif", "cif"),
	CMDF("chemical/x-cmdf", "cmdf", "cmdf"),
	CML("chemical/x-cml", "cml", "cml"),
	CSML("chemical/x-csml", "csml", "csml"),
	XYZ("chemical/x-xyz", "xyz", "xyz"),
	CGM("image/cgm", "cgm", "cgm"),
	G3("image/g3fax", "g3", "g3"),
	IEF("image/ief", "ief", "ief"),
	KTX("image/ktx", "ktx", "ktx"),
	BTIF("image/prs.btif", "btif", "btif"),
	SGI("image/sgi", "sgi", "sgi"),
	DECE_GRAPHIC("image/vnd.dece.graphic", "dece-graphic", "uvi", "uvvi", "uvg", "uvvg"),
	// extensions in DVB_SUBTITLE
	SUB("image/vnd.dvb.subtitle", "sub"),
	DJVU("image/vnd.djvu", "djvu", "djvu", "djv"),
	DWG("image/vnd.dwg", "dwg", "dwg"),
	DXF("image/vnd.dxf", "dxf", "dxf"),
	FBS("image/vnd.fastbidsheet", "fbs", "fbs"),
	FPX("image/vnd.fpx", "fpx", "fpx"),
	FST("image/vnd.fst", "fst", "fst"),
	MMR("image/vnd.fujixerox.edmics-mmr", "mmr", "mmr"),
	RLC("image/vnd.fujixerox.edmics-rlc", "rlc", "rlc"),
	MDI("image/vnd.ms-modi", "mdi", "mdi"),
	WDP("image/vnd.ms-photo", "wdp", "wdp"),
	NPX("image/vnd.net-fpx", "npx", "npx"),
	WBMP("image/vnd.wap.wbmp", "wbmp", "wbmp"),
	XIF("image/vnd.xiff", "xif", "xif"),
	WEBP("image/webp", "webp", "webp"),
	RAS("image/x-cmu-raster", "ras", "ras"),
	CMX("image/x-cmx", "cmx", "cmx"),
	FREEHAND("image/x-freehand", "freehand", "fh", "fhc", "fh4", "fh5", "fh7"),
	SID("image/x-mrsid-image", "sid", "sid"),
	PCX("image/x-pcx", "pcx", "pcx"),
	PICT("image/x-pict", "pict", "pic", "pct"),
	PNM("image/x-portable-anymap", "pnm", "pnm"),
	RGB("image/x-rgb", "rgb", "rgb"),
	TGA("image/x-tga", "tga", "tga"),
	XBM("image/x-xbitmap", "xbm", "xbm"),
	XPM("image/x-xpixmap", "xpm", "xpm"),
	XWD("image/x-xwindowdump", "xwd", "xwd"),
	IGES("model/iges", "iges", "igs", "iges"),
	MESH("model/mesh", "mesh", "msh", "mesh", "silo"),
	DAE("model/vnd.collada+xml", "dae", "dae"),
	DWF("model/vnd.dwf", "dwf", "dwf"),
	GDL("model/vnd.gdl", "gdl", "gdl"),
	GTW("model/vnd.gtw", "gtw", "gtw"),
	MTS("model/vnd.mts", "mts", "mts"),
	VTU("model/vnd.vtu", "vtu", "vtu"),
	X3D_BINARY("model/x3d+binary", "x3d-binary", "x3db", "x3dbz"),
	X3D_VRML("model/x3d+vrml", "x3d-vrml", "x3dv", "x3dvz"),
	// extensions in X3D
	X3D_XML("model/x3d+xml", "x3d-xml"),
	CALENDAR("text/calendar", "calendar", "ics", "ifb"),
	CSS("text/css", "css", "css"),
	CSV("text/csv", "csv", "csv"),
	N3("text/n3", "n3", "n3"),
	TEXT("text/plain", "text", "txt", "text"),
	DSC("text/prs.lines.tag", "dsc", "dsc"),
	RTX("text/richtext", "rtx", "rtx"),
	SGML("text/sgml", "sgml", "sgml", "sgm"),
	TSV("text/tab-separated-values", "tsv", "tsv"),
	TTL("text/turtle", "ttl", "ttl"),
	CURL("text/vnd.curl", "curl", "curl"),
	DCURL("text/vnd.curl.dcurl", "dcurl", "dcurl"),
	SCURL("text/vnd.curl.scurl", "scurl", "scurl"),
	MCURL("text/vnd.curl.mcurl", "mcurl", "mcurl"),
	DVB_SUBTITLE("text/vnd.dvb.subtitle", "dvb-subtitle", "sub"),
	FLY("text/vnd.fly", "fly", "fly"),
	FLX("text/vnd.fmi.flexstor", "flx", "flx"),
	GV("text/vnd.graphviz", "gv", "gv"),
	SPOT("text/vnd.in3d.spot", "spot", "spot"),
	JAD("text/vnd.sun.j2me.app-descriptor", "jad", "jad"),
	WML("text/vnd.wap.wml", "wml", "wml"),
	WMLS("text/vnd.wap.wmlscript", "wmls", "wmls"),
	ASSEMBLY("text/x-asm", "assembly", "s asm"),
	C_SOURCE("text/x-c", "c-source", "c", "cc", "cxx", "cpp", "h", "hh", "dic"),
	JAVA("text/x-java-source", "java", "java"),
	OPML("text/x-opml", "opml", "opml"),
	PASCAL("text/x-pascal", "pascal", "p", "pas"),
	NFO("text/x-nfo", "nfo", "nfo"),
	ETX("text/x-setext", "etx", "etx"),
	SFV("text/x-sfv", "sfv", "sfv"),
	UU("text/x-uuencode", "uu", "uu"),
	VCS("text/x-vcalendar", "vcs", "vcs"),
	THREE_GP("video/3gpp", "3gp", "3gp"),
	THREE_G2("video/3gpp2", "3g2", "3g2"),
	H261("video/h261", "h261", "h261"),
	H263("video/h263", "h263", "h263"),
	JPGV("video/jpeg", "jpgv", "jpgv"),
	JPM("video/jpm", "jpm", "jpm", "jpgm"),
	MJ2("video/mj2", "mj2", "mj2", "mjp2"),
	OGV("video/ogg", "ogv", "ogv"),
	DECE_HD("video/vnd.dece.hd", "dece-hd", "uvh", "uvvh"),
	DECE_MOBILE("video/vnd.dece.mobile", "dece-mobile", "uvm", "uvvm"),
	DECE_PD("video/vnd.dece.pd", "dece-pd", "uvp", "uvvp"),
	DECE_SD("video/vnd.dece.sd", "dece-sd", "uvs", "uvvs"),
	DECE_VIDEO("video/vnd.dece.video", "dece-video", "uvv", "uvvv"),
	DVB("video/vnd.dvb.file", "dvb", "dvb"),
	FVT("video/vnd.fvt", "fvt", "fvt"),
	MPEG_URL("video/vnd.mpegurl", "mpeg-url", "mxu", "m4u"),
	PYV("video/vnd.ms-playready.media.pyv", "pyv", "pyv"),
	UVVU_MP4("video/vnd.uvvu.mp4", "uvvu-mp4", "uvu", "uvvu"),
	VIV("video/vnd.vivo", "viv", "viv"),
	WEBM("video/webm", "webm", "webm"),
	F4V("video/x-f4v", "f4v", "f4v"),
	FLI("video/x-fli", "fli", "fli"),
	M4V("video/x-m4v", "m4v", "m4v"),
	MATROSKA("video/x-matroska", "matroska", "mkv", "mk3d", "mks"),
	MICROSOFT_ASF("video/x-ms-asf", "asf", "asf", "asx"),
	VOB("video/x-ms-vob", "vob", "vob"),
	WM("video/x-ms-wm", "wm", "wm"),
	WMV("video/x-ms-wmv", "wmv", "wmv"),
	WMX("video/x-ms-wmx", "wmx", "wmx"),
	WVX("video/x-ms-wvx", "wvx", "wvx"),
	MOVIE("video/x-sgi-movie", "movie", "movie"),
	SMV("video/x-smv", "smv", "smv"),
	ICE("x-conference/x-cooltalk", "ice", "ice"),
	HBS("text/x-handlebars-template", "hbs", "hbs"),
	AMR("audio/amr", "amr", "amr"),

	/** default if no content */
	EMPTY("application/octet-stream", "other"),
	/** default if no specific match to the mime-type */
	OTHER("application/octet-stream", "other"),
	// end
	;

	private final static Map<String, ContentType> mimeTypeMap = new HashMap<String, ContentType>();
	private final static Map<String, ContentType> fileExtensionMap = new HashMap<String, ContentType>();
	private static IanaEntries ianaEntries;

	static {
		for (ContentType type : values()) {
			if (type.mimeType != null) {
				// NOTE: this may overwrite this mapping
				mimeTypeMap.put(type.mimeType.toLowerCase(), type);
			}
			if (type.fileExtensions != null) {
				for (String fileExtension : type.fileExtensions) {
					// NOTE: this may overwrite this mapping
					fileExtensionMap.put(fileExtension, type);
				}
			}
		}
	}

	private final String mimeType;
	private final String simpleName;
	private final String[] fileExtensions;
	private final IanaEntry ianaEntry;

	private ContentType(String mimeType, String simpleName, String... fileExtensions) {
		this.mimeType = mimeType;
		this.simpleName = simpleName;
		this.fileExtensions = fileExtensions;
		this.ianaEntry = findIanaEntryByMimeType(mimeType);
	}

	/**
	 * Get simple name of the type.
	 */
	public String getSimpleName() {
		return simpleName;
	}

	public String getMimeType() {
		return mimeType;
	}

	public String[] getFileExtensions() {
		return fileExtensions;
	}

	/**
	 * Return the type associated with the mime-type string or {@link #OTHER} if not found.
	 */
	public static ContentType fromMimeType(String mimeType) {
		// NOTE: mimeType can be null
		if (mimeType != null) {
			mimeType = mimeType.toLowerCase();
		}
		ContentType type = mimeTypeMap.get(mimeType);
		if (type == null) {
			return OTHER;
		} else {
			return type;
		}

	}

	/**
	 * Return the type associated with the file-extension string or {@link #OTHER} if not found.
	 */
	public static ContentType fromFileExtension(String fileExtension) {
		// NOTE: mimeType can be null
		ContentType type = fileExtensionMap.get(fileExtension.toLowerCase());
		if (type == null) {
			return OTHER;
		} else {
			return type;
		}
	}

	/**
	 * Returns the references of the mime type or null if none.
	 */
	public List<String> getReferences() {
		if (ianaEntry == null) {
			return null;
		} else {
			return ianaEntry.getReferences();
		}
	}

	/**
	 * Returns the URL of the references or null if none.
	 */
	public List<String> getReferenceUrls() {
		if (ianaEntry == null) {
			return null;
		} else {
			return ianaEntry.getReferenceUrls();
		}
	}

	private static IanaEntry findIanaEntryByMimeType(String mimeType) {
		if (ianaEntries == null) {
			ianaEntries = new IanaEntries();
		}
		return ianaEntries.lookupByMimeType(mimeType);
	}
}
