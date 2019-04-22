/**
 * 版权：Copyright 2014- ChinaCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：NumberUtil.java
 * 描述：数字相关操作工具类
 */
package net.chinacloud.mediator.utils;

import java.math.BigDecimal;

/**
 * <数字相关操作工具类>
 * <数字相关操作工具类,如Double类型数字的四则运算>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月8日
 * @since 2014年12月8日
 */
public final class NumberUtil {
	
	/**
	 * 设置数值精度,四舍五入保留两位小数
	 * @param value
	 * @return
	 */
	public static Double round(Double value){
		return round(value,2);
	}
	/**
	 * 设置数值精度,四舍五入
	 * @param value
	 * @param scale 精度
	 * @return
	 */
	public static Double round(Double value,int scale){
		return round(value,scale,BigDecimal.ROUND_HALF_UP);
	}
	
	/**
	 * 设置数值精度
	 * @param value
	 * @param scale 精度
	 * @param roundMode 舍入模式
	 * @return
	 */
	public static Double round(Double value,int scale,int roundMode){
		if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
		BigDecimal decimal = new BigDecimal(value);
		return decimal.setScale(scale, roundMode).doubleValue();
	}
	/**
	 * 两数相加
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static Double add(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return new Double(b1.add(b2).doubleValue());
    }
	/**
	 * 两数相减
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static Double sub(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return new Double(b1.subtract(b2).doubleValue());
    }
	/**
	 * 两数相乘
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static Double mul(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return new Double(b1.multiply(b2).doubleValue());
    }
	/**
	 * 两数相除,保留两位精度
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static Double div(Double v1, Double v2) {
        return div(v1,v2,2);  
    }
	/**
	 * 两数相除
	 * @param v1
	 * @param v2
	 * @param scale 精度
	 * @return
	 */
	public static Double div(Double v1, Double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return new Double(b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP)
                .doubleValue());  
    }
	
	public static String add(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.add(b2).toString();
    }
}
