/**
 * @(#)AbstractModule.java, 2018年07月06日.
 */
package com.g4seek.minifc.admin.module;

import com.g4seek.minifc.admin.bo.AjaxResult;
import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * @author hzlvzimin
 */
@IocBean
public abstract class AbstractModule {

    @Inject
    protected NutDao dao;

    AjaxResult initSuccessResult(Object data) {
        return new AjaxResult("200", data);
    }

    AjaxResult initFailureResult(String errorMsg) {
        return new AjaxResult("400", errorMsg);
    }
}
