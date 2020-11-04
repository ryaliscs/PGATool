package pgatool.logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public final class PGALogger {

	private static Logger LOGGER;

	private PGALogger() {

	}

	public static Logger getLogger() throws IOException {
		if (LOGGER == null) {
			String fileName = "pgtool_"+System.currentTimeMillis()+".log";
			PGALogFormatter formatterTxt = new PGALogFormatter();
			FileHandler fileHandler = new FileHandler(fileName, true);
			fileHandler.setFormatter(formatterTxt);
			LOGGER = Logger.getLogger(PGALogger.class.getName());
			LOGGER.addHandler(fileHandler);
		}
		return LOGGER;
	}
}
