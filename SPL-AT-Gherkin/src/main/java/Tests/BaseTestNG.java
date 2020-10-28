package Tests;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import io.appium.java_client.android.AndroidDriver;

/**
 * @author sercansensulun
 */
public class BaseTestNG {

	public AndroidDriver driver;
	public WebDriverWait wait;
	
	@BeforeClass
	public void setup() throws InterruptedException
	{
		//Appium driver configurations here.
		
	}
	
	@AfterClass
	public void teardown() 
	{
		driver.quit();
	}

}
