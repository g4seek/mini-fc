/**
 * @(#)IndexHandler.java, 2018年09月11日.
 */
package com.g4seek.minifc.example.idincr.handler;

import com.g4seek.minifc.example.idincr.utils.JedisUtil;
import com.google.gson.JsonObject;

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
