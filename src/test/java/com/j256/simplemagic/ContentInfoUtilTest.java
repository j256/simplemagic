package com.j256.simplemagic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

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
					new FileType("/files/x.javaserial", ContentType.OTHER, "Java", null,
							"Java serialization data, version 5"),
					new FileType("/files/x.doc", ContentType.MICROSOFT_WORD, "word", "application/msword",
							"Microsoft Office Document Microsoft Word Document"),
					new FileType("/files/x.docx", ContentType.MICROSOFT_WORD_XML, "word",
							"application/vnd.openxmlformats-officedocument.wordprocessingml.document",
							"Microsoft Word 2007+"),
					new FileType("/files/x.rtf", ContentType.RTF, "rtf", "text/rtf",
							"Rich Text Format data, version 1, unknown character set unknown version"),
					new FileType("/files/x.xml", ContentType.XML, "xml", "application/xml", "XML document text"),
					new FileType("/files/jfif.jpg", ContentType.JPEG, "jpeg", "image/jpeg",
							"JPEG image data, JFIF standard 1.01"),
					// partial file here
					new FileType("/files/exif.jpg", ContentType.JPEG, "jpeg", "image/jpeg",
							"JPEG image data, EXIF standard 2.1"),
					new FileType("/files/exif2.jpg", ContentType.JPEG, "jpeg", "image/jpeg",
							"JPEG image data, EXIF standard"),
					new FileType("/files/x.jp2", ContentType.JPEG_2000, "jp2", "image/jp2", "JPEG 2000 image"),
					new FileType("/files/x.class", ContentType.JAVA_APPLET, "applet", "application/x-java-applet",
							"compiled Java class data, version 49.0"),
					new FileType("/files/x.perl", ContentType.PERL, "perl", "text/x-perl",
							"Perl script text executable"),
					new FileType("/files/x.bz2", ContentType.BZIP2, "bzip2", "application/x-bzip2",
							"bzip2 compressed data, block size = 900k"),
					new FileType("/files/x.gz", ContentType.GZIP, "gzip", "application/x-gzip",
							"gzip compressed data, from Unix, last modified: 2013-05-08 02:57:08 +0000"),
					new FileType("/files/x.m4v", ContentType.MP4A, "mp4a", "video/mp4",
							"ISO Media, MPEG v4 system, iTunes AVC-LC"),
					new FileType("/files/x.xls", ContentType.OTHER, "OLE", null, "OLE 2 Compound Document"),
					new FileType("/files/x.xlsx", ContentType.MICROSOFT_EXCEL_XML, "excel",
							"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
							"Microsoft Excel 2007+"),
					new FileType("/files/x.odt", ContentType.OPENDOCUMENT_TEXT, "opendocument-text",
							"application/vnd.oasis.opendocument.text", "OpenDocument Text"),
					new FileType("/files/x.html", ContentType.HTML, "html", "text/html", "HTML document text"),
					new FileType("/files/x.aiff", ContentType.AIFF, "aiff", "audio/x-aiff", "IFF data, AIFF audio"),
					new FileType("/files/x.mp3", ContentType.AUDIO_MPEG, "mpeg", "audio/mpeg",
							"MPEG ADTS, layer III, v1, 128 kbps, 44.1 kHz, Stereo"),
					new FileType("/files/x.wav", ContentType.WAV, "wav", "audio/x-wav",
							"RIFF (little-endian) data, WAVE audio, Microsoft PCM, 16 bit, stereo 44100 Hz"),
					// NOTE: this seems to be somewhat valid since powerpoint uses this format
					new FileType("/files/x.ppt", ContentType.OTHER, "OLE", null, "OLE 2 Compound Document"),
					new FileType("/files/x.pptx", ContentType.MICROSOFT_POWERPOINT_XML, "powerpoint",
							"application/vnd.openxmlformats-officedocument.presentationml.presentation",
							"Microsoft PowerPoint 2007+"),
					new FileType("/files/x.7z", ContentType.SEVEN_Z, "7zip", "application/x-7z-compressed",
							"7-zip archive data, version 0.3"),
					// truncated file
					new FileType("/files/x.nuv", ContentType.OTHER, "MythTV", null,
							"MythTV NuppelVideo v (640x480),progressive,aspect:1.00,fps:29.97"),
					new FileType("/files/x.webp", ContentType.WEBP, "webp", "image/webp",
							"RIFF (little-endian) data, WEBP image"),
					
					// This test fails. See comments on https://github.com/j256/simplemagic/issues/5
//					new FileType("/files/x.exe", ContentType.OTHER, "MythTV", null,
//							"MythTV NuppelVideo v (640x480),progressive,aspect:1.00,fps:29.97"),
			// end
			};

	@Test
	public void testFiles() throws Exception {
		for (FileType fileType : fileTypes) {
			testFile(getContentInfoUtil(), fileType);
		}
	}

	@Test
	public void testGif() throws Exception {
		testFile(getContentInfoUtil(), new FileType("/files/x.gif", ContentType.GIF, "gif", "image/gif",
				"GIF image data, version 89a, 32 x 32"));
	}

	@Test
	public void testExtensions() {
		assertNull(ContentInfoUtil.findExtensionMatch("hello"));
		assertEquals(ContentType.HTML, ContentInfoUtil.findExtensionMatch("html").getContentType());
		assertEquals(ContentType.HTML, ContentInfoUtil.findExtensionMatch("INDEX.HTM").getContentType());
	}

	@Test
	public void testMimeType() {
		assertNull(ContentInfoUtil.findMimeTypeMatch("something/foo"));
		assertEquals(ContentType.HTML, ContentInfoUtil.findMimeTypeMatch("text/html").getContentType());
		assertEquals(ContentType.HTML, ContentInfoUtil.findMimeTypeMatch("TEXT/HTML").getContentType());
	}

	@Test
	public void testLocalDownloadsDir() throws Exception {
		ContentInfoUtil util = new ContentInfoUtil();
		String homeDir = System.getenv("HOME");
		File downloadDir = new File(homeDir + "/Downloads");
		if (!downloadDir.isDirectory()) {
			return;
		}
		for (File file : downloadDir.listFiles()) {
			if (file.isFile()) {
				util.findMatch(file);
			}
		}
	}

	@Test
	public void testMultipleMagicFiles() throws Exception {
		ContentInfoUtil util = new ContentInfoUtil(new File("target/test-classes/magicFiles"));
		testFile(util, new FileType("/files/x.gif", ContentType.GIF, "gif", "image/gif",
				"GIF image data, version 89a, 32 x 32"));
		testFile(util, new FileType("/files/jfif.jpg", ContentType.JPEG, "jpeg", "image/jpeg",
				"JPEG image data, JFIF standard 1.01"));
	}

	@Test
	public void testPerformanceRun() throws Exception {
		ContentInfoUtil util = getContentInfoUtil();
		for (int i = 0; i < 100; i++) {
			for (FileType fileType : fileTypes) {
				testFile(util, fileType);
			}
		}
	}

	@Test(expected = FileNotFoundException.class)
	public void testMagicNotFound() throws Exception {
		new ContentInfoUtil("some-unknown-resource", null);
	}

	private void testFile(ContentInfoUtil contentInfoUtil, FileType fileType) throws IOException {
		ContentInfo details = contentInfoFromResource(contentInfoUtil, fileType.fileName);
		checkInfo(fileType, details);
		details = contentInfoFromStreamWrapper(contentInfoUtil, fileType.fileName);
		checkInfo(fileType, details);
	}

	private ContentInfoUtil getContentInfoUtil() {
		if (contentInfoUtil == null) {
			contentInfoUtil = new ContentInfoUtil();
		}
		return contentInfoUtil;
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

	private ContentInfo contentInfoFromStreamWrapper(ContentInfoUtil util, String resource) throws IOException {
		InputStream resourceStream = getClass().getResourceAsStream(resource);
		ByteArrayOutputStream outputStream;
		try {
			outputStream = new ByteArrayOutputStream();
			copyStream(resourceStream, outputStream);
		} finally {
			resourceStream.close();
		}
		byte[] resourceBytes = outputStream.toByteArray();

		ByteArrayInputStream inputSteam = new ByteArrayInputStream(resourceBytes);
		ContentInfoInputStreamWrapper wrappedStream = new ContentInfoInputStreamWrapper(inputSteam, util);
		ByteArrayOutputStream checkOutputStream = new ByteArrayOutputStream();
		copyStream(wrappedStream, checkOutputStream);

		assertTrue(Arrays.equals(resourceBytes, checkOutputStream.toByteArray()));
		try {
			return wrappedStream.findMatch();
		} finally {
			resourceStream.close();
		}
	}

	private void checkInfo(FileType fileType, ContentInfo details) {
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

	private void copyStream(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte[1024];
		while (true) {
			int numRead = input.read(buffer);
			if (numRead < 0) {
				return;
			}
			output.write(buffer, 0, numRead);
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
