package com.neko233.sql.lightrail.sharding.strategy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author SolarisNeko on 2022-12-07
 **/
public interface ShardingDbStrategyFactory {

    /**
     * recommend register by your groupName : strategy
     */
    Map<String, ShardingDbStrategy> registry = new ConcurrentHashMap<String, ShardingDbStrategy>() {{
        put("default", ShardingDbStrategyDefault.instance);
        put("100w", ShardingDbStrategy100w.instance);
    }};

    /**
     * 注册分片算法 if 不存在
     *
     * @param groupName          数据库组名
     * @param shardingDbStrategy 分片策略
     */
    static void registerIfNotExists(String groupName,
                                    ShardingDbStrategy shardingDbStrategy) {
        registry.merge(groupName, shardingDbStrategy, (t1, t2) -> {
            String msg = String.format("%s register have merge! groupName = %s, shardingDbStrategy = %s",
                    ShardingDbStrategyFactory.class.getSimpleName(), groupName,
                    shardingDbStrategy.getClass().getSimpleName());
            throw new IllegalArgumentException(msg);
        });
    }

    /**
     * 注册分片算法
     *
     * @param groupName          数据库组名
     * @param shardingDbStrategy 分片策略
     */
    static void register(String groupName,
                         ShardingDbStrategy shardingDbStrategy) {
        registry.put(groupName, shardingDbStrategy);
    }

    /**
     * get 分片策略 by 策略名
     *
     */
    static ShardingDbStrategy get(String strategyName) {
        return registry.get(String.valueOf(strategyName).toLowerCase());
    }


    static ShardingDbStrategy get(String strategyName,
                                  ShardingDbStrategy shardingDbStrategy) {
        return registry.getOrDefault(String.valueOf(strategyName).toLowerCase(), shardingDbStrategy);
    }

}
