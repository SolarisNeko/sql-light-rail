package com.neko.lightrail;

import com.neko.lightrail.orm.ORM;
import com.neko.lightrail.pojo.UserWithEmail;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author SolarisNeko
 * @date 2022-02-26
 */
public class SelectOrmTest {

    @Test
    public void selectOrmTest() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sql_light_rail?", "root", "root");

        String build = SqlLightRail.selectTable("user", UserWithEmail.class).build();

        // TODO 能不能再优化? 我不希望每次都要给个 ResultSet 给他
        ResultSet resultSet = connection.prepareStatement(build).executeQuery();
        List<UserWithEmail> test = ORM.convert(resultSet, UserWithEmail.class);
        System.out.println(test);
    }
}
