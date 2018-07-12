package com.j256.simplemagic.entries;

import java.util.ArrayList;
import java.util.List;

/**
 * <a href="https://www.iana.org/assignments/media-types/media-types.xhtml">IANA metadata</a>.
 * 
 * @author Jean-Christophe Malapert
 */
public class IanaEntry {

	private static final String MIME_TYPE_BASE_URL = "https://www.iana.org/assignments/media-types/";
	private static final String RFC_REFERENCE_BASE_URL = "https://tools.ietf.org/html/";
	private static final String MIME_TYPE_REFERENCE_BASE_URL =
			"https://www.iana.org/assignments/media-types/media-types.xhtm";

	/** Name of the file type. */
	private final String name;
	private final String mimeType;
	/** URL describing the mime type. */
	private final String mimeTypeUrl;
	/** Reference describing the mime type such as RFC document. */
	private final List<String> references;
	/** URL of the reference */
	private final List<String> referenceUrls;

	public IanaEntry(String name, String mimeType, List<String> references) {
		this.name = name;
		this.mimeType = mimeType;
		this.mimeTypeUrl = MIME_TYPE_BASE_URL + mimeType;
		this.references = references;
		this.referenceUrls = buildUrls(references);
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
	 * Returns the reference(s) of the mime type such as RFC documents.
	 */
	public List<String> getReferences() {
		return references;
	}

	/**
	 * Returns the URL(s) of the reference(s) such as the URL of the RFC documents.
	 */
	public List<String> getReferenceUrls() {
		return referenceUrls;
	}

	/**
	 * Creates the URL of each reference (such as RFC document)
	 */
	private List<String> buildUrls(List<String> references) {
		List<String> urls = new ArrayList<String>(references.size());
		for (String url : references) {
			if (url.toUpperCase().startsWith("RFC")) {
				url = RFC_REFERENCE_BASE_URL + url;
			} else if (url.startsWith("http")) {
				// already an URL so do nothing
			} else {
				url = MIME_TYPE_REFERENCE_BASE_URL + "#" + url;
			}
			urls.add(url);
		}
		return urls;
	}
}
