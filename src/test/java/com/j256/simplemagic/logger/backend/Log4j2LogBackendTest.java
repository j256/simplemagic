package com.j256.simplemagic.logger.backend;

import org.junit.Ignore;

import com.j256.simplemagic.logger.backend.Log4j2LogBackend.Log4j2LogBackendFactory;

@Ignore("works under java 8 but not < 8")
public class Log4j2LogBackendTest extends BaseLogBackendTest {

	public Log4j2LogBackendTest() {
		super(new Log4j2LogBackendFactory());
	}
}
