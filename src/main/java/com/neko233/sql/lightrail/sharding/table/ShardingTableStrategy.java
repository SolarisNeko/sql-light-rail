package com.neko233.sql.lightrail.sharding.table;

/**
 * @author SolarisNeko on 2022-12-07
 **/
public interface ShardingTableStrategy {

    int calculate(String tableName, Number shardingId);

}
