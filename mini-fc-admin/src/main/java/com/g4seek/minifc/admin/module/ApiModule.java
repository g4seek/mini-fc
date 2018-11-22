/**
 * @(#)ApiModule.java, 2018年07月03日.
 */
package com.g4seek.minifc.admin.module;

import com.g4seek.minifc.admin.bo.AjaxResult;
import com.g4seek.minifc.admin.po.FunctionInfo;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;

/**
 * 提供给其它工程调用的api接口
 *
 * @author hzlvzimin
 */
@At("/api")
@IocBean
public class ApiModule extends AbstractModule {

    @At("/getFunction")
    @Ok("json")
    public AjaxResult getFunction(Long functionId) {
        return initSuccessResult(dao.fetch(FunctionInfo.class, functionId));
    }

}
