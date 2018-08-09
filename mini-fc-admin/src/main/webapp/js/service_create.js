$("#serviceForm").submit(function (e) {
    e.preventDefault();
    $.ajax({
        type: "POST",
        url: parent.window.serviceCreateUrl,
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
