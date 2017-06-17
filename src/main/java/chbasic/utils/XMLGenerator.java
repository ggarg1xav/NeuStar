package chbasic.utils;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 * @author abhishekr.gupta
 * 
 */
public class XMLGenerator {

	FileInputStream fileInputStream;
//	Workbook myWorkBook;
	ReadProperties rp = new ReadProperties();

/*	public XMLGenerator(String fileName) {
		try {
			fileInputStream = new FileInputStream(fileName);
			myWorkBook = WorkbookFactory.create(fileInputStream);

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void addColumnValue(Sheet mySheet,
			ArrayList<String> locatorarray, int cells) {
		Row row = mySheet.getRow(0);
		locatorarray.clear();
		for (int c = 0; c < cells; c++) {

			Cell cell = row.getCell((short) c);
			if (cell != null) {
				locatorarray.add(cell.getStringCellValue());
			}
		}
	}*/

	/**
	 * function that generates XML file of each excel file row
	 * 
	 * @param tcContentArray
	 * @param sheetName
	 * @param tcName
	 * @param outPath
	 */
	public void generateXML(String[] tcContentArray, String sheetName,
			String tcName, String outPath) {
		try {
			String[] tcContetntVal = tcContentArray;
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();

			Element rootElement = doc.createElement("search_order");

			Element orderType = doc.createElement(sheetName.toLowerCase());
			rootElement.appendChild(orderType);

			for (int i = 0; i < tcContetntVal.length; i++) {
				System.out.println("value" + tcContetntVal[i]);
				String[] splitByPipe = tcContetntVal[i].split("=");
				Element ele = doc.createElement(splitByPipe[0]);
				Attr attr = doc.createAttribute("value");
				attr.setValue(splitByPipe[1]);
				ele.setAttributeNode(attr);
				orderType.appendChild(ele);
			}

			doc.appendChild(rootElement);

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);

			StreamResult result = new StreamResult(new File(outPath + "\\"
					+ tcName + ".xml"));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

			System.out.println("File saved!");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
