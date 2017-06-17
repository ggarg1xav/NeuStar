package chbasic.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;

/**
 * 
 * @author abhishekr.gupta
 * 
 */
public class FieldUtils {
	private static Logger LOGGER = Logger.getLogger(FieldUtils.class);
	public static String[] formats = { "dd/MM/yyyy", "MM/dd/yyyy hh:mm:ss", "E MMM dd HH:mm:ss zzz yyyy" };

	/**
	 * This method used to generate PON by taking customerName as param
	 * 
	 * @param customerName
	 * @return
	 */

	public synchronized String generatePON(String customerName) {
		String pon = "";
		Random randomGenerator = new Random();
		int ponInt1 = randomGenerator.nextInt(99999);
		int ponInt2 = randomGenerator.nextInt(99999);

		pon = "CHPON" + new DecimalFormat("00000000000").format(Long.parseLong("" + ponInt1 + ponInt2));

		System.out.println("Generated PON: " + pon);
		return pon;
	}

	/***
	 * This method used to generate a random username for adding a user
	 * 
	 * @return
	 */
	public String getUsername() {
		System.out.println("in getusername()");
		String username = "";
		String str1 = "CH";
		// Random string only with numbers
		String str2 = generateRandomString(4);

		Random randomGenerator = new Random();
		int usnmInt = randomGenerator.nextInt(99999);

		// Random alphabetic string
		// String str3=(RandomStringUtils.randomAlphabetic(3));

		username = str1 + str2 + usnmInt;
		System.out.println("fu username-->" + username);
		return username;
	}

	/**
	 * This method used to generate a random string of given size
	 * 
	 * @param size
	 * @return
	 */
	public String generateRandomString(int size) {
		// String rand_str = "";
		int leftLimit = 97; // letter 'a'
		int rightLimit = 122; // letter 'z'

		StringBuilder buffer = new StringBuilder(size);
		for (int i = 0; i < size; i++) {
			int randomLimitedInt = leftLimit + (int) (new Random().nextFloat() * (rightLimit - leftLimit));
			buffer.append((char) randomLimitedInt);
		}
		String generatedString = buffer.toString();

		// System.out.println(generatedString);

		return generatedString;
	}

	/**
	 * This function used to get current date time in EST
	 * 
	 * @return
	 */
	public String getCurrentDateTimeEST() {
		String currDate = "";
		Date date = new Date();

		DateFormat df = new SimpleDateFormat("dd-MMM-yy' 'hh.mm.ss.SSSSSSSSS a");

		// Tell the DateFormat that you want the time in this time zone
		df.setTimeZone(TimeZone.getTimeZone("America/Montreal"));

		currDate = df.format(date);
		System.out.println("retrning curr date--->" + currDate.toUpperCase());
		return currDate.toUpperCase();
	}

	/**
	 * This method used to get "VER" from previous ver
	 * 
	 * @param currVER
	 * @return
	 */
	public String getVER(String currVER) {
		if (currVER.equals("start")) {
			return "00";
		}
		int ver = (Integer.parseInt(currVER) + 1);
		return "0" + String.valueOf(ver);
	}

	// public String getVER(String currVER) {
	// int ver = (Integer.parseInt(currVER));
	// return "0" + String.valueOf(ver);
	// }
	//
	/**
	 * This function returns Current date
	 * 
	 * @return
	 */
	public String getCURR_DT() {
		String curr_dt;
		Date currentTime;
		SimpleDateFormat dateformat = new SimpleDateFormat("MM-dd-yyyy'-'hhmma");
		dateformat.setTimeZone(TimeZone.getTimeZone("UTC"));
		Calendar cal = Calendar.getInstance();
		currentTime = cal.getTime();
		curr_dt = dateformat.format(currentTime);
		return curr_dt;
	}

	/**
	 * This function returns Current date for UOM request
	 * 
	 * @return
	 */
	public String getCURR_DTuom() {
		String curr_dt;
		Date currentTime;
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd'-'hhmma");
		dateformat.setTimeZone(TimeZone.getTimeZone("UTC"));
		Calendar cal = Calendar.getInstance();
		currentTime = cal.getTime();
		curr_dt = dateformat.format(currentTime);
		return curr_dt;
	}

	/**
	 * This fuction returns YYYYMMDD for asr_response
	 */
	public String getResponseDateString() {
		String curr_date;
		Date currentTime;
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
		dateformat.setTimeZone(TimeZone.getTimeZone("UTC"));
		Calendar cal = Calendar.getInstance();
		currentTime = cal.getTime();
		curr_date = dateformat.format(currentTime);
		return curr_date.replaceAll("-", "");
	}

	/**
	 * a method to return timestamp object from string date in the formats
	 * {"dd/mm/yyyy HH:mm:ss","E MMM dd HH:mm:ss zzz yyyy","dd/mm/yyyy"}
	 * 
	 * @param date:
	 *            the date to format in string representation
	 * @return timestamp object
	 */
	public Timestamp timestampFormat(String date) {
		Date dateObj = new Date();
		for (String format : formats) {
			try {
				dateObj = new SimpleDateFormat(format).parse(date);
				System.out.println(dateObj.toString()); // for tracking

			} catch (Exception e) {

			}
		}
		return new Timestamp(dateObj.getTime());

	}

	/**
	 * This function returns Due Date adding up the interval of no of days
	 * 
	 * @param dueDays
	 * @return
	 */
	public String generateDUE_DT(int dueDays) {
		String due_dt;
		DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, dueDays);
		due_dt = dateFormat.format(c.getTime());
		System.out.println(due_dt);
		return due_dt;
	}

	/**
	 * This function returns Due Date supported by UOM adding up the interval of
	 * no of days
	 * 
	 * @param dueDays
	 * @return
	 */
	public String generateDUE_DTUOM(int dueDays) {
		String due_dt;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, dueDays);
		due_dt = dateFormat.format(c.getTime());
		System.out.println(due_dt);
		return due_dt.replaceAll("-", "");
	}

	/**
	 * This method used to get portedNBR
	 * 
	 * @param NPANXX
	 * @return
	 */
	public String generatePOTEDNBR(String NPANXX) {
		String portednbr;
		int randomIntNP;
		Random randomGenerator = new Random();
		randomIntNP = randomGenerator.nextInt(9999);
		portednbr = NPANXX + new DecimalFormat("0000").format(Long.parseLong("" + randomIntNP));
		portednbr = getDashedTN(portednbr);
		System.out.println("portednbr=" + portednbr);
		return portednbr;
	}

	/**
	 * This method used to get dashed TN
	 * 
	 * @param tn
	 * @return
	 */
	public String getDashedTN(String tn) {

		StringBuilder sb = new StringBuilder();
		sb.append(tn.substring(0, 3)).append("-").append(tn.substring(3, 6)).append("-").append(tn.substring(6, 10));

		return sb.toString();

	}

	/**
	 * This method used to get the msg_timestamp for UOM
	 * 
	 * @return
	 */
	public String getMsgTimestamp() {
		String msgTimeStamp = "";
		Date currentTime;
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
		dateformat.setTimeZone(TimeZone.getTimeZone("America/Montreal"));
		Calendar cal = Calendar.getInstance();
		currentTime = cal.getTime();
		msgTimeStamp = dateformat.format(currentTime);
		return msgTimeStamp;
	}

	/**
	 * This method used to take file path and return its content to String
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public String getStringFromFile(String filePath) throws IOException {
		String fileString = "";
		try {
			File file = new File(filePath);
			fileString = FileUtils.readFileToString(file);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return fileString;
	}

	/**
	 * This method used to convert String to DOM Object
	 * 
	 * @param clobData
	 * @return
	 */
	public Document stringToDom(String clobData) {
		Document doc = null;
		try {
			@SuppressWarnings("deprecation")
			InputStream ios = new StringBufferInputStream(clobData);
			DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
			dbfactory.setNamespaceAware(false);
			DocumentBuilder dBuilder = null;
			dBuilder = dbfactory.newDocumentBuilder();
			doc = dBuilder.parse(ios);
			return doc;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}

	/**
	 * This method used to post soap request to a web service
	 * 
	 * @param doc
	 * @param webServiceURL
	 * @return
	 */
	public SOAPMessage postSOAPRequest(String SOAP_REQUEST, String webServiceURL) {
		SOAPMessage response = null;
		try {
			SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
			SOAPConnection conn = scf.createConnection();
			MessageFactory msgf = MessageFactory.newInstance();
			SOAPMessage smsg = msgf.createMessage();
			SOAPPart sp = smsg.getSOAPPart();

			byte[] reqBytes = SOAP_REQUEST.getBytes();
			ByteArrayInputStream bis = new ByteArrayInputStream(reqBytes);
			StreamSource ss = new StreamSource(bis);

			sp.setContent(ss);
			smsg.writeTo(System.out);
			smsg.saveChanges();
			smsg.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "utf-16");

			smsg.getMimeHeaders().addHeader("SOAPAction", webServiceURL);
			java.net.URL endpoint = new URL(webServiceURL);

			response = conn.call(smsg, endpoint);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return response;
	}

	/**
	 * This method used to replace String by anther String
	 * 
	 * @param orig
	 * @param src
	 * @param dest
	 * @return
	 */
	public static final String replaceString(String orig, String src, String dest) {
		if (orig == null)
			return null;
		if (src == null || dest == null)
			throw new NullPointerException();
		if (src.length() == 0)
			return orig;

		StringBuffer res = new StringBuffer(orig.length() + 20); // Pure
																	// guesswork
		int start = 0;
		int end = 0;
		int last = 0;

		while ((start = orig.indexOf(src, end)) != -1) {
			res.append(orig.substring(last, start));
			res.append(dest);
			end = start + src.length();
			last = start + src.length();
		}

		res.append(orig.substring(end));

		return res.toString();
	}

	/**
	 * This method used to print soap message as String
	 * 
	 * @param soapMessage
	 * @return
	 */
	public static String getSOAPMessageAsString(SOAPMessage soapMessage) {
		try {

			TransformerFactory tff = TransformerFactory.newInstance();
			Transformer tf = tff.newTransformer();

			// Set formatting

			tf.setOutputProperty(OutputKeys.INDENT, "yes");
			tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

			Source sc = soapMessage.getSOAPPart().getContent();

			ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
			StreamResult result = new StreamResult(streamOut);
			tf.transform(sc, result);

			String strMessage = streamOut.toString();
			return strMessage;
		} catch (Exception e) {
			System.out.println("Exception in getSOAPMessageAsString " + e.getMessage());
			return null;
		}
	}

	public String generateOrdNum() {
		Random rnd = new Random();
		int num = 100000 + rnd.nextInt(900000);
		String OrdNum = "OR" + Integer.toString(num);
		System.out.println("Generated ORDNO: " + OrdNum);
		return OrdNum;
	}

}
