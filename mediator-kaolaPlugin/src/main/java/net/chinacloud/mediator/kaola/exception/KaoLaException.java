package net.chinacloud.mediator.kaola.exception;

public class KaoLaException extends Exception{
	
	private static final long serialVersionUID = -2618918023987585749L;
	private String errCode;
	private String errMsg;

	public KaoLaException() {
	}

	public KaoLaException(String message, Throwable cause) {
		super(message, cause);
	}

	public KaoLaException(String message) {
		super(message);
	}

	public KaoLaException(Throwable cause) {
		super(cause);
	}

	public KaoLaException(String errCode, String errMsg) {
		super((new StringBuilder()).append(errCode).append(": ").append(errMsg).toString());
		this.errCode = errCode;
		this.errMsg = errMsg;
	}

	public String getErrCode() {
		return errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

}
