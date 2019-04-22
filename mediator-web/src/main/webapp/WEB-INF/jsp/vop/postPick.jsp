<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/common/taglibs.jspf" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<mediator:contentHeader title="Mediator后台管理系统-渠道管理"/>
	<script type="text/javascript" src="${ctx }/static/bootstrap/fuelux/fuelux.spinner.min.js"></script>
</head>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<script type="text/javascript">
var ctxRoot = '<%=basePath%>';

function postInfo(){
	var appid = $.trim($("input[name='appid']").val());
	var po_no = $.trim($("input[name='po_no']").val());
	var pick_no = $.trim($("input[name='pick_no']").val());
	//alert(appid+po_no+pick_no);
 	var post={appid:appid,po_no:po_no,pick_no:pick_no};
 	document.getElementById("pickdiv").innerHTML = "task执行中...!";
	$.ajax({
		type : "POST",
		url:"${ctx }/system/vop/postPick",
		dataType:'html',
		data:post,
		success:function(html){
				if("failure"==html){
					document.getElementById("pickdiv").innerHTML = "失败!";
				}else{
					document.getElementById("pickdiv").innerHTML = "成功!";
					//alert("成功");
				}
			}
	});	 
}

</script>

<body>
	<div class="breadcrumbs" id="breadcrumbs">
		<script type="text/javascript">
			try{ace.settings.check('breadcrumbs' , 'fixed')}catch(e){}
		</script>
		<ul class="breadcrumb">
			<li>
				<i class="icon-home home-icon"></i>
				<a href="#">唯品会</a>
			</li>
			<li>
				<a href="#">唯品会</a>
			</li>
			<li class="active">唯品会</li>
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
				唯品会
				<small>
					<i class="icon-double-angle-right"></i>
					发送pick
				</small>
			</h1>
		</div><!-- /.page-header -->
		<div class="row">
			<div class="col-xs-12">
				 <div id="pickdiv">
	appid：<input type="text" name="appid" id="appid"   /><br>
	po_no：<input type="text" name="po_no" id="po_no"   /><br>
	pick_no：<input type="text" name="pick_no" id="pick_no"  /><br>
	<input type="button" value="执行" onclick="postInfo();" style="height:25px;">
</div>
			
			</div><!-- /.col -->
		</div><!-- /.row -->
	</div><!-- /.page-content -->
</body>
</html>