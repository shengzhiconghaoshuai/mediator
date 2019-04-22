package net.chinacloud.mediator.ych;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author 张宇 zhangyu@wuxicloud.com
 * @description
 * @since 2016年9月1日 下午3:03:49
 */
public class PropertiesUtil {

	private String configPath = null;
	private Properties props = null;
	
	public PropertiesUtil(String path) throws IOException {
		InputStream in = PropertiesUtil.class.getClassLoader()
				.getResourceAsStream(path);
		props = new Properties();
		props.load(in);
		in.close();
	}

	public String readValue(String key) throws IOException {
		return props.getProperty(key);
	}

	public Map<String, String> readAllProperties()
			throws FileNotFoundException, IOException {
		Map<String, String> map = new HashMap<String, String>();
		Enumeration<?> en = props.propertyNames();
		while (en.hasMoreElements()) {
			String key = (String) en.nextElement();
			String Property = props.getProperty(key);
			map.put(key, Property);
		}
		return map;
	}

	public void setValue(String key, String value) throws IOException {
		Properties prop = new Properties();
		InputStream fis = new FileInputStream(this.configPath);
		prop.load(fis);
		OutputStream fos = new FileOutputStream(this.configPath);
		prop.setProperty(key, value);
		prop.store(fos, "last update");
		fis.close();
		fos.close();
	}
}
