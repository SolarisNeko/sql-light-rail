package com.neko233.sql.lightrail.sharding;

/**
 * @author SolarisNeko
 * Date on 2022-12-16
 */
public interface ShardingId {

    ShardingId DEFAULT = new ShardingId100W();

    long calculateShardingId(Number shardingId);

}
