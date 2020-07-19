package PGATool.connection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import PGATool.properties.DBProperties;
import PGATool.properties.PGAProperties;

public final class DBConnection {

	public Connection connect() throws SQLException, IOException {
		DBProperties dbProperties = PGAProperties.getDBProperties();
		return DriverManager.getConnection(dbProperties.getUrl(), //
				dbProperties.getUser(), dbProperties.getPassword());
	}

	
}
