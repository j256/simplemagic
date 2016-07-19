/*
 * Copyright Rob Stryker and Contributors (as per source history)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.j256.simplemagic.integration;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;

import junit.framework.TestCase;

public abstract class AbstractMagicTest extends TestCase {

	protected void runMagicAndFileTestOp(
			String magicFileContents, byte[] fileToTest,
			String expectedResult) throws IOException {

		
		ContentInfoUtil contentInfoUtil = new ContentInfoUtil(new ByteArrayInputStream(magicFileContents.getBytes()));
		ContentInfo result = contentInfoUtil.findMatch(fileToTest);
		String msg = result.getMessage();
		System.out.println(msg);
		assertEquals(expectedResult, msg);

	}
	
	protected void runMagicAndFileTestOpNullMatch(
			String magicFileContents, byte[] fileToTest) throws IOException {

		ContentInfoUtil contentInfoUtil = new ContentInfoUtil(new ByteArrayInputStream(magicFileContents.getBytes()));
		ContentInfo result = contentInfoUtil.findMatch(fileToTest);
		assertNull(result);
	}

	public static byte[] hex2Bytes(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}
	

	public static byte[] string2ASCII(String str) {
		char[] buffer = str.toCharArray();
		byte[] b = new byte[buffer.length];
		for (int i = 0; i < b.length; i++) {
			b[i] = (byte) buffer[i];
		}
		return b;
	}
	public static byte[] combine( byte[][] arr) {
		int total = 0;
		for( int i = 0; i < arr.length; i++ ) {
			total += arr[i].length;
		}
		byte[] ret = new byte[total];
		int count = 0;
		for( int i = 0; i < arr.length; i++ ) {
			for( int j = 0; j < arr[i].length; j++ ) {
				ret[count++] = arr[i][j];
			}
		}
		return ret;
	}
}
