package com.orbitz.tests;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.orbitz.flights.FlightBooking;

public class FlightBookingTest {
	
	public WebDriver driver;
	
	FlightBooking flightBook;
	
	@Parameters({"browser"})
	@BeforeMethod
	public void browserInitialization(String browser) throws InterruptedException{
		try {
			if(browser.equals("chrome")){
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/src/main/resources/drivers/chromedriver.exe");
				driver = new ChromeDriver();
				System.out.println("chrome browser launched");
			}else if(browser.equals("firefox")){
				System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "/src/main/resources/drivers/geckodriver20.exe");
				driver = new FirefoxDriver();
				System.out.println("firefox browser launched");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());	
		}
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.get("https://www.orbitz.com/");
		flightBook = new FlightBooking(driver);
	}
	
	@Test
	public void validateSearchedFlightDetails(){
		String source = "PNQ";
		String destination = "TIR";
		flightBook.clickFlightsFromMenu();
		flightBook.selectTripType();
		flightBook.enterOriginAs(source);
		flightBook.EnterDestinationAs(destination);
		flightBook.clickDepart();
		String SelectDate = "03/30/2018";
		Date d = new Date(SelectDate);
		SimpleDateFormat dt = new SimpleDateFormat("MMM/dd/yyyy");
		String date = dt.format(d);
		String[] split = date.split("/");
		String txtDay =  split[1];
		String txtMont_year = split[0]+" "+split[2];
		flightBook.selectDepartDate(txtMont_year, txtDay);
		flightBook.clickSearchButton();
		List<WebElement> availableFlights = flightBook.listOfFlights();
		if(availableFlights.size()>0){
			for(int i=0; i<3; i++ ){	
					WebElement ele = availableFlights.get(i);
					String flightDetails = ele.getText();
					Assert.assertTrue(flightDetails.contains(source) && flightDetails.contains(destination));
			}
		}else{
			System.out.println("Couldn't find any flights in this route");
		}
	}
	
	@AfterMethod
	public void quitBrowser(){
		driver.quit();
	}

}
