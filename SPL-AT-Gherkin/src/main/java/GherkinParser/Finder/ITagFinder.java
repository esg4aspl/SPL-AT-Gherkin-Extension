package GherkinParser.Finder;

import java.util.List;
import java.util.Map;

/**
 * @author sercansensulun
 */
public interface ITagFinder {

	/**
	 * Finds AddressSign tag, according to given keyword.
	 * @param steps Given, When, Then
	 * @param keyword special keyword for AddressSign example : PAGE
	 * @return if given keyword not found, returns null. if keywords found, return identifier of the tag.
	 */
	String findAddressSignTag(GherkinReader.GherkinPOJO.Step[] steps, String keyword);
	
	/**
	 * Finds List of AddressSign tag, according to given keyword.
	 * @param steps Given, When, Then
	 * @param keyword special keyword for AddressSign example : PAGE
	 * @return if given keyword not found, returns null. if keywords found, return identifier of the tag.
	 */
	List<String> findAddressSignTagList(GherkinReader.GherkinPOJO.Step[] steps, String keyword);
	
	/**
	 * Find List of AddressSign-DolarSign pairs based on given address sign keyword.
	 * @param steps Given, When, Then
	 * @param keyword special keyword for AddressSign example : PAGE
	 * @return Dictionary of the found AddressSign-DolarSign pairs. 
	 */
	List<Map<String, String>> findAddressDolarSignPairs(GherkinReader.GherkinPOJO.Step[] steps, String keyword);
	
	/**
	 * Finds DolarSign tag, according to given keyword.
	 * @param steps Given,When Then
	 * @param keyword special keyword for AddressSign example : MOVED
	 * @return if given keyword not found, returns null. if keywords found, return identifier of the tag.
	 */
	String findDolarSignTag(GherkinReader.GherkinPOJO.Step[] steps, String keyword);
	
	/**
	 * Finds List of DolarSign tag, according to given keyword.
	 * @param steps Given,When Then
	 * @param keyword special keyword for AddressSign example : MOVED
	 * @return if given keyword not found, returns null. if keywords found, return identifier of the tag.
	 */
	List<String> findDolarSignTagList(GherkinReader.GherkinPOJO.Step[] steps, String keyword);
	
	/**
	 * 
	 * Split Steps according to given tag keyword.
	 * @param keyword special keyword for AddressSign example : PAGE
	 * @param steps overall steps.
	 * @return Dictionary of the divided Steps in occurrence order.
	 */
	Map<Integer, List<GherkinReader.GherkinPOJO.Step>> splitStepsBetweenTag(GherkinReader.GherkinPOJO.Step[] steps,String keyword);
}
