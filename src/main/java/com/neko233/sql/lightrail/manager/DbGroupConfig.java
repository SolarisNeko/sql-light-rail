package com.neko233.sql.lightrail.manager;

import com.neko233.sql.lightrail.db.DbConfig;
import com.neko233.sql.lightrail.db.DbConfigFetcher;
import com.neko233.sql.lightrail.sharding.database.ShardingDbStrategy;
import com.neko233.sql.lightrail.strategy.createDataSource.DataSourceCreateStrategy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.SQLException;
import java.util.*;

/**
 * @author SolarisNeko on 2022-12-07
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DbGroupConfig {

    private String groupName;
    private Properties datasourceConfigTemplate;
    private DataSourceCreateStrategy dataSourceCreateStrategy;
    private ShardingDbStrategy shardingDbStrategy;
    private DbConfigFetcher dbConfigFetcher;

    public Collection<DbConfig> getDbConfigs() throws SQLException {
        List<DbConfig> dbConfigs = new ArrayList<>();
        if (dbConfigFetcher == null) {
            return dbConfigs;
        }
        Collection<DbConfig> fetchConfigs = Optional.ofNullable(dbConfigFetcher.fetch(groupName)).orElse(new ArrayList<>());
        dbConfigs.addAll(fetchConfigs);
        return dbConfigs;
    }
}
