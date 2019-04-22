package net.chinacloud.mediator.qimen.request;

import net.chinacloud.mediator.qimen.response.QimenResponse;

public abstract class QimenRequest<T extends QimenResponse> {
	
	public abstract String getCustomId();

	public abstract String getApiMethodName();

	public abstract Class<T> getResponseClass();
	
}
