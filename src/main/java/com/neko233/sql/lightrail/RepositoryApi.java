package com.neko233.sql.lightrail;

import com.neko233.sql.lightrail.domain.SqlStatement;
import com.neko233.sql.lightrail.plugin.Plugin;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

/**
 * @author SolarisNeko
 * Date on 2022-12-16
 */
public interface RepositoryApi {


    // original dataSource
    RepositoryManager addDataSource(String shardingKey, DataSource newDataSource);

    RepositoryManager removeDataSource(String shardingKey);

    DataSource getDataSource(String shardingKey);


    // plugin
    RepositoryManager addGlobalPlugin(Plugin plugin);

    RepositoryManager removeGlobalPlugin(Plugin plugin);

    RepositoryManager removeAllPlugins();

    // query
    <T> List<T> executeQuery(String sql, Class<T> returnType) throws SQLException;

    <T> List<T> executeQuery(String shardingKey, String sql, Class<T> returnType) throws SQLException;

    <T> List<T> executeQuery(SqlStatement statement) throws SQLException;


    // update part
    Integer executeUpdate(String sql) throws SQLException;

    Integer executeUpdate(List<String> sqlList) throws SQLException;

    Integer executeUpdate(String shardingKey, String sql) throws SQLException;

    Integer executeUpdate(String shardingKey, List<String> sqlList) throws SQLException;

    Integer executeUpdate(SqlStatement statement) throws SQLException;

}
