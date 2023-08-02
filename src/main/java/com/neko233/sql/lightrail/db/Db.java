package com.neko233.sql.lightrail.db;

import com.neko233.sql.lightrail.domain.ExecuteSqlContext;
import com.neko233.sql.lightrail.domain.SqlStatement;
import com.neko233.sql.lightrail.exception.RailPlatformException;
import com.neko233.sql.lightrail.exception.SqlLightRailException;
import com.neko233.sql.lightrail.shardingManager.DbGroup;
import com.neko233.sql.lightrail.orm.OrmHandler;
import com.neko233.sql.lightrail.plugin.PluginRegistry;
import com.neko233.sql.lightrail.util.SqlParamsUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;

/**
 * @author SolarisNeko on 2022-12-07
 **/
@Slf4j
@EqualsAndHashCode
@Getter
public class Db implements DbApi {


    private static final String LOG_PREFIX = "[Db] ";
    public static final String DEFAULT_DB_NAME = "db_group:resources";

    private final Integer dbId;
    private final String dbName;
    private final DataSource dataSource;
    private final DbGroup dbGroup;

    public Db(DataSource dataSource) {
        this(dataSource, DbConfig.builder().build());
    }

    public Db(DataSource dataSource,
              DbConfig dbConfig) {
        this.dbId = dbConfig.getDbId();
        this.dbGroup = dbConfig.getDbGroup();
        if (this.dbGroup == null) {
            this.dbName = DEFAULT_DB_NAME;
        } else {
            this.dbName = String.join(":", dbGroup.name, String.valueOf(dbConfig.getDbId()));
        }
        this.dataSource = dataSource;
    }

    @Override
    public <T> List<T> executeQuery(String sql,
                                    Collection<Object[]> paramsArray,
                                    Class<T> returnType) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            for (Object[] params : paramsArray) {
                if (SqlParamsUtil.setParams(ps, params)) {
                    ps.addBatch();
                }
            }
            ResultSet resultSet = ps.executeQuery();
            return OrmHandler.ormBatch(resultSet, returnType);
        }
    }

    @Override
    public int executeOriginalUpdate(String sql,
                                     Collection<Object[]> multiRowParams) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            for (Object[] params : multiRowParams) {
                if (SqlParamsUtil.setParams(ps, params)) {
                    ps.addBatch();
                }
            }
            int[] array = ps.executeBatch();
            connection.commit();
            return Arrays.stream(array).sum();
        }
    }

    @Override
    public <T> List<T> executeQuery(SqlStatement statement) throws SQLException {

        assert dataSource != null;

        ExecuteSqlContext context = ExecuteSqlContext.builder()
                .isDefaultProcess(true)
                .isAutoCommit(statement.getIsAutoCommit())
                .sqlList(statement.getSqlList())
                .plugins(PluginRegistry.getAll())
                .build();
        // multi DataSource
        Connection conn = dataSource.getConnection();
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

        } catch (Exception e) {
            log.error(LOG_PREFIX + "Execute query error will rollback. SQL = {}.", statement.getSqlList(), e);
            context.getConnection().rollback();
        } finally {
            context.close();
        }

        return Optional.ofNullable(context.getDataList()).orElse(new ArrayList<T>());
    }

    @Override
    public Integer executeUpdate(SqlStatement statement) throws SQLException {
        assert dataSource != null;
        if (statement.getIsQuery()) {
            return 0;
        }
        ExecuteSqlContext<?> context = ExecuteSqlContext.builder()
                .isDefaultProcess(true)
                .isAutoCommit(statement.getIsAutoCommit() == null || statement.getIsAutoCommit())
                .sqlList(statement.getSqlList())
                .plugins(PluginRegistry.getAll())
                .build();

        // 执行实际的操作
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(statement.getIsAutoCommit());
            context.setConnection(conn);

            // pre handle
            context.notifyPluginsBegin();
            List<String> sqlList = statement.getSqlList();

            for (String sql : sqlList) {
                if (StringUtils.isBlank(sql)) {
                    continue;
                }
                context.notifyPluginsPreExecuteSql();
                context.setPreparedStatement(conn.prepareStatement(sql));
                if (context.getIsDefaultProcess()) {
                    context.executeUpdate();
                }
                // post handle
                context.notifyPluginsPostExecuteSql();
            }


            context.commitTransaction();

        } catch (Exception e) {
            log.error(LOG_PREFIX + "Execute error SQL = {} Will rollback.", context.getSqlList(), e);
            context.getConnection().rollback();
        } finally {
            context.close();
        }
        context.notifyPluginsEnd();
        return context.getUpdateCount();
    }

    @Override
    public void executeOriginalUpdate(Consumer<Connection> doAnyThingConsumer) throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            doAnyThingConsumer.accept(conn);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
