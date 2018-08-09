$.ajax({
    type: "GET",
    url: parent.window.triggerDetailUrl,
    data: {"id": parent.window.triggerId},
    success: function (result) {
        var triggerInfo = result.data.triggerInfo;
        $('#triggerId').text(triggerInfo.id);
        $('#serviceName').text(triggerInfo.serviceName);
        $('#functionName').text(triggerInfo.functionName);
        $('#triggerName').text(triggerInfo.triggerName);
        $('#triggerType').text(triggerInfo.triggerType);
        $('#triggerConfig').text(triggerInfo.triggerConfig);
    }
});

$("#returnBtn").click(function () {
    $("#consolePanel", window.parent.document).attr("src", "/html/function_detail.html");
});
