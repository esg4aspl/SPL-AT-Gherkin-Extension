package File;

import java.io.IOException;

import GherkinParser.Finder.ITagFinder;
import GherkinParser.Finder.TagFinder;

/**
 * @author sercansensulun
 */
public abstract class ClassFileGenerator extends FileGenerator
{
	
	protected ITagFinder tagFinder;
	
	public ClassFileGenerator(String folderPath){
		super(folderPath);
		tagFinder = new TagFinder();
	}
	
	
	/**
	 * Insert the given content of the method to content of the class.
	 * @param classContent class content as String Example: public classFoo {}.
	 * @param methodContent method content as String Example: public void Bar() {}
	 * @return injected class content.
	 */
	protected  String injectMethod(String classContent, String methodContent){
		
		String injectedClassContent = "";
		
		for (int i = 0; i < classContent.length(); i++) {
			 injectedClassContent += classContent.charAt(i);
			 if (classContent.charAt(i) == '{') {
				 injectedClassContent += "\n\t";
				 injectedClassContent += methodContent;
				 return injectedClassContent + classContent.substring(i+1);
			}
		}
		return null;
	}
	
	/**
	 * Insert the annotation content to given method.
	 * @param methodContent
	 * @param annotationContent
	 * @return
	 */
	protected String injectAnnotation(String methodContent, String annotationContent){
		return "\t\t@"+annotationContent+"\n"+
		methodContent;
	}
	
	/**
	 * Creates full filled method. 
	 * @param methodName name of the method. 
	 * @param methodContent content of the method as line of code. Example: foo.Bar();
	 * @param methodParameter parameter of the method. Example: String param
	 * @return
	 */
	protected String createMethod(String returnType,String methodName,String methodContent, String methodParameter){
		return "\tpublic "+ returnType + " " +methodName + "(" + methodParameter + ") throws InterruptedException\n" +
				"\t{\n" +
				"\t\t" + methodContent + "\n" +
				"\t}\n";
	}

	@Override
	protected boolean writeToFile(String fileName, String fileContent) throws IOException{
		
		if (isFileExist(fileName)) {
			//remove last char '}' from exist file. Then, add it to end of the new content.
			String existFileContent = getFileContent(fileName);
			
			if (fileContent.equals("")) {
				return false;
			}
			
			if (existFileContent == null || fileContent == null) {
				return false;
			}
			
			for (int i = existFileContent.length() - 1 ; i >= 0; i--) {
				if (existFileContent.charAt(i) == '}') {
					String start = existFileContent.substring(0, i - 1);
					String mid = fileContent;
					String end = existFileContent.substring(i,existFileContent.length());
					super.writeToFile(fileName, start + "\n" +mid + "\n" +end);
					return true;
				}
			}
			
		}
		super.writeToFile(fileName, fileContent);
		return true;
	}
	protected abstract String createClassSkelaton(String className);
	
	
}
