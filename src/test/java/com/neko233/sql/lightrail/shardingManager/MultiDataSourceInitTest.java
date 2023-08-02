package com.neko233.sql.lightrail.shardingManager;

import com.neko233.sql.lightrail.RepositoryManager;
import com.neko233.sql.lightrail.db.Db;
import com.neko233.sql.lightrail.shardingManager.initializer.RepositoryManagerInitializerByMysql;
import org.junit.Test;

import javax.sql.DataSource;
import java.util.Properties;

import static com.alibaba.druid.pool.DruidDataSourceFactory.*;

/**
 * @author SolarisNeko
 * @date 2022-03-06
 */
public class MultiDataSourceInitTest {

    public static DataSource configDbDataSource() throws Exception {
        Properties properties = new Properties();
        properties.put(PROP_URL, "jdbc:mysql://localhost:3306/sql_light_rail_config");
        properties.put(PROP_USERNAME, "root");
        properties.put(PROP_PASSWORD, "root");
        properties.put(PROP_INITIALSIZE, "5");
        properties.put(PROP_MINIDLE, "5");
        properties.put(PROP_MAXACTIVE, "10");
        properties.put(PROP_MAXWAIT, "10000");
        return createDataSource(properties);
    }

    /**
     * How to use multi DataSource
     *
     * @throws Exception 异常
     */
    @Test
    public void testInit() throws Exception {

        // auto init config | see 'DDL-sharding-sql_light_rail_config.sql'
        Db configDb = new Db(configDbDataSource());
        new RepositoryManagerInitializerByMysql().initDbGroup(configDb, "template");

        // use
        Db db = RepositoryManager.instance
                .getDbGroup("template")
                .getDb(0L);

        System.out.println(db.getDbName());

        // check
        Integer number1 = db.executeQuerySingle("select 1 from dual", Integer.class);
        System.out.println(number1);
    }


}
