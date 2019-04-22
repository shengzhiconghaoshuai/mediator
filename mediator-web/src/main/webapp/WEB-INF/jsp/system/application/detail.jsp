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
			$('#priority').ace_spinner({value:${application.priority},min:0,max:100,step:1, on_sides: true, icon_up:'icon-plus smaller-75', icon_down:'icon-minus smaller-75', btn_up_class:'btn-success' , btn_down_class:'btn-danger'});
			$('#applicationUpdateBtn').click(function(){
				$('#applicationUpdateForm').submit();
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
				<a href="#">应用</a>
			</li>
			<li class="active">详情</li>
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
					应用详情
				</small>
			</h1>
		</div><!-- /.page-header -->
		<div class="row">
			<div class="col-xs-12">
				<form class="form-horizontal" role="form" action="${ctx }/system/application/update" method="post" id="applicationUpdateForm">
					<input type="hidden" name="id" value="${application.id }">
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="channelName"> 渠道名称 </label>
						<div class="col-sm-9">
							<input type="text" readonly="readonly" name="name" id="channelName" class="col-xs-10 col-sm-5" value="${channel.name }"/>
						</div>
					</div>
					<div class="space-4"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="applicationName"> 应用名称 </label>
						<div class="col-sm-9">
							<input type="text" name="name" readonly="readonly" id="applicationName" class="col-xs-10 col-sm-5" value="${application.name }"/>
						</div>
					</div>
					<div class="space-4"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="applicationCode"> 应用代号 </label>
						<div class="col-sm-9">
							<input type="text" name="code" readonly="readonly" id="applicationCode" class="col-xs-10 col-sm-5" value="${application.code }"/>
						</div>
					</div>
					<div class="space-4"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="storeId"> store id </label>
						<div class="col-sm-9">
							<input type="text" name="storeId" readonly="readonly" id="storeId" class="col-xs-10 col-sm-5" value="${application.storeId }"/>
						</div>
					</div>
					<div class="space-4"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="nick"> 昵称 </label>
						<div class="col-sm-9">
							<input type="text" name="nick" readonly="readonly" id="nick" class="col-xs-10 col-sm-5" value="${application.nick }"/>
						</div>
					</div>
					<div class="space-4"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="priority"> 优先级 </label>
						<div class="col-sm-9">
							<input type="text" readonly="readonly" name="priority" class="input-mini" id="priority" value="${application.priority }"/>
						</div>
					</div>
					<div class="space-4"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="status"> 状态 </label>
						<div class="col-sm-9">
							<input type="text" readonly="readonly" name="status" class="input-mini" id="status" value='<c:if test="${application.status eq 0 }">关闭</c:if><c:if test="${application.status eq 1 }">启用</c:if>'/>
						</div>
					</div>
					<div class="space-4"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="createTime"> 创建时间 </label>
						<div class="col-sm-9">
							<input type="text" name="createTime" id="createTime" class="col-xs-10 col-sm-5" readonly="readonly" value='<fmt:formatDate value="${application.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/>'/>
						</div>
					</div>
					<div class="space-4"></div>
					<hr />
					<div class="space-4"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="appKey"> appKey </label>
						<div class="col-sm-9">
							<input type="text" readonly="readonly" name="param.appKey" id="appKey" class="col-xs-10 col-sm-5" value="${application.param.appKey }"/>
						</div>
					</div>
					<div class="space-4"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="appSecret"> appSecret </label>
						<div class="col-sm-9">
							<input type="text" readonly="readonly" name="param.appSecret" id="appSecret" class="col-xs-10 col-sm-5" value="${application.param.appSecret }"/>
						</div>
					</div>
					<div class="space-4"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="appUrl"> appUrl </label>
						<div class="col-sm-9">
							<input type="text" readonly="readonly" name="param.appUrl" id="appUrl" class="col-xs-10 col-sm-5" value="${application.param.appUrl }"/>
						</div>
					</div>
					<div class="space-4"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="sessionKey"> sessionKey </label>
						<div class="col-sm-9">
							<input type="text" readonly="readonly" name="param.sessionKey" id="sessionKey" class="col-xs-10 col-sm-5" value="${application.param.sessionKey }"/>
						</div>
					</div>
					<div class="space-4"></div>
					<hr />
					<div class="space-4"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="vendorId"> vendorId </label>
						<div class="col-sm-9">
							<input type="text" readonly="readonly" name="param.vendorId" id="vendorId" class="col-xs-10 col-sm-5" value="${application.param.vendorId }"/>
						</div>
					</div>
					<div class="space-4"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="vendorName"> vendorName </label>
						<div class="col-sm-9">
							<input type="text" readonly="readonly" name="param.vendorName" id="vendorName" class="col-xs-10 col-sm-5" value="${application.param.vendorName }"/>
						</div>
					</div>
					<div class="space-4"></div>
				</form>
			</div><!-- /.col -->
		</div><!-- /.row -->
	</div><!-- /.page-content -->
</body>
</html>