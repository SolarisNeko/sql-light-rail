package com.neko233.sql.lightrail.sharding.strategy;

/**
 * @author SolarisNeko on 2022-12-07
 **/
public class ShardingDbStrategyDefault implements ShardingDbStrategy {

    public static final ShardingDbStrategy instance = new ShardingDbStrategyDefault();


    /**
     * default = 0
     */
    @Override
    public int calculate(String toShardingValue) {
        return 0;
    }

}
