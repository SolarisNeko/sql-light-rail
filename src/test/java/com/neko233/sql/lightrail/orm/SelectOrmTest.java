package com.neko233.sql.lightrail.orm;

import com.neko233.sql.lightrail.SqlLightRail;
import com.neko233.sql.lightrail.datasource.MyDataSource;
import com.neko233.sql.lightrail.db.Db;
import com.neko233.sql.lightrail.plugin.PluginRegistry;
import com.neko233.sql.lightrail.plugin.SlowSqlPlugin;
import com.neko233.sql.lightrail.pojo.UserLackFields;
import com.neko233.sql.lightrail.pojo.UserWithEmail;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author SolarisNeko
 * Date on 2022-02-26
 */
public class SelectOrmTest {

    Db db = new Db(MyDataSource.getDefaultDataSource());

    public SelectOrmTest() throws Exception {
    }

    @Before
    public void before() throws Exception {
        PluginRegistry.removeAllPlugins();
        PluginRegistry.addGlobalPlugin(new SlowSqlPlugin());
    }

    @Test
    public void selectOrmTest_original_SQL_String() throws Exception {

        List<UserWithEmail> dataList = db.executeQuery(
                "select id, name From user Limit 0, 1",
                UserWithEmail.class
        );
        for (UserWithEmail user : dataList) {
            Assert.assertTrue(user.getName() != null);
        }
    }

    @Test
    public void selectOrmTest() throws Exception {

        String selectSql = SqlLightRail.selectTable("user", UserWithEmail.class)
                .limitByPage(1, 5)
                .build();
        List<UserWithEmail> dataList = db.executeQuery(
                selectSql,
                UserWithEmail.class
        );
        for (UserWithEmail user : dataList) {
            Assert.assertTrue(user.getName() != null);
        }
    }

    @Test
    public void selectOrmTest_lackSomeField_1() throws Exception {

        String selectSql = SqlLightRail.selectTable("user", UserLackFields.class)
                .limitByPage(1, 10)
                .build();
        List<UserLackFields> dataList = db.executeQuery(
                selectSql,
                UserLackFields.class
        );
        for (UserLackFields userLackFields : dataList) {
            Assert.assertTrue(userLackFields.getName() != null);
        }
    }

    /**
     * UserLackFields: name, create_time
     * Select SQL: id, name
     * Target:
     *
     * @throws Exception
     */
    @Test
    public void selectOrmTest_lackSomeField_2() throws Exception {

        String build = SqlLightRail.selectTable("user").select("id", "name")
                .limit(0, 5)
                .build();
        List<UserLackFields> dataList = db.executeQuery(
                build,
                UserLackFields.class
        );

        for (UserLackFields userLackFields : dataList) {
            Assert.assertEquals(null, userLackFields.getCreateTime());
        }
    }


}
