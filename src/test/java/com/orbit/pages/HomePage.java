package com.orbit.pages;

import java.time.LocalDate;
import org.openqa.selenium.By;

import com.orbit.utilities.OrbitUtils;
import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.step.QAFTestStep;
import com.qmetry.qaf.automation.ui.WebDriverBaseTestPage;
import com.qmetry.qaf.automation.ui.api.PageLocator;
import com.qmetry.qaf.automation.ui.api.WebDriverTestPage;
import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebElement;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebElement;
import com.qmetry.qaf.automation.util.Validator;

public class HomePage extends WebDriverBaseTestPage<WebDriverTestPage> {

	public static final String FROM = ConfigurationManager.getBundle().getString("flying.from");
	public static final String TO = ConfigurationManager.getBundle().getString("flying.to");
	
	@Override
	protected void openPage(PageLocator pageLocator, Object... args) {
	}

	@QAFTestStep(description = "user on orbit home page")
	public void userOnOrbitHomePage() {
		driver.get("/");
	}

	@QAFTestStep(description = "user click on {0} Tab")
	public void userClickOnTab(String label) {
		QAFExtendedWebElement oneway = new QAFExtendedWebElement(String.format(
				ConfigurationManager.getBundle().getString("label.oneway.homepage"),
				label));
		oneway.click();
	}

	@QAFTestStep(description = "user click on {0} button")
	public void userClickOnButton(String btn) {
		QAFExtendedWebElement flights = new QAFExtendedWebElement(String.format(
				ConfigurationManager.getBundle().getString("button.flights.homepage"),
				btn));
		flights.click();
	}

	@QAFTestStep(description = "user click on {0} field")
	public void userClickOnFromField(String from) {
		QAFExtendedWebElement fromfield = new QAFExtendedWebElement(String.format(
				ConfigurationManager.getBundle().getString("input.from.homepage"), from));
		fromfield.click();
		fromfield.sendKeys(FROM);
		//fromfield.sendKeys(pageProps.getString(flying.from));
	}

	@QAFTestStep(description = "user cick on field {0}")
	public void userCickOnField(String to) {
		QAFExtendedWebElement tofield = new QAFExtendedWebElement(String.format(
				ConfigurationManager.getBundle().getString("input.to.homepage"), to));
		tofield.click();
		tofield.sendKeys(TO);

	}

	@QAFTestStep(description = "user cick on departing field")
	public void userCickOnDepartingField() {
		QAFExtendedWebElement departing = new QAFExtendedWebElement(String.format(
				ConfigurationManager.getBundle().getString("input.departing.homepage")));
		departing.click();
	}

	@QAFTestStep(description = "user select nest day date")
	public void userSelectNestDayDate() {
		LocalDate currentDate = LocalDate.now();
		LocalDate nextDate = currentDate.plusDays(1);
		int nxtdate = nextDate.getDayOfMonth();
		String nextdate = Integer.toString(nxtdate);

		QAFExtendedWebElement month = new QAFExtendedWebElement(String.format(
				ConfigurationManager.getBundle().getString("table.month.homepage")));

		QAFExtendedWebElement day =
				month.findElement(By.xpath("(//button[@data-day=" + nextdate + "])"));
		day.click();
	}

	@QAFTestStep(description = "user click on {0}")
	public void userClickOn(String str0) {

		QAFExtendedWebElement search = new QAFExtendedWebElement(String.format(
				ConfigurationManager.getBundle().getString("button.search.homepage")));
		CommonPage.waitForLocToBeClickable("button.search.homepage");
		search.click();
	}


	@QAFTestStep(description = "user verify search flight details {0} sourse {1} destination")
	public void userVerifySearchFlightDetailsSourseDestination(String ORIGIN,
			String DEST) {

		QAFExtendedWebElement detailsclick =
				new QAFExtendedWebElement(String.format(ConfigurationManager.getBundle()
						.getString("(//span[@class='show-flight-details'])[1]")));
		detailsclick.click();

	}

	@QAFTestStep(description = "user verify search flight details by clicking on {0}")
	public void userVerifySearchFlightDetails(String click) {
		
		QAFWebElement detailsClick = CommonPage.getWebElementFromList("link.flight.homepage", click);
		OrbitUtils.scrollUpToElement(detailsClick);
		detailsClick.click();
		
		QAFExtendedWebElement details = new QAFExtendedWebElement("details.flight.homepage");
		String text = details.getText();
		Validator.verifyThat(text, org.hamcrest.Matchers.containsString(FROM));
		Validator.verifyThat(text, org.hamcrest.Matchers.containsString(TO));
	}

}
