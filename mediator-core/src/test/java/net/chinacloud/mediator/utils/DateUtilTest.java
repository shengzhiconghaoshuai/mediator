package net.chinacloud.mediator.utils;

import java.util.Date;
import java.util.List;

import net.chinacloud.mediator.utils.DateUtil;

import org.junit.Test;

public class DateUtilTest {

	@Test
	public void testParse() {
		Date date = DateUtil.parse("2014-12-04 15:09:00", "yyyy-MM-dd HH:mm:ss");
		System.out.println(date.getTime());
	}
	
	@Test
	public void testParse1(){
		Date date = DateUtil.parse("2014-12-04 15:09:00");
		System.out.println(date.getTime());
	}
	
	@Test
	public void testGetDayDiff(){
		Date date1 = DateUtil.parse("2014-12-04 15:09:00");
		Date date2 = DateUtil.parse("2014-12-03 12:09:00");
		
		System.out.println(DateUtil.getDayDiff(date1, date2));
	}
	
	/**
	 * 日期在同一天,间隔为1天
	 */
	@Test
	public void testGetDateSegment(){
		Date date1 = DateUtil.parse("2014-12-04 15:09:00");
		Date date2 = DateUtil.parse("2014-12-04 16:09:00");
		
		List<String[]> segments = DateUtil.getDateSegment(date1, date2);
		for(String[] segment : segments){
			System.out.println(segment[0] + " && " + segment[1]);
		}
	}
	
	/**
	 * 日期在同一天,间隔为2天
	 */
	@Test
	public void testGetDateSegment2(){
		Date date1 = DateUtil.parse("2014-12-04 15:09:00");
		Date date2 = DateUtil.parse("2014-12-04 16:09:00");
		
		List<String[]> segments = DateUtil.getDateSegment(date1, date2,2);
		for(String[] segment : segments){
			System.out.println(segment[0] + " && " + segment[1]);
		}
	}
	
	/**
	 * 日期不在同一天,间隔为1天
	 */
	@Test
	public void testGetDateSegment3(){
		Date date1 = DateUtil.parse("2014-12-04 15:09:00");
		Date date2 = DateUtil.parse("2014-12-08 14:09:00");
		
		List<String[]> segments = DateUtil.getDateSegment(date1, date2);
		for(String[] segment : segments){
			System.out.println(segment[0] + " && " + segment[1]);
		}
	}
	
	/**
	 * 日期不在同一天,间隔为2天
	 */
	@Test
	public void testGetDateSegment4(){
		Date date1 = DateUtil.parse("2014-12-04 15:09:00");
		Date date2 = DateUtil.parse("2014-12-08 14:09:00");
		
		List<String[]> segments = DateUtil.getDateSegment(date1, date2, 2);
		for(String[] segment : segments){
			System.out.println(segment[0] + " && " + segment[1]);
		}
	}
	
	/**
	 * 日期不在同一天,间隔为1天,跨月
	 */
	@Test
	public void testGetDateSegment5(){
		Date date1 = DateUtil.parse("2014-11-29 15:09:00");
		Date date2 = DateUtil.parse("2014-12-08 14:09:00");
		
		List<String[]> segments = DateUtil.getDateSegment(date1, date2);
		for(String[] segment : segments){
			System.out.println(segment[0] + " && " + segment[1]);
		}
	}
	
	/**
	 * 日期不在同一天,间隔为3天,跨月
	 */
	@Test
	public void testGetDateSegment6(){
		Date date1 = DateUtil.parse("2014-11-29 15:09:00");
		Date date2 = DateUtil.parse("2014-12-08 14:09:00");
		
		List<String[]> segments = DateUtil.getDateSegment(date1, date2,3);
		for(String[] segment : segments){
			System.out.println(segment[0] + " && " + segment[1]);
		}
	}
	
	/**
	 * 日期不在同一天,间隔为1天,指定格式
	 */
	@Test
	public void testGetDateSegment7(){
		Date date1 = DateUtil.parse("2014-11-29 15:09:00");
		Date date2 = DateUtil.parse("2014-12-08 14:09:00");
		
		List<String[]> segments = DateUtil.getDateSegment(date1, date2,"yyyy%MM%dd HH-mm-ss");
		for(String[] segment : segments){
			System.out.println(segment[0] + " && " + segment[1]);
		}
	}
	
	@Test
	public void testGetDateSegment1(){
		Date date1 = new Date(1461081600000L);
		Date date2 = new Date();
		
		List<String[]> segments = DateUtil.getDateSegment(date1, date2);
		for(String[] segment : segments){
			System.out.println(segment[0] + " && " + segment[1]);
		}
	}
	
	@Test
	public void testModify() {
		//System.out.println(86400000 * 84);
		Date startTime = new Date(1461081600000L);
		Date start = DateUtil.modify(startTime, (0 + 1) * 86400000 * 84L);
		System.out.println(DateUtil.format(start));
	}
}
