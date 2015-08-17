var setting_user = {
	data: {
		simpleData: {
			enable: true,
			idKey: "id",
			pIdKey: "parentId",
			rootPId: 0
		}
	},
	showLine : true,                //是否显示节点间的连线
	checkable : false,               //每个节点上是否显示 CheckBox
	callback: {
		onClick: onClick
	}
};

function onClick(event, treeId, treeNode) {
    if(treeNode.url != null && treeNode.url != '' && treeNode.url != '#') {
	    addTab(treeNode.name, treeNode.url);	//easyui添加Tab
    }else{
    	zTree.expandNode(treeNode);				//单击展开节点(默认为双击)
    }
};