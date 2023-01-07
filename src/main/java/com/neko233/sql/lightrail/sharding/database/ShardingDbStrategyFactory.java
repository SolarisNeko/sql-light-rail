package com.neko233.sql.lightrail.sharding.database;

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


    static void registerIfNotExists(String groupName, ShardingDbStrategy shardingDbStrategy) {
        registry.merge(groupName, shardingDbStrategy, (t1, t2) -> {
            String msg = String.format("%s register have merge! groupName = %s, shardingDbStrategy = %s",
                    ShardingDbStrategyFactory.class.getSimpleName(), groupName,
                    shardingDbStrategy.getClass().getSimpleName());
            throw new IllegalArgumentException(msg);
        });
    }

    static void register(String groupName, ShardingDbStrategy shardingDbStrategy) {
        registry.put(groupName, shardingDbStrategy);
    }

    static ShardingDbStrategy get(String name) {
        return registry.get(String.valueOf(name).toLowerCase());
    }


    static ShardingDbStrategy get(String name, ShardingDbStrategy shardingDbStrategy) {
        return registry.getOrDefault(String.valueOf(name).toLowerCase(), shardingDbStrategy);
    }

}
