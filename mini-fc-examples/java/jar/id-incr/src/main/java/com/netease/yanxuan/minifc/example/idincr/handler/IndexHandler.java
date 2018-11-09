/**
 * @(#)IndexHandler.java, 2018年09月11日.
 * Copyright 2018 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.netease.yanxuan.minifc.example.idincr.handler;

import com.google.gson.JsonObject;
import com.netease.yanxuan.minifc.example.idincr.utils.JedisUtil;

/**
 * @author hzlvzimin
 */
public class IndexHandler {

    private final static IndexHandler INSTANCE = new IndexHandler();

    private IndexHandler() {

    }

    public static IndexHandler getInstance() {
        return INSTANCE;
    }

    public JsonObject handle(JsonObject input) {
        JsonObject output = new JsonObject();
        if (!input.has("key")) {
            output.addProperty("error", "需要有key节点");
            return output;
        }
        String key = input.get("key").getAsString();

        Long value = JedisUtil.getInstance().incrAndGet(key);
        output.addProperty("key", key);
        output.addProperty("value", value);
        return output;
    }
}
