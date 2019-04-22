package net.chinacloud.mediator.kaola.request;


import java.io.IOException;
import java.util.Map;

public interface KaoLaRequest {
	public abstract String getApiMethodName();
	public abstract Map<String, String> getSysParams();
	public abstract Class<?> getResponseClass();
	public abstract Map<String, Object> getAppJsonParams() throws IOException;
	public abstract String getRequestMethod();
}
