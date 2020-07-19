package PGATool.file;

import java.io.File;

public class PGAFileHelper {
	static void deleteFileIfExits(String tableBackupPath) {
		File file = new File(tableBackupPath);
		if (file.exists()) {
			file.delete();
		}
	}

	static File getDMDBackupPath() {
		String property = "java.io.tmpdir";
		String tempDir = System.getProperty(property);
		File dmdBackup = new File(tempDir + "/dmdBackup");
		dmdBackup.mkdir();
		return dmdBackup;
	}
}
