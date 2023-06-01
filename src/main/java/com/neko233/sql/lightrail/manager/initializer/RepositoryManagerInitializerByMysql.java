package com.neko233.sql.lightrail.manager.initializer;

import com.neko233.sql.lightrail.RepositoryManager;
import com.neko233.sql.lightrail.condition.generator.ConditionGenerator;
import com.neko233.sql.lightrail.condition.generator.SqlOperation;
import com.neko233.sql.lightrail.db.Db;
import com.neko233.sql.lightrail.db.DbConfig;
import com.neko233.sql.lightrail.entity.Neko233ConfigTagKv;
import com.neko233.sql.lightrail.entity.Neko233Db;
import com.neko233.sql.lightrail.entity.Neko233DbShardingStrategy;
import com.neko233.sql.lightrail.entity.Neko233GroupConfigTemplate;
import com.neko233.sql.lightrail.manager.DbGroup;
import com.neko233.sql.lightrail.manager.DbGroupConfig;
import com.neko233.sql.lightrail.sharding.database.ShardingDbStrategy;
import com.neko233.sql.lightrail.sharding.database.ShardingDbStrategyDefault;
import com.neko233.sql.lightrail.sharding.database.ShardingDbStrategyFactory;
import com.neko233.sql.lightrail.sharding.database.ShardingDbStrategyNotInitException;
import com.neko233.sql.lightrail.strategy.createDataSource.DruidDataSourceCreateStrategy;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.*;

/**
 * @author SolarisNeko
 * Date on 2023-01-07
 */
@Slf4j
public class RepositoryManagerInitializerByMysql implements RepositoryManagerInitializer {

    public static final String SPLIT_TAG = "|";
    public static final String SPLIT_TAG_REGEX = "\\|";

    @Override
    public void initDbGroup(Db resourceDb,
                            List<String> groupNames) throws Exception {
        List<DbGroup> dbGroups = fetchDbGroupByMysql(resourceDb, groupNames);
        // 只要随便一个出错, 直接中断
        for (DbGroup dbGroup : dbGroups) {
            RepositoryManager.instance.addDbGroup(dbGroup);
        }
    }

    private List<DbGroup> fetchDbGroupByMysql(Db resourceDb,
                                              List<String> groupNames) throws Exception {
        checkNecessaryTables(resourceDb);

        List<DbGroup> outputDbGroupList = new ArrayList<>();
        for (String groupName : groupNames) {
            Object[] params = {groupName};
            List<Neko233GroupConfigTemplate> neko233GroupConfigTemplates = resourceDb.executeQuery(
                    "select * From neko233_db_group_config_template where group_name = ? ",
                    params,
                    Neko233GroupConfigTemplate.class);
            Map<String, Properties> groupNamePropertiesMap = Neko233GroupConfigTemplate.getGroupName2Properties(
                    neko233GroupConfigTemplates);


            Neko233DbShardingStrategy dbShardingStrategy = resourceDb.executeQuerySingle(
                    "select * From neko233_db_sharding_strategy where group_name = ? ",
                    params,
                    Neko233DbShardingStrategy.class);

            // sharding strategy
            ShardingDbStrategy shardingDbStrategy;
            if (Optional.of(dbShardingStrategy.isUseDefault).orElse(false)) {
                shardingDbStrategy = ShardingDbStrategyFactory.get(dbShardingStrategy.groupName,
                        ShardingDbStrategyDefault.instance);
            } else {
                shardingDbStrategy = ShardingDbStrategyFactory.get(dbShardingStrategy.groupName);
            }

            if (shardingDbStrategy == null) {
                throw new ShardingDbStrategyNotInitException(groupName);
            }

            // build dbGroup
            DbGroupConfig dbGroupConfig = DbGroupConfig.builder()
                    .groupName(groupName)
                    .datasourceConfigTemplate(groupNamePropertiesMap.get(groupName))
                    .dataSourceCreateStrategy(DruidDataSourceCreateStrategy.instance)
                    .shardingDbStrategy(shardingDbStrategy)
                    .dbConfigFetcher((dbGroupName -> {
                        List<Neko233Db> neko233Dbs = resourceDb.executeQuery(
                                "select * From neko233_db where group_name = ? ",
                                params,
                                Neko233Db.class);

                        List<DbConfig> dbConfigs = new ArrayList<>();
                        for (Neko233Db queryDb : neko233Dbs) {

                            String[] split = queryDb.tag.split(SPLIT_TAG_REGEX);

                            String selectConfigKvByTag = "select * From neko233_tag_config_kv where " + ConditionGenerator.condition(
                                    "tag",
                                    SqlOperation.IN,
                                    split,
                                    false);
                            log.debug("find tag from neko233_tag_config_kv. SQL = {}", selectConfigKvByTag);

                            List<Neko233ConfigTagKv> configTagKvs = resourceDb.executeQuery(
                                    selectConfigKvByTag,
                                    Neko233ConfigTagKv.class
                            );

                            Map<String, String> kvMap = Neko233ConfigTagKv.translateToKvMap(configTagKvs);
                            DbConfig dbConfig = DbConfig.builder()
                                    .dbId(queryDb.dbId)
                                    .dbGroup(null)
                                    .dbConfigMap(kvMap)
                                    .build();
                            dbConfigs.add(dbConfig);
                        }
                        return dbConfigs;
                    }))
                    .build();
            DbGroup dbGroup = new DbGroup(dbGroupConfig);
            outputDbGroupList.add(dbGroup);
        }
        return outputDbGroupList;
    }

    private static final String CREATE_CONFIG_TEMPLATE_TABLE = "create table if not exists neko233_db_group_config_template " +
            "( " +
            "    id             bigint auto_increment primary key, " +
            "    group_name     varchar(64)            not null, " +
            "    template_key   varchar(64)            not null, " +
            "    template_value text                   not null, " +
            "    add_dt         datetime default now() not null, " +
            "    unique key (group_name, template_key) " +
            ") charset = utf8mb4 ";

    private static final String CREATE_DB_TABLE = "create table if not exists neko233_db " +
            "( " +
            "    id         bigint auto_increment primary key, " +
            "    group_name varchar(64)            not null, " +
            "    db_id      int                    not null, " +
            "    tag       varchar(64)            not null comment '标签, use | split', " +
            "    add_dt     datetime default now() not null, " +
            "    unique key (group_name, db_id) " +
            ") charset = utf8mb4 ";

    private static final String CREATE_TAG_CONFIG_KV = "create table if not exists neko233_tag_config_kv " +
            "( " +
            "    id           bigint auto_increment primary key, " +
            "    tag         varchar(64), " +
            "    config_key   varchar(64)            not null, " +
            "    config_value text                   not null, " +
            "    add_dt       datetime  not null default now(), " +
            "    unique key (tag, config_key) " +
            ") charset = utf8mb4 ";

    private static final String CREATE_DB_SHARDING_STRATEGY = "create table if not exists neko233_db_sharding_strategy " +
            "( " +
            "    group_name             varchar(64)            not null primary key, " +
            "    sharding_strategy_name varchar(64)            not null comment 'db分片策略名(database 分库)', " +
            "    is_use_default         bool     default true  not null comment '是否使用默认的分片策略(不db分片)', " +
            "    add_dt                 datetime default now() not null, " +
            "    unique key (group_name) " +
            ") charset = utf8mb4 ";

    private void checkNecessaryTables(Db resourceDb) throws SQLException {
        resourceDb.executeUpdate(CREATE_CONFIG_TEMPLATE_TABLE);
        resourceDb.executeUpdate(CREATE_DB_TABLE);
        resourceDb.executeUpdate(CREATE_TAG_CONFIG_KV);
        resourceDb.executeUpdate(CREATE_DB_SHARDING_STRATEGY);
    }


}
