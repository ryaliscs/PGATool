package pgatool.properties;

public class DBProperties {
	private final String url;
	private final String user;
	private final String password;

	public DBProperties(String url, String user, String password) {
		this.url = url;
		this.user = user;
		this.password = password;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @return the userName
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

}
