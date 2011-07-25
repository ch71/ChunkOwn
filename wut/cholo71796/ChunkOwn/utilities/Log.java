package wut.cholo71796.ChunkOwn.utilities;

import java.util.logging.Logger;

import wut.cholo71796.ChunkOwn.ChunkOwnConfig;

public final class Log {
	private static Logger logger;
	private static String pluginName;
	private static ChunkOwnConfig majorConfig;
	
	public Log(String pluginName) {
		Log.pluginName = pluginName;
		logger = Logger.getLogger(pluginName);
		majorConfig = new ChunkOwnConfig();
	}
	
	public static void info(String message) {
		logger.info("[" + pluginName + "] " + message);
	}
	
	public static void warning(String message) {
		logger.warning("[" + pluginName + "] " + message);
	}
	
	public static void severe(String message) {
		logger.severe("[" + pluginName + "] " + message);
	}
	
	public static void debug(String message) {
		if (majorConfig.isVerbose())
			logger.info("[" + pluginName + "] [DEBUG] " + message);
	}
}
