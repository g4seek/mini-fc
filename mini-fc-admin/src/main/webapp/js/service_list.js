$.ajax({
    type: "GET",
    url: parent.window.serviceListUrl,
    data: {},
    success: function (result) {
        var serviceInfoList = result.data;
        for (let i = 0; i < serviceInfoList.length; i++) {
            let serviceInfo = serviceInfoList[i];
            let html = "<tr>" +
                "<td>" + serviceInfo.id + "</td>" +
                "<td>" + serviceInfo.serviceName + "</td>" +
                "<td>" + serviceInfo.description + "</td>" +
                "<td>" +
                "<button class='btn btn-default' onclick='viewServiceDetail(" + serviceInfo.id + ");'>详情</button> " +
                "<button class='btn btn-primary' onclick='updateService(" + serviceInfo.id + ");'>修改</button> " +
                "<button class='btn btn-danger' onclick='deleteService(this," + serviceInfo.id + ");'>删除</button>" +
                "</td>" +
                "</tr>";
            $('#serviceTable tbody').append(html);
        }
    }
});

$("#serviceCreateBtn").click(function () {
    $("#consolePanel", window.parent.document).attr("src", "/html/service_create.html")
});

$('#k8sOverviewBtn').click(function () {
    $("#consolePanel", window.parent.document).attr("src", "/html/k8s_overview.html")
});

function viewServiceDetail(serviceId) {
    parent.window.serviceId = serviceId;
    $("#consolePanel", window.parent.document).attr("src", "/html/service_detail.html");
}

function updateService(serviceId) {
    parent.window.serviceId = serviceId;
    $("#consolePanel", window.parent.document).attr("src", "/html/service_update.html");

}

function deleteService(ele, serviceId) {
    var buttons = $('button');
    buttons.attr("disabled", true);
    $.ajax({
        type: "GET",
        url: parent.window.serviceDeleteUrl,
        data: {"id": serviceId},
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