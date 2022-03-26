package com.neko233.lightrail;

import com.neko233.lightrail.builder.SelectSqlBuilder;
import com.neko233.lightrail.builder.SqlBuilder;
import com.neko233.lightrail.domain.ExecuteSqlContext;
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
 * @author SolarisNeko
 * Date on 2022-02-26
 */
@Slf4j
public class RailPlatform {

    private static final RailPlatform INSTANCE = new RailPlatform();

    private static final String LOG_PREFIX_TITLE = "[LightRail Platform] ";

    /**
     * Multi DataSource Cache
     */
    private static Map<String, DataSource> MULTI_DATASOURCE_MAP = new ConcurrentHashMap<>();
    private static final String DEFAULT_SHARDING_KEY = "default";

    /**
     * Plugin
     */
    private static final String JOIN_SCHEMA_TABLE_KEY = ">";

    private static final List<Plugin> GLOBAL_PLUGINS = new ArrayList<>();


    private RailPlatform() {
    }

    public synchronized static RailPlatform createLightRailPlatform(DataSource dataSource) {
        if (RailPlatform.MULTI_DATASOURCE_MAP.size() > 0) {
            log.error(LOG_PREFIX_TITLE + "You can't create RailPlatform by DataSource again.");
            return INSTANCE;
        }
        RailPlatform.MULTI_DATASOURCE_MAP.put("default", dataSource);
        return INSTANCE;
    }

    public synchronized static RailPlatform getInstance() {
        return INSTANCE;
    }

    public synchronized RailPlatform addDataSource(String shardingKey, DataSource newDataSource) {
        DataSource originalDataSource = MULTI_DATASOURCE_MAP.get(shardingKey);
        if (originalDataSource != null) {
            log.info("Override dataSource Cache. ShardingKey = {}, original DataSource = {}, new DataSource = {}",
                shardingKey, originalDataSource, newDataSource);
        }
        MULTI_DATASOURCE_MAP.put(shardingKey, newDataSource);
        return getInstance();
    }

    /**
     * The rule of creating sharding key.
     *
     * @param url    DataBase URL, you can write simple like 'localhost:3306'
     * @param schema schema = database = use ???
     * @param table  表名
     * @return shardingKey
     */
    private static String getShardingKey(String url, String schema, String table) {
        return url + JOIN_SCHEMA_TABLE_KEY + schema + JOIN_SCHEMA_TABLE_KEY + table;
    }

    /**
     * add DataSource(Pool)
     *
     * @param url
     * @param schema
     * @param table
     * @param dataSource
     * @return platform
     */
    public RailPlatform addDataSource(String url, String schema, String table, DataSource dataSource) {
        return addDataSource(getShardingKey(url, schema, table), dataSource);
    }

    public synchronized RailPlatform removeDataSource(String shardingKey) {
        DataSource remove = MULTI_DATASOURCE_MAP.remove(shardingKey);
        if (remove != null) {
            log.info("You remove a DataSource From RailPlatform cacheJ! DataSource = {}", remove);
        }
        return getInstance();
    }

    public RailPlatform removeDataSource(String url, String schema, String table) {
        return removeDataSource(getShardingKey(url, schema, table));
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

    public DataSource getDataSource(String url, String schema, String table) {
        return MULTI_DATASOURCE_MAP.get(getShardingKey(url, schema, table));
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

    /**
     * use sharding key = 'default', if you need to choose another DataSource, you need to give me
     * a SqlStatement object.
     *
     * @param sqlBuilder SQL Builder
     * @param returnType return Type
     * @param <T>
     * @return
     * @throws SQLException
     */
    public <T> List<T> executeQuery(SelectSqlBuilder sqlBuilder, Class<?> returnType) throws SQLException {
        return executeQuery(DEFAULT_SHARDING_KEY, sqlBuilder, returnType);
    }

    public <T> List<T> executeQuery(String shardingKey, SelectSqlBuilder sqlBuilder, Class<?> returnType) throws SQLException {
        return executeQuery(SqlStatement.builder()
            .shardingKey(shardingKey)
            .sql(sqlBuilder.build())
            .returnType(returnType)
            .isAutoCommit(true)
            .addTempPlugins(null)
            .excludePluginNames(null)
            .build());
    }

    public <T> List<T> executeQuery(SqlStatement statement) throws SQLException {
        checkDataSource();
        checkSelectSqlStatement(statement);

        ExecuteSqlContext context = null;
        try {
            context = ExecuteSqlContext.builder()
                .isProcessDefault(true)
                .isAutoCommit(statement.getIsAutoCommit())
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
            conn.setAutoCommit(statement.getIsAutoCommit() == null || statement.getIsAutoCommit());
            context.setConnection(conn);
            context.notifyPluginsBegin();
            context.setPreparedStatement(conn.prepareStatement(statement.getSql()));
            context.notifyPluginsPreExecuteSql();
            if (context.getIsProcessDefault()) {
                context.executeQuery();
            }
            context.notifyPluginsPostExecuteSql();
            context.notifyPluginsEnd();
        } catch (SQLException e) {
            context.getConnection().rollback();
            e.printStackTrace();
        }
        if (context.getIsProcessDefault()) {
            ResultSet rs = Optional.ofNullable(Objects.requireNonNull(context).getResultSet())
                .orElseThrow(() -> new RuntimeException(LOG_PREFIX_TITLE + "execute query error."));
            context.setDataList(RailPlatformOrm.orm(rs, statement.getReturnType()));
        }

        Objects.requireNonNull(context).getConnection().close();
        return Optional.ofNullable(context.getDataList()).orElse(new ArrayList<>());
    }


    /**
     * SQL - Insert / Update / Delete
     *
     * @param sqlBuilder sql
     * @return update count
     * @throws SQLException
     */
    public Integer executeUpdate(SqlBuilder sqlBuilder) throws SQLException {
        return executeUpdate(DEFAULT_SHARDING_KEY, sqlBuilder);
    }

    public Integer executeUpdate(String shardingKey, SqlBuilder sqlBuilder) throws SQLException {
        return executeUpdate(SqlStatement.builder()
            .shardingKey(shardingKey)
            .sql(sqlBuilder.build())
            .isAutoCommit(true)
            .addTempPlugins(null)
            .excludePluginNames(null)
            .build());
    }

    public Integer executeUpdate(SqlStatement statement) throws SQLException {
        checkDataSource();
        checkUpdateSqlStatement(statement);

        ExecuteSqlContext context = null;
        try {
            context = ExecuteSqlContext.builder()
                .isProcessDefault(true)
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
            // 如有需要, 请在 Plugin 的 Pre / Post 阶段操作。
            if (context.getIsProcessDefault()) {
                context.executeUpdate();
            }
            context.notifyPluginsPostExecuteSql();
            context.notifyPluginsEnd();
        } catch (SQLException e) {
            try {
                context.getConnection().rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        Objects.requireNonNull(context).getConnection().close();
        return context.getUpdateCount();
    }

    /**
     * Check 'sql' and 'return type'
     * set default sharding key if not absent.
     *
     * @param statement
     */
    private void checkSelectSqlStatement(SqlStatement statement) {
        if (StringUtils.isBlank(statement.getSql())) {
            throw new SqlLightRailException("[SqlStatement] You need to '.sql()' set your SQL.");
        }
        if (statement.getReturnType() == null) {
            throw new SqlLightRailException("[SqlStatement] You need to '.returnType()' set your return class.");
        }

        // set shardingKey
        if (StringUtils.isBlank(statement.getShardingKey())) {
            statement.setShardingKey(DEFAULT_SHARDING_KEY);
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
            throw new RuntimeException(LOG_PREFIX_TITLE + "Your DataSource is not set!");
        }
    }


}
