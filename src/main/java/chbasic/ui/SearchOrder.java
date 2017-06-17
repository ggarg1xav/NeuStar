package chbasic.ui;

import java.net.MalformedURLException;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import chbasic.utils.ConstantMapper;
import chbasic.utils.UIDriver;

public class SearchOrder {

	private static Logger logger = Logger.getLogger(SearchOrder.class);
	UIDriver iedriver;

	static String out_path = ConstantMapper.OUT_PATH_SEARCH_TEST_FILES;
	static String excel_path = ConstantMapper.XLSX_PATH_SEARCH;
	static String sheet_name = ConstantMapper.SHEET_OF_EXCEL_SEARCH;

	public SearchOrder(UIDriver iedriver) throws MalformedURLException {

		// iedriver = BrowserDriver.getInstance(url, exePath);
		this.iedriver = iedriver;

		logger.info("performing search");

		iedriver.findElement(By.xpath("//img[@alt='Clear']")).click();

	}

	/**
	 * This function search by pon in order to verify the submitted order
	 * 
	 * @param pon
	 */
	public boolean searchByPON(String pon) {
		try {
			iedriver.switchTodefault();
			// iedriver.switchToFrame("msgFrame");
			Thread.sleep(5000);
			iedriver.findElement(By.name("NF_PON")).sendKeys(pon);
			Thread.sleep(5000);
			iedriver.findElement(By.name("searchButton")).click();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public void searchByOrderNoE(String order_no) {
		iedriver.switchTodefault();
		iedriver.findElement(By.name("NF_OrderNumber")).sendKeys(order_no);
		iedriver.findElement(By.name("searchButton")).click();

	}

	public void searchByOrderOID(String orderId) {
		iedriver.switchTodefault();
		iedriver.findElement(
				By.name("NF_Request.Subscriber_Record_Requestcontainer.Subscriber_Record_Request(0).RequestHeader.OrderOID"))
				.sendKeys(orderId);
	}

	public void clickSearch() {
		iedriver.findElement(By.name("searchButton")).click();
		iedriver.switchTo();

	}

	public boolean searchByMESSAGE_ID(String msgid) {
		try {
			iedriver.switchTodefault();
			// iedriver.switchToFrame("msgFrame");

			iedriver.findElement(By.name("NF_MessageId")).sendKeys(msgid);
			iedriver.findElement(By.name("searchButton")).click();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public boolean searchByTXNUM(String txnum) {
		try {
			iedriver.switchTodefault();
			// iedriver.switchToFrame("msgFrame");

			iedriver.findElement(By.name("NF_TXNUM")).sendKeys(txnum);
			iedriver.findElement(By.name("searchButton")).click();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public boolean searchByTXNUMAndReqType(String txnum, String reqtype) {
		try {
			iedriver.switchTodefault();
			// iedriver.switchToFrame("msgFrame");
			if (reqtype.equals("Customer Service Records")) {
				String request = "Customer Service Record";
				new Select(iedriver.findElement(By.name("NF_ServiceType"))).selectByVisibleText(request);
				iedriver.findElement(By.name("NF_TXNUM")).sendKeys(txnum);
				iedriver.findElement(By.name("searchButton")).click();
				return true;
			} else if (reqtype.equals("Collocation Facility Assignment Verification")) {
				String request = "CFA Inquiry";
				new Select(iedriver.findElement(By.name("NF_ServiceType"))).selectByVisibleText(request);
				iedriver.findElement(By.name("NF_TXNUM")).sendKeys(txnum);
				iedriver.findElement(By.name("searchButton")).click();
				return true;
			} else {
				new Select(iedriver.findElement(By.name("NF_ServiceType"))).selectByVisibleText(reqtype);
				logger.info("Selected request type: " + reqtype);
				iedriver.findElement(By.name("NF_TXNUM")).sendKeys(txnum);

				iedriver.findElement(By.name("searchButton")).click();
				return true;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

}
