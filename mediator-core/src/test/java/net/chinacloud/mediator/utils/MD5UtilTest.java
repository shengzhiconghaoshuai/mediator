package net.chinacloud.mediator.utils;

import net.chinacloud.mediator.utils.MD5Util;

import org.junit.Test;

public class MD5UtilTest {

	@Test
	public void testGenerateRandomSigniture() {
		System.out.println(MD5Util.generateRandomSigniture());
	}

	@Test
	public void testGetMD5(){
		System.out.println(MD5Util.getMD5("test"));
	}
}
