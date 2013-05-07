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

	private FileType[] fileTypes = new FileType[] { //
			//
			// new FileType("/files/x.gif", "GIF", "GIF image data, version 89a, 32 x 32"),
			// new FileType("/files/x.pdf", "PDF", "PDF document, version 1.3"),
			// new FileType("/files/x.png", "PNG", "PNG image, 205 x 189, 8-bit/color RGB, non-interlaced"),
			// new FileType("/files/x.tiff", "TIFF", "TIFF image data, big-endian"),
			// new FileType("/files/x.zip", "Zip", "Zip archive data, at least v1.0 to extract"),
			// new FileType("/files/x.javaserial", "Java", "Java serialization data, version 5"),
			// new FileType("/files/x.doc", "Microsoft", "Microsoft Word Document"),
			// new FileType("/files/x.rtf", "Rich",
			// "Rich Text Format data, version 1, unknown character set unknown version"),
			new FileType("/files/x.xml", "XML", "XML document text"),
			//
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
		final String expectedMessage;
		private FileType(String fileName, String expectedType, String description) {
			this.fileName = fileName;
			this.expectedName = expectedType;
			this.expectedMessage = description;
		}
	}
}
