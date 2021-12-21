package com.j256.simplemagic.logger;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LogBackendTypeTest {

	@Test
	public void testStuff() {
		for (LogBackendType type : LogBackendType.values()) {
			if (type == LogBackendType.LOG4J2) {
				// works under java 8 and not < 8
				continue;
			}
			if (type == LogBackendType.NULL) {
				assertFalse(type + " should not be available", type.isAvailable());
				// NOTE: type.createLogBackend() defers to LocalLog
				continue;
			}

			assertTrue(type + " should be available", type.isAvailable());
			assertNotNull(type.createLogBackend(getClass().getSimpleName()));
		}
	}
}
