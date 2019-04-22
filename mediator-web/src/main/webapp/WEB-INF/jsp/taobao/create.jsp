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
				<a href="#">淘宝</a>
			</li>
			<li>
				<a href="#">task</a>
			</li>
			<li class="active">task创建</li>
		</ul><!-- .breadcrumb -->
	</div>
	<div class="page-content">
		<div class="page-header">
			<h1>
				淘宝
				<small>
					<i class="icon-double-angle-right"></i>
					task创建
				</small>
			</h1>
		</div><!-- /.page-header -->
		<div class="row">
			<div class="col-xs-12">
				<!-- PAGE CONTENT BEGINS -->
				<div class="row">
					<div class="col-xs-12">
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
									通知类型
									<select id="topic" name="topic">
										<option value="">--请选择type--</option>
										<!-- 交易 -->
										<option value="taobao_trade_TradeBuyerPay">交易付款</option>
										<option value="taobao_trade_TradeBuyerStepPay">预售付定金</option>
										<!-- 退款 -->
										<option value="taobao_refund_RefundCreated">退款创建</option>
										<option value="taobao_refund_RefundBuyerModifyAgreement ">退款修改</option>
										<option value="taobao_refund_RefundSuccess">退款成功</option>
										<option value="taobao_refund_RefundClosed">退款关闭</option>
									</select>
								</span>
								<span class="input-icon condition">
									dataId
									<input type="text" id="dataId">
								</span>
								<span class="input-icon condition">
									<button id="create">创建</button>
								</span>
							</div>
						</div>
						<div class="space-4"></div>
					</div><!-- /span -->
				</div><!-- /row -->
			</div><!-- /.col -->
		</div><!-- /.row -->
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
		
		$('#create').click(function(){
			var topic = $("#topic").val();
			var applicationId = $("#applicaitonId").val();
			var dataId = $("#dataId").val();
			if (null == topic || "" == topic) {
				alert("topic不能为空!!");
				return false;
			}
			if (null == applicationId || "" == applicationId) {
				alert("applicationId不能为空!!");
				return false;
			}
			if (null == dataId || "" == dataId) {
				alert("dataId不能为空!!");
				return false;
			}
			var content = "";
			var t = topic.split("_");
			if (t[1] == "refund") {
				content = "{\"refund_id\":" + dataId + "}";
			} else if (t[1] == "trade") {
				content = "{\"tid\":" + dataId + "}";
			}
			if ("" == content) {
				alert("内容不能为空!!");
				return false;
			}
			$.ajax({
				url: "${ctx }/system/taobao/order/create",
				type: "get",
				dataType: "text",
				data: {
					"topic": topic,
					"applicationId": applicationId,
					"content": content
				},
				success: function(data, textStatus){
					console.log(data);
					alert("创建成功");
				},
				error: function(){
					alert("请求应用信息失败!!");
				}
			});
		});
	});
</script>
</html>