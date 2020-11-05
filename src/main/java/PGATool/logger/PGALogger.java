package pgatool.logger;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public final class PGALogger {

	private static final String LOG_SEPARATOR = "---------------------------------------------------------------------------";
	private static Logger LOGGER;

	private PGALogger() {

	}

	public static Logger getLogger() throws IOException {
		if (LOGGER == null) {
			String fileName = "pgtool_" + System.currentTimeMillis() + ".log";
			PGALogFormatter formatterTxt = new PGALogFormatter();
			FileHandler fileHandler = new FileHandler(fileName, true);
			fileHandler.setFormatter(formatterTxt);
			LOGGER = Logger.getLogger(PGALogger.class.getName());
			LOGGER.addHandler(fileHandler);
		}
		return LOGGER;
	}

	public static void logExport(int recordCount, String tableName, int rowCount, Instant tableStartTime, Instant finish) throws IOException {
		log(recordCount, tableName, rowCount, tableStartTime, finish);
	}

	public static void logImport(int recordCount, String tableName, int rowCount, Instant tableStartTime, Instant finish) throws IOException {
		log(recordCount, tableName, rowCount, tableStartTime, finish);
	}

	public static void logSeparator() throws IOException {
		getLogger().info(LOG_SEPARATOR);
	}

	private static void log(int recordCount, String tableName, int rowCount, Instant tableStartTime, Instant finish) throws IOException {
		long totalTime = Duration.between(tableStartTime, finish).toMillis();
		getLogger().info(recordCount+". " + tableName + " with (" + rowCount + ") records in " + totalTime + " ms, Start Time:"
				+ Date.from(tableStartTime) + ", End Time:" + Date.from(finish));
	}

}
