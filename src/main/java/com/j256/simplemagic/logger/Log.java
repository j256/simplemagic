package com.j256.simplemagic.logger;

/**
 * Interface so we can front various log code which may or may not be in the classpath.
 * 
 * @author graywatson
 */
public interface Log {

	/**
	 * Returns true if the log mode is in trace or higher.
	 * @param level A level to be checked
	 * @return true if the log mode is in trace or higher.
	 */
	public boolean isLevelEnabled(Level level);

	/**
	 * Log a trace message.
	 * @param level A level to log with
	 * @param message A message
	 */
	public void log(Level level, String message);

	/**
	 * Log a trace message with a throwable.
	 * @param level A level to log with
	 * @param message A message
	 * @param t An error or exception 
	 */
	public void log(Level level, String message, Throwable t);

	/**
	 * Level of log messages being sent.
	 */
	public enum Level {
		/** for tracing messages that are very verbose */
		TRACE(1),
		/** messages suitable for debugging purposes */
		DEBUG(2),
		/** information messages */
		INFO(3),
		/** warning messages */
		WARNING(4),
		/** error messages */
		ERROR(5),
		/** severe fatal messages */
		FATAL(6),
		// end
		;

		private int level;

		private Level(int level) {
			this.level = level;
		}

		/**
		 * Return whether or not a level argument is enabled for this level value. So, Level.INFO.isEnabled(Level.WARN)
		 * returns true but Level.INFO.isEnabled(Level.DEBUG) returns false.
		 * 
		 * @param otherLevel a level to be compared against
		 * @return whether this level is enabled
		 */
		public boolean isEnabled(Level otherLevel) {
			return level <= otherLevel.level;
		}
	}
}
