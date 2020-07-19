package PGATool;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

import PGATool.file.PGTableExporter;
import PGATool.properties.PGAProperties;

public class PGATool {

	public static void main(String[] args) throws IOException, URISyntaxException, SQLException {

		PGATool pgaTool = new PGATool();
		pgaTool.execute();
	}
	
	public void execute() throws IOException, URISyntaxException, SQLException {
		PGTableExporter exporter = new PGTableExporter();
		for (String table : PGAProperties.getTables()) {
			exporter.export(table);
		}
	}
}