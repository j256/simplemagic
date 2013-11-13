package com.j256.simplemagic.entries;

import java.io.BufferedReader;
import java.io.IOException;

import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil.ErrorCallBack;

/**
 * Class which encompasses a set of entries and allows us to optimize their use.
 * 
 * @author graywatson
 */
public class MagicEntries {

	private static final int MAX_LEVELS = 20;

	private MagicEntry entryLinkedList;

	/**
	 * Read the entries so later we can find matches with them.
	 */
	public void readEntries(BufferedReader lineReader, ErrorCallBack errorCallBack) throws IOException {
		MagicEntry[] levelNexts = new MagicEntry[MAX_LEVELS];
		MagicEntry previousEntry = null;
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
				entry = MagicEntryParser.parseLine(previousEntry, line, errorCallBack);
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

			int level = entry.getLevel();
			if (previousEntry != null) {
				// if we go down a level, we need to clear the nexts above us
				for (int levelCount = level + 1; levelCount <= previousEntry.getLevel(); levelCount++) {
					levelNexts[levelCount] = null;
				}
			}

			// if this is the first at this level?
			if (levelNexts[level] == null) {
				if (level == 0) {
					// first top level entry
					entryLinkedList = entry;
				} else if (levelNexts[level - 1] != null) {
					levelNexts[level - 1].setChild(entry);
				}
			} else {
				// continue the linked list
				levelNexts[level].setNext(entry);
			}
			levelNexts[level] = entry;
			previousEntry = entry;
		}
	}

	/**
	 * Find and return a match for the associated bytes.
	 */
	public ContentInfo findMatch(byte[] bytes) {
		ContentInfo partialMatch = null;
		for (MagicEntry entry = entryLinkedList; entry != null; entry = entry.getNext()) {
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
