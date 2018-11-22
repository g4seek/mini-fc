/**
 * @(#)ConsoleModule.java, 2018年06月28日.
 */
package com.g4seek.minifc.admin.module;

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
