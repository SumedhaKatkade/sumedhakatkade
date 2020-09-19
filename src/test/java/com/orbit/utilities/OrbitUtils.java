package com.orbit.utilities;

import static com.qmetry.qaf.automation.step.CommonStep.waitForNotVisible;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orbit.pages.CommonPage;
import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.core.MessageTypes;
import com.qmetry.qaf.automation.step.CommonStep;
import com.qmetry.qaf.automation.ui.WebDriverTestBase;
import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebDriver;
import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebElement;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebElement;
import com.qmetry.qaf.automation.util.Reporter;

public class OrbitUtils {

	public static final String LOADER = "wait.loader";
	static WebDriverWait wait = new WebDriverWait(getDriver(), 30);

	public static QAFExtendedWebDriver getDriver() {
		return new WebDriverTestBase().getDriver();
	}

	public static WebElement waitForElementToBeClickable(QAFWebElement ele) {
		return wait.until(ExpectedConditions.elementToBeClickable(ele));
	}

	public static boolean textToBePresentInElement(QAFWebElement ele, String text) {
		WebDriverWait wait = new WebDriverWait(getDriver(), 2, 200);
		return wait.until(ExpectedConditions.textToBePresentInElement(ele, text));
	}

	public static void clickUsingJavaScript(WebElement ele) {
		JavascriptExecutor executor = (JavascriptExecutor) new WebDriverTestBase().getDriver();
		executor.executeScript("arguments[0].click();", ele);
	}

	public String checkForView() {
		if (CommonStep.getText("label.viewer.userhomepage") == "Manager View") {
			return "Manager View";
		} else {
			return "Employee View";
		}
	}

	public static List<QAFWebElement> getQAFWebElements(String loc) {
		List<QAFWebElement> listWebElement = new QAFExtendedWebElement(CommonPage.HTML_COMMON).findElements(loc);

		return listWebElement;
	}

	public static void scrollUpToElement(QAFWebElement ele) {
		JavascriptExecutor executor = (JavascriptExecutor) new WebDriverTestBase().getDriver();
		executor.executeScript("arguments[0].scrollIntoView()", ele);
	}

	public static String getText(QAFExtendedWebDriver driver, WebElement ele) {
		return (String) ((JavascriptExecutor) driver).executeScript("return jQuery(arguments[0]).text();", ele);
	}

	public static void clickUsingJavaScript(String loc, String... value) {
		clickUsingJavaScript(
				new QAFExtendedWebElement(String.format(ConfigurationManager.getBundle().getString(loc), value)));
	}

	private static String createLeaveDate(int increment) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
		Calendar calendar = Calendar.getInstance();
		Date date = new Date();
		String today = dateFormat.format(date);
		try {
			calendar.setTime(dateFormat.parse(today));
		} catch (Exception e) {
			Reporter.log("Not able to parse the date." + e, MessageTypes.Fail);
		}
		calendar.add(Calendar.DATE, increment); // number of days to add
		String newDate = dateFormat.format(calendar.getTime());
		Date dt1 = null;
		try {
			dt1 = dateFormat.parse(newDate);
		} catch (Exception e) {
			Reporter.log("Not able to parse the date." + e, MessageTypes.Fail);
		}
		dateFormat.applyPattern("EEEE dd-MMMM-yyyy");
		return dateFormat.format(dt1);
	}

	public static String getLeaveDate(int increment) {
		int count = increment;
		String finalDay;
		String validDate = "";
		boolean flag = true;
		while (flag) {
			finalDay = createLeaveDate(count);
			String[] array = finalDay.split(" ");
			if (array[0].equals("Saturday") || array[0].equals("Sunday")) {
				if (count > 0)
					count++;
				else
					count--;
			} else {
				flag = false;
				validDate = array[1];
			}
		}
		return validDate;
	}

	public static void verifyMessagePresent(String loc, String message) {
		boolean result = false;
		List<QAFWebElement> elements = OrbitUtils.getQAFWebElements(loc);
		for (QAFWebElement ele : elements) {
			if (ele.getText().trim().toLowerCase().contains(message.toLowerCase())) {
				result = true;
				break;
			}
		}
		if (result)
			Reporter.log(message + " is present", MessageTypes.Pass);
		else
			Reporter.log(message + " is not present", MessageTypes.Fail);
	}

	public static boolean verifyMessageVisible(String loc, String message) {
		boolean result = false;
		List<QAFWebElement> elements = OrbitUtils.getQAFWebElements(loc);
		for (QAFWebElement ele : elements) {
			if (ele.getText().trim().toLowerCase().contains(message.toLowerCase())) {
				result = true;
				break;
			}
		}
		return result;
	}

	public static void closeAllStatusBars(String loc) {
		List<QAFWebElement> statusBars = OrbitUtils.getQAFWebElements(loc);
		for (QAFWebElement statusBar : statusBars) {
			if (statusBar.isDisplayed())
				statusBar.findElement(By.tagName("img")).click();
		}
	}

	public static String converDate(String myDate) {
		try {
			DateFormat readFormat = new SimpleDateFormat("dd-MMM-yyyy");
			DateFormat writeFormat = new SimpleDateFormat("dd-MMMM-yyyy");
			Date date = null;
			try {
				date = readFormat.parse(myDate);
			} catch (ParseException e) {
				Reporter.log("Failed in date format conversion " + e.getMessage());
			}

			String formattedDate = "";
			if (date != null) {
				formattedDate = writeFormat.format(date);
			}

			return formattedDate;

		} catch (Exception e) {
			return "error";
		}
	}

	public static void scrollToElement(String loc) {
		scrollUpToElement(new QAFExtendedWebElement(loc));
		if (!new QAFExtendedWebElement(loc).isDisplayed()) {
			getDriver().executeScript("window.scrollTo(0,-150);");
		}
	}

	public static void waitForLoader() {
		waitForNotVisible(LOADER, 90);
		CommonPage.waitForLocToBeClickable("button.logout.userhomepage");
	}

	public static void waitForLoaderWithoutCondition() {
		waitForNotVisible(LOADER, 60);
	}

	public static QAFWebElement getChildElement(String parentLoc, String childLoc) {
		return getChildElement(new QAFExtendedWebElement(parentLoc), childLoc);
	}

	public static QAFWebElement getChildElement(QAFExtendedWebElement parentElement, String childLoc) {
		return new QAFExtendedWebElement(parentElement, childLoc);
	}

	public static void scrollToAxis(int x, int y) {
		((JavascriptExecutor) getDriver()).executeScript("window.scrollTo(" + x + ", " + y + ");");
	}

	public static enum PageName {
		AVAILABLE_LEAVES("Available Leave");

		private final String name;

		private PageName(String pageName) {
			this.name = pageName;
		}

		public String getName() {
			return name;
		}
	}

	public static String getCurrentURL() {
		return getDriver().getCurrentUrl();
	}

	public static boolean waitForTextToBeChanged(QAFExtendedWebElement element, String text) {
		try {
			WebDriverWait waitForText = new WebDriverWait(getDriver(), 3, 100);
			return waitForText.until(ExpectedConditions.textToBePresentInElement(element, text));
		} catch (Exception e) {
			return false;
		}
	}

	public static void scrollToViewElement(QAFExtendedWebElement element) {
		int headerBarHeight = new QAFExtendedWebElement("header.section.common").getSize().getHeight();
		long height = (long) OrbitUtils.getDriver().executeScript("return document.body.scrollHeight;");
		try {

			if (element.isPresent()) {
				if (((int) height - element.getLocation().getY()) > 50) {
					scrollToAxis(0, element.getLocation().getY() - headerBarHeight);
				}
			} else {
				Reporter.log("No such element ", MessageTypes.Fail);
			}
		} catch (Exception e) {
			Reporter.log("scrolled till end");
		}
	}

	public static void scrollToViewElement(String loc) {
		scrollToViewElement(new QAFExtendedWebElement(loc));
	}

	public static void clickUsingJavaScript(String loc) {
		clickUsingJavaScript(new QAFExtendedWebElement(loc));

	}

	public static void clickUsingAction(String loc) {
		Actions builder = new Actions(getDriver());
		builder.moveToElement(new QAFExtendedWebElement(loc)).perform();
		builder.moveToElement(new QAFExtendedWebElement(loc)).click().perform();
	}

	public static void clickUsingActionOnElement(QAFWebElement element) {
		Actions builder = new Actions(getDriver());
		builder.moveToElement(element).perform();
		builder.moveToElement(element).click().perform();
	}

}
