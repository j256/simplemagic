package com.j256.simplemagic.entries;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class MagicEntryTest {

	@Test
	public void testToString() {
		MagicEntry entry = new MagicEntry("name", 0, false, 0, null, null, null, false, 1, false, false, null);
		assertEquals("level 0,name 'name',test '1'", entry.toString());
		MagicEntry entry2 = new MagicEntry(null, 0, false, 0, null, null, null, false, 1, false, false, null);
		assertEquals("level 0,test '1'", entry2.toString());
		MagicEntry entry3 = new MagicEntry(null, 0, false, 0, null, null, null, false, null, false, false,
				new MagicFormatter("format"));
		entry3.setMimeType("mime");
		assertEquals("level 0,mime 'mime',format 'format'", entry3.toString());
	}

}
