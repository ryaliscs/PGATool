package PGATool.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.FileHandler;

import com.google.common.io.Files;

public class PGAProperties {

	private PGAProperties() {

	}

	private Properties getProperties(String propertiesName) throws IOException {
		Properties prop = new Properties();
		try (InputStream is = getClass().getClassLoader().getResourceAsStream(propertiesName)) {
			prop.load(is);
		}
		return prop;
	}

	public static DBProperties getDBProperties() throws IOException {
		PGAProperties pgaProperties = new PGAProperties();

		Properties properties = pgaProperties.getProperties("db.properties");
		String url = properties.getProperty("url");
		String user = properties.getProperty("user");
		String password = properties.getProperty("password");
		DBProperties dbprop = new DBProperties(url, user, password);
		return dbprop;
	}

	public static List<String> getTables() throws IOException, URISyntaxException {
		List<String> tables = new ArrayList<String>();
		URI resource = PGAProperties.class.getClassLoader().getResource("tables.properties").toURI();
		Scanner sc = new Scanner(Paths.get(resource));
		while (sc.hasNextLine()) {
			tables.add(sc.nextLine());
		}
		return tables;
	}

}
