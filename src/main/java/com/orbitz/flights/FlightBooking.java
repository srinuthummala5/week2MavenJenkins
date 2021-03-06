package com.orbitz.flights;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FlightBooking {

	public static WebDriverWait wait;
	WebDriver driver;

	public FlightBooking(WebDriver driver) {
		this.driver = driver;
		wait = new WebDriverWait(driver, 30);

	}

	// click Flights from menu items
	public void clickFlightsFromMenu() {
		WebElement menuFlights = driver.findElement(By.xpath("//*[contains(@id, 'tab-flight-tab')]"));
		if (isClickable(menuFlights)) {
			menuFlights.click();
		} else {
			System.out.println("element is not clickable :" + menuFlights);
		}
	}

	// select one-way trip type from radio buttons
	public void selectTripType() {
		WebElement radioOneWay = driver.findElement(By.xpath("//input[contains(@id, 'flight-type-one-way')][@type='radio']/.."));
		if (isClickable(radioOneWay)) {
			radioOneWay.click();
		} else {
			System.out.println("element is not clickable :" + radioOneWay);
		}
	}

	// Enter text into Flying from
	public void enterOriginAs(String name) {
		String originXpath = "//span[contains(text(), 'Flying from')]/following-sibling::*[starts-with(@id, 'flight-origin')]";
		WebElement origin = driver.findElement(By.xpath(originXpath));
		origin.clear();
		origin.sendKeys(name);
		String originValueXpath = "//a[contains(@data-value, '" + name + "')]";
		WebElement originValue = driver.findElement(By.xpath(originValueXpath));
		wait.until(ExpectedConditions.elementToBeClickable(originValue)).click();
	}

	// Enter text into Flying to
	public void EnterDestinationAs(String name) {
		String destXpath = "//span[contains(text(), 'Flying to')]/following-sibling::*[starts-with(@id, 'flight-destination')]";
		WebElement destination = driver.findElement(By.xpath(destXpath));
		destination.clear();
		destination.sendKeys(name);
		WebElement destinationValue = driver.findElement(By.xpath("//a[contains(@data-value, '" + name + "')]"));
		wait.until(ExpectedConditions.elementToBeClickable(destinationValue)).click();
	}

	// Click departing date for select date from calendar
	public void clickDepart() {
		String departXpath = "//span[contains(text(), 'Departing')]"
				+ "/following-sibling::input[starts-with(@id, 'flight-departing')]";
		WebElement dateDepat = driver.findElement(By.xpath(departXpath));
		if(isClickable(dateDepat)){
			dateDepat.clear();
			dateDepat.click();
		}else{
			System.out.println("element is not clickable :"+dateDepat);
		}
	}

	// Select date from calendar
	public void selectDepartDate(String monthValue, String dayValue) {
		String monthsTitleXpath = "//div[@class='datepicker-cal-month']/table[@class='datepicker-cal-weeks']/caption";
		List<WebElement> lstMonthsTable = driver.findElements(By.xpath(monthsTitleXpath));

		for (int i = 1; i <= lstMonthsTable.size(); i++) {
			String monthText = lstMonthsTable.get(i - 1).getText();
			if (monthText.equalsIgnoreCase(monthValue)) {
				String daysXpath = "//div[@class='datepicker-cal-month'][" + i
						+ "]/table[@class='datepicker-cal-weeks']" + "//td/button[@class='datepicker-cal-date']";
				List<WebElement> lstDates = driver.findElements(By.xpath(daysXpath));
				for (WebElement date : lstDates) {

					if (date.getText().contains(dayValue)) {
						((JavascriptExecutor) driver).executeScript("arguments[0].click();", date);

						return;
					}
				}
			}

		}
		Actions builder = new Actions(driver);
		String btnNextXpath = "//div[@class='datepicker-dropdown']/div[@class='datepicker-cal']"
				+ "/button[@class='datepicker-paging datepicker-next btn-paging btn-secondary next']";
		WebElement btnNext = driver.findElement(By.xpath(btnNextXpath));
		builder.moveToElement(btnNext).click().perform();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		selectDepartDate(monthValue, dayValue);

	}

	// Click search button
	public void clickSearchButton() {
		WebElement btnSearch = driver
				.findElement(By.xpath("//button[contains(@class, 'btn-action')]/span[text()='Search']"));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnSearch);

	}

	// find and return list of flights available
	public List<WebElement> listOfFlights() {
		String elePath = "//div[@class='secondary-content no-wrap']";
		List<WebElement> flightsList = driver.findElements(By.xpath(elePath));
		return flightsList;
	}
		
	//check element is clickable or not
	public static boolean isClickable(WebElement webe) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(webe));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// Quit driver
	public void quitBrowser() {
		driver.quit();
	}

}
