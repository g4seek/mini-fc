/**
 * @(#)MainModule.java, 2018年06月28日.
 */
package com.g4seek.minifc.admin;

import org.nutz.mvc.annotation.IocBy;

/**
 * 配置入口类
 *
 * @author hzlvzimin
 */
@IocBy(args = {"*js", "config.js", "*anno", "com.g4seek.minifc.admin"})
public class MainModule {

}
