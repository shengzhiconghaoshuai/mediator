package net.chinacloud.mediator.kaola.response;

import java.io.Serializable;

import net.chinacloud.mediator.kaola.exception.KaoLaErrorResponse;

public abstract class KaoLaResponse implements Serializable{
	
	private static final long serialVersionUID = 8437727684826991635L;
	
	private KaoLaErrorResponse error_response;

	public KaoLaErrorResponse getError_response() {
		return error_response;
	}

	public void setError_response(KaoLaErrorResponse error_response) {
		this.error_response = error_response;
	}
	
	
}
