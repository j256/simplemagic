package com.j256.simplemagic.entries;

import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.List;

import com.j256.simplemagic.ContentInfoUtil;
import com.j256.simplemagic.ContentInfoUtil.ErrorCallBack;

public class MagicEntryDumper {

	public static void main(String[] args) throws Exception {
		ErrorCallBack handler = new ErrorCallBack() {
			@Override
			public void error(String line, String details, Exception e) {
				System.err.println(line + ":" + details);
			}		
		};
		ContentInfoUtil util = args.length == 0 ? new ContentInfoUtil(handler) :
			new ContentInfoUtil(new FileReader(args[0]), handler);
		Field entriesField = ContentInfoUtil.class.getDeclaredField("magicEntries");
		entriesField.setAccessible(true);
		MagicEntries entries = (MagicEntries) entriesField.get(util);
		Field entryListField = MagicEntries.class.getDeclaredField("entryList");
		entryListField.setAccessible(true); 
		@SuppressWarnings("unchecked")
		List<MagicEntry> entryList = (List<MagicEntry>) entryListField.get(entries);
		for (MagicEntry entry: entryList) {
			System.out.println(entry.toString2());
		}
	}
}
