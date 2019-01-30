/**
 * @(#)StoreHouse.java, 2018年08月16日.
 */
package com.g4seek.minifc.example.po;

import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 仓库对象
 *
 * @author hzlvzimin
 */
@Table("STOREHOUSE")
public class StoreHouse {

    @Id
    private Long id;

    private String name;

    private String code;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
