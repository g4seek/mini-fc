/**
 * @(#)TriggerInfo.java, 2018年06月28日.
 */
package com.g4seek.minifc.admin.po;

import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 触发器信息
 *
 * @author hzlvzimin
 */
@Table("TB_MINIFC_TRIGGER_INFO")
public class TriggerInfo {

    /**
     * 主键
     */
    @Id
    private Long id;

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 函数名称
     */
    private String functionName;

    /**
     * 触发器类型
     */
    private String triggerType;

    /**
     * 触发器名称
     */
    private String triggerName;

    /**
     * 触发器配置(json格式)
     */
    private String triggerConfig;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getTriggerConfig() {
        return triggerConfig;
    }

    public void setTriggerConfig(String triggerConfig) {
        this.triggerConfig = triggerConfig;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}
