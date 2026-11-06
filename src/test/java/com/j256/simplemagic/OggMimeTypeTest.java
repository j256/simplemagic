package com.j256.simplemagic;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.File;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class OggMimeTypeTest {

	@Test
	public void vorbisBytesUseAudioMimeType() throws IOException {
		assertVorbis(new ContentInfoUtil().findMatch(vorbisHeader()));
	}

	@Test
	public void vorbisStreamUsesAudioMimeType() throws IOException {
		assertVorbis(new ContentInfoUtil().findMatch(new ByteArrayInputStream(vorbisHeader())));
	}

	@Test
	public void plainTextRulesUseAudioMimeType() throws IOException {
		assertVorbis(new ContentInfoUtil(new File("src/test/resources/magic")).findMatch(vorbisHeader()));
	}

	@Test
	public void audioMimeTypeHasKnownContentMetadata() {
		ContentInfo info = ContentInfoUtil.findMimeTypeMatch("audio/ogg");
		assertNotNull(info);
		assertEquals("OGA", info.getContentType().name());
		assertArrayEquals(new String[] { "oga", "ogg", "spx" }, info.getFileExtensions());
	}

	@Test
	public void unidentifiedOggKeepsGenericMimeType() throws IOException {
		ContentInfo info = new ContentInfoUtil().findMatch(oggHeader("unknown"));
		assertEquals("application/ogg", info.getMimeType());
		assertEquals(ContentType.OGG, info.getContentType());
	}

	@Test
	public void theoraAndUnknownRevisionKeepExistingFallback() throws IOException {
		ContentInfoUtil util = new ContentInfoUtil();
		assertEquals("application/ogg", util.findMatch(oggHeader("\200theora")).getMimeType());
		byte[] unknownRevision = vorbisHeader();
		unknownRevision[4] = 1;
		assertEquals("application/ogg", util.findMatch(unknownRevision).getMimeType());
	}

	@Test
	public void extensionOnlyLookupKeepsExistingContainerType() {
		assertEquals(ContentType.OGG, ContentType.fromFileExtension("ogg"));
		assertEquals(ContentType.OGG, ContentType.fromFileExtension("oga"));
		assertEquals(ContentType.OGG, ContentType.fromFileExtension("spx"));
	}

	private void assertVorbis(ContentInfo info) {
		assertEquals("audio/ogg", info.getMimeType());
		assertEquals("OGA", info.getContentType().name());
		assertArrayEquals(new String[] { "oga", "ogg", "spx" }, info.getFileExtensions());
		assertTrue(info.getMessage().contains("Vorbis audio"));
	}

	private byte[] vorbisHeader() {
		byte[] bytes = oggHeader("\001vorbis");
		bytes[39] = 2; // channels
		bytes[40] = (byte) 0x80;
		bytes[41] = (byte) 0xbb; // 48000 Hz, little endian
		return bytes;
	}

	private byte[] oggHeader(String codec) {
		// A synthetic identification-page prefix, not copied audio data.
		byte[] bytes = new byte[128];
		System.arraycopy(new byte[] { 'O', 'g', 'g', 'S' }, 0, bytes, 0, 4);
		bytes[5] = 2; // beginning of stream
		bytes[26] = 1; // one segment
		bytes[27] = 30;
		byte[] signature = codec.getBytes(StandardCharsets.ISO_8859_1);
		System.arraycopy(signature, 0, bytes, 28, signature.length);
		return bytes;
	}
}
