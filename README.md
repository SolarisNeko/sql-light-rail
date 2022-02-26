# SQL Light Rail

## 简介
> 这是一款基于 Flux / Stream / Chain / Builder 概念的 Java SQL 处理工具。让你写 SQL 如同写 Java Stream ~

搭配 IDE 的提示，你能几乎提速 200%+ 的编写 SQL 速度，并且能够借助 SQL Light Rail 避免大量的低级语法错误。

同时，将 SQL 难以编写的复杂语法，转移到 Java 层面处理，实在是非常的爽！

## Download URL
https://github.com/SolarisNeko/sql-light-rail/tags


## 初衷 / 痛点
1. 大部分公司很多 SQL 还是 SQL String, MyBatis 迁移的工作量巨大，实际提升并不明显。
2. MyBatis 并没有足够好到让我替换他，入侵性过强，较为重量。
3. MyBatis 不太兼容很多公司自研框架(例如：分库分表框架).
4. 没有语法提示。工作中使用到的 SQL 非常多, 但语法也各不一样，如: MySQL, Oracle, Spark SQL, Hive SQL, Kylin SQL...
5. 

## 发展状态

> Base function done ! 

基础功能已全部完成 ！

Aha~ We're glad to you use our Architecture to avoid Write SQL in XML / String.

我们很高兴你可以使用我们的 SQL Light Rail 去避免大量的将 SQL String 迁移到 XML / 保持原来的 SQL String 拼接。

> Advanced develop...

高级功能1 : 主要打造 LightRail Platform，将
1. SQL 生成
2. SQL 执行
3. ResultSet 结果映射

融为一体。

高级功能2 : 简单适配 BigData 领域的特殊 SQL
1. Hive 的 Delimiter By ... Row Formatter .... 等 SQL 语法。

> Special Function

yml config 


# 代码 Demo
Select
```java
String selectSql = SqlLightRail.selectTable("user a")
        .select("a.id", "a.name")
        .join(JoinCondition.builder()
                .join("user_role b")
                .on("a.id = b.id")
        )
        .build();
String target = "SELECT a.id, a.name FROM user a JOIN user_role b ON a.id = b.id ";
Assert.assertEquals(target, selectSql);
```

应对复杂业务的情况
```java
// 1 Base SQL
// if Web send you some data
String name = "root";

// 新手不要学, 某些公司会存在这种莫名其妙的判断逻辑, 分在不同的 SQL 里, 但是如果在 Java 里汇总起来, 也总比看 2 个莫名其妙的 SQL 方便
SelectSqlBuilder sqlBuilder = SqlLightRail.selectTable(User.class);
if ("root".equals(name)) {
    sqlBuilder.where(WhereCondition.builder()
        .equalsTo("deleted", "0")
        .lessThanOrEquals("create_time", new Date())
    );
}
if ("sb".equals(name)) {
    sqlBuilder.where(WhereCondition.builder()
        .equalsTo("name", "root")
        .equalsTo("1", "1")
    );
}

String selectSql = sqlBuilder.build();
String target = "SELECT id, name FROM user WHERE deleted = '0' and create_time <= '2022-02-27 12:04:58'";
Assert.assertEquals(target, selectSql);
```

# 完成进度
## 1、基本进度
1. [x] 基本 CRUD
2. [ ] Select 的字段别名, 希望加入 alias Map 支持自定义映射规则

## 2、细进度
### 1、Insert
Base Function
1. [x] insert into ...
2. [x] table(col1, col2)
3. [x] values auto generate by valueList
4. [x] values by "SQL String"

Advanced Function
1. [x] ORM generate Insert。 
| 通过 object 自动映射生成 Insert 语句。
2. [x] auto generate by pojo ORM to (?, ?) any times. 
| 自动生成占位符模板任意次。 

### 2. Select
基本功能 :
1. [x] select
2. [x] alias Map
3. [x] from
4. [x] where
5. [x] order by
6. [x] group by
7. [x] limit
8. [x] limit by pageNum, pageSize
9. [x] inner Select 
10. [x] join ... on ... 

进阶功能
1. ORM 映射查询, 直接查询全部 field

高级功能:
1. ORM 查询, 默认查询所有字段
```java
// User 对象只有 id, name 两个 fields
String selectAllSql = SqlLightRail.selectTable(User.class).build();
String target = "SELECT id, name FROM user ";
```
2. 


### 3、Update
1. [x] Update table
2. [x] set col1 = ?, col2 = ?
3. [x] Where col3 = ? and col4 = ?

### 4、Delete
1. [x] Delete From table
2. [x] where ...

# LightRailPlatform - Java DAO 统一操作平台
LightRailPlatform 由 4 部分组合而成:
1. SqlLightRail : 这是一款轻量级的 Java SQL 编写工具。主要处理各类 Java 缘分
2. JDBC : 不解释。
3. LightRailPlugin : 执行 SQL 生命周期的插件。不清楚可以见 SlowSqlPlugin (慢查询统计).
4. LightRailOrm : 对象映射

## 入口
LightRailPlatformFactory

你需要自己准备 DataSource (数据源 / 数据库连接池) 给本框架。




