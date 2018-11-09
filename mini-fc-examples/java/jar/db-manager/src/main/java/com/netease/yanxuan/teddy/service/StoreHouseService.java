/**
 * @(#)StoreHouseService.java, 2018年08月16日.
 * Copyright 2018 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.netease.yanxuan.teddy.service;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import com.netease.yanxuan.teddy.po.StoreHouse;
import com.netease.yanxuan.teddy.util.DBUtil;

/**
 * @author hzlvzimin
 */
public class StoreHouseService {

    private final static StoreHouseService instance = new StoreHouseService();

    private StoreHouseService() {

    }

    public static StoreHouseService getInstance() {
        return instance;
    }

    public StoreHouse insert(StoreHouse storeHouse) {
        return dao().insert(storeHouse);
    }

    public int update(StoreHouse storeHouse) {
        return dao().update(storeHouse, Cnd.where("id", "=", storeHouse.getId()));
    }

    public List<StoreHouse> selectAll() {
        return dao().query(StoreHouse.class, null);
    }

    public int delete(Long id) {
        return dao().delete(StoreHouse.class, id);
    }

    public StoreHouse getById(Long id) {
        return dao().fetch(StoreHouse.class, id);
    }

    private Dao dao() {
        return DBUtil.getInstance().getDao();
    }
}
