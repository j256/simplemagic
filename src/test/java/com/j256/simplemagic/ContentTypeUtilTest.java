package com.j256.simplemagic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class ContentTypeUtilTest {

	private ContentTypeUtil contentTypeUtil = new ContentTypeUtil();

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
			// NEED: jpeg (JFIF and Exif), java code, c code, perl code, c++ code, javascript, html, excel,
			// open-office, text-plain
			};

	@Test
	public void testFiles() throws Exception {
		for (FileType fileType : fileTypes) {
			testFile(fileType);
		}
	}

	@Test
	public void testXml() throws Exception {
		ContentTypeUtil util = new ContentTypeUtil(new File("/tmp/x"));
		ContentType type = contentTypeFromResource(util, "/files/x.xml");
		System.out.println(type);
	}

	@Test
	public void testJpeg() throws Exception {
		ContentTypeUtil util = new ContentTypeUtil(new File("/usr/share/file/magic/jpeg"));
		ContentType type = util.findMatch("/Users/graywatson/Downloads/norwichtrip.jpg");
		System.out.println(type);
	}

	@Test
	public void testJpegJfif() throws Exception {
		ContentTypeUtil util = new ContentTypeUtil(new File("/usr/share/file/magic/jpeg"));
		ContentType type = util.findMatch("/Users/graywatson/Downloads/CKC_042-XL.jpg");
		System.out.println(type);
	}

	@Test
	public void testDownloadsDir() throws Exception {
		ContentTypeUtil util = new ContentTypeUtil();
		for (File file : new File("/Users/graywatson/Downloads").listFiles()) {
			if (file.isFile()) {
				ContentType type = util.findMatch(file);
				System.out.println(file + " = " + type);
			}
		}
	}

	private void testFile(FileType fileType) throws IOException {
		ContentType type = contentTypeFromResource(contentTypeUtil, fileType.fileName);
		if (fileType.expectedName == null) {
			assertNull("expecting the content type of " + fileType.fileName + " to be null", type);
		} else {
			assertNotNull("not expecting the content type of " + fileType.fileName + " to be null", type);
			assertEquals(fileType.expectedName, type.getName());
			assertEquals(fileType.expectedMimeType, type.getMimeType());
			assertEquals(fileType.expectedMessage, type.getMessage());
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
