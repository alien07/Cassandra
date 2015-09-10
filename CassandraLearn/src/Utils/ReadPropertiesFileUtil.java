package Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

public class ReadPropertiesFileUtil {

	public static void main(String[] args) {
		readDbConfig();
		System.exit(0);
	}

	private ReadPropertiesFileUtil() {

	}

	public static Properties readDbConfig() {
		Properties properties = new Properties();
		try {
			File file = new File("db_config.properties");// //resources//configs//db_config.properties
			FileInputStream fileInput = new FileInputStream(file);
			properties.load(fileInput);
			fileInput.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}

	private static void printProperties(Properties properties) {
		Enumeration enuKeys = properties.keys();
		while (enuKeys.hasMoreElements()) {
			String key = (String) enuKeys.nextElement();
			String value = properties.getProperty(key);
			System.out.println(key + ": " + value);
		}
	}

}
