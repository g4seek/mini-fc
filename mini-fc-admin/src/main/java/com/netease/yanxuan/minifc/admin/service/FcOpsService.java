/**
 * @(#)FcOpsService.java, 2018年07月07日.
 * Copyright 2018 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.netease.yanxuan.minifc.admin.service;

import org.nutz.http.Http;
import org.nutz.http.Response;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * 函数计算运维平台服务
 * 
 * @author hzlvzimin
 */
@IocBean
public class FcOpsService {

    private final String FC_OPS_URL = "http://10.240.131.51:13000";

    public String createFunction(Long functionId) {
        Response response = Http.get(FC_OPS_URL + "/createFunction?functionId=" + functionId);
        return response.getContent();
    }

    public String deleteFunction(Long functionId) {
        Response response = Http.get(FC_OPS_URL + "/deleteFunction?functionId=" + functionId);
        return response.getContent();
    }

    public String executeFunction(Long functionId) {
        Response response = Http.get(FC_OPS_URL + "/executeFunction?functionId=" + functionId);
        return response.getContent();
    }

    public String getExecuteLog(Long functionId) {
        Response response = Http.get(FC_OPS_URL + "/getExecuteLog?functionId=" + functionId);
        return response.getContent();
    }

    public String executek8sFunction(Long functionId) {
        Response response = Http.get(FC_OPS_URL + "/executeK8sFunction?functionId=" + functionId);
        return response.getContent();
    }

    public String getK8sExecuteLog(Long functionId) {
        Response response = Http.get(FC_OPS_URL + "/getK8sExecuteLog?functionId=" + functionId);
        return response.getContent();
    }

}
