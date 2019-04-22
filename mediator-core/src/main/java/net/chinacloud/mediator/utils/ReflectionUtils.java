/**
 * Copyright (c) 2015-2015 yejunwu126@126.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package net.chinacloud.mediator.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @description 反射工具类
 * @author ywu@wuxicloud.com
 * @2015年4月24日 @下午3:04:59
 */
public final class ReflectionUtils {
	
	private ReflectionUtils(){}
	
	/**
	 * 获取泛型父类指定下标的泛型类型的实际类型,eg: A extends B<C,D>
	 * @param clazz 子类的class,eg:A
	 * @param index 泛型类型下标,eg:C的下标为0,D的下标为1
	 * @return 泛型参数的实际类型
	 */
	public static Class<?> getSuperClassGenericType(Class<?> clazz, int index) {
		//获取泛型父类
		Type genericSuperType = clazz.getGenericSuperclass();
		//如果不支持泛型,则返回Object.class
		if (!(genericSuperType instanceof ParameterizedType)) {
			return Object.class;
		}
		Type[] actualTypes = ((ParameterizedType)genericSuperType).getActualTypeArguments();

		if (index < 0 || index >= actualTypes.length) {
			throw new IllegalArgumentException("参数 index 无效,必须 >=0 ,< " + actualTypes.length);
		}
		Type actualType = actualTypes[index];
		if (!(actualType instanceof Class<?>)) {
			return Object.class;
		}
		return (Class<?>)actualType;
	}
	
	/**
	 * 获取泛型父类指定下标的泛型类型的实际类型,eg: A extends B<C,D>,默认下标为0
	 * @param clazz 子类的class,eg:A
	 * @return C的实际类型
	 */
	public static Class<?> getSuperClassGenericType(Class<?> clazz) {
		return getSuperClassGenericType(clazz, 0);
	}
}
