package GherkinParser;
import java.io.File;
import File.BasePageClassGenerator;
import File.TestNGClassGenerator;
import File.TestNGXMLFileGenerator;
import File.POMFileModifier;
import GherkinReader.GherkinPOJO.Main;
import Utils.PomDependency;

public class MainProgram {

    // Set as pretty / ugly format for for JSON output. By default it is pretty
    static GherkinParser testG = new GherkinParser("pretty");

    public static void main(String[] args) {

        try {
            String featureDirectoryPath = args[0];
            String testDirectoryPath = args[1];
            String pageDirectoryPath = args[2];
            String testngXMLDirectory = args[3];
            String pomDirectory = args[3]; //we assume that pom.xml and testng.xml files are located under same folder.

            BasePageClassGenerator basePageGenerator = new BasePageClassGenerator(pageDirectoryPath);
            TestNGClassGenerator testNGClassGenerator = new TestNGClassGenerator(testDirectoryPath);
            TestNGXMLFileGenerator testNGXMLFileGenerator = new TestNGXMLFileGenerator(testngXMLDirectory);
            POMFileModifier pomFileModifier = new POMFileModifier(pomDirectory);


            File folder = new File(featureDirectoryPath);
            File[] listOfFiles = folder.listFiles();
            //All features in project.
            GherkinReader.GherkinPOJO.Element[] elements = new GherkinReader.GherkinPOJO.Element[listOfFiles.length];

            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {

                    Main main = testG.gherkinTojsonPOJO(featureDirectoryPath+listOfFiles[i].getName(), "");
                    GherkinReader.GherkinPOJO.Element element = main.getElements()[0];
                    GherkinReader.GherkinPOJO.Step[] steps = element.getSteps();

                    basePageGenerator.createClass(steps);
                    testNGClassGenerator.createClass(element);
                    elements[i] = element;
                }
            }
            testNGXMLFileGenerator.createXMLFile(elements);
            basePageGenerator.createBasePageClass();
            testNGClassGenerator.createBaseTestClass();

            pomFileModifier.addDependency(new PomDependency("org.seleniumhq.selenium",
                    "selenium-support", "3.141.59", null));
            pomFileModifier.addDependency(new PomDependency("io.appium",
                    "java-client", "7.3.0", null));
            pomFileModifier.addDependency(new PomDependency("org.testng",
                    "testng", "6.8", "test"));

            System.out.println("Test classes, page classes and testng xml files are generated successfully.");
            System.out.println("Test Classes path is " + testDirectoryPath);
            System.out.println("Page Classes path is " + pageDirectoryPath);
            System.out.println("testng xml file path is " + testngXMLDirectory);
        }catch (Exception e){

            System.out.println("Generation failed, please check exception. ");
            System.out.print(e.toString());
        }

    }
}
