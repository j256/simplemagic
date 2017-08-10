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
 * Loads the IANA databases (build on 10 august 2017)
 * @author Jean-Christophe Malapert (jcmalapert@gmail.com)
 */
public class IanaEntries {

    private final static String INTERNAL_IANA_APP_DB = "/iana_app.gz";
    private final static String INTERNAL_IANA_AUDIO_DB = "/iana_audio.gz";
    private final static String INTERNAL_IANA_FONT_DB = "/iana_font.gz";
    
    /**
     * The database
     */
    private final Map<String, IanaEntry> _ianaDB;

    /**
     * Constructor.
     */
    public IanaEntries() {
        this._ianaDB = new HashMap<String, IanaEntry>();
        loadDb(INTERNAL_IANA_APP_DB);
        loadDb(INTERNAL_IANA_AUDIO_DB);
        loadDb(INTERNAL_IANA_FONT_DB);
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
            BufferedReader lineReader = new BufferedReader(reader);
            String line = "";
            lineReader.readLine();//skip the first line
            while ((line = lineReader.readLine()) != null) {
                String[] ianaEntryParsed = line.split(","); 
                if(ianaEntryParsed[2].startsWith("\"")) {
                    line = lineReader.readLine().replaceAll("\\s+","");
                    ianaEntryParsed[2]+=line;
                }
                IanaEntry ianaEntry = new IanaEntry(ianaEntryParsed[0], ianaEntryParsed[1], ianaEntryParsed[2]);
                addMimeType(ianaEntry.getMimeType(), ianaEntry);
            }
        } catch (IOException ex) {
            Logger.getLogger(IanaEntries.class.getName()).log(Level.SEVERE, null, ex);
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
     * @param mimeType mime type
     * @param ianaEntry metadata of the mime type
     */
    private void addMimeType(final String mimeType, final IanaEntry ianaEntry) {
        if(!mimeType.isEmpty()) {
            this._ianaDB.put(mimeType, ianaEntry);
        }
    }

    /**
     * Returns the IANA database.
     * @return the _ianaDB
     */
    public Map<String, IanaEntry> getIanaDB() {
        return _ianaDB;
    }

    /**
     * Returns the IANA metadata for a specific mime type
     * @param mimeType
     * @return 
     */
    public IanaEntry getIanaMetadata(final String mimeType) {
        return this._ianaDB.getOrDefault(mimeType, null);
    }

}
