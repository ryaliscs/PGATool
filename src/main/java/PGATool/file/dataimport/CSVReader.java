package PGATool.file.dataimport;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;

import com.google.common.io.Files;

public class CSVReader {

	public void readCSVFile(String csvFile, Path path) throws IOException {
		List<String> readLines = Files.readLines(new File(path.toUri()), Charset.defaultCharset());
		readLines.stream().forEach(System.out::println);
		String[] header = readLines.get(0).split(",");
		StringBuilder sb = new StringBuilder("INSERT INTO " + csvFile);
		for (int i = 1; i < readLines.size(); i++) {

		}
	}
}
