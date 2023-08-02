package com.neko233.sql.lightrail.shardingManager.createDataSource;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.neko233.sql.lightrail.common.KvTemplate;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;

/**
 * @author SolarisNeko on 2022-12-07
 **/
public class DruidDataSourceCreateStrategy implements DataSourceCreateStrategy {

    public static final DataSourceCreateStrategy instance = new DruidDataSourceCreateStrategy();

    @Override
    public String name() {
        return "druid";
    }

    @Override
    public DataSource create(Properties dsConfigProperties, Map<String, String> kvMap) throws Exception {
        for (String propName : dsConfigProperties.stringPropertyNames()) {
            String propValue = KvTemplate.builder(dsConfigProperties.getProperty(propName))
                    .put(kvMap)
                    .build();
            dsConfigProperties.setProperty(propName, propValue);
        }
        return DruidDataSourceFactory.createDataSource(dsConfigProperties);
    }
}
