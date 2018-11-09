/**
 * @(#)FunctionInfoModule.java, 2018年07月03日.
 * Copyright 2018 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.netease.yanxuan.minifc.admin.module;

import com.netease.yanxuan.minifc.admin.bo.AjaxResult;
import com.netease.yanxuan.minifc.admin.po.FunctionInfo;
import com.netease.yanxuan.minifc.admin.po.TriggerInfo;
import com.netease.yanxuan.minifc.admin.service.FcOpsService;
import org.nutz.dao.Cnd;
import org.nutz.http.Header;
import org.nutz.http.Http;
import org.nutz.http.Response;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Files;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.util.Regex;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.upload.UploadAdaptor;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    @Inject
    private PropertiesProxy appConf;

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
        functionInfo.setFunctionVersion(1);
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

    @At("/update")
    @Ok("json")
    public AjaxResult update(@Param("..") FunctionInfo functionInfo) {
        try {
            validateFunctionInfo(functionInfo);
        } catch (Exception e) {
            return initFailureResult("修改函数失败!" + e.getMessage());
        }
        FunctionInfo functionInfoInDB = dao.fetch(FunctionInfo.class, functionInfo.getId());
        if (functionInfoInDB == null) {
            return initFailureResult("更新失败,函数不存在!");
        }
        FunctionInfo functionInfoToUpdate = new FunctionInfo();
        Lang.copyProperties(functionInfoInDB, functionInfoToUpdate);
        functionInfoToUpdate.setDescription(functionInfo.getDescription());
        functionInfoToUpdate.setFunctionEntrance(functionInfo.getFunctionEntrance());
        functionInfoToUpdate.setSourceCode(functionInfo.getSourceCode());
        functionInfoToUpdate.setUploadFilePath(functionInfo.getUploadFilePath());
        functionInfoToUpdate.setUpdateTime(System.currentTimeMillis());
        boolean isSourceUpdated = (!Strings.equals(functionInfo.getSourceCode(), functionInfoInDB.getSourceCode()))
            || (!Strings.equals(functionInfo.getUploadFilePath(), functionInfoInDB.getUploadFilePath()));
        if (!isSourceUpdated) {
            dao.update(functionInfoToUpdate);
            return initSuccessResult("更新函数成功!");
        } else {
            functionInfoToUpdate.setFunctionVersion(functionInfoInDB.getFunctionVersion() + 1);
            dao.update(functionInfoToUpdate);
        }
        String result = fcOpsService.createFunction(functionInfo.getId());
        if ("ok".equals(result)) {
            return initSuccessResult("更新函数成功!");
        } else {
            dao.update(functionInfoInDB);
            return initFailureResult("更新函数失败!" + result);
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

    @At("/proxy")
    @Ok("json")
    public AjaxResult proxy(String requestUri, String requestData, String method, String contentType) {
        Header header = Header.create();
        if (Strings.equals(contentType, "json")) {
            header = header.asJsonContentType();
        } else if (Strings.equals(contentType, "form")) {
            header = header.asFormContentType();
        }
        try {
            Response response = Http.post3(requestUri, requestData, header, 30000);
            if (response.isOK()) {
                return initSuccessResult(response.getContent());
            } else {
                return initFailureResult("函数调用异常," + response.getDetail());
            }
        } catch (Exception e) {
            return initFailureResult("函数调用异常," + e.getMessage());
        }
    }

    @At("/upload")
    @Ok("json")
    @AdaptBy(type = UploadAdaptor.class, args = { "/tmp" })
    public AjaxResult upload(@Param("sourceFile") File sourceFile) {
        String uploadDir = appConf.get("upload-dir");
        String randomId = UUID.randomUUID().toString();
        String suffix = Files.getSuffix(sourceFile);
        String destDir = uploadDir + randomId + suffix;
        File destFile = new File(destDir);
        Files.copy(sourceFile, destFile);
        return initSuccessResult(destDir);
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
        String sourceCode = functionInfo.getSourceCode();
        String uploadFilePath = functionInfo.getUploadFilePath();
        if (Strings.isEmpty(sourceCode) && Strings.isEmpty(uploadFilePath)) {
            throw new Exception("源代码和上传文件路径不能同时为空");
        }
        if (!Strings.isEmpty(sourceCode) && !Strings.isEmpty(uploadFilePath)) {
            throw new Exception("源代码和上传文件路径不能同时填写");
        }
    }
}
