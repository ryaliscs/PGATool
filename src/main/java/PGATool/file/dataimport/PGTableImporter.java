package PGATool.file.dataimport;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import PGATool.properties.AppProperties;

public class PGTableImporter {
	
	public void importData() throws IOException
	{
		readFiles();
	}

	
	public void readFiles() throws IOException {
		 List<String> listFiles = new ArrayList<>();
		 Files.newDirectoryStream(Paths.get(AppProperties.getInstance().getBackupFilesPath()))
			        .forEach(p -> listFiles.add(p.toString()));
		 listFiles.stream().forEach(System.out::println);
	}
}
