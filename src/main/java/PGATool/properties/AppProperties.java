package PGATool.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public final class AppProperties {

	private static AppProperties APP_PROPERTIES;
	private String dbPropertiesFilePath;
	private String tablesPropertiesFilePath;
	private String backupFilesPath;

	private AppProperties() {
	}

	public static AppProperties getInstance() throws FileNotFoundException, IOException {
		if (APP_PROPERTIES == null) {
			APP_PROPERTIES = new AppProperties();			
		}
		return APP_PROPERTIES;
	}

	/**
	 * init App properties
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void init(String aPropertiesFilePath) throws FileNotFoundException, IOException {
		this.dbPropertiesFilePath = aPropertiesFilePath;
		Properties properties = new Properties();
		properties.load(new FileInputStream(new File(this.dbPropertiesFilePath)));
		this.tablesPropertiesFilePath = properties.getProperty("tables");
		this.backupFilesPath = properties.getProperty("backupPath");
	}
	

	/**
	 * @return the dbPropertiesFilePath
	 */
	public String getDbPropertiesFilePath() {
		return dbPropertiesFilePath;
	}

	/**
	 * @return the tablesPropertiesFilePath
	 */
	public String getTablesPropertiesFilePath() {
		return tablesPropertiesFilePath;
	}

	/**
	 * @return the backupFilesPath
	 */
	public String getBackupFilesPath() {
		return backupFilesPath;
	}

}
