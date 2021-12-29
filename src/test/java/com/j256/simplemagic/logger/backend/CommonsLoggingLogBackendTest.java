package com.j256.simplemagic.logger.backend;

import com.j256.simplemagic.logger.backend.CommonsLoggingLogBackend.CommonsLoggingLogBackendFactory;

public class CommonsLoggingLogBackendTest extends BaseLogBackendTest {

	public CommonsLoggingLogBackendTest() {
		super(new CommonsLoggingLogBackendFactory());
	}
}
