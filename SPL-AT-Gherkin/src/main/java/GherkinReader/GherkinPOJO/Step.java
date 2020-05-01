package GherkinReader.GherkinPOJO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author sercansensulun
 */
public class Step {

	private int line;
	private String name;
	private String keyword;
	private String delimitedParameter;
	
	public int getLine() {
		return line;
	}
	public void setLine(int line) {
		this.line = line;
	}
	public String getKeyword() {
		//remove white space char, if exist.
		return keyword.replaceAll("\\s","");
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDelimitedParameter() {
		Pattern pattern = Pattern.compile("\\<.*?\\>");
		Matcher matcher = pattern.matcher(getName());
		if (matcher.find()) {
			return (String) matcher.group().subSequence(1, matcher.group().length()-1);
		}
		return delimitedParameter;
	}
	
}
