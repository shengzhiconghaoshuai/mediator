package net.chinacloud.mediator.utils;

import junit.framework.Assert;
import net.chinacloud.mediator.utils.NumberUtil;

import org.junit.Test;

public class NumberUtilTest {

	@Test
	public void testRound() {
		System.out.println(NumberUtil.round(123.5642222222));;
	}
	
	@Test
	public void testAdd(){
		Double actual = NumberUtil.add(13.5, 12.8);
		Assert.assertEquals(26.3, actual, 0);
	}
	
	@Test
	public void testSub(){
		Double actual = NumberUtil.sub(13.5, 12.8);
		Assert.assertEquals(0.7, actual,0);
	}
	
	@Test
	public void testMul(){
		Double actual = NumberUtil.mul(1.3, 1.3);
		Assert.assertEquals(1.69, actual,0.001);
	}
	
	@Test
	public void testDiv(){
		Double actual = NumberUtil.div(1.44, 1.2);
		Assert.assertEquals(1.2, actual,0);
	}
}
