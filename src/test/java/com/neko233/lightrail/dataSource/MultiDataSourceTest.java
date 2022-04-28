package com.neko233.lightrail.dataSource;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.neko233.lightrail.RailPlatform;
import com.neko233.lightrail.RailPlatformFactory;
import com.neko233.lightrail.SqlLightRail;
import com.neko233.lightrail.pojo.SqlStatement;
import com.neko233.lightrail.pojo.User;
import org.junit.Test;

import javax.sql.DataSource;
import java.util.List;
import java.util.Properties;

import static com.alibaba.druid.pool.DruidDataSourceFactory.*;

/**
 * @author SolarisNeko
 * @date 2022-03-06
 */
public class MultiDataSourceTest {

    DataSource ds = DruidDataSourceFactory.createDataSource(getDefaultDbConfig());
    DataSource ds1 = DruidDataSourceFactory.createDataSource(getMultiDataSource_1());

    public static Properties getDefaultDbConfig() {
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

    public static Properties getMultiDataSource_1() {
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
     * @throws Exception 异常
     */
    @Test
    public void multiDataSourceTest() throws Exception {
        // 多个 dataSource
        RailPlatform platform = RailPlatformFactory.createLightRailPlatform(ds);
        platform.addDataSource("ds1", ds1);

        System.out.println("--------- sql_light_rail 数据源 -------------- ");
        String sql = SqlLightRail.selectTable(User.class)
                .limitByPage(1, 1)
                .build();
        List<User> users = platform.executeQuery(SqlStatement.builder()
            .sql(sql)
            .returnType(User.class)
            .build());
        users.forEach(System.out::println);

        System.out.println("--------- sql_light_rail_1 数据源 -------------- ");
        List<User> otherUsers = platform.executeQuery(SqlStatement.builder()
            .shardingKey("ds1")
            .sql(SqlLightRail.selectTable(User.class)
                .limit(0, 1)
                .build())
            .returnType(User.class)
            .build());
        otherUsers.forEach(System.out::println);
    }


}
