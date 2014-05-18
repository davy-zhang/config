
function onNodeClick(treeId, treeNode) {
	$("#config").show()
	$.getJSON("getConfig?path="+treeNode.id, buildConfigForm);
	return true;
}
