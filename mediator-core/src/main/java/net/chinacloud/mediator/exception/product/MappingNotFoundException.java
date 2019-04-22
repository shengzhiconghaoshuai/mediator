package net.chinacloud.mediator.exception.product;

public class MappingNotFoundException extends ProductException {

	private static final long serialVersionUID = 693198563914877604L;

	/**
	 * 
	 * @param property1 模块名称,如分类、属性、属性值
	 * @param property2 系统标识
	 */
	public MappingNotFoundException(String property1, String property2) {
		super("exception.product.mapping.notfound", property1, property2);
	}

}
