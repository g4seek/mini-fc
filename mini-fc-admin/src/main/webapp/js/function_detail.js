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
        if (functionInfo.sourceCode !== '') {
            $('#sourceCode').text(functionInfo.sourceCode);
            if (functionInfo.execEnviroment === "Java8") {
                $('#sourceCode').addClass("language-java")
            } else if (functionInfo.execEnviroment === "Python2.7") {
                $('#sourceCode').addClass("language-python")
            }
            $('#sourceCodeDiv').show();
            Prism.highlightAll();

        }
        if (functionInfo.uploadFilePath !== '') {
            $('#uploadFilePath').text(functionInfo.uploadFilePath);
            $('#uploadFilePathDiv').show();
        }

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

    }
});

$("#executeTab a:first").tab("show");
$("#jsonRequestDiv").hide();
/*
$('#createTriggerBtn').click(function () {
    $("#consolePanel", window.parent.document).attr("src", "/html/trigger_create.html");
});
*/
$("#returnBtn").click(function () {
    $("#consolePanel", window.parent.document).attr("src", "/html/service_detail.html");
});
$("#showFunctionUrlBtn").click(function () {
    $.ajax({
        type: "GET",
        url: parent.window.functionExecuteUrl,
        data: {"id": parent.window.functionId},
        success: function (result) {
            if (result.code.toString() === '200') {
                var functionUri = result.data;
                $('#functionUri').val(functionUri);
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

$("#k8sShowFunctionUrlBtn").click(function () {
    $.ajax({
        type: "GET",
        url: parent.window.k8sFunctionExecuteUrl,
        data: {"id": parent.window.functionId},
        success: function (result) {
            if (result.code.toString() === '200') {
                var functionUri = result.data;
                $('#functionUri').val(functionUri);
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

$("input[name='requestTypeRadio']").change(function () {
    var value = $("input[name='requestTypeRadio']:checked").val();
    if (value === "json") {
        $("#formRequestDiv").hide();
        $("#jsonRequestDiv").show();
    } else if (value === "form") {
        $("#formRequestDiv").show();
        $("#jsonRequestDiv").hide();
    }
});

$("#executeTab a").click(function (e) {
    e.preventDefault();
    $(this).tab('show');
});

$("#functionExecuteBtn").click(function () {
    var requestType = $("input[name='requestTypeRadio']:checked").val();
    var requestUri = $('#functionUri').val();
    var requestData = $("#jsonRequestData").val();

    $.ajax({
        type: "POST",
        url: parent.window.functionProxyUrl,
        data: {
            requestUri: requestUri,
            requestData: requestData,
            method: "POST",
            contentType: requestType
        },
        success: function (result) {
            if (result.code.toString() === '200') {
                $("#executeResultSpan").text(result.data);
            } else {
                $("#executeResultSpan").text(result.errorMsg);
            }
        }
    });
});

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