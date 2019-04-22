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
	<script type="text/javascript" src="${ctx }/static/jquery/js/ajaxfileupload.js"></script>
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
						
							<div class="form-group">
								<div class="col-sm-12">
									<span class="input-icon condition">
										渠道商品id
										<input type="text" id="channelProductId" name="channelProductId"/>
									</span>
									
									<span class="input-icon condition">
										外部商品id
										<input type="text" id="outer_id" name="outer_id"/>
									</span>
									
									<span class="input-icon condition">
										<button class="btn btn-info" type="button" id="QueryBtn">
											<i class="icon-ok bigger-110"></i>
											查询
										</button>
									</span>
									
									<span class="input-icon condition">
										<button class="btn btn-info" type="button" id="RefreshBtn">
											<i class="icon-ok bigger-110"></i>
											重置
										</button>
									</span>
									
									<span class="input-icon condition">
									<button id="importConfig"
										class='btn btn-info' >
										<i class="icon-check"></i>&nbsp;导入商品映射文件
									</button>
									</span>
								</div>
							</div>
						
					</div><!-- /span -->
				</div><!-- /row -->
				
				<div id="importJitConfig-dialog" style="display: none" title="导入商品映射文件">
						<div class="row">
							<div class="col-xs-12">
								<form class="form-horizontal" role="form" action="${ctx }/system/schedule/add" method="post" id="scheduleAddForm">
									<div class="form-group">
										<label class="col-sm-3 control-label no-padding-right" for="channelId"> 渠道 </label>
									<div class="col-sm-3">
										<select class="form-control" id="channelId" name="channelId">
											<option value="-1">--请选择渠道--</option>
											<c:forEach items="${channels }" var="channel">
												<option value="${channel.id }">${channel.name }</option>
											</c:forEach>
										</select>
									</div>
									</div>
									<div class="space-4"></div>
									<div class="form-group">
										<label class="col-sm-3 control-label no-padding-right">应用</label>
										<div class="col-sm-5" id="applications">
										</div>
									</div>
									<div class="space-4"></div>
									<div class="form-group">
										<label class="col-sm-3 control-label no-padding-right">选择文件</label>
										<div class="col-sm-5">
										<input type="file" id="jitfile_input" name="jitfile_input">
										</div>
									</div>
								</form>
							</div>
						</div>
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
		url:"${ctx }/system/productMapping/listData",
		mtype: "POST", 					 
		datatype: "json",
		height: "400",
		colNames:['mappingId','渠道商品ID','外部商品ID','类型','应用ID'],
		colModel:[
            {name:'id',index:'id',sortable:false,align:'center',width:'195',hidden:true,search:false},
		    {name:'channelProductId',index:'channel_product_id',sortable:false,align:'center',width:'195',search:true},
			{name:'outerId',index:'outerId', sortable:false,align:'center',width:'165',search:true},							
			{name:'type',index:'type', sortable:false,align:'center',width:'175',search:false},
			{name:'applicationId',index:'applicationId', sortable:false,align:'center',width:'155',search:false}
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
			}, 0);
		    /* var ids = jQuery(grid_selector).jqGrid('getDataIDs');
			for (var i = 0; i < ids.length; i++) {
				var ret = jQuery(grid_selector).jqGrid('getRowData',ids[i]);
				jQuery(grid_selector).jqGrid('setRowData', ids[i], {								
					ACTION: "<button class='btn btn-xs btn-info' onclick = 'editForm("+ids[i]+")'><i title='编辑' class='icon-edit bigger-120'></i></button>"
				});
			} */
		}
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

$(document).ready(function() {
	
		$("#QueryBtn").click(function(){
			var channelproductId = $("#channelProductId").val();
			var outer_id = $("#outer_id").val();
			
			jQuery("#grid-table").jqGrid('setGridParam', {
				postData : {
					"channelProductId":channelproductId,
					"outer_id":outer_id
				}
			}).trigger('reloadGrid');
		});
		
		$("#RefreshBtn").click(function(){
			jQuery("#grid-table").jqGrid('setGridParam', {
				postData : {
					"channelProductId":"",
					"outer_id":""
				}
			}).trigger('reloadGrid');
		});

		$("#importConfig").on('click',function(){
			$("#importJitConfig-dialog").removeClass('hide').dialog({
				modal : true,
				height : 400,
				width : 600,
				resizable : false,
				title : "导入商品映射文件",
				buttons : [ {
					html : "<i class='icon-cloud-upload'></i>上传",
					"class" : "btn btn-success btn-sm btn-next",
					click : function() {
						uploadJitConfig();
						$(this).dialog('close');
					}

				} ]
			})
		});
			

		$('#channelId').change(function() {
			var channelId = $(this).val();
			$.ajax({
				url : "${ctx }/system/application/getByChannel/" + channelId,
				type : "POST",
				success : function(data, textStatus) {
					$("#applications").html(data);
				},
				error : function() {
					alert("请求应用信息失败!!");
				}
			});
		});

		function uploadJitConfig() {

			var filename = $("#jitfile_input").val();
			var channelIds = $("#channelId").val();
			var applicationIds;
			$(":checkbox[name=applicationIds]").each(function() {
				if ($(this).prop("checked")) {
					applicationIds = $(this).val();
				}
			});

			if (filename == "") {
				alert("请选择文件!");
				return;
			}
			$.ajaxFileUpload({
				url : "${ctx }/system/productMapping/upload", //用于文件上传的服务器端请求地址
				secureuri : false, //是否需要安全协议，一般设置为false
				fileElementId : [ 'jitfile_input' ], //文件上传域的ID
				dataType : 'json', //返回值类型 一般设置为json
				data : {
					"channelId" : channelIds,
					"applicationId" : applicationIds
				},
				success : function(data, status) //服务器成功响应处理函数
				{
					if (data.code == "1") {
						$.unblockUI();
						alert("上传完成");
					} else if (data.code == "0" && data.message == null) {
						$.unblockUI();
						alert("系统异常");
					} else {
						$.unblockUI();
						alert("上传失败： \n" + data.message);
					}
				},
				error : function(data, status) {
					//服务器响应失败处理函数
					alert("服务器响应失败");
				},
			});
		}

	})
</script>

</html>