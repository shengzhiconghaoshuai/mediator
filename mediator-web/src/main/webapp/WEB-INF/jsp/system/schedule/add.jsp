<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/common/taglibs.jspf" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<mediator:contentHeader title="Mediator后台管理系统-渠道管理"/>
	<script type="text/javascript" src="${ctx }/static/bootstrap/fuelux/fuelux.spinner.min.js"></script>
	<%-- <script type="text/javascript" src="${ctx }/static/js/environment.js"></script> --%>
	<script type="text/javascript" src="${ctx }/static/My97DatePicker/WdatePicker.js"></script>
	<style type="text/css">
		.condition {
			margin: 3px 2%;
		}
	</style>
	<script type="text/javascript">
		$(document).ready(function(){
			$('#priority').ace_spinner({value:0,min:0,max:100,step:1, on_sides: true, icon_up:'icon-plus smaller-75', icon_down:'icon-minus smaller-75', btn_up_class:'btn-success' , btn_down_class:'btn-danger'});
			$('#scheduleAddBtn').click(function(){
				$('#scheduleAddForm').submit();
			});
			$('#channelId').change(function(){
				var channelId = $(this).val();
				//ajax 方式获取channel对应的applications
				$.ajax({
					url: "${ctx }/system/application/getByChannel/" + channelId,
					type: "POST",
					//dataType: "json",
					success: function(data, textStatus){
						$("#applications").html(data);
					},
					error: function(){
						alert("请求应用信息失败!!");
					}
				});
			});
			$("#addParam").click(function(){
				var dom = "<div style='margin-bottom: 2px;'>"
							+ "<input type='text' class='input-small' placeholder='参数名' name='paramName'/>"
							+ "&nbsp－&nbsp"
							+ "<input type='text' class='input-small' placeholder='参数值' name='paramValue'/>"
							+ "</div>";
				$("#paramContainer").children().last().after(dom);
			});
			
			$('#cron').change(function(){
				var cron = $(this).val();
				if (cron == "1") {
					$("#expression_div").show();
					$("#startTime_div").hide();
					//$("#endTime_div").hide();
					$("#repeatCount_div").hide();
					$("#repeatInterval_div").hide();
				} else {
					$("#expression_div").hide();
					$("#startTime_div").show();
					//$("#endTime_div").show();
					$("#repeatCount_div").show();
					$("#repeatInterval_div").show();
				}
			});
			
			$("#startTime_div").hide();
			$("#endTime_div").hide();
			$("#repeatCount_div").hide();
			$("#repeatInterval_div").hide();
		});
	</script>
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
					调度列表
				</small>
			</h1>
		</div><!-- /.page-header -->
		<div class="row">
			<div class="col-xs-12">
				<form class="form-horizontal" role="form" action="${ctx }/system/schedule/add" method="post" id="scheduleAddForm">
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="description"> 描述 </label>
						<div class="col-sm-4">
							<textarea class="form-control limited" name="description" id="description" maxlength="50"></textarea>
						</div>
					</div>
					<div class="space-4"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="className"> job类名称 </label>
						<div class="col-sm-9">
							<input type="text" name="className" id="className" placeholder="job类名称" class="col-xs-10 col-sm-5" />
						</div>
					</div>
					<div class="space-4"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="global"> 是否全局 </label>
						<div class="col-sm-3">
							<select class="form-control" id="global" name="global">
								<option value="0">否</option>
								<option value="1">是</option>
							</select>
						</div>
					</div>
					<div class="space-4"></div>
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
						<label class="col-sm-3 control-label no-padding-right" for="cron"> 是否是cron </label>
						<div class="col-xs-3">
							<select class="form-control" id="cron" name="cron">
								<option value="1">是</option>
								<option value="0">否</option>
							</select>
						</div>
					</div>
					<div class="space-4"></div>
					<div class="form-group" id="expression_div">
						<label class="col-sm-3 control-label no-padding-right" for="expression"> cron表达式 </label>
						<div class="col-sm-9">
							<input type="text" name="expression" id="expression" placeholder="cron表达式" class="col-xs-10 col-sm-5" />
						</div>
					</div>
					<div class="space-4"></div>
					<div class="form-group" id="startTime_div">
						<label class="col-sm-3 control-label no-padding-right" for="startTime"> 开始时间 </label>
						<div class="col-sm-9">
							<input type="text" name="startTime" id="startTime" class="col-xs-10 col-sm-5" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', minDate:'%y-%M-%d %H:%m:%s', readOnly:true})"/>
						</div>
					</div>
					<!-- <div class="space-4"></div>
					<div class="form-group" id="endTime_div">
						<label class="col-sm-3 control-label no-padding-right" for="endTime"> 结束时间 </label>
						<div class="col-sm-9">
							<input type="text" name="endTime" id="endTime" class="col-xs-10 col-sm-5" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', minDate:'#F{$dp.$D(\'startTime\')}', readOnly:true})"/>
						</div>
					</div> -->
					<div class="space-4"></div>
					<div class="form-group" id="repeatCount_div">
						<label class="col-sm-3 control-label no-padding-right" for="repeatCount"> 重复次数 </label>
						<div class="col-sm-9">
							<input type="text" name="repeatCount" id="repeatCount" placeholder="重复次数" class="col-xs-10 col-sm-5" />
						</div>
					</div>
					<div class="space-4"></div>
					<div class="form-group" id="repeatInterval_div">
						<label class="col-sm-3 control-label no-padding-right" for="repeatInterval"> 间隔(秒) </label>
						<div class="col-sm-9">
							<input type="text" name="repeatInterval" id="repeatInterval" placeholder="间隔(秒)" class="col-xs-10 col-sm-5" />
						</div>
					</div>
					<div class="space-4"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="channelCode"> 参数 </label>
						<div class="col-sm-9" id="paramContainer">
							<div style="margin-bottom: 2px;">
								<input type="text" class="input-small" placeholder="参数名" name="paramName"/>
								－
								<input type="text" class="input-small" placeholder="参数值" name="paramValue"/>
								<span class="btn btn-warning btn-xs" id="addParam">
									<i class="icon-plus-sign  bigger-110 icon-only"></i>
								</span>
							</div>
						</div>
					</div>
					<div class="space-4"></div>
					<div class="clearfix form-actions">
						<div class="col-md-offset-3 col-md-9">
							<button class="btn btn-info" type="button" id="scheduleAddBtn">
								<i class="icon-ok bigger-110"></i>
								提交
							</button>
							&nbsp; &nbsp; &nbsp;
							<button class="btn" type="reset">
								<i class="icon-undo bigger-110"></i>
								重置
							</button>
						</div>
					</div>
				</form>
			</div><!-- /.col -->
		</div><!-- /.row -->
	</div><!-- /.page-content -->
</body>
</html>