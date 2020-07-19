package PGATool.file;

import java.io.IOException;
import java.sql.SQLException;

public class PGTableExporter {

	public void export(String tableName) throws IOException, SQLException {

		CSVExporter exporter = new CSVExporter();
		exporter.writeToFile(tableName);
	}

}
