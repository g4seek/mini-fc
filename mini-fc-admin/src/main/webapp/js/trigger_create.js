$.ajax({
    type: "GET",
    url: parent.window.functionDetailUrl,
    data: {"id": parent.window.functionId},
    success: function (result) {
        var functionInfo = result.data.functionInfo;
        $('#serviceName').val(functionInfo.serviceName);
        $('#functionName').val(functionInfo.functionName);
    }
});

$("#triggerForm").submit(function (e) {
    e.preventDefault();
    $.ajax({
        type: "POST",
        url: parent.window.triggerCreateUrl,
        data: $("#triggerForm").serialize(),
        dataType: "json",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        success: function (result) {
            if (result.code.toString() === "200") {
                $("#consolePanel", window.parent.document).attr("src", "/html/function_detail.html");
            } else {
                alert(result.errorMsg);
            }
        }
    });
});

$("#returnBtn").click(function () {
    $("#consolePanel", window.parent.document).attr("src", "/html/function_detail.html");
});
