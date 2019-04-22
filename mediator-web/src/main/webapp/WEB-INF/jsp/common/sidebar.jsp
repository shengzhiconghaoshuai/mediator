<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<a class="menu-toggler" id="menu-toggler" href="">
	<span class="menu-text"></span>
</a>
<div class="sidebar" id="sidebar">
	<script type="text/javascript">
		try{ace.settings.check('sidebar' , 'fixed')}catch(e){}
	</script>
	<ul class="nav nav-list">
		<shiro:hasRole name="admin">
			<li class="active">
				<a href="${ctx }/system/home/">
					<i class="icon-dashboard"></i>
					<span class="menu-text"> 首页 </span>
				</a>
			</li>
			<li>
				<a href="#" class="dropdown-toggle">
					<i class="icon-desktop"></i>
					<span class="menu-text"> 系统管理 </span>
					<b class="arrow icon-angle-down"></b>
				</a>
				<ul class="submenu">
					<li>
						<a href="${ctx }/system/channel/list" target="content">
							<i class="icon-double-angle-right"></i>
							渠道管理
						</a>
					</li>
					<li>
						<a href="${ctx }/system/application/list" target="content">
							<i class="icon-double-angle-right"></i>
							应用管理
						</a>
					</li>
					<li>
						<a href="${ctx }/system/monitor/druid/index.html" target="blank">
							<i class="icon-double-angle-right"></i>
							数据源监控
						</a>
					</li>
					<li>
						<a href="${ctx }/system/schedule/list" target="content">
							<i class="icon-double-angle-right"></i>
							调度管理
						</a>
					</li>
					<li>
						<a href="${ctx }/system/schedule/crontime/list" target="content">
							<i class="icon-double-angle-right"></i>
							调度时间管理
						</a>
					</li>
					<li>
						<a href="${ctx }/system/task/list" target="content">
							<i class="icon-double-angle-right"></i>
							task管理
						</a>
					</li>
					<li>
						<a href="${ctx }/system/task/template/list" target="content">
							<i class="icon-double-angle-right"></i>
							task模板
						</a>
					</li>
					<li>
						<a href="${ctx }/system/task/registry/" target="content">
							<i class="icon-double-angle-right"></i>
							注册表
						</a>
					</li>
					<li>
						<a href="${ctx }/system/productMapping/list" target="content">
							<i class="icon-double-angle-right"></i>
							商品映射管理
						</a>
					</li>
				</ul>
			</li>
		</shiro:hasRole>
		<shiro:hasAnyRoles name="admin,common">
			<li>
				<a href="#" class="dropdown-toggle">
					<i class="icon-list"></i>
					<span class="menu-text"> 淘宝 </span>
					<b class="arrow icon-angle-down"></b>
				</a>
				<ul class="submenu">
					<li>
						<a href="${ctx }/system/taobao/mapping/" target="content">
							<i class="icon-double-angle-right"></i>
							主目录映射
						</a>
					</li>
					<li>
						<a href="${ctx }/system/taobao/order/tocreate/" target="content">
							<i class="icon-double-angle-right"></i>
							task创建
						</a>
					</li>
					<!-- <li>
						<a href="${ctx }/">
							<i class="icon-double-angle-right"></i>
							属性映射
						</a>
					</li> -->
				</ul>
			</li>
			<li>
				<a href="#" class="dropdown-toggle">
					<i class="icon-list"></i>
					<span class="menu-text"> 天猫 </span>
					<b class="arrow icon-angle-down"></b>
				</a>
				<ul class="submenu">
					<li>
						<a href="${ctx }/system/tmall/mapping/" target="content">
							<i class="icon-double-angle-right"></i>
							主目录映射
						</a>
					</li>
					<!-- <li>
						<a href="${ctx }/">
							<i class="icon-double-angle-right"></i>
							属性映射
						</a>
					</li> -->
				</ul>
			</li>
			<li>
				<a href="#" class="dropdown-toggle">
					<i class="icon-list"></i>
					<span class="menu-text"> 京东 </span>
					<b class="arrow icon-angle-down"></b>
				</a>
				<ul class="submenu">
					<li>
						<a href="${ctx }/system/jingdong/mapping/" target="content">
							<i class="icon-double-angle-right"></i>
							主目录映射
						</a>
					</li>
				</ul>
			</li>
		</shiro:hasAnyRoles>
	</ul><!-- /.nav-list -->
	<div class="sidebar-collapse" id="sidebar-collapse">
		<i class="icon-double-angle-left" data-icon1="icon-double-angle-left" data-icon2="icon-double-angle-right"></i>
	</div>
	<script type="text/javascript">
		try{ace.settings.check('sidebar' , 'collapsed')}catch(e){}
	</script>
</div>