package com.j256.simplemagic.entries;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

/**
 * Loads the IANA databases (build on 10 august 2017).
 * IANA databases provides the following elements in a CSV file:
 * <ul>
 * <li>Name of the file type</li>
 * <li>mime type</li>
 * <li>Name of the articles describing the mime type</li>
 * </ul>
 * In addition to these elements, two URLs are created in order to locate the
 * description of the mime type and the URL of the articles.
 * @author Jean-Christophe Malapert (jcmalapert@gmail.com)
 */
public class IanaEntries {

	private final static String INTERNAL_IANA_APP_DB = "/iana_app.gz";
	private final static String INTERNAL_IANA_AUDIO_DB = "/iana_audio.gz";
	private final static String INTERNAL_IANA_FONT_DB = "/iana_font.gz";
    
	/**
	 * The database
	 */
	private final Map<String, IanaEntry> ianaDB = new HashMap<String, IanaEntry>();

        /**
         * Constructor.
         */
        public IanaEntries() {
        	loadDb(INTERNAL_IANA_APP_DB);
                loadDb(INTERNAL_IANA_AUDIO_DB);
                loadDb(INTERNAL_IANA_FONT_DB);
        }
    
        /**
         * Returns the IANA database.
         */
        public Map<String, IanaEntry> getIanaDB() {
            return ianaDB;
        }

        /**
         * Returns the IANA metadata for a specific mime type or null when
         * the mime type is not found.
         */
        public IanaEntry getIanaMetadata(final String mimeType) {
                return this.ianaDB.getOrDefault(mimeType, null);
        }    

        /**
         * Loads the IANA database
         * @param db 
         */
        private void loadDb(String db) {        
                InputStream stream = getClass().getResourceAsStream(db);
                if (stream == null) {
                        throw new RuntimeException(db + " is missing");
                }
                Reader reader = null;
                try {
                        reader = new InputStreamReader(new GZIPInputStream(new BufferedInputStream(stream)));
                        stream = null;
                        BufferedReader lineReader = new BufferedReader(reader);
                        String line = "";
                        lineReader.readLine();//skip the first line
                        while ((line = lineReader.readLine()) != null) {
                                // parse the CSV file. The CSV file contains
                                // three elements per row
                                String[] ianaEntryParsed = line.split(","); 
                                // fix problem in the CSV file provided by IANA
                                // such as G719,audio/G719,"[RFC5404][RFC Errata 
                                //                                         3245]"
                                if(ianaEntryParsed[2].startsWith("\"")) {
                                        line = lineReader.readLine().replaceAll("\\s+","");
                                        ianaEntryParsed[2]+=line;
                                }
                                IanaEntry ianaEntry = new IanaEntry(ianaEntryParsed[0], ianaEntryParsed[1], ianaEntryParsed[2]);
                                addMimeType(ianaEntry.getMimeType(), ianaEntry);
                        }
                } catch (IOException ex) {
                        throw new RuntimeException("Error when loading " + db);
                } finally {
                        closeQuietly(reader);
                        closeQuietly(stream);
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
         * Add an entry in the IANA database.
         */
        private void addMimeType(final String mimeType, final IanaEntry ianaEntry) {
                if(!mimeType.isEmpty()) {
                        this.ianaDB.put(mimeType, ianaEntry);
                }
        }
}
