package com.neko233.sql.lightrail.sharding;

/**
 * @author SolarisNeko
 * Date on 2022-12-16
 */
public class ShardingKey {

    private static volatile ShardingId shardingId;

    public static void init(ShardingId shardingId) {
        ShardingKey.shardingId = shardingId;
    }

    public static String getShardingKeyName(String name) {
        return name;
    }

    public static String getShardingKeyName(String name, Number toShardingId) {
        if (toShardingId == null) {
            return name;
        }
        isNeedInit();
        return name + shardingId.calculateShardingId(toShardingId);
    }

    private static void isNeedInit() {
        if (shardingId == null) {
            synchronized (ShardingKey.class) {
                if (shardingId == null) {
                    shardingId = ShardingId.DEFAULT;
                }
            }
        }
    }

}
