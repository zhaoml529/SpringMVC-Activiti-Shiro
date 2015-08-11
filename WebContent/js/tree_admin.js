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
            isCopy: true,	//拖拽时, 设置是否允许复制节点
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
		alert(resourceId);
		
		var newNode = [{id:resourceId, name:name, pId:parentId}];
		newNode = zTree.addNodes(treeNode, newNode);
		//setTimeout(function () { zTree.expandNode(treeNode, true, true, true); }, 100);
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
			url: ctx+"/resource/updateTree",
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
	return true;
}

function beforeDragOpen(treeId, treeNode) {
	autoExpandNode = treeNode;
	return true;
}

function beforeDrop(treeId, treeNodes, targetNode, moveType, isCopy) {
	return true;
}

function onDrag(event, treeId, treeNodes) {
}

function onDrop(event, treeId, treeNodes, targetNode, moveType, isCopy) {
}
