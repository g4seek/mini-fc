/**
 * @(#)ServiceInfo.java, 2018年06月28日.
 */
package com.g4seek.minifc.admin.po;

import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 服务信息
 *
 * @author hzlvzimin
 */
@Table("TB_MINIFC_SERVICE_INFO")
public class ServiceInfo {

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
     * 描述信息
     */
    private String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
