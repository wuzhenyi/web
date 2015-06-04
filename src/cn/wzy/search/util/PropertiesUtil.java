package cn.wzy.search.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
	
	private static Properties properties = new Properties();
	
	static {
		try {
			InputStream is = PropertiesUtil.class.getResourceAsStream("/system.properties");
			if (is != null) {
				properties.load(is);
				is.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String get(String key) {
		Object val = properties.get(key);
		if (val == null)
			throw new RuntimeException("Properties [" + key + "] does not exist !");
		return val.toString();
	}
}
