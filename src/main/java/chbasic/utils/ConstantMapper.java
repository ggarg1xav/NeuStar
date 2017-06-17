package chbasic.utils;

import java.util.Properties;

/**
 * 
 * @author abhishekr.gupta
 *
 */

/**
 * 
 * This class used to store the relative path of object repository's in string
 * constants so that it can be used throughout the project.
 * 
 */
public class ConstantMapper {

	public static final String LOGIN_OR_PATH = "repository/objectrepository/LOGIN.xml";
	public static final String HOME_OR_PATH = "repository/objectrepository/NAVIGATION_BAR.xml";
	public static final String LSR_ORDER_OR_PATH = "repository/objectrepository/LSR_ORDER.xml";
	public static final String CREATE_ORDER_OR_PATH = "repository/objectrepository/CREATE_ORDER.xml";
	public static final String SUBMIT_ORDER_OR_PATH = "repository/objectrepository/ACTIONS.xml";
	public static final String ORDER_HISTORY_OR_PATH = "repository/objectrepository/ORDER_HISTORY.xml";
	public static final String NAV_SEARCH_TAB_OR_PATH = "repository/objectrepository/SEARCH_NAV_BAR.xml";
	public static final String SEARCH_ORDER_OR_PATH = "repository/objectrepository/SEARCH_ORDER.xml";
	public static final String EMERGENCY_ORDER_OR_PATH = "repository/objectrepository/E911.xml";
	public static final String ASR_ORDER_OR_PATH = "repository/objectrepository/ASR_SEND_ORDER.xml";
	public static final String LSR_RECEIVE_OR_PATH = "repository/objectrepository/OR_LSR_RECEIVE_REQUEST.xml";
	public static final String LSR_RECEIVE_SHORT_OR_PATH = "repository/objectrepository/LSR_RECEIVE_SHORT_FORM.xml";
	//public static final String LSR_PREORDER_CSR = "repository/objectrepository/LSR_PREORDER_CSR.xml";
	public static final String ASR_ETHERNETE_TEST_PATH = "repository/test_data/SESE_ALTEL.xml";
	public static final String ASR_ETHERNETS_TEST_PATH = "repository/test_data/SESS_GoldenRequest.xml";
	public static final String ASR_TRANS_TEST_PATH = "repository/test_data/msla.xml";
	public static final String ASR_MULTILEC_TEST_PATH = "repository/test_data/MULTILEC.xml";
	public static final String E911_OR_PATH = "repository/objectrepository/E911.xml";
	public static final String ASR_PREORDER_OR_PATH = "repository/objectrepository/ASR_SEND_PREORDER.xml";
	
	public static final String LSR_PREORDER_OR_PATH = "repository/objectrepository/LSR_PREORDER_REQUEST/LSR_SEND_PREORDER.xml";
  public static final String LSR_PREORDER_CSR_OR_PATH = "repository/objectrepository/LSR_PREORDER_REQUEST/LSR_PREORDER_CSR.xml";
  public static final String LSR_PREORDER_AV_OR_PATH = "repository/objectrepository/LSR_PREORDER_REQUEST/LSR_PREORDER_AV.xml";
  public static final String LSR_PREORDER_LQ_OR_PATH = "repository/objectrepository/LSR_PREORDER_REQUEST/LSR_PREORDER_LQ.xml";

	
	public static final String ASR_RECEIVE_PREORDER_OR_PATH = "repository/objectrepository/ASR_RECEIVE_PREORDER.xml";
	//need to check if used
	public static final String LSR_RECIEVE_PREORDER_OR_PATH = "repository/objectrepository/LSR_RECEIVE_PREORDER.xml";
	public static final String LSR_RECEIVE_PREORDER_CSR_OR_PATH = "repository/objectrepository/LSR_RECEIVE_PREORDER_CSR.xml";
	public static final String LSR_RECEIVE_PREORDER_AV_OR_PATH = "repository/objectrepository/LSR_RECEIVE_PREORDER_AV.xml";
	public static final String LSR_RECEIVE_PREORDER_LQ_OR_PATH = "repository/objectrepository/LSR_RECEIVE_PREORDER_LQ.xml";
	// till here need to check 
	public static final String LIDB_OR_PATH = "repository/objectrepository/LIDB.xml";
	
	public static final String LSR_PREORDER_CSR = "repository/objectrepository/LSR_PREORDER_REQUEST/LSR_PREORDER_CSR.xml";
	public static final String ASR_RECEIVE_RESPONSE_OR_PATH = "repository/objectrepository/ASR_RECEIVE_ORDER-OR.xml";

	public static final String ASR_RECEIVE_PREORDER_MAP = "repository/maps/asr-receive-preorder.xsl";
	public static final String ASR_SEND_PREORDER_MAP = "repository/maps/asr-send-preorder.xsl";
	public static final String ASR_SEND_ORDER_MAP = "repository/maps/asr-send-order.xsl";
	public static final String E911_ORDER_MAP = "repository/maps/E911order.xsl";
	public static final String LIDB_ORDER_MAP = "repository/maps/LIDBorder.xsl";
	public static final String ASR_SEND_ORDER_SUPP_MAP = "repository/maps/asr_send_order_supp.xsl";
	public static final String LSR_SEND_ORDER_MAP = "repository/maps/lsr-send-order.xsl";
	public static final String LSR_SEND_ORDER_SUP_MAP="repository/maps/lsr-send-sup-order.xsl";
	public static final String LSR_RECEIVE_ORDER_MAP = "repository/maps/lsr_recieve.xsl";
	public static final String LSR_RECEIVE_RESPONSE_MAP = "repository/maps/lsr_receive_order_response.xsl";
	public static final String LSR_PREORDER_MAP = "repository/maps/lsr_preorder.xsl";
	public static final String ASR_RECEIVE_ORDER_MAP = "repository/maps/asr_receive_order.xsl";
	public static final String ASR_RECEIVE_ORDER_RESPONSE_MAP = "repository/maps/asr_receive_order_response.xsl";
	//public static final String ASR_RECEIVE_ORDER_SUP_MAP="repository/maps/asr_receive_order_sup.xsl";
	
	public static final String TEMPLATE_MAP_PATH = "repository/maps/template.xsl";
	public static final String LSR_SEND_PREORDER_MAP = "repository/maps/lsr-send-preorder.xsl";
	public static final String LSR_RECEIVE_PREORDER_MAP = "repository/maps/lsr-receive-preorder.xsl";
	public static String ASR_RECEIVE_RESPONSE_MAP = "repository/maps/asr-receive-response-preorder.xsl";

	public static final String LSR_ORDER_AB_ATT_SUP_PATH = "repository/sup_files/ATT_AB_REQ_BODY_SUP.xml";
	public static final String LSR_ORDER_AB_ATT_TEST_PATH = "repository/test_data/ATT_AB_REQ_BODY.xml";
	public static final String OUT_PATH_SEARCH_TEST_FILES = "repository/outXMLfiles";
	public static final String EMERGENCY_TEST_FILE = "repository/test_data/E911_Intrado_Change.xml";
	public static final String XLSX_PATH_SEARCH = "repository/excel_files/test.xlsx";
	public static final String SHEET_OF_EXCEL_SEARCH = "LSR_ORDER";
	public static final String ASR_RECEIVE_ORDER_SUP_MAP="repository/maps/asr_receive_order_sup.xsl";

	// loading database properties

	static Properties propPepDB = ReadProperties.loadProperties("conf/pep_db.properties");

	public static String DB_URL_PROPERTY = propPepDB.getProperty("db.url");
	public static String DB_USERNAME_PROPERTY = propPepDB.getProperty("db.username");
	public static String DB_PASSWORD_PROPERTY = propPepDB.getProperty("db.password");
	public static String DB_MIN_CONN_POOL_SIZE_PROPERTY = propPepDB.getProperty("db.min_pool_size");
	public static String DB_MAX_CONN_POOL_SIZE_PROPERTY = propPepDB.getProperty("db.max_pool_size");
	public static String DB_DRIVER_CLASS_PROPERTY = propPepDB.getProperty("db.driver");
	public static String DB_CONNECTION_TIMEOUT = propPepDB.getProperty("db.db.connection_timeout");

	// loading environment properties

	static Properties propEnv = ReadProperties.loadProperties("conf/environment.properties");
	public static String ASR_RECEIVE_SHORT_OR_PATH;

	public static final String EXECUTABLE_PATH = propEnv.getProperty("EXE_PATH");
	public static final String BRANDED_URL = propEnv.getProperty("BRANDED_GUI_URL");
	public static final String SOAP_URL=propEnv.getProperty("SOAP_REQUEST_HANDLER_URL");
	public static final String LT_BRANDED_URL=propEnv.getProperty("LIVE_TEST_BRANDED_URL");
	public static final String BASIC_URL = propEnv.getProperty("URL");
	public static final String LT_BASIC_URL=propEnv.getProperty("LIVE_TEST_GUI");
	public static final String DOMAIN = propEnv.getProperty("DOMAIN");
	public static final String USERNAME = propEnv.getProperty("USERNAME");
	public static final String PASSWORD = propEnv.getProperty("PASSWORD");
	public static final String STERLING_BRANDED_URL=propEnv.getProperty("STERLING_BRANDED_URL");
	public static final String STERLING_BASIC_URL=propEnv.getProperty("STERLING_BASIC_URL");
	
	public static final String DUE_DATE_INT = propEnv.getProperty("DUE_DATE_INTERVAL");
	public static final String CALLER_XML_PATH = propEnv.getProperty("repository/callerXML/AsrSendOrder.xml");

	public static final String HUBUrl = propEnv.getProperty("HUBUrl");
	public static final String RESPONSE_BASE_PATH = propEnv.getProperty("RESPONSE_BASE_PATH");
	public static final String BOX_IP = propEnv.getProperty("BOX_IP");
	public static final String BOX_2IP = propEnv.getProperty("BOX2_IP");
	public static final String BOX_USERNAME = propEnv.getProperty("BOX_USERNAME");
	public static final String BOX_PASSWORD = propEnv.getProperty("BOX_PASSWORD");
	
	public static final String E911_USERNAME = propEnv.getProperty("E911_USERNAME");
	public static final String E911_PASSWORD = propEnv.getProperty("E911_PASSWORD");
	
	
	public static final String Frontier_WSURL = propEnv.getProperty("Frontier_WSURL");

	public static final String FPC_WSURL = propEnv.getProperty("FPC_WSURL");
	public static final String ATT_WSURL = propEnv.getProperty("ATT_WSURL");
	public static final String COX_WSURL = propEnv.getProperty("COX_WSURL");
	public static final String VZW_WSURL = propEnv.getProperty("VZW_WSURL");
	public static final String ATT_PO_WSURL = propEnv.getProperty("ATT_PO_WSURL");
	public static final String CHARTER_WSURL = propEnv.getProperty("CHARTER_WSURL");
	public static final String LEVEL3_WSURL = propEnv.getProperty("LEVEL3_WSURL");
	public static final String CONSOLIDATEDCOMMUNICATIONS_WSURL = propEnv.getProperty("CONSOLIDATEDCOMMUNICATIONS_WSURL");
	public static final String CenturyLink_WSURL = propEnv.getProperty("CenturyLink_WSURL");
	public static final String XO_WSURL = propEnv.getProperty("XO_WSURL");
	public static final String CinBell_WSURL = propEnv.getProperty("CinBell_WSURL");
	public static final String COMCAST_WSURL = propEnv.getProperty("COMCAST_WSURL");
	

}
