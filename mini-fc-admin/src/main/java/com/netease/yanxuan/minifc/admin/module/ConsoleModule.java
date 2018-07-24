/**
 * @(#)ConsoleModule.java, 2018年06月28日.
 * Copyright 2018 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.netease.yanxuan.minifc.admin.module;

import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;

/**
 * 控制台模块
 * 
 * @author hzlvzimin
 */
@At("/console")
@IocBean
public class ConsoleModule {

    @At("/")
    @Ok("->:/html/console.html")
    public void to() {

    }
}
