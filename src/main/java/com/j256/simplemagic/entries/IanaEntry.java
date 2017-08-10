/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.j256.simplemagic.entries;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Jean-Christophe Malapert <jean-christophe.malapert@cnes.fr>
 */
public class IanaEntry {
    
    private static final String MIME_TYPE_BASE_URL = "https://www.iana.org/assignments/media-types/";
    private static final String RFC_REFERENCE_BASE_URL = "https://tools.ietf.org/html/";    
    private static final String MIME_TYPE_REFERENCE_BASE_URL = "https://www.iana.org/assignments/media-types/media-types.xhtm";
    private static final Pattern PATTERN_REGEX = Pattern.compile("\\[(.+?)\\]");
    
    private final String _name;
    private final String _mimeType;
    private final String _mimeTypeUrl;
    private final List<String> _reference;       
    private List<String> _referenceURL;       
    
    public IanaEntry(final String name, final String mimeType, final String reference) {
        this._name = name;
        this._mimeType = mimeType;
        this._reference = parseReference(reference);
        this._mimeTypeUrl = MIME_TYPE_BASE_URL+_mimeType;
    }
    
    private List<String> parseReference(String reference) {
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
     * @return the _name
     */
    public String getName() {
        return _name;
    }

    /**
     * @return the _mimeType
     */
    public String getMimeType() {
        return _mimeType;
    }

    /**
     * @return the _mimeTypeUrl
     */
    public String getMimeTypeUrl() {
        return _mimeTypeUrl;
    }

    /**
     * @return the _reference
     */
    public List<String> getReference() {
        return _reference;
    }

    /**
     * @return the _referenceURL
     */
    public List<String> getReferenceURL() {
        return _referenceURL;
    }
    
}
