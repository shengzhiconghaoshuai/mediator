<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/common/taglibs.jspf" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<mediator:contentHeader title="Mediator后台管理系统"/>
	<script type="text/javascript" src="${ctx }/static/js/environment.js"></script>
	<mediator:calendar/>
</head>
<body>
	<!-- 顶部导航 -->
	<%@include file="../common/nav.jsp" %>
	
	<div class="main-container" id="main-container">
		<script type="text/javascript">
			try{ace.settings.check('main-container' , 'fixed')}catch(e){}
		</script>
		<div class="main-container-inner">
			<!-- 左侧菜单 -->
			<%@include file="../common/sidebar.jsp" %>

			<div class="main-content">
			<!-- 右侧内容 -->
			<iframe id="contentFrame" name="content" src="${ctx }/system/home/calendar" width="100%" scrolling="no" frameborder="0" style="background-color: white;" ></iframe>
			</div><!-- /.main-content -->

			<!-- ace settings -->
		<%-- 	<%@include file="../common/aceSettings.jsp" %> --%>
		</div><!-- /.main-container-inner -->
		<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
			<i class="icon-double-angle-up icon-only bigger-110"></i>
		</a>
	</div><!-- /.main-container -->
</body>
</html>