var grid_selector = "#grid-table";
var pager_selector = "#grid-pager";
$(document).ready(function(){
	jQuery(grid_selector).jqGrid({
		url:"loadTaskTemplates",
		mtype: "POST", 					 
		datatype: "json",
		height: "400",
		colNames:['id','类型','子类型','描述','重复','重跑','挂起','优先级','状态','操作'],
		colModel:[
            {name:'id',index:'id',sortable:false,width:'195',hidden:true},
			{name:'type',index:'type', sortable:false,width:'175'},
			{name:'subType',index:'subType', sortable:false,width:'155'},
			{name:'description',index:'description',sortable:false,width:'195'},
			{name:'repeatable',index:'repeatable', sortable:false,width:'155'},		
			{name:'reRun',index:'reRun', sortable:false,width:'155'},		
			{name:'hung',index:'hung', sortable:false,width:'155'},		
			{name:'priority',index:'priority', sortable:false,width:'155'},
			{name:'status',index:'status', sortable:false,width:'155'},
			{name:'ACTION',index:'action', sortable:false,align:'center',width:'155'}
		],
		viewrecords : true,
		rowNum:20,
		rowList:[20,30,40],
		pager : pager_selector,
		altRows: true,
		autowidth:true,
		multiselect: false,
	    multiboxonly: true,
	    gridComplete: function() {
			var table = this;
			setTimeout(function(){
				updatePagerIcons(table);
				enableTooltips(table);
			}, 0);
		    var ids = jQuery(grid_selector).jqGrid('getDataIDs');
			for (var i = 0; i < ids.length; i++) {
				jQuery(grid_selector).jqGrid('setRowData', ids[i], {								
					ACTION: "<button class='btn btn-xs btn-info' onclick = 'editForm("+ids[i]+")'><i title='编辑' class='icon-edit bigger-120'></i></button>" +
							"&nbsp;<button class='btn btn-xs btn-danger' onclick = 'deleteForm("+ids[i]+")' ><i class='icon-trash bigger-120' title='删除'></i></button>"
				});
			}
		},
	});   
	
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
	
	$('#taskType').change(function(){
		var type = $(this).val();
		//ajax 方式获取channel对应的applications
		$.ajax({
			url: "getByType",
			type: "POST",
			data: {
				taskType: type
			},
			dataType: "json",
			success: function(data, textStatus){
				var options = "<option value=''>--请选择subType--</option>";
				if (data != null) {
					for (var i = 0; i < data.length; i++) {
						var option = "<option value='" + data[i].subType + "'>" + data[i].subType + "</option>";
						options += option;
					}
				}
				$("#_subType").html(options);
			},
			error: function(){
				alert("请求应用信息失败!!");
			}
		});
	});
	
	$("#taskQueryBtn").click(function(){
		 var taskType = $("#taskType").val();
		 var subType = $("#_subType").val();
		 jQuery(grid_selector).jqGrid('setGridParam',
	    		    {postData:{
	    		    	"type":taskType,
	    		    	"subType":subType
	    		    }
			        }).trigger('reloadGrid');
	});
	
	$("#taskAddBtn").click(function(){
		$("#id").val("");
		$("#type").val("");
		$("#subType").val("");
		$("#description").val("");
		$("#reRun").val("");
		$("#repeatable").val("");
		$("#hung").val("");
		$("#priority").val("");
		$("#status").val("");
		$("#dialog").removeClass("hide").dialog({
			modal: true,
		    title: "添加TASK模版",
			resizable: true, 
			height:500,
			width:600,
			buttons: [
				{
					text: "确定",
					"class" : "btn btn-xs",
					click: function() {
						$.ajax({
							url:'add',
							data:$('#templateAddForm').serialize(),
							type:'post',
							success:function(e){
								if(e=="1"){
									alert("添加成功");
								}else{
									alert("添加失败");
								}
								 jQuery(grid_selector).jqGrid('setGridParam', {
			         		        }).trigger('reloadGrid');
							}
						});
						$( this ).dialog( "close" ); 
					} 
				},
				{
					text: "取消",
					"class" : "btn btn-primary btn-xs",
					click: function() {
						$( this ).dialog( "close" ); 
					} 
				}
			]
	});
	});
	
	
});

function editForm(rowId){
	$("#id").val("");
	$("#type").val("");
	$("#subType").val("");
	$("#description").val("");
	$("#repeatable").val("");
	$("#reRun").val("");
	$("#hung").val("");
	$("#priority").val("");
	$("#status").val("");
	var rowData = $(grid_selector).jqGrid('getRowData',rowId);
	$("#id").val(rowData.id);
	$("#type").val(rowData.type);
	$("#subType").val(rowData.subType);
	$("#description").val(rowData.description);
	$("#repeatable").val(rowData.repeatable);
	$("#reRun").val(rowData.reRun);
	$("#hung").val(rowData.hung);
	$("#priority").val(rowData.priority);
	$("#status").val(rowData.status);
	$("#dialog").removeClass("hide").dialog({
		modal: true,
	    title: "编辑TASK模版",
		resizable: true, 
		height:500,
		width:600,
		buttons: [
			{
				text: "确定",
				"class" : "btn btn-xs",
				click: function() {
					$.ajax({
                     url:"update",
                     type:"POST",
                     data:$('#templateAddForm').serialize(),
                     success:function(e){
                    	 if(e=="1"){
								alert("编辑成功");
							}else{
								alert("编辑失败");
							}
                        jQuery(grid_selector).jqGrid('setGridParam', {
         		        }).trigger('reloadGrid');
                     }
					});
					$( this ).dialog( "close" ); 
				} 
			},
			{
				text: "取消",
				"class" : "btn btn-primary btn-xs",
				click: function() {
					$( this ).dialog( "close" ); 
				} 
			}
		]
});
}

function deleteForm(ids){
  if(confirm('确认删除此信息吗?')){
	  $.ajax({
		 url:'deleteTaskTemplate',
		 type:'post',
		 dataType:'json',
		 data:{
			 'id':ids
		 },
	     success:function(data){
	    	 if(data=="1"){
	    		  alert("删除成功");
	    		  
	    	 } else {
	    		 alert("删除失败");
	    	 }
	    	 jQuery(grid_selector).jqGrid('setGridParam', {
		        }).trigger('reloadGrid');
	     }
	  });
  }
}