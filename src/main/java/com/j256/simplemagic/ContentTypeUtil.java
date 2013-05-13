package com.j256.simplemagic;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPInputStream;

import com.j256.simplemagic.entries.MagicEntry;

/**
 * Class which reads in the magic files and determines the {@link ContentType} for files and byte arrays. You use the
 * default constructor {@link #ContentTypeUtil()} to use the internal rules file or load in a local file from the
 * file-system using {@link #ContentTypeUtil(String)}. Once the rules are loaded, you use {@link #findMatch(String)} or
 * other such methods to get the content-type of a file or bytes.
 * 
 * @author graywatson
 */
public class ContentTypeUtil {

	private final static String INTERNAL_MAGIC_FILE = "/magic.gz";
	// if this changes, fixed the javadocs for setFileReadSize() below
	private final static int DEFAULT_READ_SIZE = 10 * 1024;

	/** internal entries loaded once if the {@link ContentTypeUtil#MagicUtil()} constructor is used. */
	private static List<MagicEntry> internalMagicEntries;

	private final List<MagicEntry> magicEntries;
	private int fileReadSize = DEFAULT_READ_SIZE;
	private ErrorCallBack errorCallBack;

	/**
	 * Construct a magic utility using the internal magic file built into the package.
	 * 
	 * @throws IllegalStateException
	 *             If there was a problem reading the magic entries from the internal magic file.
	 */
	public ContentTypeUtil() {
		this((ErrorCallBack) null);
	}

	/**
	 * Construct a magic utility using the internal magic file built into the package. This also allows the caller to
	 * log any errors discovered in the file(s).
	 * 
	 * @throws IllegalStateException
	 *             If there was a problem reading the magic entries from the internal magic file.
	 */
	public ContentTypeUtil(ErrorCallBack errorCallBack) {
		this.errorCallBack = errorCallBack;
		if (internalMagicEntries == null) {
			try {
				internalMagicEntries = loadInternalEntries();
			} catch (IOException e) {
				throw new IllegalStateException("Could not load entries from internal magic file: "
						+ INTERNAL_MAGIC_FILE, e);
			}
		}
		this.magicEntries = internalMagicEntries;
	}

	/**
	 * Construct a magic utility using the magic files from a file or a directory of files.
	 * 
	 * @throws IOException
	 *             If there was a problem reading the magic entries from the internal magic file.
	 */
	public ContentTypeUtil(String fileOrDirectoryPath) throws IOException {
		this(new File(fileOrDirectoryPath), null);
	}

	/**
	 * Construct a magic utility using the magic files from a file or a directory of files. This also allows the caller
	 * to log any errors discovered in the file(s).
	 * 
	 * @throws IOException
	 *             If there was a problem reading the magic entries from the internal magic file.
	 */
	public ContentTypeUtil(String fileOrDirectoryPath, ErrorCallBack errorCallBack) throws IOException {
		this(new File(fileOrDirectoryPath), errorCallBack);
	}

	/**
	 * Construct a magic utility using the magic files from a file or a directory of files.
	 * 
	 * @throws IOException
	 *             If there was a problem reading the magic entries from the internal magic file.
	 */
	public ContentTypeUtil(File fileOrDirectory) throws IOException {
		this(fileOrDirectory, null);
	}

	/**
	 * Construct a magic utility using the magic files from a file or a directory of files. This also allows the caller
	 * to log any errors discovered in the file(s).
	 * 
	 * @throws IOException
	 *             If there was a problem reading the magic entries from the internal magic file.
	 */
	public ContentTypeUtil(File fileOrDirectory, ErrorCallBack errorCallBack) throws IOException {
		this.errorCallBack = errorCallBack;
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
	 * @throws IOException
	 *             If there was a problem reading from the file.
	 */
	public ContentType findMatch(String filePath) throws IOException {
		return findMatch(new File(filePath));
	}

	/**
	 * Return the content type for the file or null if none of the magic entries matched.
	 * 
	 * @throws IOException
	 *             If there was a problem reading from the file.
	 */
	public ContentType findMatch(File file) throws IOException {
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
		return findMatch(bytes);
	}

	/**
	 * Return the content type for the input-stream or null if none of the magic entries matched.
	 * 
	 * <p>
	 * <b>NOTE:</b> The caller is responsible for closing the input-stream.
	 * </p>
	 * 
	 * @throws IOException
	 *             If there was a problem reading from the input-stream.
	 */
	public ContentType findMatch(InputStream inputStream) throws IOException {
		byte[] bytes = new byte[fileReadSize];
		int read = inputStream.read(bytes);
		if (read < 0) {
			return null;
		}
		if (read < bytes.length) {
			// move the bytes into a smaller array
			bytes = Arrays.copyOf(bytes, read);
		}
		return findMatch(bytes);
	}

	/**
	 * Return the content type from the associated bytes or null if none of the magic entries matched.
	 */
	public ContentType findMatch(byte[] bytes) {
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
	 * Set the default size that will be read if we are getting the content from a file. The default is most likely 10k.
	 */
	public void setFileReadSize(int fileReadSize) {
		this.fileReadSize = fileReadSize;
	}

	/**
	 * Set our class which will get called whenever we get a configuration error.
	 */
	public void setErrorCallBack(ErrorCallBack errorCallBack) {
		this.errorCallBack = errorCallBack;
	}

	private List<MagicEntry> loadInternalEntries() throws IOException {
		InputStream stream = getClass().getResourceAsStream(INTERNAL_MAGIC_FILE);
		if (stream == null) {
			throw new FileNotFoundException("Internal magic file not found in class-path: " + INTERNAL_MAGIC_FILE);
		}
		Reader reader = null;
		try {
			reader = new InputStreamReader(new GZIPInputStream(new BufferedInputStream(stream)));
			stream = null;
			List<MagicEntry> entryList = new ArrayList<MagicEntry>();
			readFile(entryList, reader);
			return entryList;
		} finally {
			closeQuietly(reader);
			closeQuietly(stream);
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
				entry = MagicEntry.parseLine(previous, line, errorCallBack);
			} catch (IllegalArgumentException e) {
				if (errorCallBack != null) {
					errorCallBack.error(line, e.getMessage(), e);
				}
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

	private void closeQuietly(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				// ignored
			}
		}
	}

	/**
	 * While we are parsing the magic configuration files, there are usually tons of badly formed lines and other
	 * errors. This class defines the call-back which will be made whenever we discover an error.
	 */
	public interface ErrorCallBack {

		/**
		 * An error was generated while processing the line.
		 * 
		 * @param line
		 *            Line where the error happened.
		 * @param details
		 *            Specific information about the error.
		 * @param e
		 *            Exception that was thrown trying to parse the line or null if none.
		 */
		public void error(String line, String details, Exception e);
	}
}
