package com.neko233.sql.lightrail.shardingManager.createDataSource;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;

/**
 * @author SolarisNeko on 2022-12-07
 **/
public interface DataSourceCreateStrategy {


    /**
     * @return 策略名
     */
    String name();


    /**
     * 创建 DataSource
     *
     * @param dsConfigProperties 数据源配置
     * @param kvMap              配置
     * @return DataSource
     */
    DataSource create(Properties dsConfigProperties,
                      Map<String, String> kvMap) throws Exception;


}
