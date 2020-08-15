package PGATool.file.dataexport;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class PGTableExporter {

	public void export(List<String> tables) throws IOException, SQLException {

		CSVExporter exporter = new CSVExporter();
		exporter.writeToFile(tables);
	}

}
