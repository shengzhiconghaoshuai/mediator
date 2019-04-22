<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/common/taglibs.jspf" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<mediator:contentHeader title="Mediator后台管理系统-渠道管理"/>
	<script type="text/javascript" src="${ctx }/static/bootstrap/fuelux/fuelux.spinner.min.js"></script>
	<%-- <script type="text/javascript" src="${ctx }/static/js/environment.js"></script> --%>
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
						var dom;
						for(var i = 0;i < data.length;i++){
							dom += "<div class='checkbox' style='float: left;'>"
									+ "<label>"
									+	"<input name='applicationIds' class='ace ace-checkbox-2' type='checkbox' value='" + data[i].id + "'/>"
									+	"<span class='lbl'>" + data[i].name + "</span>"
									+ "</label>"
									+"</div>";
						}
						$("#applications").html(dom);
					},
					error: function(){
						alert("请求应用信息失败!!");
					}
				});
			});
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
							<!-- <div class="checkbox" style="float: left;">
								<label>
									<input name="applicationIds" class="ace ace-checkbox-2" type="checkbox" />
									<span class="lbl"> choice 3</span>
								</label>
							</div>
							<div class="checkbox" style="float: left;">
								<label>
									<input name="applicationIds" class="ace ace-checkbox-2" type="checkbox" />
									<span class="lbl"> choice 3</span>
								</label>
							</div>
							<div class="checkbox" style="float: left;">
								<label>
									<input name="applicationIds" class="ace ace-checkbox-2" type="checkbox" />
									<span class="lbl"> choice 3</span>
								</label>
							</div>
							<div class="checkbox" style="float: left;">
								<label>
									<input name="applicationIds" class="ace ace-checkbox-2" type="checkbox" />
									<span class="lbl"> choice 3</span>
								</label>
							</div>
							<div class="checkbox" style="float: left;">
								<label>
									<input name="applicationIds" class="ace ace-checkbox-2" type="checkbox" />
									<span class="lbl"> choice 3</span>
								</label>
							</div> -->
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="channelCode"> 是否是cron </label>
						<div class="col-xs-3">
							<div class="col-sm-9">
								<input name="cron" class="ace ace-switch ace-switch-2" type="checkbox" />
								<span class="lbl"></span>
							</div>
						</div>
					</div>
					<div class="space-4"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="channelCode"> cron表达式 </label>
						<div class="col-sm-9">
							<input type="text" name="expression" id="expression" placeholder="cron表达式" class="col-xs-10 col-sm-5" />
						</div>
					</div>
					<div class="space-4"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="channelCode"> 参数 </label>
						<div class="col-sm-9">
							<input type="text" name="params" id="params" placeholder="参数" class="col-xs-10 col-sm-5" />
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