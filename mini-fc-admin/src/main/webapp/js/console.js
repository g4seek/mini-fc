var src = "/html/service_list.html";

var serviceId = null;
var functionId = null;
var triggerId = null;

var serviceDetailUrl = "/service/detail";
var serviceListUrl = "/service/list";
var serviceCreateUrl = "/service/create";
var serviceUpdateUrl = "/service/update";
var serviceDeleteUrl = "/service/delete";

var functionDetailUrl = "/function/detail";
var functionCreateUrl = "/function/create";
var functionUpdateUrl = "/function/update";
var functionDeleteUrl = "/function/delete";
var functionExecuteUrl = "/function/execute";
var functionLogUrl = "/function/log";
var functionProxyUrl = "/function/proxy";
var functionUploadUrl = "/function/upload";

var k8sFunctionExecuteUrl = "/function/k8sExecute";
var k8sFunctionLogUrl = "/function/k8sLog";

var triggerDetailUrl = "/trigger/detail";
var triggerCreateUrl = "/trigger/create";
var triggerDeleteUrl = "/trigger/delete";

var k8sOverviewUrl = "/k8s/overview";
var k8sScaleUrl = "/k8s/scale";

$("#consolePanel", window.parent.document).attr("src", src);