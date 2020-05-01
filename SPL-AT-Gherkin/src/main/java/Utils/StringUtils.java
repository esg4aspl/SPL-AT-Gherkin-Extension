package Utils;

/**
 * @author sercansensulun
 */
public class StringUtils 
{
	public static String replaceSpecialCharsWithUnderscore(String input){
		return input.replaceAll("[^a-zA-Z0-9]", "_");
	}
}
