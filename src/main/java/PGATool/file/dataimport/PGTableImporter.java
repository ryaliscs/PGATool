package pgatool.file.dataimport;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Logger;

import pgatool.connection.DBConnection;
import pgatool.properties.PGAProperties;

public class PGTableImporter {
	private final static Logger LOGGER = Logger.getLogger(PGTableImporter.class.getName());

	public void importData() throws IOException, URISyntaxException, SQLException {
		CSVReader reader = new CSVReader();
		List<String> tables = PGAProperties.getTables();
		LOGGER.info("Started cleaning the database tables....");
		cleanup(tables);
		LOGGER.info("Cleaning the database tables....Done");

		reader.importData(tables);

		LOGGER.info("Importing of tables finished");

	}

	private void cleanup(List<String> tables) throws SQLException, IOException {
		DBConnection dbcon = new DBConnection();
		try (Connection conn = dbcon.connect(false/* logger disabled */); Statement stmt = conn.createStatement();) {
			for (int i = tables.size() - 1; i >= 0; i--) {
				try {
					stmt.execute("delete from " + tables.get(i));
				} catch (Exception ex) {
					LOGGER.info("cannot find table " + tables.get(i));

				}
			}
		}
	}

}
