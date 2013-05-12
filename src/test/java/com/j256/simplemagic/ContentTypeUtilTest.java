package com.j256.simplemagic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class ContentTypeUtilTest {

	private ContentTypeUtil contentTypeUtil;

	private FileType[] fileTypes =
			new FileType[] { //
					//
					new FileType("/files/x.gif", "GIF", "image/gif", "GIF image data, version 89a, 32 x 32"),
					new FileType("/files/x.pdf", "PDF", "application/pdf", "PDF document, version 1.3"),
					new FileType("/files/x.png", "PNG", "image/png",
							"PNG image, 205 x 189, 8-bit/color RGB, non-interlaced"),
					new FileType("/files/x.tiff", "TIFF", "image/tiff", "TIFF image data, big-endian"),
					new FileType("/files/x.zip", "Zip", "application/zip", "Zip archive data, at least v1.0 to extract"),
					new FileType("/files/x.javaserial", "Java", null, "Java serialization data, version 5"),
					new FileType("/files/x.doc", "Microsoft", "application/msword", "Microsoft Word Document"),
					new FileType("/files/x.rtf", "Rich", "text/rtf",
							"Rich Text Format data, version 1, unknown character set unknown version"),
					new FileType("/files/x.xml", "XML", "application/xml", "XML document text"),
					new FileType("/files/jfif.jpg", "JPEG", "image/jpeg", "JPEG image data, JFIF standard 1.01"),
					// partial file here
					// new FileType("/files/exif.jpg", "JPEG", null, "JPEG image data, EXIF standard "),
					new FileType("/files/x.jp2", "JPEG", "image/jp2", "JPEG 2000 image"),
					new FileType("/files/x.class", "compiled", "application/x-java-applet",
							"compiled Java class data, version 49.0"),
					new FileType("/files/x.perl", "Perl", "text/x-perl", "Perl script text executable"),
					new FileType("/files/x.bz2", "bzip2", "application/x-bzip2",
							"bzip2 compressed data, block size = 900k"),
					new FileType("/files/x.gz", "gzip", "application/x-gzip",
							"gzip compressed data, from Unix, last modified: 2013-05-08 02:57:08 +0000"),
					new FileType("/files/x.m4v", "ISO", "video/mp4", "ISO Media, MPEG v4 system, iTunes AVC-LC"),
					new FileType("/files/x.xls", "OLE", null, "OLE 2 Compound Document"),
					new FileType("/files/x.xlsx", "Zip", "application/zip",
							"Zip archive data, at least v2.0 to extract"),
					new FileType("/files/x.odt", "OpenDocument", "application/vnd.oasis.opendocument.text",
							"OpenDocument Text"),
					new FileType("/files/x.html", "HTML", "text/html", "HTML document text"),
					new FileType("/files/x.aiff", "IFF", "audio/x-aiff", "IFF data, AIFF audio"),
					new FileType("/files/x.mp3", "MPEG", "audio/mpeg",
							"MPEG ADTS, layer III, v1, 128 kbps, 44.1 kHz, Stereo"),
					new FileType("/files/x.wav", "RIFF", "audio/x-wav",
							"RIFF (little-endian) data, WAVE audio, Microsoft PCM, 16 bit, stereo 44100 Hz"),
			// end
			};

	@Test
	public void testFiles() throws Exception {
		for (FileType fileType : fileTypes) {
			testFile(fileType);
		}
	}

	@Test
	public void testSpecial() throws Exception {
		// ContentTypeUtil util = new ContentTypeUtil(new File("/tmp/x"));
		ContentTypeUtil util = new ContentTypeUtil();
		ContentType type = contentTypeFromResource(util, "/files/exif.jpg");
		System.out.println(type);
	}

	@Test
	public void testDownloadsDir() throws Exception {
		ContentTypeUtil util = new ContentTypeUtil();
		String homeDir = System.getenv("HOME");
		for (File file : new File(homeDir + "/Downloads").listFiles()) {
			if (file.isFile()) {
				ContentType type = util.findMatch(file);
				System.out.println(file + " = " + type);
			}
		}
	}

	private void testFile(FileType fileType) throws IOException {
		if (contentTypeUtil == null) {
			contentTypeUtil = new ContentTypeUtil();
		}
		ContentType type = contentTypeFromResource(contentTypeUtil, fileType.fileName);
		if (fileType.expectedName == null) {
			assertNull("expecting the content type of " + fileType.fileName + " to be null", type);
		} else {
			assertNotNull("not expecting the content type of " + fileType.fileName + " to be null", type);
			assertEquals("bad name of " + fileType.fileName, fileType.expectedName, type.getName());
			assertEquals("bad mime-type of " + fileType.fileName, fileType.expectedMimeType, type.getMimeType());
			assertEquals("bad message for " + fileType.fileName, fileType.expectedMessage, type.getMessage());
		}
	}

	private ContentType contentTypeFromResource(ContentTypeUtil util, String resource) throws IOException {
		InputStream stream = getClass().getResourceAsStream(resource);
		assertNotNull("Could not file resource: " + resource, stream);
		try {
			return util.findMatch(stream);
		} finally {
			stream.close();
		}
	}

	private static class FileType {
		final String fileName;
		final String expectedName;
		final String expectedMimeType;
		final String expectedMessage;
		private FileType(String fileName, String expectedName, String expectedMimeType, String description) {
			this.fileName = fileName;
			this.expectedName = expectedName;
			this.expectedMimeType = expectedMimeType;
			this.expectedMessage = description;
		}
	}
}
