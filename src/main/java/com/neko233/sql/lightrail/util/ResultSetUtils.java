package com.neko233.sql.lightrail.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultSetUtils {

    /**
     * 获取 ResultSet 中的所有数据，并以列表形式返回
     *
     * @param resultSet ResultSet 对象
     * @return 包含所有行数据的列表，每行数据以键值对的形式存储在一个 Map 中
     * @throws SQLException 如果读取 ResultSet 出现错误
     */
    public static List<Map<String, Object>> getAllData(ResultSet resultSet) throws SQLException {
        List<Map<String, Object>> resultList = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (resultSet.next()) {
            Map<String, Object> rowMap = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                Object columnValue = resultSet.getObject(i);
                rowMap.put(columnName, columnValue);
            }
            resultList.add(rowMap);
        }

        return resultList;
    }

    /**
     * 打印 ResultSet 中的所有数据
     *
     * @param resultSet ResultSet 对象
     * @throws SQLException 如果读取 ResultSet 出现错误
     */
    public static void printResultSet(ResultSet resultSet) throws SQLException {
        List<Map<String, Object>> dataList = getAllData(resultSet);
        for (Map<String, Object> data : dataList) {
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                System.out.print(entry.getKey() + ": " + entry.getValue() + "\t");
            }
            System.out.println();
        }
    }
}
