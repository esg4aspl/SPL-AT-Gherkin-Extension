package GherkinParser.Finder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import GherkinReader.GherkinPOJO.Step;

/**
 * Finder for tag structure. 
 * Example : @, $.
 * @author sercansensulun
 */
public class TagFinder implements ITagFinder{

	@Override
	public String findAddressSignTag(Step[] steps, String keyword) {
		// TODO Auto-generated method stub
				for (GherkinReader.GherkinPOJO.Step step : steps) {
					if (step.getName().contains(keyword)) 
					{
						String stepName = step.getName();
						List<String> splittedSentence = Arrays.asList(stepName.split(" "));
						for (String splittedPart : splittedSentence) 
						{
							if (splittedPart.contains(keyword)) 
							{
								int indexOfKeyword = splittedSentence.indexOf(keyword);
								return splittedSentence.get(++indexOfKeyword);
							}
						}
					}
				}
				return null;
	}

	@Override
	public String findDolarSignTag(Step[] steps, String keyword) {
		// TODO Auto-generated method stub
		for (GherkinReader.GherkinPOJO.Step step : steps) {
			if (step.getName().contains(keyword)) 
			{
				String stepName = step.getName();
				List<String> splittedSentence = Arrays.asList(stepName.split(" "));
				for (String splittedPart : splittedSentence) 
				{
					if (splittedPart.contains(keyword)) 
					{
						return keyword;
					}
				}
			}
		}
		return null;
	}
	

	@Override
	public Map<Integer, List<GherkinReader.GherkinPOJO.Step>> splitStepsBetweenTag(GherkinReader.GherkinPOJO.Step[] steps,String keyword) {
		// TODO Auto-generated method stub
		
		Map<Integer,List<Step>> dividedSteps = new HashMap<Integer, List<Step>>();
		
		int numberOfTagsFound = 0;
		int foundTagIndex = 0;
		
		List<Step> dividedStepList = new ArrayList();
		
		for (int i = 0; i < steps.length; i++) {
			
			if (steps[i].getName().contains(keyword)) {
				if ((foundTagIndex != i) && (i != steps.length - 1)) {
					//another tag found
					dividedSteps.put(numberOfTagsFound, dividedStepList);
					numberOfTagsFound++;
					foundTagIndex = i;
					dividedStepList = new ArrayList();
					dividedStepList.add(steps[i]);
					continue;
				}
			}
			dividedStepList.add(steps[i]);
			if (i == steps.length - 1) {
				dividedSteps.put(numberOfTagsFound, dividedStepList);
			}
		}
		return dividedSteps;
	}

	@Override
	public List<String> findAddressSignTagList(Step[] steps, String keyword) {
		// TODO Auto-generated method stub
		List<String> foundTags = new ArrayList<String>();
		for (Step step : steps) {
			String foundTag = findAddressSignTag(new Step[] {step}, keyword);
			if (foundTag != null) {
				foundTags.add(foundTag);
			}
		}
		return foundTags;
	}

	@Override
	public List<String> findDolarSignTagList(Step[] steps, String keyword) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, String>> findAddressDolarSignPairs(Step[] steps, String keyword) {
		// TODO Auto-generated method stub
		return null;
	}

}
