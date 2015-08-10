/**
 * admin 可编辑菜单
 */
function zTreeOnClick(event, treeId, treeNode) {
    event.preventDefault();		//阻止zTree自动打开连接，默认为 target='_blank'
    if(treeNode.url != '#' || treeNode.url == '') {
	    addTab(treeNode.name, treeNode.url);	//easyui添加Tab
    }else{
    	zTree.expandNode(treeNode);				//展开节点
    }
};

var setting_admin = {
	view: {
		addHoverDom: addHoverDom,
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
		onClick: zTreeOnClick,				//点击节点时
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
	$.messager.confirm('提示','确定删除此菜单？', function (result) {
		return result;
	});
	return false;
}

var newCount = 1;
function addHoverDom(treeId, treeNode) {
	var sObj = $("#" + treeNode.tId + "_span");
	if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
	var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
		+ "' title='add node' onfocus='this.blur();'></span>";
	sObj.after(addStr);
	var btn = $("#addBtn_"+treeNode.tId);
	if (btn) btn.bind("click", function(){
		//var zTree = $.fn.zTree.getZTreeObj("treeDemo");
		zTree.addNodes(treeNode, {id:(100 + newCount), pId:treeNode.id, name:"new node" + (newCount++)});
		return false;
	});
};
function removeHoverDom(treeId, treeNode) {
	$("#addBtn_"+treeNode.tId).unbind().remove();
};

//更新
function onRename(event, treeId, treeNode, isCancel) {
	alert(treeNode.id + ", " + treeNode.name);
	$.ajax({
		async: false,			//同步，等待success完成后继续执行。
		cache: false,
		url: ctx+"/resource/doUpdateName",
		data: { id: treeNode.id, name: treeNode.name },
		type: 'POST',
		dataType: "json",
		success: function(data){ 
			if (data.status) {
				zTree.reAsyncChildNodes(null, "refresh");	//重新异步加载 zTree
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

//删除
function onRemove(event, treeId, treeNode) {
	alert(treeNode.tId + ", " + treeNode.name);
	$.ajax({
		async: false,			//同步，等待success完成后继续执行。
		cache: false,
		url: ctx+"/resource/doDelete",
		data: { id: treeNode.id },
		type: 'POST',
		dataType: "json",
		success: function(data){ 
			if (data.status) {
				zTree.reAsyncChildNodes(null, "refresh");	//重新异步加载 zTree
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
	className = (className === "dark" ? "":"dark");
	showLog("[ "+getTime()+" beforeDrop ]&nbsp;&nbsp;&nbsp;&nbsp; moveType:" + moveType);
	showLog("target: " + (targetNode ? targetNode.name : "root") + "  -- is "+ (isCopy==null? "cancel" : isCopy ? "copy" : "move"));
	return true;
}

function onDrag(event, treeId, treeNodes) {
	className = (className === "dark" ? "":"dark");
	showLog("[ "+getTime()+" onDrag ]&nbsp;&nbsp;&nbsp;&nbsp; drag: " + treeNodes.length + " nodes." );
}

function onDrop(event, treeId, treeNodes, targetNode, moveType, isCopy) {
	className = (className === "dark" ? "":"dark");
	showLog("[ "+getTime()+" onDrop ]&nbsp;&nbsp;&nbsp;&nbsp; moveType:" + moveType);
	showLog("target: " + (targetNode ? targetNode.name : "root") + "  -- is "+ (isCopy==null? "cancel" : isCopy ? "copy" : "move"))
}
//所有父节点不显示删除按钮
function setRemoveBtn(treeId, treeNode) {
	return !treeNode.isParent;
}