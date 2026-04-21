package com.j256.simplemagic.logger.backend;

import com.j256.simplemagic.logger.backend.ConsoleLogBackend.ConsoleLogBackendFactory;

public class ConsoleLogBackendTest extends BaseLogBackendTest {

	public ConsoleLogBackendTest() {
		super(new ConsoleLogBackendFactory());
	}
}
