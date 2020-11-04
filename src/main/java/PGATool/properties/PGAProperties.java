package pgatool.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class PGAProperties {

	private static PGAProperties PGA_PROPERTIES;

	private PGAProperties() {
	}

	public static PGAProperties getInstance() {
		if (PGA_PROPERTIES == null) {
			PGA_PROPERTIES = new PGAProperties();
		}
		return PGA_PROPERTIES;
	}

	public static DBProperties getDBProperties() throws IOException {
		Properties properties = new Properties();
		properties.load(new FileInputStream(new File(AppProperties.getInstance().getDbPropertiesFilePath())));
		String url = properties.getProperty("url");
		String user = properties.getProperty("user");
		String password = properties.getProperty("password");
		DBProperties dbprop = new DBProperties(url, user, password);
		return dbprop;
	}

	public static List<String> getTables() throws IOException, URISyntaxException {
		List<String> tables = new ArrayList<String>();
		try (Scanner sc = new Scanner(new File(AppProperties.getInstance().getTablesPropertiesFilePath()))) {
			while (sc.hasNextLine()) {
				tables.add(sc.nextLine());
			}
		}
		return tables;
	}

}
