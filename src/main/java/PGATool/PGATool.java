package PGATool;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

import PGATool.file.dataexport.PGTableExporter;
import PGATool.file.dataimport.PGTableImporter;
import PGATool.properties.AppProperties;
import PGATool.properties.PGAProperties;

public class PGATool {
	public static boolean IS_EXPORT = true;

	public static void main(String[] args) throws IOException, URISyntaxException, SQLException {
		PGATool pgaTool = new PGATool();

		initApplication(args);
		if (IS_EXPORT) {
			pgaTool.exportDATA();
		} else {
			pgaTool.importDATA();
		}

	}

	private static void initApplication(String[] args) throws FileNotFoundException, IOException {
		if (args.length == 2) {
			IS_EXPORT = args[0].equalsIgnoreCase("EXPORT");			
			AppProperties.getInstance().init(args[1]);
		} else {
			throw new IllegalArgumentException("expected arguments EXPORT dbpropertiespath tablepropertiespath \n"
					+ "or \n IMPORT dbpropertiespath tablepropertiespath backuppath");
		}
	}

	public void exportDATA() throws IOException, URISyntaxException, SQLException {
		PGTableExporter exporter = new PGTableExporter();
		exporter.export(PGAProperties.getTables());
	}

	private void importDATA() throws IOException, URISyntaxException, SQLException {
		PGTableImporter importer = new PGTableImporter();
		importer.importData();
	}

}
