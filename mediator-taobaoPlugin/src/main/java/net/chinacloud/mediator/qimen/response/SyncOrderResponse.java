package net.chinacloud.mediator.qimen.response;

import com.thoughtworks.xstream.XStream;

/**
 * Created by Octopus8 on 2017/5/16.
 */
public class SyncOrderResponse extends QimenResponse{

    private static final long serialVersionUID = 1L;
    @Override
    public XStream getXStream() {
		XStream xstream = new XStream(); 
		xstream.alias("response", SyncOrderResponse.class);
		return xstream;
	}
}
