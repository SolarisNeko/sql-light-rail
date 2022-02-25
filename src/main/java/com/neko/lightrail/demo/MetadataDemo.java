package com.neko.lightrail.demo;

import com.neko.lightrail.SqlLightRail;
import com.neko.lightrail.condition.WhereCondition;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author SolarisNeko
 * @date 2022-02-24
 */
public class MetadataDemo {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sql_light_rail?", "root", "root");
        DatabaseMetaData metaData = connection.getMetaData();

        String build = SqlLightRail.selectBuilder("information_schema", "COLUMNS")
            .select("column_name, ordinal_position, column_type")
            .where(WhereCondition.builder()
                .equalsTo("table_name", "user")
                .equalsTo("TABLE_SCHEMA", "sql_light_rail")
            )
            .build();

        PreparedStatement ps = connection.prepareStatement(build);
        ResultSet resultSet = ps.executeQuery();

        while (resultSet.next()) {
            String c1 = resultSet.getString(1);
            String c2 = resultSet.getString(2);
            String c3 = resultSet.getString(3);
            System.out.println(c1 + " : " + c2 + " : " + c3);
        }
    }
}
