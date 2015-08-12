/**
 * admin 可编辑菜单
 */
var setting_admin = {
	async: {
		enable: true,
		url:ctx+"/menu",
		autoParam:["id"],
		type: 'POST',
		dataType: "json"
	},
	view: {
		addHoverDom: addHoverDom,//自定义添加节点，官方没有提供直接添加的接口
		removeHoverDom: removeHoverDom,
		selectedMulti: false,	//是否允许同时选中多个节点
		showLine : true,		//是否显示节点间的连线
		dblClickExpand: false	//双击节点时，是否自动展开父节点的标识
	},
	edit: {
        enable: true,
        showRemoveBtn: setRemoveBtn,
        showRenameBtn: true,		//显示编辑按钮
        drag: {
        	autoExpandTrigger: true,	//拖拽时父节点自动展开
            prev: dropPrev,		//允许向上拖动
            next: dropNext,		//允许向下拖动
            inner: dropInner,	//允许当前层次内进行拖动
            isCopy: false,	//拖拽时, 设置是否允许复制节点
            isMove: true    //拖拽时, 设置是否允许移动节点
        }
    },
	data: {
		simpleData: {
			enable: true,
			idKey: "id",
			pIdKey: "parentId",
			rootPId: 0
		}
	},
	callback: {
		onClick: onClick,				//点击节点时
		beforeRename: beforeRename,	//修改节点之前
		beforeRemove: beforeRemove,	//删除之前
		
		onRename: onRename,					
		onRemove: onRemove,
		
		beforeDrag: beforeDrag,
		beforeDrop: beforeDrop,
		beforeDragOpen: beforeDragOpen,
		onDrag: onDrag,
		onDrop: onDrop
		
	}
}

//所有父节点不显示删除按钮
function setRemoveBtn(treeId, treeNode) {
	return !treeNode.isParent;
}

function beforeRename(treeId, treeNode, newName, isCancel) {
	if (newName.length == 0) {
        $.messager.alert("提示", "节点名称不能为空!","info", function() {
        	zTree.editName(treeNode); //输入框获取焦点
        });
        return false;
    }
    return true;
}

function beforeRemove(treeId, treeNode) {
	zTree.selectNode(treeNode);
//	$.messager.confirm('提示','确定删除此菜单？', function (result) {	//此方法不能实现，因为它是同步的
//		if(result){
//
//		}
//	});
	return confirm("确认删除节点【" + treeNode.name + "】吗？");
}

function onClick(event, treeId, treeNode) {
    event.preventDefault();		//阻止zTree自动打开连接，默认为 target='_blank'
    if(treeNode.url != '#' || treeNode.url == '') {
	    addTab(treeNode.name, treeNode.url);	//easyui添加Tab
    }else{
    	zTree.expandNode(treeNode);				//单击展开节点(默认为双击)
    }
};

var newCount = 1, new_Tid;
function addHoverDom(treeId, treeNode) {
	var sObj = $("#" + treeNode.tId + "_span");
	if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
	var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
		+ "' title='add' onfocus='this.blur();'></span>";
	sObj.after(addStr);
	var btn = $("#addBtn_"+treeNode.tId);
	if (btn) btn.bind("click", function(){
		var parentId = treeNode.id;
		var name = "新节点" + (newCount++);
		var resourceId = onAdd(parentId, name);	//数据库中添加节点
		var newNode = [{id:resourceId, name:name, pId:parentId}];
		newNode = zTree.addNodes(treeNode, newNode);
		//获取数的排序
		var NodeArr = getSortAfterNodes();
		updateTreeSort(resourceId, NodeArr, false);
		
		zTree.editName(newNode[0]);
		return false;
	});
};
function removeHoverDom(treeId, treeNode) {
	$("#addBtn_"+treeNode.tId).unbind().remove();
};

//添加
function onAdd(pId, name) {
	var resourceId;
	$.ajax({
		async: false,			//同步，等待success完成后继续执行。
		cache: false,
		url: ctx+"/resource/addTree",
		data: { parentId: pId, name: name },
		type: 'POST',
		dataType: "json",
		success: function(result){ 
			if(result.status){
				resourceId = result.data;
				//zTree.reAsyncChildNodes(null, "refresh");	//重新异步加载 zTree
			}
			$.messager.show({
				title : result.title,
				msg : result.message,
				icon: "info",			//error、question、info、warning
				timeout : 1000 * 2
			});
		}
	});
	return resourceId;
}

//更新
function onRename(event, treeId, treeNode, isCancel) {
	if(!isCancel){	//是否取消编辑
		$.ajax({
			async: false,			//同步，等待success完成后继续执行。
			cache: false,
			url: ctx+"/resource/updateTreeName",
			data: { id: treeNode.id, name: treeNode.name },
			type: 'POST',
			dataType: "json",
			success: function(data){ 
				if (data.status) {
					zTree.reAsyncChildNodes(treeNode, "refresh");	//重新异步加载 zTree
				} 
				$.messager.show({
					title : data.title,
					msg : data.message,
					icon: "info",			//error、question、info、warning
					timeout : 1000 * 2
				});
			}
		});
	}else{
		zTree.cancelEditName();
    }
}

//删除
function onRemove(event, treeId, treeNode) {
	$.ajax({
		async: false,			//同步，等待success完成后继续执行。
		cache: false,
		url: ctx+"/resource/doDelete",
		data: { id: treeNode.id },
		type: 'POST',
		dataType: "json",
		success: function(data){ 
			if (data.status) {
				zTree.reAsyncChildNodes(treeNode, "refresh");	//重新异步加载 zTree
			} 
			$.messager.show({
				title : data.title,
				msg : data.message,
				icon: "info",			//error、question、info、warning
				timeout : 1000 * 2
			});
		}
	});
}

function dropPrev(treeId, nodes, targetNode) {
	var pNode = targetNode.getParentNode();
	if (pNode && pNode.dropInner === false) {
		return false;
	} else {
		for (var i=0,l=curDragNodes.length; i<l; i++) {
			var curPNode = curDragNodes[i].getParentNode();
			if (curPNode && curPNode !== targetNode.getParentNode() && curPNode.childOuter === false) {
				return false;
			}
		}
	}
	return true;
}

function dropInner(treeId, nodes, targetNode) {
	if (targetNode && targetNode.dropInner === false) {
		return false;
	} else {
		for (var i=0,l=curDragNodes.length; i<l; i++) {
			if (!targetNode && curDragNodes[i].dropRoot === false) {
				return false;
			} else if (curDragNodes[i].parentTId && curDragNodes[i].getParentNode() !== targetNode && curDragNodes[i].getParentNode().childOuter === false) {
				return false;
			}
		}
	}
	return true;
}

function dropNext(treeId, nodes, targetNode) {
	var pNode = targetNode.getParentNode();
	if (pNode && pNode.dropInner === false) {
		return false;
	} else {
		for (var i=0,l=curDragNodes.length; i<l; i++) {
			var curPNode = curDragNodes[i].getParentNode();
			if (curPNode && curPNode !== targetNode.getParentNode() && curPNode.childOuter === false) {
				return false;
			}
		}
	}
	return true;
}

var log, className = "dark", curDragNodes, autoExpandNode;
function beforeDrag(treeId, treeNodes) {
	for (var i=0,l=treeNodes.length; i<l; i++) {
		if (treeNodes[i].drag === false) {
			curDragNodes = null;
			return false;
		} else if (treeNodes[i].parentTId && treeNodes[i].getParentNode().childDrag === false) {
			curDragNodes = null;
			return false;
		}
	}
	curDragNodes = treeNodes;
	//alert("beforeDrag:节点被拖拽之前的事件回调函数，并且根据返回值确定是否允许开启拖拽操作");
	//$("#divSortContent").empty();
	return true;
}

function beforeDragOpen(treeId, treeNode) {
	autoExpandNode = treeNode;
	//alert("beforeDragOpen:捕获拖拽节点移动到折叠状态的父节点后，即将自动展开该父节点之前的事件回调函数，并且根据返回值确定是否允许自动展开操作");
	return true;
}

function beforeDrop(treeId, treeNodes, targetNode, moveType, isCopy) {
	//alert("beforeDrop:节点拖拽操作结束之前的事件回调函数，并且根据返回值确定是否允许此拖拽操作");
	return targetNode ? targetNode.drop !== false : true;
}

function onDrag(event, treeId, treeNodes) {
	//alert("onDrag:进行拖拽节点后");
}

function onDrop(event, treeId, treeNodes, targetNode, moveType, isCopy) {
	//alert("onDrop:每次拖拽操作结束后");
	//treeNodes[0]的前提是只能单个拖拽
	var NodeArr = getSortAfterNodes();
	updateTreeSort(treeNodes[0].id, NodeArr, true);
}

//更新拖拽后树的顺序
//nodeId: 拖拽的节点id
//NodeArr: 排序后的节点信息
//showMsg: 是否显示提示信息
function updateTreeSort(nodeId, NodeArr, showMsg) {
	var parentId;
	var sortArr = new Array();
	var nodeArr = new Array();
	for (var i = 0; i < NodeArr.length; i++) {
		if(nodeId == NodeArr[i].id){
			//更新节点的sortNum到数据库
			parentId = NodeArr[i].pId;
			break;
		}
	}
	for (var i = 0; i < NodeArr.length; i++) {
		if(parentId == NodeArr[i].pId){
			nodeArr.push(NodeArr[i].id);
			sortArr.push(NodeArr[i].sortNum);
		}
	}
	
	$.ajax({
		async: false,
		cache: false,
		url: ctx+"/resource/updateTreeSort",
		data: { sortArr: sortArr.toString(), nodeArr: nodeArr.toString() },
		type: 'POST',
		dataType: "json",
		success: function (data) {
			$.messager.progress("close");
			if(showMsg){
				$.messager.show({
					title : data.title,
					msg : data.message,
					icon: "info",			//error、question、info、warning
					timeout : 1000 * 2
				});
			}
		}
	});
}

//递归遍历旗下子节点
function ForeachFindChildNode(nodes, NodeArr) {
	//遍历获取每一个节点
	for (var i = 0; i < nodes.children.length; i++) {
	    var node = new Object();
	    node.id = nodes.children[i].id;
	    node.name = nodes.children[i].name;
	    node.pId = nodes.children[i].parentId;
	    node.sortNum = i+1;
	    //将new后的新对象附加到数组内
	    NodeArr.push(node);
	    //判断其是否有子节点 有且进入递归查找
	    if (nodes.children[i].children != null && nodes.children[i].children.length > 0) {
	        ForeachFindChildNode(nodes.children[i], NodeArr);
	    }
	}
}

//获得排序后的节点数组
function getSortAfterNodes() {
    //定义一个数组用于存放排序后的节点信息
    var NodeArr = new Array();
    //获得树对象
    //var treeLeftObj = $.fn.zTree.getZTreeObj("treeDemo");
    //拿到所有节点
    var nodes = zTree.getNodes();
    //遍历获取每一个节点
    for (var i = 0; i < nodes.length; i++) {
        var node = new Object();
        node.id = nodes[i].id;
        node.name = nodes[i].name;
        node.pId = nodes[i].parentId;
        node.sortNum = i+1;
        //将new后的新对象附加到数组内
        NodeArr.push(node);
        //判断其是否有子节点 有且进入递归查找
        if (nodes[i].children != null && nodes[i].children.length > 0) {
            ForeachFindChildNode(nodes[i], NodeArr);
        }
    }
    var divObj = $("#divSortContent");
    for (var i = 0; i < NodeArr.length; i++) {
    	divObj.append(NodeArr[i].id+"   "+NodeArr[i].name+"   "+NodeArr[i].pId + "  " + NodeArr[i].sortNum);
    	divObj.append("<br/>");
    }
    divObj.append("------------------------------<br/>");
    return NodeArr;
}
