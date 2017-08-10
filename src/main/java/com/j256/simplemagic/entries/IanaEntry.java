package com.j256.simplemagic.entries;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <a href="https://www.iana.org/assignments/media-types/media-types.xhtml">IANA metadata</a> coming from
 * @author Jean-Christophe Malapert (jcmalapert@gmail.com)
 */
public class IanaEntry {
    
    private static final String MIME_TYPE_BASE_URL = "https://www.iana.org/assignments/media-types/";
    private static final String RFC_REFERENCE_BASE_URL = "https://tools.ietf.org/html/";    
    private static final String MIME_TYPE_REFERENCE_BASE_URL = "https://www.iana.org/assignments/media-types/media-types.xhtm";
    private static final Pattern PATTERN_REGEX = Pattern.compile("\\[(.+?)\\]");
    
    /**
     * Name of the file type
     */
    private final String _name;
    
    /**
     * Mime type
     */
    private final String _mimeType;
    
    /**
     * URL describing the mime type
     */
    private final String _mimeTypeUrl;
    
    /**
     * Reference describing the mime type such as RFC
     */
    private final List<String> _reference;       
    
    /**
     * URL of the reference
     */
    private List<String> _referenceURL;       
    
    public IanaEntry(final String name, final String mimeType, final String reference) {
        this._name = name;
        this._mimeType = mimeType;
        this._reference = parseReference(reference);
        this._mimeTypeUrl = MIME_TYPE_BASE_URL+_mimeType;
        this._referenceURL = buildUrl(_reference);
    }
    
    /**
     * Parse the references and creates one entry by reference.
     * @param reference
     * @return the references.
     */
    private List<String> parseReference(final String reference) {
        final List<String> refValues = new ArrayList<String>();
        final Matcher matcher = PATTERN_REGEX.matcher(reference);
        while (matcher.find()) {
            System.out.println(matcher.group(1));
            refValues.add(matcher.group(1));
        }
        return refValues;
    }
    
    
    private List<String> buildUrl(final List<String> references) {
        List<String> urls = new ArrayList<String>(references);
        for(String url:urls) {
            if(url.toUpperCase().startsWith("RFC")) {
                url = RFC_REFERENCE_BASE_URL+url;
            } else if(url.startsWith("http")) {
                // do nothing
            } else {
                url = MIME_TYPE_REFERENCE_BASE_URL+"#"+url;
            }
        }
        return urls;
    }

    /**
     * Returns the name of the file type.
     * @return the _name
     */
    public String getName() {
        return _name;
    }

    /**
     * Returns the mime type.
     * @return the _mimeType
     */
    public String getMimeType() {
        return _mimeType;
    }

    /**
     * Returns the URL describing the mime type.
     * @return the _mimeTypeUrl
     */
    public String getMimeTypeUrl() {
        return _mimeTypeUrl;
    }

    /**
     * Returns the references of the mime type.
     * @return the _reference
     */
    public List<String> getReference() {
        return _reference;
    }

    /**
     * Returns the URL of the references.
     * @return the _referenceURL
     */
    public List<String> getReferenceURL() {
        return _referenceURL;
    }
    
}
