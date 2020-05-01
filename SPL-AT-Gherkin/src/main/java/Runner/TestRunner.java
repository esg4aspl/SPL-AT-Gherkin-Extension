package Runner;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;


@CucumberOptions(features = {"Element"}, glue = {"Tests"})
public class TestRunner extends AbstractTestNGCucumberTests
{

}
