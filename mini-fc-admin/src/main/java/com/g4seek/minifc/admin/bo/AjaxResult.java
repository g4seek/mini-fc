/**
 * @(#)AjaxResult.java, 2018年07月06日.
 */
package com.g4seek.minifc.admin.bo;

import java.util.LinkedHashMap;

/**
 * ajax接口的响应对象
 *
 * @author hzlvzimin
 */
public class AjaxResult extends LinkedHashMap<String, Object> {

    private final String CODE = "code";

    private final String DATA = "data";

    private final String ERROR_MSG = "errorMsg";

    public AjaxResult(String code, Object data) {
        setCode(code);
        setData(data);
    }

    public AjaxResult(String code, String errorMsg) {
        setCode(code);
        setErrorMsg(errorMsg);
    }

    public String getCode() {
        return (String) get(CODE);
    }

    public void setCode(String code) {
        put(CODE, code);
    }

    public Object getData() {
        return get(DATA);
    }

    public void setData(Object data) {
        put(DATA, data);
    }

    public String getErrorMsg() {
        return (String) get(ERROR_MSG);
    }

    public void setErrorMsg(String errorMsg) {
        put(ERROR_MSG, errorMsg);
    }

}
