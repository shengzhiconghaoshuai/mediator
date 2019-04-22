<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/common/taglibs.jspf" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<mediator:contentHeader title="Mediator后台管理系统-渠道管理"/>
	<script type="text/javascript" src="${ctx }/static/bootstrap/fuelux/fuelux.spinner.min.js"></script>
	<%-- <script type="text/javascript" src="${ctx }/static/js/environment.js"></script> --%>
	
	<link rel="stylesheet" type="text/css" href="${ctx }/static/json/jsoneditor.min.css">
  	<script type="text/javascript" src="${ctx }/static/json/jsoneditor.min.js"></script>

  	<!-- ace editor -->
  	<script type="text/javascript" src="${ctx }/static/json/asset/ace/ace.js"></script>

  	<!-- json lint -->
  	<script type="text/javascript" src="${ctx }/static/json/asset/jsonlint/jsonlint.js"></script>
	
	<script type="text/javascript">
		$(document).ready(function(){
			var container = document.getElementById('dataContainer');
			var options = {
				mode: 'text',
				modes: ['code', 'form', 'text', 'tree', 'view'], // allowed modes
				error: function (err) {
				alert(err.toString());
				}
			};
			var json = ${task['DATA'] };
			var editor = new JSONEditor(container, options, json);
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
				<a href="#">task</a>
			</li>
			<li class="active">编辑</li>
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
					task编辑
				</small>
			</h1>
		</div><!-- /.page-header -->
		<div class="row">
			<div class="col-xs-12">
				<form class="form-horizontal" role="form" action="${ctx }/system/application/update" method="post" id="applicationUpdateForm">
					<input type="hidden" name="id" value="${task['TASK_ID'] }">
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="channelName"> 渠道名称 </label>
						<div class="col-sm-9">
							<input type="text" readonly="readonly" name="channelName" id="channelName" class="col-xs-10 col-sm-5" value="${task['CNAME'] }"/>
						</div>
					</div>
					<div class="space-4"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="applicationName"> 应用名称 </label>
						<div class="col-sm-9">
							<input type="text" readonly="readonly" name="name" id="applicationName" class="col-xs-10 col-sm-5" value="${task['ANAME'] }"/>
						</div>
					</div>
					<div class="space-4"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="type"> TYPE </label>
						<div class="col-sm-9">
							<input type="text" name="type" id="type" class="col-xs-10 col-sm-5" value="${task['TYPE'] }"/>
						</div>
					</div>
					<div class="space-4"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="subType"> subType </label>
						<div class="col-sm-9">
							<input type="text" name="subType" id="subType" class="col-xs-10 col-sm-5" value="${task['SUBTYPE'] }"/>
						</div>
					</div>
					<div class="space-4"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="dataId"> DATAID </label>
						<div class="col-sm-9">
							<input type="text" name="dataId" id="dataId" class="col-xs-10 col-sm-5" value="${task['DATAID'] }"/>
						</div>
					</div>
					<div class="space-4"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="startTime"> 开始时间 </label>
						<div class="col-sm-9">
							<input type="text" name="startTime" class="col-xs-10 col-sm-5" id="startTime" value='<fmt:formatDate value="${task['STARTTIME'] }" pattern="yyyy-MM-dd HH:mm:ss"/>'/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="endTime"> 结束时间 </label>
						<div class="col-sm-9">
							<input type="text" name="endTime" class="col-xs-10 col-sm-5" id="endTime" value='<fmt:formatDate value="${task['ENDTIME'] }" pattern="yyyy-MM-dd HH:mm:ss"/>'/>
						</div>
					</div>
					<div class="space-4"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="status"> 状态 </label>
						<div class="col-sm-9">
							<input type="text" name="status" id="status" class="col-xs-10 col-sm-5" value="${task['STATUS'] }"/>
						</div>
					</div>
					<div class="space-4"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="dataContainer"> DATA </label>
						<div class="col-sm-5" id="dataContainer" style="height: 200px;">
							
						</div>
					</div>
					<div class="space-4"></div>
					<div class="clearfix form-actions">
						<div class="col-md-offset-3 col-md-9">
							<button class="btn btn-info" type="button" id="applicationUpdateBtn">
								<i class="icon-ok bigger-110"></i>
								修改
							</button>
							&nbsp; &nbsp; &nbsp;
							<button class="btn" type="reset">
								<i class="icon-undo bigger-110"></i>
								立即重跑
							</button>
						</div>
					</div>
				</form>
			</div><!-- /.col -->
		</div><!-- /.row -->
	</div><!-- /.page-content -->
</body>
</html>