/**
 * @(#)TriggerInfoModule.java, 2018年07月03日.
 * Copyright 2018 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.netease.yanxuan.minifc.admin.module;

import java.util.HashMap;
import java.util.Map;

import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.netease.yanxuan.minifc.admin.bo.AjaxResult;
import com.netease.yanxuan.minifc.admin.po.TriggerInfo;

/**
 * 触发器管理模块
 * 
 * @author hzlvzimin
 */
@At("/trigger")
@IocBean
public class TriggerInfoModule extends AbstractModule {

    @At("/list")
    @Ok("json")
    public AjaxResult list(String serviceName, String functionName) {
        return initSuccessResult(dao.query(TriggerInfo.class,
            Cnd.where("serviceName", "=", serviceName).and("functionName", "=", functionName)));
    }

    @At("/detail")
    @Ok("json")
    public AjaxResult detail(Long id) {
        Map<String, Object> map = new HashMap<>();
        TriggerInfo triggerInfo = dao.fetch(TriggerInfo.class, id);
        map.put("triggerInfo", triggerInfo);
        return initSuccessResult(map);
    }

    @At("/create")
    @Ok("json")
    public AjaxResult create(@Param("..") TriggerInfo triggerInfo) {
        TriggerInfo triggerInfoInDB = dao.fetch(TriggerInfo.class,
            Cnd.where("serviceName", "=", triggerInfo.getServiceName())
                .and("functionName", "=", triggerInfo.getFunctionName())
                .and("triggerName", "=", triggerInfo.getTriggerName()));
        if (triggerInfoInDB != null) {
            return initFailureResult("创建触发器失败!当前函数存在相同名称的触发器");
        }
        triggerInfo.setCreateTime(System.currentTimeMillis());
        triggerInfo.setUpdateTime(System.currentTimeMillis());
        dao.insert(triggerInfo);
        return initSuccessResult("创建触发器成功!");
    }

    @At("/delete")
    @Ok("json")
    public AjaxResult delete(Long id) {
        dao.delete(TriggerInfo.class, id);
        return initSuccessResult("删除触发器成功!");
    }

}
