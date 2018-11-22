/**
 * @(#)StoreHouseService.java, 2018年08月16日.
 */
package com.g4seek.teddy.service;

import com.g4seek.teddy.po.StoreHouse;
import com.g4seek.teddy.util.DBUtil;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import java.util.List;

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
