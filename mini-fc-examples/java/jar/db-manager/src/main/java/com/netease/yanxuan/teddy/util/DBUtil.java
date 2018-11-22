/**
 * @(#)DBUtil.java, 2018年08月16日.
 */
package com.g4seek.teddy.util;

import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.impl.SimpleDataSource;

import javax.sql.DataSource;

/**
 * 数据库工具类
 *
 * @author hzlvzimin
 */
public class DBUtil {

    private static Dao dao = null;

    private DBUtil() {

    }

    private static DataSource getDataSource() {

        SimpleDataSource dataSource = new SimpleDataSource();
        try {
            dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        dataSource.setUrl(
                "jdbc:mysql://10.216.41.160:3306/minifc_test?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        return dataSource;
    }

    public static DBUtil getInstance() {
        return DBUtilHolder.INSTANCE;
    }

    /**
     * 通过数据源创建Dao
     *
     * @return Dao实例
     */
    public Dao getDao() {
        if (dao != null) {
            return dao;
        }
        DataSource dataSource = getDataSource();
        dao = new NutDao(dataSource);
        return dao;
    }

    private static class DBUtilHolder {
        private static final DBUtil INSTANCE = new DBUtil();

        private DBUtilHolder() {

        }
    }
}
