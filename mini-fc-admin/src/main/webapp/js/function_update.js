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
        $('#uploadFilePath').val(functionInfo.uploadFilePath);
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

$("#uploadBtn").click(function () {
    var formData = new FormData();
    formData.append('sourceFile', $('#sourceFile')[0].files[0]);
    $.ajax({
        type: "POST",
        url: parent.window.functionUploadUrl,
        data: formData,
        // dataType: "json",
        contentType: false,
        processData: false,
        success: function (result) {
            if (result.code.toString() === "200") {
                $("#uploadFilePath").val(result.data);
            } else {
                alert(result.errorMsg);
            }
        }
    });
});

$("#returnBtn").click(function () {
    $("#consolePanel", window.parent.document).attr("src", "/html/service_detail.html");
});
