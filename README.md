# SQL Light Rail

## 发展状态
目前 Light Rail 还处于 Cycle 等级。

发展等级：
Material -> Cycle -> Car -> Subway -> LightRail

# 完成进度
## 1、基本进度
1. [x] 基本 CRUD
2. [ ] Select 的字段别名, 希望加入 alias Map 支持自定义映射规则

## 2、细进度

### 1、Insert
11. [x] insert into ...
12. [x] table(col1, col2)
13. [x] values auto generate by valueList
14. [x] values by "SQL String"

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




