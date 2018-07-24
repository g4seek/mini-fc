/**
 * @(#)MainModule.java, 2018年06月28日.
 * Copyright 2018 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.netease.yanxuan.minifc.admin;

import org.nutz.mvc.annotation.IocBy;

/**
 * 配置入口类
 * 
 * @author hzlvzimin
 */
@IocBy(args = { "*js", "dao.js", "*anno", "com.netease.yanxuan.minifc.admin" })
public class MainModule {

}
