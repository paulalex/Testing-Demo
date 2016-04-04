package uk.gov.hscic.test.integration;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;


public class GeneralIT {
	private static URI siteBase;
	private static WebDriver drv;
	private static String PHANTOMJS_BINARY;
	
	@BeforeClass
	public static void setUp() throws Exception {
		System.out.println("*****Setting up web driver *****");
		siteBase = new URI("http://localhost:10001/MockitoTests");		
		setPhantomJSDriver();
	}
	
	/*@After 
	public void closeWebDriver() {
		drv.close();
	}*/
	
	@AfterClass
	public static void tearDown() throws Exception {				
		System.out.println("***** Tearing down web driver *****");		
		drv.close();
		drv.quit();
	}
		
	@Test
	public void testWeSeeHelloWorld() {
		drv.get(siteBase.toString());
		assertTrue(drv.getPageSource().contains("Hello world!"));
		WebElement wrapperDiv = drv.findElement(By.id("button_wrapper_div"));
		assertNotNull(wrapperDiv);
	}
	
	@Test
	public void testNumberOfButtonsOnHomePage() {
		int validNumberOfButtons = 4;
		drv.get(siteBase.toString());
		assertTrue(drv.getPageSource().contains("Hello world!"));
		List buttons = drv.findElements(By.tagName("button"));
		assertNotNull(buttons);
		assertEquals(validNumberOfButtons, buttons.size());
	}
	
	@Test
	public void testPersonUrl() {
		drv.get(siteBase.toString()+"/person");
		assertTrue(drv.getPageSource().contains("Hello Person"));
		assertTrue(drv.getPageSource().contains("name: John Smith"));
		assertTrue(drv.getPageSource().contains("age: 30"));
	}
	
	@Test
	public void testPersonNameUrl() {
		drv.get(siteBase.toString()+"/person/Mike");
		assertTrue(drv.getPageSource().contains("Hello Person by name"));		
	}
	
	@Test
	public void testPersonAgeInDaysUrl() {
		drv.get(siteBase.toString()+"/person_age_in_days");
		assertTrue(drv.getPageSource().contains("Hello Person age in days"));			
	}
	
	@Test
	public void testUnknownUrl() {
		drv.get(siteBase.toString()+"/invalid");		
		assertTrue(drv.getPageSource().contains("Error: 404 Page Not Found Error"));		
	}
	
	@Test
	public void testPersonAgeInDaysButton() {
		drv.get(siteBase.toString());		
		WebElement ageButton = drv.findElement(By.className("btn_age_class"));
		assertNotNull(ageButton);
		ageButton.click();
		assertTrue(drv.getPageSource().contains("Hello Person age in days"));
		assertTrue(drv.getPageSource().contains("The persons age is "));
	}
	
	@Test
	public void testPersonByNameButton() {
		drv.get(siteBase.toString());		
		WebElement nameButton = drv.findElement(By.id("name_btn"));
		assertNotNull(nameButton);
		nameButton.click();
		assertTrue(drv.getPageSource().contains("Hello Person by name"));
		assertTrue(drv.getPageSource().contains("name: Mike"));
		assertTrue(drv.getPageSource().contains("age: 30"));
	}
	
	@Test
	public void testPersonBtn() {
		drv.get(siteBase.toString());		
		WebElement personButton = drv.findElement(By.id("person_btn"));
		assertNotNull(personButton);
		personButton.click();
		assertTrue(drv.getPageSource().contains("Hello Person"));
		assertTrue(drv.getPageSource().contains("name: John Smith"));
		assertTrue(drv.getPageSource().contains("age: 30"));
	}
	
	
	@Test
	public void testPersonByName404ErrorButton() {
		drv.get(siteBase.toString());		
		WebElement ageButton404 = drv.findElement(By.id("name_404_btn"));
		assertNotNull(ageButton404);
		ageButton404.click();
		assertTrue(drv.getPageSource().contains("Error: 404 Page Not Found Error"));
	}
	
	private static void setPhantomJSDriver() {   		
		PHANTOMJS_BINARY = System.getProperty("phantomjs.binary");
		ArrayList<String> cliArgsCap = new ArrayList<String>();
        cliArgsCap.add("--proxy-type=none");
        cliArgsCap.add("--ignore-ssl-errors=true");
        
        assertNotNull(PHANTOMJS_BINARY);	    
	    assertTrue(new File(PHANTOMJS_BINARY).exists());
        
        DesiredCapabilities capabilities = DesiredCapabilities.phantomjs();
        capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);
        capabilities.setJavascriptEnabled(true);
        capabilities.setCapability("takesScreenshot", false);
        
        capabilities.setCapability(
            PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
            PHANTOMJS_BINARY
        );
               
        EventFiringWebDriver driver = new EventFiringWebDriver(new PhantomJSDriver(capabilities));
        	
        drv = driver;     
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }	
}
