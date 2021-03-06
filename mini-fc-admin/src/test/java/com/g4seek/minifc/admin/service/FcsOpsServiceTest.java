/**
 * @(#)FcsOpsServiceTest.java, 2018年08月03日.
 */
package com.g4seek.minifc.admin.service;

import com.g4seek.minifc.admin.MyTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * @author hzlvzimin
 */
@RunWith(MyTestRunner.class)
@IocBean
public class FcsOpsServiceTest {

    @Inject
    private FcOpsService fcOpsService;

    @Test
    public void testCreateFunction() {
        fcOpsService.createFunction(null);
    }

    @Test
    public void testDeleteFunction() {
        fcOpsService.deleteFunction(null);
    }

    @Test
    public void testExecuteFunction() {
        fcOpsService.executeFunction(null);
    }

    @Test
    public void testGetExecuteLog() {
        fcOpsService.getExecuteLog(null);
    }

    @Test
    public void testExecutek8sFunction() {
        fcOpsService.executek8sFunction(null);
    }

    @Test
    public void testGetK8sExecuteLog() {
        fcOpsService.getK8sExecuteLog(null);
    }

    @Test
    public void testGetK8sOverview() {
        fcOpsService.getK8sOverview(null);
    }

    @Test
    public void testScaleDeployment() {
        fcOpsService.scaleDeployment(null, null);
    }

}
