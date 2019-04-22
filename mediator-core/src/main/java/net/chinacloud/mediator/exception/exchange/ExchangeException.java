package net.chinacloud.mediator.exception.exchange;

import net.chinacloud.mediator.exception.ApplicationException;

public class ExchangeException extends ApplicationException{


	private static final long serialVersionUID = 1L;
	
	protected static final String MODULE = "exchange";
	
	public ExchangeException(String code, Object... args){
		super(MODULE, code, args);
	}
}
