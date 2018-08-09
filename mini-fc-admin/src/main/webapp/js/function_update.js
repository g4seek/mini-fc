$.ajax({
    type: "GET",
    url: parent.window.functionDetailUrl,
    data: {"id": parent.window.functionId},
    success: function (result) {
        var functionInfo = result.data.functionInfo;
        $('#functionId').val(functionInfo.id);
        $('#serviceName').val(functionInfo.serviceName);
        $('#functionName').val(functionInfo.functionName);
        $('#description').val(functionInfo.description);
        $('#execEnviroment').val(functionInfo.execEnviroment);
        $('#functionEntrance').val(functionInfo.functionEntrance);
        $('#sourceCode').val(functionInfo.sourceCode);
    }
});

$("#functionForm").submit(function (e) {
    $('button').attr("disabled", true);
    e.preventDefault();
    $.ajax({
        type: "POST",
        url: parent.window.functionUpdateUrl,
        data: $("#functionForm").serialize(),
        dataType: "json",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        success: function (result) {
            if (result.code.toString() === "200") {
                $("#consolePanel", window.parent.document).attr("src", "/html/service_detail.html");
            } else {
                alert(result.errorMsg);
                $('button').attr("disabled", false);
            }
        }
    });
});

$("#returnBtn").click(function () {
    $("#consolePanel", window.parent.document).attr("src", "/html/service_detail.html");
});
