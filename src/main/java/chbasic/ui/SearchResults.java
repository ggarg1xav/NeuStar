package chbasic.ui;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import chbasic.utils.UIDriver;

public class SearchResults {
	
	private static Logger logger = Logger.getLogger(SearchResults.class);
	UIDriver iedriver;

	public SearchResults(UIDriver driver) throws MalformedURLException {
		iedriver = driver;
		logger.info("****** In SEARCH_RESULTS *******");
	}

	public boolean accessResultsPage() throws InterruptedException, MalformedURLException {
		try {
			// Code for switching to search results page
			String currentWindowHandle = iedriver.getWindowHandle();
			Set<String> openWindowsList = iedriver.getWindowHandles();
			logger.info("Number of windows being displayed: " + openWindowsList.size());
			String popUpWindowHandle = null;
			for (String windowHandle : openWindowsList) {
				if (!windowHandle.equals(currentWindowHandle)) {
					logger.info("Hello I am here");
					popUpWindowHandle = windowHandle;
				}
			}
			logger.info("currentWindowHandle" + currentWindowHandle);
			logger.info("popUpWindowHandle" + popUpWindowHandle);
			logger.info("iedriver.getTitle" + iedriver.getTitle());
			iedriver.switchToWindow(popUpWindowHandle);
			logger.info("iedriver.getTitle1" + iedriver.getTitle());
			if (iedriver.getTitle().equals("Certificate Error: Navigation Blocked")) {
				iedriver.get("javascript:document.getElementById('overridelink').click()");
			}
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			iedriver.stop();
			return false;
		}
	}

	public boolean viewOrderHistory() throws InterruptedException {
		try {
			@SuppressWarnings("unused")
			String popUpWindowHandle = null;
			Thread.sleep(4000);
			iedriver.findElement(By.name("Details")).click();
			String currentWindowHandle1 = iedriver.getWindowHandle();
			Thread.currentThread();
			Thread.sleep(5000);
			// Code for switching back to Order History page(Parent window)
			Set<String> openWindowsList1 = iedriver.getWindowHandles();
			
//			for (String windowHandle1 : openWindowsList1) {
//				logger.info("hELL0--->" + windowHandle1);
//				if (!windowHandle1.equalsIgnoreCase(currentWindowHandle1)){
//					logger.info("Hello I am here to Switch Window" );
//					iedriver.switchToWindow(windowHandle1);
//				}
//			}
			
			for (String windowHandle1 : openWindowsList1) {
				logger.info("hELL0--->" + windowHandle1);

				logger.info("Hello I am here to Switch Window");
				iedriver.switchToWindow(windowHandle1);
				Thread.sleep(3000);
				if (iedriver.getTitle().contains("Order History")) {
					System.out.println("Yes Finally I am here");
					break;
				} else {
					System.out.println("No the Destination");
				}

			}
			logger.info("iedriver.getTitle2" + iedriver.getTitle());
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			iedriver.stop();
			return false;
		}
	}

	public String getResultCount() {
		String resultCount = iedriver.findElement(By.xpath("//td[@class='PageCount']")).getText();
		return resultCount;
	}

	public String getPageCount() {
		String pageCount = iedriver.findElement(By.xpath("//div[@id='pageSelection']/table/tbody/tr/td[1]")).getText();
		return pageCount;
	}

	public List<String> getResultFromTable(int pagecount) {
		List<String> guiSearchContents = new ArrayList<String>();
		if (pagecount > 3) {
			for (int pagecounter = 0; pagecounter < 3; pagecounter++) {

				for (int trCounter = 2; trCounter < 5; trCounter++) {
					String contents = "";
					for (int tdCounter = 3; tdCounter < 16; tdCounter++) {
						String tempContents = "";
						WebElement we = iedriver.findElement(By.xpath(
								"//form[@name='get_detail']/table/tbody/tr[" + trCounter + "]/td[" + tdCounter + "]"));
						tempContents = we.getText();
						contents = contents + tempContents + ",";

					}
					guiSearchContents.add(contents);

				}

				iedriver.findElement(
						By.xpath("//img[@src='https://192.168.100.16:8292/gateway/images/next-page-bullet.gif']"))
						.click();
			}

		} else {
			for (int pagecounter = 0; pagecounter < pagecount; pagecounter++) {

				for (int trCounter = 2; trCounter < 4; trCounter++) {
					String contents = "";
					for (int tdCounter = 3; tdCounter < 16; tdCounter++) {
						String tempContents = "";
						WebElement we = iedriver.findElement(By.xpath(
								"//form[@name='get_detail']/table/tbody/tr[" + trCounter + "]/td[" + tdCounter + "]"));
						tempContents = we.getText();
						contents = contents + tempContents + ",";

						guiSearchContents.add(contents);
					}

				}

				iedriver.findElement(
						By.xpath("//img[@src='https://192.168.100.16:8292/gateway/images/next-page-bullet.gif']"))
						.click();
			}

		}
		return guiSearchContents;

	}

}