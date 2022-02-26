package com.neko.lightrail.domain;

import com.neko.lightrail.plugin.LightRailPlugin;
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

/**
 * 执行 SQL 期间的上下文
 *
 * @author SolarisNeko
 * @date 2022-02-26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExecuteSqlContext {

    private String sql;
    // prepareStatement 的占位符值
    private List<Object[]> valueList;
    private Connection connection;
    private PreparedStatement preparedStatement;
    // 插件
    private List<LightRailPlugin> plugins;
    // 处理结果
    private ResultSet resultSet;
    private Integer updateCount;


    public ResultSet executeQuery() throws SQLException {
        ResultSet rs = preparedStatement.executeQuery();
        this.setResultSet(rs);
        return rs;
    }

    /**
     * 链式前置处理
     *
     * @param plugins
     * @param context
     */
    public void notifyPluginsPreExecuteSql() {
        for (LightRailPlugin plugin : plugins) {
            plugin.preExecuteSql(this);
        }
    }

    /**
     * 调用所有开始阶段
     *
     * @param plugins
     */
    public void notifyPluginsBegin() {
        for (LightRailPlugin plugin : plugins) {
            plugin.begin();
        }
    }


    /**
     * 执行查询, 插件处理结果集
     */
    public void notifyPluginsPostExecuteSql() throws SQLException {
        for (LightRailPlugin plugin : plugins) {
            plugin.postExecuteSql(this);
        }
    }

    public void notifyPluginsFinish() {
        for (LightRailPlugin plugin : plugins) {
            plugin.finish();
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
