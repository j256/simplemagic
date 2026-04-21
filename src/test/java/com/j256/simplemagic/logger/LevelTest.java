package com.j256.simplemagic.logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.Test;

public class LevelTest {

	@Test
	public void testCoverage() {
		assertFalse(Level.OFF.isEnabled(Level.TRACE));
		assertFalse(Level.TRACE.isEnabled(Level.OFF));

		// if outer level is in place then lesser levels should not be enabled
		for (Level outer : Level.values()) {
			if (outer == Level.OFF) {
				continue;
			}
			for (Level inner : Level.values()) {
				if (inner == Level.OFF) {
					continue;
				}
				if (inner == outer) {
					break;
				}
				assertFalse(outer + ".isEnabled(" + inner + ") should be false", outer.isEnabled(inner));
			}
		}

		// if outer level is in place then the same or greater levels should be enabled
		for (Level outer : Level.values()) {
			if (outer == Level.OFF) {
				continue;
			}
			boolean compare = false;
			for (Level inner : Level.values()) {
				if (inner == Level.OFF) {
					continue;
				}
				if (inner == outer) {
					compare = true;
				}
				if (compare) {
					assertTrue(outer + ".isEnabled(" + inner + ") should be true", outer.isEnabled(inner));
				}
			}
		}

		// some explicit tests
		assertTrue(Level.TRACE.isEnabled(Level.INFO));
		assertFalse(Level.INFO.isEnabled(Level.TRACE));
	}

	@Test
	public void testFromString() {
		assertNull(Level.fromString(null));
		assertNull(Level.fromString(""));
		for (Level level : Level.values()) {
			assertEquals(level, Level.fromString(level.name()));
			assertTrue(Level.fromString(level.name().toLowerCase()) == level
					|| Level.fromString(level.name().toLowerCase(Locale.ENGLISH)) == level);
		}
		assertNull(Level.fromString("unknown"));
	}
}
