package PGATool.file;

import java.io.File;

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
