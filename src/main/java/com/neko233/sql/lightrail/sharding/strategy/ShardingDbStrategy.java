package com.neko233.sql.lightrail.sharding.strategy;

/**
 * @author SolarisNeko on 2022-12-07
 **/
public interface ShardingDbStrategy {

    /**
     * 计算分片 id
     *
     * @param toShardingId 用于计算分片的数据
     * @return 0 = 默认数据源 / other
     */
    default int calculate(Number toShardingId) {
        String toShardingValue = String.valueOf(toShardingId);
        return calculate(toShardingValue);
    }

    int calculate(String toShardingValue);

}
