package File;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import GherkinReader.GherkinPOJO.Element;
import GherkinReader.GherkinPOJO.Example;
import GherkinReader.GherkinPOJO.Row;

/**
 * testng.xml configuration file generator.
 * @author sercansensulun
 */
public class TestNGXMLFileGenerator extends XMLFileGenerator{

	public TestNGXMLFileGenerator(String folderPath){
		super(folderPath);
	}
	
	@Override
	public String getAbsolutePath() {
		// TODO Auto-generated method stub
		return super.getAbsolutePath() + "";
	}

	/**
	 * Creates a testng.xml file for the project.
	 * In other words, it is implementation of the Rule 8.
	 * @param elements All Features for the project.
	 * @return
	 */
	public String createXMLFile(Element[] elements){

		String testSuiteXMLTag = "<suite name=\"Suite\">";
		String candidateTestCasesXMLTag = "";
		
		if (elements.length == 0) {
			return "";
		}
		
		for (Element element : elements) {
			Example[] example = element.getExamples();
			
			if (example == null) {
				continue;
			}
			
			//first index is always header row for the data table.
			Row[] exampleDataTablerows = example[0].getRows();
			Row exampleDataTableHeaderRow = example[0].getRows()[0];
			
			String candidateTestNGContent = createCandidateTestNGContent();
			
			
			//do not scan header row, because it is not test case for us.
			for (int startIndexForParameterValues = 1; startIndexForParameterValues < exampleDataTablerows.length; startIndexForParameterValues++) {
			
				String testXMLTag = "<test name=\"" + java.util.UUID.randomUUID().toString() + "\">";
				
				String[] exampleDataTableHeaderCells = exampleDataTableHeaderRow.getCells();
				String[] exampleDataTableParameterValueCells = exampleDataTablerows[startIndexForParameterValues].getCells();
				
				String parametersValueTagAsString = "";
				
				for (int i = 0; i < exampleDataTableHeaderCells.length; i++) {
					String parameterXMLTag = 
							"\t<parameter "
							+ "\n\t\tname=\"" + exampleDataTableHeaderCells[i] + "\" "
							+ "\t\tvalue=" + exampleDataTableParameterValueCells[i] + ">\n"
							+ "\t</parameter> \n";
					
					parametersValueTagAsString += parameterXMLTag;
				}
				
				parametersValueTagAsString += "\t<classes>\n"
						+ "\t\t<class name=\"Tests." + element.getName() + "\">"
						+ "</class>\n"
						+ "\t</classes>\n";
				
				candidateTestCasesXMLTag += injectTag(testXMLTag, parametersValueTagAsString);
			}
		}
		
		String candidateTestNGContent = createCandidateTestNGContent() + injectTag(testSuiteXMLTag, candidateTestCasesXMLTag);

		
		try {
			super.writeToFile(getFileName(), candidateTestNGContent);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	private String createCandidateTestNGContent() {
		// TODO Auto-generated method stub
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n"
				+ "<!DOCTYPE suite SYSTEM \"http://testng.org/testng-1.0.dtd\">\n";
	}
	
	private String getFileName(){
		return "testng.xml";
	}
}
