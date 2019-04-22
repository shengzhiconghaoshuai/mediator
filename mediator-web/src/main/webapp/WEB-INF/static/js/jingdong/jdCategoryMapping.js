function onClick(event, treeId, treeNode, clickFlag){
	 var jdselectId = $("#jdselect").val();
	 if(treeNode["identify"]=="叶子目录"){
		 $("#catId").val("");
	     $("#catName").val("");
		 var channelId = treeNode["id"];
		 var channelName = treeNode["name"];
	     $("#channelId").val(channelId);
	     $("#channelName").val(channelName);
		 $("#channelDetail").removeClass("hide").dialog({
			    modal: true,
				title: "添加系统类目及类目ID",
				resizable: true, 
				height:400,
				width:400,
				buttons: [ 
					{
						text: "添加",
						"class" : "btn btn-xs",
						click: function() {
							var catId = $("#catId").val();
							var catName = $("#catName").val();
							$.ajax({
								url:"saveCategroyMapping",
								type:"post",
								data:{
									"applicationId":jdselectId,
									"channelId":channelId,
									"channelName":channelName,
									"catId":catId,
									"catName":catName
								},
								success:function(data){
									if(data==1){
										alert("添加成功");
										jQuery("#grid-table").jqGrid('setGridParam', {
								        }).trigger('reloadGrid');
									}else{
										alert("添加失败");
									}
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
				],
				open:function(){
					$('div[aria-describedby=channelDetail]').attr("style","position: absolute; height: auto; width: 400px; top: 50px; left: 379.5px; display: block;");
				}
		 });
	 }
	 if(treeNode["identify"]=="属性映射"){
		 $("#jdattrcode").val("");
		 $("#defaultValue").val("");
		 var channelAttrvalId = treeNode["id"];
		 var channelAttrval = treeNode["name"];
		 var channelId = treeNode["fid"]; 
		 var attType = treeNode["attType"];
		 var colorProp = treeNode["colorProp"];
		 var inputType = treeNode["inputType"];
		 var keyProp = treeNode["keyProp"];
		 var req = treeNode["isReq"];
		 var saleProp = treeNode["saleProp"];
		 var sizeProp = treeNode["sizeProp"];
		 var status = treeNode["status"];
		 $("#jdchannelId").val(channelId);
	     $("#jdchannelAttrId").val(channelAttrvalId);
	     $("#jdchannelAttrName").val(channelAttrval);
	     $("#jdchannelattDetail").removeClass("hide").dialog({
	    	 modal: true,
				title: "添加系统属性值映射",
				resizable: true, 
				height:400,
				width:400,
				buttons: [ 
							{
								text: "添加",
								"class" : "btn btn-xs",
								click: function() {
									var jdattrcode = $("#jdattrcode").val();
								    var defaultValue = $("#defaultValue").val();
									$.ajax({
										url:"savejdChannelAttrMapping",
										type:"post",
										async:false, 
										data:{
											"channelId":channelId,
											"applicationId":jdselectId,
											"jdattrcode":jdattrcode,
											"defaultValue":defaultValue,
											"channelAttrval":channelAttrval,
											"channelAttrvalId":channelAttrvalId,
											"attributeType":attType,
											"colorProperty":colorProp,
											"inputType":inputType,
											"keyProperty":keyProp,
											"required":req,
											"saleProperty":saleProp,
											"sizeProperty":sizeProp,
											"status":status
										},
										success:function(data){
											if(data==1){
												alert("添加成功");
												jQuery("#grid-table1").jqGrid('setGridParam', {
										        }).trigger('reloadGrid');
											}else{
												alert("添加失败");
											}
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
						],
						open:function(){
							$('div[aria-describedby=jdchannelattDetail]').attr("style","position: absolute; height: auto; width: 400px; top: 50px; left: 379.5px; display: block;");
						}
	     });
	 }
	 if(treeNode["identify"]=="属性值映射"){
		 var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
		 var sNodes = treeObj.getSelectedNodes();
		 var node;
		 if (sNodes.length > 0) {
			node = sNodes[0].getParentNode().getParentNode();
		 }
		 $("#attrcode").val("");
	     $("#attrvalcode").val("");
	     $("#attrval").val("");
		 var channelAttrId = treeNode["fid"];
		 var channelAttrvalId = treeNode["id"];
		 var channelAttrval = treeNode["name"];
		 var channelId = node["id"];
	     $("#channelAttrId").val(channelAttrId);
	     $("#channelAttrvalId").val(channelAttrvalId);
	     $("#channelAttrval").val(channelAttrval);
	     $("#_channelId").val(channelId);
		 $("#channelattDetail").removeClass("hide").dialog({
			    modal: true,
				title: "添加系统属性值映射",
				resizable: true, 
				height:400,
				width:400,
				buttons: [ 
					{
						text: "添加",
						"class" : "btn btn-xs",
						click: function() {
							var attrcode = $("#attrcode").val();
						    var attrvalcode = $("#attrvalcode").val();
						    var attrval = $("#attrval").val();
							$.ajax({
								url:"saveChannelAttrvalMapping",
								type:"post",
								async:false, 
								data:{
									"channelId":channelId,
									"applicationId":jdselectId,
									"channelAttrId":channelAttrId,
									"channelAttrvalId":channelAttrvalId,
									"channelAttrval":channelAttrval,
									"attrcode":attrcode,
									"attrvalcode":attrvalcode,
									"attrval":attrval
								},
								success:function(data){
									if(data==1){
										alert("添加成功");
										jQuery("#grid-table2").jqGrid('setGridParam', {
								        }).trigger('reloadGrid');
									}else{
										alert("添加失败");
									}
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
				],
				open:function(){
					$('div[aria-describedby=channelattDetail]').attr("style","position: absolute; height: auto; width: 400px; top: 50px; left: 379.5px; display: block;");
				}
		 });
	 }
}


function addDiyDom(treeId, treeNode) {
	if(treeNode.isReq=="true"){
		var aObj = $("#" + treeNode.tId + "_a");
		var editStr = "<span id='diyBtn_" +treeNode.id+ "'><font color='red'>(必填属性)</font></span>";
		aObj.after(editStr);
	}
	if(treeNode.saleProp==true){
		var aObj = $("#" + treeNode.tId + "_a");
		var editStr = "<span id='diyBtn_" +treeNode.id+ "'><font color='blue'>(销售属性)</font></span>";
		aObj.after(editStr);
	}
}

var zNodesObj;
var treeObj;
$(document).ready(function(){
	var grid_selector = "#grid-table";
	var pager_selector = "#grid-pager";
	
	/*var grid_selector1 = "#grid-table1";
	var pager_selector1 = "#grid-pager1";
	
	var grid_selector2 = "#grid-table2";
	var pager_selector2 = "#grid-pager2";*/
			
	$("#jdselect").on('change',function(){
		 var selectValue = $("#jdselect").val();
		 if(selectValue==0){
			 location.reload();
		 }else{
				    var t = $("#treeDemo");
				    var setting = {
			    	  		view: {
			    	  			addDiyDom: addDiyDom
			    	  		},
			    	  		data: {
			    	  			simpleData: {
			    	  				enable:true,
			    	  				idKey: "id",
			    	  				pIdKey: "fid",
			    	  			}
			    	  		},
			    	  		async: {
								enable: true,//开启异步加载模式
								type: "post", 
								dataType: "json",//Ajax 获取的数据类型
								"url" : "getCategory",
								autoParam: ["id","lev","attType"],
								otherParam:{"selectValue":selectValue},
			    	  		},
			    	  		callback: {
			    	  			beforeClick: function(treeId, treeNode) {
			    	  				return true;
			    	  			},
			    	  			onClick: onClick
			    	  		}
			    	  	};
					t = $.fn.zTree.init(t, setting);
					jQuery(grid_selector).jqGrid('setGridParam',
							{postData:{"applicationId":selectValue}
			        }).trigger('reloadGrid');
					/*jQuery(grid_selector1).jqGrid('setGridParam', {postData:{"_applicationId":selectValue}
			        }).trigger('reloadGrid');
					jQuery(grid_selector2).jqGrid('setGridParam', {postData:{"_applicationId":selectValue}
			        }).trigger('reloadGrid');*/
				    }
		 jQuery(grid_selector).jqGrid({
				url:"loadCategoryMapping",
				mtype: "POST", 					 
				datatype: "json",
				width: "100",
				height: "200",
				colNames:['id','渠道类目Id','渠道类目名称','系统类目Id','系统类目名称'],
				colModel:[
					{name:'id',index:'id',sortable:false,align:'center',width:'195',hidden:true},
				    {name:'channelCategoryId',index:'channelCategoryId',sortable:false,align:'center',width:'195',search: false},
					{name:'channelCategoryName',index:'channelCategoryName', sortable:false,align:'center',width:'165',search:true},							
					{name:'categoryId',index:'categoryId', sortable:false,align:'center',width:'175',search: false},
					{name:'categoryName',index:'categoryName', sortable:false,align:'center',width:'155',search:true}			
				],
				postData:{
					"applicationId":selectValue
				},
				viewrecords : true,
				rowNum:10,
				rowList:[10,20,30],
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
				},
			});   
			jQuery(grid_selector).jqGrid("filterToolbar", {
				searchOnEnter: true,
				enableClear: false
			});
			/*jQuery(grid_selector1).jqGrid({
				url:"loadjdChannelAttrMapping",
				mtype: "POST", 					 
				datatype: "json",
				width: "100",
				height: "200",
				colNames:['id','渠道类目Id','渠道属性id','渠道属性名称','对应的系统属性代号','默认值','属性类型','输入类型','是否是关键属性','是否是销售属性','是否是颜色属性','是否是尺码属性','是否必填','应用id','状态'],
				colModel:[
				    {name:'id',index:'id',sortable:false,align:'center',width:'195',hidden:true},
				    {name:'channelCategoryId',index:'channelCategoryId',sortable:false,align:'center',width:'195',search: false},
					{name:'channelAttributeId',index:'channelAttributeId', sortable:false,align:'center',width:'165',search: false},							
					{name:'channelAttributeName',index:'channelAttributeName', sortable:false,align:'center',width:'175',search:true},
					{name:'attributeCode',index:'attributeCode', sortable:false,align:'center',width:'155',search: false},
					{name:'defaultValue',index:'defaultValue', sortable:false,align:'center',width:'155',search: false},
					{name:'attributeType',index:'attributeType', sortable:false,align:'center',width:'155',search: false},
					{name:'inputType',index:'inputType', sortable:false,align:'center',width:'155',search: false},
					{name:'keyProperty',index:'keyProperty', sortable:false,align:'center',width:'155',search: false},
					{name:'saleProperty',index:'saleProperty', sortable:false,align:'center',width:'155',search: false},
					{name:'colorProperty',index:'colorProperty', sortable:false,align:'center',width:'155',search: false},
					{name:'sizeProperty',index:'sizeProperty', sortable:false,align:'center',width:'155',search: false},
					{name:'required',index:'required', sortable:false,align:'center',width:'155',search: false},
					{name:'applicationId',index:'applicationId', sortable:false,align:'center',width:'155',search: false},
					{name:'status',index:'status', sortable:false,align:'center',width:'155',search: false}
				],
				postData:{
					"_applicationId":selectValue
				},
				viewrecords : true,
				rowNum:10,
				rowList:[10,20,30],
				pager : pager_selector1	,
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
				}
												
			});  
			jQuery(grid_selector1).jqGrid("filterToolbar", {
				searchOnEnter: true,
				enableClear: false
			});
			jQuery(grid_selector2).jqGrid({
				url:"loadChannelAttrvalMapping",
				mtype: "POST", 					 
				datatype: "json",
				width: "100",
				height: "200",
				colNames:['id','渠道类目Id','渠道属性id','渠道属性值id','渠道属性值','系统属性代号','系统属性值代号','系统属性值','应用id'],
				colModel:[
                    {name:'id',index:'id',sortable:false,align:'center',width:'195',hidden:true},
				    {name:'channelCategoryId',index:'channelCategoryId',sortable:false,align:'center',width:'195',search: false},
					{name:'channelAttributeId',index:'channelAttributeId', sortable:false,align:'center',width:'165',search: false},							
					{name:'channelAttributeValueId',index:'channelAttributeValueId', sortable:false,align:'center',width:'175',search: false},
					{name:'channelAttributeValue',index:'channelAttributeValue', sortable:false,align:'center',width:'155',search:true},
					{name:'attributeCode',index:'attributeCode', sortable:false,align:'center',width:'155',search: false},
					{name:'attributeValueCode',index:'attributeValueCode', sortable:false,align:'center',width:'155',search: false},
					{name:'attributeValue',index:'attributeValue', sortable:false,align:'center',width:'155',search:true},
					{name:'applicationId',index:'applicationId', sortable:false,align:'center',width:'155',search: false}
				],
				postData:{
					"_applicationId":selectValue
				},
				viewrecords : true,
				rowNum:10,
				rowList:[10,20,30],
				pager : pager_selector2,
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
				}
			});  
			jQuery(grid_selector2).jqGrid("filterToolbar", {
				searchOnEnter: true,
				enableClear: false
			});	*/
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
	
});

function modify(type){
	var jdselectId = $("#jdselect").val();
	if(type=="grid1"){
		var ids = $("#grid-table").jqGrid('getGridParam','selrow');
		if(ids==null){
			alert("请选中对应行再执行操作");
		}else{
		var rowData = $("#grid-table").jqGrid("getRowData",ids);
		var catId = rowData["categoryId"];
		var catName = rowData["categoryName"];
		var channelCategoryId = rowData["channelCategoryId"];
		var channelCategoryName = rowData["channelCategoryName"];
		 $("#catId").val(catId);
		 $("#catName").val(catName);
		 $("#channelId").val(channelCategoryId);
		 $("#channelName").val(channelCategoryName);
		 $("#channelDetail").removeClass("hide").dialog({
			    modal: true,
			    title: "编辑系统类目及类目ID",
				resizable: true, 
				height:400,
				width:400,
				buttons: [ 
					{
						text: "确定",
						"class" : "btn btn-xs",
						click: function() {
							var catId = $("#catId").val();
							var catName = $("#catName").val();
							$.ajax({
								url:"updateCategroyMapping",
								type:"post",
								data:{
									"id":ids,
									"applicationId":jdselectId,
									"channelId":channelCategoryId,
									"channelName":channelCategoryName,
									"catId":catId,
									"catName":catName
								},
								success:function(data){
									if(data==1){
										alert("编辑成功");
										jQuery("#grid-table").jqGrid('setGridParam', {
								        }).trigger('reloadGrid');
									}else{
										alert("失败");
									}
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
				],
				open:function(){
					$('div[aria-describedby=channelDetail]').attr("style","position: absolute; height: auto; width: 400px; top: 50px; left: 379.5px; display: block;");
				}
		 });
		}
	}
	if(type=="grid2"){
		var ids = $("#grid-table1").jqGrid('getGridParam','selrow');
		if(ids==null){
			alert("请选中对应行再执行操作");
		}else{
			 var rowData = $("#grid-table1").jqGrid("getRowData",ids);
			 var channelAttrvalId = rowData["channelAttributeId"];
			 var channelAttrval = rowData["channelAttributeName"];
			 var channelId = rowData["channelCategoryId"]; 
			 var attType = rowData["attributeType"];
			 var colorProp = rowData["colorProperty"];
			 var inputType = rowData["inputType"];
			 var keyProp = rowData["keyProperty"];
			 var req = rowData["required"];
			 var saleProp = rowData["saleProperty"];
			 var sizeProp = rowData["sizeProperty"];
			 var status = rowData["status"];
			 var attributeCode = rowData["attributeCode"];
			 var defaultValue = rowData["defaultValue"];
			 $("#jdattrcode").val(attributeCode);
			 $("#defaultValue").val(defaultValue);
			 $("#jdchannelId").val(channelId);
		     $("#jdchannelAttrId").val(channelAttrvalId);
		     $("#jdchannelAttrName").val(channelAttrval);	
		     $("#jdchannelattDetail").removeClass("hide").dialog({
			    modal: true,
			    title: "编辑系统类目及类目ID",
				resizable: true, 
				height:400,
				width:400,
				buttons: [ 
					{
						text: "确定",
						"class" : "btn btn-xs",
						click: function() {
							var jdattrcode = $("#jdattrcode").val();
						    var defaultValue = $("#defaultValue").val();
							$.ajax({
								url:"updatejdChannelAttrMapping",
								type:"post",
								data:{
									"id":ids,
									"channelId":channelId,
									"applicationId":jdselectId,
									"jdattrcode":jdattrcode,
									"defaultValue":defaultValue,
									"channelAttrval":channelAttrval,
									"channelAttrvalId":channelAttrvalId,
									"attributeType":attType,
									"colorProperty":colorProp,
									"inputType":inputType,
									"keyProperty":keyProp,
									"required":req,
									"saleProperty":saleProp,
									"sizeProperty":sizeProp,
									"status":status
									},
								success:function(data){
									if(data==1){
										alert("编辑成功");
										jQuery("#grid-table1").jqGrid('setGridParam', {
								        }).trigger('reloadGrid');
									}else{
										alert("失败");
									}
								}
							});
							$("#jdchannelattDetail").dialog( "close" ); 
						} 
					},
					{
						text: "取消",
						"class" : "btn btn-primary btn-xs",
						click: function() {
							$("#jdchannelattDetail").dialog( "close" ); 
						} 
					}
				],
				open:function(){
					$('div[aria-describedby=jdchannelattDetail]').attr("style","position: absolute; height: auto; width: 400px; top: 50px; left: 379.5px; display: block;");
				}
		 });
		}
	}
	if(type=="grid3"){
		var ids = $("#grid-table2").jqGrid('getGridParam','selrow');
		if(ids==null){
			alert("请选中对应行再执行操作");
		}else{
		var rowData = $("#grid-table2").jqGrid("getRowData",ids);
		var channelCategoryId = rowData["channelCategoryId"];
		var channelAttributeId = rowData["channelAttributeId"];
		var channelAttributeValueId = rowData["channelAttributeValueId"];
		var channelAttributeValue = rowData["channelAttributeValue"];
		var attributeCode = rowData["attributeCode"];
		var attributeValueCode = rowData["attributeValueCode"];
		var attributeValue = rowData["attributeValue"];
		var applicationId = rowData["applicationId"];
	     $("#channelAttrId").val(channelAttributeId);
	     $("#channelAttrvalId").val(channelAttributeValueId);
	     $("#channelAttrval").val(channelAttributeValue);
	     $("#_channelId").val(channelCategoryId);
	     $("#attrcode").val(attributeCode);
		 $("#attrvalcode").val(attributeValueCode);
		 $("#attrval").val(attributeValue);
		 $("#channelattDetail").removeClass("hide").dialog({
			    modal: true,
			    title: "编辑系统类目及类目ID",
				resizable: true, 
				height:400,
				width:400,
				buttons: [ 
					{
						text: "确定",
						"class" : "btn btn-xs",
						click: function() {
							var attrcode = $("#attrcode").val();
						    var attrvalcode = $("#attrvalcode").val();
						    var attrval = $("#attrval").val();
							$.ajax({
								url:"updateChannelAttrvalMapping",
								type:"post",
								data:{
									"id":ids,
									"channelId":channelCategoryId,
									"applicationId":jdselectId,
									"channelAttrId":channelAttributeId,
									"channelAttrvalId":channelAttributeValueId,
									"channelAttrval":channelAttributeValue,
									"attrcode":attrcode,
									"attrvalcode":attrvalcode,
									"attrval":attrval
								},
								success:function(data){
									if(data==1){
										alert("编辑成功");
										jQuery("#grid-table2").jqGrid('setGridParam', {
								        }).trigger('reloadGrid');
									}else{
										alert("失败");
									}
								}
							});
							$("#channelattDetail").dialog( "close" ); 
						} 
					},
					{
						text: "取消",
						"class" : "btn btn-primary btn-xs",
						click: function() {
							$("#channelattDetail").dialog( "close" ); 
						} 
					}
				],
				open:function(){
					$('div[aria-describedby=channelattDetail]').attr("style","position: absolute; height: auto; width: 400px; top: 50px; left: 379.5px; display: block;");
				}
		 });
		}
	}
	
}

function remove1(type){
	if(type=="grid1"){
		var ids = $("#grid-table").jqGrid('getGridParam','selrow');
		if(ids==null){
			alert("请选中对应行再执行操作");
		}else{
			$.ajax({
				url:'removeCategoryMapping',
				type:'post',
				async:false, 
				data:{
					'id':ids
				},
			    success:function(data){
			       if(data==1){
			    	   alert("删除成功");
			    	   jQuery("#grid-table").jqGrid('setGridParam', {
	       	        }).trigger('reloadGrid');
			       }
			    }
			});
		}	
	}
	if(type=="grid2"){
		var ids = $("#grid-table1").jqGrid('getGridParam','selrow');
		if(ids==null){
			alert("请选中对应行再执行操作");
		}else{
			$.ajax({
				url:'removejdChannelAttrMapping',
				type:'post',
				async:false, 
				data:{
					'id':ids
				},
			    success:function(data){
			       if(data==1){
			    	   alert("删除成功");
			    	   jQuery("#grid-table1").jqGrid('setGridParam', {
	       	        }).trigger('reloadGrid');
			       }
			    }
			});
		}	
	}
	if(type=="grid3"){
		var ids = $("#grid-table2").jqGrid('getGridParam','selrow');
		if(ids==null){
			alert("请选中对应行再执行操作");
		}else{
			$.ajax({
				url:'removeChannelAttrvalMapping',
				type:'post',
				async:false, 
				data:{
					'id':ids
				},
			    success:function(data){
			       if(data==1){
			    	   alert("删除成功");
			    	   jQuery("#grid-table2").jqGrid('setGridParam', {
	       	        }).trigger('reloadGrid');
			       }
			    }
			});
		}	
	}
	
}