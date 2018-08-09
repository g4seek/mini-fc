$.ajax({
    type: "GET",
    url: parent.window.k8sOverviewUrl,
    data: {},
    success: function (result) {
        var data = result.data
        var podList = data.podList;
        for (let i = 0; i < podList.length; i++) {
            let podInfo = podList[i];
            let html = "<tr>" +
                "<td>" + podInfo.name + "</td>" +
                "<td>" + podInfo.ready + "</td>" +
                "<td>" + podInfo.status + "</td>" +
                "<td>" + podInfo.restarts + "</td>" +
                "<td>" + podInfo.age + "</td>" +
                "<td>" + podInfo.ip + "</td>" +
                "<td>" + podInfo.node + "</td>" +
                "</tr>";
            $('#podsTable tbody').append(html);
        }

        var deploymentList = data.deploymentList;
        for (let i = 0; i < deploymentList.length; i++) {
            let deploymentInfo = deploymentList[i];
            var desired = deploymentInfo.desired;
            var name = deploymentInfo.name;
            let html = "<tr>" +
                "<td>" + name + "</td>" +
                "<td>" + desired + "</td>" +
                "<td>" + deploymentInfo.current + "</td>" +
                "<td>" + deploymentInfo.upToDate + "</td>" +
                "<td>" + deploymentInfo.available + "</td>" +
                "<td>" + deploymentInfo.age + "</td>" +
                "<td>" + deploymentInfo.containers + "</td>" +
                "<td>" + deploymentInfo.images + "</td>" +
                "<td>" + deploymentInfo.selector + "</td>" +
                "<td>" +
                "<button class='btn btn-primary' onclick='scaleUp(" + desired + ",\"" + name + "\");'>扩容</button>" +
                "<button class='btn btn-danger' onclick='scaleDown(" + desired + ",\"" + name + "\");'>缩容</button>" +
                "</td>" +
                "</tr>";
            $('#deploymentsTable tbody').append(html);
        }

        var serviceList = data.serviceList;
        for (let i = 0; i < serviceList.length; i++) {
            let serviceInfo = serviceList[i];
            let html = "<tr>" +
                "<td>" + serviceInfo.name + "</td>" +
                "<td>" + serviceInfo.type + "</td>" +
                "<td>" + serviceInfo.clusterIp + "</td>" +
                "<td>" + serviceInfo.externalIp + "</td>" +
                "<td>" + serviceInfo.ports + "</td>" +
                "<td>" + serviceInfo.age + "</td>" +
                "<td>" + serviceInfo.selector + "</td>" +
                "</tr>";
            $('#servicesTable tbody').append(html);
        }
    }
});

$('#refreshBtn').click(function () {
    $("#consolePanel", window.parent.document).attr("src", "/html/k8s_overview.html");
});

$('#returnBtn').click(function () {
    $("#consolePanel", window.parent.document).attr("src", "/html/service_list.html");
});

function scaleUp(desired, deploymentName) {
    if (desired === 4) {
        alert("最多只能扩容到4个实例");
        return;
    }
    targetNum = desired + 1;
    k8sScale(targetNum, deploymentName);
}

function scaleDown(desired, deploymentName) {
    if (desired === 1) {
        alert("最多只能缩容到1个实例");
        return;
    }
    targetNum = desired - 1;
    k8sScale(targetNum, deploymentName);
}

function k8sScale(targetNum, deploymentName) {
    $.ajax({
        type: "GET",
        url: parent.window.k8sScaleUrl,
        data: {
            "targetNum": targetNum,
            "deploymentName": deploymentName
        },
        success: function (result) {
            alert("操作成功!");
            $("#consolePanel", window.parent.document).attr("src", "/html/k8s_overview.html");
        }
    });
}