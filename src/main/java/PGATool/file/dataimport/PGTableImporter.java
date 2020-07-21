package PGATool.file.dataimport;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import PGATool.properties.AppProperties;
import PGATool.properties.PGAProperties;

public class PGTableImporter {

	public void importData() throws IOException, URISyntaxException, SQLException {
		Map<String, Path> filePathsFromBackupSource = getFilePathsFromBackupSource();
		CSVReader reader = new CSVReader();

		List<String> tables = PGAProperties.getTables();
		for (int i = tables.size() - 1; i >= 0; i--) {
			reader.cleanup(tables.get(i));
		}

		for (String table : tables) {
			reader.readCSVFile(table, filePathsFromBackupSource.get(table));
		}
	}

	private Map<String, Path> getFilePathsFromBackupSource() throws IOException {
		List<Path> listFiles = new ArrayList<>();
		Files.newDirectoryStream(Paths.get(AppProperties.getInstance().getBackupFilesPath()))
				.forEach(path -> listFiles.add(path));
		Map<String, Path> mapFilePath = new HashMap<>(listFiles.size());
		for (Path path : listFiles) {
			String filePath = path.toString();
			int si = filePath.lastIndexOf("\\");
			int li = filePath.indexOf(".");
			String fileName = filePath.substring(si + 1, li);
			mapFilePath.put(fileName, path);
		}
		return Collections.unmodifiableMap(mapFilePath);
	}
}
