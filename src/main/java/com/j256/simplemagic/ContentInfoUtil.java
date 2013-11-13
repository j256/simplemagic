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
import java.util.Arrays;
import java.util.zip.GZIPInputStream;

import com.j256.simplemagic.entries.MagicEntries;

/**
 * Class which reads in the magic files and determines the {@link ContentInfo} for files and byte arrays. You use the
 * default constructor {@link #ContentInfoUtil()} to use the internal rules file or load in a local file from the
 * file-system using {@link #ContentInfoUtil(String)}. Once the rules are loaded, you use {@link #findMatch(String)} or
 * other such methods to get the content-type of a file or bytes.
 * 
 * @author graywatson
 */
public class ContentInfoUtil {

	private final static String INTERNAL_MAGIC_FILE = "/magic.gz";
	// if this changes, fixed the javadocs for setFileReadSize() below
	public final static int DEFAULT_READ_SIZE = 10 * 1024;

	/** internal entries loaded once if the {@link ContentInfoUtil#MagicUtil()} constructor is used. */
	private static MagicEntries internalMagicEntries;

	private final MagicEntries magicEntries;
	private int fileReadSize = DEFAULT_READ_SIZE;
	private ErrorCallBack errorCallBack;

	/**
	 * Construct a magic utility using the internal magic file built into the package.
	 * 
	 * @throws IllegalStateException
	 *             If there was a problem reading the magic entries from the internal magic file.
	 */
	public ContentInfoUtil() {
		this((ErrorCallBack) null);
	}

	/**
	 * Construct a magic utility using the internal magic file built into the package. This also allows the caller to
	 * log any errors discovered in the file(s).
	 * 
	 * @param errorCallBack
	 *            Call back which shows any problems with the magic entries loaded.
	 * @throws IllegalStateException
	 *             If there was a problem reading the magic entries from the internal magic file.
	 */
	public ContentInfoUtil(ErrorCallBack errorCallBack) {
		this.errorCallBack = errorCallBack;
		if (internalMagicEntries == null) {
			try {
				internalMagicEntries = readEntriesFromResource(INTERNAL_MAGIC_FILE);
			} catch (IOException e) {
				throw new IllegalStateException("Could not load entries from internal magic file: "
						+ INTERNAL_MAGIC_FILE, e);
			}
			if (internalMagicEntries == null) {
				throw new IllegalStateException("Internal magic file not found in class-path: " + INTERNAL_MAGIC_FILE);
			}
		}
		this.magicEntries = internalMagicEntries;
	}

	/**
	 * Construct a magic utility using the magic files from a file or a directory of files.
	 * 
	 * @param fileOrDirectoryOrResourcePath
	 *            A path which can be a magic file, or a directory of magic files, or a magic file in a resource path.
	 * @throws IOException
	 *             If there was a problem reading the magic entries from the internal magic file.
	 */
	public ContentInfoUtil(String fileOrDirectoryPath) throws IOException {
		this(new File(fileOrDirectoryPath), null);
	}

	/**
	 * Construct a magic utility using the magic files from a file or a directory of files. This also allows the caller
	 * to log any errors discovered in the file(s).
	 * 
	 * @param fileOrDirectoryOrResourcePath
	 *            A path which can be a magic file, or a directory of magic files, or a magic file in a resource path.
	 * @param errorCallBack
	 *            Call back which shows any problems with the magic entries loaded.
	 * @throws IOException
	 *             If there was a problem reading the magic entries from the internal magic file.
	 */
	public ContentInfoUtil(String fileOrDirectoryOrResourcePath, ErrorCallBack errorCallBack) throws IOException {
		this.errorCallBack = errorCallBack;
		MagicEntries magicEntries = readEntriesFromResource(fileOrDirectoryOrResourcePath);
		if (magicEntries == null) {
			File file = new File(fileOrDirectoryOrResourcePath);
			magicEntries = readEntriesFromFile(file);
		}
		if (magicEntries == null) {
			throw new IllegalArgumentException("Magic path specified is not a file, directory, or resource: "
					+ fileOrDirectoryOrResourcePath);
		}
		this.magicEntries = magicEntries;
	}

	/**
	 * Construct a magic utility using the magic files from a file or a directory of files.
	 * 
	 * @param fileOrDirectory
	 *            A path which can be a magic file, or a directory of magic files.
	 * @throws IOException
	 *             If there was a problem reading the magic entries from the internal magic file.
	 */
	public ContentInfoUtil(File fileOrDirectory) throws IOException {
		this(fileOrDirectory, null);
	}

	/**
	 * Construct a magic utility using the magic files from a file or a directory of files. This also allows the caller
	 * to log any errors discovered in the file(s).
	 * 
	 * @param fileOrDirectory
	 *            A path which can be a magic file, or a directory of magic files.
	 * @param errorCallBack
	 *            Call back which shows any problems with the magic entries loaded.
	 * @throws IOException
	 *             If there was a problem reading the magic entries from the internal magic file.
	 */
	public ContentInfoUtil(File fileOrDirectory, ErrorCallBack errorCallBack) throws IOException {
		this.errorCallBack = errorCallBack;
		this.magicEntries = readEntriesFromFile(fileOrDirectory);
		if (this.magicEntries == null) {
			throw new IllegalArgumentException("Magic path specified is not a file, directory, or resource: "
					+ fileOrDirectory);
		}
	}

	/**
	 * Return the content type for the file or null if none of the magic entries matched.
	 * 
	 * @throws IOException
	 *             If there was a problem reading from the file.
	 */
	public ContentInfo findMatch(String filePath) throws IOException {
		return findMatch(new File(filePath));
	}

	/**
	 * Return the content type for the file or null if none of the magic entries matched.
	 * 
	 * @throws IOException
	 *             If there was a problem reading from the file.
	 */
	public ContentInfo findMatch(File file) throws IOException {
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
	public ContentInfo findMatch(InputStream inputStream) throws IOException {
		byte[] bytes = new byte[fileReadSize];
		int numRead = inputStream.read(bytes);
		if (numRead < 0) {
			return null;
		}
		if (numRead < bytes.length) {
			// move the bytes into a smaller array
			bytes = Arrays.copyOf(bytes, numRead);
		}
		return findMatch(bytes);
	}

	/**
	 * Return the content type from the associated bytes or null if none of the magic entries matched.
	 */
	public ContentInfo findMatch(byte[] bytes) {
		return magicEntries.findMatch(bytes);
	}

	/**
	 * Return the content type if the extension from the file-name matches our internal list. This can either be just
	 * the extension part or it will look for the last period and take the string after that as the extension.
	 * 
	 * @return The matching content-info or null if no matches.
	 */
	public static ContentInfo findExtensionMatch(String name) {
		name = name.toLowerCase();

		// look up the whole name first
		ContentType type = ContentType.fromFileExtension(name);
		if (type != ContentType.OTHER) {
			return new ContentInfo(type);
		}

		// now find the .ext part
		int index = name.lastIndexOf('.');
		if (index < 0 || index == name.length() - 1) {
			return null;
		}

		type = ContentType.fromFileExtension(name.substring(index + 1));
		if (type == ContentType.OTHER) {
			return null;
		} else {
			return new ContentInfo(type);
		}
	}

	/**
	 * Return the content type if the mime-type matches our internal list.
	 * 
	 * @return The matching content-info or null if no matches.
	 */
	public static ContentInfo findMimeTypeMatch(String mimeType) {
		ContentType type = ContentType.fromMimeType(mimeType.toLowerCase());
		if (type == ContentType.OTHER) {
			return null;
		} else {
			return new ContentInfo(type);
		}
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

	private MagicEntries readEntriesFromFile(File fileOrDirectory) throws FileNotFoundException, IOException {
		if (fileOrDirectory.isFile()) {
			FileReader reader = new FileReader(fileOrDirectory);
			try {
				return readEntries(reader);
			} finally {
				closeQuietly(reader);
			}
		} else if (fileOrDirectory.isDirectory()) {
			for (File subFile : fileOrDirectory.listFiles()) {
				FileReader reader = null;
				try {
					reader = new FileReader(subFile);
					return readEntries(reader);
				} catch (IOException e) {
					// ignore the file
				} finally {
					closeQuietly(reader);
				}
			}
		}
		return null;
	}

	private MagicEntries readEntriesFromResource(String resource) throws IOException {
		InputStream stream = getClass().getResourceAsStream(resource);
		if (stream == null) {
			return null;
		}
		Reader reader = null;
		try {
			// this suffix test is here for testing purposes so we can generate a simple magic file
			if (resource.endsWith(".gz")) {
				reader = new InputStreamReader(new GZIPInputStream(new BufferedInputStream(stream)));
			} else {
				reader = new InputStreamReader(new BufferedInputStream(stream));
			}
			stream = null;
			return readEntries(reader);
		} finally {
			closeQuietly(reader);
			closeQuietly(stream);
		}
	}

	private MagicEntries readEntries(Reader reader) throws IOException {
		MagicEntries entries = new MagicEntries();
		BufferedReader lineReader = new BufferedReader(reader);
		try {
			entries.readEntries(lineReader, errorCallBack);
			return entries;
		} finally {
			closeQuietly(lineReader);
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
