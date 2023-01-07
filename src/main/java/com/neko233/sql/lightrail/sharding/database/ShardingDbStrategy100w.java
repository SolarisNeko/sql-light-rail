package com.neko233.sql.lightrail.sharding.database;

/**
 * @author SolarisNeko on 2022-12-07
 **/
public class ShardingDbStrategy100w implements ShardingDbStrategy {

    public static final ShardingDbStrategy instance = new ShardingDbStrategy100w();


    /**
     * default = 0
     */
    @Override
    public int calculate(Number shardingId) {
        if (shardingId == null) {
            return 0;
        }
        long l = shardingId.longValue();
        if (l < 0) {
            return 0;
        }
        return (int) (l / 100_0000);
    }

}
