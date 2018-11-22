/**
 * @(#)K8sInfoModule.java, 2018年07月25日.
 */
package com.g4seek.minifc.admin.module;

import com.g4seek.minifc.admin.bo.AjaxResult;
import com.g4seek.minifc.admin.bo.K8sDeploymentInfo;
import com.g4seek.minifc.admin.bo.K8sPodInfo;
import com.g4seek.minifc.admin.bo.K8sServiceInfo;
import com.g4seek.minifc.admin.service.FcOpsService;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hzlvzimin
 */
@At("/k8s")
@IocBean
public class K8sInfoModule extends AbstractModule {

    @Inject
    private FcOpsService fcOpsService;

    @At("/overview")
    @Ok("json")
    public AjaxResult overview() {
        String podJson = fcOpsService.getK8sOverview("pod");
        List<K8sPodInfo> podList = Json.fromJsonAsList(K8sPodInfo.class, podJson);

        String deploymentJson = fcOpsService.getK8sOverview("deployment");
        List<K8sDeploymentInfo> deploymentList = Json.fromJsonAsList(K8sDeploymentInfo.class, deploymentJson);

        String serviceJson = fcOpsService.getK8sOverview("service");
        List<K8sServiceInfo> serviceList = Json.fromJsonAsList(K8sServiceInfo.class, serviceJson);

        Map<String, Object> map = new HashMap<>();
        map.put("podList", podList);
        map.put("deploymentList", deploymentList);
        map.put("serviceList", serviceList);
        return initSuccessResult(map);
    }

    @At("/scale")
    @Ok("json")
    public AjaxResult scale(Integer targetNum, String deploymentName) {
        try {
            return initSuccessResult(fcOpsService.scaleDeployment(targetNum, deploymentName));
        } catch (Exception e) {
            return initFailureResult(e.getMessage());
        }
    }
}
