package PGATool.file.dataimport;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.io.Files;

import PGATool.connection.DBConnection;

public class CSVReader {

	public void readCSVFile(String tableName, Path path) throws IOException, SQLException {

		List<String> insertStatements = getInsertStatements(tableName, path);
		updateDataBase(tableName, insertStatements);
	}

	private void updateDataBase(String tableName, List<String> insertStatements) throws SQLException, IOException {
		DBConnection dbcon = new DBConnection();
		System.out.println("Inserting into " + tableName + ":");
		try (Connection conn = dbcon.connect(); Statement stmt = conn.createStatement();) {
			for (String insertStatement : insertStatements) {
				System.out.println(insertStatement);
				stmt.execute(insertStatement);
			}
		}
	}

	private List<String> getInsertStatements(String tableName, Path path) throws SQLException, IOException {
		List<String> insertStatements = new ArrayList<>();
		Map<String, String> metaData = getMetaData(tableName);

		List<String> readLines = Files.readLines(new File(path.toUri()), Charset.defaultCharset());
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
		return insertStatements;
	}

	private String getActualValue(String value, String headerColumn, Map<String, String> metaData) {
		String result = "";
		switch (metaData.get(headerColumn)) {
		case "int8":
		case "int4":
			result = resolveNull(value);
			break;
		case "varchar":
		case "text":
			result = resolveStringNull(value);
			break;
		case "timestamp":
			result = "'" + resolveNull(value)+ "'";
			break;
		case "bool":
			result = resolveStringNull(value);
			break;
		default:
			System.out.println(metaData.toString());
			assert true : "cannot find right type";
		}
		return result;
	}

	private String resolveStringNull(String value) {
		return value != null && !value.equals("null") ? "'" +value+ "'" : null;
	}

	private String resolveNull(String value) {
		return value != null ? value : null;
	}

	private Map<String, String> getMetaData(String tableName) throws SQLException, IOException {
		DBConnection dbcon = new DBConnection();
		Map<String, String> mapColumnType = new HashMap<>();
		try (Connection conn = dbcon.connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("select * from " + tableName)) {
			ResultSetMetaData metaData = rs.getMetaData();
			for (int i = 1; i <= metaData.getColumnCount(); i++) {
				mapColumnType.put(metaData.getColumnName(i), metaData.getColumnTypeName(i));
			}
		}
		return mapColumnType;
	}

	public void cleanup(String tableName) throws SQLException, IOException {
		DBConnection dbcon = new DBConnection();
		try (Connection conn = dbcon.connect(); Statement stmt = conn.createStatement();) {
			stmt.execute("delete from " + tableName);
		}
	}
}
