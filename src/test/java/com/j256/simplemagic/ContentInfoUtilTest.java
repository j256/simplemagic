package com.j256.simplemagic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class ContentInfoUtilTest {

	private ContentInfoUtil contentInfoUtil;

	private FileType[] fileTypes =
			new FileType[] { //
					//
					new FileType("/files/x.gif", ContentType.GIF, "gif", "image/gif",
							"GIF image data, version 89a, 32 x 32"),
					new FileType("/files/x.pdf", ContentType.PDF, "pdf", "application/pdf", "PDF document, version 1.3"),
					new FileType("/files/x.png", ContentType.PNG, "png", "image/png",
							"PNG image, 205 x 189, 8-bit/color RGB, non-interlaced"),
					new FileType("/files/x.tiff", ContentType.TIFF, "tiff", "image/tiff", "TIFF image data, big-endian"),
					new FileType("/files/x.zip", ContentType.ZIP, "zip", "application/zip",
							"Zip archive data, at least v1.0 to extract"),
					new FileType("/files/x.javaserial", ContentType.UNKNOWN, "Java", null,
							"Java serialization data, version 5"),
					new FileType("/files/x.doc", ContentType.MICROSOFT_WORD, "word", "application/msword",
							"Microsoft Word Document"),
					new FileType("/files/x.rtf", ContentType.RTF, "rtf", "text/rtf",
							"Rich Text Format data, version 1, unknown character set unknown version"),
					new FileType("/files/x.xml", ContentType.XML, "xml", "application/xml", "XML document text"),
					new FileType("/files/jfif.jpg", ContentType.JPEG, "jpeg", "image/jpeg",
							"JPEG image data, JFIF standard 1.01"),
					// partial file here
					new FileType("/files/exif.jpg", ContentType.JPEG, "jpeg", "image/jpeg",
							"JPEG image data, EXIF standard 2.1"),
					new FileType("/files/x.jp2", ContentType.JPEG_2000, "jp2", "image/jp2", "JPEG 2000 image"),
					new FileType("/files/x.class", ContentType.JAVA_APPLET, "applet", "application/x-java-applet",
							"compiled Java class data, version 49.0"),
					new FileType("/files/x.perl", ContentType.PERL, "perl", "text/x-perl",
							"Perl script text executable"),
					new FileType("/files/x.bz2", ContentType.BZIP2, "bzip2", "application/x-bzip2",
							"bzip2 compressed data, block size = 900k"),
					new FileType("/files/x.gz", ContentType.GZIP, "gzip", "application/x-gzip",
							"gzip compressed data, from Unix, last modified: 2013-05-08 02:57:08 +0000"),
					new FileType("/files/x.m4v", ContentType.MP4, "mp4", "video/mp4",
							"ISO Media, MPEG v4 system, iTunes AVC-LC"),
					new FileType("/files/x.xls", ContentType.UNKNOWN, "OLE", null, "OLE 2 Compound Document"),
					new FileType("/files/x.xlsx", ContentType.ZIP, "zip", "application/zip",
							"Zip archive data, at least v2.0 to extract"),
					new FileType("/files/x.odt", ContentType.UNKNOWN, "OpenDocument",
							"application/vnd.oasis.opendocument.text", "OpenDocument Text"),
					new FileType("/files/x.html", ContentType.HTML, "html", "text/html", "HTML document text"),
					new FileType("/files/x.aiff", ContentType.AIFF, "aiff", "audio/x-aiff", "IFF data, AIFF audio"),
					new FileType("/files/x.mp3", ContentType.AUDIO_MPEG, "mpeg", "audio/mpeg",
							"MPEG ADTS, layer III, v1, 128 kbps, 44.1 kHz, Stereo"),
					new FileType("/files/x.wav", ContentType.WAV, "wav", "audio/x-wav",
							"RIFF (little-endian) data, WAVE audio, Microsoft PCM, 16 bit, stereo 44100 Hz"),
			// end
			};

	@Test
	public void testFiles() throws Exception {
		for (FileType fileType : fileTypes) {
			testFile(fileType);
		}
	}

	// @Test
	// public void testSpecial() throws Exception {
	// ContentInfoUtil util = new ContentInfoUtil("/tmp/x", new ErrorCallBack() {
	// public void error(String line, String details, Exception e) {
	// System.err.println("Error " + details + ": " + line);
	// }
	// });
	// ContentInfo info = contentInfoFromResource(util, "/files/x.gz");
	// System.out.println(type);
	// }

	@Test
	public void testDownloadsDir() throws Exception {
		ContentInfoUtil util = new ContentInfoUtil();
		String homeDir = System.getenv("HOME");
		for (File file : new File(homeDir + "/Downloads").listFiles()) {
			if (file.isFile()) {
				ContentInfo type = util.findMatch(file);
				System.out.println(file + " = " + type);
			}
		}
	}

	private void testFile(FileType fileType) throws IOException {
		if (contentInfoUtil == null) {
			contentInfoUtil = new ContentInfoUtil();
		}
		ContentInfo details = contentInfoFromResource(contentInfoUtil, fileType.fileName);
		if (fileType.expectedName == null) {
			assertNull("expecting the content type of " + fileType.fileName + " to be null", details);
		} else {
			assertNotNull("not expecting the content name of " + fileType.fileName + " to be null", details);
			assertEquals("bad content-type of " + fileType.fileName, fileType.expectedType, details.getContentType());
			assertEquals("bad name of " + fileType.fileName, fileType.expectedName, details.getName());
			assertEquals("bad mime-type of " + fileType.fileName, fileType.expectedMimeType, details.getMimeType());
			assertEquals("bad message for " + fileType.fileName, fileType.expectedMessage, details.getMessage());
		}
	}

	private ContentInfo contentInfoFromResource(ContentInfoUtil util, String resource) throws IOException {
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
		final ContentType expectedType;
		final String expectedName;
		final String expectedMimeType;
		final String expectedMessage;
		private FileType(String fileName, ContentType expectedType, String expectedName, String expectedMimeType,
				String description) {
			this.fileName = fileName;
			this.expectedType = expectedType;
			this.expectedName = expectedName;
			this.expectedMimeType = expectedMimeType;
			this.expectedMessage = description;
		}
	}
}
