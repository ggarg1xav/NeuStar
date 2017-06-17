package chbasic.ui;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import chbasic.utils.CommonUtils;
import chbasic.utils.FieldUtils;
import chbasic.utils.ReadProperties;
import chbasic.utils.UIDriver;
import chbasic.utils.UIPopulation;

/**
 * 
 * @author abhishekr.gupta
 * 
 */
public class OrderHistory {

	private static Logger logger = Logger.getLogger(OrderHistory.class);
	UIDriver iedriver;
	UIPopulation uiPopulation;
	CommonUtils cn = new CommonUtils();

	public OrderHistory(UIDriver driver) throws MalformedURLException {
		iedriver = driver;
		logger.info("home page clicked");

	}

	public OrderHistory(String or_path, UIDriver driver) throws Exception {
		uiPopulation = new UIPopulation(or_path, null, driver);
		iedriver = driver;

		// DocumentBuilder builder = DocumentBuilderFactory.newInstance()
		// .newDocumentBuilder();
		// File or_file = new File(or_path);

		// orDocument = builder.parse(or_file);
		// uiPopulation = new UIPopulation(order_history_or_path, null, driver);
		logger.info("parsing order history or file...");
	}

	public boolean verifyResponses(String remarks) throws InterruptedException {
		boolean flag = true;

		logger.info("Recieved String remarks: " + remarks);
		String remarksArray[] = remarks.split(":");
		logger.info("Got remarks to verify in order history");
		logger.info("String Array Size is=" + remarksArray.length);

		for (int i = 0; i < remarksArray.length; i++) {
			logger.info("looping for each responseArray element");
			for (int k = 0; k < 20; k++) {
				iedriver.refresh();
				List<WebElement> testTable = iedriver
						.findElements(By
								.xpath("//table[@class='Main']/tbody/tr[5]/td/table/tbody/tr/td[3]"));

				for (int j = 0; j < (testTable.size() - 1); j++) {
					logger.info("looping in table row");
					if (!iedriver
							.findElement(
									By.xpath("//table[@class='Main']/tbody/tr[5]/td/table/tbody/tr/td[3]"))
									.isDisplayed()) {

						logger.info("Transaction form not available");
						flag = false;
						continue;

					} else {
						String respFrmGui = testTable.get(j).getText();
						logger.info("Response Type got from ORDER HISTORY="
								+ respFrmGui);
						if (respFrmGui.equals(remarksArray[i])) {
							logger.info("Response recieved successfully::"
									+ respFrmGui + "=" + remarksArray[i]);
							flag = true;
							break;

						} else {
							logger.info("Response is not successfully recieved::"
									+ remarksArray[i]);
							flag = false;
						}
					}

				}
				if (flag == true) {
					continue;
				} else {
					break;
				}

			}
		}
		if (flag == true) {
			logger.info("Remarks is verify Successfully On OrderHistory Page");
		} else {
			logger.info("Remarks is not received ");
		}
		return flag;

	}





	public boolean VerifyGUIResponsesASR(String ExpectedResponses, Map<String,String>testCaseContext,String ILEC) throws XPathExpressionException, IOException, ParserConfigurationException, SAXException, InterruptedException {

		String xpath ="/order_history/History_Table/ViewPON";
		String ViewPON = uiPopulation.getNodeValByXpath(xpath);
		iedriver.findElement(cn.getLocator(ViewPON)).click(); 
		List<String> explist = new ArrayList<String>();

		File filePath = new File("repository/testdata/input_request_xml/ASR_SEND_ORDER/"
				+ ILEC + "/" + testCaseContext.get("BODY") + ".xml");
		System.out.println("file path is :"+ filePath);
		DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
		dbfactory.setNamespaceAware(true);
		DocumentBuilder dbuilder = dbfactory.newDocumentBuilder();
		Document doc = dbuilder.parse((filePath));
		String reqType =doc.getElementsByTagName("REQTYP").item(0).getAttributes().getNamedItem("value").getNodeValue();
		Thread.sleep(10000);
		//    String ver="0";
		//    String ExpResp[]=ExpectedResponses.split("\\|");
		//    
		//    System.out.println("ExpResp.length"+ExpResp.length);
		//    for(int Exprescnt=0;Exprescnt<ExpResp.length;Exprescnt++)
		//    {                    
		//    //       iedriver.findElement(By.xpath("//img[@alt='View Detail']")).click();
		//          ver="0"+Exprescnt;
		//Properties asrResponseMappings=ReadProperties.loadProperties("repository/productproperties/asr_gui_response_mapping.properties");              
		//
		//
		//    for (String token : ExpResp[Exprescnt].split(":")) {
		//          String val=asrResponseMappings.getProperty(token);            
		//explist.add(val);
		//    }
		//
		//    }
		FieldUtils fu = new FieldUtils();
		String CurrVer=testCaseContext.get("VER").trim();

		System.out.println("GUI ver CurrVer"+CurrVer);
		String flow="NEW|"+testCaseContext.get("FLOW");
		//String Expflow[]=flow.split("\\|");		
		String ExpResp[]=ExpectedResponses.split("\\|");		
		System.out.println("flowv"+flow);
		for(int Exprescnt=0;Exprescnt<ExpResp.length;Exprescnt++)
		{               

			//			String ver="0"+Exprescnt;
			String ver=CurrVer;
			Properties asrResponseMappings=ReadProperties.loadProperties("repository/productproperties/asr_gui_response_mapping.properties");             

			for (String token : ExpResp[Exprescnt].split(":")) {
				String val=asrResponseMappings.getProperty(token)+":"+ver;            
				explist.add(val);			
			}
			//	if(!(Expflow[Exprescnt].equals("SUPP1"))){
			explist.add(reqType+":save"+":"+ver);
			explist.add(reqType+":submit"+":"+ver);
			CurrVer=fu.getVER(ver);

			//}
		}
		System.out.println("explist"+explist);


		xpath = "/order_history/History_Table/ResultTable";
		String HistoryTable_locator = uiPopulation.getNodeValByXpath(xpath);
		logger.info("Order_type Locator::" + HistoryTable_locator);

		int Histtabsize=iedriver.findElements(cn.getLocator(HistoryTable_locator)).size();
		// int sze=iedriver.findElements(By.xpath("//table[@class='List']/tbody/form")).size();
		List<String> list = new ArrayList<String>();
		System.out.println("Hist tab size"+Histtabsize);
		for(int i = 0;i <= Histtabsize;i++)
		{
			xpath = "/order_history/History_Table/DynamicTableAtt";
			String RuntimeTable_locator_status = uiPopulation.getNodeValByXpath(xpath);
			if(RuntimeTable_locator_status.contains("RUNTIMECnt")){
				String RuntimeAtt=Integer.toString(i);

				System.out.println("Rutime att is"+RuntimeAtt);
				RuntimeTable_locator_status=RuntimeTable_locator_status.replace("RUNTIMECnt",RuntimeAtt);
			}
			System.out.println("RuntimeTable_locator iss"+RuntimeTable_locator_status);


			xpath = "/order_history/History_Table/actDynamicTableAtt";
			String Act_RuntimeTable_locator = uiPopulation.getNodeValByXpath(xpath);
			System.out.println("Act_RuntimeTable_locator vv"+Act_RuntimeTable_locator);
			if(Act_RuntimeTable_locator.contains("RUNTIMECnt")){
				String actRuntimeAtt=Integer.toString(i);

				System.out.println("Rutime att is"+actRuntimeAtt);
				Act_RuntimeTable_locator=Act_RuntimeTable_locator.replace("RUNTIMECnt",actRuntimeAtt);
			}
			System.out.println("RuntimeTable_locator isss"+Act_RuntimeTable_locator);

			xpath = "/order_history/History_Table/verDynamicTableAtt";
			String Ver_RuntimeTable_locator = uiPopulation.getNodeValByXpath(xpath);
			if(Ver_RuntimeTable_locator.contains("RUNTIMECnt")){
				String verRuntimeAtt=Integer.toString(i);       
				System.out.println("Rutime verRuntimeAtt is"+verRuntimeAtt);
				Ver_RuntimeTable_locator=Ver_RuntimeTable_locator.replace("RUNTIMECnt",verRuntimeAtt);
			}

			//Thread.sleep(1000);
			if (iedriver.findElement(By.xpath(RuntimeTable_locator_status)).isDisplayed()){

				String ActualVer=iedriver.findElement(By.xpath(Ver_RuntimeTable_locator)).getText().trim();   
				String ActualStatus=iedriver.findElement(By.xpath(RuntimeTable_locator_status)).getText();
				String ActualAct=iedriver.findElement(By.xpath(Act_RuntimeTable_locator)).getText();
				//        xpath|//form[@name=&quot;Transaction-RUNTIMECnt-Form&quot;]/tr/td[6]"/>
				//String ActualStatus=iedriver.findElement(cn.getLocator(RuntimeTable_locator)).getText();
				String FinActStatus=ActualStatus+":"+ActualAct+":"+ActualVer;
				System.out.println("act status"+FinActStatus);
				list.add(FinActStatus);
				System.out.println("Displayed list"+list);
				//}//new if


			}//if displayed
		}

		//explist.add(reqType+":save");
		//explist.add(reqType+":submit");
		System.out.println("actual cal sz is "+list.size());
		System.out.println("actual list cal is "+list);
		System.out.println("exp cal sz is "+explist.size());
		System.out.println("exp list cal is "+explist);
		//System.out.println("actual lenmis:"+arr.length);



		if( explist.containsAll(list)&& (explist.size()==list.size()) ){
			System.out.println("list verified");
			//return true;
		}
		else{
			System.out.println("list not verified");  
			return false;
		}
		// }
		return true;
	}


	public boolean selectAction(String action) {
		try {
			logger.info("Recieved action=" + action);
			List<WebElement> tableRows = iedriver
					.findElements(By
							.xpath("//table[@class='Main']/tbody/tr[5]/td/table/tbody/tr"));
			System.out.println("test table size=" + tableRows.size());
			for (int i = 1; i < (tableRows.size() - 1); i++) {
				System.out.println("printing table row contents="
						+ tableRows.get(i).getText());
				logger.info(" innerhtml --> "
						+ tableRows.get(i).getAttribute("innerHTML"));
				logger.info("looping in order history table");
				String status = tableRows.get(i).findElement(By.xpath("td[3]"))
						.getText();
				logger.info("status" + status);
				if (status.equalsIgnoreCase("new")) {

					WebElement select = tableRows.get(i).findElement(
							By.xpath("td[2]"));

					new Select(select.findElement(By.name("ActionDropDown")))
					.selectByVisibleText(action);
					select.findElement(By.xpath("//img[@alt='Perform Action']"))
					.click();
				}
			}
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			iedriver.stop();
			return false;
		}
	}

	public boolean orderAction(String action) throws XPathExpressionException,
	IOException, ParserConfigurationException, SAXException {
		try {
			iedriver.switchTodefault();
			String button_xpath = "/request/order_history/action_button/";
			String order_history_button_loc = "";

			button_xpath = button_xpath + "/" + action;
			order_history_button_loc = uiPopulation
					.getNodeValByXpath(button_xpath);
			logger.info("button_loc for SUSPEND ORDER="
					+ order_history_button_loc);
			iedriver.findElement(cn.getLocator(order_history_button_loc))
			.click();
			logger.info("Order Suspended button clicked for order");
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * this method selects the response type by drop down from Order History
	 * page
	 * 
	 * @return
	 */
	public boolean selectResponseLSRRecieveOrder(String respType) {
		List<WebElement> tableRows;
		boolean flag = false;
		try {
			iedriver.switchTodefault();
			System.out.println("respType now"+respType);
			Thread.sleep(2000);
			WebElement elem = iedriver.findElement(By.xpath("//td[contains(text(),'receive')]/parent::tr/td[2]/select"));
			//WebElement elem = iedriver.findElement(By.xpath("//td[@name=ActionDropDown]"));
			Select sel1=new Select(elem);
			for (WebElement webElement : sel1.getOptions())
			{
				System.out.println("**element is present  "+webElement.getText());
			}
			sel1.selectByValue(respType);
			Thread.sleep(4000);
			WebElement buttonLoc = iedriver.findElement(By
					.xpath("//td[contains(text(),'receive')]/parent::tr/td[2]/a"));
			logger.info("+++++++++++>" + buttonLoc.getAttribute("alt"));
			buttonLoc.click();
			//tableRows = iedriver
					//.findElements(By
						//	.xpath("//table[@class = 'Main']/tbody/tr[5]/td/table/tbody//tr"));
			//for (int i = 0; i < tableRows.size(); i++) {
			//	logger.info("Checking row " + "@@@"+i+"@@@" + tableRows.get(i).getText());
			//	if (tableRows.get(i).getText().contains("receive")) 
			//	{
			//		System.out.println("xpath created //table[@class = 'Main']/tbody/tr[5]/td/table/tbody//tr["+(i+1)+"]/td[2]/select");
				//	WebElement select = iedriver.findElement(By.xpath("//table[@class = 'Main']/tbody/tr[5]/td/table/tbody//tr["+(i+1)+"]/td[2]/select"));
				//	Thread.sleep(4000);
					//System.out.println("@@@@@@@@@"+i);
				//	Select sel=new Select(select);
				//	List<WebElement> list = sel.getOptions();
					//new Select(select.findElement(By.name("ActionDropDown"))).selectByValue(respType);
				//	for (WebElement webElement : list) {
				//		System.out.println("**element is present  "+webElement.getText());
				//	}
				//	sel.selectByValue(respType);
					
				//	break;
			//	}
			//	}
			flag = true;
			return flag;

		} catch (Exception exception) {
			exception.printStackTrace();
			return flag;
		}
	}
	
	public boolean checkDuplicateRequest() {
		boolean flag = false;
		System.out.println(iedriver);
		 List<WebElement> tableRows = iedriver.findElements(By.xpath("//table[@class = 'Main']/tbody/tr[5]/td/table/tbody//tr"));
		for (int i = 0; i < tableRows.size(); i++) {
		logger.info("Checking row " + "@@@"+i+"@@@" + tableRows.get(i).getText());
			if (tableRows.get(i).getText().contains("FOC Accept") || tableRows.get(i).getText().contains("Supplement Accept")) {
				System.out.println("Accept Found" + tableRows.get(i).getText());
				flag = true;
			}
			break;
		}
		
	    
		return flag;
		
	}
	
	

	/**
	 * this method selects the supplements in order history page
	 */
	
	//Need to change here
	public boolean selectSupplementLSRRecieveOrder(String supType) {
		List<WebElement> tableRows;
		boolean flag = false;
		try {
			iedriver.switchTodefault();
			WebElement elem = iedriver.findElement(By.xpath("//td[contains(text(),'Submit')]/parent::tr/td[2]/select"));
			Select sel1=new Select(elem);
			for (WebElement webElement : sel1.getOptions())
			{
				System.out.println("**element is present  "+webElement.getText());
			}
			sel1.selectByValue(supType);
			Thread.sleep(2000);
			WebElement buttonLoc = iedriver.findElement(By
					.xpath("//td[contains(text(),'Submit')]/parent::tr/td[2]/a"));
			logger.info("+++++++++++>" + buttonLoc.getAttribute("alt"));
			buttonLoc.click();
			if (supType.equals("send-supp1")) {
				iedriver.switchToAlert();

			}
			/*tableRows = iedriver
					.findElements(By
							.xpath("//table[@class = 'Main']/tbody/tr[5]/td/table/tbody/tr"));
			for (int i = 0; i < tableRows.size(); i++) {
				logger.info("Checking row" + tableRows.get(i).getText());
				if (tableRows.get(i).getText().contains("Submit")) {
					WebElement select = tableRows.get(i).findElement(
							By.xpath("td[2]"));
					new Select(select.findElement(By.name("ActionDropDown")))
					.selectByValue(supType);

					WebElement buttonLoc = select.findElement(By
							.xpath(".//a/img[@alt='Perform Action']"));
					logger.info("+++++++++++>" + buttonLoc.getAttribute("alt"));
					buttonLoc.click();
					if (supType.equals("send-supp1")) {
						iedriver.switchToAlert();

					}

					break;

				}
			}*/
			flag = true;
			return flag;

		} catch (Exception exception) {
			exception.printStackTrace();
			return flag;
		}
	}


	public boolean selectSupplementLSRSendOrder(String supType) throws MalformedURLException {	

		List<WebElement> tableRows;
		boolean flag = true;
		try {			
			String xpath ="/order_history/History_Table/ViewPON";
			String ViewPON = uiPopulation.getNodeValByXpath(xpath);
			Thread.sleep(3000);
			iedriver.findElement(cn.getLocator(ViewPON)).click();
			String title = iedriver.getTitle();
			System.out.println(title);
			if (title.contains("Order History")){
				System.out.println("View Button Clicked");	
			}else{
				Thread.sleep(3000);
				iedriver.findElement(cn.getLocator(ViewPON)).click();
			}
			Thread.sleep(4000);
			iedriver.switchTodefault();

			tableRows = iedriver.findElements(By.xpath("//table[@class = 'Main']/tbody/tr[5]/td/table/tbody/tr"));
			System.out.println("tableRows"+tableRows);
			for (int i = 0; i < tableRows.size(); i++) {	
				logger.info("Checking row" + tableRows.get(i).getText());
				if (tableRows.get(i).getText().contains("Submit")) {					
					WebElement select = tableRows.get(i).findElement(By.xpath("//form[@name='Transaction-0-Form']/tr/td[2]/a/img"));
					new Select(iedriver.findElement(By.xpath("//form[@name='Transaction-0-Form']/tr/td[2]/select"))).selectByValue(supType);					
					Thread.sleep(3000);
					select.click();
					break;

				}
			}
			flag = true;
			return flag;

		} catch (Exception exception) {
			exception.printStackTrace();
			return flag;
		}
	}





	public boolean VerifyLSRGUIResponses(String ExpectedResponses,Map<String,String>testCaseContext) throws XPathExpressionException, IOException, ParserConfigurationException, SAXException, InterruptedException {
		System.out.println("00000000000000000");
		Thread.sleep(100000);
		System.out.println("Helelelelelelele");
		//Thread.sleep(1050000);
		FieldUtils fu = new FieldUtils();
		String NewReqVer=testCaseContext.get("VER").trim();
		System.out.println("LSRSRSRSRSRSRSRSRSRSRSRSRSR");
		String xpath ="/order_history/History_Table/ViewPON";
		String ViewPON = uiPopulation.getNodeValByXpath(xpath);
		Thread.sleep(1000);
		iedriver.findElement(cn.getLocator(ViewPON)).click();
		//Change Here
		String title = iedriver.getTitle();
		System.out.println(title);
		if (title.contains("Order History")){
			System.out.println("View Button Clicked");	
		}else{
			iedriver.findElement(cn.getLocator(ViewPON)).click();
		}
		Thread.sleep(5000);
		//String ver="0";	
		Thread.sleep(5000);
		System.out.println("ExpectedResponses are"+ExpectedResponses);			
		Properties LSRDBResponseMappings=ReadProperties.loadProperties("repository/productproperties/lsr_responses_mappings.properties");
		Properties LSRGUIResponseMappings=ReadProperties.loadProperties("repository/productproperties/lsr_DBGUI_responses_mappings.properties");		 
		String AllResp[]=ExpectedResponses.split("\\|");
		for (int flowCounter = 0; flowCounter < AllResp.length; flowCounter++) {
			//		ver=0+String.valueOf(flowCounter+1);
			//	ver=NewReqVer;
			String currresp=AllResp[flowCounter];
			System.out.println("currresp to verify"+currresp);
			System.out.println("ver fin"+NewReqVer);
			List<String> explist = new ArrayList<String>();
			String RespProp=LSRDBResponseMappings.getProperty(currresp);

			int k=1;

			for (String token : RespProp.split(",")) {
				for (String Resp : token.split(":")) {

					System.out.println("resp at "+k +"==="+Resp);
					System.out.println("property value ====="+LSRGUIResponseMappings.getProperty(Resp));

					RespProp=RespProp.replaceAll(Resp, LSRGUIResponseMappings.getProperty(Resp));		
				}

			}


			for (String GUIResp : RespProp.split(",")) {		
				explist.add(GUIResp);	
			}
			System.out.println("explist val"+explist);

			xpath = "/order_history/History_Table/ResultTable";
			String HistoryTable_locator = uiPopulation.getNodeValByXpath(xpath);
			logger.info("Order_type Locator::" + HistoryTable_locator);

			int Histtabsize=iedriver.findElements(cn.getLocator(HistoryTable_locator)).size();

			List<String> list = new ArrayList<String>();
			for(int i = 0;i <= Histtabsize;i++)
			{
				xpath = "/order_history/History_Table/DynamicTableAtt";
				String RuntimeTable_locator_status = uiPopulation.getNodeValByXpath(xpath);
				if(RuntimeTable_locator_status.contains("RUNTIMECnt")){
					String RuntimeAtt=Integer.toString(i);			
					RuntimeTable_locator_status=RuntimeTable_locator_status.replace("RUNTIMECnt",RuntimeAtt);
				}  			

				xpath = "/order_history/History_Table/actDynamicTableAtt";
				String Act_RuntimeTable_locator = uiPopulation.getNodeValByXpath(xpath); 		
				if(Act_RuntimeTable_locator.contains("RUNTIMECnt")){
					String actRuntimeAtt=Integer.toString(i); 			
					Act_RuntimeTable_locator=Act_RuntimeTable_locator.replace("RUNTIMECnt",actRuntimeAtt);
				}


				xpath = "/order_history/History_Table/verDynamicTableAtt";
				String Ver_RuntimeTable_locator = uiPopulation.getNodeValByXpath(xpath); 	
				if(Ver_RuntimeTable_locator.contains("RUNTIMECnt")){
					String verRuntimeAtt=Integer.toString(i); 				 		
					Ver_RuntimeTable_locator=Ver_RuntimeTable_locator.replace("RUNTIMECnt",verRuntimeAtt);
				}


				if (iedriver.findElement(By.xpath(RuntimeTable_locator_status)).isDisplayed()){    
					String ActualVer=iedriver.findElement(By.xpath(Ver_RuntimeTable_locator)).getText().trim();      
					System.out.println("Exp and act ver"+ActualVer+"ex"+NewReqVer);

					if(NewReqVer.equals(ActualVer)){
						String ActualStatus=iedriver.findElement(By.xpath(RuntimeTable_locator_status)).getText().trim();
						String ActualAct=iedriver.findElement(By.xpath(Act_RuntimeTable_locator)).getText().trim();
						String FinActStatus=ActualStatus+":"+ActualAct;   	   
						list.add(FinActStatus);
					}


				}
			}

			System.out.println("actual list size is "+list.size());
			System.out.println("actual list is "+list);
			System.out.println("expected list size is "+explist.size());
			System.out.println("expected list is "+explist);




			if( explist.containsAll(list)&& (explist.size()==list.size()) ){
				System.out.println("Responses verified.Result is Success"+flowCounter);
				//return true;
			}
			else{
				System.out.println("Responses verified.Result is failure"+flowCounter);
				return false;
			}
			NewReqVer=fu.getVER(NewReqVer);
		}
		return true;


	}



	public boolean VerifyE911GUIResponses(String ExpectedResponses) throws XPathExpressionException, IOException, ParserConfigurationException, SAXException, InterruptedException {
		Thread.sleep(80000);
		// FieldUtils fu = new FieldUtils();

		String xpath ="/order_history/History_Table/ViewPON";
		String ViewPON = uiPopulation.getNodeValByXpath(xpath);
		iedriver.findElement(cn.getLocator(ViewPON)).click();

		//String ver="0";		
		System.out.println("ExpectedResponses are"+ExpectedResponses);			

		Properties E911GUIResponseMappings=ReadProperties.loadProperties("repository/productproperties/E911_gui_response_mapping.properties");		 
		String AllResp[]=ExpectedResponses.split("\\|");
		for (int flowCounter = 0; flowCounter < AllResp.length; flowCounter++) {
			//		ver=0+String.valueOf(flowCounter+1);
			//	ver=NewReqVer;
			String currresp=ExpectedResponses;
			System.out.println("currresp to verify"+currresp);

			List<String> explist = new ArrayList<String>();
			String RespProp=E911GUIResponseMappings.getProperty(currresp);




			for (String GUIResp : RespProp.split(",")) {		
				explist.add(GUIResp);	
			}
			System.out.println("explist val"+explist);

			xpath = "/order_history/History_Table/ResultTable";
			String HistoryTable_locator = uiPopulation.getNodeValByXpath(xpath);
			logger.info("Order_type Locator::" + HistoryTable_locator);

			int Histtabsize=iedriver.findElements(cn.getLocator(HistoryTable_locator)).size();

			List<String> list = new ArrayList<String>();
			for(int i = 0;i <= Histtabsize;i++)
			{
				xpath = "/order_history/History_Table/DynamicTableAtt";
				String RuntimeTable_locator_status = uiPopulation.getNodeValByXpath(xpath);
				if(RuntimeTable_locator_status.contains("RUNTIMECnt")){
					String RuntimeAtt=Integer.toString(i);			
					RuntimeTable_locator_status=RuntimeTable_locator_status.replace("RUNTIMECnt",RuntimeAtt);
				}  			

				xpath = "/order_history/History_Table/E911ActDynamicTableAtt";
				String Act_RuntimeTable_locator = uiPopulation.getNodeValByXpath(xpath); 		
				if(Act_RuntimeTable_locator.contains("RUNTIMECnt")){
					String actRuntimeAtt=Integer.toString(i); 			
					Act_RuntimeTable_locator=Act_RuntimeTable_locator.replace("RUNTIMECnt",actRuntimeAtt);
				}



				if (iedriver.findElement(By.xpath(RuntimeTable_locator_status)).isDisplayed()){        	

					String ActualStatus=iedriver.findElement(By.xpath(RuntimeTable_locator_status)).getText().trim();
					String ActualAct=iedriver.findElement(By.xpath(Act_RuntimeTable_locator)).getText().trim();
					String FinActStatus=ActualStatus+":"+ActualAct;   	   
					list.add(FinActStatus);  	

				}
			}

			System.out.println("actual list size is "+list.size());
			System.out.println("actual list is "+list);
			System.out.println("expected list size is "+explist.size());
			System.out.println("expected list is "+explist);




			if( explist.containsAll(list)&& (explist.size()==list.size()) ){
				System.out.println("Responses verified.Result is Success"+flowCounter);
				//return true;
			}
			else{
				System.out.println("Responses verified.Result is failure"+flowCounter);
				return false;
			}

		}
		return true;


	}



	public boolean VerifyLIDBGUIResponses(String ExpectedResponses) throws XPathExpressionException, IOException, ParserConfigurationException, SAXException, InterruptedException {
		Thread.sleep(8000);
		// FieldUtils fu = new FieldUtils();

		String xpath ="/order_history/History_Table/ViewPON";
		String ViewPON = uiPopulation.getNodeValByXpath(xpath);
		iedriver.findElement(cn.getLocator(ViewPON)).click();	

		System.out.println("ExpectedResponses are"+ExpectedResponses);			

		Properties LIDBGUIResponseMappings=ReadProperties.loadProperties("repository/productproperties/LIDB_gui_response_mapping.properties");		 
		//	 String AllResp[]=ExpectedResponses.split("\\|");
		//	for (int flowCounter = 0; flowCounter < AllResp.length; flowCounter++) {
		//		ver=0+String.valueOf(flowCounter+1);
		//	ver=NewReqVer;
		String currresp=ExpectedResponses;
		System.out.println("currresp to verify"+currresp);

		List<String> explist = new ArrayList<String>();
		String RespProp=LIDBGUIResponseMappings.getProperty(currresp);		 

		for (String GUIResp : RespProp.split(",")) {		
			explist.add(GUIResp);	
		}


		//	 explist.add(RespProp);


		System.out.println("explist val"+explist);

		xpath = "/order_history/History_Table/ResultTable";
		String HistoryTable_locator = uiPopulation.getNodeValByXpath(xpath);
		logger.info("Order_type Locator::" + HistoryTable_locator);

		int Histtabsize=iedriver.findElements(cn.getLocator(HistoryTable_locator)).size();

		List<String> list = new ArrayList<String>();
		for(int i = 0;i <= Histtabsize;i++)
		{
			xpath = "/order_history/History_Table/statusDynamicTableAtt";
			String RuntimeTable_locator_status = uiPopulation.getNodeValByXpath(xpath);
			if(RuntimeTable_locator_status.contains("RUNTIMECnt")){
				String RuntimeAtt=Integer.toString(i);			
				RuntimeTable_locator_status=RuntimeTable_locator_status.replace("RUNTIMECnt",RuntimeAtt);
			}  			

			xpath = "/order_history/History_Table/actDynamicTableAtt";
			String Act_RuntimeTable_locator = uiPopulation.getNodeValByXpath(xpath); 		
			if(Act_RuntimeTable_locator.contains("RUNTIMECnt")){
				String actRuntimeAtt=Integer.toString(i); 			
				Act_RuntimeTable_locator=Act_RuntimeTable_locator.replace("RUNTIMECnt",actRuntimeAtt);
			}



			if (iedriver.findElement(By.xpath(RuntimeTable_locator_status)).isDisplayed()){        	

				String ActualStatus=iedriver.findElement(By.xpath(RuntimeTable_locator_status)).getText().trim();
				String ActualAct=iedriver.findElement(By.xpath(Act_RuntimeTable_locator)).getText().trim();
				String FinActStatus=ActualStatus+":"+ActualAct;   	   
				list.add(FinActStatus);  	

			}
		}

		System.out.println("actual list size is "+list.size());
		System.out.println("actual list is "+list);
		System.out.println("expected list size is "+explist.size());
		System.out.println("expected list is "+explist);




		if( explist.containsAll(list)&& (explist.size()==list.size()) ){
			System.out.println("Responses verified.Result is Success");
			//return true;
		}
		else{
			System.out.println("Responses verified.Result is failure");
			return false;
		}

		//		}
		return true;


	}





	public boolean SuppASR(int count) throws XPathExpressionException, IOException, ParserConfigurationException, SAXException, InterruptedException {
		String RuntimeAtt=null;
		String  xpath;
		String  ViewPON;
		System.out.println("count vall"+count);
		//	 if(count!=1){
		xpath ="/order_history/History_Table/ViewPON";
		ViewPON = uiPopulation.getNodeValByXpath(xpath);
		Thread.sleep(10000);
		iedriver.findElement(cn.getLocator(ViewPON)).click();
		//Change Here
		String title = iedriver.getTitle();
		System.out.println(title);
		if (title.contains("Order History")){
			System.out.println("View Button Clicked");	
		}else{
			iedriver.findElement(cn.getLocator(ViewPON)).click();
		}

		// }
		Thread.sleep(5000);

		xpath = "/order_history/History_Table/ResultTable";
		String HistoryTable_locator = uiPopulation.getNodeValByXpath(xpath);
		logger.info("Order_type Locator::" + HistoryTable_locator);

		int sze=iedriver.findElements(cn.getLocator(HistoryTable_locator)).size();

		//List<String> list = new ArrayList<String>();
		System.out.println("szeeee"+sze);
		for(int i = 0;i <= sze;i++)
		{
			xpath = "/order_history/History_Table/actDynamicTableAtt";
			String RuntimeTable_locator = uiPopulation.getNodeValByXpath(xpath);
			if(RuntimeTable_locator.contains("RUNTIMECnt")){
				RuntimeAtt=Integer.toString(i+1);
				System.out.println("Rutime att is"+RuntimeAtt);
				RuntimeTable_locator=RuntimeTable_locator.replace("RUNTIMECnt",RuntimeAtt);
			}
			System.out.println("RuntimeTable_locator iss"+RuntimeTable_locator);

			if (iedriver.findElement(By.xpath(RuntimeTable_locator)).isDisplayed()){
				System.out.println("RuntimeTable_locator iss"+RuntimeTable_locator);
				String ActualStatus=iedriver.findElement(By.xpath(RuntimeTable_locator)).getText();
				System.out.println("Actual status is: "+ActualStatus);
				if(ActualStatus.equals("save")){

					xpath = "/order_history/History_Table/PerformAct";
					String RuntimeTable_PerformAct = uiPopulation.getNodeValByXpath(xpath);
					if(RuntimeTable_PerformAct.contains("RUNTIMECnt")){
						RuntimeTable_PerformAct=RuntimeTable_PerformAct.replace("RUNTIMECnt",RuntimeAtt);
						System.out.println("Run time locator is: "+RuntimeTable_PerformAct);
						iedriver.findElement(By.xpath(RuntimeTable_PerformAct)).click();
						System.out.println("Button clicked");
						break;
					}

					break;
				}	 

			}
		}
		return true;


	}

	public boolean selectSupplement() throws InterruptedException{
		boolean flag=false;
		List<WebElement> tableRows;
		logger.info("Clicking view button..");

		iedriver.findElement(By.xpath(".//img[@src='/gateway/images/ViewButton.gif']")).click();
		Thread.sleep(2000);
		tableRows=iedriver.findElements(By.xpath("//table[@class='List']/tbody"));
		for(int i=0;i<tableRows.size();i++){
			//iedriver.switchTodefault();
			iedriver.refresh();
			WebElement temp=tableRows.get(i).findElement(By.xpath("tr["+i+1+"]"));
			if(temp.getText().contains("submit")){
				WebElement button=temp.findElement(By.xpath(".//img[@alt='Perform Action']")); 
				button.click();
				flag=true;
			}
		}
		return flag;
	}



	/**
	 * this method selects the response type by drop down for LSR-RECEIVE_PREORDER and ASR_RECEIVE_PREORDER from Order History
	 * page
	 * 
	 * @return
	 */
	//	 public boolean selectResponseReceivePreorder(String response, String reqType) {
	//			List<WebElement> tableRows;
	//			boolean flag = false;
	//			try {
	//				if((reqType.equals("csr"))||(reqType.equals("address_validation")))
	//				{
	//					
	//					iedriver.switchTodefault();
	//					tableRows = iedriver
	//							.findElements(By
	//									.xpath("//table[@class = 'Main']/tbody/tr[5]/td/table/tbody/tr"));
	//					for (int i = 0; i < tableRows.size(); i++) 
	//					{
	//						logger.info("Checking row" + tableRows.get(i).getText());
	//						
	//							WebElement select = tableRows.get(i).findElement(
	//									By.xpath("td[2]"));
	//							logger.info(" response type to be selected is: "+response);
	//							new Select(select.findElement(By.name("ActionDropDown")))
	//									.selectByVisibleText(response);
	//							logger.info("selected response type is: "+response);						
	//							Thread.currentThread().sleep(2000);
	//							WebElement buttonLoc = select.findElement(By
	//									.xpath(".//a/img[@alt='Perform Action']"));
	//							logger.info("+++++++++++>" + buttonLoc.getAttribute("alt"));
	//							buttonLoc.click();
	//							break;
	//						
	//					}
	//				}
	//				else{
	//					iedriver.switchTodefault();
	//					
	//					Thread.currentThread().sleep(2000);
	//					WebElement buttonLoc = iedriver.findElement(By
	//							.xpath(".//a/img[@alt='Perform Action']"));
	//					logger.info("+++++++++++>" + buttonLoc.getAttribute("alt"));
	//					buttonLoc.click();
	//					//break;
	//					}
	//				
	//				flag = true;
	//				return flag;
	//				
	//			} catch (Exception exception) {
	//				exception.printStackTrace();
	//				return flag;
	//			}
	//		}
	//		




	public boolean VerifyLSRSendPreOrderGUIResponses(String ExpectedResponses) throws XPathExpressionException, IOException, ParserConfigurationException, SAXException, InterruptedException {

		//FieldUtils fu = new FieldUtils(); 
		System.out.println("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
		String xpath ="/order_history/History_Table/ViewPON";
		String ViewPON = uiPopulation.getNodeValByXpath(xpath);
		iedriver.findElement(cn.getLocator(ViewPON)).click(); 
		Thread.sleep(5000);
		//Change here
		String title = iedriver.getTitle();
		System.out.println(title);
		if (title.contains("Order History")){
			System.out.println("View Button Clicked");	
		}else{
			iedriver.findElement(cn.getLocator(ViewPON)).click();
		}
		Thread.sleep(4000);
		System.out.println("ExpectedResponses are"+ExpectedResponses);		

		Properties LSRPOResponseMappings=ReadProperties.loadProperties("repository/productproperties/lsr_responses_mappings.properties");
		Properties LSRPOGUIResponseMappings=ReadProperties.loadProperties("repository/productproperties/lsr_DBGUI_responses_mappings.properties");
		String currresp=ExpectedResponses;
		System.out.println("currresp to verify"+currresp);

		List<String> explist = new ArrayList<String>();
		String RespProp=LSRPOResponseMappings.getProperty(currresp);

		for (String token : RespProp.split(",")) {
			for (String Resp : token.split(":")) {
				RespProp=RespProp.replaceAll(Resp, LSRPOGUIResponseMappings.getProperty(Resp));		
			}

		}


		for (String GUIResp : RespProp.split(",")) {		
			explist.add(GUIResp);	
		}
		System.out.println("explist val"+explist);
		Thread.sleep(15000);
		xpath = "/order_history/History_Table/ResultTable";
		String HistoryTable_locator = uiPopulation.getNodeValByXpath(xpath);
		logger.info("Order_type Locator::" + HistoryTable_locator);

		int Histtabsize=iedriver.findElements(cn.getLocator(HistoryTable_locator)).size();

		List<String> list = new ArrayList<String>();
		for(int i = 0;i <= Histtabsize;i++)
		{
			xpath = "/order_history/History_Table/DynamicTableAtt";
			String RuntimeTable_locator_status = uiPopulation.getNodeValByXpath(xpath);
			if(RuntimeTable_locator_status.contains("RUNTIMECnt")){
				String RuntimeAtt=Integer.toString(i);			
				RuntimeTable_locator_status=RuntimeTable_locator_status.replace("RUNTIMECnt",RuntimeAtt);
			}  			

			xpath = "/order_history/History_Table/E911ActDynamicTableAtt";
			String Act_RuntimeTable_locator = uiPopulation.getNodeValByXpath(xpath); 		
			if(Act_RuntimeTable_locator.contains("RUNTIMECnt")){
				String actRuntimeAtt=Integer.toString(i); 			
				Act_RuntimeTable_locator=Act_RuntimeTable_locator.replace("RUNTIMECnt",actRuntimeAtt);
			}



			if (iedriver.findElement(By.xpath(RuntimeTable_locator_status)).isDisplayed()){        	

				String ActualStatus=iedriver.findElement(By.xpath(RuntimeTable_locator_status)).getText().trim();
				String ActualAct=iedriver.findElement(By.xpath(Act_RuntimeTable_locator)).getText().trim();
				String FinActStatus=ActualStatus+":"+ActualAct;   	   
				list.add(FinActStatus);  	

			}
		}

		System.out.println("actual list size is "+list.size());
		System.out.println("actual list is "+list);
		System.out.println("expected list size is "+explist.size());
		System.out.println("expected list is "+explist);




		if( explist.containsAll(list)&& (explist.size()==list.size()) ){
			System.out.println("Responses verified.Result is Success");
			//return true;
		}
		else{
			System.out.println("Responses verified.Result is failure");
			return false;
		}

		//	}
		return true;


	}




	public boolean VerifyASRRecievePreOrderGUIResponses(String ExpectedResponses) throws XPathExpressionException, IOException, ParserConfigurationException, SAXException, InterruptedException {
		Thread.sleep(8000);
		// FieldUtils fu = new FieldUtils();

		String xpath ="/order_history/History_Table/ViewPON";
		System.out.println("ExpectedResponses are"+ExpectedResponses);			

		Properties ASRRecPOGUIResponseMappings=ReadProperties.loadProperties("repository/productproperties/asr_preorder_receive_mappings.properties");		 


		String currresp=ExpectedResponses;
		System.out.println("currresp to verify"+currresp);

		List<String> explist = new ArrayList<String>();
		String RespProp=ASRRecPOGUIResponseMappings.getProperty(currresp);




		for (String GUIResp : RespProp.split(",")) {		
			explist.add(GUIResp);	
		}
		System.out.println("explist val"+explist);

		xpath = "/order_history/History_Table/ResultTable";
		String HistoryTable_locator = uiPopulation.getNodeValByXpath(xpath);
		logger.info("Order_type Locator::" + HistoryTable_locator);

		int Histtabsize=iedriver.findElements(cn.getLocator(HistoryTable_locator)).size();

		List<String> list = new ArrayList<String>();
		for(int i = 0;i <= Histtabsize;i++)
		{
			xpath = "/order_history/History_Table/DynamicTableAtt";
			String RuntimeTable_locator_status = uiPopulation.getNodeValByXpath(xpath);
			if(RuntimeTable_locator_status.contains("RUNTIMECnt")){
				String RuntimeAtt=Integer.toString(i);			
				RuntimeTable_locator_status=RuntimeTable_locator_status.replace("RUNTIMECnt",RuntimeAtt);
			}  			

			xpath = "/order_history/History_Table/E911ActDynamicTableAtt";
			String Act_RuntimeTable_locator = uiPopulation.getNodeValByXpath(xpath); 		
			if(Act_RuntimeTable_locator.contains("RUNTIMECnt")){
				String actRuntimeAtt=Integer.toString(i); 			
				Act_RuntimeTable_locator=Act_RuntimeTable_locator.replace("RUNTIMECnt",actRuntimeAtt);
			}



			if (iedriver.findElement(By.xpath(RuntimeTable_locator_status)).isDisplayed()){        	

				String ActualStatus=iedriver.findElement(By.xpath(RuntimeTable_locator_status)).getText().trim();
				String ActualAct=iedriver.findElement(By.xpath(Act_RuntimeTable_locator)).getText().trim();
				String FinActStatus=ActualStatus+":"+ActualAct;   	   
				list.add(FinActStatus);  	

			}
		}

		System.out.println("actual list size is "+list.size());
		System.out.println("actual list is "+list);
		System.out.println("expected list size is "+explist.size());
		System.out.println("expected list is "+explist);




		if( explist.containsAll(list)&& (explist.size()==list.size()) ){
			System.out.println("Responses verified.Result is Success");
			//return true;
		}
		else{
			System.out.println("Responses verified.Result is failure");
			return false;
		}

		//	}
		return true;


	}


	public boolean VerifyLSRRecievePreOrderGUIResponses(String ExpectedResponses) throws XPathExpressionException, IOException, ParserConfigurationException, SAXException, InterruptedException {

		//FieldUtils fu = new FieldUtils(); 		
		String xpath ="/order_history/History_Table/ViewPON";
		System.out.println("ExpectedResponses are"+ExpectedResponses);		

		Properties LSRPOResponseMappings=ReadProperties.loadProperties("repository/productproperties/LSR_RECEIVEDBMapping_PREORDER.properties");
		Properties LSRPOGUIResponseMappings=ReadProperties.loadProperties("repository/productproperties/lsr_PO_DBGUI_responses_mappings.properties");
		Properties LSRBasicGUIResponseMappings=ReadProperties.loadProperties("repository/productproperties/lsr_DBGUI_responses_mappings.properties");

		String currresp=ExpectedResponses;
		System.out.println("currresp to verify"+currresp);

		List<String> explist = new ArrayList<String>();
		String RespProp=LSRPOResponseMappings.getProperty(currresp);
		System.out.println("---------------------"+ RespProp);
		int f=0;
		for (String token : RespProp.split(",")) {
			for (String Resp : token.split(":")) {

				System.out.println("@@@@@@@value of"+ f + " is@@@@@@@@@" + Resp);
				if(ExpectedResponses.startsWith("Rec")){
					RespProp=RespProp.replaceAll("\\b"+Resp+"\\b", LSRPOGUIResponseMappings.getProperty(Resp));
					System.out.println("After split"+ RespProp);

				}
				else{
					RespProp=RespProp.replaceAll("\\b"+Resp+"\\b", LSRBasicGUIResponseMappings.getProperty(Resp));
					System.out.println("After split 2"+ RespProp);
				}
				f++;
			}

		}


		for (String GUIResp : RespProp.split(",")) {		
			explist.add(GUIResp);	
		}
		System.out.println("explist val"+explist);

		xpath = "/order_history/History_Table/ResultTable";
		String HistoryTable_locator = uiPopulation.getNodeValByXpath(xpath);
		logger.info("Order_type Locator::" + HistoryTable_locator);

		int Histtabsize=iedriver.findElements(cn.getLocator(HistoryTable_locator)).size();

		List<String> list = new ArrayList<String>();
		for(int i = 0;i <= Histtabsize;i++)
		{
			xpath = "/order_history/History_Table/DynamicTableAtt";
			String RuntimeTable_locator_status = uiPopulation.getNodeValByXpath(xpath);
			if(RuntimeTable_locator_status.contains("RUNTIMECnt")){
				String RuntimeAtt=Integer.toString(i);			
				RuntimeTable_locator_status=RuntimeTable_locator_status.replace("RUNTIMECnt",RuntimeAtt);
			}  			

			xpath = "/order_history/History_Table/E911ActDynamicTableAtt";
			String Act_RuntimeTable_locator = uiPopulation.getNodeValByXpath(xpath); 		
			if(Act_RuntimeTable_locator.contains("RUNTIMECnt")){
				String actRuntimeAtt=Integer.toString(i); 			
				Act_RuntimeTable_locator=Act_RuntimeTable_locator.replace("RUNTIMECnt",actRuntimeAtt);
			}



			if (iedriver.findElement(By.xpath(RuntimeTable_locator_status)).isDisplayed()){        	

				String ActualStatus=iedriver.findElement(By.xpath(RuntimeTable_locator_status)).getText().trim();
				String ActualAct=iedriver.findElement(By.xpath(Act_RuntimeTable_locator)).getText().trim();
				String FinActStatus=ActualStatus+":"+ActualAct;   	   
				list.add(FinActStatus);  	

			}
		}

		System.out.println("actual list size is "+list.size());
		System.out.println("actual list is "+list);
		System.out.println("expected list size is "+explist.size());
		System.out.println("expected list is "+explist);




		if( explist.containsAll(list)&& (explist.size()==list.size()) ){
			System.out.println("Responses verified.Result is Success");

		}
		else{
			System.out.println("Responses verified.Result is failure");
			return false;
		}

		//	}
		return true;


	}


	public boolean clickViewBut() {
		String  xpath;
		String  ViewPON;
		System.out.println("To click on view ");

		xpath ="/order_history/History_Table/ViewPON";
		try {
			ViewPON = uiPopulation.getNodeValByXpath(xpath);
			iedriver.findElement(cn.getLocator(ViewPON)).click();
			return true;

		} catch (Exception e) {

			e.printStackTrace();
			return false;
		}



	}


	/**
	 * this method selects the response type by drop down for
	 * LSR-RECEIVE_PREORDER and ASR_RECEIVE_PREORDER from Order History page
	 * 
	 * @return
	 */
	public boolean selectResponseReceivePreorder(String response, String reqType) {
		List<WebElement> tableRows;
		boolean flag = false;

		try {
			logger.info("inside fuction  selectResponseReceivePreorder");
			logger.info("reqType is : " + reqType);
			logger.info("response is : " + response);
			if ((reqType.equals("csr"))
					|| (reqType.equals("address_validation"))) {
				iedriver.switchTodefault();
				tableRows = iedriver
						.findElements(By
								.xpath("//table[@class = 'Main']/tbody/tr[5]/td/table/tbody/tr"));
				logger.info("++++++TABLE ROWS FOUND +++++>: " + tableRows);
				for (int i = 0; i < tableRows.size(); i++) {
					logger.info("Checking row : " + "i : " + i + tableRows.get(i).getText());
					if (tableRows.get(i).getText().contains("receive")) {
						WebElement select = tableRows.get(i).findElement(
								By.xpath("form/tr/td[2]"));
						logger.info("+++++++++++>field going : " + select);
						new Select(
								select.findElement(By.name("ActionDropDown")))
						.selectByVisibleText(response);
						Thread.sleep(2000);
						WebElement buttonLoc = select.findElement(By
								.xpath(".//a/img[@alt='Perform Action']"));
						logger.info("+++++++++++>"
								+ buttonLoc.getAttribute("alt"));
						buttonLoc.click();
						break;

						// }
					}
				}
			}

			else {
				iedriver.switchTodefault();

				Thread.sleep(2000);
				WebElement buttonLoc = iedriver.findElement(By
						.xpath(".//a/img[@alt='Perform Action']"));
				logger.info("+++++++++++>" + buttonLoc.getAttribute("alt"));
				buttonLoc.click();
				// break;
			}

			flag = true;
			return flag;

		} catch (Exception exception) {
			exception.printStackTrace();
			return false;
		}
	}
	
	// Select Response for CSRNB LSR Send PreOrder
	
	public boolean SelectCSRNBResponse(String response, String reqType) {
		List<WebElement> tableRows;
		boolean flag = false;
		
		try {
			logger.info("inside fuction SelectCSRNBResponse");
			logger.info("reqType is : " + reqType);
			logger.info("response is : " + response);
			iedriver.switchTodefault();
			WebElement elem = iedriver.findElement(By.xpath("//td[contains(text(),'Submit')]/parent::tr/td[2]/select"));
			Select sel1=new Select(elem);
			for (WebElement webElement : sel1.getOptions())
			{
				System.out.println("**element is present  "+webElement.getText());
			}
			sel1.selectByVisibleText(response);
			Thread.sleep(2000);
			WebElement buttonLoc = iedriver.findElement(By
					.xpath("//td[contains(text(),'Submit')]/parent::tr/td[2]/a"));
			logger.info("+++++++++++>" + buttonLoc.getAttribute("alt"));
			buttonLoc.click();
			flag = true;
			return flag;

		} catch (Exception exception) {
			exception.printStackTrace();
			return false;
		}
	}


	/**
	 * this method selects the response type by drop down for
	 * LSR-RECEIVE_PREORDER and ASR_RECEIVE_PREORDER from Order History page
	 * 
	 * @return
	 */
	public boolean selectMultiResponseReceivePreorder(String response, String reqType, int responseCounter) {
		List<WebElement> tableRows;
		boolean flag = false;

		try {
			logger.info("inside fuction  selectResponseReceivePreorder");
			logger.info("reqType is : " + reqType);
			logger.info("response is : " + response);
			if ((reqType.equals("csr"))
					|| (reqType.equals("address_validation"))) {
				iedriver.switchTodefault();
				tableRows = iedriver
						.findElements(By
								.xpath("//table[@class = 'Main']/tbody/tr[5]/td/table/tbody/form[" + responseCounter*2 + "]"));
				logger.info("++++++TABLE ROWS FOUND +++++>: " + tableRows);

				for (int i = 0; i < tableRows.size(); i++) {
					logger.info("Checking row : " + "i : " + i + tableRows.get(i).getText());
					if (tableRows.get(i).getText().contains("receive")) {


						WebElement select = tableRows.get(i).findElement(
								By.xpath("tr/td[2]"));
						logger.info("+++++++++++>field going : " + select);
						new Select(
								select.findElement(By.name("ActionDropDown")))
						.selectByVisibleText(response);
						Thread.sleep(2000);
						WebElement buttonLoc = select.findElement(By
								.xpath(".//a/img[@alt='Perform Action']"));
						logger.info("+++++++++++>"
								+ buttonLoc.getAttribute("alt"));
						buttonLoc.click();
						break;

						// }
					}

				}
			}


			else {
				iedriver.switchTodefault();

				Thread.sleep(2000);
				WebElement buttonLoc = iedriver.findElement(By
						.xpath(".//a/img[@alt='Perform Action']"));
				logger.info("+++++++++++>" + buttonLoc.getAttribute("alt"));
				buttonLoc.click();
				// break;
			}

			flag = true;
			return flag;

		} catch (Exception exception) {
			exception.printStackTrace();
			return false;
		}
	}




	//******************


	public boolean selectResponseReceiveOrder(String response) {
		List<WebElement> tableRows;
		boolean flag = false;

		try {
			logger.info("inside fuction  selectResponseReceivePreorder");

			logger.info("response is : " + response);

			iedriver.switchTodefault();
			tableRows = iedriver
					.findElements(By
							.xpath("//table[@class = 'Main']/tbody/tr[5]/td/table/tbody/tr"));
			logger.info("++++++TABLE ROWS FOUND +++++>: " + tableRows);
			for (int i = 0; i < tableRows.size(); i++) {
				logger.info("Checking row : " + "i : " + i + tableRows.get(i).getText());
				if (tableRows.get(i).getText().contains("receive")) {
					WebElement select = tableRows.get(i).findElement(
							By.xpath("form/tr/td[2]"));
					logger.info("+++++++++++>field going : " + select);
					new Select(
							select.findElement(By.name("ActionDropDown")))
					.selectByValue(response);
					Thread.sleep(2000);
					WebElement buttonLoc = select.findElement(By
							.xpath(".//a/img[@alt='Perform Action']"));
					logger.info("+++++++++++>"
							+ buttonLoc.getAttribute("alt"));
					buttonLoc.click();
					break;


				}
			}

			flag = true;
			return flag;

		} catch (Exception exception) {
			exception.printStackTrace();
			return false;
		}
	}

	/**
	 * this method selects the response type by drop down for
	 * LSR-RECEIVE_PREORDER for multiple responses from Order History page
	 * 
	 * @return
	 */
	public boolean selectMultiResponseReceiveOrder(String response, int responseCounter) {
		List<WebElement> tableRows;
		boolean flag = false;

		try {
			logger.info("inside fuction  selectResponseReceiveOrder");
			logger.info("response is : " + response);

			iedriver.switchTodefault();
			tableRows = iedriver
					.findElements(By
							.xpath("//table[@class = 'Main']/tbody/tr[5]/td/table/tbody/form[" + responseCounter*2 + "]"));
			logger.info("++++++TABLE ROWS FOUND +++++>: " + tableRows);

			for (int i = 0; i < tableRows.size(); i++) {
				logger.info("Checking row : " + "i : " + i + tableRows.get(i).getText());
				if (tableRows.get(i).getText().contains("receive")) {


					WebElement select = tableRows.get(i).findElement(
							By.xpath("tr/td[2]"));
					logger.info("+++++++++++>field going : " + select);
					new Select(
							select.findElement(By.name("ActionDropDown")))
					.selectByValue(response);
					Thread.sleep(2000);
					WebElement buttonLoc = select.findElement(By
							.xpath(".//a/img[@alt='Perform Action']"));
					logger.info("+++++++++++>"
							+ buttonLoc.getAttribute("alt"));
					buttonLoc.click();
					break;


				}
			}

			flag = true;
			return flag;

		} catch (Exception exception) {
			exception.printStackTrace();
			return false;
		}

	}

	/**
	 * This method used to edit the request for Supplements
	 * 
	 * @return
	 */

	public boolean editRequest() {
		List<WebElement> tableRows;
		boolean flag = false;
		try {
			iedriver.switchTodefault();
			tableRows = iedriver
					.findElements(By
							.xpath("//table[@class = 'Main']/tbody/tr[5]/td/table/tbody/tr"));
			tableRows.remove(0);
			for (int i = 0; i < tableRows.size(); i++) {
				logger.info("Checking row" + tableRows.get(i).getText());
				if (tableRows.get(i).getText().contains("submit")) {
					WebElement select = tableRows.get(i).findElement(
							By.xpath("td[2]"));
					new Select(select.findElement(By.name("ActionDropDown")))
					.selectByVisibleText("Edit");
					WebElement buttonLoc = select.findElement(By
							.xpath(".//a/img[@alt='Perform Action']"));
					logger.info("+++++++++++>" + buttonLoc.getAttribute("alt"));
					buttonLoc.click();
					break;
				}

			}
			flag = true;
		} catch (Exception ex) {
			logger.error("Error while editing the request");
			ex.printStackTrace();
		}
		return flag;
	}

}
