package com.neko233.sql.lightrail.dataSource;

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
 *
 * @author SolarisNeko
 * Date on 2022-02-26
 */
public class MyDataSource {

    private static DataSource dataSource;


    public static Connection getConnection() throws Exception {
        return getDefaultDataSource().getConnection();
    }

    public static DataSource getDefaultDataSource() throws Exception {
        MyDataSource.dataSource = DruidDataSourceFactory.createDataSource(getDefaultDbConfig());
        return dataSource;
    }

    /**
     * 获取数据库配置
     *
     * @return 配置信息
     */
    public static Properties getDefaultDbConfig() {
        Properties properties = new Properties();
        properties.put(PROP_URL, "jdbc:mysql://114.132.247.235:3306/xxl_job");
        properties.put(PROP_USERNAME, "root");
        properties.put(PROP_PASSWORD, "luojug00");
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
