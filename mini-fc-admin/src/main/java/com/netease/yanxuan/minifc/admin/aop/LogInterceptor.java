/**
 * @(#)LogInterceptor.java, 2018年08月23日.
 * Copyright 2018 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.netease.yanxuan.minifc.admin.aop;

import java.util.UUID;

import org.nutz.aop.InterceptorChain;
import org.nutz.aop.MethodInterceptor;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.log.Log;
import org.nutz.log.Logs;

/**
 * 日志拦截器
 * 
 * @author hzlvzimin
 */
@IocBean
public class LogInterceptor implements MethodInterceptor {

    private Log logger = Logs.get();

    private ThreadLocal<String> traceIdLocal = new ThreadLocal<>();

    @Override
    public void filter(InterceptorChain chain) throws Throwable {

        String traceId = this.getTraceId();
        String clazz = chain.getCallingObj().getClass().getSimpleName();
        String method = chain.getCallingMethod().getName();
        String args = Json.toJson(chain.getArgs(), JsonFormat.compact());
        logger.infof("[%s.%s] REQUEST traceId=%s, args=%s", clazz, method, traceId, args);
        try {
            chain.doChain();
        } catch (Throwable t) {
            logger.errorf("[%s.%s] ERROR traceId=%s, args=%s, exception=%s", clazz, method, traceId, args,
                t.getMessage());
            logger.error(t);
            throw t;
        }
        String result = Json.toJson(chain.getReturn(), JsonFormat.compact());
        logger.infof("[%s.%s] RESPONSE traceId=%s, return=%s", clazz, method, traceId, result);

    }

    private String getTraceId() {
        if (traceIdLocal.get() != null) {
            return traceIdLocal.get();
        } else {
            String traceId = UUID.randomUUID().toString();
            traceIdLocal.set(traceId);
            return traceId;
        }
    }
}
