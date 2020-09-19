package com.orbit.pages;

import static com.qmetry.qaf.automation.step.CommonStep.click;
import static com.qmetry.qaf.automation.step.CommonStep.sendKeys;
import static com.qmetry.qaf.automation.step.CommonStep.verifyPresent;
import static com.qmetry.qaf.automation.step.CommonStep.verifyText;
import static com.qmetry.qaf.automation.step.CommonStep.waitForVisible;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.hamcrest.Matchers;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orbit.utilities.OrbitUtils;
import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.core.MessageTypes;
import com.qmetry.qaf.automation.step.CommonStep;
import com.qmetry.qaf.automation.step.QAFTestStep;
import com.qmetry.qaf.automation.ui.WebDriverTestBase;
import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebElement;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebElement;
import com.qmetry.qaf.automation.util.Reporter;
import com.qmetry.qaf.automation.util.Validator;

public class CommonPage {
	public static final String ARGUMENT = "arguments[0].click();";
	public static final String HTML_COMMON = "html.common";
	public static final String WAITLOADER = "wait.loader";
	public static final String COMMON_BREADCRUMB = "common.breadcrumb";
	public static final String BORDER_COLOR = "border-color";
	public static final String TRAINING_TITLE_TEXTBOX = "trainingpage.trainingtitle.textbox";

	static WebDriverWait wait = new WebDriverWait(OrbitUtils.getDriver(), 30);

	@QAFTestStep(description = "I scroll to {element}")
	public static void scrollUpToElement(String ele) {
		JavascriptExecutor executor =
				(JavascriptExecutor) new WebDriverTestBase().getDriver();
		executor.executeScript("arguments[0].scrollIntoView()",
				new QAFExtendedWebElement(ele));
	}

	@QAFTestStep(description = "user verify {0} locator text is {1}")
	public void userVerifyBreadcrumbTextIs(String loc, String text) {
		String locText = CommonStep.getText(loc);
		String ActualBreadCrumbText =
				locText.substring(locText.indexOf("\n") + 1, locText.length());
		Validator.verifyThat("BreadCrumb visible!",
				ActualBreadCrumbText.equalsIgnoreCase(text), Matchers.equalTo(true));
	}

	@QAFTestStep(description = "I click using javascript {loc}")
	public static void clickUsingJavaScript(String loc) {
		JavascriptExecutor executor =
				(JavascriptExecutor) new WebDriverTestBase().getDriver();
		executor.executeScript("arguments[0].click();", new QAFExtendedWebElement(loc));
	}

	@QAFTestStep(description = "I wait for {loc} to be clickable")
	public static void waitForLocToBeClickable(String loc) {
		wait.until(
				ExpectedConditions.elementToBeClickable(new QAFExtendedWebElement(loc)));
	}

	@QAFTestStep(description = "verify {loc} text contains {text}")
	public static boolean verifyLocContainsText(String loc, String text) {
		return new QAFExtendedWebElement(loc).getText().contains(text);
	}

	@QAFTestStep(description = "user switch to manager view")
	public void switchToViews() {
		CommonStep.waitForEnabled("btn.SwitchingViews.userhomepage");
		CommonStep.click("btn.SwitchingViews.userhomepage");
		CommonStep.waitForNotVisible("loader.userhomepage");
	}

	@QAFTestStep(description = "user navigate back")
	public void iNavigateBack() {
		OrbitUtils.waitForLoader();
		OrbitUtils.getDriver().navigate().back();
	}

	@QAFTestStep(description = "user change the view to {0}")
	public void switchView(String viewName) {
		OrbitUtils.waitForLoader();
		String actualView = CommonStep.getText("label.view.userhomepage");
		if (actualView.trim().toLowerCase().contentEquals(viewName.toLowerCase())) {
			Reporter.log(viewName + " is already selected.", MessageTypes.Pass);
		} else {
			click("button.view.userhomepage");
			OrbitUtils.waitForLoader();
		}
	}

	@QAFTestStep(description = "user verify the view {0}")
	public void verifyView(String viewName) {
		verifyText("label.view.userhomepage", viewName);
	}

	@QAFTestStep(description = "user verifies {0} message is present")
	public void verifyLeaveMessage(String message) {
		OrbitUtils.waitForLoader();
		OrbitUtils.verifyMessagePresent("label.status.common", message);
		OrbitUtils.closeAllStatusBars("label.status.common");
	}

	@QAFTestStep(description = "user verify Title of web page")
	public static boolean verifyTitle(String title) {
	
		return OrbitUtils.getDriver().getTitle().contains(title);
		
	}

	@QAFTestStep(description = "switch tab after clicking on {link}")
	public static String switchTabAfterClickingOnLink(String locatorForLink) {
		String oldTab = OrbitUtils.getDriver().getWindowHandle();
		waitForLocToBeClickable(locatorForLink);
		clickUsingJavaScript(locatorForLink);
		ArrayList<String> newTab =
				new ArrayList<String>(OrbitUtils.getDriver().getWindowHandles());
		OrbitUtils.getDriver().switchTo().window(newTab.get(1));
		return oldTab;
	}

	@QAFTestStep(description = "user navigate to home page")
	public static void navigateOnHomePage() {
		OrbitUtils.waitForLoader();
		OrbitUtils.scrollToAxis(0, 0);
		click("home.page.link");
		OrbitUtils.waitForLoader();
	}

	@QAFTestStep(description = "user Refresh Page")
	public static void iRefreshPage() {
		OrbitUtils.getDriver().navigate().refresh();
	}

	@QAFTestStep(description = "user verifies {0} message is present after applying leave")
	public void verifyLeaveMessages(String message) {
		waitForVisible("label.status.common");
		OrbitUtils.verifyMessagePresent("label.status.common", message);
		OrbitUtils.closeAllStatusBars("label.status.common");
	}

	public static String appendWithBaseURL(String endPointKey) {
		return ConfigurationManager.getBundle().getString("env.baseurl") + "/#/"
				+ ConfigurationManager.getBundle().getString(endPointKey);
	}

	public static String tagWithEquals(String tagName, String attribute, String value) {
		return String.format(
				ConfigurationManager.getBundle().getString("tag.equals.loc.common"),
				attribute, value);
	}

	public static String tagWithContains(String tagName, String attribute, String value) {
		return String.format(
				ConfigurationManager.getBundle().getString("tag.contains.loc.common"),
				attribute, value);
	}

	public static QAFWebElement getWebElementFromList(String generalizedLocator,
			String fieldToCheck) {

		QAFExtendedWebElement element = new QAFExtendedWebElement(String.format(
				ConfigurationManager.getBundle().getString(generalizedLocator),
				fieldToCheck));

		return element;
	}
	
	@QAFTestStep(description = "user should see {0} for {1}")
	public void userShouldSeeBlocksAndFields(String fields, String loc) {
		OrbitUtils.getDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		CommonStep.waitForEnabled(CommonPage.COMMON_BREADCRUMB);
		String[] fieldList = fields.split(",");
		Reporter.log("String received" + Arrays.toString((fieldList)));
		for (int i = 0; i < fieldList.length; i++) {
			Validator.verifyThat(getWebElementFromList(loc, fieldList[i]).isPresent(),
					Matchers.equalTo(true));
		}

	}

	@QAFTestStep(description = "user verifies the border color of all the mandatory fields using {0} and {1}")
	public void verifyBorderColorOfMandatoryFields(String fields, String loc) {

		String[] fieldList = fields.split(",");
		Reporter.log("String received" + Arrays.toString((fieldList)));

		for (int i = 0; i < fieldList.length; i++) {
			QAFExtendedWebElement makeElement = new QAFExtendedWebElement(String.format(
					ConfigurationManager.getBundle().getString(loc), fieldList[i]));
			String color = makeElement.getCssValue(BORDER_COLOR);
			Reporter.log(color);
			if ((color.contains("rgb(255, 0, 0)")) || (color.contains("rgb(242, 53, 53)"))
					|| (color.contains("rgb(204, 204, 204)"))
					|| (color.contains("rgb(51, 51, 51)"))
					|| (color.contains("rgb(237, 237, 237)"))
					|| (color.contains("rgb(225, 119, 119)")))
				Reporter.log("The border color of the field is RED", MessageTypes.Pass);
			else

				Reporter.log("The border color of the field is not RED",
						MessageTypes.Fail);
		}

	}

	public static String elementTextEquals(String value) {
		return String.format(
				ConfigurationManager.getBundle().getString("text.equals.loc.common"),
				value);
	}

	public static String elementTextContains(String value) {
		return String.format(
				ConfigurationManager.getBundle().getString("text.contains.loc.common"),
				value);
	}

	public static String divTextEquals(String value) {
		return String.format(
				ConfigurationManager.getBundle().getString("div.equals.loc.common"),
				value);
	}

	public static String divTextContains(String value) {
		return String.format(
				ConfigurationManager.getBundle().getString("div.contains.loc.common"),
				value);
	}
	
	public static String divClassContains(String value) {
		return String.format(
				ConfigurationManager.getBundle().getString("div.contains.class"),
				value);
	}

	public static String buttonTextEquals(String value) {
		return String.format(
				ConfigurationManager.getBundle().getString("button.equals.loc.common"),
				value);
	}

	public static String buttonTextContains(String value) {
		return String.format(
				ConfigurationManager.getBundle().getString("button.contains.loc.common"),
				value);
	}

	public static String inputNameEquals(String value) {
		return String.format(ConfigurationManager.getBundle()
				.getString("input.name.equals.loc.common"), value);
	}

	public static String inputNameContains(String value) {
		return String.format(ConfigurationManager.getBundle()
				.getString("input.name.contains.loc.common"), value);
	}

	public static void enterLoginDetails(String userName, String password) {
		sendKeys(userName, "input.username.loginpage");
		sendKeys(password, "input.password.loginpage");
	}

	public static void clickLoginButton() {
		WebElement login = new QAFExtendedWebElement("button.login.loginpage");
		OrbitUtils.clickUsingJavaScript(login);
	}

	public static String getStringLocator(String locator, String varibale) {
		return String.format(ConfigurationManager.getBundle().getString(locator),
				varibale);
	}
	public static void commonDisplayedLocatorClick(String commonLocator) {
		List<QAFWebElement> locator = OrbitUtils.getQAFWebElements(commonLocator);
		for (int i = 0; i < locator.size(); i++) {
			if (locator.get(i).isDisplayed()) {
				JavascriptExecutor jse = (JavascriptExecutor) OrbitUtils.getDriver();
				jse.executeScript("window.scrollTo(0, document.body.scrollHeight)");
				locator.get(i).click();
				break;
			}
		}
	}
	public static void commonDisplayedLocatorSendKeys(String commonLocator,
			String textToEnter) {
		List<QAFWebElement> locator = OrbitUtils.getQAFWebElements(commonLocator);
		for (int i = 0; i < locator.size(); i++) {
			if (locator.get(i).isEnabled()) {
				JavascriptExecutor jse = (JavascriptExecutor) OrbitUtils.getDriver();
				jse.executeScript("window.scrollTo(0, document.body.scrollHeight)");
				locator.get(i).sendKeys(textToEnter);
				break;
			}
		}
	}

	@QAFTestStep(description = "user click on {0} icon")
	public void clickOnIcon(String title) {
		OrbitUtils.clickUsingJavaScript(getWebElementFromList("icons.with.title", title));
		OrbitUtils.waitForLoader();
	}
	
	@QAFTestStep(description = "user verifies training details are in editable form")
	public void userVerifyTrainingDetailsInEditableForm(String box) {

		Validator.verifyThat("Training title text box is present", verifyPresent("trainingpage.trainingtitle.textbox"), Matchers.equalTo(true));
		OrbitUtils.waitForLoader();
	}

	@QAFTestStep(description = "user verifies {0} fields are present for {1}")
	public static void verifyTableHeaders(String strHeaders, String loc) {
		String[] headers =
				ConfigurationManager.getBundle().getString(strHeaders).split(":");
		for (String header : headers) {
			if (verifyFieldTextPresent(loc, header.trim()))
				Reporter.log(header + " is present", MessageTypes.Pass);
			else Reporter.log(header + " is not present", MessageTypes.Fail);
		}
	}

	private static boolean verifyFieldTextPresent(String loc, String verifyText) {
		boolean present = false;
		for (QAFWebElement option : OrbitUtils.getQAFWebElements(loc)) {
			if (option.getText().trim().contains(verifyText)) {
				present = true;
				break;
			}
		}
		return present;
	}

	@QAFTestStep(description = "user verifies pagination")
	public void verifyPagination() {
		OrbitUtils.waitForLoader();

		OrbitUtils.scrollToViewElement("list.pagenumber.pagination.common");
		List<QAFWebElement> pageNumber =
				OrbitUtils.getQAFWebElements("list.pagenumber.pagination.common");
		List<QAFWebElement> page =
				OrbitUtils.getQAFWebElements("list.page.pagination.common");
		if (pageNumber.size() > 1) {
			for (int i = 0; i < pageNumber.size(); i++) {
				OrbitUtils.scrollUpToElement(pageNumber.get(i));
				if (i == 0 || i == pageNumber.size())
					if (pageNumber.get(i).getText().contains("..."))
						continue;
				OrbitUtils.clickUsingJavaScript(pageNumber.get(i));
				OrbitUtils.waitForLoader();
				if (page.get(i).getAttribute("class").contains("active"))
					Reporter.log("Clicked on expected page successfully",
							MessageTypes.Info);
				else
					Reporter.logWithScreenShot("Failed to click on expected page",
							MessageTypes.Fail);
			}
		} else {
			Reporter.log("page number size " + pageNumber.size());
			Reporter.logWithScreenShot("multiple pages are not present",
					MessageTypes.Pass);
		}
	}
	@QAFTestStep(description = "user click on {0}")
	public void clickOnButton(String btn) {
		QAFExtendedWebElement element =
				new QAFExtendedWebElement(CommonPage.buttonTextContains(btn));
		OrbitUtils.waitForLoader();
		element.waitForVisible(5000);
		JavascriptExecutor executor =
				(JavascriptExecutor) new WebDriverTestBase().getDriver();
		executor.executeScript(ARGUMENT, element);
		OrbitUtils.waitForLoader();
	}

	@QAFTestStep(description = "user verifies colors according to the theme of the application")
	public void verifyThemeColor() {
		QAFExtendedWebElement searchButton =
				new QAFExtendedWebElement(elementTextContains("Search"));
		String color = searchButton.getCssValue("background-color");
		if (color.contains("rgba(235, 114, 3, 1)"))
			Reporter.logWithScreenShot("Colors on page are according to the theme",
					MessageTypes.Pass);
		else
			Reporter.log("Colors on page are not according to the theme",
					MessageTypes.Fail);
	}

	public static String getStringLocator(String locator, String varibale1,
			String variable2) {
		return String.format(ConfigurationManager.getBundle().getString(locator),
				varibale1, variable2);
	}

	/** * takes user to first page */
	public void goToFirstPage() {

	}

	/** * takes user to last page */
	public void goToLastPage() {

	}

	/** * takes user to previous page */
	public void goToPreviousPage() {

	}

	/** * takes user to next page */
	public void goToNextPage() {

	}

	/** * traverse all the page */
	public void traverseAllPages() {

	}

	/** * get current page */
	public String getCurrentPageNumber() {
		String pageNumber = "";
		return pageNumber;
	}

	/** * go to specific page */
	public void goToPageNumber(String pageNo) {

	}

	
	@QAFTestStep(description = "user click on each TAB {0} having locator {1}user should see the list of subtab {2} having common locator {3}")
	public void userClickOnEachTABHavingLocatorUserShouldSeeTheListOfSubtabHavingCommonLocator(
			String listoftab, String tabcommonloc, String listofsubtab, String subtabcommonloc) {
		String[] tab = listoftab.split(",");
		String[] subtab = listofsubtab.split(",");
		for (int i = 0; i < tab.length; i++) {
			OrbitUtils.getDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			CommonStep.waitForEnabled(CommonPage.COMMON_BREADCRUMB);
			OrbitUtils.clickUsingJavaScript(String.format(ConfigurationManager.getBundle().getString(tabcommonloc),tab[i]));
			for (int j = 0; j < subtab.length; j++) {
				Validator.verifyThat(getWebElementFromList(subtabcommonloc, subtab[j]).isPresent(),Matchers.equalTo(true));
													}
												}
		}
	
	public static String spanTextEquals(String value) {
		return String.format(
				ConfigurationManager.getBundle().getString("span.title.contains.common"),
				value);
	}
	}
