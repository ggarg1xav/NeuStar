package chbasic.utils;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;

public class CommonUtils {
	private static final Logger logger = Logger.getLogger(CommonUtils.class);

	public enum LOCATOR_TYPE {
		name, id, linktext, xpath, partialLinkText, cssSelector
	};

	/**
	 * This function used to return the object of "By" which takes string as
	 * parameter
	 * 
	 * @param fieldLocator
	 * @return
	 */
	public By getLocator(String fieldLocator) {
		logger.info("Field locator recieved::" + fieldLocator);
		final String locatorType;
		final String locatorValue;
		By by = null;
		System.out.println("fieldLocator : " + fieldLocator);
		String splitArray[] = fieldLocator.split("\\|");
		System.out.println("splitArraySize%%%" + splitArray.length);
		locatorType = splitArray[0];
		locatorValue = splitArray[1];
		System.out.println("locatorType : " + locatorType);
		System.out.println("locatorValue : " + locatorValue);

		LOCATOR_TYPE loc_temp = LOCATOR_TYPE.valueOf(locatorType);

		switch (loc_temp) {
		case name: {
			logger.info("locator type of the field =" + locatorType);
			by = By.name(locatorValue);
			break;
		}

		case linktext: {
			logger.info("locator type of the field =" + locatorType);
			by = By.linkText(locatorValue);
			break;
		}

		case xpath: {
			logger.info("locator type of the field =" + locatorType);
			by = By.xpath(locatorValue);
			break;
		}

		case partialLinkText: {
			logger.info("locator type of the field =" + locatorType);
			by = By.partialLinkText(locatorValue);
			break;
		}

		case cssSelector: {
			logger.info("locator type of the field =" + locatorType);
			by = By.cssSelector(locatorValue);
			break;
		}

		case id: {
			logger.info("locator type of the field =" + locatorType);
			by = By.id(locatorValue);
			break;
		}

		default: {
			logger.info("You just can not move from here");
		}
		}
		return by;
	}

}