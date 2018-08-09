/**
 * @(#)K8sInfoModule.java, 2018年07月25日.
 * Copyright 2018 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.netease.yanxuan.minifc.admin.module;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;

import com.netease.yanxuan.minifc.admin.bo.AjaxResult;
import com.netease.yanxuan.minifc.admin.bo.K8sDeploymentInfo;
import com.netease.yanxuan.minifc.admin.bo.K8sPodInfo;
import com.netease.yanxuan.minifc.admin.bo.K8sServiceInfo;
import com.netease.yanxuan.minifc.admin.service.FcOpsService;

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
