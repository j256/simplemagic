package com.j256.simplemagic.logger.backend;

import com.j256.simplemagic.logger.backend.Log4jLogBackend.Log4jLogBackendFactory;

public class Log4jLogBackendTest extends BaseLogBackendTest {

	public Log4jLogBackendTest() {
		super(new Log4jLogBackendFactory());
	}
}
