package net.chinacloud.mediator.vip.vop.exception;

import net.chinacloud.mediator.exception.ApplicationException;
public class VopJitException extends ApplicationException{

	/**
	 * vop Exception
	 */
	private static final long serialVersionUID = 2525014623622044178L;

	//private String errCode;
	//private String errMsg;
	
	protected static final String MODULE = "vop";
	
	public VopJitException(String code, Object... args){
		
		super(MODULE, code, args, code);
	}
	
	

}