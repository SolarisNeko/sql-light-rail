package com.neko233.sql.lightrail.db;

import com.neko233.sql.lightrail.SqlLightRail;
import com.neko233.sql.lightrail.builder.InsertSqlBuilder;
import com.neko233.sql.lightrail.condition.single.SetCondition;
import com.neko233.sql.lightrail.condition.single.WhereCondition;
import com.neko233.sql.lightrail.datasource.MyDataSource;
import com.neko233.sql.lightrail.pojo.User;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @title:
 * @description:
 * @author: SolarisNeko
 * Date on: 2/26/2022
 */
public class UpdateDbTest {

    Db db = new Db(MyDataSource.getDefaultDataSource());

    public UpdateDbTest() throws Exception {
    }


    @Test
    public void baseTest_selectByAutoGenerate() throws SQLException {
        String sql = SqlLightRail.selectTable(User.class).build();
        List<User> users = db
                .executeQuery(sql, User.class);
        Assert.assertTrue(users.size() > 0);
    }

    @Test
    public void baseTest_insert2User() throws Exception {

        InsertSqlBuilder builder = SqlLightRail.insertTable("user")
                .columnNames("name")
                .values("('demo10'), ('demo11') ");
        String build = builder.build();
        Integer rowCount = db
                .executeUpdate(build);

        Assert.assertTrue(2 == rowCount);
    }

    @Test
    public void baseTest_insert2User_ORM() throws Exception {

        List<User> valueList = new ArrayList<User>() {{
            add(User.builder().name("demo21").build());
            add(User.builder().name("demo22").build());
        }};

        String insertSql = SqlLightRail.insertTable("user")
                .columnNames("name")
                .values(valueList)
                .build();
        Integer rowCount = db
                .executeUpdate(insertSql);

        Assert.assertTrue(2 == rowCount);
    }

    @Test
    public void baseTest_update2User() throws Exception {

        String updateSql = SqlLightRail.updateTable("user")
                .set("create_time = '2022-01-01 11:11:11'")
                .where(WhereCondition.builder()
                        .equalsTo("id", 1)
                ).build();
        Integer rowCount = db
                .executeUpdate(updateSql);

        Assert.assertTrue(1 == rowCount);
    }

    @Test
    public void baseTest_update2User_SetDate() throws Exception {

        String updateSql = SqlLightRail.updateTable("user")
                .set(SetCondition.builder().equalsTo("create_time", new Date()))
                .where(WhereCondition.builder()
                        .equalsTo("id", 1)
                )
                .build();
        Integer rowCount = db
                .executeUpdate(updateSql);

        Assert.assertTrue(1 == rowCount);
    }

    @Test
    public void updateMultiSql_2_successfully() throws Exception {

        String build = SqlLightRail.updateTable("user")
                .set(SetCondition.builder().equalsTo("create_time", new Date()))
                .where(WhereCondition.builder()
                        .equalsTo("id", 1)
                ).build();
        String build2 = SqlLightRail.updateTable("user")
                .set(SetCondition.builder().equalsTo("create_time", new Date()))
                .where(WhereCondition.builder()
                        .equalsTo("id", 1)
                ).build();

        Integer rowCount = db
                .executeUpdate(Arrays.asList(build, build2));

        Assert.assertTrue(2 == rowCount);
    }


}
