package File;

import java.util.List;
import java.util.Map;

import GherkinReader.GherkinPOJO.Step;
import Utils.BasePageClassConfiguration;
import Utils.TagConfiguration;

/**
 * Child of base page generator. 
 * @author sercansensulun
 *
 */
public class BasePageClassGenerator extends ClassFileGenerator{

	
	public BasePageClassGenerator(String folderPath) {
		// TODO Auto-generated constructor stub
		super(folderPath);
	}
	
	@Override
	public String getAbsolutePath(){
		// TODO Auto-generated method stub
		return super.getAbsolutePath();
	}

	
	/**
	 * Creates empty class Skelaton that is child of the BasePage class. 
	 * Example: public Foo extends BasePage {} 
	 *  
	 * @param className name of the class.
	 * @return
	 */
	@Override
	protected String createClassSkelaton(String className){	
		return "package pages;\n"
				+ "import org.openqa.selenium.support.ui.WebDriverWait;\n"
				+ "import io.appium.java_client.android.AndroidDriver;\n"
				
				+ "public class "+className+" extends BasePage{"
				+"\n"
				+ "\tpublic "+className+"(AndroidDriver driver,WebDriverWait wait)\n"
				+ "\t{\n"
				+ "\t\t super(driver,wait);\n"
				+ "\t}\n"		
				+ "}";
	}

	/**
	 * Creates method for Edit Text.
	 * @param editTextIdentifier identifier for the Edit Text that exists on Project Under Test.
	 * @return
	 */
	private String createMethodContentForEditText(String editTextIdentifier) {
		// TODO Auto-generated method stub
		return "super.setText(\""+editTextIdentifier+"\",param);";
	}
	
	/**
	 * Creates method for Button
	 * @param buttonIdentifier identifier for the Button that exists on Project Under Test.
	 * @return
	 */
	private String createMethodContentForButton(String buttonIdentifier){
		return "super.click(\""+buttonIdentifier+"\");";
	}
	
	private String createMethodContentForTextView(String textViewIdentifier){
		return "return super.isShown(\""+textViewIdentifier+"\");";
	}

	
	/**
	 * Creates the child class that is child of the BasePage class.
	 * In other words, implementation of the Rule 1, Rule 2 and Rule 3 that are written on Documentation.
	 * @param steps_ SPL-MA Gherkin Steps.
	 * @return created content of the class.
	 */
	public String createClass(Step[] steps_) {

		Map<Integer, List<Step>> dividedSteps =  tagFinder.splitStepsBetweenTag(steps_, TagConfiguration.PAGE_TAG);
		
		for (Map.Entry<Integer, List<Step>> step : dividedSteps.entrySet()) {
			
			Step[] stepsAsArray = new Step[step.getValue().size()];
			stepsAsArray = step.getValue().toArray(stepsAsArray);
			
			String candidateBasePageChildClass = tagFinder.findAddressSignTag(stepsAsArray, TagConfiguration.PAGE_TAG);
	        
	        if (candidateBasePageChildClass == null) 
	        {
				System.out.println("child of BasePage class cannot created. Info: Invalid SPL-MA Gherkin.");
				return null;
			}
	        
	        String candidateBasePageChildClassContent = "", 
	        		candidateEditTextMethodContent = "",
	        		candidateButtonMethodContent = "",
	        		candidateTextViewMethodContent = "";
	        
	        
	        String candidateBasePageChildClassName = candidateBasePageChildClass+"Page";

	        if (isFileExist(candidateBasePageChildClassName+".java")) {
	        	//if file exist, set candidate content of the base page child class.
	        	candidateBasePageChildClassContent = getFileContent(candidateBasePageChildClassName+".java");
			}
	        
	        List<String> candidateEditTextMethodIdentifierList = tagFinder.findAddressSignTagList(stepsAsArray, 
	        		TagConfiguration.EDITTEXT_TAG);
	        
	        for (String candidateEditTextMethodIdentifier : candidateEditTextMethodIdentifierList) {
	        	
	        	String candidateEditTextMethodName = BasePageClassConfiguration.setTextMethodPreName + candidateEditTextMethodIdentifier;
	        	
	        	if (candidateBasePageChildClassContent.
	        			contains(candidateEditTextMethodName)) {
	        		//no need to add the method. 
	        		//just continue.
					continue;
				}
	        	
	        	String editTextMethodContent = createMethodContentForEditText(candidateEditTextMethodIdentifier);
	        	
	        	candidateEditTextMethodContent += createMethod(
	        			"void",
	        			candidateEditTextMethodName,
	        			editTextMethodContent,
	        			"String param");	
			}
	        
	        List<String> candidateButtonMethodList = 
	        		tagFinder.findAddressSignTagList(stepsAsArray, TagConfiguration.BUTTON_TAG);
	        
	        for (String candidateButtonMethod : candidateButtonMethodList) {
	        	String candidateButtonMethodName = BasePageClassConfiguration.clickMethodPreName + candidateButtonMethod;
	        	
	        	if (candidateBasePageChildClassContent.contains(candidateButtonMethodName)) {
	        		//no need to add the method. 
	        		//just continue.
					continue;
				}
	        	
	        	String buttonMethodContent = createMethodContentForButton(candidateButtonMethod);
	        	candidateButtonMethodContent += createMethod(
	        			"void",
	        			candidateButtonMethodName,
	        			buttonMethodContent,
	        			"");		
			}
	        
	        List<String> candidateTextViewMethodList = tagFinder.findAddressSignTagList(stepsAsArray, 
	        		TagConfiguration.TEXT_VIEW_TAG);
	        
	        for (String candidateTextViewMethod : candidateTextViewMethodList) {
				
	        	String candidateTextViewMethodName = BasePageClassConfiguration.isShownMethodPreName + candidateTextViewMethod;
	        	
	        	if (candidateBasePageChildClassContent.contains(candidateTextViewMethodName)) {
					continue;
				}
	        	
	        	String textViewMethodContent = createMethodContentForTextView(candidateTextViewMethod);
	        	
	        	candidateTextViewMethodContent += createMethod(
	        			"boolean",
	        			candidateTextViewMethodName, 
	        			textViewMethodContent, 
	        			"");
			}
	        
	        
	        try {
	        	if (!isFileExist(candidateBasePageChildClassName+".java")) {
		        	//if file with the same name does not exist, then create a empty class skelaton.
		        	//otherwise, do nothing.
		        	candidateBasePageChildClassContent = 
			        		createClassSkelaton(candidateBasePageChildClassName);
		        	
		        	String overAllClassContentAfterEditText = injectMethod(
			        		candidateBasePageChildClassContent,
			        		candidateEditTextMethodContent);
		        	
		        	String overAllClassContentAfterButton = injectMethod(
		        			overAllClassContentAfterEditText, candidateButtonMethodContent);
		        	
		        	String overAllClassContentAfterTextView = injectMethod(
		        			overAllClassContentAfterButton, 
		        			candidateTextViewMethodContent);
			        
		        	super.writeToFile(candidateBasePageChildClassName+".java", 
		        			overAllClassContentAfterTextView);
				}
		        else{
		        	super.writeToFile(candidateBasePageChildClassName+".java",
		        			candidateButtonMethodContent + candidateEditTextMethodContent + candidateTextViewMethodContent);
		        }
				
			} catch (Exception e) {

			}	
		}
		return null;
	}

	public void createBasePageClass() {
		String basePageFileName = "BasePage.java";
		if (isFileExist(basePageFileName)){
			return;
		}
		String overAllClassContentAfterTextView = "package pages;\n" +
				"\n" +
				"import org.openqa.selenium.By;\n" +
				"import org.openqa.selenium.support.ui.WebDriverWait;\n" +
				"\n" +
				"import io.appium.java_client.android.AndroidDriver;\n" +
				"\n" +
				"public class BasePage \n" +
				"{\n" +
				"\t\n" +
				"\tpublic AndroidDriver driver;\n" +
				"\tpublic WebDriverWait wait;\n" +
				"\t\n" +
				"\tpublic BasePage(AndroidDriver driver,WebDriverWait wait)\n" +
				"\t{\n" +
				"\t\tthis.driver = driver;\n" +
				"\t\tthis.wait = wait;\n" +
				"\t}\n" +
				"\t\n" +
				"\tprotected void click(String identifier)\n" +
				"\t{\n" +
				"\t\tdriver.findElement(By.id(identifier)).click();\n" +
				"\t}\n" +
				"\t\n" +
				"\tprotected void setText(String identifier, String text)\n" +
				"\t{\n" +
				"\t\tdriver.findElement(By.id(identifier)).clear();\n" +
				"\t\tdriver.findElement(By.id(identifier)).sendKeys(text);\n" +
				"\t}\n" +
				"\t\n" +
				"\tprotected String getText(String identifier)\n" +
				"\t{\n" +
				"\t\treturn driver.findElement(By.id(identifier)).getText();\n" +
				"\t}\n" +
				"\t\n" +
				"\tprotected boolean isEnable(String identifier){\n" +
				"\t\treturn driver.findElement(By.id(identifier)).isEnabled();\n" +
				"\t}\n" +
				"\t\n" +
				"\tpublic boolean isShown(String identifier){\n" +
				"\t\treturn driver.findElement(By.id(identifier)).isDisplayed();\n" +
				"\t}\n" +
				"\t\n" +
				"\tpublic AndroidDriver getDriver() {\n" +
				"\t\treturn driver;\n" +
				"\t}\n" +
				"\t\n" +
				"\tpublic WebDriverWait getWait() {\n" +
				"\t\treturn wait;\n" +
				"\t}\n" +
				"}\n";
		try {
			super.writeToFile(basePageFileName,
					overAllClassContentAfterTextView);

		}catch (Exception e){
			System.out.println(e.toString());
		}
	}


}
