package com.j256.simplemagic;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

import com.j256.simplemagic.ContentInfoUtil.ErrorCallBack;

@Ignore("For now we have to skip this, sigh")
public class MagicFileTest {

	@Test
	public void testMagicFileParse() throws IOException {
		new ContentInfoUtil("/magic.gz", new ErrorCallBack() {
			@Override
			public void error(String line, String details, Exception e) {
				fail("Got this error: '" + details + "' for line: " + line);
			}
		});
	}
}
