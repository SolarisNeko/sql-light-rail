package com.neko233.sql.lightrail.manager.initializer;

import com.neko233.sql.lightrail.db.Db;

import java.util.Arrays;
import java.util.List;

/**
 * @author SolarisNeko
 * Date on 2023-01-07
 */
public interface RepositoryManagerInitializer {

    default void initDbGroup(Db configDb, String... groupNames) throws Exception {
        initDbGroup(configDb, Arrays.asList(groupNames));
    }

    void initDbGroup(Db resourceDb, List<String> groupNames) throws Exception;


}
