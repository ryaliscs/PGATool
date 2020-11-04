package pgatool;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

import pgatool.file.dataexport.PGTableExporter;
import pgatool.file.dataimport.PGTableImporter;
import pgatool.properties.AppProperties;
import pgatool.properties.PGAProperties;

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
			throw new IllegalArgumentException("\n************************************************************\n"
					+ "expected arguments\n EXPORT propertiespath \n"
					+ "or \n IMPORT propertiespath"
					+ "\n************************************************************\n");
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
