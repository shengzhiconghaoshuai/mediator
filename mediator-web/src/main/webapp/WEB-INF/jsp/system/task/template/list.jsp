<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/common/taglibs.jspf" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<mediator:contentHeader title="Mediator后台管理系统-渠道管理"/>
	<%-- <script type="text/javascript" src="${ctx }/static/js/environment.js"></script> --%>
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
    <script type="text/javascript" src="${ctx }/static/js/jquery-ui.js"></script>
    <script src="${ctx }/static/js/jquery.jqGrid.src.js"></script>
    <script src="${ctx }/static/js/grid.locale-cn.js"></script>
    <script type="text/javascript" src="${ctx }/static/bootstrap/fuelux/fuelux.spinner.min.js"></script>
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
				<a href="#">task</a>
			</li>
			<li class="active">模板列表</li>
		</ul><!-- .breadcrumb -->
	</div>
	<div class="page-content">
		<div class="page-header">
			<h1>
				系统
				<small>
					<i class="icon-double-angle-right"></i>
					task模板列表
				</small>
			</h1>
		</div><!-- /.page-header -->
		<div class="row">
			<div class="col-xs-12">
				<!-- PAGE CONTENT BEGINS -->
				 <div class="widget-box">
                     <div class="widget-header">
                     <span class="input-icon condition">
                     <label>类型:</label> 
                     <select id="taskType" name="taskType">
                     <option value="">--请选择type--</option>
					 <c:forEach items="${templateTypes }" var="templateType">
						<option value="${templateType }">${templateType }</option>
					 </c:forEach>
					 </select>
                     </span>
                     <span class="input-icon condition">
                     <label>子类型:</label>
                     <select name="templateId" id="_subType">
						<option value="">--请选择subType--</option>
					 </select>
                     </span>
                     <!-- 查询 -->
					 <span class="input-icon condition">
						<button class="btn btn-info" type="button" id="taskQueryBtn">
							<i class="icon-ok bigger-110"></i>
							查询
						</button>
					 </span>
					  <span class="input-icon condition">
						<button class="btn btn-info" type="button" id="taskAddBtn">
							<i class="icon-plus-sign purple bigger-110"></i>
							添加模板
						</button>
					 </span>
                     </div>
                 </div>
						<div class="table-responsive">
						<table id="grid-table"></table>
						<div id="grid-pager"></div>
					    </div><!-- /span -->
		</div><!-- /.row -->
	</div>
	<div id="dialog" title="" class="hide">
	    <div style="margin: 20px;">
	    <form class="form-horizontal"  action="${ctx }/system/task/template/add" method="post" id="templateAddForm">
	        <div class="hide"> <input type="text" id="id" name="id" /> </div>
			<table class="add_brand_table " style="width:100%;">
				<tr style="height: 40px;" >
					<td class="add_brand_td" style="vertical-align:middle;"><span style="color:red;">*</span>类型:</td>
					<td>
					<input type="text" id="type" class="dialog_input"   name="type" />
					</td> 
				</tr>
				<tr style="height: 40px;">
					<td class="add_brand_td" style="vertical-align:top;padding-top:5px;"><span style="color:red;">*</span>子类型：</td>
					<td>
					<input type="text" id="subType" class="dialog_input"  name="subType" />
					</td>
				</tr>
				<tr style="height: 40px;">
					<td class="add_brand_td" style="vertical-align:top;padding-top:5px;"><span style="color:red;">*</span>描述：</td>
					<td>
					<textarea class="form-control limited" name="description" id="description" ></textarea>
					</td>
				</tr>
				<tr style="height: 40px;">
					<td class="add_brand_td" style="vertical-align:top;padding-top:5px;"><span style="color:red;">*</span>是否可重复 ：</td>
					<td>
					<select  id="repeatable" name="repeatable">
						<option value="0">否</option>
						<option value="1">是</option>
					</select>
					</td>
				</tr>
				<tr style="height: 40px;">
					<td class="add_brand_td" style="vertical-align:top;padding-top:5px;"><span style="color:red;">*</span>是否可重跑 ：</td>
					<td>
					<select  id="reRun" name="reRun">
						<option value="0">否</option>
						<option value="1">是</option>
					</select>
					</td>
				</tr>
				<tr style="height: 40px;">
					<td class="add_brand_td" style="vertical-align:top;padding-top:5px;"><span style="color:red;">*</span>是否可挂起：</td>
					<td>
					<select  id="hung" name="hung">
						<option value="0">否</option>
						<option value="1">是</option>
					</select>
					</td>
				</tr>
				<tr style="height: 40px;">
					<td class="add_brand_td" style="vertical-align:top;padding-top:5px;"><span style="color:red;">*</span>优先级：</td>
					<td>
					<input type="text" name="priority" class="input-mini" id="priority" />
					</td>
				</tr>
				<tr style="height: 40px;">
					<td class="add_brand_td" style="vertical-align:top;padding-top:5px;"><span style="color:red;">*</span>状态：</td>
					<td>
					<select  id="status" name="status">
						<option value="0">关闭</option>
						<option value="1">启用</option>
					</select>
					</td>
				</tr>
			</table>
			</form>
		</div>
	</div>
</div><!-- /.page-content -->
</body>
<script src="${ctx }/static/js/task/taskTemplate.js"></script>
</html>