package PGATool.properties;

public final class AppProperties {

	private static AppProperties APP_PROPERTIES;
	private String dbPropertiesFilePath;
	private String tablesPropertiesFilePath;

	private AppProperties() {

	}

	public static AppProperties getInstance() {
		if (APP_PROPERTIES == null) {
			APP_PROPERTIES = new AppProperties();
		}
		return APP_PROPERTIES;
	}

	/**
	 * @return the dbPropertiesFilePath
	 */
	public String getDbPropertiesFilePath() {
		return dbPropertiesFilePath;
	}

	/**
	 * @param dbPropertiesFilePath the dbPropertiesFilePath to set
	 */
	public void setDbPropertiesFilePath(String dbPropertiesFilePath) {
		this.dbPropertiesFilePath = dbPropertiesFilePath;
	}

	/**
	 * @return the tablesPropertiesFilePath
	 */
	public String getTablesPropertiesFilePath() {
		return tablesPropertiesFilePath;
	}

	/**
	 * @param tablesPropertiesFilePath the tablesPropertiesFilePath to set
	 */
	public void setTablesPropertiesFilePath(String tablesPropertiesFilePath) {
		this.tablesPropertiesFilePath = tablesPropertiesFilePath;
	}

}
