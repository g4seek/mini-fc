var ioc = {
    appConf: {
        type: "org.nutz.ioc.impl.PropertiesProxy",
        fields: {
            paths: ["app.properties"]
        }
    },
    dbConf: {
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
            url: {java: "$dbConf.get('db.url')"},
            username: {java: "$dbConf.get('db.username')"},
            password: {java: "$dbConf.get('db.password')"},
            testWhileIdle: true,
            validationQuery: {java: "$dbConf.get('db.validationQuery')"},
            maxActive: {java: "$dbConf.get('db.maxActive')"}
        }
    },
    dao: {
        type: "org.nutz.dao.impl.NutDao",
        args: [{refer: "dataSource"}]
    },
    logInterceptor: {
        type: 'com.g4seek.minifc.admin.aop.LogInterceptor'
    },

    $aop: {
        type: 'org.nutz.ioc.aop.config.impl.JsonAopConfigration',
        fields: {
            itemList: [
                ['com\.g4seek\.minifc\.admin\.module\..+', '.+', 'ioc:logInterceptor', 'false']
            ]
        }
    }
};