package net.chinacloud.mediator.xhs.init;

import net.chinacloud.mediator.xhs.exception.XhsException;
import net.chinacloud.mediator.xhs.request.XhsRequest;


public interface XhsClient {

	public abstract Object execute(XhsRequest request) throws XhsException;
}
