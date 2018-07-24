/**
 * @(#)AbstractModule.java, 2018年07月06日.
 * Copyright 2018 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.netease.yanxuan.minifc.admin.module;

import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.netease.yanxuan.minifc.admin.bo.AjaxResult;

/**
 * @author hzlvzimin
 */
@IocBean
public abstract class AbstractModule {

    @Inject
    protected NutDao dao;

    AjaxResult initSuccessResult(Object data) {
        return new AjaxResult("200", data);
    }

    AjaxResult initFailureResult(String errorMsg) {
        return new AjaxResult("400", errorMsg);
    }
}
