/**
 * @(#)ServiceInfoModule.java, 2018年06月30日.
 * Copyright 2018 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.netease.yanxuan.minifc.admin.module;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.util.Regex;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.netease.yanxuan.minifc.admin.bo.AjaxResult;
import com.netease.yanxuan.minifc.admin.po.FunctionInfo;
import com.netease.yanxuan.minifc.admin.po.ServiceInfo;

/**
 * 服务管理模块
 *
 * @author hzlvzimin
 */
@At("/service")
@IocBean
public class ServiceInfoModule extends AbstractModule {

    @At("/list")
    @Ok("json")
    public AjaxResult list() {
        return initSuccessResult(dao.query(ServiceInfo.class, Cnd.cri()));
    }

    @At("/detail")
    @Ok("json")
    public AjaxResult detail(Long id) {
        Map<String, Object> map = new HashMap<>();
        ServiceInfo serviceInfo = dao.fetch(ServiceInfo.class, id);
        if (serviceInfo == null) {
            return initSuccessResult(map);
        }
        String serviceName = serviceInfo.getServiceName();
        List<FunctionInfo> functionInfoList = dao.query(FunctionInfo.class, Cnd.where("serviceName", "=", serviceName));
        map.put("serviceInfo", serviceInfo);
        map.put("functionInfoList", functionInfoList);
        return initSuccessResult(map);
    }

    @At("/create")
    @Ok("json")
    public AjaxResult create(@Param("..") ServiceInfo serviceInfo) {
        try {
            validateServiceInfo(serviceInfo);
        } catch (Exception e) {
            return initFailureResult("创建服务失败!" + e.getMessage());
        }
        ServiceInfo serviceInfoInDB = dao.fetch(ServiceInfo.class,
            Cnd.where("serviceName", "=", serviceInfo.getServiceName()));
        if (serviceInfoInDB != null) {
            return initFailureResult("创建服务失败!存在相同名称的服务");
        }
        serviceInfo.setCreateTime(System.currentTimeMillis());
        serviceInfo.setUpdateTime(System.currentTimeMillis());
        dao.insert(serviceInfo);
        return initSuccessResult("创建服务成功!");
    }

    @At("/update")
    @Ok("json")
    public AjaxResult update(@Param("..") ServiceInfo serviceInfo) {
        try {
            validateServiceInfo(serviceInfo);
        } catch (Exception e) {
            return initFailureResult("修改函数失败!" + e.getMessage());
        }
        Long id = serviceInfo.getId();
        ServiceInfo serviceInfoInDB = dao.fetch(ServiceInfo.class, id);
        if (serviceInfoInDB == null) {
            return initFailureResult("更新失败,服务不存在!");
        }
        serviceInfoInDB.setDescription(serviceInfo.getDescription());
        serviceInfoInDB.setUpdateTime(System.currentTimeMillis());
        dao.update(serviceInfoInDB);
        return initSuccessResult("更新服务成功!");
    }

    @At("/delete")
    @Ok("json")
    public AjaxResult delete(Long id) {

        ServiceInfo serviceInfo = dao.fetch(ServiceInfo.class, id);
        if (serviceInfo == null) {
            return initFailureResult("服务不存在!");
        }
        String serviceName = serviceInfo.getServiceName();
        List<FunctionInfo> functionInfoList = dao.query(FunctionInfo.class, Cnd.where("serviceName", "=", serviceName));
        if (functionInfoList != null && functionInfoList.size() > 0) {
            return initFailureResult("需要先删除关联的函数!");
        }
        dao.delete(ServiceInfo.class, id);
        return initSuccessResult("删除服务成功!");
    }

    private void validateServiceInfo(ServiceInfo serviceInfo) throws Exception {
        String serviceName = serviceInfo.getServiceName();
        boolean isMatch = Regex.match("^[a-z|A-Z][a-z|A-Z|0-9|\\-]+$", serviceName);
        if (!isMatch) {
            throw new Exception("服务名称需要以英文字母开头,并且只包含英文字母、数字和中划线");
        }
    }
}
