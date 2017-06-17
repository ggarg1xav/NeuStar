package chbasic.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ReadProperties {
	Properties prop = new Properties();
	InputStream input = null;
	private static final Logger logger = Logger.getLogger(ReadProperties.class);

	public static Properties loadProperties(String propFilePath) {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(propFilePath));

		} catch (IOException e) {
			e.printStackTrace();
		}

		return prop;
	}

	public List<String> readAllProperties(String propFileName) {
		List<String> tcNameList = new ArrayList<String>();
		try {
			input = getClass().getClassLoader().getResourceAsStream(propFileName);
			if (input == null) {
				logger.error("Sorry, unable to find " + propFileName);

			}

			prop.load(input);
			Enumeration<?> e = prop.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				String value = prop.getProperty(key);
				if (value.equalsIgnoreCase("TRUE")) {
					tcNameList.add(key);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (tcNameList.isEmpty()) {
			try {
				throw new Exception();
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
		return tcNameList;

	}

}
