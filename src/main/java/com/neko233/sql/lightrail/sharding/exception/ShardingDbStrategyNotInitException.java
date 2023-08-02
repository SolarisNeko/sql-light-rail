package com.neko233.sql.lightrail.sharding.exception;

/**
 * @author SolarisNeko
 * Date on 2023-01-07
 */
public class ShardingDbStrategyNotInitException extends Exception{

    public ShardingDbStrategyNotInitException(String name) {
        super(String.format("you must init your ShardingDbStrategy first. name = %s", name));
    }

}
