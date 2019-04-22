package net.chinacloud.mediator.xhs.exception;

public class XhsException extends Exception{
	
	private static final long serialVersionUID = -2618918023987585749L;
	private String errCode;
	private String errMsg;

	public XhsException() {
	}

	public XhsException(String message, Throwable cause) {
		super(message, cause);
	}

	public XhsException(String message) {
		super(message);
	}

	public XhsException(Throwable cause) {
		super(cause);
	}

	public XhsException(String errCode, String errMsg) {
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
