package com.j256.simplemagic.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;

public class LongTypeTest {

	@Test
	public void testRiscOsChunk() throws IOException {
		String name = "jpwefjepwjfepfjowef";
		String hexBytes = "c3cbc6c5";
		String magicFile = "0\tlelong\t0x" + hexBytes + "\t" + name + "\n";
		ContentInfoUtil util = new ContentInfoUtil(new StringReader(magicFile));
		ContentInfo info = util.findMatch(hexToBytes(hexBytes));
		assertNotNull(info);
		assertEquals(name, info.getName());
	}

	private byte[] hexToBytes(String hexString) {
		int len = hexString.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
					+ Character.digit(hexString.charAt(i + 1), 16));
		}
		return data;
	}
}
