package com.neko233.sql.lightrail.db;

import com.neko233.sql.lightrail.datasource.MyDataSource;
import com.neko233.sql.lightrail.pojo.User;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author SolarisNeko
 * @date 4/23/2022
 */
public class SelectDbTest {

    Db db = new Db(MyDataSource.getDefaultDataSource());

    public SelectDbTest() throws Exception {
    }


    @Test
    public void selectTest_IntegerOrm() throws Exception {

        List<Integer> testIntValue = db
                .executeQuery("Select 1 From dual ", Integer.class);

        Assert.assertEquals(1, testIntValue.get(0).intValue());
    }

    @Test
    public void selectTest_ObjectOrm() throws Exception {

        List<User> users = db.executeQuery("Select id, name From user Limit 0, 1 ", User.class);

        Assert.assertTrue(users.size() > 0);
    }

    @Test
    public void selectTest_paramsSet() throws Exception {
        Object[] params = {1};
        List<User> users = db
                .executeQuery("Select id, name From user Where id != ? Limit 0, 1 ", params, User.class);

        Assert.assertTrue(users.size() > 0);
    }

}
