/**
 * @(#)OperateType.java, 2018年08月16日.
 * Copyright 2018 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.netease.yanxuan.teddy.enums;

import org.nutz.lang.Strings;

/**
 * @author hzlvzimin
 */
public enum OperateType {

    INSERT, UPDATE, DELETE, SELECT;

    public static OperateType getByName(String name) {
        for (OperateType operateType: OperateType.values()) {
            if (Strings.equals(name, operateType.name())) {
                return operateType;
            }
        }
        return null;
    }
}
