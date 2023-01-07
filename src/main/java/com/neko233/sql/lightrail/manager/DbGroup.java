package com.neko233.sql.lightrail.manager;

import com.neko233.sql.lightrail.common.LifeCycle;
import com.neko233.sql.lightrail.db.Db;
import com.neko233.sql.lightrail.db.DbConfig;
import com.neko233.sql.lightrail.manager.exception.NotFoundInDbManagerException;
import com.neko233.sql.lightrail.sharding.database.ShardingDbStrategyDefault;
import com.neko233.sql.lightrail.sharding.database.ShardingDbStrategy;
import com.neko233.sql.lightrail.strategy.createDataSource.DataSourceCreateStrategy;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * init Db by same config
 *
 * @author SolarisNeko on 2022-12-07
 **/
@Slf4j
@ToString
public class DbGroup implements LifeCycle {

    private volatile boolean isInit;
    public final String name;
    private final DataSourceCreateStrategy createStrategy;
    private final ShardingDbStrategy shardingDbStrategy;
    private final Properties datasourceConfigTemplate = new Properties();
    // db
    private final List<DbConfig> dbConfigs = new ArrayList<>();
    private final Map<Integer, Db> dbMap = new ConcurrentHashMap<>();

    public DbGroup(final DbGroupConfig dbGroupConfig) throws SQLException {
        assert StringUtils.isNotBlank(dbGroupConfig.getGroupName());
        assert dbGroupConfig.getDataSourceCreateStrategy() != null;
        assert dbGroupConfig.getDatasourceConfigTemplate() != null;

        this.name = dbGroupConfig.getGroupName();
        this.createStrategy = dbGroupConfig.getDataSourceCreateStrategy();
        this.shardingDbStrategy = Optional.ofNullable(dbGroupConfig.getShardingDbStrategy()).orElse(ShardingDbStrategyDefault.instance);
        this.datasourceConfigTemplate.putAll(dbGroupConfig.getDatasourceConfigTemplate());
        this.dbConfigs.addAll(dbGroupConfig.getDbConfigs());
    }

    @Override
    public void init() {

    }


    @Override
    public void create() throws Exception {
        createDb(this.dbConfigs);
    }

    private void createDb(Collection<DbConfig> dbConfigs) throws Exception {
        if (isInit) {
            return;
        }
        isInit = true;

        if (createStrategy == null) {
            log.error("your DataSourceCreateStrategy is not set success, please check. dbGroup = {}", name);
            return;
        }

        for (DbConfig dbConfig : Optional.ofNullable(dbConfigs).orElse(Collections.emptyList())) {
            Integer dbId = dbConfig.getDbId();
            if (dbId == null) {
                log.error("shardingId is null. dbConfig = {}", dbConfig);
                continue;
            }
            dbMap.put(dbId, createDb(dbConfig));
        }
    }

    private Db createDb(DbConfig dbConfig) throws Exception {
        DataSource dataSource = createStrategy.create(datasourceConfigTemplate, dbConfig.getDbConfigMap());
        DbConfig configBuild = DbConfig.builder()
                .dbGroup(this)
                .dbId(dbConfig.getDbId())
                .build();
        return new Db(dataSource, configBuild);
    }

    public Db getDefulatDb() {
        return dbMap.get(0);
    }

    public Db getDb(Long toShardingId) {
        assert toShardingId != null;
        int dbId = shardingDbStrategy.calculate(toShardingId);
        Db db = dbMap.get(dbId);
        if (db == null) {
            throw new NotFoundInDbManagerException(this.name, dbId);
        }
        return db;
    }

    @Override
    public void destroy() {

    }

    @Override
    public void shutdown() {

    }


}
