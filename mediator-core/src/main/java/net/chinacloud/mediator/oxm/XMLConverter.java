package net.chinacloud.mediator.oxm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;
/**
 * java Object <-> xml
 * @author Administrator
 *
 */
public class XMLConverter {
	private Marshaller marshaller;
	private Unmarshaller unmarshaller;
	
	protected static Logger LOGGER = LoggerFactory.getLogger(XMLConverter.class);
	
	public void setMarshaller(Marshaller marshaller) {
		this.marshaller = marshaller;
	}
	public void setUnmarshaller(Unmarshaller unmarshaller) {
		this.unmarshaller = unmarshaller;
	}
	
	public String convertFromObject2XML(Object source) throws  IOException{
		String result = null;
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			this.marshaller.marshal(source, new StreamResult(baos));
			result = baos.toString();
			/*if(LOGGER.isInfoEnabled()){
				LOGGER.info("xml:" + result);
			}*/
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally{
			if(null != baos){
				try {
					baos.close();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if(null != baos){
						baos.close();
					}
				}
			}
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T convertFromXML2Object(String source) throws XmlMappingException, IOException{
		//ByteArrayInputStream bais = new ByteArrayInputStream(source.getBytes());
		StringReader sr = null;
		Object obj = null;
		try {
			sr = new StringReader(source);
			obj = this.unmarshaller.unmarshal(new StreamSource(sr));
			sr.close();
			return (T)obj;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally{
			try {
				if(null != sr){
					sr.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}finally{
				if(null != sr){
					sr.close();
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T convertFromXML2Object(InputStream is) throws XmlMappingException, IOException{
		//ByteArrayInputStream bais = new ByteArrayInputStream(source.getBytes());
		Object obj = null;
		try {
			obj = this.unmarshaller.unmarshal(new StreamSource(is));
			return (T)obj;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally{
			try {
				if(null != is){
					is.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}finally{
				if(null != is){
					is.close();
				}
			}
		}
	}
}
