package pgatool.file.dataimport;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.common.io.Files;

import pgatool.connection.DBConnection;
import pgatool.file.PGAFileHelper;
import pgatool.logger.PGALogger;

public class CSVReader {
	private int total_rows_count = 0;
	Instant tableStartTime;
	int recordCount=0;

	public void importData(List<String> tables) throws IOException, SQLException {
		Instant start = Instant.now();
		DBConnection dbcon = new DBConnection();
		Map<String, Path> filePathsFromBackupSource = PGAFileHelper.getFilePathsFromBackupSource();
		try (Connection conn = dbcon.connect(); Statement stmt = conn.createStatement();) {
			PGALogger.getLogger().info("Started Importing (" + tables.size() + ") tables");
			PGALogger.logSeparator();
			
			for (int i = 0; i < tables.size(); i++) {
				tableStartTime = Instant.now();
				String table = tables.get(i);				
				readCSVFile(stmt, table, filePathsFromBackupSource.get(table));
			}
		}

		Instant finish = Instant.now();
		long totalTime = Duration.between(start, finish).toMillis();
		PGALogger.logSeparator();
		PGALogger.getLogger().info("Total Number of Tables imported " + tables.size());
		PGALogger.getLogger().info("Total number of records imported in database (" + this.total_rows_count + ")");
		PGALogger.getLogger().info("Imported in " + totalTime + " ms, Start Time:"+ Date.from(start) + ", End Time:" + Date.from(finish));
		PGALogger.logSeparator();
	}

	private void readCSVFile(Statement stmt, String tableName, Path path) throws IOException, SQLException {

		List<String> insertStatements = getInsertStatements(stmt, tableName, path);
		updateDataBase(stmt, tableName, insertStatements);
		int rowsInTable = insertStatements.size();
		total_rows_count += rowsInTable;
		Instant finish = Instant.now();			
		PGALogger.logImport(++recordCount, tableName, rowsInTable, tableStartTime, finish);
	}

	private void updateDataBase(Statement stmt, String tableName, List<String> insertStatements)
			throws SQLException, IOException {
		for (String insertStatement : insertStatements) {
//				LOGGER.info(insertStatement);
			stmt.execute(insertStatement);
		}
	}

	private List<String> getInsertStatements(Statement stmt, String tableName, Path path)
			throws SQLException, IOException {
		List<String> insertStatements = new ArrayList<>();
		Map<String, String> metaData = getMetaData(stmt, tableName);

		List<String> readLines = Files.readLines(new File(path.toUri()), Charset.defaultCharset());
		if (readLines.size() > 0) {
			String header = readLines.get(0);
			String[] headerColumns = header.split(",");

			for (int i = 1; i < readLines.size(); i++) {
				StringBuilder sb = new StringBuilder();
				sb.append("INSERT INTO " + tableName);
				sb.append("(").append(header).append(")");
				sb.append("VALUES (");
				String[] values = readLines.get(i).split(",");
				String v = getActualValue(values[0], headerColumns[0], metaData);
				for (int j = 1; j < values.length; j++) {
					v = v + ",";
					v = v + getActualValue(values[j], headerColumns[j], metaData);
				}
				v = v + ")";
				sb.append(v);
				insertStatements.add(sb.toString());
			}
		}
		return insertStatements;
	}

	private String getActualValue(String value, String headerColumn, Map<String, String> metaData) throws IOException {
		String result = "";
		switch (metaData.get(headerColumn)) {
		case "int8":
		case "int4":
			result = resolveNull(value);
			break;
		case "varchar":
		case "text":
		case "bytea":
			result = resolveStringNull(value);
			break;
		case "timestamp":
			result = resolveStringNull(value);
			break;
		case "bool":
			result = resolveStringNull(value);
			break;
		default:
			PGALogger.getLogger().info(metaData.toString());
			assert true : "cannot find right type";
		}
		return PGAFileHelper.correctTheDelimeterInValue(result);
	}

	private String resolveStringNull(String value) {
		return value != null && !value.equals("null") ? "'" + value + "'" : null;
	}

	private String resolveNull(String value) {
		return value != null ? value : null;
	}

	private Map<String, String> getMetaData(Statement stmt, String tableName) throws SQLException, IOException {
		Map<String, String> mapColumnType = new HashMap<>();
		try (ResultSet rs = stmt.executeQuery("select * from " + tableName)) {
			ResultSetMetaData metaData = rs.getMetaData();
			for (int i = 1; i <= metaData.getColumnCount(); i++) {
				mapColumnType.put(metaData.getColumnName(i), metaData.getColumnTypeName(i));
			}
		}
		return mapColumnType;
	}

}
