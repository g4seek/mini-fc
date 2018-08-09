$.ajax({
    type: "GET",
    url: parent.window.functionDetailUrl,
    data: {"id": parent.window.functionId},
    success: function (result) {
        var functionInfo = result.data.functionInfo;
        $('#functionId').text(functionInfo.id);
        $('#serviceName').text(functionInfo.serviceName);
        $('#functionName').text(functionInfo.functionName);
        $('#description').text(functionInfo.description);
        $('#execEnviroment').text(functionInfo.execEnviroment);
        $('#functionEntrance').text(functionInfo.functionEntrance);
        $('#functionVersion').text(functionInfo.functionVersion);
        $('#sourceCode').text(functionInfo.sourceCode);
        /*
        var triggerInfoList = result.data.triggerInfoList;
        for (let i = 0; i < triggerInfoList.length; i++) {
            let triggerInfo = triggerInfoList[i];
            let html = "<tr>" +
                "<td>" + triggerInfo.id + "</td>" +
                "<td>" + triggerInfo.triggerName + "</td>" +
                "<td>" + triggerInfo.triggerType + "</td>" +
                "<td>" +
                "<button class='btn btn-default' onclick='viewTriggerDetail(" + triggerInfo.id + ");'>详情</button>" +
                "<button class='btn btn-danger' onclick='deleteTrigger(this," + triggerInfo.id + ");'>删除</button>" +
                "</td>" +
                "</tr>";
            $('#triggerTable tbody').append(html);
        }
        */
        Prism.highlightAll();
    }
});

$("#executeTab a:first").tab("show");
/*
$('#createTriggerBtn').click(function () {
    $("#consolePanel", window.parent.document).attr("src", "/html/trigger_create.html");
});
*/
$("#returnBtn").click(function () {
    $("#consolePanel", window.parent.document).attr("src", "/html/service_detail.html");
});
$("#executeFunctionBtn").click(function () {
    $.ajax({
        type: "GET",
        url: parent.window.functionExecuteUrl,
        data: {"id": parent.window.functionId},
        success: function (result) {
            if (result.code.toString() === '200') {
                var functionUri = result.data;
                window.open(functionUri, "函数执行-" + parent.window.functionId, null, true);
                $('#functionUri').text(functionUri);
            } else {
                alert(result.errorMsg);
            }
        }
    });
});

$("#showExecuteLogBtn").click(function () {
    $.ajax({
        type: "GET",
        url: parent.window.functionLogUrl,
        data: {"id": parent.window.functionId},
        success: function (result) {
            if (result.code.toString() === '200') {
                $("#functionLog").text(result.data);
            } else {
                $("#functionLog").text(result.errorMsg);
            }
        }
    });
});

$("#k8sExecuteFunctionBtn").click(function () {
    $.ajax({
        type: "GET",
        url: parent.window.k8sFunctionExecuteUrl,
        data: {"id": parent.window.functionId},
        success: function (result) {
            if (result.code.toString() === '200') {
                var functionUri = result.data;
                window.open(functionUri, "函数执行-" + parent.window.functionId, null, true);
                $('#k8sFunctionUri').text(functionUri);
            } else {
                alert(result.errorMsg);
            }
        }
    });
});

$("#k8sShowExecuteLogBtn").click(function () {
    $.ajax({
        type: "GET",
        url: parent.window.k8sFunctionLogUrl,
        data: {"id": parent.window.functionId},
        success: function (result) {
            if (result.code.toString() === '200') {
                $("#k8sFunctionLog").text(result.data);
            } else {
                $("#k8sFunctionLog").text(result.errorMsg);
            }
        }
    });
});

$("#executeTab a").click(function (e) {
    e.preventDefault();
    $(this).tab('show');
})

/*
function viewTriggerDetail(triggerId) {
    parent.window.triggerId = triggerId;
    $("#consolePanel", window.parent.document).attr("src", "/html/trigger_detail.html");
}

function deleteTrigger(ele, triggerId) {
    var buttons = $('button');
    buttons.attr("disabled", true);
    $.ajax({
        type: "GET",
        url: parent.window.triggerDeleteUrl,
        data: {"id": triggerId},
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
*/