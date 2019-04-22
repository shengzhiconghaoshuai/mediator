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
			$('#scheduleUpdateBtn').click(function(){
				$('#scheduleUpdateForm').submit();
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
			<li class="active">修改</li>
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
					调度修改
				</small>
			</h1>
		</div><!-- /.page-header -->
		<div class="row">
			<div class="col-xs-12">
				<form class="form-horizontal" role="form" action="${ctx }/system/schedule/update" method="post" id="scheduleUpdateForm">
					<input type="hidden" name="id" value="${cronConfig.id }">
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="description"> 描述 </label>
						<div class="col-sm-4">
							<textarea class="form-control limited" name="description" id="description" maxlength="50">${cronConfig.description }</textarea>
						</div>
					</div>
					<div class="space-4"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="className"> job类名称 </label>
						<div class="col-sm-9">
							<input type="text" name="className" id="className" placeholder="job类名称" class="col-xs-10 col-sm-5" value="${cronConfig.className }"/>
						</div>
					</div>
					<div class="space-4"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="global"> 全局 </label>
						<div class="col-sm-3">
							<select class="form-control" id="global" name="global">
								<option value="0" <c:if test="${cronConfig.global eq 0 }">selected='selected'</c:if>>否</option>
								<option value="1" <c:if test="${cronConfig.global eq 1 }">selected='selected'</c:if>>是</option>
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
									<c:forEach items="${cronConfig.params }" var="params">
										<c:choose>
											<c:when test="${params.paramName == 'CHANNEL_ID' }">
												<c:choose>
													<c:when test="${params.paramValue == channel.id }">
														<option value="${channel.id }" selected='selected'>${channel.name }</option>
													</c:when>
													<c:otherwise>
														<option value="${channel.id }">${channel.name }</option>
													</c:otherwise>
												</c:choose>
											</c:when>
										</c:choose>
									</c:forEach>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="space-4"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right">应用</label>
						<div class="col-sm-5" id="applications">
							<!-- 这边这个选中有点复杂,使用自定义函数解决 -->
							<c:set var="selectedAppIds" value=""></c:set>
							<c:forEach items="${cronConfig.params }" var="params">
								<c:if test="${params.paramName == 'APPLICATION_ID' }">
									<c:set var="selectedAppIds" value="${params.paramValue }"></c:set>
								</c:if>
							</c:forEach>
							<c:forEach items="${applications }" var="application">
								<div class="checkbox" style="float: left;">
									<label>
										<input name="applicationIds" class="ace ace-checkbox-2" type="checkbox" value="${application.id }" <c:if test="${m:checked(application.id, selectedAppIds) }">checked="checked"</c:if>/>
										<span class="lbl">${application.name }</span>
									</label>
								</div>
							</c:forEach>
						</div>
					</div>
					<div class="space-4"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="status"> 是否是cron </label>
						<div class="col-sm-3">
							<select class="form-control" id="cron" name="cron">
								<option value="0" <c:if test="${cronConfig.cron eq 0 }">selected='selected'</c:if>>否</option>
								<option value="1" <c:if test="${cronConfig.cron eq 1 }">selected='selected'</c:if>>是</option>
							</select>
						</div>
					</div>
					<div class="space-4"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="expression"> cron表达式 </label>
						<div class="col-sm-9">
							<input type="text" name="expression" id="expression" placeholder="cron表达式" class="col-xs-10 col-sm-5" value="${cronConfig.expression }"/>
						</div>
					</div>
					<div class="space-4"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="status"> 状态 </label>
						<div class="col-sm-3">
							<select class="form-control" id="status" name="status">
								<option value="0" <c:if test="${cronConfig.status eq 0 }">selected='selected'</c:if>>关闭</option>
								<option value="1" <c:if test="${cronConfig.status eq 1 }">selected='selected'</c:if>>启用</option>
							</select>
						</div>
					</div>
					<div class="space-4"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="channelCode"> 参数 </label>
						<div class="col-sm-9" id="paramContainer">
							<c:set var="count" value="0"></c:set>
							<c:forEach items="${cronConfig.params }" var="params">
								<c:if test="${params.paramName != 'APPLICATION_ID' and params.paramName != 'CHANNEL_ID' }">
									<c:set var="count" value="${count + 1 }"></c:set>
									<div style="margin-bottom: 2px;">
										<input type="text" class="input-small" name="paramName" value="${params.paramName }"/>
										－
										<input type="text" class="input-small" name="paramValue" value="${params.paramValue }"/>
										<c:if test="${count eq 1 }">
											<span class="btn btn-warning btn-xs" id="addParam">
												<i class="icon-plus-sign  bigger-110 icon-only"></i>
											</span>
										</c:if>
									</div>
								</c:if>
							</c:forEach>
						</div>
					</div>
					<div class="space-4"></div>
					<div class="clearfix form-actions">
						<div class="col-md-offset-3 col-md-9">
							<button class="btn btn-info" type="button" id="scheduleUpdateBtn">
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