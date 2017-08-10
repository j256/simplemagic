package com.j256.simplemagic.entries;

import java.util.ArrayList;
import java.util.Iterator;
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
         * Name of the file type.
         */
        private final String name;

        /**
         * Mime type.
         */
        private final String mimeType;

        /**
         * URL describing the mime type.
         */
        private final String mimeTypeUrl;

        /**
         * Reference describing the mime type such as RFC document.
         */
        private final List<String> reference;       

        /**
         * URL of the reference
         */
        private List<String> referenceURL;       

        public IanaEntry(final String name, final String mimeType, final String ref) {
                this.name = name;
                this.mimeType = mimeType;
                this.reference = parseReference(ref);
                this.mimeTypeUrl = MIME_TYPE_BASE_URL+mimeType;
                this.referenceURL = buildUrl(reference);
        }
        
        /**
         * Returns the name of the file type.
         */
        public String getName() {
                return name;
        }

        /**
         * Returns the mime type.
         */
        public String getMimeType() {
                return mimeType;
        }

        /**
         * Returns the URL of the web page describing the mime type.
         */
        public String getMimeTypeUrl() {
                return mimeTypeUrl;
        }

        /**
         * Returns the references of the mime type such as RFC documents.
         */
        public List<String> getReference() {
                return reference;
        }

        /**
         * Returns the URL of the references such as the URL of the RFC documents.
         */
        public List<String> getReferenceURL() {
                return referenceURL;
        }        

        /**
         * Parses the references (such as RFC document) associated to a mime type.
         * One or several references can be associated to a mime type. Each 
         * reference is encompassed by this pattern [ ]
         */
        private List<String> parseReference(final String reference) {
                final List<String> refValues = new ArrayList<String>();
                final Matcher matcher = PATTERN_REGEX.matcher(reference);
                while (matcher.find()) {
                        refValues.add(matcher.group(1));
                }
                return refValues;
        }

        /**
         * Creates the URL of each reference (such as RFC document) 
         */
        private List<String> buildUrl(final List<String> references) {
                List<String> urls = new ArrayList<String>();
                Iterator<String> iter = references.listIterator();
                while(iter.hasNext()){
                        String url = iter.next();
                        if(url.toUpperCase().startsWith("RFC")) {
                                url = RFC_REFERENCE_BASE_URL+url;
                        } else if(url.startsWith("http")) {
                            // do nothing
                        } else {
                                url = MIME_TYPE_REFERENCE_BASE_URL+"#"+url;
                        }                    
                        urls.add(url);
                }
                return urls;
        }    
}
