/**
 * @(#)FunctionInfoModule.java, 2018年07月03日.
 * Copyright 2018 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.netease.yanxuan.minifc.admin.module;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.util.Regex;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.netease.yanxuan.minifc.admin.bo.AjaxResult;
import com.netease.yanxuan.minifc.admin.po.FunctionInfo;
import com.netease.yanxuan.minifc.admin.po.TriggerInfo;
import com.netease.yanxuan.minifc.admin.service.FcOpsService;

/**
 * 函数管理模块
 * 
 * @author hzlvzimin
 */
@At("/function")
@IocBean
public class FunctionInfoModule extends AbstractModule {

    @Inject
    private FcOpsService fcOpsService;

    @At("/list")
    @Ok("json")
    public AjaxResult list(String serviceName) {
        return initSuccessResult(dao.query(FunctionInfo.class, Cnd.where("serviceName", "=", serviceName)));
    }

    @At("/detail")
    @Ok("json")
    public AjaxResult detail(Long id) {
        Map<String, Object> map = new HashMap<>();
        FunctionInfo functionInfo = dao.fetch(FunctionInfo.class, id);
        if (functionInfo == null) {
            return initSuccessResult(map);
        }
        String serviceName = functionInfo.getServiceName();
        String functionName = functionInfo.getFunctionName();
        List<TriggerInfo> triggerInfoList = dao.query(TriggerInfo.class,
            Cnd.where("serviceName", "=", serviceName).and("functionName", "=", functionName));
        map.put("functionInfo", functionInfo);
        map.put("triggerInfoList", triggerInfoList);
        return initSuccessResult(map);
    }

    @At("/create")
    @Ok("json")
    public AjaxResult create(@Param("..") FunctionInfo functionInfo) {

        try {
            validateFunctionInfo(functionInfo);
        } catch (Exception e) {
            return initFailureResult("创建函数失败!" + e.getMessage());
        }
        FunctionInfo functionInfoInDB = dao.fetch(FunctionInfo.class,
            Cnd.where("serviceName", "=", functionInfo.getServiceName()).and("functionName", "=",
                functionInfo.getFunctionName()));
        if (functionInfoInDB != null) {
            return initFailureResult("创建函数失败!当前服务存在相同名称的函数");
        }
        functionInfo.setCreateTime(System.currentTimeMillis());
        functionInfo.setUpdateTime(System.currentTimeMillis());
        functionInfo = dao.insert(functionInfo);
        String result = fcOpsService.createFunction(functionInfo.getId());
        if ("ok".equals(result)) {
            return initSuccessResult("创建函数成功!");
        } else {
            dao.delete(FunctionInfo.class, functionInfo.getId());
            return initFailureResult("创建函数失败!" + result);
        }
    }

    @At("/delete")
    @Ok("json")
    public AjaxResult delete(Long id) {
        FunctionInfo functionInfo = dao.fetch(FunctionInfo.class, id);
        if (functionInfo == null) {
            return initFailureResult("函数不存在!");
        }
        String serviceName = functionInfo.getServiceName();
        String functionName = functionInfo.getFunctionName();
        List<TriggerInfo> triggerInfoList = dao.query(TriggerInfo.class,
            Cnd.where("serviceName", "=", serviceName).and("functionName", "=", functionName));
        if (triggerInfoList != null && triggerInfoList.size() > 0) {
            return initFailureResult("需要先删除关联的触发器!");
        }
        String result = fcOpsService.deleteFunction(functionInfo.getId());
        if ("ok".equals(result)) {
            dao.delete(FunctionInfo.class, id);
            return initSuccessResult("删除函数成功!");
        } else {
            return initFailureResult("删除函数失败!" + result);
        }
    }

    @At("/execute")
    @Ok("json")
    public AjaxResult execute(Long id) {
        try {
            String result = fcOpsService.executeFunction(id);
            if (!result.startsWith("http")) {
                return initFailureResult(result);
            } else {
                return initSuccessResult(result);
            }
        } catch (Exception e) {
            return initFailureResult(e.getMessage());
        }
    }

    @At("/log")
    @Ok("json")
    public AjaxResult log(Long id) {
        try {
            return initSuccessResult(fcOpsService.getExecuteLog(id));
        } catch (Exception e) {
            return initFailureResult(e.getMessage());
        }
    }

    @At("/k8sExecute")
    @Ok("json")
    public AjaxResult k8sExecute(Long id) {
        try {
            String result = fcOpsService.executek8sFunction(id);
            if (!result.startsWith("http")) {
                return initFailureResult(result);
            } else {
                return initSuccessResult(result);
            }
        } catch (Exception e) {
            return initFailureResult(e.getMessage());
        }
    }

    @At("/k8sLog")
    @Ok("json")
    public AjaxResult k8sLog(Long id) {
        try {
            return initSuccessResult(fcOpsService.getK8sExecuteLog(id));
        } catch (Exception e) {
            return initFailureResult(e.getMessage());
        }
    }

    private void validateFunctionInfo(FunctionInfo functionInfo) throws Exception {
        String functionName = functionInfo.getFunctionName();
        boolean isMatch = Regex.match("^[a-z|A-Z][a-z|A-Z|0-9|\\-]+$", functionName);
        if (!isMatch) {
            throw new Exception("函数名称需要以英文字母开头,并且只包含英文字母、数字和中划线");
        }
        String functionEntrance = functionInfo.getFunctionEntrance();
        isMatch = Regex.match("^[a-z|A-Z][\\w]+\\.[a-z|A-Z][\\w]+$", functionEntrance);
        if (!isMatch) {
            throw new Exception("函数入口格式为: 文件名.函数名\n文件名和函数名都需要以英文字母开头,并且只包含英文字母、数字和下划线");
        }
    }
}
