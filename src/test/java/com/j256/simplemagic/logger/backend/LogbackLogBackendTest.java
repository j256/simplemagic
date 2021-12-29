package com.j256.simplemagic.logger.backend;

import com.j256.simplemagic.logger.backend.LogbackLogBackend.LogbackLogBackendFactory;

public class LogbackLogBackendTest extends BaseLogBackendTest {

	public LogbackLogBackendTest() {
		super(new LogbackLogBackendFactory());
	}
}
