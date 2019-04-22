package net.chinacloud.mediator.qimen.response;

import com.thoughtworks.xstream.XStream;

public class ItemStoreBanDingResponse extends QimenResponse{

	private static final long serialVersionUID = -2360184432284419428L;

	@Override
	public XStream getXStream() {
		XStream xstream = new XStream(); 
		xstream.alias("response", ItemStoreBanDingResponse.class);
		return xstream;
	}

}
