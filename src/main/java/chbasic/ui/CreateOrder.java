package chbasic.ui;

import java.net.MalformedURLException;

import org.apache.log4j.Logger;
import org.openqa.selenium.support.ui.Select;

import chbasic.utils.CommonUtils;
import chbasic.utils.ConstantMapper;
import chbasic.utils.UIDriver;
import chbasic.utils.UIPopulation;

public class CreateOrder {

	UIDriver iedriver;
	private static Logger logger = Logger.getLogger(CreateOrder.class);
	static String create_order_or_path = ConstantMapper.CREATE_ORDER_OR_PATH;
	CommonUtils cn = new CommonUtils();
	UIPopulation uiPopulation;

	public CreateOrder(String create_order_or_path, UIDriver driver) throws MalformedURLException {
		uiPopulation = new UIPopulation(create_order_or_path, null, driver);
		iedriver = driver;
		logger.info("*******ENTERED IN CREATE ORDER PAGE************");
	}

	public boolean selectCreateOptions(String order_type, String req_type, String supplier) {
		try {
			logger.info("Selecting order_type,req_type and supplier in Create Order page");
			Thread.sleep(1000);
			String xpath = "/create_order/select_order/ORDER_TYPE";
			String order_type_locator = uiPopulation.getNodeValByXpath(xpath);
			logger.info("Order_type Locator::" + order_type_locator);
			new Select(iedriver.findElement(cn.getLocator(order_type_locator))).selectByVisibleText(order_type);
			Thread.sleep(2000);
			xpath = "/create_order/select_order/REQUEST_TYPE";
			String req_type_locator = uiPopulation.getNodeValByXpath(xpath);
			logger.info("Request_type Locator::" + req_type_locator);
			System.out.println(req_type);
			new Select(iedriver.findElement(cn.getLocator(req_type_locator))).selectByVisibleText(req_type);
			Thread.sleep(2000);
			xpath = "/create_order/select_order/SUPPLIER";
			String supplier_locator = uiPopulation.getNodeValByXpath(xpath);
			logger.info("$$$$$$$$$Request_type Locator::$$$$" + supplier_locator);
			System.out.println("supplier value is" + supplier);
			new Select(iedriver.findElement(cn.getLocator(supplier_locator))).selectByVisibleText(supplier);

			xpath = "/create_order/CREATE_ORDER_BUTTON";
			String Create_order_button_locator = uiPopulation.getNodeValByXpath(xpath);
			logger.info("Create_order_button Locator::" + Create_order_button_locator);
			Thread.sleep(2000);
			iedriver.findElement(cn.getLocator(Create_order_button_locator)).click();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			// iedriver.stop();
			return false;
		}

	}

	public boolean selectCreateOptionsWithTemplate(String order_type, String req_type, String supplier,
			String template_name) {
		try {
			logger.info("Selecting order_type,req_type and supplier in Create Order page");

			String xpath_temp = "/create_order/select_order/ORDER_TYPE";
			String order_type_locator = uiPopulation.getNodeValByXpath(xpath_temp);
			logger.info("Order_type Locator::" + order_type_locator);
			new Select(iedriver.findElement(cn.getLocator(order_type_locator))).selectByVisibleText(order_type);

			xpath_temp = "/create_order/select_order/REQUEST_TYPE";
			String req_type_locator = uiPopulation.getNodeValByXpath(xpath_temp);
			logger.info("Request_type Locator::" + req_type_locator);
			new Select(iedriver.findElement(cn.getLocator(req_type_locator))).selectByVisibleText(req_type);

			xpath_temp = "/create_order/select_order/SUPPLIER";
			String supplier_locator = uiPopulation.getNodeValByXpath(xpath_temp);
			logger.info("Supplier is" + supplier);
			logger.info("@@@@@@Request_type Locator::@@@@@@@" + supplier_locator);
			System.out.println("Supplier is" + supplier);
			new Select(iedriver.findElement(cn.getLocator(supplier_locator))).selectByVisibleText(supplier);

			xpath_temp = "/create_order/select_order/TEMPLATE";
			String template_locator = uiPopulation.getNodeValByXpath(xpath_temp);
			logger.info("Order_type Locator::" + template_locator);
			new Select(iedriver.findElement(cn.getLocator(template_locator))).selectByVisibleText(template_name);

			xpath_temp = "/create_order/CREATE_ORDER_BUTTON";
			String Create_order_button_locator = uiPopulation.getNodeValByXpath(xpath_temp);
			logger.info("Create_order_button Locator::" + Create_order_button_locator);
			iedriver.findElement(cn.getLocator(Create_order_button_locator)).click();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			iedriver.stop();
			return false;
		}

	}

}