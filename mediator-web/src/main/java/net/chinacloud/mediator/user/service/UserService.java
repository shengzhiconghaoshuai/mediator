/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：UserService.java
 * 描述： 
 */
package net.chinacloud.mediator.user.service;

import net.chinacloud.mediator.user.dao.UserDao;
import net.chinacloud.mediator.user.domain.User;
import net.chinacloud.mediator.user.exception.PasswordNotMatchException;
import net.chinacloud.mediator.user.exception.UserNotExistsException;
import net.chinacloud.mediator.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description 
 * @author yejunwu123@gmail.com
 * @since 2015年8月19日 下午3:41:57
 */
@Service
public class UserService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	UserDao userRepository;
	
	@Autowired
	PasswordService passwordService;
	
	public User save(User entity) {
		entity.randomSalt();
		entity.setPassword(passwordService.encrypt(entity.getUserName(), entity.getPassword(), entity.getSalt()));
		userRepository.save(entity);
		return entity;
	}

	public User login(String userName, String password) throws UserNotExistsException, PasswordNotMatchException {
		if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
			throw new UserNotExistsException();
		}
		
		User loginUser = null;
		try {
			loginUser = userRepository.findByUserName(userName);
		} catch (Exception e) {
			//e.printStackTrace();
			LOGGER.error("no user found for userName {}", userName);
		}
		
		if (null == loginUser) {
			throw new UserNotExistsException();
		}
		
		passwordService.validate(loginUser, password);
		
		return loginUser;
	}

}
