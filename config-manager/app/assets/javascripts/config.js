function buildConfigForm(config) {
	if (config != null && config != "null") {
		$("#configPath").val(config.path)
		$("#version").val(config.version)
		$("#params").empty()
		addParameters(config.parentParameters, "pk_", "pv_", true)
		addParameters(config.selfParameters, "k_", "v_", false)
	}
}

var selfParameterIndex = 1;

function addParameters(map, keyStart, valueStart, readOnly) {
	var index = 1;
	for ( var key in map) {
		addInput(keyStart + index, valueStart + index, key, map[key], readOnly)
		index++;
		selfParameterIndex++;
	}
}

function addInput(keyName, valueName, key, value, readOnly) {
	$("#params").append("<div id='" + keyName + "'><input name='" + keyName + "' value='" + key + "' " + (readOnly ? " readonly='readonly'" : "") + " />= <input name='" + valueName + "' value='" + value + "' " + (readOnly ? " readonly='readonly'" : "") + "/></div>");
	if (!readOnly) {
		$("#" + keyName).append("<a href=\"javascript:deleteConfig('" + keyName + "')\">删除</a>");
	}
}

function deleteConfig(id) {
	$("#" + id).remove();
}

function addParameter() {
	addInput("k_" + selfParameterIndex, "v_" + selfParameterIndex, "", "", false)
	selfParameterIndex++;
}

function saveConfig() {
	$("#configForm").ajaxSubmit(function(ok) {
		if(ok){
			alert("保存配置成功");
		}else{
			alert("保存配置失败");
		}
	});
}

