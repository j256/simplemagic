package com.j256.simplemagic.entries;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil.ErrorCallBack;

/**
 * Class which encompasses a set of entries and allows us to optimize their use.
 * 
 * @author graywatson
 */
public class MagicEntries {

	private MagicEntry[] entryArray;
	private MagicEntry previous;

	/**
	 * Read the entries so later we can find matches with them.
	 */
	public void readEntries(BufferedReader lineReader, ErrorCallBack errorCallBack) throws IOException {
		List<MagicEntry> entryList = new ArrayList<MagicEntry>();
		while (true) {
			String line = lineReader.readLine();
			if (line == null) {
				break;
			}
			// skip blanks and comments
			if (line.length() == 0 || line.charAt(0) == '#') {
				continue;
			}

			MagicEntry entry;
			try {
				entry = MagicEntryParser.parseLine(previous, line, errorCallBack);
				if (entry == null) {
					continue;
				}
			} catch (IllegalArgumentException e) {
				if (errorCallBack != null) {
					errorCallBack.error(line, e.getMessage(), e);
				}
				// ignore this entry
				continue;
			}

			// is this a top level line?
			if (entry.getLevel() == 0) {
				entryList.add(entry);
			}
			previous = entry;
		}

		this.entryArray = entryList.toArray(new MagicEntry[entryList.size()]);
		// when we are done, we do some cleanup
		for (MagicEntry entry : entryArray) {
			entry.parseComplete();
		}
	}

	/**
	 * Find and return a match for the associated bytes.
	 */
	public ContentInfo findMatch(byte[] bytes) {
		ContentInfo partialMatch = null;
		for (MagicEntry entry : entryArray) {
			ContentInfo info = entry.processBytes(bytes);
			if (info == null) {
				continue;
			}
			if (!info.isPartial()) {
				// first non-partial wins
				return info;
			} else if (partialMatch == null) {
				// first partial match wins
				partialMatch = info;
			} else {
				// first partial match wins
			}
		}
		return partialMatch;
	}
}
