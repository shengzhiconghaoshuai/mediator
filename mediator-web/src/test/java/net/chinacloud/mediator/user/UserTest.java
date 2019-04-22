/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：UserTest.java
 * 描述： 
 */
package net.chinacloud.mediator.user;

import java.text.MessageFormat;
import java.util.Date;

import net.chinacloud.mediator.user.domain.User;
import net.chinacloud.mediator.user.service.PasswordService;
import net.chinacloud.mediator.utils.DateUtil;

import org.junit.Test;

/**
 * @description 用户测试
 * @author yejunwu123@gmail.com
 * @since 2015年8月19日 下午5:57:39
 */
public class UserTest {

	@Test
	public void test() {
		PasswordService ps = new PasswordService();
		
		User user = new User("test1", "passw0rd");
		user.setAdmin(true);
		user.setCreateTime(new Date());
		user.setDeleted(0);
		user.setEmail("test1@wuxicloud.com");
		user.setLastUpdate(new Date());
		user.randomSalt();
		
		user.setPassword(ps.encrypt(user.getUserName(), user.getPassword(), user.getSalt()));
		
		String sql = MessageFormat.format("INSERT INTO SYS_USER (USERNAME, PASSWORD, EMAIL, SALT, CREATETIME, LASTUPDATE, ADMIN, DELETED) VALUES (''{0}'', ''{1}'', ''{2}'', ''{3}'', ''{4}'', ''{5}'', {6}, {7});", 
				user.getUserName(),
				user.getPassword(),
				user.getEmail(),
				user.getSalt(),
				DateUtil.format(user.getCreateTime()),
				DateUtil.format(user.getLastUpdate()),
				user.isAdmin() ? 1 : 0,
				user.getDeleted());
		System.out.println(sql);
	}

}
