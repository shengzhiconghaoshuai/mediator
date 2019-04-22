var IDMark_A = "_a";
function filter(treeId, parentNode, childNodes) {
		if (!childNodes) return null;
		for (var i=0, l=childNodes.length; i<l; i++) {
			childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
		}
		return childNodes;
	}
	
	function OnRightClick(event, treeId, treeNode) {
		if (!treeNode && event.target.tagName.toLowerCase() != "button" && $(event.target).parents("a").length == 0) {
			categoryzTree.cancelSelectedNode();
			showRMenu("root", event.clientX, event.clientY);
		} else if (treeNode && !treeNode.noR) {
			categoryzTree.selectNode(treeNode);
			showRMenu("node", event.clientX, event.clientY);
		}
	}
	
	/**
	 * 添加属性/值
	 */
	function mappingAttrOrAttrval(event, treeId, treeNode) {
		var isParent = treeNode.isParent;
		if (!isParent) {
			mappingAttrVal(event, treeId, treeNode);
		} else {
			mappingAttr(event, treeId, treeNode);
		}
	}
	
	function addDiyDom(treeId, treeNode) {
		if (!treeNode.isParent) return;
		var aObj = $("#" + treeNode.tId + IDMark_A);
		if (treeNode.required) {
			var editStr = "<span style='color:red;'>(必填属性)</span>";
			aObj.after(editStr);
		}
	}
	
	/**
	 * 映射属性
	 */
	function mappingAttr(event, treeId, treeNode) {
		var tbCategoryId = treeNode.cid;
		var tbAttrId = treeNode.pid;
		var tbAttrName = treeNode.name;
		$("#tbAttrId").val(tbAttrId);
		$("#tbAttrName").val(tbAttrName);
		$("#systemAttrId").val("");
		$("#defaultValue").val("");
		// 属性映射
		$("#addAttrDialog").removeClass("hide").dialog({
			modal: true,
			title: "添加系统属性映射",
			resizable: true, 
			height:410,
			width:472,
			buttons: [ 
				{
					text: "添加",
					"class" : "btn btn-xs",
					click: function() {
						var applicationId = $("#selectApp").val();
						var systemAttrId = $("#systemAttrId").val();;
						var defaultValue = $("#defaultValue").val();;
						$.ajax({
							url: ctx + "/system/tmall/mapping/attr/save",
							type:"post",
							async:false, 
							data:{
								"channelCid": tbCategoryId,
								"tbAttrId": tbAttrId,
								"tbAttrName": tbAttrName,
								"systemAttrId": systemAttrId,
								"defaultValue": defaultValue,
								"applicationId": applicationId
							},
							success:function(data){
								if(data.status == 0){
									alert("添加成功");
									//刷新属性
									jQuery(mappedAttr_selector).setGridParam({postData: {
										"applicationId":applicationId 
									} }).trigger("reloadGrid");
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
			]
	    });
	}
	
	/**
	 * 映射属性值
	 */
	function mappingAttrVal(event, treeId, treeNode) {
		var tbAttrValName = treeNode.name;
		var parent = treeNode.getParentNode();
		
		var tbCategoryId = parent.cid;
		var tbAttrId = parent.pid;
		var channelAttrValId = treeNode.vid;
		
		$("#tbAttrVal").val(tbAttrValName);
		$("#sysAttrId").val("");
		$("#systemAttrVal").val("");
		// 属性值映射
		$("#addAttrValDialog").removeClass("hide").dialog({
			modal: true,
			title: "添加系统属性值映射",
			resizable: true, 
			height:250,
			width:380,
			buttons: [ 
				{
					text: "添加",
					"class" : "btn btn-xs",
					click: function() {
						var applicationId = $("#selectApp").val();
						var systemAttrId = $("#sysAttrId").val();
						var systemAttrVal = $("#systemAttrVal").val();
						$.ajax({
							url: ctx + "/system/tmall/mapping/attrval/save",
							type:"post",
							async:false, 
							data:{
								"channelCid": tbCategoryId,
								"tbAttrId": tbAttrId,
								"channelAttrValId": channelAttrValId,
								"channelAttrVal": tbAttrValName,
								"systemAttrId": systemAttrId,
								"systemAttrVal": systemAttrVal,
								"applicationId": applicationId
							},
							success:function(data){
								if(data.status == 0){
									alert("添加成功");
									//刷新属性
									jQuery(mappedAttrVal_selector).setGridParam({postData: {
										"applicationId":applicationId 
									} }).trigger("reloadGrid");
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
			]
	    });
	}
	
	function showRMenu(type, x, y) {
		$("#rMenu ul").show();
		if (type=="root") {
			$("#m_del").hide();
			$("#m_check").hide();
			$("#m_unCheck").hide();
		} else {
			$("#m_del").show();
			$("#m_check").show();
			$("#m_unCheck").show();
		}
		y = y - 105;
		rMenu.css({"top":y+"px", "left":x+"px", "visibility":"visible"});

		$("body").bind("mousedown", onBodyMouseDown);
	}
	function hideRMenu() {
		if (rMenu) rMenu.css({"visibility": "hidden"});
		$("body").unbind("mousedown", onBodyMouseDown);
	}
	function onBodyMouseDown(event){
		if (!(event.target.id == "rMenu" || $(event.target).parents("#rMenu").length>0)) {
			rMenu.css({"visibility" : "hidden"});
		}
	}
	
	/**
	 * 类目映射
	 */
	function categoryMapping() {
		hideRMenu();
		var nodes = categoryzTree.getSelectedNodes();
		if (nodes && nodes.length>0) {
			if (nodes[0].isParent) {
				alert("请选择叶子类目!");
			} else {
				//
				var tbCategoryId = nodes[0].cid;
				var tbCategoryName = nodes[0].name;
				$("#tbCid").val(tbCategoryId);
				$("#tbCname").val(tbCategoryName);
				$("#systemCid").val("");
				$("#systemCname").val("");
				$("#addMappingDialog").removeClass("hide").dialog({
					modal: true,
					title: "添加系统类目映射",
					resizable: true, 
					height:300,
					width:349,
					buttons: [ 
						{
							text: "添加",
							"class" : "btn btn-xs",
							click: function() {
								var applicationId = $("#selectApp").val();
								var systemCid = $("#systemCid").val();;
								var systemCname = $("#systemCname").val();;
								$.ajax({
									url: ctx + "/system/tmall/mapping/category/save",
									type:"post",
									async:false, 
									data:{
										"channelCid": tbCategoryId,
										"channelCname": tbCategoryName,
										"systemCid": systemCid,
										"systemCname": systemCname,
										"applicationId": applicationId
									},
									success:function(data){
										if(data.status == 0){
											alert("添加成功");
											//刷新类目
											jQuery(mappedCategory_selector).setGridParam({postData: {
												"applicationId":applicationId 
											} }).trigger("reloadGrid");
										}else{
											alert("添加失败" + data.msg);
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
					]
			    });
			}
		}
	}
	
	//编辑类目映射
	function categoryEdit(categoryMappingId) {
		var rowData = $(mappedCategory_selector).jqGrid("getRowData",categoryMappingId);
		var systemCid = rowData["categoryId"];
		var systemCname = rowData["categoryName"];
		var tbCid = rowData["channelCategoryId"];
		var tbCname = rowData["channelCategoryName"];
		$("#systemCid").val(systemCid);
		$("#systemCname").val(systemCname);
		$("#tbCid").val(tbCid);
		$("#tbCname").val(tbCname);
		$("#addMappingDialog").removeClass("hide").dialog({
			modal: true,
			title: "修改系统类目映射",
			resizable: true, 
			height:300,
			width:349,
			buttons: [ 
				{
					text: "保存",
					"class" : "btn btn-xs",
					click: function() {
						var applicationId = $("#selectApp").val();
						var newSystemCid = $("#systemCid").val();;
						var newSystemCname = $("#systemCname").val();;
						$.ajax({
							url: ctx + "/system/tmall/mapping/category/update",
							type:"post",
							async:false, 
							data:{
								"id":categoryMappingId,
								"channelCid": tbCid,
								"channelCname": tbCname,
								"systemCid": newSystemCid,
								"systemCname": newSystemCname,
								"applicationId": applicationId
							},
							success:function(data){
								if(data.status == 0){
									alert("修改成功");
									//刷新类目
									jQuery(mappedCategory_selector).setGridParam({postData: {
										"applicationId":applicationId 
									} }).trigger("reloadGrid");
								}else{
									alert("修改失败");
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
			]
	    });
	}
	//删除类目映射
	function categoryDelete(categoryMappingId) {
		if (confirm("确认删除?")) {
			var applicationId = $("#selectApp").val();
			$.ajax({
				url:ctx + "/system/tmall/mapping/category/delete",
				type:'post',
				async:false, 
				data:{
					'id':categoryMappingId
				},
			    success:function(data){
			    	if(data.status == 0){
						alert("删除成功");
						//刷新类目
						jQuery(mappedCategory_selector).setGridParam({postData: {
							"applicationId":applicationId 
						} }).trigger("reloadGrid");
					}else{
						alert("删除失败");
					}
			    }
			});
		}
	}
	
	//编辑属性映射
	function attrEdit(attrMappingId) {
		var rowData = $(mappedAttr_selector).jqGrid("getRowData",attrMappingId);
		var tbAttrId = rowData["channelAttributeId"];
		var tbAttrName = rowData["channelAttributeName"];
		var tbKeyProp = rowData["keyProperty"];
		var tbSalesProp = rowData["saleProperty"];
		var tbColorProp = rowData["colorProperty"];
		var tbReqProp = rowData["required"];
		var sysAttrName = rowData["attributeCode"];
		var systemAttrId = rowData["attributeCode"];
		var defaultValue = rowData["defaultValue"];
		
		$("#tbAttrId").val(tbAttrId);
		$("#tbAttrName").val(tbAttrName);
		$("#tbKeyProp").val(tbKeyProp);
		$("#tbSalesProp").val(tbSalesProp);
		$("#tbColorProp").val(tbColorProp);
		$("#tbReqProp").val(tbReqProp);
		$("#sysAttrName").val(sysAttrName);
		$("#systemAttrId").val(systemAttrId);
		$("#defaultValue").val(defaultValue);
		
		$("#addAttrDialog").removeClass("hide").dialog({
			modal: true,
			title: "修改系统属性映射",
			resizable: true, 
			height:410,
			width:472,
			buttons: [ 
				{
					text: "保存",
					"class" : "btn btn-xs",
					click: function() {
						var applicationId = $("#selectApp").val();
						var newSystemAttrId = $("#systemAttrId").val();
						var newDefaultValue = $("#defaultValue").val();
						$.ajax({
							url: ctx + "/system/tmall/mapping/attr/update",
							type:"post",
							async:false, 
							data:{
								"id":attrMappingId,
								"newSystemAttrId": newSystemAttrId,
								"newDefaultValue": newDefaultValue,
								"applicationId": applicationId
							},
							success:function(data){
								if(data.status == 0){
									alert("修改成功");
									//刷新类目
									jQuery(mappedAttr_selector).setGridParam({postData: {
										"applicationId":applicationId 
									} }).trigger("reloadGrid");
								}else{
									alert("修改失败");
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
			]
	    });
	}
	
	//删除属性映射
	function attrDelete(attrMappingId) {
		if (confirm("确认删除?")) {
			var applicationId = $("#selectApp").val();
			$.ajax({
				url:ctx + "/system/tmall/mapping/attr/delete",
				type:'post',
				async:false, 
				data:{
					'id':attrMappingId
				},
			    success:function(data){
			    	if(data.status == 0){
						alert("删除成功");
						//刷新类目
						jQuery(mappedAttr_selector).setGridParam({postData: {
							"applicationId":applicationId 
						} }).trigger("reloadGrid");
					}else{
						alert("删除失败");
					}
			    }
			});
		}
	}
	
	//编辑属性值映射
	function attrValEdit(attrValMappingId) {
		var rowData = $(mappedAttrVal_selector).jqGrid("getRowData",attrValMappingId);
		var tbAttrValName = rowData["channelAttributeValue"];
		var systemAttrId = rowData["attributeCode"];
		var systemAttrVal = rowData["attributeValue"];
		
		$("#tbAttrVal").val(tbAttrValName);
		$("#sysAttrId").val(systemAttrId);
		$("#systemAttrVal").val(systemAttrVal);
		
		$("#addAttrValDialog").removeClass("hide").dialog({
			modal: true,
			title: "修改系统属性值映射",
			resizable: true, 
			height:250,
			width:380,
			buttons: [ 
				{
					text: "保存",
					"class" : "btn btn-xs",
					click: function() {
						var applicationId = $("#selectApp").val();
						var newSystemAttrId = $("#sysAttrId").val();
						var newSystemAttrVal = $("#systemAttrVal").val();
						$.ajax({
							url: ctx + "/system/tmall/mapping/attrval/update",
							type:"post",
							async:false, 
							data:{
								"id":attrValMappingId,
								"newSystemAttrId": newSystemAttrId,
								"newSystemAttrVal": newSystemAttrVal
							},
							success:function(data){
								if(data.status == 0){
									alert("修改成功");
									//刷新属性值
									jQuery(mappedAttrVal_selector).setGridParam({postData: {
										"applicationId":applicationId 
									} }).trigger("reloadGrid");
								}else{
									alert("修改失败");
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
			]
	    });
	}
	
	//删除属性值映射
	function attrValDelete(attrValMappingId) {
		if (confirm("确认删除?")) {
			var applicationId = $("#selectApp").val();
			$.ajax({
				url:ctx + "/system/tmall/mapping/attrval/delete",
				type:'post',
				async:false, 
				data:{
					'id':attrValMappingId
				},
			    success:function(data){
			    	if(data.status == 0){
						alert("删除成功");
						//刷新属性值
						jQuery(mappedAttrVal_selector).setGridParam({postData: {
							"applicationId":applicationId 
						} }).trigger("reloadGrid");
					}else{
						alert("删除失败");
					}
			    }
			});
		}
	}
	
	/**
	 * 加载属性树
	 */
	function attributeMapping() {
		hideRMenu();
		var nodes = categoryzTree.getSelectedNodes();
		var applicationId = $("#selectApp").val();
		
		var attrSetting = {
			data: {
	  			simpleData: {
	  				enable:true,
	  				idKey: "pid",
	  			}/*,
	  			key: {
	  				children: "propValues"
	  			}*/
	  		},
	  		async: {
				enable: true,
				url: ctx + "/system/tmall/mapping/attrval/get",
				autoParam:["cid","pid"],
				otherParam:{"applicationId":applicationId},
				dataFilter: filter
			},
			callback: {
				onClick: mappingAttrOrAttrval
			},
			view: {
				addDiyDom: addDiyDom
			}
		};
		
		if (nodes && nodes.length>0) {
			if (nodes[0].isParent) {
				alert("请选择叶子类目!");
			} else {
				var tbCategoryId = nodes[0].cid;
				$.ajax({
					url: ctx + "/system/tmall/mapping/attr/get",
					type:"post",
					async:false, 
					data:{
						"channelCid": tbCategoryId,
						"applicationId": applicationId
					},
					success:function(data){
						//初始化属性树
						attrzTree = $.fn.zTree.init($("#taobaoAttr"), attrSetting, data);
					}
				});
			}
		}
	}
	
	//初始化产品类目
	function initProductCat() {
		var applicationId = $("#selectApp").val();
		if (null == applicationId || "" == applicationId) {
			alert("请选择应用");
			return false;
		}
		$.ajax({
			url: ctx + "/system/tmall/mapping/productcatmapping",
			type:"post",
			async:false, 
			data:{
				"applicationId": applicationId
			},
			success:function(data){
				if(data.status == 0){
					alert("渠道产品类目初始化成功");
				}else{
					alert("渠道产品类目初始化失败");
				}
			}
		});
	}
 	
	var categoryzTree, attrzTree, rMenu;
	var mappedCategory_selector, mappedCategoryPager_selector;
	var mappedAttr_selector, mappedAttrPager_selector;
	var mappedAttrVal_selector, mappedAttrValPager_selector;
	
	$(document).ready(function(){
		var applicationId = -1;
		$("#selectApp").change(function() {
			applicationId = $(this).val();
			var categorySetting = {
				data: {
		  			simpleData: {
		  				enable:true,
		  				idKey: "cid",
		  				pIdKey: "parentCid"
		  			}
		  		},
				async: {
					enable: true,
					url: ctx + "/system/tmall/mapping/category/get",
					autoParam:["cid"],
					otherParam:{"applicationId":applicationId},
					dataFilter: filter
				},
				callback: {
					onRightClick: OnRightClick
				}
			};
			categoryzTree = $.fn.zTree.init($("#taobaoCategory"), categorySetting);
			rMenu = $("#rMenu");
			
			// 刷新grid
			jQuery(mappedCategory_selector).setGridParam({postData: {
				"applicationId":applicationId 
			} }).trigger("reloadGrid");
			jQuery(mappedAttr_selector).setGridParam({postData: {
				"applicationId":applicationId 
			} }).trigger("reloadGrid");
			jQuery(mappedAttrVal_selector).setGridParam({postData: {
				"applicationId":applicationId 
			} }).trigger("reloadGrid");
		});
		
		// 类目映射table
		mappedCategory_selector = "#mappedCategory";
		mappedCategoryPager_selector = "#mappedCategoryPager";
		jQuery(mappedCategory_selector).jqGrid({
			url: ctx + "/system/tmall/mapping/category/loadCategoryMapping",
			mtype: "POST", 					 
			datatype: "json",
			colNames:['序号','渠道类目Id','渠道类目名称','系统类目Id','系统类目名称','操作'],
			colModel:[
			    {name:'id',index:'id',width:30,fixed:true,sortable:false,resize:false,hidden: true,editable: false,align: "center"},
			    {name:'channelCategoryId',index:'channelCategoryId', search: true,sortable:false,align:'center',width:'150'},
				{name:'channelCategoryName',index:'channelCategoryName', search: true, sortable:false,align:'center',width:'165'},							
				{name:'categoryId',index:'categoryId', sortable:false,search: false, align:'center',width:'133'},
				{name:'categoryName',index:'categoryName', search: true, sortable:false,align:'center',width:'155'},	
				{name:'edit',index:'edit', search: false, sortable:false,align:'center',width:'175'}		
			],
			postData:{
				"applicationId": applicationId
			},
			viewrecords : true,
			height:'auto',
			rowNum:10,
			rowList:[10,20,30],
			pager : mappedCategoryPager_selector,
			altRows: false,
			autowidth:true,
			multiselect: false,
		    multiboxonly: false,
		    gridComplete: function() {
		    	var table = this;
				setTimeout(function(){
					updatePagerIcons(table);
					enableTooltips(table);
				}, 0);
		    	var ids = jQuery(mappedCategory_selector).jqGrid("getDataIDs");
				for (var i = 0; i < ids.length; i++) {
					jQuery(mappedCategory_selector).jqGrid("setRowData", ids[i], {
						edit: "<button class='btn btn-xs btn-primary' onclick=categoryEdit(" + ids[i] + ")><i class='icon-wrench align-top bigger-110'></i>修改</button>　<button class='btn btn-xs btn-danger' onclick=categoryDelete(" + ids[i] + ")><i class='icon-wrench align-top bigger-110'></i>删除</button>"
					});
				}
			}
		});
		jQuery(mappedCategory_selector).jqGrid("filterToolbar", {
			searchOnEnter: true,
			enableClear: false
		});
		
		// 属性映射table
		mappedAttr_selector = "#mappedAttr";
		mappedAttrPager_selector = "#mappedAttrPager";
		jQuery(mappedAttr_selector).jqGrid({
			url: ctx + "/system/tmall/mapping/attr/loadAttrMapping",
			mtype: "POST", 					 
			datatype: "json",
			height: "auto",
			colNames:['序号','渠道类目Id','渠道属性id','渠道属性名称','对应的系统属性代号','默认值','操作'],
			colModel:[
			    {name:'id',index:'id',width:30,fixed:true,sortable:false,resize:false,hidden: true,editable: false,align: "center"},
			    {name:'channelCategoryId',index:'channelCategoryId',search: false,sortable:false,align:'center',width:'100'},
				{name:'channelAttributeId',index:'channelAttributeId',search: false, sortable:false,align:'center',width:'90'},							
				{name:'channelAttributeName',index:'channelAttributeName', sortable:false,align:'center',width:'140'},
				{name:'attributeCode',index:'attributeCode',search: false, sortable:false,align:'center',width:'130'},
				{name:'defaultValue',index:'defaultValue',search: false, sortable:false,align:'center',width:'100'},
				{name:'edit',index:'edit', sortable:false,search: false,align:'center',width:'155'}
			],
			postData:{
				"applicationId": applicationId
			},
			viewrecords : true,
			rowNum:10,
			rowList:[10,20,30],
			pager : mappedAttrPager_selector,
			altRows: true,
			autowidth:true,
			multiselect: false,
		    multiboxonly: true,
		    shrinkToFit:false,
		    gridComplete: function() {
		    	var table = this;
				setTimeout(function(){
					updatePagerIcons(table);
					enableTooltips(table);
				}, 0);
		    	
				var ids = jQuery(mappedAttr_selector).jqGrid("getDataIDs");
				for (var i = 0; i < ids.length; i++) {
					jQuery(mappedAttr_selector).jqGrid("setRowData", ids[i], {
						edit: "<button class='btn btn-xs btn-primary' onclick=attrEdit(" + ids[i] + ")><i class='icon-wrench align-top bigger-110'></i>修改</button>　<button class='btn btn-xs btn-danger' onclick=attrDelete(" + ids[i] + ")><i class='icon-wrench align-top bigger-110'></i>删除</button>"
					});
				}
			}
		});
		jQuery(mappedAttr_selector).jqGrid("filterToolbar", {
			searchOnEnter: true,
			enableClear: false
		});
		
		//属性值映射
		mappedAttrVal_selector = "#mappedAttrVal";
		mappedAttrValPager_selector = "#mappedAttrValPager";
		jQuery(mappedAttrVal_selector).jqGrid({
			url: ctx + "/system/tmall/mapping/attrval/loadAttrValMapping",
			mtype: "POST", 					 
			datatype: "json",
			//width: "100",
			height: "auto",
			colNames:['id','渠道类目Id','渠道属性id','渠道属性值id','渠道属性值','系统属性代号','系统属性值','操作'],
			colModel:[
                {name:'id',index:'id',sortable:false,align:'center',width:'100',hidden:true},
			    {name:'channelCategoryId',index:'channelCategoryId',search: false,sortable:false,align:'center',width:'110'},
				{name:'channelAttributeId',index:'channelAttributeId',search: false, sortable:false,align:'center',width:'100'},							
				{name:'channelAttributeValueId',index:'channelAttributeValueId',search: false, sortable:false,align:'center',width:'175'},
				{name:'channelAttributeValue',index:'channelAttributeValue', sortable:false,align:'center',width:'155'},
				{name:'attributeCode',index:'attributeCode',search: false, sortable:false,align:'center',width:'110'},
				{name:'attributeValue',index:'attributeValue', sortable:false,align:'center',width:'155'},
				{name:'edit',index:'edit', sortable:false,search: false,align:'center',width:'155'}
			],
			postData:{ 
				"applicationId":applicationId
			},
			viewrecords : true,
			rowNum:10,
			rowList:[10,20,30],
			pager : mappedAttrValPager_selector,
			altRows: true,
			autowidth:true,
			multiselect: false,
		    multiboxonly: true,
		    shrinkToFit:false,
		    gridComplete: function() {
				var table = this;
				setTimeout(function(){
					updatePagerIcons(table);
					enableTooltips(table);
				}, 0);
				
				var ids = jQuery(mappedAttrVal_selector).jqGrid("getDataIDs");
				for (var i = 0; i < ids.length; i++) {
					jQuery(mappedAttrVal_selector).jqGrid("setRowData", ids[i], {
						edit: "<button class='btn btn-xs btn-primary' onclick=attrValEdit(" + ids[i] + ")><i class='icon-wrench align-top bigger-110'></i>修改</button>　<button class='btn btn-xs btn-danger' onclick=attrValDelete(" + ids[i] + ")><i class='icon-wrench align-top bigger-110'></i>删除</button>"
					});
				}
			}
		});
		jQuery(mappedAttrVal_selector).jqGrid("filterToolbar", {
			searchOnEnter: true,
			enableClear: false
		});
		
		$(window).triggerHandler('resize.jqGrid');//trigger window resize to make the grid get the correct size
		
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
