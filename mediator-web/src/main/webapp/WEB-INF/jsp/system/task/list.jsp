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
				<a href="#">task</a>
			</li>
			<li class="active">task列表</li>
		</ul><!-- .breadcrumb -->
	</div>
	<div class="page-content">
		<div class="page-header">
			<h1>
				系统
				<small>
					<i class="icon-double-angle-right"></i>
					task列表
				</small>
			</h1>
		</div><!-- /.page-header -->
		<div class="row">
			<div class="col-xs-12">
				<!-- PAGE CONTENT BEGINS -->
				<div class="row">
					<div class="col-xs-12">
						<!-- form -->
						<form class="form-horizontal" role="form" id="taskQueryForm">
							<div class="form-group">
								<div class="col-sm-12">
									<!-- channel & application -->
									<span class="input-icon condition">
										渠道
										<select class="" id="channelId" name="channelId">
											<option value="">--请选择渠道--</option>
											<c:forEach items="${channels }" var="channel">
												<option value="${channel.id }">${channel.name }</option>
											</c:forEach>
										</select>
									</span>
									<span class="input-icon condition">
										应用
										<select name="applicaitonId" id="applicaitonId">
											<option value="">--请选择应用--</option>
										</select>
									</span>
									<!-- type & subType -->
									<span class="input-icon condition">
										type
										<select id="taskType" name="taskType">
											<option value="">--请选择type--</option>
											<c:forEach items="${templateTypes }" var="templateType">
												<option value="${templateType }">${templateType }</option>
											</c:forEach>
										</select>
									</span>
									<span class="input-icon condition">
										subType
										<select name="templateId" id="subType">
											<option value="">--请选择subType--</option>
										</select>
									</span>
									
									<!-- 状态 -->
									<span class="input-icon condition">
										状态
										<select name="status" id = "status">
											<option value="">--请选择状态--</option>
											<option value="1">新建</option>
											<option value="2">运行</option>
											<option value="3">挂起</option>
											<option value="4">失败</option>
											<option value="5">成功</option>
										</select>
									</span>
								</div>
							</div>
							<div class="space-4"></div>
							<div class="form-group">
								<div class="col-sm-12">
									<!-- time -->
									<span class="input-icon condition">
										起始时间
										<input type="text" id="startTime" name="startTime" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', maxDate:'#F{$dp.$D(\'endTime\')}', readOnly:true})"/>
									</span>
									<span class="input-icon condition">
										截止时间
										<input type="text" id="endTime" name="endTime" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', maxDate:'%y-%M-%d %H:%m:%s', minDate:'#F{$dp.$D(\'startTime\')}', readOnly:true})"/>
									</span>
									
									<!-- dada id -->
									<span class="input-icon condition">
										dataId
										<input type="text" id="dataId" name="dataId"/>
									</span>
									
									<!-- 查询 -->
									<span class="input-icon condition">
										<button class="btn btn-info" type="button" id="taskQueryBtn">
											<i class="icon-ok bigger-110"></i>
											查询
										</button>
									</span>
										<span class="input-icon condition">
										<button class="btn btn-info" type="button" id="taskclearBtn">
											<i class="icon-ok bigger-110"></i>
										         重置
										</button>
									</span>
								</div>
							</div>
						</form>
					</div><!-- /span -->
				</div><!-- /row -->
				
				<div style="margin: 20px;" id="applicationUpdateForm" class="hidden">
						<table class="add_brand_table " style="width:100%;">
							<tr style="height: 40px;" >
								<td class="add_brand_td" style="vertical-align:middle;"><span style="color:red;">*</span>渠道名称:</td>
								<td>
								<input type="text" id="formchannelName" class="dialog_input" value=""  disabled="disabled"/>
								</td>
							</tr>
							<tr style="height: 40px;"> 
								<td class="add_brand_td" style="vertical-align:top;padding-top:5px;"><span style="color:red;">*</span>应用名称：</td>
								<td>
								<input type="text" id="formapplicationName" class="dialog_input"  value="" disabled="disabled"/>
								</td>
							</tr>
							<tr style="height: 40px;">
								<td class="add_brand_td" style="vertical-align:top;padding-top:5px;"><span style="color:red;">*</span>TYPE：</td>
								<td>
								<input type="text" id="formtype" class="dialog_input"  value="" disabled="disabled"/>
								</td>
							</tr>
							<tr style="height: 40px;">
								<td class="add_brand_td" style="vertical-align:top;padding-top:5px;"><span style="color:red;">*</span>subType：</td>
								<td>
								<input type="text" id="formsubType" class="dialog_input"  value="" disabled="disabled"/>
								</td>
							</tr>
							<tr style="height: 40px;">
								<td class="add_brand_td" style="vertical-align:top;padding-top:5px;"><span style="color:red;">*</span>DATAID：</td>
								<td>
								<input type="text" id="formdataId" class="dialog_input"  value="" disabled="disabled"/>
								</td>
							</tr>
							<tr style="height: 40px;">
								<td class="add_brand_td" style="vertical-align:top;padding-top:5px;"><span style="color:red;">*</span>开始时间：</td>
								<td>
								<input type="text" id="formstartTime" class="dialog_input"  value="" disabled="disabled" />
								</td>
							</tr>
							<tr style="height: 40px;">
								<td class="add_brand_td" style="vertical-align:top;padding-top:5px;"><span style="color:red;">*</span>结束时间：</td>
								<td>
								<input type="text" id="formendTime" class="dialog_input"  value="" disabled="disabled" />
								</td>
							</tr>
							<tr style="height: 40px;">
								<td class="add_brand_td" style="vertical-align:top;padding-top:5px;"><span style="color:red;">*</span>状态：</td>
								<td>
								<input type="text" id="formstatus" class="dialog_input"  value="" disabled="disabled" />
								</td>
							</tr>
							<tr style="height: 40px;">
								<td class="add_brand_td" style="vertical-align:top;padding-top:5px;"><span style="color:red;">*</span>DATA：</td>
								<td>
								<textarea style="height: 200px; width: 400px" id = "formdataContainer"></textarea>
								</td>
							</tr>
						</table>
				</div>
				<div class="row">
					<div class="col-xs-12">
						<table id ="grid-table"></table>
						<div id ="grid-pager"></div>
					</div><!-- /span -->
				</div><!-- /row -->
				
			</div><!-- /.col -->
		</div><!-- /.row -->
	</div><!-- /.page-content -->
</body>
<script type="text/javascript">
var grid_selector = "#grid-table";
var pager_selector = "#grid-pager";
$(document).ready(function(){
	jQuery(grid_selector).jqGrid({
		url:"${ctx }/system/task/list",
		mtype: "POST", 					 
		datatype: "json",
		height: "400",
		colNames:['taskId','渠道','应用','类型','子类型','开始时间','结束时间','DATAID','状态','操作'],
		colModel:[
            {name:'taskId',index:'taskId',sortable:false,align:'center',width:'195',hidden:true},
		    {name:'cname',index:'cname',sortable:false,align:'center',width:'195'},
			{name:'aname',index:'aname', sortable:false,align:'center',width:'165'},							
			{name:'type',index:'type', sortable:false,align:'center',width:'175'},
			{name:'subtype',index:'subtype', sortable:false,align:'center',width:'155'},		
			{name:'startTime',index:'startTime', sortable:false,align:'center',width:'155'},		
			{name:'endTime',index:'endTime', sortable:false,align:'center',width:'155'},		
			{name:'dataId',index:'dataId', sortable:false,align:'center',width:'155'},		
			{name:'status',index:'status', sortable:false,align:'center',width:'155'},
			{name:'ACTION',index:'action', sortable:false,align:'center',width:'155'}
		],
		viewrecords : true,
		rowNum:20,
		rowList:[20,30,40],
		pager : pager_selector,
		altRows: true,
		autowidth:true,
		multiselect: false,
	    multiboxonly: true,
	    gridComplete: function() {
			var table = this;
			setTimeout(function(){
				updatePagerIcons(table);
				//enableTooltips(table);
			}, 0);
		    var ids = jQuery(grid_selector).jqGrid('getDataIDs');
			for (var i = 0; i < ids.length; i++) {
				var ret = jQuery(grid_selector).jqGrid('getRowData',ids[i]);
				jQuery(grid_selector).jqGrid('setRowData', ids[i], {								
					ACTION: "<button class='btn btn-xs btn-info' onclick = 'editForm("+ids[i]+")'><i title='编辑' class='icon-edit bigger-120'></i></button>"
				});
			}
		},
	});   
	
	function updatePagerIcons(table) {
		var replacement = 
		{
			'ui-icon-seek-first' : 'ace-icon fa fa-angle-double-left bigger-140',
			'ui-icon-seek-prev' : 'ace-icon fa fa-angle-left bigger-140',
			'ui-icon-seek-next' : 'ace-icon fa fa-angle-right bigger-140',
			'ui-icon-seek-end' : 'ace-icon fa fa-angle-double-right bigger-140'
		};
		$('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function(){
			var icon = $(this);
			var $class = $.trim(icon.attr('class').replace('ui-icon', ''));
			
			if($class in replacement) icon.attr('class', 'ui-icon '+replacement[$class]);
		});
	}
});

$(document).ready(function(){
	$("#taskclearBtn").click(function(){
		location.reload();
	});
	
	$('#taskQueryBtn').click(function(){
		var channelId = $("#channelId").val();
		var applicaitonId = $("#applicaitonId").val();
		var taskType = $("#taskType").val();
		var subType = $("#subType").val();
		var status = $("#status").val();
		var startTime = $("#startTime").val();
		var endTime = $("#endTime").val();
		var dataId = $("#dataId").val();
	    jQuery(grid_selector).jqGrid('setGridParam',
	    		    {postData:{
	    		    	"channelId":channelId,
	    		    	"applicaitonId":applicaitonId,
	    		    	"taskType":taskType,
	    		    	"subType":subType,
	    		    	"status":status,
	    		    	"startTime":startTime,
	    		    	"endTime":endTime,
	    		    	"dataId":dataId
	    		    }
			        }).trigger('reloadGrid');
	});
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

function editForm(id){
	var taskId = id;
	var ret = jQuery(grid_selector).jqGrid('getRowData',taskId);
	var status = ret.status;
	$("#formchannelName").val(ret.cname);
	$("#formapplicationName").val(ret.aname);
	$("#formtype").val(ret.type);
	$("#formsubType").val(ret.subtype);
	$("#formdataId").val(ret.dataId);
	$("#formstartTime").val(ret.startTime);
	$("#formendTime").val(ret.endTime);
	$("#formstatus").val(status);
	//$("#formdataContainer").val(ret.data);
	
	$.ajax({
	    url:"${ctx }/system/task/getdata/" + taskId,
	    type:"GET",
	    dataType: "text",
	    async: false,
	    success:function(data){
	    	$("#formdataContainer").val(data);
	    }
		
	});
	
	/* var container = document.getElementById('formdataContainer');
	var options = {
		mode: 'text',
		modes: ['code', 'form', 'text', 'tree', 'view'], // allowed modes
		error: function (err) {
		alert(err.toString());
		}
	};
	var json = ret.data;
	var editor = new JSONEditor(container, options, json); */
	
	//if(status=='4') {
	 	$("#applicationUpdateForm").removeClass("hidden").dialog({
		    modal: true,
		    title: "查看TASK详情",
			resizable: true, 
			height:680,
			width:600,
			buttons: [
				{
					text: "立即重跑",
					"class" : "btn btn-xs",
					click: function() {
						$.ajax({
                           url:"${ctx }/system/task/updateTask",
                           type:"POST",
                           data:{
                        	   "taskId":ret.taskId,
                        	   "data":$("#formdataContainer").val()
                           },
                           success:function(){
                        	   
                           }
							
						});
						$( this ).dialog( "close" ); 
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
	/* } else {
		$("#applicationUpdateForm").removeClass("hidden").dialog({
		    modal: true,
		    title: "查看TASK详情",
			resizable: true, 
			height:680,
			width:600,
			buttons: [
				{
					text: "取消",
					"class" : "btn btn-primary btn-xs",
					click: function() {
						$( this ).dialog( "close" ); 
					} 
				}
			]
	    });
	} */

}
</script>

</html>