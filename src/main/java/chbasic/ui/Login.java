package chbasic.ui;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.xml.sax.SAXException;

import chbasic.utils.CommonUtils;
import chbasic.utils.ConstantMapper;
import chbasic.utils.UIDriver;
import chbasic.utils.UIPopulation;

public class Login {

	UIPopulation uiPopulation;
	// WebdriverFactory driverFactory = new WebdriverFactory();
	UIDriver iedriver;
	static String login_or_path = ConstantMapper.LOGIN_OR_PATH;
	static String exePath = ConstantMapper.EXECUTABLE_PATH;
	static String url = ConstantMapper.BASIC_URL;
	static String domain = ConstantMapper.DOMAIN;
	static String username = ConstantMapper.USERNAME;
	static String password = ConstantMapper.PASSWORD;
	String xpath = "";
	CommonUtils cmmn = new CommonUtils();

	private static Logger logger = Logger.getLogger(Login.class);

	/**
	 * This Class used for opening browser by given url
	 * 
	 * @param url
	 * @param exePath
	 * @throws MalformedURLException
	 */
	public Login(String login_or_path, UIDriver iedriver) throws MalformedURLException {
		System.out.println("EXEPATH---->" + exePath);

		// iedriver = BrowserDriver.getInstance(url, exePath);
		this.iedriver = iedriver;
		System.out.println("login_or_path " + login_or_path);
		uiPopulation = new UIPopulation(login_or_path, null, iedriver);
		logger.info("Browser Opened Successfully for :Login Class");
	}

	public Login(UIDriver iedriver) throws MalformedURLException {

		this.iedriver = iedriver;
		// iedriver = BrowserDriver.getInstance(null, null);
		logger.info("In default constructor");

	}

	/**
	 * This method used to get the header title of the login page
	 * 
	 * @return
	 */
	public String getPageTitle() {
		logger.info("Getting page title for :Login Class");
		return iedriver.getTitle();
	}

	/**
	 * This method populates the corresponding textboxes and clicks the LOGIN
	 * button in order to get successfully logged in
	 * 
	 * @param customerid
	 * @param user
	 * @param passwd
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws XPathExpressionException
	 */
	public boolean doLogin(String userName, String password, String domain) throws InterruptedException,
			XPathExpressionException, IOException, ParserConfigurationException, SAXException {
		try {

			logger.info("Performing Login Operation:Login Class");
			xpath = "/login/login_fields/DOMAIN";
			String domain_locator = uiPopulation.getNodeValByXpath(xpath);
			logger.info("Domain Locator::" + domain_locator);
			iedriver.findElement(cmmn.getLocator(domain_locator)).clear();
			iedriver.findElement(cmmn.getLocator(domain_locator)).sendKeys((domain));

			xpath = "/login/login_fields/USERNAME";
			System.out.println("Irfan  -------" + xpath + " User Name " + userName);
			String username_locator = uiPopulation.getNodeValByXpath(xpath);
			logger.info("Username Locator::" + username_locator);
			iedriver.findElement(cmmn.getLocator(username_locator)).clear();
			iedriver.findElement(cmmn.getLocator(username_locator)).sendKeys((userName));

			xpath = "/login/login_fields/PASSWORD";
			String password_locator = uiPopulation.getNodeValByXpath(xpath);
			logger.info("Password Locator::" + password_locator);
			iedriver.findElement(cmmn.getLocator(password_locator)).clear();
			iedriver.findElement(cmmn.getLocator(password_locator)).sendKeys((password));

			xpath = "/login/login_button";
			String login_locator = uiPopulation.getNodeValByXpath(xpath);
			logger.info("Login_button Locator::" + login_locator);
			iedriver.findElement(cmmn.getLocator(login_locator)).click();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			iedriver.stop();
			return false;
		}
	}

	public boolean logOff() {
		try {
			logger.info("Performing logout operation");
			Thread.sleep(5000);
			iedriver.findElement(By.partialLinkText("Logoff")).click();
			logger.info("Stoping the Browser Driver");
			Thread.sleep(5000);
			iedriver.stop();
			// iedriver.close();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			iedriver.stop();
			return false;
		}
	}

}