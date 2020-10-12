package PGATool.file.dataexport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Logger;

import PGATool.connection.DBConnection;
import PGATool.file.PGAFileHelper;

public class CSVExporter {

	private final static Logger LOGGER = Logger.getLogger(CSVExporter.class.getName());
	int total_rows_exported = 0;

	public void writeToFile(List<String> tables) throws IOException, SQLException {

		DBConnection con = new DBConnection();
		try (Connection conn = con.connect(); Statement stmt = conn.createStatement();) {
			for (String tableName : tables) {
				LOGGER.info("Exporting data from: " + tableName);
				String tableBackupPath = getExportFilePath(tableName);
				Path path = Files.createFile(Paths.get(tableBackupPath));
				// Writing data here
				byte[] buf = getDataFromTable(stmt, tableName).getBytes();
				Files.write(path, buf);
				LOGGER.info("Exported data of: " + tableName);
			}
		}

		LOGGER.info("Total number of records exported in database (" + this.total_rows_exported + ")");
	}

	private String getExportFilePath(String tableName) throws FileNotFoundException, IOException {
		File dmdBackup = PGAFileHelper.getBackupPath();
		String tableBackupPath = dmdBackup.toPath().toString() + "/" + tableName + ".csv";
		PGAFileHelper.deleteFileIfExits(tableBackupPath);
		return tableBackupPath;
	}

	private String getDataFromTable(Statement stmt, String tableName) throws IOException {
		try (ResultSet rs = stmt.executeQuery("select * from " + tableName)) {
			StringBuilder sb = new StringBuilder();
			prepareHeader(sb, rs.getMetaData());
			addData(sb, rs);
			return sb.toString();
		} catch (SQLException ex) {
			LOGGER.severe("Missing table :" + tableName);
			// ex.printStackTrace();
		}
		return "";
	}

	private void addData(StringBuilder sb, ResultSet rs) throws SQLException {
		ResultSetMetaData metaData = rs.getMetaData();
		int rowCount = 0;
		while (rs.next()) {
			rowCount++;
			int columnCount = metaData.getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				String columnLabel = metaData.getColumnLabel(i);
				String value = PGAFileHelper.replaceTheDelimeterInValue(rs.getString(columnLabel));
				// LOGGER.info(columnLabel + "-" + value);
				sb.append(value);
				if (i != columnCount) {
					sb.append(",");
				}
			}
			sb.append("\n");
		}
		this.total_rows_exported += rowCount;
		LOGGER.info("Total number of records exported (" + rowCount+")");
	}

	private void prepareHeader(StringBuilder sb, ResultSetMetaData metaData) throws SQLException {
		sb.append(metaData.getColumnName(1));
		for (int i = 2; i <= metaData.getColumnCount(); i++) {
			sb.append(",").append(metaData.getColumnName(i));
		}
		sb.append("\n");
	}
}
