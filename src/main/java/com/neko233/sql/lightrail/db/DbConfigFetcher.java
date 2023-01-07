package com.neko233.sql.lightrail.db;

import java.sql.SQLException;
import java.util.Collection;

/**
 * @author SolarisNeko
 * Date on 2023-01-07
 */
@FunctionalInterface
public interface DbConfigFetcher {

    Collection<DbConfig> fetch(String groupName) throws SQLException;

}
