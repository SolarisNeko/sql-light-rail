package com.neko233.sql.lightrail;

import com.neko233.sql.lightrail.builder.SelectSqlBuilder;
import com.neko233.sql.lightrail.builder.SqlBuilder;
import com.neko233.sql.lightrail.domain.ExecuteSqlContext;
import com.neko233.sql.lightrail.exception.RailPlatformException;
import com.neko233.sql.lightrail.exception.SqlLightRailException;
import com.neko233.sql.lightrail.orm.OrmHandler;
import com.neko233.sql.lightrail.plugin.Plugin;
import com.neko233.sql.lightrail.domain.SqlStatement;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
    public static final String NULL_STRING = "null";


    private RailPlatform() {
    }

    public synchronized static RailPlatform createLightRailPlatform(DataSource dataSource) {
        if (RailPlatform.MULTI_DATASOURCE_MAP.size() > 0) {
            log.debug(LOG_PREFIX + "You can't create RailPlatform by DataSource again.");
            return INSTANCE;
        }
        RailPlatform.MULTI_DATASOURCE_MAP.put("default", dataSource);
        return INSTANCE;
    }

    public synchronized static RailPlatform getInstance() {
        return INSTANCE;
    }

    public synchronized RailPlatform addDataSource(String shardingKey, DataSource newDataSource) {
        if (StringUtils.isBlank(shardingKey) || NULL_STRING.equalsIgnoreCase(shardingKey)) {
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

    public <T> List<T> executeQuery(String sql, Class<T> returnType) throws SQLException {
        return executeQuery(DEFAULT_SHARDING_KEY, sql, returnType);
    }

    public <T> List<T> executeQuery(String shardingKey, String sql, Class<T> returnType) throws SQLException {
        if (StringUtils.isBlank(shardingKey)) {
            log.error(LOG_PREFIX + "Sharding Key must not null!");
            throw new RailPlatformException(LOG_PREFIX + "Sharding Key must not null!");
        }
        return executeQuery(SqlStatement.builder()
                .shardingKey(shardingKey)
                .sqlList(Collections.singletonList(sql))
                .returnType(returnType)
                .isAutoCommit(true)
                .addTempPlugins(null)
                .excludePluginNames(null)
                .build());
    }

    public <T> List<T> executeQuery(SqlStatement statement) throws SQLException {
        checkDataSource();
        addSql2SqlListInSqlStatement(statement);
        checkSelectSqlStatement(statement);

        ExecuteSqlContext context = ExecuteSqlContext.builder()
                .shardingKey(statement.getShardingKey())
                .isDefaultProcess(true)
                .isAutoCommit(statement.getIsAutoCommit())
                .sqlList(statement.getSqlList())
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
        List<String> sqlList = statement.getSqlList();

        if (CollectionUtils.isEmpty(sqlList)) {
            throw new SqlLightRailException("Un-support empty SQL query");
        } else if (sqlList.size() > 1) {
            throw new SqlLightRailException("Un-support multi SQL Query! SQL = " + String.join("\n", sqlList));
        }

        context.setPreparedStatement(conn.prepareStatement(sqlList.get(0)));
        context.notifyPluginsPreExecuteSql();
        try {
            if (context.getIsDefaultProcess()) {
                context.executeQuery();
            }
            context.notifyPluginsPostExecuteSql();
            context.notifyPluginsEnd();

            // 获取最终结果
            if (context.getIsDefaultProcess()) {
                ResultSet rs = Optional.ofNullable(Objects.requireNonNull(context).getResultSet())
                        .orElseThrow(() -> new RailPlatformException(LOG_PREFIX + "Execute query error. SQL = " + statement.getSqlList()));
                List<?> orm = OrmHandler.ormBatch(rs, statement.getReturnType());
                context.setDataList(orm);
            }
        } catch (SQLException e) {
            log.error(LOG_PREFIX + "Execute query error will rollback. SQL = {}.", statement.getSqlList(), e);
            context.getConnection().rollback();
        } finally {
            Objects.requireNonNull(context).getConnection().close();
        }

        return Optional.ofNullable(context.getDataList()).orElse(new ArrayList<T>());
    }

    /**
     * 将 .sql() -> 整合到 .sqlList
     * @param statement 封装的查询语句
     */
    private static void addSql2SqlListInSqlStatement(SqlStatement statement) {
        List<String> sqlList = Optional.ofNullable(statement.getSqlList()).orElse(new ArrayList<>());
        String sql = statement.getSql();
        if (StringUtils.isNotBlank(sql)) {
            sqlList.add(sql);
            statement.setSqlList(sqlList);
        }
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

    public Integer executeUpdate(List<String> sqlList) throws SQLException {
        return executeUpdate(DEFAULT_SHARDING_KEY, sqlList);
    }

    public Integer executeUpdate(String shardingKey, String sql) throws SQLException {
        return executeUpdate(shardingKey, Collections.singletonList(sql));
    }

    public Integer executeUpdate(String shardingKey, List<String> sqlList) throws SQLException {
        if (StringUtils.isBlank(shardingKey)) {
            log.error(LOG_PREFIX + "Sharding Key must not null!");
            throw new RailPlatformException(LOG_PREFIX + "Sharding Key must not null!");
        }
        return executeUpdate(SqlStatement.builder()
                .shardingKey(shardingKey)
                .sqlList(sqlList)
                .isAutoCommit(true)
                .addTempPlugins(null)
                .excludePluginNames(null)
                .build());
    }

    public Integer executeUpdate(SqlStatement statement) throws SQLException {
        checkDataSource();
        addSql2SqlListInSqlStatement(statement);
        checkUpdateSqlStatement(statement);
        // 转换成一个完整的 RailPlatform sql 上下文
        ExecuteSqlContext context = ExecuteSqlContext.builder()
                .shardingKey(statement.getShardingKey())
                .isDefaultProcess(true)
                .isAutoCommit(statement.getIsAutoCommit() == null || statement.getIsAutoCommit())
                .sqlList(statement.getSqlList())
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
        // pre handle
        context.notifyPluginsBegin();
        List<String> sqlList = statement.getSqlList();
        // 执行实际的操作
        try {
            for (String sql : sqlList) {
                if (StringUtils.isBlank(sql)) {
                    continue;
                }
                context.notifyPluginsPreExecuteSql();
                context.setPreparedStatement(conn.prepareStatement(sql.trim()));
                if (context.getIsDefaultProcess()) {
                    context.executeUpdate();
                }
                // post handle
                context.notifyPluginsPostExecuteSql();
            }

        } catch (SQLException e) {
            log.error(LOG_PREFIX + "Execute error SQL = {} Will rollback.", context.getSqlList(), e);
            context.getConnection().rollback();
        } finally {
            Objects.requireNonNull(context).getConnection().close();
        }
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
        List<String> sqlList = sqlStatement.getSqlList();
        if (CollectionUtils.isEmpty(sqlList)) {
            throw new SqlLightRailException("[SqlStatement] You need to use '.sql(..)/.sqlList(..)' set your SQL.");
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
        List<String> sqlList = statement.getSqlList();
        if (CollectionUtils.isEmpty(sqlList)) {
            throw new SqlLightRailException("[SqlStatement] You need to use '.sql()' set your SQL.");
        }
        List<String> notBlankSql = sqlList.stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
        statement.setSqlList(notBlankSql);

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
