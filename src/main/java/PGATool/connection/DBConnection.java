package PGATool.connection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

import PGATool.properties.DBProperties;
import PGATool.properties.PGAProperties;

/**
 * Class to establish the Database connection with the given
 * {@link DBProperties}
 * 
 * @author Sarat
 *
 */
public final class DBConnection {
	private final static Logger LOGGER = Logger.getLogger(DBConnection.class.getName());

	/**
	 * Returns the DB connection
	 * 
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public Connection connect() throws SQLException, IOException {
		DBProperties dbProperties = PGAProperties.getDBProperties();
		LOGGER.info("---------------------------------------------\n" + //
				"Connect to Data Base:\n" + //
				"URL:" + dbProperties.getUrl() + "\n" + //
				"User Name:" + dbProperties.getUser() + "\n" + //
				"---------------------------------------------");
		return DriverManager.getConnection(dbProperties.getUrl(), //
				dbProperties.getUser(), dbProperties.getPassword());
	}

}
