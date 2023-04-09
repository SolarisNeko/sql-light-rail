package com.neko233.sql.lightrail.sql_builder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author SolarisNeko
 * Date on 2022-02-20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SqlContext {

    private List<String> select = new ArrayList<>();
    private List<String> tableList = new ArrayList<>();

    // select
    private String join = null;
    private Map<String, String> aliasMap = new HashMap<>();

    // Select, Update 使用
    private String where = null;
    private String orderBy = null;
    private String groupBy = null;
    private String limit = null;

    // Update
    private String set = null;

    // Insert
    private List<String> columnNameList = new ArrayList<>();
    private List<String> columnValueList = new ArrayList<>();

}
