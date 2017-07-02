package com.j256.simplemagic.entries;

import org.junit.Test;

public class MagicEntryTest {

	@Test
	public void testCoverage() {
		MagicEntry entry = new MagicEntry("name", 0, false, 0, null, null, null, false, 1, false, false, null);
		entry.toString();
		MagicEntry entry2 = new MagicEntry(null, 0, false, 0, null, null, null, false, 1, false, false, null);
		entry2.toString();
		MagicEntry entry3 = new MagicEntry(null, 0, false, 0, null, null, null, false, null, false, false,
				new MagicFormatter("format"));
		entry3.setMimeType("mime");
		entry3.toString();
	}

}
