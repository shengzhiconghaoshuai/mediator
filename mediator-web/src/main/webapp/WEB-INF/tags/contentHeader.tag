<%@tag pageEncoding="utf-8"%>
<%@attribute name="title" description="html title" type="java.lang.String" required="false"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>${title }</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<!-- basic styles -->
<link href="${ctx }/static/ace/css/bootstrap.min.css" rel="stylesheet" />
<link rel="stylesheet" href="${ctx }/static/ace/css/font-awesome.min.css" />

<!--[if IE 7]>
  <link rel="stylesheet" href="${ctx }/static/ace/css/font-awesome-ie7.min.css" />
<![endif]-->

<!-- page specific plugin styles -->
<!-- fonts -->
<!-- <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Open+Sans:400,300" /> -->

<!-- ace styles -->
<link rel="stylesheet" href="${ctx }/static/ace/css/ace.min.css" />
<link rel="stylesheet" href="${ctx }/static/ace/css/ace-rtl.min.css" />
<link rel="stylesheet" href="${ctx }/static/ace/css/ace-skins.min.css" />

<!--[if lte IE 8]>
  <link rel="stylesheet" href="${ctx }/static/ace/css/ace-ie.min.css" />
<![endif]-->

<!-- inline styles related to this page -->

<!-- ace settings handler -->
<script src="${ctx }/static/ace/js/ace-extra.min.js"></script>

<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
<script src="${ctx }/static/html5/js/html5shiv.js"></script>
<script src="${ctx }/static/ace/js/respond.min.js"></script>
<![endif]-->

<!-- basic scripts -->
<!--[if !IE]> -->
	<script src="${ctx }/static/jquery/js/jquery-2.0.3.min.js"></script>
<!-- <![endif]-->
<!--[if IE]>
	<script src="${ctx }/static/jquery/js/jquery-1.10.2.min.js"></script>
<![endif]-->
<!--[if !IE]> -->
	<script type="text/javascript">
		window.jQuery || document.write("<script src='${ctx }/static/jquery/js/jquery-2.0.3.min.js'>"+"<"+"script>");
	</script>
<!-- <![endif]-->
<!--[if IE]>
	<script type="text/javascript">
		window.jQuery || document.write("<script src='${ctx }/static/jquery/js/jquery-1.10.2.min.js'>"+"<"+"script>");
	</script>
<![endif]-->

<script src="${ctx }/static/bootstrap/js/bootstrap.min.js"></script>

<!-- page specific plugin scripts -->
<!--[if lte IE 8]>
  <script src="${ctx }/static/html5/js/excanvas.min.js"></script>
<![endif]-->

<!-- ace scripts -->
<script src="${ctx }/static/ace/js/ace-elements.min.js"></script>
<script src="${ctx }/static/ace/js/ace.min.js"></script>
<script type="text/javascript">
	var ctx = "${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}";
</script>