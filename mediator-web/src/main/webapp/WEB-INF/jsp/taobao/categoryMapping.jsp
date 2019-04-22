<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/common/taglibs.jspf" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<mediator:contentHeader title="Mediator后台管理系统-渠道管理"/>
	<link rel="stylesheet" href="${ctx}/static/css/ui.jqgrid.css" />
	<link rel="stylesheet" href="${ctx}/static/css/bootstrap.css" />
	<link rel="stylesheet" href="${ctx}/static/css/font-awesome.css" />
	<!-- text fonts -->
	<link rel="stylesheet" href="${ctx}/static/css/ace-fonts.css" />
	<!-- ace styles -->
	<link rel="stylesheet" href="${ctx}/static/css/ace.css" />
	<link rel="stylesheet" href="${ctx}/static/css/ace-rtl.css" />
	<link rel="stylesheet" href="${ctx}/static/css/jquery-ui.css" />
	<link rel="stylesheet" href="${ctx}/static/css/jquery-ui.custom.css" />
	<link rel="stylesheet" href="${ctx }/static/css/zTreeStyle/zTreeStyle.css" type="text/css">

    <script type="text/javascript" src="${ctx }/static/zTree/jquery.ztree.core-3.5.js"></script>
    <script type="text/javascript" src="${ctx }/static/js/jquery-ui.js"></script>
    <script src="${ctx }/static/js/jquery.jqGrid.src.js"></script>
    <script src="${ctx }/static/js/grid.locale-cn.js"></script>
    <script type="text/javascript" src="${ctx }/static/js/taobao/mapping.js"></script>
	
	<style type="text/css">
		.ztree {
			height: 300px;
		}
		div#rMenu {
			position:absolute; 
			visibility:hidden; 
			top:0; 
			background-color: #555;
			text-align: 
			left;padding: 2px;
		}
		div#rMenu ul {
			margin: 0;
		}
		div#rMenu ul li {
			margin: 1px 0;
			padding: 0 5px;
			cursor: pointer;
			list-style: none outside none;
			background-color: #DFDFDF;
		}
	</style>
</head>
<body>
	<div class="breadcrumbs" id="breadcrumbs">
		<script type="text/javascript">
			try{ace.settings.check('breadcrumbs' , 'fixed')}catch(e){}
		</script>
		<ul class="breadcrumb">
			<li>
				<i class="icon-home home-icon"></i>
				<a href="#">系统</a>
			</li>
			<li>
				<a href="#">淘宝</a>
			</li>
			<li class="active">主目录映射</li>
		</ul><!-- .breadcrumb -->
		<div class="nav-search" id="nav-search">
			<form class="form-search">
				<span class="input-icon">
					<input type="text" placeholder="Search ..." class="nav-search-input" id="nav-search-input" autocomplete="off" />
					<i class="icon-search nav-search-icon"></i>
				</span>
			</form>
		</div><!-- #nav-search -->
	</div>
	<div class="page-content">
		<div class="page-header">
			<h1>
				淘宝
				<small>
					<i class="icon-double-angle-right"></i>
					主目录映射
				</small>
			</h1>
		</div><!-- /.page-header -->
		<div class="row" >
			<div class="col-xs-12" style="height: 2000px;">
				<div class="row">
					<div class="col-xs-12">
						<div class="row">
							<div class="col-xs-12">
								<fieldset>
									<label>淘宝店铺:</label>
									<select id="selectApp" class="" style="width: 160px;">
										<option value="0">--请选择-- </option>
										<c:forEach var="application" items="${applications }">
											<option value="${application.id }">${application.name }</option>
										</c:forEach>
									</select>
								</fieldset>
							</div>
						</div>
						<div class="space-2"></div>
						<div class="row">
							<div class="col-xs-4">
								<div style="overflow: scroll;">
									<span class="label label-info">淘宝目录树</span>
									<ul id="taobaoCategory" class="ztree"></ul>
								</div>
							</div>
							<div class="col-xs-8">
								<span class="label label-info">已映射叶子目录</span>
								<div style="height: auto;">
									<table id="mappedCategory"></table>
						       		<div id="mappedCategoryPager"></div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="space-2"></div>
				<hr />
				<div class="space-2"></div>
				<!-- <div class="row">
					<div class="col-xs-12" >
						<div class="row">
							<div class="col-xs-4">
								<div style="overflow: scroll;">
									<span class="label label-info">淘宝属性/值</span>
									<ul id="taobaoAttr" class="ztree"></ul>
								</div>
							</div>
							<div class="col-xs-8">
								<div class="row">
									<span class="label label-info">已映射属性</span>
									<table id="mappedAttr"></table>
									<div id="mappedAttrPager"></div>
								</div>
								<div class="space-2"></div>
								<div class="row">
									<span class="label label-info">已映射属性值</span>
									<table id="mappedAttrVal"></table>
									<div id="mappedAttrValPager"></div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- 右键菜单 -->
				<div id="rMenu">
					<ul>
						<li id="cMapping" onclick="categoryMapping();">类目映射</li>
						<!-- <li id="aMapping" onclick="attributeMapping();">属性映射</li> -->
					</ul>
				</div>
				
				<%@include file="dialog.jsp" %>
				
			</div><!-- /.col -->
		</div><!-- /.row -->
	</div><!-- /.page-content -->
</body>
</html>