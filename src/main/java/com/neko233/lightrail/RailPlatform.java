package com.neko233.lightrail;

import com.neko233.lightrail.builder.SelectSqlBuilder;
import com.neko233.lightrail.builder.SqlBuilder;
import com.neko233.lightrail.domain.ExecuteSqlContext;
import com.neko233.lightrail.orm.LightRailOrm;
import com.neko233.lightrail.plugin.Plugin;
import lombok.extern.slf4j.Slf4j;

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

    /**
     * 多数据源
     */
    private static Map<String, DataSource> MULTI_DATASOURCE_MAP = new ConcurrentHashMap<>();
    private static final String DEFAULT_DATASOURCE_KEY = "default";
    private static final String JOIN_SCHEMA_TABLE_KEY = ">";

    private static final List<Plugin> GLOBAL_PLUGINS = new ArrayList<>();

    private static final String LOG_PREFIX_TITLE = "[LightRail Platform] ";

    private RailPlatform() {
    }

    public synchronized static RailPlatform createLightRailPlatform(DataSource dataSource) {
        if (RailPlatform.MULTI_DATASOURCE_MAP.size() > 0) {
            log.warn(LOG_PREFIX_TITLE + "You can't create DataSource again.");
            return INSTANCE;
        }
        RailPlatform.MULTI_DATASOURCE_MAP.put("default", dataSource);
        return INSTANCE;
    }

    public synchronized static RailPlatform getInstance() {
        return INSTANCE;
    }

    public static RailPlatform addDataSource(String shardingKey, DataSource dataSource) {
        MULTI_DATASOURCE_MAP.put(shardingKey, dataSource);
        return getInstance();
    }

    public static RailPlatform addDataSource(String schema, String table, DataSource dataSource) {
        return addDataSource(schema + JOIN_SCHEMA_TABLE_KEY + table, dataSource);
    }

    public static RailPlatform removeDataSource(String shardingKey) {
        MULTI_DATASOURCE_MAP.remove(shardingKey);
        return getInstance();
    }

    public static RailPlatform removeDataSource(String schema, String table) {
        return removeDataSource(schema + JOIN_SCHEMA_TABLE_KEY + table);
    }

    public static DataSource getDefaultDataSource() {
        return MULTI_DATASOURCE_MAP.get(DEFAULT_DATASOURCE_KEY);
    }

    public static DataSource getDataSource(String shardingKey) {
        return MULTI_DATASOURCE_MAP.get(shardingKey);
    }

    public static DataSource getDataSource(String schema, String table) {
        return MULTI_DATASOURCE_MAP.get(schema + JOIN_SCHEMA_TABLE_KEY + table);
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

    public <T> List<T> executeQuery(Class<?> clazz) {
        return executeQuery(SqlLightRail.selectTable(clazz).build(), clazz, true, null, null);
    }

    public <T> List<T> executeQuery(SelectSqlBuilder sqlBuilder, Class<?> clazz) {
        return executeQuery(sqlBuilder.build(), clazz, true, null, null);
    }

    public <T> List<T> executeQuery(String sql, Class<?> clazz, Boolean isAutoCommit, List<Plugin> addPlugins, List<String> excludePluginNames) {
        checkDataSource();
        ExecuteSqlContext context = null;
        try {
            context = ExecuteSqlContext.builder()
                    .isProcessDefault(true)
                    .isAutoCommit(isAutoCommit)
                    .sql(sql)
                    .plugins(GLOBAL_PLUGINS)
                    .addPlugins(addPlugins)
                    .excludePluginNames(excludePluginNames)
                    .build();
            Connection conn = getDefaultDataSource().getConnection();
            conn.setAutoCommit(isAutoCommit);
            context.setConnection(conn);
            context.notifyPluginsBegin();
            context.setPreparedStatement(conn.prepareStatement(sql));
            context.notifyPluginsPreExecuteSql();
            if (context.getIsProcessDefault()) {
                context.executeQuery();
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
        if (context.getIsProcessDefault()) {
            ResultSet rs = Optional.ofNullable(Objects.requireNonNull(context).getResultSet())
                    .orElseThrow(() -> new RuntimeException(LOG_PREFIX_TITLE + "execute query error."));
            context.setDataList(LightRailOrm.mapping(rs, clazz));
        }
        return Optional.ofNullable(context.getDataList()).orElse(new ArrayList<>());
    }

    public Integer executeUpdate(SqlBuilder sqlBuilder) {
        return executeUpdate(sqlBuilder.build(), true, null, null);
    }

    public Integer executeUpdate(SqlBuilder sqlBuilder, Boolean isAutoCommit) {
        return executeUpdate(sqlBuilder.build(), isAutoCommit, null, null);
    }

    public Integer executeUpdate(SqlBuilder sqlBuilder, Boolean isAutoCommit, List<String> excludePluginNames) {
        return executeUpdate(sqlBuilder.build(), isAutoCommit, null, excludePluginNames);
    }


    public Integer executeUpdate(String sql, Boolean isAutoCommit, List<Plugin> addPlugins, List<String> excludePluginNames) {
        checkDataSource();
        ExecuteSqlContext context = null;
        try {
            context = ExecuteSqlContext.builder()
                    .isProcessDefault(true)
                    .isAutoCommit(isAutoCommit)
                    .sql(sql)
                    .plugins(GLOBAL_PLUGINS)
                    .addPlugins(addPlugins)
                    .excludePluginNames(excludePluginNames)
                    .build();
            Connection conn = getDefaultDataSource().getConnection();
            conn.setAutoCommit(isAutoCommit);
            context.setConnection(conn);
            context.notifyPluginsBegin();
            context.setPreparedStatement(conn.prepareStatement(sql));
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
        return context.getUpdateCount();
    }

    public Integer executeUpdate(String sql, List<Object[]> valueList, Boolean isAutoCommit, List<Plugin> addPlugins, List<String> excludePluginNames) {
        checkDataSource();
        ExecuteSqlContext context = null;
        try {
            context = ExecuteSqlContext.builder()
                    .isProcessDefault(true)
                    .isAutoCommit(isAutoCommit)
                    .sql(sql)
                    .valueList(valueList)
                    .plugins(GLOBAL_PLUGINS)
                    .excludePluginNames(excludePluginNames)
                    .build();
            Connection conn = getDefaultDataSource().getConnection();
            conn.setAutoCommit(isAutoCommit);
            context.setConnection(conn);
            context.notifyPluginsBegin();
            context.setPreparedStatement(conn.prepareStatement(sql));
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
        return context.getUpdateCount();
    }

    private void checkDataSource() {
        if (MULTI_DATASOURCE_MAP == null) {
            throw new RuntimeException(LOG_PREFIX_TITLE + "Your DataSource is not set!");
        }
    }


}
