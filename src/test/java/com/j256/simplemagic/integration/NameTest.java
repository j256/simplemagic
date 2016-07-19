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

public class NameTest extends AbstractMagicTest {
//	public void testNamedEntry() throws IOException {
//		String magicFile = "0 name mynamedentry";
//		MagicFileModel mfm = new MagicFileLoader().readMagicFile(
//				new ByteArrayInputStream(magicFile.getBytes()));
//		MagicNode mn = mfm.getNamedNode("mynamedentry");
//		assertNotNull(mn);
//		
//		mn = mfm.getNamedNode("garbage");
//		assertNull(mn);
//	}
//
//	public void testUseNoOffsetEntry() throws IOException {
//		String magicFile = 
//				   "4  byte  0xF  match1 \n" +
//		           ">0   use  test1\n" + 
//				   "0   name test1\n" + 
//				   ">0  byte  5   match2 \n";
//		
//		byte[] objpart = hex2Bytes("050000000F");
//		runMagicAndFileTestOp(magicFile, objpart, "Test: match1 match2");
//	}
//	
//	public void testUseWithOffsetEntry() throws IOException {
//		String magicFile = 
//				   "4  byte  0xF  match1 \n" +
//		           ">8   use  test1\n" + 
//				   "0   name test1\n" + 
//				   ">0  byte  5   match2 \n";
//		
//		byte[] objpart = hex2Bytes("000000000F00000005");
//		runMagicAndFileTestOp(magicFile, objpart, "Test: match1 match2");
//	}
//
//	public void testUseFlipEndianEntry() throws IOException {
//		String magicFile = 
//				   "2  leshort  0xFF  match1 \n" +
//		           ">5   use  ^test1\n" + 
//				   "0   name test1\n" + 
//				   ">0  leshort  5   match2 \n";
//		//                                      space + LE-2 + buffer + BE-5  (bc flipped)
//		byte[] objpart = hex2Bytes("0000" + "FF00" + "00" + "0005");
//		runMagicAndFileTestOp(magicFile, objpart, "Test: match1 match2");
//	}
}
