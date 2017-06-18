package chbasic.ui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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

public class OrderDetails {

	static String lsr_order_or_path = ConstantMapper.LSR_ORDER_OR_PATH;

	static String submit_order_or_path = ConstantMapper.SUBMIT_ORDER_OR_PATH;
	UIPopulation uiPopulation;
	UIDriver iedriver;
	String xpath = "";
	CommonUtils cmm = new CommonUtils();
	private static Logger logger = Logger.getLogger(OrderDetails.class);

	public OrderDetails(UIDriver driver) throws MalformedURLException {

		iedriver = driver;
		logger.info("home page clicked");
	}

	public boolean populateOrder(String lsr_order_or_path, String test_path, String map_path)
			throws MalformedURLException {
		try {

			uiPopulation = new UIPopulation(lsr_order_or_path, test_path, iedriver);
			uiPopulation.populate();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			iedriver.stop();
			return false;
		}
	}

	public boolean performAction(String actionsOR, String tcName) {
		try {
			uiPopulation = new UIPopulation(actionsOR, null, iedriver);

			xpath = "/submit/submit_order/VALIDATE";
			String val_locator = uiPopulation.getNodeValByXpath(xpath);
			logger.info("Validate Button Locator:::" + val_locator);
			// // commented validation
			iedriver.findElement(cmm.getLocator(val_locator)).click();
			iedriver.switchToAlert();
			Thread.sleep(5000);

			xpath = "/submit/submit_order/SAVE";
			String save_locator = uiPopulation.getNodeValByXpath(xpath);
			logger.info("Save Button Locator::" + save_locator);
			iedriver.switchTo();
			iedriver.switchToMessageFrame();
			// iedriver.findElement(cmm.getLocator(save_locator)).click();
			logger.info("Order Saved successfully");
			// iedriver.switchToAlert();

			xpath = "/submit/submit_order/SUBMIT";
			String submit_locaor = uiPopulation.getNodeValByXpath(xpath);
			logger.info("Submit Button Locator::" + submit_locaor);
			iedriver.switchTo();
			iedriver.switchToMessageFrame();
			iedriver.findElement(cmm.getLocator(submit_locaor)).click();

			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			// iedriver.stop();
			return false;
		}
	}

	public void Validationlog(String Errvalue, String tcName, String button) throws IOException, InterruptedException {

		File file;
		file = new File("log" + "\\" + tcName + "_" + button + ".log");
		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(), false);

		BufferedWriter writefile = new BufferedWriter(fileWriter);
		writefile.write("\n------------------Validations Error for TestCase------------------------------------");
		writefile.write("\n" + Errvalue);
		writefile.write("\n------------------------------------------------------");
		writefile.close();

		System.out.println("Validation log File created Successfully");
	}

	public void populateSupp2(String Remarks) throws XPathExpressionException, IOException,
			ParserConfigurationException, SAXException, InterruptedException {
		System.out.println("Rem" + Remarks);
		Thread.sleep(5000);
		uiPopulation = new UIPopulation(lsr_order_or_path, null, iedriver);
		xpath = "/lsr_order/lsr/REMARKS";
		String Rem_locator = uiPopulation.getNodeValByXpath(xpath);
		logger.info("Rem_locator Button Locator::" + Rem_locator);
		iedriver.findElement(By.id("barGrp.sectionBar.LSR.REMARKS_SECTION")).click();
		iedriver.switchTodefault();
		iedriver.switchToFrame("msgFrame");
		iedriver.findElement(By.name("NF_Request.lsr_order.lsr(0).REMARKS")).clear();
		iedriver.findElement(By.name("NF_Request.lsr_order.lsr(0).REMARKS")).sendKeys(Remarks);

	}

}
