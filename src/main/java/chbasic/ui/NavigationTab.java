package chbasic.ui;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import chbasic.utils.CommonUtils;
import chbasic.utils.ConstantMapper;
import chbasic.utils.UIDriver;
import chbasic.utils.UIPopulation;

public class NavigationTab {

	UIDriver iedriver;
	CommonUtils cmn = new CommonUtils();
	private static Logger logger = Logger.getLogger(NavigationTab.class);
	private static String xpath = "";
	UIPopulation uiPopulation;
	static String home_tab_or_path = ConstantMapper.NAV_SEARCH_TAB_OR_PATH;

	public NavigationTab(String home_or_path, UIDriver driver) throws MalformedURLException {
		iedriver = driver;
		uiPopulation = new UIPopulation(home_or_path, null, iedriver);
		logger.info("home page clicked");
	}

	public boolean navigateLink(String name)
			throws XPathExpressionException, IOException, ParserConfigurationException, SAXException {
		try {
			xpath = "/search_order/order_type_tab/" + name;
			logger.info("xpath appended by the node name in SEARCH_NAV_BAR.xml");
			String home_locator = uiPopulation.getNodeValByXpath(xpath);
			System.out.println("11111111111111" + home_locator);
			logger.info("Node value(locator) of the given node::" + home_locator);
			Thread.sleep(7000);
			iedriver.findElement(cmn.getLocator(home_locator)).click();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			iedriver.stop();
			return false;
		}
	}
}
