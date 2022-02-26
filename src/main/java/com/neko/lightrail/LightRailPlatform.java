package com.neko.lightrail;

import com.neko.lightrail.builder.DeleteSqlBuilder;
import com.neko.lightrail.builder.InsertSqlBuilder;
import com.neko.lightrail.builder.SelectSqlBuilder;
import com.neko.lightrail.builder.SqlBuilder;
import com.neko.lightrail.builder.UpdateSqlBuilder;
import com.neko.lightrail.domain.ExecuteSqlContext;
import com.neko.lightrail.orm.LightRailOrm;
import com.neko.lightrail.plugin.LightRailPlugin;
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
 * @date 2022-02-26
 */
@Slf4j
public final class LightRailPlatform {

    private static final LightRailPlatform INSTANCE = new LightRailPlatform();

    /**
     * 多数据源
     */
    private static Map<String, DataSource> MULTI_DATASOURCE_MAP = new ConcurrentHashMap<>();
    private static final String DEFAULT_DATASOURCE_KEY = "default";
    private static final String JOIN_SCHEMA_TABLE_KEY = ">";

    private static final List<LightRailPlugin> plugins = new ArrayList<>();

    private static final String LOG_PREFIX_TITLE = "[LightRail Platform] ";

    private LightRailPlatform() {
    }

    public synchronized static LightRailPlatform createLightRailPlatform(DataSource dataSource) {
        if (LightRailPlatform.MULTI_DATASOURCE_MAP.size() > 0) {
            log.warn(LOG_PREFIX_TITLE + "You can't create DataSource again.");
            return INSTANCE;
        }
        LightRailPlatform.MULTI_DATASOURCE_MAP.put("default", dataSource);
        return INSTANCE;
    }

    public synchronized static LightRailPlatform getInstance() {
        return INSTANCE;
    }

    public static LightRailPlatform addDataSource(String shardingKey, DataSource dataSource) {
        MULTI_DATASOURCE_MAP.put(shardingKey, dataSource);
        return getInstance();
    }

    public static LightRailPlatform addDataSource(String schema, String table, DataSource dataSource) {
        return addDataSource(schema + JOIN_SCHEMA_TABLE_KEY + table, dataSource);
    }

    public static LightRailPlatform removeDataSource(String shardingKey) {
        MULTI_DATASOURCE_MAP.remove(shardingKey);
        return getInstance();
    }

    public static LightRailPlatform removeDataSource(String schema, String table) {
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

    /**
     * 注册插件
     * @param plugin
     * @return
     */
    public LightRailPlatform registerPlugin(LightRailPlugin plugin) {
        plugins.add(plugin);
        plugin.initPlugin();
        return getInstance();
    }

    /**
     * Select
     */
    public static Integer executeUpdate(SqlBuilder sqlBuilder) {
        return executeUpdate(sqlBuilder.build());
    }

    /**
     * Select
     */
    public static <T> List<T> executeQuery(SelectSqlBuilder sqlBuilder, Class<?> clazz) {
        return executeQuery(sqlBuilder.build(), clazz);
    }

    /**
     * Select ORM to List<Class object>
     */
    public static <T> List<T> executeQuery(String sql, Class<?> clazz) {
        checkDataSource();
        ExecuteSqlContext context = null;
        try {
            context = ExecuteSqlContext.builder()
                .sql(sql)
                .plugins(plugins)
                .build();
            context.notifyPluginsBegin();
            Connection conn = getDefaultDataSource().getConnection();
            context.setConnection(conn);
            context.setPreparedStatement(conn.prepareStatement(sql));
            context.notifyPluginsPreExecuteSql();
            // 如有需要, 请在 Plugin 的 Pre / Post 阶段操作。
            context.executeQuery();
            context.notifyPluginsPostExecuteSql();
            context.notifyPluginsFinish();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ResultSet rs = Optional.ofNullable(Objects.requireNonNull(context).getResultSet())
            .orElseThrow(() -> new RuntimeException(LOG_PREFIX_TITLE + "execute query error."));
        return LightRailOrm.mapping(rs, clazz);
    }

    /**
     * Insert
     */
    public static Integer executeUpdate(InsertSqlBuilder builder) {
        return executeUpdate(builder.build());
    }

    /**
     * Update
     */
    public static Integer executeUpdate(UpdateSqlBuilder builder) {
        return executeUpdate(builder.build());
    }

    /**
     * Delete
     */
    public static Integer executeUpdate(DeleteSqlBuilder builder) {
        return executeUpdate(builder.build());
    }

    /**
     * 执行更新
     * @return 修改行数
     */
    public static Integer executeUpdate(String sql) {
        checkDataSource();
        ExecuteSqlContext context = null;
        try {
            context = ExecuteSqlContext.builder()
                .sql(sql)
                .plugins(plugins)
                .build();
            context.notifyPluginsBegin();
            Connection conn = getDefaultDataSource().getConnection();
            context.setConnection(conn);
            context.setPreparedStatement(conn.prepareStatement(sql));
            context.notifyPluginsPreExecuteSql();
            // 如有需要, 请在 Plugin 的 Pre / Post 阶段操作。
            context.executeUpdate();
            context.notifyPluginsPostExecuteSql();
            context.notifyPluginsFinish();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return context.getUpdateCount();
    }

    /**
     * 有占位符的 insert/Update SQL
     * @return 更新数量
     */
    public static Integer executeUpdate(String sql, List<Object[]> valueList) {
        checkDataSource();
        ExecuteSqlContext context = null;
        try {
            context = ExecuteSqlContext.builder()
                .sql(sql)
                .valueList(valueList)
                .plugins(plugins)
                .build();
            context.notifyPluginsBegin();
            Connection conn = getDefaultDataSource().getConnection();
            context.setConnection(conn);
            context.setPreparedStatement(conn.prepareStatement(sql));
            context.notifyPluginsPreExecuteSql();
            // 如有需要, 请在 Plugin 的 Pre / Post 阶段操作。
            context.executeUpdate();
            context.notifyPluginsPostExecuteSql();
            context.notifyPluginsFinish();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return context.getUpdateCount();
    }

    private static void checkDataSource() {
        if (MULTI_DATASOURCE_MAP == null) {
            throw new RuntimeException(LOG_PREFIX_TITLE + "Your DataSource is not set!");
        }
    }





}
