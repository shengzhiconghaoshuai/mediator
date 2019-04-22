<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/common/taglibs.jspf" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<mediator:contentHeader title="Mediator后台管理系统"/>
	<mediator:calendar/>
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
				<a href="#">Home</a>
			</li>
			<li class="active">Calendar</li>
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
				日历
				<small>
					<i class="icon-double-angle-right"></i>
					with draggable and editable events
				</small>
			</h1>
		</div><!-- /.page-header -->
		<div class="row">
			<div class="col-xs-12">
				<!-- PAGE CONTENT BEGINS -->
				<div class="row">
					<div class="col-sm-9">
						<div class="space"></div>
						<div id="calendar"></div>
					</div>
					<div class="col-sm-3">
						<div class="widget-box transparent">
							<div class="widget-header">
								<h4>Draggable events</h4>
							</div>
							<div class="widget-body">
								<div class="widget-main no-padding">
									<div id="external-events">
										<div class="external-event label-grey" data-class="label-grey">
											<i class="icon-move"></i>
											My Event 1
										</div>
										<div class="external-event label-success" data-class="label-success">
											<i class="icon-move"></i>
											My Event 2
										</div>
										<div class="external-event label-danger" data-class="label-danger">
											<i class="icon-move"></i>
											My Event 3
										</div>
										<div class="external-event label-purple" data-class="label-purple">
											<i class="icon-move"></i>
											My Event 4
										</div>
										<div class="external-event label-yellow" data-class="label-yellow">
											<i class="icon-move"></i>
											My Event 5
										</div>
										<div class="external-event label-pink" data-class="label-pink">
											<i class="icon-move"></i>
											My Event 6
										</div>
										<div class="external-event label-info" data-class="label-info">
											<i class="icon-move"></i>
											My Event 7
										</div>
										<label>
											<input type="checkbox" class="ace ace-checkbox" id="drop-remove" />
											<span class="lbl"> Remove after drop</span>
										</label>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- PAGE CONTENT ENDS -->
			</div><!-- /.col -->
		</div><!-- /.row -->
	</div><!-- /.page-content -->
</body>
</html>