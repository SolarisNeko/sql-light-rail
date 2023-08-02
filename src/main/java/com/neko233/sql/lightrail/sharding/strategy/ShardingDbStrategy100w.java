package com.neko233.sql.lightrail.sharding.strategy;

import com.neko233.skilltree.commons.core.base.StringUtils233;

/**
 * @author SolarisNeko on 2022-12-07
 **/
public class ShardingDbStrategy100w implements ShardingDbStrategy {

    public static final ShardingDbStrategy instance = new ShardingDbStrategy100w();


    /**
     * default = 0
     */
    @Override
    public int calculate(String toShardingValue) {
        if (StringUtils233.isNotNumber(toShardingValue)) {
            return 0;
        }
        long toShardingNumber = Long.parseLong(toShardingValue.trim());
        if (toShardingNumber < 0) {
            return 0;
        }
        return (int) (toShardingNumber / 100_0000);
    }

}
