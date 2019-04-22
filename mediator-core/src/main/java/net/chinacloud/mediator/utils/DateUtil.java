/**
 * 版权：Copyright 2014- ChinaCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：DateUtil.java
 * 描述： 
 */
package net.chinacloud.mediator.utils;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <日期相关操作工具类>
 * <提供日期相关的操作>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月8日
 * @since 2014年12月8日
 */
public final class DateUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);
	
	private static final int AN_HOUR = 60 * 60 * 1000;	//一小时
	private static final long A_DAY = 24 * AN_HOUR;		//一天
	
	private static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";	//标准格式
	
	/**
	 * 解析字符串格式的日期为Date对象
	 * @param dateStr 字符串格式的日期,eg:2014-12-04 14:04:30
	 * @param pattern 日期格式,eg:yyyy-MM-dd HH:mm:ss
	 * @return java.util.Date
	 */
	public static Date parse(String dateStr,String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			LOGGER.error(e.toString());
			throw new IllegalArgumentException(MessageFormat.format("日期 {0} 转换失败,请检查日期格式 {1}", dateStr, pattern));
		}
	}
	
	/**
	 * 解析字符串格式的日期为Date对象
	 * 日期格式为yyyy-MM-dd HH:mm:ss
	 * @param dateStr 字符串格式的日期
	 * @return java.util.Date
	 */
	public static Date parse(String dateStr){
		return parse(dateStr,STANDARD_FORMAT);
	}
	
	/**
	 * 格式化日期
	 * @param date 日期对象
	 * @param pattern 日期格式,eg:yyyy-MM-dd
	 * @return 字符串格式的日期
	 */
	public static String format(Date date,String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
	
	/**
	 * 格式化日期,默认格式为yyyy-MM-dd HH:mm:ss
	 * @param date 日期对象
	 * @return 字符串格式的日期
	 */
	public static String format(Date date){
		return format(date,STANDARD_FORMAT);
	}
	
	/**
	 * 得到当前时间
	 * @return 长整形的时间
	 */
	public static long getCurrentTime(){
		return new Date().getTime();
	}
	
	/**
	 * 判断两个时间是否是同一天
	 * @param date1
	 * @param date2
	 * @return true:date1和date2表示同一天,false:date1和date2表示不同的天
	 */
	public static boolean isSameDay(Date date1,Date date2){
		String pattern = "yyyy-MM-dd";
		return format(date1, pattern).equals(format(date2,pattern));
	}
	
	/**
	 * 获取两个时间之间间隔的天数
	 * eg:
	 * 2014-12-04 14:04:30与2014-12-04 15:04:30间隔0天
	 * 2014-12-04 14:04:30与2014-12-05 13:04:30间隔1天
	 * 2014-12-04 14:04:30与2014-12-02 16:04:30间隔2天
	 * @param date1
	 * @param date2
	 * @return 间隔天数
	 */
	public static int getDayDiff(Date date1,Date date2){
		String pattern = "yyyy-MM-dd";
		String s1 = format(date1, pattern);
		String s2 = format(date2, pattern);
		
		Date d1 = parse(s1, pattern);
		Date d2 = parse(s2, pattern);
		
		return (int)(Math.abs(d1.getTime() - d2.getTime()) / A_DAY);
	}
	
	/**
	 * 获取两个时间之间间隔的天数
	 * eg:
	 * 2014-12-04 14:04:30与2014-12-04 15:04:30间隔0天
	 * 2014-12-04 14:04:30与2014-12-05 13:04:30间隔1天
	 * 2014-12-04 14:04:30与2014-12-02 16:04:30间隔2天
	 * @param date1
	 * @param date2
	 * @return 间隔天数
	 */
	public static int getDayDiffStandardFormat(Date date1,Date date2){
		String pattern = STANDARD_FORMAT;
		String s1 = format(date1, pattern);
		String s2 = format(date2, pattern);
		
		Date d1 = parse(s1, pattern);
		Date d2 = parse(s2, pattern);
		
		return (int)(Math.abs(d1.getTime() - d2.getTime()) / A_DAY);
	}
	
	/**
	 * 时间增加或减少count毫秒
	 * @param date
	 * @param count > 0表示增加,< 0表示减少
	 * @return 增加或减少count毫秒后的时间
	 */
	public static Date modify(Date date, long count){
		return new Date(date.getTime() + count);
	}
	
	/**
	 * 获取一天中最后的时间
	 * @param date
	 * @return 一天中最后的时间,eg:2014-12-04 23:59:59
	 */
	public static Date getLastTimeByDate(Date date){
		String dateStr = format(date, "yyyy-MM-dd");
		return parse(dateStr + " 23:59:59");
	}
	
	/**
	 * 获取一天中最开始的时间
	 * @param date
	 * @return 一天中最开始的时间,eg:2014-12-04 00:00:00
	 */
	public static Date getFirstTimeByDate(Date date){
		String dateStr = format(date, "yyyy-MM-dd");
		return parse(dateStr + " 00:00:00");
	}
	
	/**
	 * 分割时间片段
	 * <p>如果开始时间和结束时间是在同一天,则返回开始、结束时间的String格式 
	 * 如果开始时间和结束时间不是同一天,则按照interval间隔分割时间,eg：
	 * date1=2014-12-01 14:04:30,date2=2014-12-04 14:04:30,interval=0
	 * 那么分割后的时间片段为
	 * {
	 * 		['2014-12-01 14:04:30','2014-12-01 23:59:59'],
	 * 		['2014-12-02 00:00:00','2014-12-02 23:59:59'],
	 * 		['2014-12-03 00:00:00','2014-12-03 23:59:59'],
	 * 		['2014-12-04 00:00:00','2014-12-04 14:04:30']
	 * }
	 * @param date1 
	 * @param date2 
	 * @param interval 分割后的时间片段中的开始时间和结束时间之间间隔的天数,0表示开始时间和结束时间都在同一天
	 * @return 间隔的时间片段,格式为yyyy-MM-dd HH:mm:ss
	 */
	public static List<String[]> getDateSegment(Date date1,Date date2,int interval){
		return getDateSegment(date1,date2,STANDARD_FORMAT,interval);
	}
	
	/**
	 * 分割时间片段
	 * <p>如果开始时间和结束时间是在同一天,则返回开始、结束时间的String格式 
	 * 如果开始时间和结束时间不是同一天,则按照1天间隔分割时间,eg：
	 * date1=2014-12-01 14:04:30,date2=2014-12-04 14:04:30,pattern=yyyy-MM-dd HH:mm:ss,
	 * 那么分割后的时间片段为
	 * {
	 * 		['2014-12-01 14:04:30','2014-12-01 23:59:59'],
	 * 		['2014-12-02 00:00:00','2014-12-02 23:59:59'],
	 * 		['2014-12-03 00:00:00','2014-12-03 23:59:59'],
	 * 		['2014-12-04 00:00:00','2014-12-04 14:04:30']
	 * }
	 * @param date1 
	 * @param date2 
	 * @param interval 分割后的时间片段中的开始时间和结束时间之间间隔的天数,0表示开始时间和结束时间都在同一天
	 * @return 间隔的时间片段,格式为yyyy-MM-dd HH:mm:ss
	 */
	public static List<String[]> getDateSegment(Date date1,Date date2,String pattern){
		return getDateSegment(date1,date2,pattern,0);
	}
	
	/**
	 * 分割时间片段
	 * <p>如果开始时间和结束时间是在同一天,则返回开始、结束时间的String格式 
	 * 如果开始时间和结束时间不是同一天,则按照1天间隔分割时间,eg：
	 * date1=2014-12-01 14:04:30,date2=2014-12-04 14:04:30,interval=0
	 * 那么分割后的时间片段为
	 * {
	 * 		['2014-12-01 14:04:30','2014-12-01 23:59:59'],
	 * 		['2014-12-02 00:00:00','2014-12-02 23:59:59'],
	 * 		['2014-12-03 00:00:00','2014-12-03 23:59:59'],
	 * 		['2014-12-04 00:00:00','2014-12-04 14:04:30']
	 * }
	 * @param date1 
	 * @param date2 
	 * @param interval 分割后的时间片段中的开始时间和结束时间之间间隔的天数,0表示开始时间和结束时间都在同一天
	 * @return 间隔的时间片段,格式为yyyy-MM-dd HH:mm:ss,片段中开始时间和结束时间在同一天
	 */
	public static List<String[]> getDateSegment(Date date1,Date date2){
		return getDateSegment(date1,date2,STANDARD_FORMAT,0);
	}
	
	/**
	 * 分割时间片段
	 * <p>如果开始时间和结束时间是在同一天,则返回开始、结束时间的String格式 
	 * 如果开始时间和结束时间不是同一天,则按照interval间隔分割时间,eg：
	 * date1=2014-12-01 14:04:30,date2=2014-12-04 14:04:30,interval=0,pattern=yyyy-MM-dd HH:mm:ss,
	 * 那么分割后的时间片段为
	 * {
	 * 		['2014-12-01 14:04:30','2014-12-01 23:59:59'],
	 * 		['2014-12-02 00:00:00','2014-12-02 23:59:59'],
	 * 		['2014-12-03 00:00:00','2014-12-03 23:59:59'],
	 * 		['2014-12-04 00:00:00','2014-12-04 14:04:30']
	 * }
	 * @param date1 
	 * @param date2 
	 * @param pattern 日期格式
	 * @param interval 分割后的时间片段中的开始时间和结束时间之间间隔的天数,0表示开始时间和结束时间都在同一天
	 * @return 间隔的时间片段
	 */
	public static List<String[]> getDateSegment(Date date1,Date date2,String pattern,int interval){
		//排序,时间小的在前,时间大的在后
		Date startTime = null;
		Date endTime = null;
		if(date1.getTime() < date2.getTime()){
			startTime = date1;
			endTime = date2;
		}else{
			startTime = date2;
			endTime = date1;
		}
		
		List<String[]> segments = new ArrayList<String[]>();
		
		if(isSameDay(startTime, endTime)){	//如果是同一天
			String[] segment = {format(startTime, pattern),format(endTime, pattern)};
			segments.add(segment);
		}else{
			if(interval < 0){
				throw new IllegalArgumentException("参数interval不合法");
			}
			//获取两个时间之间间隔的天数
			int diff = getDayDiff(startTime, endTime) + 1;
			//lookup times
			int count = (diff % (interval + 1) == 0) ? (diff / (interval + 1)) : (diff / (interval + 1) + 1);
			if(count == 0){		//如果分段的间隔不小于两个时间间隔的天数,不够分割,返回开始时间和结束时间
				String[] segment = {format(startTime, pattern),format(endTime, pattern)};
				segments.add(segment);
			}else{
				for(int i = 0;i < count;i++){
					if(i == 0){		//第一段
						String[] segment = {format(startTime,pattern),format(getLastTimeByDate(modify(startTime, interval * A_DAY)), pattern)};
						segments.add(segment);
					}else if(i == (count - 1)){	//最后一段
						Date start = modify(startTime, (interval + 1) * A_DAY * i);
						//System.out.println("----" + format(start));
						if(start.getTime() > endTime.getTime()){
							String[] segment = {format(getFirstTimeByDate(endTime),pattern),format(endTime,pattern)};
							segments.add(segment);
						}else{
							String[] segment = {format(getFirstTimeByDate(start),pattern),format(endTime,pattern)};
							segments.add(segment);
						}
					}else{	//中间
						Date start = getFirstTimeByDate(modify(startTime, (interval + 1) * A_DAY * i));
						Date end = getLastTimeByDate(modify(startTime, ((interval + 1) * (i + 1) - 1) * A_DAY));
						String[] segment = {format(start,pattern),format(end,pattern)};
						segments.add(segment);
					}
				}
			}
		}
		return segments;
	}
}
