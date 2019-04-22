package net.chinacloud.mediator.utils;

import java.io.StringWriter;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import com.thoughtworks.xstream.converters.collections.CollectionConverter;
import com.thoughtworks.xstream.io.xml.CompactWriter;
import com.thoughtworks.xstream.mapper.ClassAliasingMapper;

public class XStreamUtils {
	  
	public static String header = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
	
	 private static final Logger LOGGER = LoggerFactory.getLogger(XStreamUtils.class);
	
	public static String transformObjectToXMLStr(Object obj) {
		XStream xstream = new XStream();
        StringBuffer xmlStr = new StringBuffer(header);
		xstream.autodetectAnnotations(true);
		xstream.registerConverter(new DateConverter("yyyy-MM-dd HH:mm:ss",null,TimeZone.getTimeZone("Asia/Shanghai")));
		StringWriter sw = new StringWriter();
		xstream.marshal(obj,  new CompactWriter(sw));
		xmlStr.append(sw.toString());
		return xmlStr.toString();
	}
	
	public static String transformObjectToXMLStr(Object obj,String fieldName,Class clz) {
		XStream xstream = new XStream();
        StringBuffer xmlStr = new StringBuffer(header);
		ClassAliasingMapper mapper = new ClassAliasingMapper(xstream.getMapper());    
        mapper.addClassAlias(fieldName,clz);  
        xstream.registerConverter(new CollectionConverter(mapper));  
        xstream.autodetectAnnotations(true);
		StringWriter sw = new StringWriter();
		xstream.marshal(obj,  new CompactWriter(sw));
		xmlStr.append(sw.toString());
		return xmlStr.toString();
	}
	
}
