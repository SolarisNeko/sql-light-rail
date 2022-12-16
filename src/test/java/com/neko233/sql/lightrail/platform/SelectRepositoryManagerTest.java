package com.neko233.sql.lightrail.platform;

import com.neko233.sql.lightrail.RepositoryManager;
import com.neko233.sql.lightrail.RepositoryManagerFactory;
import com.neko233.sql.lightrail.dataSource.MyDataSource;
import com.neko233.sql.lightrail.pojo.User;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author SolarisNeko
 * @date 4/23/2022
 */
public class SelectRepositoryManagerTest {


    @Test
    public void selectTest_IntegerOrm() throws Exception {
        RepositoryManager repositoryManager = RepositoryManagerFactory.create(MyDataSource.getDefaultDataSource());

        List<Integer> testIntValue = repositoryManager.executeQuery("Select 1 From dual ", Integer.class);

        Assert.assertEquals(1, testIntValue.get(0).intValue());
    }

    @Test
    public void selectTest_ObjectOrm() throws Exception {
        RepositoryManager repositoryManager = RepositoryManagerFactory.create(MyDataSource.getDefaultDataSource());

        List<User> users = repositoryManager.executeQuery("Select id, name From user Limit 0, 1 ", User.class);

        Assert.assertTrue(users != null);
    }

}
