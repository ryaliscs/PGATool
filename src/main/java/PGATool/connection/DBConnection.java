package pgatool.connection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import pgatool.logger.PGALogger;
import pgatool.properties.DBProperties;
import pgatool.properties.PGAProperties;

/**
 * Class to establish the Database connection with the given
 * {@link DBProperties}
 * 
 * @author Sarat
 *
 */
public final class DBConnection {

	/**
	 * Returns the DB connection
	 * 
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public Connection connect() throws SQLException, IOException {
		DBProperties dbProperties = PGAProperties.getDBProperties();
		String urlString = dbProperties.getUrl();
		int endIndex = urlString.contains("?") ? urlString.indexOf('?') : urlString.length();
		String dbName = urlString.substring(urlString.lastIndexOf('/') + 1, endIndex);
		PGALogger.getLogger().info("---------------------------------------------\n" + //
				"Connected to Data Base:\n" + //
				"Name :" + dbName + "\n" + //
				"User Name:" + dbProperties.getUser() + "\n" + //
				"---------------------------------------------");
		return DriverManager.getConnection(urlString, //
				dbProperties.getUser(), dbProperties.getPassword());
	}

}
