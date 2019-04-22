package net.chinacloud.mediator.xhs.request;


import java.io.IOException;
import java.util.Map;

public interface XhsRequest {
	public abstract String getApiMethodName();
	public abstract Map<Object, Object> getSysParams();
	public abstract Class<?> getResponseClass();
	public abstract Map<Object, Object> getAppJsonParams() throws IOException;
	public abstract String getRequestMethod();
}
