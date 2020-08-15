package PGATool.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import PGATool.properties.AppProperties;

public class PGAFileHelper {
	public static void deleteFileIfExits(String tableBackupPath) {
		File file = new File(tableBackupPath);
		if (file.exists()) {
			file.delete();
		}
	}

	public static File getDMDBackupPath() {
		String property = "java.io.tmpdir";
		String tempDir = System.getProperty(property);
		File dmdBackup = new File(tempDir + "/dmdBackup");
		dmdBackup.mkdir();
		return dmdBackup;
	}
	
	/**
	 * Get path of the files backup source from where the files are imported into DB
	 * @return
	 * @throws IOException
	 */
	public static Map<String, Path> getFilePathsFromBackupSource() throws IOException {
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

	/**
	 * Replace the "," with ";"
	 * 
	 * @param value
	 * @return
	 */
	public static String replaceTheDelimeterInValue(String value) {
		if (value != null && value.contains(",")) {
			value = value.replaceAll(",", ";");
		}
		return value;
	}

	/**
	 * Replace the "," with ";"
	 * 
	 * @param value
	 * @return
	 */
	public static String correctTheDelimeterInValue(String value) {
		if (value != null && value.contains(";")) {
			value = value.replaceAll(";", ",");
		}
		return value;
	}
}
