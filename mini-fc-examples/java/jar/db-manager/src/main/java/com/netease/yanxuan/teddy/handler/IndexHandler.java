/**
 * @(#)IndexHandler.java, 2018年08月16日.
 */
package com.g4seek.teddy.handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.g4seek.teddy.enums.OperateType;
import com.g4seek.teddy.po.StoreHouse;
import com.g4seek.teddy.service.StoreHouseService;

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
        if (!input.has("type") || !input.has("data")) {
            output.addProperty("error", "需要有type和data节点");
            return output;
        }
        String type = input.get("type").getAsString();
        OperateType operateType = OperateType.getByName(type);
        if (operateType == null) {
            output.addProperty("error", "type值不合法");
            return output;
        }
        StoreHouseService service = StoreHouseService.getInstance();
        Gson gson = new Gson();
        StoreHouse storeHouse = gson.fromJson(input.get("data"), StoreHouse.class);
        String result = "";
        switch (operateType) {
            case INSERT:
                result = gson.toJson(service.insert(storeHouse));
                break;
            case UPDATE:
                result = gson.toJson(service.update(storeHouse));
                break;
            case DELETE:
                result = gson.toJson(service.delete(storeHouse.getId()));
                break;
            case SELECT:
                if (storeHouse.getId() == null) {
                    result = gson.toJson(service.selectAll());
                } else {
                    result = gson.toJson(service.getById(storeHouse.getId()));
                }
                break;
        }
        output.addProperty("result", result);
        return output;
    }

}
