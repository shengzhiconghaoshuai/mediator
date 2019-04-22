/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ImageUploadException.java
 * 描述： 
 */
package net.chinacloud.mediator.exception.product;

/**
 * @description 图片上传失败异常
 * @author yejunwu123@gmail.com
 * @since 2015年8月6日 下午1:01:48
 */
public class ImageUploadException extends ProductException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2134958176266469227L;
	/**
	 * @param code
	 * @param args
	 */
	public ImageUploadException(String imageUrl, String errorMessage) {
		super("exception.product.image.upload.fail", imageUrl, errorMessage);
	}

}
