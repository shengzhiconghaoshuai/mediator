package net.chinacloud.mediator.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.FilenameUtils;

public class ImageUtil {
	/**
	 * 将800*800的图片序列化成byte[]数组
	 * 
	 * @return
	 * @throws Exception
	 */
	public static byte[] image2Bytes(String imagePath) throws Exception {
		URL url = new URL(imagePath.trim());
		// 打开链接
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(5 * 1000);
		InputStream inStream = conn.getInputStream();
		// 得到图片的二进制数据，以二进制封装得到数据，具有通用性
		byte[] data = readInputStream(inStream);
		return data;

	}

	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		// 创建一个Buffer字符串 
		byte[] buffer = new byte[1024];
		// 每次读取的字符串长度，如果为-1，代表全部读取完毕
		int len = 0;
		byte[] b = null ;
		try{
			// 使用一个输入流从buffer里把数据读取出来
			while ((len = inStream.read(buffer)) != -1) {
				// 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
				outStream.write(buffer, 0, len);
			}
			// 把outStream里的数据写入内存
			b = outStream.toByteArray();
		}catch(Exception e){
			throw new Exception();
		}finally{
			if(inStream!=null){
				// 关闭输入流
				inStream.close();
				inStream = null; 
			}
			if(outStream!=null){
				outStream.close();	
				outStream = null;
			}
		}
		return b;
	}

	/**
	 * 根据文件路径获取文件名称
	 * @param url
	 * @return
	 */
	public static String toFileName(String url) {
		return FilenameUtils.getName(url);
	}

}
