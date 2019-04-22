<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/common/taglibs.jspf" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<mediator:contentHeader title="Mediator后台管理系统-渠道管理"/>
	<%-- <script type="text/javascript" src="${ctx }/static/js/environment.js"></script> --%>
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
				<!-- PAGE CONTENT BEGINS -->
				<div class="row">
					<div class="col-xs-12">
						<div class="table-responsive">
							<table id="sample-table-1" class="table table-striped table-bordered table-hover">
								<thead>
									<tr>
										<th class="center">
											<label>
												<input type="checkbox" class="ace" />
												<span class="lbl"></span>
											</label>
										</th>
										<th>描述</th>
										<th>job类名</th>
										<th>
											<i class="icon-time bigger-110 hidden-480"></i>
											表达式
										</th>
										<th>
											全局
										</th>
										<th>
											cron
										</th>
										<th class="hidden-480">状态</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${cronConfigs }" var="cronConfig">
										<tr>
											<td class="center">
												<label>
													<input type="checkbox" class="ace" />
													<span class="lbl"></span>
												</label>
											</td>
											<td>
												<a href="#">${cronConfig.description }</a>
											</td>
											<td>${cronConfig.className }</td>
											<td class="hidden-480">${cronConfig.expression }</td>
											<td class="hidden-480">
												<c:choose>
													<c:when test="${cronConfig.global eq 0 }">
														<span class="label label-sm label-warning">
															否
														</span>
													</c:when>
													<c:otherwise>
														<span class="label label-sm label-success">
															是
														</span>
													</c:otherwise>
												</c:choose>
											</td>
											<td class="hidden-480">
												<c:choose>
													<c:when test="${cronConfig.cron eq 0 }">
														<span class="label label-sm label-warning">
															否
														</span>
													</c:when>
													<c:otherwise>
														<span class="label label-sm label-success">
															是
														</span>
													</c:otherwise>
												</c:choose>
											</td>
											<td class="hidden-480">
												<c:choose>
													<c:when test="${cronConfig.status eq 0 }">
														<span class="label label-sm label-warning">
															关闭
														</span>
													</c:when>
													<c:otherwise>
														<span class="label label-sm label-success">
															启用
														</span>
													</c:otherwise>
												</c:choose>
											</td>
											<td>
												<div class="visible-md visible-lg hidden-sm hidden-xs btn-group">
													<a href="${ctx }/system/schedule/update/${cronConfig.id}" onclick="javascript:void(0);" style="text-decoration: none;">
														<button class="btn btn-xs btn-info" id="channel_edit">
															<i class="icon-edit bigger-120" title="编辑"></i>
														</button>
													</a>
													<a href="${ctx }/system/schedule/delete/${cronConfig.id}" onclick="javascript:void(0);" style="text-decoration: none;">
														<button class="btn btn-xs btn-danger" id="channel_delete">
															<i class="icon-trash bigger-120" title="删除"></i>
														</button>
													</a>
												</div>
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div><!-- /.table-responsive -->
						<div class="clearfix form-actions">
							<div class="col-md-offset-1 col-md-9">
								<a href="${ctx }/system/schedule/add" onclick="javascript:void(0);" class="btn btn-app btn-light btn-xs">
									<i class="icon-plus-sign purple bigger-120"></i>
									添加调度
								</a>
							</div>
						</div>
					</div><!-- /span -->
				</div><!-- /row -->
			</div><!-- /.col -->
		</div><!-- /.row -->
	</div><!-- /.page-content -->
</body>
</html>