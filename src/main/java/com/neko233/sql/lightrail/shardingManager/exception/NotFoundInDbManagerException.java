package com.neko233.sql.lightrail.shardingManager.exception;

/**
 * @author SolarisNeko
 * Date on 2023-01-07
 */
public class NotFoundInDbManagerException extends RuntimeException {

    public NotFoundInDbManagerException(String groupName, Integer dbId) {
        super(String.format("not found db in group. please check your config. dbGroup = %s, dbId = %s", groupName, dbId));
    }

}
