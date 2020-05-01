package File;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import GherkinReader.GherkinPOJO.Element;
import GherkinReader.GherkinPOJO.Step;
import Utils.TestNGClassConfiguration;
import Utils.BasePageClassConfiguration;
import Utils.GherkinStepConfiguration;
import Utils.StringUtils;
import Utils.TagConfiguration;

/**
 * child of BaseTestNG class generator.
 * @author sercansensulun
 */
public class TestNGClassGenerator extends ClassFileGenerator{

	
	public TestNGClassGenerator(String folderPath) {
		// TODO Auto-generated constructor stub
		super(folderPath);
	}
	
	@Override
	public String getAbsolutePath() {
		// TODO Auto-generated method stub
		return super.getAbsolutePath();
	}

	/**
	 * * Creates empty class Skelaton that is child of the BaseTestNG class. 
	 * Example: public Foo extends BaseTestNG {} 
	 *  
	 */
	@Override
	protected String createClassSkelaton(String className) {
		// TODO Auto-generated method stub
		return "package Tests;\n"
				+ "import org.testng.Assert;\n"
				+ "import org.testng.annotations.Test;\n"
				+ "import Pages.*;\n"
				+ "import static org.testng.Assert.assertEquals;\n"
				+ "import org.testng.annotations.Parameters;\n"
				+ "import static org.testng.Assert.assertTrue;\n"
				+ "import io.appium.java_client.MobileElement;\n"
				+ "import org.testng.annotations.BeforeClass; \n"
				+ "import io.appium.java_client.android.AndroidDriver;"
				+ ""
				+ "public class "+className+" extends BaseTestNG{"
				+ "}";
	}

	/**
	 * Creates the child class that is child of the BaseTestNG class.
	 * In other words, implementation of from the Rule 4 to Rule 9 that are written on Documentation.
	 * @param element SPL-MA Gherkin Element.
	 * @return created content of the class.
	 */
	public String createClass(Element element) {
		// TODO Auto-generated method stub
		if (element.getName() == null) {
			System.out.println("child of BasePage class cannot created. Info: Invalid SPL-MA Gherkin.");
			return null;
		}
		
		String candidateTestClassName = element.getName();
		
		//Rule 4
		String candidateBaseTestNGChildClassContent = createClassSkelaton(candidateTestClassName);
		
		String candidateAllMethodsContent = "";
		
		int priorityValue = 0; //it will be set to @TEST annotation priority while incrementing by 1.
		
		Map<Integer, List<Step>> dividedSteps = tagFinder.splitStepsBetweenTag(element.getSteps(), TagConfiguration.PAGE_TAG);
		
		String candidateBasePageChildClasses = "\n";
		
		String candidateInitializerBasePageChildClasses = "";
		
		for (Map.Entry<Integer, List<Step>> step : dividedSteps.entrySet()){
		
			Step[] stepsAsArray = new Step[step.getValue().size()];
			stepsAsArray = step.getValue().toArray(stepsAsArray);
			
			candidateBasePageChildClasses += "\t private " + tagFinder.findAddressSignTag(stepsAsArray, TagConfiguration.PAGE_TAG) + "Page page"+step.getKey() + ";\n";
			
			candidateInitializerBasePageChildClasses += "\tpage"+ step.getKey() + "= new " 
					+ tagFinder.findAddressSignTag(stepsAsArray, TagConfiguration.PAGE_TAG) + "Page(driver,wait);\n\t";
			
			/*
			candidateBasePageChildClasses += "\t private " + tagFinder.findAddressSignTag(stepsAsArray, TagConfiguration.PAGE_TAG) + "Page page"+step.getKey()+"= new " 
											+ tagFinder.findAddressSignTag(stepsAsArray, TagConfiguration.PAGE_TAG) + "Page(driver,wait);\n";
			*/
			
			for (Step innerStep : stepsAsArray) {
				
				String methodParameter = "";
				String parameterAnnotation = "";
				String methodContent = "";
				
				String methodName = innerStep.getKeyword()+
						StringUtils.replaceSpecialCharsWithUnderscore(innerStep.getName());
				
				//Rule 7
				String delimitedParameter = innerStep.getDelimitedParameter();
				if(delimitedParameter != null){
					methodParameter = "String param";
					parameterAnnotation = "\n\t"+TestNGClassConfiguration.PARAMETER_ANNOTATION + "({\"" + delimitedParameter + "\"})";
				}
				
				//Rule 9
				methodContent = createMethodContent(innerStep, step.getKey());
				
				
				//Rule 5
				String candidateTestMethod = createMethodWithAnnotation(
						methodName, //does not inject remove all space on getName() method, because it is used by the BasePageGenerator.
						methodContent,
						methodParameter, 
						parameterAnnotation + "\n\t" + TestNGClassConfiguration.TEST_ANNOTATION + 
						"("+TestNGClassConfiguration.TEST_ANNOTATION_PRIORITY_KEY+"="+priorityValue+")");
				
				
				candidateAllMethodsContent += candidateTestMethod;
				
				//Rule 6
				priorityValue++;
			}
	        
		}
		
		String classContent = injectMethod(candidateBaseTestNGChildClassContent, candidateAllMethodsContent);
		
		//inject @BeforeClass method to initialize child of BasePage classes.
		classContent = injectMethod(classContent, createMethodWithAnnotation("setup", "super.setup();\n" + candidateInitializerBasePageChildClasses, "", TestNGClassConfiguration.BEFORE_CLASS_ANNOTATION));
		
		//inject child of BasePage class to child of BaseTestNG class
		classContent = injectMethod(classContent, candidateBasePageChildClasses);
		
		try {
			if (!isFileExist(candidateTestClassName+".java")) {
				//if the file does not exist, then create it with the content.
				super.writeToFile(candidateTestClassName+".java", classContent);
			}
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

	
	private String createMethodContent(Step step, Integer basePageIndex) {
		// TODO Auto-generated method stub
		String textViewTagIdentifier = tagFinder.findAddressSignTag(new Step[]{step}, TagConfiguration.TEXT_VIEW_TAG);
		
		if (textViewTagIdentifier != null) {
			if (step.getKeyword().equals(GherkinStepConfiguration.THEN)) {
				if (tagFinder.findDolarSignTag(new Step[]{step}, TagConfiguration.SHOWN_TAG) != null) {
					return "assertTrue(page".concat(basePageIndex.toString()+".").concat(BasePageClassConfiguration.isShownMethodPreName).concat(textViewTagIdentifier).concat("());");
				}
			}
		}
		
		String buttonTagIdentifier = tagFinder.findAddressSignTag(new Step[]{step}, TagConfiguration.BUTTON_TAG);
		if (buttonTagIdentifier != null) {
			if (step.getKeyword().equals(GherkinStepConfiguration.WHEN) || step.getKeyword().equals(GherkinStepConfiguration.AND)) {
				if (tagFinder.findDolarSignTag(new Step[]{step}, TagConfiguration.CLIKED_TAG) != null) {
					return "page".concat(basePageIndex.toString()+".").concat(BasePageClassConfiguration.clickMethodPreName).concat(buttonTagIdentifier).concat("();");
				}	
			}
			if (step.getKeyword().equals(GherkinStepConfiguration.THEN)) {
				if (tagFinder.findDolarSignTag(new Step[]{step}, TagConfiguration.ENABLED_TAG) != null) {
					return "assertTrue(page".concat(basePageIndex.toString()+".").concat(BasePageClassConfiguration.isEnabledMethodPreName).concat(buttonTagIdentifier).concat("());");
				}
				if (tagFinder.findDolarSignTag(new Step[]{step}, TagConfiguration.DISABLED_TAG) != null) {
					return "assertTrue(!page".concat(basePageIndex.toString()+".").concat(BasePageClassConfiguration.isEnabledMethodPreName).concat(buttonTagIdentifier).concat("());");
				}
			}
		}
		
		String editTextTagIdentifier = tagFinder.findAddressSignTag(new Step[]{step}, TagConfiguration.EDITTEXT_TAG);
		if (editTextTagIdentifier != null) {
			if (step.getKeyword().equals(GherkinStepConfiguration.WHEN) || step.getKeyword().equals(GherkinStepConfiguration.AND)) {
				if(tagFinder.findDolarSignTag(new Step[]{step}, TagConfiguration.ENTERED_TAG) != null){
					return "page".concat(basePageIndex.toString()+".").concat(BasePageClassConfiguration.setTextMethodPreName).concat(editTextTagIdentifier).concat("(param);");	
				}
				
			}
			if (step.getKeyword().equals(GherkinStepConfiguration.THEN)) {
				if (tagFinder.findDolarSignTag(new Step[]{step}, TagConfiguration.ENABLED_TAG) != null) {
					return "asserTrue(".concat(basePageIndex.toString()+".").concat(BasePageClassConfiguration.isEnabledMethodPreName).concat(editTextTagIdentifier).concat("());");
				}
				if (tagFinder.findDolarSignTag(new Step[]{step}, TagConfiguration.DISABLED_TAG) != null) {
					return "asserTrue(!".concat(basePageIndex.toString()+".").concat(BasePageClassConfiguration.isEnabledMethodPreName).concat(editTextTagIdentifier).concat("());");
				}
			}
		}
		
		String pageTagIdentifier = tagFinder.findAddressSignTag(new Step[]{step}, TagConfiguration.PAGE_TAG);
		if (pageTagIdentifier != null) {
			if (step.getKeyword().equals(GherkinStepConfiguration.THEN) || step.getKeyword().equals(GherkinStepConfiguration.AND)) {
				if (tagFinder.findDolarSignTag(new Step[]{step}, TagConfiguration.MOVED_TAG) != null) {
					return "\t\tWaitForUI(); \n"
							+ "\t\tassertEquals(((AndroidDriver<MobileElement>) driver).currentActivity(),param);\n";	
				}
				if (tagFinder.findDolarSignTag(new Step[]{step}, TagConfiguration.OPENED_TAG) != null) {
					return "\t\tWaitForUI();";
				}
			}
			if (step.getKeyword().equals(GherkinStepConfiguration.GIVEN)) {
				return "\t\tWaitForUI();";
			}
		}
		
		
		
		return "";
	}

	private String createMethodWithAnnotation(String methodName, String methodContent,String methodParameter,String annotationContent) {
		// TODO Auto-generated method stub
		return ""+annotationContent + "\n" + 
		super.createMethod("void",methodName,methodContent, methodParameter);
		
	}
}
