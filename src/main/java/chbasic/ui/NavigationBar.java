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

public class NavigationBar {
	UIDriver iedriver;
	CommonUtils cmn = new CommonUtils();
	private static Logger logger = Logger.getLogger(NavigationBar.class);
	private static String xpath = "";
	UIPopulation uiPopulation;

	static String home_or_path = ConstantMapper.HOME_OR_PATH;

	public NavigationBar(String home_or_path, UIDriver iedriver) throws MalformedURLException {
		uiPopulation = new UIPopulation(home_or_path, null, iedriver);
		this.iedriver = iedriver;
		logger.info("home page clicked");
	}

	public boolean navigateLink(String name) throws XPathExpressionException,
			IOException, ParserConfigurationException, SAXException {
		try {
			xpath = "/home/home_loc/" + name;
			logger.info("xpath appended by the node name in HOME.xml");
			String home_locator = uiPopulation.getNodeValByXpath(xpath);
			logger.info("Node value(locator) of the given node::"
					+ home_locator);

			iedriver.findElement(cmn.getLocator(home_locator)).click();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			iedriver.stop();
			return false;
		}
	}
}
