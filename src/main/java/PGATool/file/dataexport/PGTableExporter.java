package PGATool.file.dataexport;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class PGTableExporter {
	private final static Logger LOGGER = Logger.getLogger(PGTableExporter.class.getName());

	public void export(List<String> tables) throws IOException, SQLException {

		CSVExporter exporter = new CSVExporter();
		exporter.writeToFile(tables);
		LOGGER.info("Exporting of tables finished");
	}

}
