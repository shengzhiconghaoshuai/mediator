<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/common/taglibs.jspf" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<mediator:contentHeader title="Mediator后台管理系统-渠道管理"/>
	<script type="text/javascript" src="${ctx }/static/js/environment.js"></script>
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
	
	<script type="text/javascript" src="${ctx }/static/jquery/js/jquery-2.0.3.min.js"></script>
	<script type="text/javascript" src="${ctx }/static/bootstrap/fuelux/fuelux.spinner.min.js"></script>
	<script type="text/javascript" src="${ctx }/static/js/channel.js"></script>
    <script type="text/javascript" src="${ctx }/static/js/jquery-ui.js"></script>
    <script src="${ctx }/static/js/jquery.jqGrid.src.js"></script>
    <script src="${ctx }/static/js/grid.locale-cn.js"></script>
    <style>
    ul.ztree {margin-top: 10px;border: 1px solid #617775;background: #f0f6e4;width:300px;height:360px;overflow-y:scroll;overflow-x:auto;}
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
				<a href="#">注册</a>
			</li>
		</ul><!-- .breadcrumb -->
	</div>
	<div class="page-content">
		<div class="page-header">
			<h1>
				系统
				<small>
					<i class="icon-double-angle-right"></i>
					注册
				</small>
			</h1>
		</div><!-- /.page-header -->
		<div class="row">
			<div class="col-xs-12">
				<div class="space-2"></div>
				<div class="row">
				<div class="widget-box">
                            <div class="widget-header">
                            <label>注册表</label>
                            <button class="btn btn-white btn-info btn-bold" style="margin-left: 850px;"  onclick="add()">
							<i class="ace-icon fa fa-pencil-square-o bigger-120 blue"></i>
						            新增
							</button>
                            <button class="btn btn-white btn-info btn-bold" onclick="modify()">
							<i class="ace-icon fa fa-pencil-square-o bigger-120 blue"></i>
						            编辑
							</button>
							<button class="btn btn-white btn-info btn-bold" onclick="remove1()">
							<i class="ace-icon fa fa-trash-o bigger-120 blue"></i>
						            删除
							</button>
                            </div>
                </div>
						<table id="grid-table"></table>
				       <div id="grid-pager"></div>	
				 </div>
				</div>
		</div>
		           <div id = "registry" class="hide">
					 <div style="margin: 20px;">
						<table class="add_brand_table " style="width:100%;">
							<tr style="height: 40px;" >
								<td class="add_brand_td" style="vertical-align:middle;"><span style="color:red;">*</span>注册key:</td>
								<td>
								<input type="text" id="rkey" class="dialog_input" value=""  disabled="disabled"/>
								</td> 
							</tr>
							<tr style="height: 40px;">
								<td class="add_brand_td" style="vertical-align:top;padding-top:5px;"><span style="color:red;">*</span>注册value：</td>
								<td>
								<input type="text" id="rvalue" class="dialog_input"  value="" />
								</td>
							</tr>
							<tr style="height: 40px;">
								<td class="add_brand_td" style="vertical-align:top;padding-top:5px;"><span style="color:red;">*</span>描述：</td>
								<td>
								<input type="text" id="rdescription" class="dialog_input"  value="" />
								</td>
							</tr>
						</table>
					</div>
					</div> 
		
		
			</div><!-- /.col -->
</body>
<script src="${ctx }/static/js/registry/registry.js"></script>
</html>
