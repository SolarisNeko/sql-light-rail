package com.neko.lightrail.domain;

import com.neko.lightrail.plugin.Plugin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * 执行 SQL 期间的上下文
 *
 * @author SolarisNeko
 * Date on 2022-02-26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExecuteSqlContext<T> {

    private String sql;
    // 对应 prepareStatement 的占位符值
    private List<Object[]> valueList;
    // JDBC Connect
    private Boolean isAutoCommit;
    private Connection connection;
    private PreparedStatement preparedStatement;
    // 插件
    private List<Plugin> plugins;
    private List<Plugin> addPlugins;
    private List<String> excludePluginNames;
    // 是否使用默认执行的 JDBC, 如果为 false 需要提供 dataList 操作结果。
    private Boolean isProcessDefault;
    // 处理结果
    private ResultSet resultSet;
    private Integer updateCount;
    private List<T> dataList;


    public ResultSet executeQuery() throws SQLException {
        ResultSet rs = preparedStatement.executeQuery();
        this.setResultSet(rs);
        return rs;
    }

    /**
     * 前置处理
     */
    public void notifyPluginsPreExecuteSql() {
        for (Plugin plugin : plugins) {
            plugin.preExecuteSql(this);
        }
    }

    /**
     * 调用所有开始阶段
     */
    public void notifyPluginsBegin() {
        // add plugins
        if (CollectionUtils.isNotEmpty(addPlugins)) {
            plugins.addAll(addPlugins);
        }
        // remove plugins
        if (CollectionUtils.isNotEmpty(excludePluginNames)) {
            List<Plugin> ratePlugins = plugins.stream()
                .filter(plugin -> !excludePluginNames.contains(plugin.getPluginName()))
                .collect(toList());
            plugins = ratePlugins;
        }
        // template method
        for (Plugin plugin : plugins) {
            plugin.begin();
        }
    }


    /**
     * 执行查询, 插件处理结果集
     */
    public void notifyPluginsPostExecuteSql() throws SQLException {
        for (Plugin plugin : plugins) {
            plugin.postExecuteSql(this);
        }
    }

    public void notifyPluginsEnd() {
        for (Plugin plugin : plugins) {
            plugin.end();
        }
    }


    public void executeUpdate() throws SQLException {
        if (CollectionUtils.isNotEmpty(valueList)) {
            setValuesToPrepareStatement(preparedStatement, valueList);
        }
        updateCount = preparedStatement.executeUpdate();
    }

    private static void setValuesToPrepareStatement(PreparedStatement ps, List<Object[]> values) throws SQLException {
        for (Object[] value : values) {
            int count = 1;
            for (Object o : value) {
                String typeName = value.getClass().getTypeName();
                switch (typeName) {
                    case "Integer":
                    case "java.lang.Integer": {
                        ps.setInt(count, (Integer) o);
                        break;
                    }
                    case "Float":
                    case "java.lang.Float": {
                        ps.setFloat(count, (Float) o);
                        break;
                    }
                    case "Double":
                    case "java.lang.Double": {
                        ps.setDouble(count, (Double) o);
                        break;
                    }
                    case "Long":
                    case "java.lang.Long": {
                        ps.setLong(count, (Long) o);
                        break;
                    }
                    case "String":
                    case "java.lang.String": {
                        ps.setString(count, (String) o);
                        break;
                    }
                    case "BigDecimal":
                    case "java.math.BigDecimal": {
                        ps.setBigDecimal(count, (BigDecimal) o);
                        break;
                    }
                    case "Boolean":
                    case "java.lang.Boolean": {
                        ps.setBoolean(count, (Boolean) o);
                        break;
                    }
                    case "Date":
                    case "java.util.Date": {
                        // datetime = "yyyy-MM-dd hh:mm:ss"
                        ps.setDate(count, (Date) o);
                        break;
                    }
                    case "Byte":
                    case "java.lang.Byte": {
                        ps.setByte(count, (Byte) o);
                        break;
                    }
                    default: {
                        throw new RuntimeException("[ORM] 不支持该类型");
                    }
                }
                count++;
            }

        }
    }
}
