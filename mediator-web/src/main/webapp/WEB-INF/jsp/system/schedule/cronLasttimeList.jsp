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
	<link rel="stylesheet" href="${ctx }/static/css/zTreeStyle/zTreeStyle.css" type="text/css">
	<link rel="stylesheet" type="text/css" href="${ctx }/static/json/jsoneditor.min.css">
	
	<script type="text/javascript" src="${ctx }/static/jquery/js/jquery-2.0.3.min.js"></script>
	<script type="text/javascript" src="${ctx }/static/bootstrap/fuelux/fuelux.spinner.min.js"></script>
	<script type="text/javascript" src="${ctx }/static/js/channel.js"></script>
    <script type="text/javascript" src="${ctx }/static/zTree/jquery.ztree.core-3.5.js"></script>
    <script type="text/javascript" src="${ctx }/static/js/jquery-ui.js"></script>
    <script src="${ctx }/static/js/jquery.jqGrid.src.js"></script>
    <script src="${ctx }/static/js/grid.locale-cn.js"></script>
	<script type="text/javascript" src="${ctx }/static/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="${ctx }/static/json/jsoneditor.min.js"></script>
	<script type="text/javascript" src="${ctx }/static/json/asset/jsonlint/jsonlint.js"></script>
	<style type="text/css">
		.condition {
			margin: 3px 2%;
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
				<a href="#">调度</a>
			</li>
			<li class="active">调度列表</li>
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
				系统
				<small>
					<i class="icon-double-angle-right"></i>
					调度时间列表
				</small>
			</h1>
		</div><!-- /.page-header -->
		<div class="row">
			<div class="col-xs-12">
				<!-- PAGE CONTENT BEGINS -->
				<div class="row">
					<div class="col-xs-12">
						<div class="table-responsive">
							<table id="sample-table-1" class="table table-striped table-bordered table-hover">
								<thead>
									<tr>
										<th class="center">
											<label>
												<input type="checkbox" class="ace" />
												<span class="lbl"></span>
											</label>
										</th>
										<th>应用名称</th>
										<th>模板</th>
										<th>
											<i class="icon-time bigger-110 hidden-480"></i>
											上次运行时间
										</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${cronTimes }" var="cronTime">
										<tr>
											<td class="center">
												<label>
													<input type="checkbox" class="ace" />
													<span class="lbl"></span>
												</label>
											</td>
											<td>
												<a href="#">${cronTime['NAME'] }</a>
											</td>
											<td>${cronTime['DESCRIPTION'] }</td>
											<td class="hidden-480"><fmt:formatDate value="${cronTime['LASTTIME'] }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
											<td>
												<div class="visible-md visible-lg hidden-sm hidden-xs btn-group">
													<button class="btn btn-xs btn-info" id="cronlasttime_edit" onclick="changeLastTime(${cronTime['APPLICATION_ID'] }, ${cronTime['TEMPLATE_ID'] })">
														<i class="icon-edit bigger-120" title="修改时间"></i>
													</button>
												</div>
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div><!-- /.table-responsive -->
						<div class="clearfix form-actions">
							<div class="col-md-offset-1 col-md-9">
								<button class="btn btn-xs btn-info" id="cronlasttime_edit" onclick="addLastTime()">
									<i class="icon-edit bigger-120" title="创建运行时间"></i>
								</button>
							</div>
						</div>
					</div><!-- /span -->
				</div><!-- /row -->
			</div><!-- /.col -->
		</div><!-- /.row -->
		<div style="margin: 20px;" id="cronTimeUpdate" class="hidden">
			<table class="add_brand_table " style="width:100%;">
				<tr style="height: 40px;" >
					<td class="add_brand_td" style="vertical-align:middle;"><span style="color:red;">*</span>运行时间:</td>
					<td>
						<input type="hidden" id="appId">
						<input type="hidden" id="tempId">
						<input type="text" id="newLastTime" class="dialog_input"  value="" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', readOnly:true})"/>
					</td>
				</tr>
			</table>
		</div>
		<div style="margin: 20px;" id="cronTimeAdd" class="hidden">
			<table class="add_brand_table " style="width:100%;">
				<tr style="height: 40px;" >
					<td class="add_brand_td" style="vertical-align:middle;"><span style="color:red;">*</span>渠道名称:</td>
					<td>
						<select class="" id="channelId" name="channelId">
							<option value="">--请选择渠道--</option>
							<c:forEach items="${channels }" var="channel">
								<option value="${channel.id }">${channel.name }</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr style="height: 40px;"> 
					<td class="add_brand_td" style="vertical-align:top;padding-top:5px;"><span style="color:red;">*</span>应用名称：</td>
					<td>
						<select name="applicaitonId" id="applicaitonId">
							<option value="">--请选择应用--</option>
						</select>
					</td>
				</tr>
				<tr style="height: 40px;">
					<td class="add_brand_td" style="vertical-align:top;padding-top:5px;"><span style="color:red;">*</span>TYPE：</td>
					<td>
						<select id="taskType" name="taskType">
							<option value="">--请选择type--</option>
							<c:forEach items="${templateTypes }" var="templateType">
								<option value="${templateType }">${templateType }</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr style="height: 40px;">
					<td class="add_brand_td" style="vertical-align:top;padding-top:5px;"><span style="color:red;">*</span>SUBTYPE：</td>
					<td>
						<select name="templateId" id="subType">
							<option value="">--请选择subType--</option>
						</select>
					</td>
				</tr>
				<tr style="height: 40px;">
					<td class="add_brand_td" style="vertical-align:top;padding-top:5px;"><span style="color:red;">*</span>开始时间：</td>
					<td>
					<input type="text" id="lastTime" class="dialog_input" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', readOnly:true})"/>
					</td>
				</tr>
			</table>
		</div>
	</div><!-- /.page-content -->
</body>
<script type="text/javascript">
	$(document).ready(function(){
		$('#channelId').change(function(){
			var channelId = $(this).val();
			if(channelId!==""){
				//ajax 方式获取channel对应的applications
				$.ajax({
					url: "${ctx }/system/application/getByChannelId/" + channelId,
					type: "POST",
					dataType: "json",
					success: function(data, textStatus){
						//alert(data);
						var options = "<option value=''>--请选择应用--</option>";
						if (data != null) {
							for (var i = 0; i < data.length; i++) {
								var option = "<option value='" + data[i].id + "'>" + data[i].name + "</option>";
								options += option;
							}
						}
						$("#applicaitonId").html(options);
					},
					error: function(){
						alert("请求应用信息失败!!");
					}
				});
			}
		});
		
		$('#taskType').change(function(){
			var type = $(this).val();
			//ajax 方式获取channel对应的applications
			$.ajax({
				url: "${ctx }/system/task/template/getByType",
				type: "POST",
				data: {
					taskType: type
				},
				dataType: "json",
				success: function(data, textStatus){
					//alert(data);
					var options = "<option value=''>--请选择subType--</option>";
					if (data != null) {
						for (var i = 0; i < data.length; i++) {
							var option = "<option value='" + data[i].subType + "'>" + data[i].subType + "</option>";
							options += option;
						}
					}
					$("#subType").html(options);
				},
				error: function(){
					alert("请求应用信息失败!!");
				}
			});
		});
	});
	
	function changeLastTime(applicationId, templateId) {
		$("#appId").val(applicationId);
		$("#tempId").val(templateId);
		$("#cronTimeUpdate").removeClass("hidden").dialog({
		    modal: true,
		    title: "修改调度上次运行时间",
			resizable: true, 
			height:280,
			width:400,
			buttons: [
				{
					text: "修改",
					"class" : "btn btn-xs",
					click: function() {
						var lastTime = $("#newLastTime").val();
						if (null != lastTime && "" != lastTime) {
							$.ajax({
	                           url:"${ctx }/system/schedule/crontime/update",
	                           type:"POST",
	                           data:{
	                        	   "applicationId":applicationId,
	                        	   "templateId":templateId,
	                        	   "lastTime": lastTime
	                           },
	                           success:function(){
	                        	   alert("更新成功");
	                           }
							});
						}
						$(this).dialog( "close" ); 
					} 
				},
				{
					text: "取消",
					"class" : "btn btn-primary btn-xs",
					click: function() {
						$( this ).dialog( "close" ); 
					} 
				}
			]
	    });
	}
	
	function addLastTime() {
		$("#cronTimeAdd").removeClass("hidden").dialog({
		    modal: true,
		    title: "创建调度上次运行时间",
			resizable: true, 
			height:330,
			width:400,
			buttons: [
				{
					text: "创建",
					"class" : "btn btn-xs",
					click: function() {
						var lastTime = $("#lastTime").val();
						var applicationId = $("#applicaitonId").val();
						var taskType = $("#taskType").val();
						var subType = $("#subType").val();
						if (null != lastTime && "" != lastTime) {
							$.ajax({
	                           url:"${ctx }/system/schedule/crontime/add",
	                           type:"POST",
	                           data:{
	                        	   "applicationId":applicationId,
	                        	   "taskType":taskType,
	                        	   "subType":subType,
	                        	   "lastTime": lastTime
	                           },
	                           success:function(){
	                        	   alert("更新成功");
	                           }
							});
						}
						$(this).dialog( "close" ); 
					} 
				},
				{
					text: "取消",
					"class" : "btn btn-primary btn-xs",
					click: function() {
						$( this ).dialog( "close" ); 
					} 
				}
			]
	    });
	}
</script>
</html>