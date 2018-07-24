/**
 * @(#)ApiModule.java, 2018年07月03日.
 * Copyright 2018 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.netease.yanxuan.minifc.admin.module;

import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;

import com.netease.yanxuan.minifc.admin.bo.AjaxResult;
import com.netease.yanxuan.minifc.admin.po.FunctionInfo;

/**
 * 提供给其它工程调用的api接口
 * 
 * @author hzlvzimin
 */
@At("/api")
@IocBean
public class ApiModule extends AbstractModule {

    @At("/getFunction")
    @Ok("json")
    public AjaxResult getFunction(Long functionId) {
        return initSuccessResult(dao.fetch(FunctionInfo.class, functionId));
    }

}
