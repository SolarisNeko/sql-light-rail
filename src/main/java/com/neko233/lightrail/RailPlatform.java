package com.neko233.lightrail;

import com.neko233.lightrail.builder.SelectSqlBuilder;
import com.neko233.lightrail.builder.SqlBuilder;
import com.neko233.lightrail.domain.ExecuteSqlContext;
import com.neko233.lightrail.exception.RailPlatformException;
import com.neko233.lightrail.exception.SqlLightRailException;
import com.neko233.lightrail.orm.RailPlatformOrm;
import com.neko233.lightrail.plugin.Plugin;
import com.neko233.lightrail.pojo.SqlStatement;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ShardingKey is not sorted and random. Need Developer to set your rule to keep it in rule.
 * ShardingKey 无规则, 需开发者自定义一套规则。
 *
 * @author SolarisNeko
 * Date on 2022-02-26
 */
@Slf4j
public class RailPlatform {

    private static final RailPlatform INSTANCE = new RailPlatform();

    private static final String LOG_PREFIX = "[RailPlatform] ";

    /**
     * Multi DataSource Cache
     */
    private static final Map<String, DataSource> MULTI_DATASOURCE_MAP = new ConcurrentHashMap<>();
    private static final String DEFAULT_SHARDING_KEY = "default";

    private static final List<Plugin> GLOBAL_PLUGINS = new ArrayList<>();


    private RailPlatform() {
    }

    public synchronized static RailPlatform createLightRailPlatform(DataSource dataSource) {
        if (RailPlatform.MULTI_DATASOURCE_MAP.size() > 0) {
            log.error(LOG_PREFIX + "You can't create RailPlatform by DataSource again.");
            return INSTANCE;
        }
        RailPlatform.MULTI_DATASOURCE_MAP.put("default", dataSource);
        return INSTANCE;
    }

    public synchronized static RailPlatform getInstance() {
        return INSTANCE;
    }

    public synchronized RailPlatform addDataSource(String shardingKey, DataSource newDataSource) {
        if (StringUtils.isBlank(shardingKey) || "null".equalsIgnoreCase(shardingKey)) {
            throw new RuntimeException("[RailPlatform] Sharding Key must not null / 'null' !");
        }
        DataSource originalDataSource = MULTI_DATASOURCE_MAP.get(shardingKey);
        if (originalDataSource != null) {
            log.info("Override dataSource Cache. ShardingKey = {}, original DataSource = {}, new DataSource = {}",
                    shardingKey, originalDataSource, newDataSource);
        }
        MULTI_DATASOURCE_MAP.put(shardingKey, newDataSource);
        return getInstance();
    }

    public synchronized RailPlatform removeDataSource(String shardingKey) {
        DataSource remove = MULTI_DATASOURCE_MAP.remove(shardingKey);
        if (remove != null) {
            log.info(LOG_PREFIX + "You remove a DataSource From RailPlatform cacheJ! DataSource = {}", remove);
        }
        return getInstance();
    }

    public DataSource getDefaultDataSource() {
        return MULTI_DATASOURCE_MAP.get(DEFAULT_SHARDING_KEY);
    }

    /**
     * get DataSource by sharding key.
     *
     * @param shardingKey 分库分表的 ID
     * @return DataSource | maybe null
     */
    public DataSource getDataSource(String shardingKey) {
        return MULTI_DATASOURCE_MAP.get(shardingKey);
    }

    public RailPlatform addGlobalPlugin(Plugin plugin) {
        if (GLOBAL_PLUGINS.contains(plugin)) {
            return getInstance();
        }
        GLOBAL_PLUGINS.add(plugin);
        plugin.initPlugin();
        return getInstance();
    }

    public RailPlatform removeGlobalPlugin(Plugin plugin) {
        GLOBAL_PLUGINS.remove(plugin);
        plugin.initPlugin();
        return getInstance();
    }

    public RailPlatform removeAllPlugins() {
        GLOBAL_PLUGINS.clear();
        return getInstance();
    }

    /**
     * use sharding key = 'default', if you need to choose another DataSource, you need to give me
     * a SqlStatement object.
     *
     * @param sqlBuilder SQL Builder
     * @param returnType return Type
     * @param <T>        范型
     * @return 列表
     * @throws SQLException 执行 SQL 异常
     */
    public <T> List<T> executeQuery(SelectSqlBuilder sqlBuilder, Class<T> returnType) throws SQLException {
        return executeQuery(sqlBuilder.build(), returnType);
    }

    public <T> List<T> executeQuery(String sql, Class<?> returnType) throws SQLException {
        return executeQuery(DEFAULT_SHARDING_KEY, sql, returnType);
    }

    public <T> List<T> executeQuery(String shardingKey, String sql, Class<?> returnType) throws SQLException {
        if (StringUtils.isBlank(shardingKey)) {
            log.error(LOG_PREFIX + "Sharding Key must not null!");
            throw new RailPlatformException(LOG_PREFIX + "Sharding Key must not null!");
        }
        return executeQuery(SqlStatement.builder()
                .shardingKey(shardingKey)
                .sql(sql)
                .returnType(returnType)
                .isAutoCommit(true)
                .addTempPlugins(null)
                .excludePluginNames(null)
                .build());
    }

    public <T> List<T> executeQuery(SqlStatement statement) throws SQLException {
        checkDataSource();
        checkSelectSqlStatement(statement);

        ExecuteSqlContext context;
        context = ExecuteSqlContext.builder()
                .shardingKey(statement.getShardingKey())
                .isDefaultProcess(true)
                .isAutoCommit(statement.getIsAutoCommit())
                .sql(statement.getSql())
                .plugins(GLOBAL_PLUGINS)
                .addPlugins(statement.getAddTempPlugins())
                .excludePluginNames(statement.getExcludePluginNames())
                .build();
        // multi DataSource
        Connection conn;
        if (MULTI_DATASOURCE_MAP.get(statement.getShardingKey()) == null) {
            log.error(LOG_PREFIX + "ShardingKey miss! Please check your input!");
            throw new RuntimeException(LOG_PREFIX + "ShardingKey miss! Please check your input!");
        } else {
            conn = MULTI_DATASOURCE_MAP.get(statement.getShardingKey()).getConnection();
        }
        conn.setAutoCommit(statement.getIsAutoCommit() == null || statement.getIsAutoCommit());
        context.setConnection(conn);
        context.notifyPluginsBegin();
        context.setPreparedStatement(conn.prepareStatement(statement.getSql()));
        context.notifyPluginsPreExecuteSql();
        try {
            if (context.getIsDefaultProcess()) {
                context.executeQuery();
            }
        } catch (SQLException e) {
            log.error(LOG_PREFIX + "Execute query error will rollback. SQL = {}.", statement.getSql(), e);
            context.getConnection().rollback();
        } finally {
            Objects.requireNonNull(context).getConnection().close();
        }
        context.notifyPluginsPostExecuteSql();
        context.notifyPluginsEnd();

        // 获取最终结果
        if (context.getIsDefaultProcess()) {
            ResultSet rs = Optional.ofNullable(Objects.requireNonNull(context).getResultSet())
                    .orElseThrow(() -> new RailPlatformException(LOG_PREFIX + "Execute query error. SQL = " + statement.getSql()));
            context.setDataList(RailPlatformOrm.orm(rs, statement.getReturnType()));
        }
        return Optional.ofNullable(context.getDataList()).orElse(new ArrayList<T>());
    }


    /**
     * SQL - Insert / Update / Delete
     *
     * @param sqlBuilder sql
     * @return update count
     * @throws SQLException SQL 有问题
     */
    public Integer executeUpdate(SqlBuilder sqlBuilder) throws SQLException {
        return executeUpdate(DEFAULT_SHARDING_KEY, sqlBuilder.build());
    }

    public Integer executeUpdate(String sql) throws SQLException {
        return executeUpdate(DEFAULT_SHARDING_KEY, sql);
    }

    public Integer executeUpdate(String shardingKey, String sql) throws SQLException {
        if (StringUtils.isBlank(shardingKey)) {
            log.error(LOG_PREFIX + "Sharding Key must not null!");
            throw new RailPlatformException(LOG_PREFIX + "Sharding Key must not null!");
        }
        return executeUpdate(SqlStatement.builder()
                .shardingKey(shardingKey)
                .sql(sql)
                .isAutoCommit(true)
                .addTempPlugins(null)
                .excludePluginNames(null)
                .build());
    }

    public Integer executeUpdate(SqlStatement statement) throws SQLException {
        checkDataSource();
        checkUpdateSqlStatement(statement);
        // 转换成一个完整的 RailPlatform sql 上下文
        ExecuteSqlContext context;
        context = ExecuteSqlContext.builder()
                .shardingKey(statement.getShardingKey())
                .isDefaultProcess(true)
                .isAutoCommit(statement.getIsAutoCommit() == null || statement.getIsAutoCommit())
                .sql(statement.getSql())
                .plugins(GLOBAL_PLUGINS)
                .addPlugins(statement.getAddTempPlugins())
                .excludePluginNames(statement.getExcludePluginNames())
                .build();
        // multi DataSource
        Connection conn;
        if (MULTI_DATASOURCE_MAP.get(statement.getShardingKey()) == null) {
            conn = getDefaultDataSource().getConnection();
        } else {
            conn = MULTI_DATASOURCE_MAP.get(statement.getShardingKey()).getConnection();
        }

        conn.setAutoCommit(statement.getIsAutoCommit());
        context.setConnection(conn);
        context.notifyPluginsBegin();
        context.setPreparedStatement(conn.prepareStatement(statement.getSql()));
        context.notifyPluginsPreExecuteSql();
        // 执行实际的操作
        try {
            if (context.getIsDefaultProcess()) {
                context.executeUpdate();
            }
        } catch (SQLException e) {
            log.error(LOG_PREFIX + "Execute error SQL = {} Will rollback.", context.getSql(), e);
            context.getConnection().rollback();
        } finally {
            Objects.requireNonNull(context).getConnection().close();
        }
        context.notifyPluginsPostExecuteSql();
        context.notifyPluginsEnd();
        return context.getUpdateCount();
    }

    /**
     * Check 'sql' and 'return type'
     * set default sharding key if not absent.
     *
     * @param sqlStatement SQL 清单
     */
    private void checkSelectSqlStatement(SqlStatement sqlStatement) {
        if (StringUtils.isBlank(sqlStatement.getSql())) {
            throw new SqlLightRailException("[SqlStatement] You need to '.sql()' set your SQL.");
        }
        if (sqlStatement.getReturnType() == null) {
            throw new SqlLightRailException("[SqlStatement] You need to '.returnType()' set your return class.");
        }

        // set shardingKey
        if (StringUtils.isBlank(sqlStatement.getShardingKey())) {
            sqlStatement.setShardingKey(DEFAULT_SHARDING_KEY);
        }
    }

    private void checkUpdateSqlStatement(SqlStatement statement) {
        if (StringUtils.isBlank(statement.getSql())) {
            throw new SqlLightRailException("[SqlStatement] You need to '.sql()' set your SQL.");
        }

        // set shardingKey
        if (StringUtils.isBlank(statement.getShardingKey())) {
            statement.setShardingKey(DEFAULT_SHARDING_KEY);
        }
    }

    private void checkDataSource() {
        if (MULTI_DATASOURCE_MAP == null) {
            throw new RuntimeException(LOG_PREFIX + "Your DataSource is not set!");
        }
    }


}
