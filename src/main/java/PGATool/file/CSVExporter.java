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

public class CSVExporter {
	
	public void writeToFile(String tableName) throws IOException {
		String tableBackupPath = getExportFilePath(tableName);
		Path path = Files.createFile(Paths.get(tableBackupPath));

		// Writing data here
		byte[] buf = getDataFromTable(tableName).getBytes();
		Files.write(path, buf);
	}

	private String getExportFilePath(String tableName) {
		File dmdBackup = PGAFileHelper.getDMDBackupPath();
		String tableBackupPath = dmdBackup.toPath().toString() + "/" + tableName + ".csv";
		PGAFileHelper.deleteFileIfExits(tableBackupPath);
		return tableBackupPath;
	}


	private String getDataFromTable(String tableName) throws IOException {
		DBConnection con = new DBConnection();
		try (Connection conn = con.connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("select * from " + tableName)) {
			StringBuilder sb = new StringBuilder();
			prepareHeader(sb, rs.getMetaData());
			addData(sb, rs);
			return sb.toString();
		} catch (SQLException ex) {
			ex.printStackTrace();
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
