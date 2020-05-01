package File;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author sercansensulun
 */
public abstract class XMLFileGenerator extends FileGenerator{

	public XMLFileGenerator(String folderPath){
		super(folderPath);
	}

	/**
	 * Insert the given child tag into the parent tag. 
	 * Tag is different from our concept tag. It is XML tag. 
	 * @param parentTag Example : <test name="74129e81-7ce2-458b-8683-0a235978dc98">
	 * @param childTag Example : <parameter name="delimited_parameter_1" value ="this_is_value_for_param_1">, <parameter name="delimited_parameter_1" value   ="this_is_a_value_for_param_1">
	 * @return
	 */
	protected String injectTag(String parentTag, String childTag){
		//find parent tag name.
		Pattern pattern = Pattern.compile("\\<.*?\\ ");
		Matcher matcher = pattern.matcher(parentTag);
		String parentTagName = "";
		if (matcher.find()) {
			parentTagName = (String) matcher.group().subSequence(1, matcher.group().length()-1);
		}
		return parentTag + 
				"\n" + 
				childTag + "\n</" +parentTagName+">\n";
	}
}
