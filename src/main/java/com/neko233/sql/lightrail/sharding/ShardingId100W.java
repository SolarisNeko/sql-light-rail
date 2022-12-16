package com.neko233.sql.lightrail.sharding;

/**
 * @author SolarisNeko
 * Date on 2022-12-16
 */
public class ShardingId100W implements ShardingId {

    private static final int SHARDING_NUMBER_PART = 100_0000;

    @Override
    public long calculateShardingId(Number shardingId) {
        return (shardingId.longValue() / SHARDING_NUMBER_PART);
    }

}
