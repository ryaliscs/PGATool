package pgatool.file.dataexport;

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
import java.time.Duration;
import java.time.Instant;
import java.util.List;

import pgatool.connection.DBConnection;
import pgatool.file.PGAFileHelper;
import pgatool.logger.PGALogger;

public class CSVExporter {

	int total_rows_exported = 0;
	Instant tableStartTime;

	public void writeToFile(List<String> tables) throws IOException, SQLException {
		PGALogger.getLogger().info("Started exporting ( " + tables.size() + " ) tables");

		Instant start = Instant.now();
		DBConnection con = new DBConnection();
		try (Connection conn = con.connect(); Statement stmt = conn.createStatement();) {
			for (String tableName : tables) {
				tableStartTime = Instant.now();
				String tableBackupPath = getExportFilePath(tableName);
				Path path = Files.createFile(Paths.get(tableBackupPath));
				// Writing data here
				byte[] buf = getDataFromTable(stmt, tableName).getBytes();
				Files.write(path, buf);
			}
		}
		Instant finish = Instant.now();
		long totalTime = Duration.between(start, finish).toMillis();
		PGALogger.getLogger().info("---------------------------------------------------------------------------");
		PGALogger.getLogger().info("Total Number of Tables exported " + tables.size());
		PGALogger.getLogger().info("Total number of records exported in database (" + this.total_rows_exported + ")");
		PGALogger.getLogger().info("Exported in ( " + totalTime + " ) ms");
		PGALogger.getLogger().info("---------------------------------------------------------------------------");
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
			PGALogger.getLogger().severe("Missing table :" + tableName);
			// ex.printStackTrace();
		}
		return "";
	}

	private void addData(StringBuilder sb, ResultSet rs) throws SQLException, IOException {
		ResultSetMetaData metaData = rs.getMetaData();
		int rowCount = 0;
		while (rs.next()) {
			rowCount++;
			int columnCount = metaData.getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				String columnLabel = metaData.getColumnLabel(i);
				String value = PGAFileHelper.replaceTheDelimeterInValue(rs.getString(columnLabel));
				// PGALogger.getLogger().info(columnLabel + "-" + value);
				sb.append(value);
				if (i != columnCount) {
					sb.append(",");
				}
			}
			sb.append("\n");
		}
		this.total_rows_exported += rowCount;
		Instant finish = Instant.now();
		long totalTime = Duration.between(tableStartTime, finish).toMillis();
		PGALogger.getLogger().info(
				"Exported " + metaData.getTableName(1) + " with (" + rowCount + ") records in (" + totalTime + ") ms");
	}

	private void prepareHeader(StringBuilder sb, ResultSetMetaData metaData) throws SQLException {
		sb.append(metaData.getColumnName(1));
		for (int i = 2; i <= metaData.getColumnCount(); i++) {
			sb.append(",").append(metaData.getColumnName(i));
		}
		sb.append("\n");
	}
}
