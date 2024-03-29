package com.neko233.sql.lightrail.db;

import com.neko233.sql.lightrail.domain.SqlStatement;
import org.apache.commons.collections4.CollectionUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author SolarisNeko
 * Date on 2022-12-16
 */
public interface DbApi {

    DataSource getDataSource();

    default <T> T executeQuerySingle(String sql,
                                     Object[] paramsArray,
                                     Class<T> returnType) throws SQLException {
        if (paramsArray == null) {
            throw new IllegalArgumentException("your SQL prepareStatement ? params is null! check you input");
        }
        List<T> ts = executeQuery(sql, paramsArray, returnType);
        if (CollectionUtils.isEmpty(ts)) {
            return null;
        }
        return ts.get(0);
    }

    /**
     * 查询
     *
     * @param sql        SQL 模板
     * @param returnType 返回值
     * @param params     ? 对应的对象, 有多少个 ? 就要有多少个对象
     * @return 对象List
     * @throws SQLException
     */
    default <T> List<T> executeQuery(String sql,
                                     Object[] params,
                                     Class<T> returnType
    ) throws SQLException {
        return executeQuery(sql, Collections.singletonList(params), returnType);
    }


    /**
     * 占位符方式请求, 原生 JDBC
     *
     * @param sql         带 ? 占位符的 SQL
     * @param paramsArray 批量请求参数
     * @param returnType  返回类型
     * @param <T>         返回对象的范型
     * @return 对象
     * @throws SQLException
     */
    <T> List<T> executeQuery(String sql,
                             Collection<Object[]> paramsArray,
                             Class<T> returnType) throws SQLException;


    default <T> T executeQuerySingle(String sql,
                                     Class<T> returnType) throws SQLException {
        List<T> ts = executeQuery(sql, returnType);
        if (CollectionUtils.isEmpty(ts)) {
            return null;
        }
        return ts.get(0);
    }

    // query
    default <T> List<T> executeQuery(String sql,
                                     Class<T> returnType) throws SQLException {
        return executeQuery(
                SqlStatement.builder()
                        .isQuery(true)
                        .isAutoCommit(true)
                        .sqlList(Collections.singletonList(sql))
                        .returnType(returnType)
                        .build()
        );
    }

    <T> List<T> executeQuery(SqlStatement statement) throws SQLException;

    // update part
    default Integer executeUpdate(String sql) throws SQLException {
        return executeUpdate(Collections.singletonList(sql));
    }

    default Integer executeUpdate(List<String> sqlList) throws SQLException {
        return executeUpdate(SqlStatement.builder()
                .isQuery(false)
                .isAutoCommit(true)
                .sqlList(sqlList)
                .returnType(null)
                .build()
        );
    }

    Integer executeUpdate(SqlStatement statement) throws SQLException;

    default int executeOriginalUpdate(String sql,
                                      Object[] singleRowParams) throws SQLException {
        return executeOriginalUpdate(sql, Collections.singletonList(singleRowParams));
    }

    int executeOriginalUpdate(String sql,
                              Collection<Object[]> multiRowParams) throws SQLException;

    void executeOriginalUpdate(Consumer<Connection> doAnyThingConsumer) throws SQLException, Exception;

}
