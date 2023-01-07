package com.neko233.sql.lightrail.demo;

import com.neko233.sql.lightrail.SqlLightRail;
import com.neko233.sql.lightrail.builder.InsertSqlBuilder;
import com.neko233.sql.lightrail.condition.single.WhereCondition;
import com.neko233.sql.lightrail.pojo.LoginSumDaily;
import com.neko233.sql.lightrail.pojo.User;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author SolarisNeko
 * Date on 2022-02-24
 */
public class WithJdbcDemo {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        getMetadata();

//       selectUserDemo();
//        insertUserDemo();
        insertUserDemo2();

    }

    private static void getMetadata() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sql_light_rail?", "root", "root");
        DatabaseMetaData metaData = connection.getMetaData();

        String build = SqlLightRail.selectTable("information_schema", "COLUMNS")
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

    private static void insertUserDemo2() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sql_light_rail?", "root", "root");
        DatabaseMetaData metaData = connection.getMetaData();

        ArrayList<LoginSumDaily> dataList = new ArrayList<LoginSumDaily>() {{
            add(LoginSumDaily.builder().userSum(10).roleSum(10).deviceSum(10).build());
            add(LoginSumDaily.builder().userSum(20).roleSum(20).deviceSum(20).build());
            add(LoginSumDaily.builder().userSum(30).roleSum(30).deviceSum(30).build());
        }};

        InsertSqlBuilder builder = SqlLightRail.insertTable("login_sum_daily")
            .values(dataList);


        String sql = builder.build();
        System.out.println(sql);
        PreparedStatement ps = connection.prepareStatement(sql);
        int i = ps.executeUpdate();

        System.out.println("Update " + i + " rows.");
    }

    private static void insertUserDemo() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sql_light_rail?", "root", "root");
        DatabaseMetaData metaData = connection.getMetaData();

        ArrayList<User> users = new ArrayList<User>() {{
            add(User.builder().name("insert-1").build());
            add(User.builder().name("insert-2").build());
            add(User.builder().name("insert-3").build());
        }};

        InsertSqlBuilder builder = SqlLightRail.insertTable("user")
            .values(users);


        String sql = builder.build();
        System.out.println(sql);
        PreparedStatement ps = connection.prepareStatement(sql);
        int i = ps.executeUpdate();

        System.out.println("Update " + i + " rows.");
    }

    private static void selectUserDemo() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sql_light_rail?", "root", "root");
        DatabaseMetaData metaData = connection.getMetaData();

        String build = SqlLightRail.selectTable("user")
            .select("id", "name")
            .where(WhereCondition.builder()
                .equalsTo("name", "neko")
            )
            .build();

        PreparedStatement ps = connection.prepareStatement(build);
        ResultSet resultSet = ps.executeQuery();

        while (resultSet.next()) {
            String id = resultSet.getString(1);
            String name = resultSet.getString(2);
            System.out.println(id + " : " + name);
        }
    }
}
