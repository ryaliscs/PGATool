package PGATool.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import PGATool.connection.DBConnection;

public class PGTableExporter {

	public void export(String tableName) throws IOException, SQLException {

		String result = getDataFromTable(tableName);

		System.out.println(result);

		writeToFile(tableName, result);
	}

	private void writeToFile(String tableName, String result) throws IOException {
		String tableBackupPath = getExportFilePath(tableName);
		Path path = Files.createFile(Paths.get(tableBackupPath));
		System.out.println("Temp file : " + path);

		// Writing data here
		byte[] buf = result.getBytes();
		Files.write(path, buf);
	}

	private String getExportFilePath(String tableName) {
		String property = "java.io.tmpdir";
		String tempDir = System.getProperty(property);
		File dmdBackup = new File(tempDir + "/dmdBackup");
		dmdBackup.mkdir();
		String tableBackupPath = dmdBackup.toPath().toString() + "/" + tableName + ".csv";
		deleteFileIfExits(tableBackupPath);
		return tableBackupPath;
	}

	private void deleteFileIfExits(String tableBackupPath) {
		File file = new File(tableBackupPath);
		if (file.exists()) {
			file.delete();
		}

	}

	public String getDataFromTable(String tableName) throws IOException {
		DBConnection con = new DBConnection();
		try (Connection conn = con.connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("select * from " + tableName)) {
			StringBuilder sb = new StringBuilder();
			prepareHeader(sb, rs.getMetaData());
			addData(sb, rs);
			return sb.toString();
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
		return "";
	}

	private void addData(StringBuilder sb, ResultSet rs) throws SQLException {
		ResultSetMetaData metaData = rs.getMetaData();
		while (rs.next()) {
			for (int i = 1; i <= metaData.getColumnCount(); i++) {
				sb.append(rs.getString(i));
				if (i != metaData.getColumnCount()) {
					sb.append(",");
				}
			}
			sb.append("\n");
		}
	}

	private void prepareHeader(StringBuilder sb, ResultSetMetaData metaData) throws SQLException {
		sb.append(metaData.getColumnName(1));
		for (int i = 2; i <= metaData.getColumnCount(); i++) {
			sb.append(",").append(metaData.getColumnName(i));
		}
		sb.append("\n");
	}

}
