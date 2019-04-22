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
	
	<script type="text/javascript" src="${ctx }/static/jquery/js/jquery-2.0.3.min.js"></script>
	<script type="text/javascript" src="${ctx }/static/bootstrap/fuelux/fuelux.spinner.min.js"></script>
	<script type="text/javascript" src="${ctx }/static/js/channel.js"></script>
    <script type="text/javascript" src="${ctx }/static/zTree/jquery.ztree.core-3.5.js"></script>
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
				<a href="#">京东</a>
			</li>
			<li class="active">主目录映射</li>
		</ul><!-- .breadcrumb -->
	</div>
	<div class="page-content">
		<div class="page-header">
			<h1>
				京东
				<small>
					<i class="icon-double-angle-right"></i>
					主目录映射
				</small>
			</h1>
		</div><!-- /.page-header -->
		<div class="row">
			<div class="col-xs-12">
				<div class="row">
					<div class="col-xs-12">
						<fieldset>
							<label>京东店铺:</label>
							<select class="" style="width: 160px;" id= "jdselect">
								<option value="0">--请选择-- </option>
								<c:forEach var="application" items="${applications }">
									<option value="${application.id}">${application.name }</option>
								</c:forEach>
							</select>
						</fieldset>
					</div>
				</div>
				<div class="space-2"></div>
				<div class="row">
					<div class="col-xs-6">
						京东目录树
					 <ul id="treeDemo" class="ztree"></ul>
					</div>
					<div class="col-xs-6">
					<div class="widget-box">
                            <div class="widget-header">
                            <label>已映射叶子目录</label>
                            <button class="btn btn-white btn-info btn-bold" style="margin-left: 250px;" onclick="modify('grid1')">
							<i class="ace-icon fa fa-pencil-square-o bigger-120 blue"></i>
						            编辑
							</button>
							<button class="btn btn-white btn-info btn-bold" onclick="remove1('grid1')">
							<i class="ace-icon fa fa-trash-o bigger-120 blue"></i>
						            删除
							</button>
                            </div>
                     </div>
						<table id="grid-table"></table>
				       <div id="grid-pager"></div>	
					</div>
				</div>
					<!-- <div class="widget-box">
                            <div class="widget-header">
                            <label>已映射属性目录</label>
                            <button class="btn btn-white btn-info btn-bold" style="margin-left: 820px;" onclick="modify('grid2')">
							<i class="ace-icon fa fa-pencil-square-o bigger-120 blue"></i>
						            编辑
							</button>
							<button class="btn btn-white btn-info btn-bold" onclick="remove1('grid2')">
							<i class="ace-icon fa fa-trash-o bigger-120 blue"></i>
						            删除
							</button>
                            </div>
                     </div>
					<table id="grid-table1"></table>
					<div id="grid-pager1"></div>
					<div class="widget-box">
                            <div class="widget-header">
                            <label>已映射属性值目录</label>
                            <button class="btn btn-white btn-info btn-bold" style="margin-left:806px" onclick="modify('grid3')">
							<i class="ace-icon fa fa-pencil-square-o bigger-120 blue"></i>
						            编辑
							</button>
							<button class="btn btn-white btn-info btn-bold" onclick="remove1('grid3')">
							<i class="ace-icon fa fa-trash-o bigger-120 blue"></i>
						            删除
							</button>
                            </div>
                     </div>
					<table id="grid-table2"></table>
					<div id="grid-pager2"></div> -->
				     <div id = "channelDetail" class="hide">
					 <div style="margin: 20px;">
						<table class="add_brand_table " style="width:100%;">
							<tr style="height: 40px;" >
								<td class="add_brand_td" style="vertical-align:middle;"><span style="color:red;">*</span>渠道类目ID:</td>
								<td>
								<input type="text" id="channelId" class="dialog_input" value=""  disabled="disabled"/>
								</td>
							</tr>
							<tr style="height: 40px;"> 
								<td class="add_brand_td" style="vertical-align:top;padding-top:5px;"><span style="color:red;">*</span>渠道类目名称：</td>
								<td>
								<input type="text" id="channelName" class="dialog_input"  value="" disabled="disabled"/>
								</td>
							</tr>
							<tr style="height: 40px;">
								<td class="add_brand_td" style="vertical-align:top;padding-top:5px;"><span style="color:red;">*</span>系统类目ID：</td>
								<td>
								<input type="text" id="catId" class="dialog_input"  value="" autocomplete="off" placeholder="请填写系统类目ID"/>
								</td>
							</tr>
							<tr style="height: 40px;">
								<td class="add_brand_td" style="vertical-align:top;padding-top:5px;"><span style="color:red;">*</span>系统类目名称：</td>
								<td>
								<input type="text" id="catName" class="dialog_input"  value="" autocomplete="off" placeholder="请填写系统类目名称"/>
								</td>
							</tr>
						</table>
					</div>
					</div>
					 <div id = "channelattDetail" class="hide">
					 <div style="margin: 20px;">
						<table class="add_brand_table " style="width:100%;">
							<tr style="height: 40px;" >
								<td class="add_brand_td" style="vertical-align:middle;"><span style="color:red;">*</span>渠道类目ID:</td>
								<td>
								<input type="text" id="_channelId" class="dialog_input" value=""  disabled="disabled"/>
								</td>
							</tr>
							<tr style="height: 40px;">
								<td class="add_brand_td" style="vertical-align:top;padding-top:5px;"><span style="color:red;">*</span>渠道属性ID：</td>
								<td>
								<input type="text" id="channelAttrId" class="dialog_input"  value="" disabled="disabled"/>
								</td>
							</tr>
							<tr style="height: 40px;">
								<td class="add_brand_td" style="vertical-align:top;padding-top:5px;"><span style="color:red;">*</span>渠道属性值ID：</td>
								<td>
								<input type="text" id="channelAttrvalId" class="dialog_input"  value="" disabled="disabled"/>
								</td>
							</tr>
							<tr style="height: 40px;">
								<td class="add_brand_td" style="vertical-align:top;padding-top:5px;"><span style="color:red;">*</span>渠道属性值：</td>
								<td>
								<input type="text" id="channelAttrval" class="dialog_input"  value="" disabled="disabled"/>
								</td>
							</tr>
							<tr style="height: 40px;">
								<td class="add_brand_td" style="vertical-align:top;padding-top:5px;"><span style="color:red;">*</span>系统属性代号：</td>
								<td>
								<input type="text" id="attrcode" class="dialog_input"  value="" autocomplete="off" placeholder="请填写系统属性代号"/>
								</td>
							</tr>
							<tr style="height: 40px;">
								<td class="add_brand_td" style="vertical-align:top;padding-top:5px;"><span style="color:red;">*</span>系统属性值ID：</td>
								<td>
								<input type="text" id="attrvalcode" class="dialog_input"  value="" autocomplete="off" placeholder="请填写系统属性值ID"/>
								</td>
							</tr>
							<tr style="height: 40px;">
								<td class="add_brand_td" style="vertical-align:top;padding-top:5px;"><span style="color:red;">*</span>系统属性值：</td>
								<td>
								<input type="text" id="attrval" class="dialog_input"  value="" autocomplete="off" placeholder="请填写系统属性值"/>
								</td>
							</tr>
						</table>
					</div>
					</div>
					 <div id = "jdchannelattDetail" class="hide">
					 <div style="margin: 20px;">
						<table class="add_brand_table " style="width:100%;">
							<tr style="height: 40px;" >
								<td class="add_brand_td" style="vertical-align:middle;"><span style="color:red;">*</span>渠道类目ID:</td>
								<td>
								<input type="text" id="jdchannelId" class="dialog_input" value=""  disabled="disabled"/>
								</td>
							</tr>
							<tr style="height: 40px;">
								<td class="add_brand_td" style="vertical-align:top;padding-top:5px;"><span style="color:red;">*</span>渠道属性ID：</td>
								<td>
								<input type="text" id="jdchannelAttrId" class="dialog_input"  value="" disabled="disabled"/>
								</td>
							</tr>
							<tr style="height: 40px;">
								<td class="add_brand_td" style="vertical-align:top;padding-top:5px;"><span style="color:red;">*</span>渠道属性名：</td>
								<td>
								<input type="text" id="jdchannelAttrName" class="dialog_input"  value="" disabled="disabled"/>
								</td>
							</tr>
							<tr style="height: 40px;">
								<td class="add_brand_td" style="vertical-align:top;padding-top:5px;"><span style="color:red;">*</span>系统属性代号：</td>
								<td>
								<input type="text" id="jdattrcode" class="dialog_input"  value="" autocomplete="off" placeholder="请填写系统属性代号"/>
								</td>
							</tr>
							<tr style="height: 40px;">
								<td class="add_brand_td" style="vertical-align:top;padding-top:5px;"><span style="color:red;">*</span>默认值：</td>
								<td>
								<input type="text" id="defaultValue" class="dialog_input"  value="" autocomplete="off" placeholder="请填写默认值"/>
								</td>
							</tr>  
						</table>
					</div>
					</div>
			</div><!-- /.col -->
		</div><!-- /.row -->
	</div><!-- /.page-content -->
	
</body>
  <script src="${ctx }/static/js/jingdong/jdCategoryMapping.js"></script>
</html>