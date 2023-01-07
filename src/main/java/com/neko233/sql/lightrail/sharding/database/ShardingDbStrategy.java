package com.neko233.sql.lightrail.sharding.database;

/**
 * @author SolarisNeko on 2022-12-07
 **/
public interface ShardingDbStrategy {

    int calculate(Number shardingId);

}
