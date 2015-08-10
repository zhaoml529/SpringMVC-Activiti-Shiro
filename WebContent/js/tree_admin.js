/**
 * admin 可编辑菜单
 */
function zTreeOnClick(event, treeId, treeNode) {
    event.preventDefault();		//阻止zTree自动打开连接，默认为 target='_blank'
    if(treeNode.url != '#' || treeNode.url == '') {
	    addTab(treeNode.name, treeNode.url);	//easyui添加Tab
    }
};

var setting_admin = {
	data: {
		simpleData: {
			enable: true,
			idKey: "id",
			pIdKey: "parentId",
			rootPId: 0
		}
	},
	showLine : true,                		//是否显示节点间的连线
	checkable : false,               		//每个节点上是否显示 CheckBox
	callback: {
		onClick: zTreeOnClick,				//点击节点时
		beforeRename: zTreeBeforeRename,	//修改节点之前
		onRename: zTreeOnRename
	},
	edit: {
        enable: true,
        showRemoveBtn: setRemoveBtn,
        showRenameBtn: true,	//显示编辑按钮
        drag: {
            prev: true,//允许向上拖动
            next: true,//不允许向下拖动
            inner: true//允许当前层次内进行拖动
        }
    }
}

function zTreeBeforeRename(treeId, treeNode, newName, isCancel) {
	if (newName.length == 0) {
        $.messager.alert("提示", "节点名称不能为空!","info", function() {
        	zTree.editName(treeNode); //输入框获取焦点
        });
        return false;
    }
    return true;
}

function zTreeOnRename(event, treeId, treeNode, isCancel) {
	alert(treeNode.id + ", " + treeNode.name);
	$.ajax({
		async: false,			//同步，等待success完成后继续执行。
		cache: false,
		url: ctx+"/menu",
		data: { id: treeNode.id, name: treeNode.name },
		type: 'POST',
		dataType: "json",
		error: function () {
			alert('请求失败');
		},
		success: function(data){ 
			treeNodes = data;
		}
	});

}

//所有父节点不显示删除按钮
function setRemoveBtn(treeId, treeNode) {
	return !treeNode.isParent;
}