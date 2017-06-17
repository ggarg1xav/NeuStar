package chbasic.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;


import org.apache.log4j.Logger;
import org.openqa.selenium.support.ui.Select;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import chbasic.utils.UIDriver;

/**
 * 
 * @author abhishekr.gupta
 * 
 */

public class UIPopulation {

	private Document orDocument;
	private Document testDataDocument;
	private static final Logger logger = Logger.getLogger(UIPopulation.class);
	UIDriver iedriver;
	static CommonUtils commonUtils = new CommonUtils();

	public UIPopulation() {

	}

	/**
	 * constructor which take file path as params
	 * 
	 * @param or_path
	 * @param test_path
	 * @throws MalformedURLException
	 */
	public UIPopulation(String or_path, String test_path, UIDriver driver) throws MalformedURLException {
		iedriver = driver;
		logger.info(
				"------------------------------------------------------------------------------------------------------------------------------------******** Started populating*******------------------------------------------------------------------------------------------------------------------------------------------");
		logger.info(
				"----------------------------------------------------------------------------------------------test_path is--------------------------------------------------------------------------------------------------------------------------------- :"
						+ test_path);
		try {
			if (test_path != null) {
				DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				File test_file = new File(test_path);
				System.out.println("aaaaaaaaaaaaaaa" + test_path + "bbbbbbbbbb" + test_file);

				testDataDocument = dBuilder.parse(test_file);
			}
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			File or_file = new File(or_path);

			orDocument = builder.parse(or_file);

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Exception while making the document object of XML files");
		}

	}

	/**
	 * This constructor takes the input strem in params
	 * 
	 * @param or_path
	 * @param test_path
	 * @throws MalformedURLException
	 * @throws InterruptedException
	 */
	public UIPopulation(InputStream or_path, InputStream test_path, UIDriver driver)
			throws MalformedURLException, InterruptedException {
		iedriver = driver;
		logger.info("******** Started populating*******");
		try {
			if (test_path != null) {
				DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

				testDataDocument = dBuilder.parse(test_path);
			}
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

			orDocument = builder.parse(or_path);

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Exception while making the document object of XML files");
		}

	}

	/**
	 * This method used to initialize the parsing mechanism
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public boolean populate() throws InterruptedException {
		boolean flag = false;

		try {
			if (testDataDocument.hasChildNodes()) {

				dataPopulator(testDataDocument.getChildNodes());

				flag = true;
			}
		} catch (Exception e) {
			logger.error("Exception occure while calling the parsing mechanism");
			e.getMessage();

		}
		return flag;

	}

	/**
	 * 
	 * @param nodeValFrmOr
	 * @return This method used to split string in order to perform specific
	 *         operation for respective fields
	 * 
	 */
	public String[] getLocatorAttributes(String nodeValFrmOr) {

		String val[] = nodeValFrmOr.split("\\|");

		return val;

	}

	/**
	 * Function that returns node value attribute by given xpath. called in
	 * printNote
	 * 
	 * @param xpath
	 * @return
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws XPathExpressionException
	 */
	public String getNodeValByXpath(String xpath)
			throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
		String nodeVal = "";
		logger.info(" xpath " + xpath);
		/**
		 * check for SupplierLSROrderRequest (for "xpath" change )
		 */
		if (xpath.contains("SupplierLSROrderRequest")) {
			xpath = xpath.replaceAll("SupplierLSROrderRequest", "lsr_order");
			logger.info("'SupplierLSROrderRequest' replaced by 'lsr_order' in xpath ");
		}
		try {
			XPath xPath = XPathFactory.newInstance().newXPath();

			nodeVal = xPath.compile(xpath + "/@value").evaluate(orDocument);
		} catch (Exception e) {
			e.printStackTrace();

		}
		logger.info("node value returned from the given xpath" + nodeVal);
		return nodeVal;
	}

	/**
	 * 
	 * @param test_tempNode
	 * @return xpath
	 * 
	 *         Function that returns xpath called from printNote
	 * @throws InterruptedException
	 */
	private String getXPath(Node test_tempNode) throws InterruptedException {

		if (test_tempNode == null || test_tempNode.getNodeType() != Node.ELEMENT_NODE) {
			return "";
		}

		// find index of the test_tempNode node in parent "list"
		Node parent = test_tempNode.getParentNode();
		NodeList childNodes = parent.getChildNodes();
		int index = 0;
		int found = 0;
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node current = childNodes.item(i);
			if (current.getNodeName().equals(test_tempNode.getNodeName())) {
				if (current == test_tempNode) {
					found = index;
				}
				index++;
			}
		}

		String strIdx = "[" + found + "]";
		if (index == 1) {
			strIdx = "";
		}
		return getXPath(test_tempNode.getParentNode()) + "/" + test_tempNode.getNodeName() + strIdx;
	}

	/**
	 * 
	 * @param xpath
	 * @return
	 */
	private static ArrayList<Integer> getIndexList(String xpath) {
		ArrayList<Integer> tempIndexList = new ArrayList<Integer>();

		while (xpath.contains("]")) {
			xpath = xpath.substring(0, xpath.lastIndexOf(']'));
			tempIndexList.add(Integer.parseInt(xpath.substring(xpath.lastIndexOf('[') + 1, xpath.length())));

		}
		logger.info("list size---->" + tempIndexList.size());
		return tempIndexList;

	}

	/***
	 * TBD
	 * 
	 * @param xpath
	 * @return
	 */
	public boolean getXpathPosition(String xpath) {
		boolean pos;
		if ((xpath.charAt(xpath.lastIndexOf("/") - 1)) == ']') {
			pos = false;
		} else {
			pos = true;
		}
		return pos;
	}

	/**
	 * This function used to update locator by replacing ")" by "!" and again
	 * replacing "!" by ")" finally
	 * 
	 * @param nodeValFrmOr
	 * @param indexList
	 * @return
	 */
	private String updateLocator(String nodeValFrmOr, ArrayList<Integer> indexList, boolean pos) {
		String updatedLocator = "";

		String first, second;
		logger.info("veriffying pos=" + pos);
		if (pos && nodeValFrmOr.contains(")")) {
			int i = nodeValFrmOr.lastIndexOf(")");
			if (i >= 0) {
				nodeValFrmOr = nodeValFrmOr.substring(0, i) + "!" + nodeValFrmOr.substring(i + 1);
				logger.info("after replacement" + nodeValFrmOr);
			}

		}
		if (!(nodeValFrmOr.equals(null))) {
			if (nodeValFrmOr.contains(")")) {

				for (int i = 0; i < indexList.size(); i++) {
					logger.info("debugging node value---->" + nodeValFrmOr);
					first = nodeValFrmOr.substring(0, nodeValFrmOr.lastIndexOf(")"));
					second = nodeValFrmOr.substring(nodeValFrmOr.lastIndexOf(")"), nodeValFrmOr.length());
					logger.info("FIRST--->" + first);
					logger.info("SECOND--->" + second);

					second = second.replaceAll("[)]", "!");
					first = first.substring(0, first.length() - 1) + indexList.get(i);
					// + first.substring(0, first.length());
					nodeValFrmOr = first + second;

				}
			}
		}

		updatedLocator = nodeValFrmOr.replaceAll("!", ")");

		logger.info("UPDATED LOCATOR--->" + updatedLocator);
		return updatedLocator;
	}

	/**
	 * 
	 * @param test_nodeList
	 * @throws XPathExpressionException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerException
	 * 
	 *             A recursive function that call its self to parse the
	 *             TEST_DATA xml which is one to one mapped to OR. Populating
	 *             the data from test data on gui also
	 * @throws InterruptedException
	 */

	private void dataPopulator(NodeList test_nodeList)
			throws XPathExpressionException, ParserConfigurationException, SAXException, IOException,
			TransformerFactoryConfigurationError, TransformerException, InterruptedException {
		Thread.sleep(4000);
		String fieldLocator = "";
		String updatedLocator = "";
		boolean pos = false;

		logger.info("Inside dataPopulator");

		ArrayList<String> repeatingElementList = new ArrayList<String>();

		for (int orNodeCount = 0; orNodeCount < (test_nodeList.getLength()); orNodeCount++) {

			Node test_tempNode = test_nodeList.item(orNodeCount);

			String tempXpath;

			// make sure it's element node.
			if (test_tempNode.getNodeType() == Node.ELEMENT_NODE) {
				// getting indexed xpath
				String xpath = getXPath(test_tempNode);

				if (!(xpath.indexOf("]") == -1)) {
					pos = getXpathPosition(xpath);
					// replace [] from indexed xpath

					tempXpath = xpath.replaceAll("\\[.*?\\]", "");

				} else {
					tempXpath = xpath;
				}
				logger.debug("got indexed xpath-->  " + xpath + "  for Field Name--->  " + test_tempNode.getNodeName());
				String nodeValFrmOr = getNodeValByXpath(tempXpath);

				logger.debug("Got locator from OR-->  " + nodeValFrmOr);
				// null check for node value of OR
				if (!(nodeValFrmOr.equals(null) || nodeValFrmOr == "")) {

					String[] valNodes = getLocatorAttributes(nodeValFrmOr);
					logger.debug("OR value array length = " + valNodes.length);
					logger.info("Node Name =" + test_tempNode.getNodeName() + " [OPEN]");

					logger.info("Node Value =" + test_tempNode.getNodeName());

					ArrayList<Integer> indexList = getIndexList(xpath);

					logger.info("index list contents::" + indexList.toString());

					if (!(indexList.isEmpty())) {

						logger.info("boolean value found to de--->>>" + pos);
						updatedLocator = updateLocator(valNodes[1], indexList, pos);
						valNodes[1] = updatedLocator;
					}

					/*
					 * if ((valNodes[0] != "") && (valNodes[1] != "")) {
					 * 
					 * fieldLocator = valNodes[0] + "|" + valNodes[1];
					 * logger.info("*********" + fieldLocator); }
					 */
					fieldLocator = valNodes[0] + "|" + valNodes[1];
					String currTestData = "";
					logger.info("111valNodes.length -- " + valNodes.length);
					try {
						logger.info("Node Name =" + test_tempNode.getAttributes().getNamedItem("value").getNodeValue()
								+ " [CLOSE]");
						currTestData = test_tempNode.getAttributes().getNamedItem("value").getNodeValue();

					} catch (Exception e) {
						// try block for empty test data nodes
					}
					// if block for POPULATED in TEXTBOX
					if (valNodes.length == 2) {
						logger.debug("This Field is to be POPULATED in TEXTBOX");
						logger.debug("Locator Val=" + valNodes[1]);
						iedriver.findElement(commonUtils.getLocator(fieldLocator)).clear();
						logger.info("populating value in textbox::: value=" + currTestData);
						if (!(currTestData.equals(null) || currTestData.equals(""))) {

							iedriver.findElement(commonUtils.getLocator(fieldLocator)).sendKeys(currTestData);

						}
						logger.info("field populated");

					} // for field to be NAVIGATED
					else if (valNodes.length == 3) {
						if (valNodes[2].equals("navigate")) {
							logger.debug("This field is to be NAVIGATED");
							logger.info("Locator Val=" + valNodes[1]);
							iedriver.switchTodefault();
							logger.debug("On default Frame");
							iedriver.switchToFrame("navFrame");
							logger.debug("On nav frame");

							iedriver.findElement(commonUtils.getLocator(fieldLocator)).click();
							logger.debug("respective field clicked");
							iedriver.switchTodefault();
							iedriver.switchToFrame("msgFrame");

							logger.debug("On msg Frame");

						} else if (valNodes[2].equals("addSection")) {
							logger.info("adding form by this locator=  " + valNodes[1]);

							logger.info("respective field clicked");
							iedriver.switchTodefault();
							iedriver.switchToFrame("msgFrame");

							if (repeatingElementList.contains(tempXpath)) {
								iedriver.findElement(commonUtils.getLocator(fieldLocator)).click();
								logger.info("Repeating the sections--->" + test_tempNode.getNodeName());
							} else {

								repeatingElementList.add(tempXpath);
								logger.info("Adding Section to repeating Element List elements--->" + tempXpath);

							}
						} else if (valNodes[2].equals("addSubSection")) {
							logger.info("adding form by this locator=  " + valNodes[1]);

							logger.info("respective field clicked");
							iedriver.switchTodefault();
							iedriver.switchToFrame("msgFrame");
							if (repeatingElementList.contains(tempXpath)) {
								iedriver.findElement(commonUtils.getLocator(fieldLocator)).click();
								logger.info("Repeating the sections--->" + test_tempNode.getNodeName());
							} else {
								repeatingElementList.add(tempXpath);
								logger.info("Adding Subsection to repeating Element List elements--->" + tempXpath);
							}

						} // if block for ccna and icsc
						else if (valNodes[2].equals("typeOrSelect")) {
							String type = "";
							type = iedriver.findElement(commonUtils.getLocator(fieldLocator)).getTagName();
							if (type.equals("input")) {
								logger.debug("This Field is to be POPULATED in TEXTBOX");
								logger.debug("Locator Val=" + valNodes[1]);
								iedriver.findElement(commonUtils.getLocator(fieldLocator)).clear();
								logger.info("populating value in textbox::: value=" + currTestData);
								if (!(currTestData.equals(null) || currTestData.equals(""))) {

									iedriver.findElement(commonUtils.getLocator(fieldLocator)).sendKeys(currTestData);

								}
								logger.info("field populated");

							} else {
								logger.info("populating value by selecting the values :::: value=" + currTestData);
								if (!(currTestData.equals(null) || currTestData.equals(""))) {

									new Select(iedriver.findElement(commonUtils.getLocator(fieldLocator)))
											.selectByVisibleText(currTestData);

									System.out.println(
											"-------------------------------------------------------" + currTestData);
									logger.info("Field populated by selecting the value" + currTestData);

								}
							}
						}

						else {

							for (int i = 0; i < valNodes.length; i++) {

								if (valNodes[i] == "") {
									logger.info("Empty Node");
								} else if ((valNodes[2].equals("select"))) {
									logger.info("This Field is to be POPULATED by SELECT");
									logger.info("Locator Val=" + valNodes[1]);
									// in case of multiple selection in search
									System.out.println("String^^^^^" + currTestData);
									if (currTestData.contains("*")) {

										for (String data : currTestData.split("\\*")) {

											System.out.println(
													"populating value by selecting the values :::: value=" + data);

											new Select(iedriver.findElement(commonUtils.getLocator(fieldLocator)))
													.selectByVisibleText(data);

											logger.info("Field populated by selecting the value" + data);

										}
									} else {
										logger.info(
												"populating value by selecting the values :::: value=" + currTestData);
										if (!(currTestData.equals(null) || currTestData.equals(""))) {

											new Select(iedriver.findElement(commonUtils.getLocator(fieldLocator)))
													.selectByVisibleText(currTestData);
											logger.info("Field populated by selecting the value" + currTestData);

										}

									}
								}
							}

						}
					}
				}

				if (test_tempNode.hasChildNodes()) {

					// loop again if it has child nodes
					dataPopulator(test_tempNode.getChildNodes());

				}
				try {
					logger.info("Node Name =" + test_tempNode.getAttributes().getNamedItem("value").getNodeValue()
							+ " [CLOSE]");
				} catch (Exception e) {

				}

			}

		}

	}
}