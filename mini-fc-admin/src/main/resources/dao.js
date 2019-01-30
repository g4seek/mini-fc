var ioc = {
    conf: {
        type: "org.nutz.ioc.impl.PropertiesProxy",
        fields: {
            paths: ["db.properties"]
        }
    },
    dataSource: {
        type: "com.alibaba.druid.pool.DruidDataSource",
        events: {
            create: "init",
            depose: 'close'
        },
        fields: {
            url: {java: "$conf.get('db.url')"},
            username: {java: "$conf.get('db.username')"},
            password: {java: "$conf.get('db.password')"},
            testWhileIdle: true,
            validationQuery: {java: "$conf.get('db.validationQuery')"},
            maxActive: {java: "$conf.get('db.maxActive')"}
        }
    },
    dao: {
        type: "org.nutz.dao.impl.NutDao",
        args: [{refer: "dataSource"}]
    }
};