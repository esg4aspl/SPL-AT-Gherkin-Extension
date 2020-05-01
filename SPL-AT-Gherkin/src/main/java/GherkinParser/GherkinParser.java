package GherkinParser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import com.google.gson.Gson;
import GherkinReader.GherkinPOJO.Main;
import gherkin.formatter.JSONFormatter;
import gherkin.formatter.JSONPrettyFormatter;
import gherkin.parser.Parser;
import gherkin.util.FixJava;

/**
 * Gherkin to Json parser core file.
 */
public class GherkinParser {
	private String format;
	//To get the total running time (optional)
	long startTime = System.currentTimeMillis();

	public GherkinParser(String outFormat) {
		this.format = outFormat;
	}
	public String getOutFormat() {
		return format;
	}

	@SuppressWarnings("deprecation")
	public Main gherkinTojsonPOJO(String fPath, String jPath) {

		// Define Main file and JSON File path.
		String gherkin = null;
		try {
			gherkin = FixJava.readReader(new InputStreamReader(
					new FileInputStream(fPath), "UTF-8"));
		} catch (FileNotFoundException e) {
			System.out.println("Main file not found");
			// e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}

		StringBuilder json = new StringBuilder();
		JSONFormatter formatter;
		// pretty or ugly selection, pretty by default
		if (format.equalsIgnoreCase("ugly")) {
			formatter = new JSONFormatter(json);// not pretty
		} else {
			formatter = new JSONPrettyFormatter(json);// pretty
		}

		Parser parser = new Parser(formatter);
		parser.parse(gherkin, fPath, 0);
		formatter.done();
		formatter.close();

		Gson gson = new Gson();
		
		Main[] main =  gson.fromJson(json.toString(), Main[].class);

		long endTime = System.currentTimeMillis();
		return main[0];
	}
}