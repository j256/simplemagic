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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class NumericComparisonTest extends AbstractMagicTest {
	
	public void testNotEqualComparisonByte() throws IOException {
		String magicFile = "0 byte !0 match";
		byte[] objpart = hex2Bytes("0106");
		runMagicAndFileTestOp(magicFile, objpart, "match");
		
		objpart = hex2Bytes("0006");
		runMagicAndFileTestOpNullMatch(magicFile, objpart);
	}

	public void testGTComparisonByte() throws IOException {
		String magicFile = "0 byte >5 match";
		byte[] objpart = hex2Bytes("0606");
		runMagicAndFileTestOp(magicFile, objpart, "match");
		
		objpart = hex2Bytes("0306");
		runMagicAndFileTestOpNullMatch(magicFile, objpart);
	}

	public void testLTComparisonByte() throws IOException {
		String magicFile = "0 byte <5 match";
		byte[] objpart = hex2Bytes("0206");
		runMagicAndFileTestOp(magicFile, objpart, "match");
		
		objpart = hex2Bytes("0806");
		runMagicAndFileTestOpNullMatch(magicFile, objpart);
	}

	public void testANDComparisonByte() throws IOException {
		String magicFile = "0 byte &3 match";
		byte[] objpart = hex2Bytes("0706");
		runMagicAndFileTestOp(magicFile, objpart, "match");
		
		objpart = hex2Bytes("0806");
		runMagicAndFileTestOpNullMatch(magicFile, objpart);
	}

	public void testXORComparisonByte() throws IOException {
		String magicFile = "0 byte ^0x24 match";
		byte[] objpart = hex2Bytes("CA");
		runMagicAndFileTestOp(magicFile, objpart, "match");
		
		objpart = hex2Bytes("CE");
		runMagicAndFileTestOpNullMatch(magicFile, objpart);
	}

	public void testNegateComparisonByte() throws IOException {
		String magicFile = "0 byte ~3 match";
		byte[] objpart = hex2Bytes("FC");
		runMagicAndFileTestOp(magicFile, objpart, "match");
		
		objpart = hex2Bytes("FF");
		runMagicAndFileTestOpNullMatch(magicFile, objpart);
	}

	// short unsigned
	public void testUnsignedByteGT() throws IOException {
		String magicFile = "0 ubyte >0xF0 match";
		byte[] objpart = hex2Bytes("FF");
		runMagicAndFileTestOp(magicFile, objpart, "match");

		objpart = hex2Bytes("F1");
		runMagicAndFileTestOp(magicFile, objpart, "match");
		
		objpart = hex2Bytes("EF");
		runMagicAndFileTestOpNullMatch(magicFile, objpart);
		
		objpart = hex2Bytes("AA");
		runMagicAndFileTestOpNullMatch(magicFile, objpart);
		
		objpart = hex2Bytes("80");
		runMagicAndFileTestOpNullMatch(magicFile, objpart);

		objpart = hex2Bytes("7F");
		runMagicAndFileTestOpNullMatch(magicFile, objpart);

		objpart = hex2Bytes("00");
		runMagicAndFileTestOpNullMatch(magicFile, objpart);

	}

	public void testSignedByteGT() throws IOException {
		String magicFile = "0 byte >0xF0 match";
		
		// higher always
		byte[] objpart = hex2Bytes("F1");
		runMagicAndFileTestOp(magicFile, objpart, "match");
		objpart = hex2Bytes("FF");
		runMagicAndFileTestOp(magicFile, objpart, "match");
		
		// Higher bc of two's complement
		objpart = hex2Bytes("00");
		runMagicAndFileTestOp(magicFile, objpart, "match");
		objpart = hex2Bytes("7F");
		runMagicAndFileTestOp(magicFile, objpart, "match");
		
		
		// lower always
		objpart = hex2Bytes("EF");
		runMagicAndFileTestOpNullMatch(magicFile, objpart);
		objpart = hex2Bytes("AA");
		runMagicAndFileTestOpNullMatch(magicFile, objpart);
		objpart = hex2Bytes("80");
		runMagicAndFileTestOpNullMatch(magicFile, objpart);
	}
	
	
	/*
	 * Shorts below
	 */
	
	public void testANDComparisonShort() throws IOException {
		String magicFile = "0 beshort &0xFF match";
		byte[] objpart = hex2Bytes("FFFF");
		runMagicAndFileTestOp(magicFile, objpart, "match");
		
		objpart = hex2Bytes("FFF4");
		runMagicAndFileTestOpNullMatch(magicFile, objpart);
	}
	

	public void testXORComparisonShort() throws IOException {
		String magicFile = "0 short ^0x24 match";
		byte[] objpart = hex2Bytes("FFCA");
		runMagicAndFileTestOp(magicFile, objpart, "match");
		
		objpart = hex2Bytes("FFCE");
		runMagicAndFileTestOpNullMatch(magicFile, objpart);
	}
	
	public void testNegateComparisonShort() throws IOException {
		String magicFile = "0 beshort ~3 match";
		byte[] objpart = hex2Bytes("FFFC");
		runMagicAndFileTestOp(magicFile, objpart, "match");
		
		objpart = hex2Bytes("FFFE");
		runMagicAndFileTestOpNullMatch(magicFile, objpart);
	}

	public void testShortWithMask() throws IOException {
		String magicFile = "0 beshort&0xF >7 match";
		byte[] objpart = hex2Bytes("FFFF");
		runMagicAndFileTestOp(magicFile, objpart, "match");
		
		objpart = hex2Bytes("000F");
		runMagicAndFileTestOp(magicFile, objpart, "match");

		objpart = hex2Bytes("0009");
		runMagicAndFileTestOp(magicFile, objpart, "match");

		objpart = hex2Bytes("0008");
		runMagicAndFileTestOp(magicFile, objpart, "match");

		// fail bc not gt 7
		objpart = hex2Bytes("0007");
		runMagicAndFileTestOpNullMatch(magicFile, objpart);



		objpart = hex2Bytes("FFF3");
		runMagicAndFileTestOpNullMatch(magicFile, objpart);

		objpart = hex2Bytes("FFF0");
		runMagicAndFileTestOpNullMatch(magicFile, objpart);
	}

	// short unsigned
	public void testUnsignedShortGT() throws IOException {
		String magicFile = "0 ubeshort >0xF000 match";
		byte[] objpart = hex2Bytes("FFFF");
		runMagicAndFileTestOp(magicFile, objpart, "match");

		objpart = hex2Bytes("F001");
		runMagicAndFileTestOp(magicFile, objpart, "match");
		
		objpart = hex2Bytes("EFFF");
		runMagicAndFileTestOpNullMatch(magicFile, objpart);
		
		objpart = hex2Bytes("AAAA");
		runMagicAndFileTestOpNullMatch(magicFile, objpart);
		
		objpart = hex2Bytes("8000");
		runMagicAndFileTestOpNullMatch(magicFile, objpart);

		objpart = hex2Bytes("7FFF");
		runMagicAndFileTestOpNullMatch(magicFile, objpart);

		objpart = hex2Bytes("0000");
		runMagicAndFileTestOpNullMatch(magicFile, objpart);

	}

	public void testSignedShortGT() throws IOException {
		String magicFile = "0 beshort >0xF000 match";
		
		// higher always
		byte[] objpart = hex2Bytes("F001");
		runMagicAndFileTestOp(magicFile, objpart, "match");
		objpart = hex2Bytes("FFFF");
		runMagicAndFileTestOp(magicFile, objpart, "match");
		
		// Higher bc of two's complement
		objpart = hex2Bytes("0000");
		runMagicAndFileTestOp(magicFile, objpart, "match");
		objpart = hex2Bytes("7FFF");
		runMagicAndFileTestOp(magicFile, objpart, "match");
		
		
		// lower always
		objpart = hex2Bytes("EFFF");
		runMagicAndFileTestOpNullMatch(magicFile, objpart);
		objpart = hex2Bytes("AAAA");
		runMagicAndFileTestOpNullMatch(magicFile, objpart);
		objpart = hex2Bytes("8000");
		runMagicAndFileTestOpNullMatch(magicFile, objpart);

	}

	
	
	
	
	
	
	

	// short unsigned
	public void testUnsignedLongGT() throws IOException {
		String magicFile = "0 ubelong >0xF0000000 match";
		byte[] objpart = hex2Bytes("FFFFFFFF");
		runMagicAndFileTestOp(magicFile, objpart, "match");

		objpart = hex2Bytes("F0000001");
		runMagicAndFileTestOp(magicFile, objpart, "match");
		
		objpart = hex2Bytes("EFFFFFFF");
		runMagicAndFileTestOpNullMatch(magicFile, objpart);
		
		objpart = hex2Bytes("AAAAAAAA");
		runMagicAndFileTestOpNullMatch(magicFile, objpart);
		
		objpart = hex2Bytes("80000000");
		runMagicAndFileTestOpNullMatch(magicFile, objpart);

		objpart = hex2Bytes("7FFFFFFF");
		runMagicAndFileTestOpNullMatch(magicFile, objpart);

		objpart = hex2Bytes("00000000");
		runMagicAndFileTestOpNullMatch(magicFile, objpart);

	}

	public void testSignedLongGT() throws IOException {
		String magicFile = "0 belong >0xF0000000 match";
		
		// higher always
		byte[] objpart = hex2Bytes("F0000001");
		runMagicAndFileTestOp(magicFile, objpart, "match");
		objpart = hex2Bytes("FFFFFFFF");
		runMagicAndFileTestOp(magicFile, objpart, "match");
		
		// Higher bc of two's complement
		objpart = hex2Bytes("00000000");
		runMagicAndFileTestOp(magicFile, objpart, "match");
		objpart = hex2Bytes("7FFFFFFF");
		runMagicAndFileTestOp(magicFile, objpart, "match");
		
		
		// lower always
		objpart = hex2Bytes("EFFFFFFF");
		runMagicAndFileTestOpNullMatch(magicFile, objpart);
		objpart = hex2Bytes("AAAAAAAA");
		runMagicAndFileTestOpNullMatch(magicFile, objpart);
		objpart = hex2Bytes("80000000");
		runMagicAndFileTestOpNullMatch(magicFile, objpart);

	}


	public void testBEDouble() throws IOException {
		String magicFile = "0 bedouble >8.64e+13 match";
		
		ByteBuffer bb = ByteBuffer.allocate(8);
		bb.putDouble(Double.parseDouble("8.72e+13"));
		bb.flip();
		byte[] objpart = bb.array();
		runMagicAndFileTestOp(magicFile, objpart, "match");
		
		
		bb = ByteBuffer.allocate(8);
		bb.putDouble(Double.parseDouble("8.2e+13"));
		bb.flip();
		objpart = bb.array();
		runMagicAndFileTestOpNullMatch(magicFile, objpart);
	}	

	public void testLEDouble() throws IOException {
		String magicFile = "0 ledouble >8.64e+13 match";
		
		ByteBuffer bb = ByteBuffer.allocate(8);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.putDouble(Double.parseDouble("8.72e+13"));
		bb.flip();
		byte[] objpart = bb.array();
		runMagicAndFileTestOp(magicFile, objpart, "match");
		
		
		bb = ByteBuffer.allocate(8);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.putDouble(Double.parseDouble("8.2e+13"));
		bb.flip();
		objpart = bb.array();
		runMagicAndFileTestOpNullMatch(magicFile, objpart);
	}	

	public void testNotEqualComparisonQuad() throws IOException {
		String magicFile = "0 bequad !0 match";
		byte[] objpart = hex2Bytes("FFFFFFFFFFFFFFFF");
		runMagicAndFileTestOp(magicFile, objpart, "match");
		
		objpart = hex2Bytes("0000000000000000");
		runMagicAndFileTestOpNullMatch(magicFile, objpart);
	}

	public void testGTComparisonQuad() throws IOException {
		String magicFile = "0 bequad >0x7FFFFFFFFFFF0000 match";
		byte[] objpart = hex2Bytes("7FFFFFFFFFFFFFFF");
		runMagicAndFileTestOp(magicFile, objpart, "match");
		
		objpart = hex2Bytes("8000000000000000");
		runMagicAndFileTestOpNullMatch(magicFile, objpart);
	}

	public void testGTComparisonUnsignedQuad() throws IOException {
		String magicFile = "0 ubequad >0x7FFFFFFFFFFF0000 match";
		byte[] objpart = hex2Bytes("7FFFFFFFFFFFFFFF");
		runMagicAndFileTestOp(magicFile, objpart, "match");
		
		objpart = hex2Bytes("8000000000000000");
		runMagicAndFileTestOp(magicFile, objpart, "match");
	}

	
	
}
