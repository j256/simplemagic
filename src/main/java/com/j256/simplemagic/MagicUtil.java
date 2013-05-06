package com.j256.simplemagic;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import com.j256.simplemagic.entries.MagicEntry;

/**
 * Class which reads in the magic files and determines the {@link ContentType} for files and byte arrays.
 * 
 * @author graywatson
 */
public class MagicUtil {

	private final static String INTERNAL_MAGIC_FILE = "/magic.gz";
	private final static int DEFAULT_READ_SIZE = 100 * 1024;

	private List<MagicEntry> magicEntries;
	private int fileReadSize = DEFAULT_READ_SIZE;

	/**
	 * Construct a magic utility using the internal magic file built into the package.
	 * 
	 * @throws IOException
	 *             If there was a problem reading the magic entries from the internal magic file.
	 */
	public MagicUtil() throws IOException {
		InputStream stream = getClass().getResourceAsStream(INTERNAL_MAGIC_FILE);
		if (stream == null) {
			throw new IllegalStateException("Internal magic file not found: " + INTERNAL_MAGIC_FILE);
		}
		Reader reader = null;
		try {
			reader = new InputStreamReader(new GZIPInputStream(new BufferedInputStream(stream)));
			stream = null;
			List<MagicEntry> entryList = new ArrayList<MagicEntry>();
			readFile(entryList, reader);
			magicEntries = entryList;
		} finally {
			closeQuietly(reader);
			closeQuietly(stream);
		}
	}

	/**
	 * Construct a magic utility using the magic files from a file or a directory of files.
	 * 
	 * @throws IOException
	 *             If there was a problem reading the magic entries from the internal magic file.
	 */
	public MagicUtil(File fileOrDirectory) throws IOException {
		List<MagicEntry> entryList = new ArrayList<MagicEntry>();
		if (fileOrDirectory.isFile()) {
			FileReader reader = new FileReader(fileOrDirectory);
			try {
				readFile(entryList, reader);
			} finally {
				closeQuietly(reader);
			}
		} else if (fileOrDirectory.isDirectory()) {
			for (File subFile : fileOrDirectory.listFiles()) {
				FileReader reader = null;
				try {
					reader = new FileReader(subFile);
					readFile(entryList, reader);
				} catch (IOException e) {
					// ignore the file
				} finally {
					closeQuietly(reader);
				}
			}
		} else {
			throw new IllegalArgumentException("Magic file directory specified is not a file or directory: "
					+ fileOrDirectory);
		}
		magicEntries = entryList;
	}

	/**
	 * Return the content type for the file or null if none of the magic entries matched.
	 * 
	 * <p>
	 * <b>NOTE:</b> one of the {@link #loadMagicFile(File)}, {@link #loadMagicFileDirectory(File)}, or
	 * {@link #loadSystemMagicFiles()} must be called before this method.
	 * </p>
	 */
	public ContentType contentTypeOfFile(String filePath) throws IOException {
		return contentTypeOfFile(new File(filePath));
	}

	/**
	 * Return the content type for the file or null if none of the magic entries matched.
	 * 
	 * <p>
	 * <b>NOTE:</b> one of the {@link #loadMagicFile(File)}, {@link #loadMagicFileDirectory(File)}, or
	 * {@link #loadSystemMagicFiles()} must be called before this method.
	 * </p>
	 */
	public ContentType contentTypeOfFile(File file) throws IOException {
		int readSize = fileReadSize;
		if (file.length() < readSize) {
			readSize = (int) file.length();
		}
		byte[] bytes = new byte[readSize];
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			fis.read(bytes);
		} finally {
			closeQuietly(fis);
		}
		return contentTypeOfBytes(bytes);
	}

	/**
	 * Return the content type from the associated bytes or null if none of the magic entries matched.
	 * 
	 * <p>
	 * <b>NOTE:</b> one of the {@link #loadMagicFile(File)}, {@link #loadMagicFileDirectory(File)}, or
	 * {@link #loadSystemMagicFiles()} must be called before this method.
	 * </p>
	 */
	public ContentType contentTypeOfBytes(byte[] bytes) {
		if (magicEntries == null) {
			throw new IllegalStateException("Magic files have not been loaded");
		}
		for (MagicEntry entry : magicEntries) {
			ContentType contentType = entry.processBytes(bytes);
			if (contentType != null) {
				return contentType;
			}
		}
		return null;
	}

	/**
	 * Set the default size that will be read if we are getting the content from a file. The default is
	 * {@link #DEFAULT_READ_SIZE}.
	 */
	public void setFileReadSize(int fileReadSize) {
		this.fileReadSize = fileReadSize;
	}

	private void closeQuietly(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				// ignored
			}
		}
	}

	private void readFile(List<MagicEntry> entryList, Reader reader) throws IOException {
		BufferedReader lineReader = new BufferedReader(reader);
		MagicEntry previous = null;
		while (true) {
			String line = lineReader.readLine();
			if (line == null) {
				return;
			}
			if (line.length() == 0 || line.charAt(0) == '#') {
				continue;
			}

			MagicEntry entry;
			try {
				entry = MagicEntry.parseString(previous, line);
			} catch (IllegalArgumentException e) {
				// System.err.println("Error in entry: " + e);
				// ignore this entry
				continue;
			}
			if (entry == null) {
				continue;
			}
			if (entry.getLevel() == 0) {
				entryList.add(entry);
			}
			previous = entry;
		}
	}
}
