package PGATool;

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

	private static void initApplication(String[] args) {
		if (args.length == 3) {
			IS_EXPORT = args[0].equalsIgnoreCase("EXPORT");
			assert IS_EXPORT : "expected EXPORT";
			AppProperties.getInstance().setDbPropertiesFilePath(args[1]);
			AppProperties.getInstance().setTablesPropertiesFilePath(args[2]);
		} else if (args.length == 4) {
			IS_EXPORT = !args[0].equalsIgnoreCase("IMPORT");
			assert !IS_EXPORT : "expected IMPORT";
			AppProperties.getInstance().setDbPropertiesFilePath(args[1]);
			AppProperties.getInstance().setTablesPropertiesFilePath(args[2]);
			AppProperties.getInstance().setBackupFilesPath(args[3]);
		} else {
			throw new IllegalArgumentException("expected arguments EXPORT dbpropertiespath tablepropertiespath \n"
					+ "or \n IMPORT dbpropertiespath tablepropertiespath backuppath");
		}
	}

	public void exportDATA() throws IOException, URISyntaxException, SQLException {
		PGTableExporter exporter = new PGTableExporter();
		for (String table : PGAProperties.getTables()) {
			exporter.export(table);
		}
	}

	private void importDATA() throws IOException, URISyntaxException {
		PGTableImporter importer = new PGTableImporter();
		importer.importData();
	}

}
