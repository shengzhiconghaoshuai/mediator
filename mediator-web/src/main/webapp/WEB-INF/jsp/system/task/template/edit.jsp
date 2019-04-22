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
			$('#priority').ace_spinner({value:${template.priority},min:0,max:100,step:1, on_sides: true, icon_up:'icon-plus smaller-75', icon_down:'icon-minus smaller-75', btn_up_class:'btn-success' , btn_down_class:'btn-danger'});
			$('#templateUpdateBtn').click(function(){
				$('#templateUpdateForm').submit();
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
				<a href="#">task模板</a>
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
					task模板修改
				</small>
			</h1>
		</div><!-- /.page-header -->
		<div class="row">
			<div class="col-xs-12">
				<form class="form-horizontal" role="form" action="${ctx }/system/task/template/update" method="post" id="templateUpdateForm">
					<input type="hidden" name="id" value="${template.id }">
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="type"> 类型 </label>
						<div class="col-sm-9">
							<input type="text" name="type" id="type" class="col-xs-10 col-sm-5" value="${template.type }"/>
						</div>
					</div>
					<div class="space-4"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="subType"> 子类型 </label>
						<div class="col-sm-9">
							<input type="text" name="subType" id="subType" class="col-xs-10 col-sm-5" value="${template.subType }"/>
						</div>
					</div>
					<div class="space-4"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="description"> 描述 </label>
						<div class="col-sm-4">
							<textarea class="form-control limited" name="description" id="description" maxlength="50">${template.description }</textarea>
						</div>
					</div>
					<div class="space-4"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="priority"> 优先级 </label>
						<div class="col-sm-9">
							<input type="text" name="priority" class="input-mini" id="priority" value="${template.priority }"/>
						</div>
					</div>
					<div class="space-4"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="status"> 状态 </label>
						<div class="col-sm-3">
							<select class="form-control" id="status" name="status">
								<option value="0" <c:if test="${template.status eq 0 }">selected='selected'</c:if>>关闭</option>
								<option value="1" <c:if test="${template.status eq 1 }">selected='selected'</c:if>>启用</option>
							</select>
						</div>
					</div>
					<div class="space-4"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="repeatable"> 重复 </label>
						<div class="col-sm-3">
							<select class="form-control" id="repeatable" name="repeatable">
								<option value="0" <c:if test="${template.repeatable eq 0 }">selected='selected'</c:if>>否</option>
								<option value="1" <c:if test="${template.repeatable eq 1 }">selected='selected'</c:if>>是</option>
							</select>
						</div>
					</div>
					<div class="space-4"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="reRun"> 重跑 </label>
						<div class="col-sm-3">
							<select class="form-control" id="reRun" name="reRun">
								<option value="0" <c:if test="${template.reRun eq 0 }">selected='selected'</c:if>>否</option>
								<option value="1" <c:if test="${template.reRun eq 1 }">selected='selected'</c:if>>是</option>
							</select>
						</div>
					</div>
					<div class="space-4"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="hung"> 挂起 </label>
						<div class="col-sm-3">
							<select class="form-control" id="hung" name="hung">
								<option value="0" <c:if test="${template.hung eq 0 }">selected='selected'</c:if>>否</option>
								<option value="1" <c:if test="${template.hung eq 1 }">selected='selected'</c:if>>是</option>
							</select>
						</div>
					</div>
					<div class="space-4"></div>
					<div class="clearfix form-actions">
						<div class="col-md-offset-3 col-md-9">
							<button class="btn btn-info" type="button" id="templateUpdateBtn">
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