/**
 * @(#)MyTestRunner.java, 2018年08月03日.
 * Copyright 2018 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.netease.yanxuan.minifc.admin;

import org.junit.runners.model.InitializationError;
import org.nutz.mock.NutTestRunner;

/**
 * @author hzlvzimin
 */
public class MyTestRunner extends NutTestRunner {

    public MyTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    public Class<?> getMainModule() {
        return MainModule.class;
    }
}
