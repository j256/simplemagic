package com.j256.simplemagic.logger.backend;

import com.j256.simplemagic.logger.backend.JavaUtilLogBackend.JavaUtilLogBackendFactory;

public class JavaUtilLogBackendTest extends BaseLogBackendTest {

	public JavaUtilLogBackendTest() {
		super(new JavaUtilLogBackendFactory());
	}
}
