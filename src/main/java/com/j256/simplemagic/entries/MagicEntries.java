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
	private static final int FIRST_BYTE_LINKED_LIST_SIZE = 256;

	private MagicEntry entryLinkedList;
	private final MagicEntry[] firstByteLinkedLists = new MagicEntry[FIRST_BYTE_LINKED_LIST_SIZE];

	/**
	 * Read the entries so later we can find matches with them.
	 */
	public void readEntries(BufferedReader lineReader, ErrorCallBack errorCallBack) throws IOException {
		MagicEntry[] levelNexts = new MagicEntry[MAX_LEVELS];
		MagicEntry previousNonStartEntry = null;
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
				entry = MagicEntryParser.parseLine(previousNonStartEntry, line, errorCallBack);
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
			if (previousNonStartEntry == null) {
				if (level != 0) {
					if (errorCallBack != null) {
						errorCallBack.error(line, "first entry of the file but the leve (" + level + ") should be 0",
								null);
					}
					continue;
				}
			} else {
				// if we go down a level, we need to clear the nexts above us
				for (int levelCount = level + 1; levelCount <= previousNonStartEntry.getLevel(); levelCount++) {
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
			previousNonStartEntry = entry;
		}

//		 if (true) {
//		 return;
//		 }
		// now we post process the entries and remove the first byte ones we can optimize
		MagicEntry[] firstByteNexts = new MagicEntry[firstByteLinkedLists.length];
		previousNonStartEntry = null;
		for (MagicEntry entry = entryLinkedList; entry != null; entry = entry.getNext()) {
			byte[] startingBytes = entry.getStartsWithByte();
			if (startingBytes == null || startingBytes.length == 0) {
				// continue the entry linked list
				if (previousNonStartEntry == null) {
					entryLinkedList = entry;
				} else {
					previousNonStartEntry.setNext(entry);
				}
				previousNonStartEntry = entry;
			} else {
				int index = (0xFF & startingBytes[0]);
				if (firstByteNexts[index] == null) {
					firstByteLinkedLists[index] = entry;
				} else {
					firstByteNexts[index].setNext(entry);
				}
				firstByteNexts[index] = entry;
			}
		}
		if (previousNonStartEntry != null) {
			previousNonStartEntry.setNext(null);
		}
		for (int i = 0; i < firstByteNexts.length; i++) {
			if (firstByteNexts[i] != null) {
				firstByteNexts[i].setNext(null);
			}
		}
	}

	/**
	 * Find and return a match for the associated bytes.
	 */
	public ContentInfo findMatch(byte[] bytes) {
		if (bytes.length == 0) {
			return null;
		}
		ContentInfo partialMatch = null;
		// first do the start byte ones
		int index = (0xFF & bytes[0]);
		if (index < firstByteLinkedLists.length && firstByteLinkedLists[index] != null) {
			for (MagicEntry entry = firstByteLinkedLists[index]; entry != null; entry = entry.getNext()) {
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
			// XXX: not sure if this is right, maybe if we only have a partial here we should try all of the rest
			if (partialMatch != null) {
				return partialMatch;
			}
		}
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
