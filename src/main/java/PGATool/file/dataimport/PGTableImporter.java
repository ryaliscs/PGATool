package PGATool.file.dataimport;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import PGATool.connection.DBConnection;
import PGATool.properties.PGAProperties;

public class PGTableImporter {

	public void importData() throws IOException, URISyntaxException, SQLException {
		CSVReader reader = new CSVReader();
		List<String> tables = PGAProperties.getTables();
		System.out.println("Started cleaning the database tables....");
		cleanup(tables);
		System.out.println("Cleaning the database tables....Done");

		reader.importData(tables);

	}

	private void cleanup(List<String> tables) throws SQLException, IOException {
		DBConnection dbcon = new DBConnection();
		try (Connection conn = dbcon.connect(); Statement stmt = conn.createStatement();) {
			for (int i = tables.size() - 1; i >= 0; i--) {
				stmt.execute("delete from " + tables.get(i));
			}
		}
	}

}
