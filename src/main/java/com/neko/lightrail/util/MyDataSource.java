package com.neko.lightrail.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import static com.alibaba.druid.pool.DruidDataSourceFactory.PROP_INITIALSIZE;
import static com.alibaba.druid.pool.DruidDataSourceFactory.PROP_MAXACTIVE;
import static com.alibaba.druid.pool.DruidDataSourceFactory.PROP_MAXWAIT;
import static com.alibaba.druid.pool.DruidDataSourceFactory.PROP_MINIDLE;
import static com.alibaba.druid.pool.DruidDataSourceFactory.PROP_PASSWORD;
import static com.alibaba.druid.pool.DruidDataSourceFactory.PROP_URL;
import static com.alibaba.druid.pool.DruidDataSourceFactory.PROP_USERNAME;

/**
 * 演示用
 * @author SolarisNeko
 * @date 2022-02-26
 */
public class MyDataSource {

    private static DataSource dataSource;


    public static Connection getConnection() throws Exception {
        return getDataSource().getConnection();
    }

    public static DataSource getDataSource() throws Exception {
        MyDataSource.dataSource = DruidDataSourceFactory.createDataSource(getDbConfig());
        return dataSource;
    }

    /**
     * 获取数据库配置
     *
     * @return 配置信息
     */
    public static Properties getDbConfig() {
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

    public static ResultSet executeSelect(String sql) throws Exception {
        PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
        return preparedStatement.executeQuery();
    }

}
