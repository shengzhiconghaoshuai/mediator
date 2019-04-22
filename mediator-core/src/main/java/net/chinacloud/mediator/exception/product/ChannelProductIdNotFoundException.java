package net.chinacloud.mediator.exception.product;

public class ChannelProductIdNotFoundException extends ProductException {
	private static final long serialVersionUID = 6152424957992754479L;

	public ChannelProductIdNotFoundException(String arg) {
		super("exception.product.channelProductId.notfound",arg);
	}
}
