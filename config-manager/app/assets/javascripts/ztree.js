function dblClickExpand(treeId, treeNode) {
	return treeNode.level > 0;
}

function go() {
	$.getJSON("getTreeNodes", buildTreeCallBack);
}

function buildTreeCallBack(configs) {
	var setting = {
		treeId : "#configTree",
		view : {
			dblClickExpand : false,
			showLine : true,
			editNameSelectAll : true,
			addHoverDom : createNode,
			removeHoverDom : removeHoverDom
		},
		data : {
			simpleData : {
				enable : true,
				idKey : "id",
				pIdKey : "pid"
			}
		},
		edit : {
			enable : true,
			showRemoveBtn : showRemoveBtn,
			showRenameBtn : showRenameBtn
		},
		callback : {
			beforeClick : onNodeClick,
			beforeDrag : beforeDrag,
			beforeEditName : beforeEditName,
			beforeRemove : remove,
			beforeRename : rename
		}

	};
	$.fn.zTree.init($("#configTree"), setting, configs);
}

function beforeDrag(treeId, treeNodes) {
	return false;
}
function beforeEditName(treeId, treeNode) {
	var zTree = $.fn.zTree.getZTreeObj("configTree");
	zTree.selectNode(treeNode);
	return confirm("确认修改节点" + treeNode.name + "吗？如果修改，那么要获取配置的path也要修改。");
}
function remove(treeId, treeNode) {
	var zTree = $.fn.zTree.getZTreeObj("configTree");
	zTree.selectNode(treeNode);
	var isDelete = confirm("确认删除节点" + treeNode.name + "吗？");
	if (isDelete) {
		$.ajax({
			url : "deleteNode",
			data : {
				"path" : treeNode.id
			},
			type : "GET",
			async : false,
			success : function(result) {
				isDelete = result;
			}
		});
	}
	if (!isDelete) {
		alert("删除节点失败");
	}
	return isDelete
}
function rename(treeId, treeNode, newName, isCancel) {
	if (isCancel)
		return;
	if (newName.length == 0 || newName.trim().length == 0) {
		alert("节点名称不能为空.");
		return false;
	}
	if (newName.trim().search("^~.*") != -1) {
		alert("节点不能以~开头,那是系统预留的");
		return false;
	}
	var ok = false;
	$.ajax({
		url : "updateNode",
		data : {
			"path" : treeNode.id,
			"newName" : newName
		},
		type : "GET",
		async : false,
		success : function(result) {
			ok = result;
		}
	});
	if (!ok) {
		alert("修改节点名失败");
	}
	return ok;
}
function showRemoveBtn(treeId, treeNode) {
	return !treeNode.isParent && treeNode.name.search("^~.*") == -1;
}
function showRenameBtn(treeId, treeNode) {
	return !treeNode.isParent && treeNode.name.search("^~.*") == -1;
}

function createNode(treeId, treeNode) {
	if (treeNode.name.search("^~.*") != -1)
		return;
	var sObj = $("#" + treeNode.tId + "_span");
	if (treeNode.editNameFlag || $("#addBtn_" + treeNode.tId).length > 0)
		return;
	var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
			+ "' title='add node' onfocus='this.blur();'></span>";
	sObj.after(addStr);
	var btn = $("#addBtn_" + treeNode.tId);
	if (btn)
		btn.bind("click", function() {
			var newName = prompt("请输入新建节点的名字", "newNode");
			var path = treeNode.id + "/" + newName;
			$.get("createNode", {
				"path" : path
			}, function(isOk) {
				if (isOk=="true") {
					var zTree = $.fn.zTree.getZTreeObj("configTree");
					zTree.addNodes(treeNode, {
						id : path,
						pId : treeNode.id,
						name : newName
					});
				}else{
					alert("创建节点失败");
				}
			});
			return true;

		});
};

function removeHoverDom(treeId, treeNode) {
	$("#addBtn_" + treeNode.tId).unbind().remove();
};
function selectAll() {
	var zTree = $.fn.zTree.getZTreeObj("configTree");
	zTree.setting.edit.editNameSelectAll = $("#selectAll").attr("checked");
}
