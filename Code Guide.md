# Code Guide For `SQL Light Rail`
## Dependency
maven
```xml
<!-- ORM -->
<dependency>
    <groupId>com.neko233</groupId>
    <artifactId>sql-light-rail</artifactId>
    <version>0.2.2</version>
</dependency>
<!-- DataSource -->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid</artifactId>
    <version>1.2.8</version>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.25</version>
</dependency>
```

# 组装 SQL 入口 - class SqlLightRail
动态组装 SQL


# DAO 统一操作层 - class RailPlatform 
## 多数据源演示
需要 2 个依赖, DataSource + Connector.
```xml

```
## Demo Code
```java

public static DataSource configDbDataSource() throws Exception {
        Properties properties = new Properties();
        properties.put(PROP_URL, "jdbc:mysql://localhost:3306/sql_light_rail");
        properties.put(PROP_USERNAME, "root");
        properties.put(PROP_PASSWORD, "root");
        properties.put(PROP_INITIALSIZE, "5");
        properties.put(PROP_MINIDLE, "5");
        properties.put(PROP_MAXACTIVE, "10");
        properties.put(PROP_MAXWAIT, "10000");
        return createDataSource(properties);
        }

/**
 * How to use multi DataSource
 *
 * @throws Exception 异常
 */
@Test
public void testInit() throws Exception {

        // auto init config | see 'DDL-sharding-sql_light_rail_config.sql'
        Db configDb = new Db(configDbDataSource());
        new RepositoryManagerInitializerByMysql().initDbGroup(configDb, "template");

        // use 
        Db db = RepositoryManager.instance
        .getDbGroup("template")
        .getDb(0L);

        System.out.println(db.getDbName());

        // check
        Integer number1 = db.executeQuerySingle("select 1 from dual", Integer.class);
        System.out.println(number1);
        }


```