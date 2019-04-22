/**
 * Copyright (c) 2015-2015 yejunwu126@126.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package net.chinacloud.mediator.resource.domain;

/**
 * @description 操作实体
 * @author ywu@wuxicloud.com
 * 2015年4月30日 下午4:37:09
 */
public enum Operation {
	ADD {
		@Override
		String getName() {
			return "新增";
		}
	}, DELETE {
		@Override
		String getName() {
			return "删除";
		}
	}, UPDATE {
		@Override
		String getName() {
			return "修改";
		}
	}, VIEW {
		@Override
		String getName() {
			return "查看";
		}
	}, ALL {
		@Override
		String getName() {
			return "所有";
		}
	};
	
	/**
	 * 获取操作名称
	 * @return
	 */
	abstract String getName();
}
