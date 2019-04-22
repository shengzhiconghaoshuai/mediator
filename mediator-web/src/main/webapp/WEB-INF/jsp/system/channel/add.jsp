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
			$('#channelAddBtn').click(function(){
				$('#channelAddForm').submit();
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
				<a href="#">渠道</a>
			</li>
			<li class="active">渠道列表</li>
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
					渠道列表
				</small>
			</h1>
		</div><!-- /.page-header -->
		<div class="row">
			<div class="col-xs-12">
				<form class="form-horizontal" role="form" action="${ctx }/system/channel/add" method="post" id="channelAddForm">
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="channelName"> 渠道名称 </label>
						<div class="col-sm-9">
							<input type="text" name="name" id="channelName" placeholder="渠道名称" class="col-xs-10 col-sm-5" />
						</div>
					</div>
					<div class="space-4"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="channelCode"> 渠道代号 </label>
						<div class="col-sm-9">
							<input type="text" name="code" id="channelCode" placeholder="渠道代号" class="col-xs-10 col-sm-5" />
						</div>
					</div>
					<div class="space-4"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="priority"> 优先级 </label>
						<div class="col-sm-9">
							<input type="text" name="priority" class="input-mini" id="priority" />
						</div>
					</div>
					<div class="space-4"></div>
					<div class="clearfix form-actions">
						<div class="col-md-offset-3 col-md-9">
							<button class="btn btn-info" type="button" id="channelAddBtn">
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