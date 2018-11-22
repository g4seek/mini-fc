/**
 * @(#)HttpTriggerConfigBean.java, 2018年06月29日.
 */
package com.g4seek.minifc.admin.bo;

import java.util.List;

/**
 * HTTP触发器配置对象
 *
 * @author hzlvzimin
 */
public class HttpTriggerConfigBean {

    private List<String> requestMethods;

    public List<String> getRequestMethods() {
        return requestMethods;
    }

    public void setRequestMethods(List<String> requestMethods) {
        this.requestMethods = requestMethods;
    }
}
