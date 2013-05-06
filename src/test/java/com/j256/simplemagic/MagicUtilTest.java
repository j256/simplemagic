package com.j256.simplemagic;

import java.io.File;

import org.junit.Test;

public class MagicUtilTest {

	@Test
	public void testJpeg() throws Exception {
		MagicUtil magicUtil = new MagicUtil(new File("/usr/share/file/magic/jpeg"));
		ContentType type = magicUtil.contentTypeOfFile("/Users/graywatson/Downloads/norwichtrip.jpg");
		System.out.println(type);
	}

	@Test
	public void testJpegJfif() throws Exception {
		MagicUtil magicUtil = new MagicUtil(new File("/usr/share/file/magic/jpeg"));
		ContentType type = magicUtil.contentTypeOfFile("/Users/graywatson/Downloads/CKC_042-XL.jpg");
		System.out.println(type);
	}

	@Test
	public void testZip() throws Exception {
		MagicUtil magicUtil = new MagicUtil(new File("/usr/share/file/magic/archive"));
		ContentType type = magicUtil.contentTypeOfFile("/Users/graywatson/Downloads/svn2git-master.zip");
		System.out.println(type);
	}

	@Test
	public void testGif() throws Exception {
		MagicUtil magicUtil = new MagicUtil(new File("/usr/share/file/magic/images"));
		ContentType type = magicUtil.contentTypeOfFile("/Users/graywatson/Downloads/spinning_wheel.gif");
		System.out.println(type);
	}

	@Test
	public void testPdf() throws Exception {
		MagicUtil magicUtil = new MagicUtil(new File("/usr/share/file/magic/pdf"));
		ContentType type = magicUtil.contentTypeOfFile("/Users/graywatson/Downloads/2013-03-04 Pct 2.pdf");
		System.out.println(type);
	}

	@Test
	public void testPng() throws Exception {
		MagicUtil magicUtil = new MagicUtil(new File("/usr/share/file/magic/images"));
		ContentType type = magicUtil.contentTypeOfFile("/Users/graywatson/Downloads/US6012053.png");
		System.out.println(type);
	}

	@Test
	public void testDownloads() throws Exception {
		MagicUtil magicUtil = new MagicUtil();
		for (File file : new File("/Users/graywatson/Downloads").listFiles()) {
			if (file.isFile()) {
				ContentType type = magicUtil.contentTypeOfFile(file);
				System.out.println(file + " = " + type);
			}
		}
	}
}
