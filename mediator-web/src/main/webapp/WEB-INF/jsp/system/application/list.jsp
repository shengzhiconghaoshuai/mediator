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
				<a href="#">应用</a>
			</li>
			<li class="active">应用列表</li>
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
					应用列表
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
										<th>名称</th>
										<th>代号</th>
										<th>
											<i class="icon-time bigger-110 hidden-480"></i>
											创建时间
										</th>
										<th>store id</th>
										<th>昵称</th>
										<th>
											优先级
										</th>
										<th class="hidden-480">状态</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${applications }" var="application">
										<tr>
											<td class="center">
												<label>
													<input type="checkbox" class="ace" />
													<span class="lbl"></span>
												</label>
											</td>
											<td>
												<a href="#">${application.name }</a>
											</td>
											<td>${application.code }</td>
											<td class="hidden-480"><fmt:formatDate value="${application.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
											<td>${application.storeId }</td>
											<td>${application.nick }</td>
											<td>${application.priority }</td>
											<td class="hidden-480">
												<c:choose>
													<c:when test="${application.status eq 0 }">
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
													<a href="${ctx }/system/application/detail/${application.id}" onclick="javascript:void(0);" style="text-decoration: none;">
														<button class="btn btn-xs btn-success">
															<i class="icon-eye-open bigger-120" title="详情"></i>
														</button>
													</a>
													<a href="${ctx }/system/application/update/${application.id}" onclick="javascript:void(0);" style="text-decoration: none;">
														<button class="btn btn-xs btn-info">
															<i class="icon-edit bigger-120" title="编辑"></i>
														</button>
													</a>
													<a style="text-decoration: none;">
														<button class="btn btn-xs btn-danger">
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
					</div><!-- /span -->
				</div><!-- /row -->
			</div><!-- /.col -->
		</div><!-- /.row -->
	</div><!-- /.page-content -->
</body>
</html>