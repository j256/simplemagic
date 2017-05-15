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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Run tests from a real magic file
 */
public class IntegrationTest extends AbstractMagicTest {
	public void testRiscOSChunk() throws IOException {
		String magicFile = "0\tlelong\t0xc3cbc6c5\tRISC OS Chunk data";
		byte[] fileToTest = hex2Bytes("C5C6CbC3");
		String expectedResults = "RISC OS Chunk data";
		runMagicAndFileTestOp(magicFile, fileToTest, expectedResults);
	}
	
	public void testRiscOSChunkAOF() throws IOException {
		String magicFile = "0\tlelong\t0xc3cbc6c5\tRISC OS Chunk data\n"
				+ ">12 string OBJ_ \\b, AOF object";

		byte[] hex = hex2Bytes("C5C6CbC3");
		byte[] objpart = string2ASCII("OBJ_\0aaaaaaa");
		byte[] fileToTest = combine(new byte[][] { hex, hex, hex, objpart});

		String expectedResults = "RISC OS Chunk data, AOF object";
		runMagicAndFileTestOp(magicFile, fileToTest, expectedResults);
	}

	public void testRiscOSChunkALF() throws IOException {
		String magicFile = "0\tlelong\t0xc3cbc6c5\tRISC OS Chunk data\n"
				+ ">12 string OBJ_ \\b, AOF object\n"
				+ ">12 string LIB_ \\b, ALF object";

		byte[] hex = hex2Bytes("C5C6CbC3");
		byte[] objpart = string2ASCII("LIB_\0aaaaaaa");
		byte[] fileToTest = combine(new byte[][] { hex, hex, hex, objpart});

		String expectedResults = "RISC OS Chunk data, ALF object";
		runMagicAndFileTestOp(magicFile, fileToTest, expectedResults);
	}


	public void testRiscOSCAIFExec() throws IOException {
		String magicFile = "16 lelong 0xef000011 RISC OS AIF executable";

		byte[] filler = hex2Bytes("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		byte[] objpart = hex2Bytes("110000EF");
		byte[] fileToTest = combine(new byte[][] { filler, objpart});

		String expectedResults = "RISC OS AIF executable";
		runMagicAndFileTestOp(magicFile, fileToTest, expectedResults);
	}
	
	public void testRiscOSDraw() throws IOException {
		String magicFile = "0 string Draw RISC OS Draw file data";
		byte[] fileToTest = string2ASCII("Draw_____________");
		String expectedResults = "RISC OS Draw file data";
		runMagicAndFileTestOp(magicFile, fileToTest, expectedResults);
	}
	
	public void testRiscOSFont0() throws IOException {
		String magicFile = "0 string FONT\\0 RISC OS outline font data,\n"
				+ ">5 byte x version %d";
		byte[] filler = string2ASCII("FONT");
		byte[] objpart = hex2Bytes("0006");
		byte[] fileToTest = combine(new byte[][] { filler, objpart});
		String expectedResults = "RISC OS outline font data, version 6";
		runMagicAndFileTestOp(magicFile, fileToTest, expectedResults);
	}

	public void testRiscOSFont1() throws IOException {
		String magicFile = "0 string FONT\\1 RISC OS 1bpp font data,\n"
				+ ">5 byte x version %d";
		byte[] filler = string2ASCII("FONT");
		byte[] objpart = hex2Bytes("0106");
		byte[] fileToTest = combine(new byte[][] { filler, objpart});
		String expectedResults = "RISC OS 1bpp font data, version 6";
		runMagicAndFileTestOp(magicFile, fileToTest, expectedResults);
	}

	public void testRiscOSFont4() throws IOException {
		String magicFile = "0 string FONT\\4 RISC OS 4bpp font data,\n"
				+ ">5 byte x version %d";
		byte[] filler = string2ASCII("FONT");
		byte[] objpart = hex2Bytes("0406");
		byte[] fileToTest = combine(new byte[][] { filler, objpart});
		String expectedResults = "RISC OS 4bpp font data, version 6";
		runMagicAndFileTestOp(magicFile, fileToTest, expectedResults);
	}

	public void testRiscOSFont1FailsFont4() throws IOException {
		String magicFile = "0 string FONT\\1 RISC OS 1bpp font data,\n"
				+ ">5 byte x version %d";
		byte[] filler = string2ASCII("FONT");
		byte[] objpart = hex2Bytes("0406");
		byte[] fileToTest = combine(new byte[][] { filler, objpart});
		runMagicAndFileTestOpNullMatch(magicFile, fileToTest);
	}

	public void testRiscOSMusicFile() throws IOException {
		String magicFile = "0 string Maestro\\r RISC OS music file\n"
				+ ">8 byte x version %d\n"
				+ ">8 byte x type %d";

		byte[] filler = string2ASCII("Maestro\r");
		byte[] objpart = hex2Bytes("04aaaaaaaa");
		byte[] fileToTest = combine(new byte[][] { filler, objpart});
		String expectedResults = "RISC OS music file version 4 type 4";
		runMagicAndFileTestOp(magicFile, fileToTest, expectedResults);
	}

	
	/*
	 * The magic definition for the next few tests
	 */
	private String digitalSymphonyMagicDefinition() {
		return new String(
		"0               string  \\x02\\x01\\x13\\x13\\x13\\x01\\x0d\\x10        Digital Symphony sound sample (RISC OS),\n" +
		">8              byte    x       version %d,\n" +
		">9              pstring x       named \"%s\",\n" +
		">(9.b+19)       byte    =0      8-bit logarithmic\n" +
		">(9.b+19)       byte    =1      LZW-compressed linear\n" +
		">(9.b+19)       byte    =2      8-bit linear signed\n" +
		">(9.b+19)       byte    =3      16-bit linear signed\n" +
		">(9.b+19)       byte    =4      SigmaDelta-compressed linear\n" +
		">(9.b+19)       byte    =5      SigmaDelta-compressed logarithmic\n" +
		">(9.b+19)       byte    >5      unknown format");

	}
	
	/*
	 * These test pstring
	 */
	
	/*
	 * printing name in pstring, and tests a dereferenced byte
	 */
	public void testDigitalSymphony1() throws IOException {
		String magicFile = digitalSymphonyMagicDefinition();
		byte[] objpart = hex2Bytes("0201131313010d100505");
		byte[] filler = string2ASCII("TESTING");
		byte[] fileToTest = combine(new byte[][] { objpart, filler});
		
		String expectedResults = 
				"Digital Symphony sound sample (RISC OS), version 5, named \"TESTI\",";
		runMagicAndFileTestOp(magicFile, fileToTest, expectedResults);
	}

	/*
	 * Increasing size of pstring increases the string being printed
	 */
	public void testDigitalSymphony1a() throws IOException {
		String magicFile = digitalSymphonyMagicDefinition();
		byte[] objpart = hex2Bytes("0201131313010d100507");
		byte[] filler = string2ASCII("TESTING");
		byte[] fileToTest = combine(new byte[][] { objpart, filler});
		String expectedResults = 
				"Digital Symphony sound sample (RISC OS), version 5, named \"TESTING\",";
		runMagicAndFileTestOp(magicFile, fileToTest, expectedResults);
	}

	
	
	public void testDigitalSymphony2() throws IOException {
		String magicFile = digitalSymphonyMagicDefinition();
		//           magic key 7-bytes    |vers | pstring |    9 byte unknown 
		String hex = "0201131313010d10" + "05" + "027272" + "aaaaaaaaaaaaaaaaaa" + "04";
		byte[] fileToTest = hex2Bytes(hex);
		String expectedResults = 
				"Digital Symphony sound sample (RISC OS), version 5, " 
						+ "named \"rr\", SigmaDelta-compressed linear";
		runMagicAndFileTestOp(magicFile, fileToTest, expectedResults);
	}

	public void testDigitalSymphony3() throws IOException {
		String magicFile = digitalSymphonyMagicDefinition();
		//           magic key 7-bytes    |vers | pstring |    9 byte unknown 
		String hex = "0201131313010d10" + "05" + "0C726f62206973206772656174" + "aaaaaaaaaaaaaaaaaa" + "04";
		byte[] fileToTest = hex2Bytes(hex);
		String expectedResults = 
				"Digital Symphony sound sample (RISC OS), version 5, " 
						+ "named \"rob is great\", SigmaDelta-compressed linear";
		runMagicAndFileTestOp(magicFile, fileToTest, expectedResults);
	}

	/*
	 * Tests greater-than comparison
	 */
	public void testDigitalSymphony4() throws IOException {
		String magicFile = digitalSymphonyMagicDefinition();
		//           magic key 7-bytes    |vers | pstring |    9 byte unknown 
		String hex = "0201131313010d10" + "05" + "0C726f62206973206772656174" + "aaaaaaaaaaaaaaaaaa" + "07";
		byte[] fileToTest = hex2Bytes(hex);
		String expectedResults = 
				"Digital Symphony sound sample (RISC OS), version 5, " 
						+ "named \"rob is great\", unknown format";
		runMagicAndFileTestOp(magicFile, fileToTest, expectedResults);
	}
	
	private String digitalSymphonySongDefinition() {
		String ret = 
		"0       string  \\x02\\x01\\x13\\x13\\x14\\x12\\x01\\x0b        Digital Symphony song (RISC OS),\n" +
		">8      byte    x       version %d,\n" +
		">9      byte    =1      1 voice,\n" +
		">9      byte    !1      %d voices,\n" +
		">10     leshort =1      1 track,\n" +
		">10     leshort !1      %d tracks,\n" +
		">12     leshort =1      1 pattern\n" +
		">12     leshort !1      %d patterns\n";
		return ret;
	}
	
	public void testDigitalSymphonySong() throws IOException {
		String magicFile = digitalSymphonySongDefinition();
		//           magic key 7-bytes    |vers | voice | track | patterns 
		String hex = "020113131412010b" + "05" + "03" + "0300" + "0300";
		byte[] fileToTest = hex2Bytes(hex);
		String expectedResults = 
				"Digital Symphony song (RISC OS), version 5, 3 voices, 3 tracks, 3 patterns"; 
		runMagicAndFileTestOp(magicFile, fileToTest, expectedResults);
	}

	

}
