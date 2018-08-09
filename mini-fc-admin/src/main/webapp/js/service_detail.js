$.ajax({
    type: "GET",
    url: parent.window.serviceDetailUrl,
    data: {"id": parent.window.serviceId},
    success: function (result) {
        var serviceInfo = result.data.serviceInfo;
        $('#serviceId').text(serviceInfo.id);
        $('#serviceName').text(serviceInfo.serviceName);
        $('#description').text(serviceInfo.description);
        var functionInfoList = result.data.functionInfoList;
        for (let i = 0; i < functionInfoList.length; i++) {
            let functionInfo = functionInfoList[i];
            let html = "<tr>" +
                "<td>" + functionInfo.id + "</td>" +
                "<td>" + functionInfo.functionName + "</td>" +
                "<td>" + functionInfo.description + "</td>" +
                "<td>" +
                "<button class='btn btn-default' onclick='viewFunctionDetail(" + functionInfo.id + ");'>详情</button> " +
                "<button class='btn btn-primary' onclick='updateFunction(" + functionInfo.id + ");'>修改</button> " +
                "<button class='btn btn-danger' onclick='deleteFunction(this," + functionInfo.id + ");'>删除</button> " +
                "</td>" +
                "</tr>";
            $('#functionTable tbody').append(html);
        }
    }
});

$('#createFunctionBtn').click(function () {
    $("#consolePanel", window.parent.document).attr("src", "/html/function_create.html");
});

$("#returnBtn").click(function () {
    $("#consolePanel", window.parent.document).attr("src", "/html/service_list.html");
});

function viewFunctionDetail(functionId) {
    parent.window.functionId = functionId;
    $("#consolePanel", window.parent.document).attr("src", "/html/function_detail.html");
}

function updateFunction(functionId) {
    parent.window.functionId = functionId;
    $("#consolePanel", window.parent.document).attr("src", "/html/function_update.html");
}

function deleteFunction(ele, functionId) {
    var buttons = $('button');
    buttons.attr("disabled", true);
    $.ajax({
        type: "GET",
        url: parent.window.functionDeleteUrl,
        data: {"id": functionId},
        success: function (result) {
            if (result.code.toString() === '200') {
                $(ele).parent().parent().remove();
            } else {
                alert(result.errorMsg);
            }
        }
    });
    buttons.attr("disabled", false);
}