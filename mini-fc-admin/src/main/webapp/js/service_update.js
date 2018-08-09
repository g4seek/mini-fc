$.ajax({
    type: "GET",
    url: parent.window.serviceDetailUrl,
    data: {"id": parent.window.serviceId},
    success: function (result) {
        var serviceInfo = result.data.serviceInfo;
        $('#serviceId').val(serviceInfo.id);
        $('#serviceName').val(serviceInfo.serviceName);
        $('#description').val(serviceInfo.description);
    }
});

$("#serviceForm").submit(function (e) {
    e.preventDefault();
    $.ajax({
        type: "POST",
        url: parent.window.serviceUpdateUrl,
        data: $("#serviceForm").serialize(),
        dataType: "json",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        success: function (result) {
            if (result.code.toString() === "200") {
                $("#consolePanel", window.parent.document).attr("src", "/html/service_list.html");
            } else {
                alert(result.errorMsg);
            }
        }
    });
});

$("#returnBtn").click(function () {
    $("#consolePanel", window.parent.document).attr("src", "/html/service_list.html");
});
