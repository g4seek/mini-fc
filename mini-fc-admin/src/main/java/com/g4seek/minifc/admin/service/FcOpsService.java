/**
 * @(#)FcOpsService.java, 2018年07月07日.
 */
package com.g4seek.minifc.admin.service;

import org.nutz.http.Http;
import org.nutz.http.Response;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * 函数计算运维平台服务
 *
 * @author hzlvzimin
 */
@IocBean
public class FcOpsService {

    @Inject
    private PropertiesProxy appConf;

    public String createFunction(Long functionId) {
        Response response = Http.get(getFcsUrl() + "/createFunction?functionId=" + functionId);
        return response.getContent();
    }

    public String deleteFunction(Long functionId) {
        Response response = Http.get(getFcsUrl() + "/deleteFunction?functionId=" + functionId);
        return response.getContent();
    }

    public String executeFunction(Long functionId) {
        Response response = Http.get(getFcsUrl() + "/executeFunction?functionId=" + functionId);
        return response.getContent();
    }

    public String getExecuteLog(Long functionId) {
        Response response = Http.get(getFcsUrl() + "/getExecuteLog?functionId=" + functionId);
        return response.getContent();
    }

    public String executek8sFunction(Long functionId) {
        Response response = Http.get(getFcsUrl() + "/executeK8sFunction?functionId=" + functionId);
        return response.getContent();
    }

    public String getK8sExecuteLog(Long functionId) {
        Response response = Http.get(getFcsUrl() + "/getK8sExecuteLog?functionId=" + functionId);
        return response.getContent();
    }

    public String getK8sOverview(String type) {
        Response response = Http.get(getFcsUrl() + "/getK8sOverview?type=" + type);
        return response.getContent();
    }

    public String scaleDeployment(Integer targetNum, String deploymentName) {
        Response response = Http
                .get(getFcsUrl() + "/scaleDeployment?targetNum=" + targetNum + "&deploymentName=" + deploymentName);
        return response.getContent();
    }

    private String getFcsUrl() {
        return appConf.get("fc-ops.url");
    }

}
