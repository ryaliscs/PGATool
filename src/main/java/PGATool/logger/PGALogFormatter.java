package pgatool.logger;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class PGALogFormatter extends Formatter {

	@Override
	public String format(LogRecord record) {
		return record.getMessage()+"\n";
	}

}
