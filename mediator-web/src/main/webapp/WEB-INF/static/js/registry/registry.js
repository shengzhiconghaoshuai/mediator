 $(document).ready(function(){
       	var grid_selector = "#grid-table";
        var pager_selector = "#grid-pager";
        jQuery(grid_selector).jqGrid({
			url:"loadRegistry",
			mtype: "POST", 					 
			datatype: "json",
			colNames:['key','值','描述'],
			colModel:[
				{name:'key',index:'registry_key',sortable:false,align:'center'},
			    {name:'value',index:'registry_value',sortable:false,align:'center'},
				{name:'description',index:'registry_description', sortable:false,align:'center'}							
			], 
			viewrecords : true,
			rowNum:20,
			rowList:[20,30,50],
			pager : pager_selector,
			altRows: false,
			autowidth:true,
			multiselect: false,
		    multiboxonly: false,
		    userDataOnFooter : true,
		    gridComplete: function() {
				var table = this;
				setTimeout(function(){
					updatePagerIcons(table);
					enableTooltips(table);
				}, 0);
			},
		});  

        var newHeight = $(window).height() - 340;
        $(".ui-jqgrid .ui-jqgrid-bdiv").css("cssText","height: "+newHeight+"px!important;");
        
        function updatePagerIcons(table) {
    		var replacement = 
    		{
    			'ui-icon-seek-first' : 'ace-icon fa fa-angle-double-left bigger-140',
    			'ui-icon-seek-prev' : 'ace-icon fa fa-angle-left bigger-140',
    			'ui-icon-seek-next' : 'ace-icon fa fa-angle-right bigger-140',
    			'ui-icon-seek-end' : 'ace-icon fa fa-angle-double-right bigger-140'
    		};
    		$('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function(){
    			var icon = $(this);
    			var $class = $.trim(icon.attr('class').replace('ui-icon', ''));
    			
    			if($class in replacement) icon.attr('class', 'ui-icon '+replacement[$class]);
    		});
    	}
    	function enableTooltips(table) {
    		$('.navtable .ui-pg-button').tooltip({container:'body'});
    		$(table).find('.ui-pg-div').tooltip({container:'body'});
    	}
 });

 
 function modify(){
	 $("#rkey").attr("disabled","disabled");
	 $("#rkey").val("");
	 $("#rvalue").val("");
	 $("#rdescription").val("");
	 var rowId = $("#grid-table").jqGrid('getGridParam','selrow');
	 if(rowId==null){
       alert("请选中行后再执行编辑操作");
       return null;
	 }
	 var rowData = $("#grid-table").jqGrid('getRowData',rowId);
	 var key = rowData.key;
	 var value = rowData.value;
	 var description = rowData.description;
	 $("#rkey").val(key);
	 $("#rvalue").val(value);
	 $("#rdescription").val(description);
	 showRegistry("编辑注册表","updateRegistry");
 }
 
 function remove1(){
	 var rowId = $("#grid-table").jqGrid('getGridParam','selrow');
	 if(rowId==null){
		 alert("请选中行后再执行删除操作");
		 return false;
	 }
	 var rowData = $("#grid-table").jqGrid('getRowData',rowId);
	 var key = rowData.key;
	 $.ajax({
		url:"removeRegistry",
		type:"post",
		data:{
			"key":key
		},
		success:function(data){
			 if(data=='1'){
				 alert("删除成功");
				 jQuery("#grid-table").jqGrid('setGridParam', {
			        }).trigger('reloadGrid');
			 }
		 }
	 });
 }
 
 
 function add(){
	 $("#rkey").val("");
	 $("#rvalue").val("");
	 $("#rdescription").val("");
	 $("#rkey").attr("disabled",false);
	 showRegistry("新增注册表","addRegistry");
 }
 
 
 
 function showRegistry(title,url){
	 $("#registry").removeClass('hide').dialog({
		 modal: true,
		 title: title,
		 resizable: true, 
		 height:400,
		 width:400,
		 buttons: [
					{
						text: "确定",
						"class" : "btn btn-xs",
						click: function() {
							 var key = $("#rkey").val();
							 var value = $("#rvalue").val();
							 var description = $("#rdescription").val();
							$.ajax({
								url:url,
								type:"post",
								data:{
									"key":key,
									"value":value,
									"description":description
								},
								async:false,
								success:function(data){
									if(data=='1'){
										alert("操作成功");
										jQuery("#grid-table").jqGrid('setGridParam', {
								        }).trigger('reloadGrid');
									}else{
										alert("失败");
									}
								}
							});
							$(this).dialog( "close" ); 
						} 
					},
					{
						text: "取消",
						"class" : "btn btn-primary btn-xs",
						click: function() {
							$(this).dialog( "close" ); 
						} 
					}
				]
	 });
 }