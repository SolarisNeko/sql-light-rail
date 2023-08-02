package com.neko233.sql.lightrail.shardingManager;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.neko233.sql.lightrail.RepositoryManager;
import com.neko233.sql.lightrail.SqlBuilder233;
import com.neko233.sql.lightrail.db.Db;
import com.neko233.sql.lightrail.db.DbConfig;
import com.neko233.sql.lightrail.pojo.User;
import com.neko233.sql.lightrail.sharding.strategy.ShardingDbStrategy100w;
import com.neko233.sql.lightrail.shardingManager.createDataSource.DruidDataSourceCreateStrategy;
import org.junit.Assert;
import org.junit.Test;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static com.alibaba.druid.pool.DruidDataSourceFactory.*;

/**
 * @author SolarisNeko
 * @date 2022-03-06
 */
public class MultiDataSourceTest {

    DataSource ds1 = DruidDataSourceFactory.createDataSource(getDataSourceProperties1());
    DataSource ds2 = DruidDataSourceFactory.createDataSource(getDataSourceProperties2());

    public static Properties getDsPropertiesTemplate() {
        Properties properties = new Properties();
        properties.put(PROP_URL, "jdbc:mysql://localhost:3306/${dbName}");
        properties.put(PROP_USERNAME, "root");
        properties.put(PROP_PASSWORD, "root");
        properties.put(PROP_INITIALSIZE, "5");
        properties.put(PROP_MINIDLE, "5");
        properties.put(PROP_MAXACTIVE, "10");
        properties.put(PROP_MAXWAIT, "10000");
        return properties;
    }

    public static Properties getDataSourceProperties1() {
        Properties properties = new Properties();
        properties.put(PROP_URL, "jdbc:mysql://localhost:3306/sql_light_rail");
        properties.put(PROP_USERNAME, "root");
        properties.put(PROP_PASSWORD, "root");
        properties.put(PROP_INITIALSIZE, "5");
        properties.put(PROP_MINIDLE, "5");
        properties.put(PROP_MAXACTIVE, "10");
        properties.put(PROP_MAXWAIT, "10000");
        return properties;
    }

    public static Properties getDataSourceProperties2() {
        Properties properties = new Properties();
        properties.put(PROP_URL, "jdbc:mysql://localhost:3306/sql_light_rail_1");
        properties.put(PROP_USERNAME, "root");
        properties.put(PROP_PASSWORD, "root");
        properties.put(PROP_INITIALSIZE, "5");
        properties.put(PROP_MINIDLE, "5");
        properties.put(PROP_MAXACTIVE, "10");
        properties.put(PROP_MAXWAIT, "10000");
        return properties;
    }

    public MultiDataSourceTest() throws Exception {
    }

    /**
     * How to use multi DataSource
     *
     * @throws Exception 异常
     */
    @Test
    public void multiDataSourceTest() throws Throwable {
        // 多个 dataSource
        DbGroup dbGroup = new DbGroup(DbGroupConfig.builder()
                .groupName("group1")
                .datasourceConfigTemplate(getDsPropertiesTemplate())
                .dataSourceCreateStrategy(DruidDataSourceCreateStrategy.instance)
                .shardingDbStrategy(ShardingDbStrategy100w.instance)
                .dbConfigFetcher((groupName -> {
                    List<DbConfig> dbConfigs = new ArrayList<>();
                    for (int i = 0; i < 2; i++) {
                        dbConfigs.add(DbConfig.builder()
                                .dbId(i)
                                .dbGroup(null)
                                .dbConfigMap(Collections.singletonMap("dbName", "sql_light_rail"))
                                .build());
                    }
                    return dbConfigs;
                }))
                .build());


        RepositoryManager.instance.addDbGroup(dbGroup);

        // dataSource 1
        String sql = SqlBuilder233.selectTable(User.class)
                .limitByPage(1, 1)
                .build();
        Db db1 = RepositoryManager.instance.getDbGroup("group1")
                .getDb(1L);
        Assert.assertEquals(0, (int) db1.getDbId());
        List<User> users = db1.executeQuery(sql, User.class);
        Assert.assertTrue(users.size() >= 1);

        // dataSource 2
        Db db2 = RepositoryManager.instance.getDbGroup("group1")
                .getDb(100_0000L);
        Assert.assertEquals(1, (int) db2.getDbId());
        List<User> users2 = db2.executeQuery(sql, User.class);
        Assert.assertTrue(users2.size() >= 1);
    }


}
