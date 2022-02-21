package com.neko.lightrail.builder;

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
 * @date 2022-02-20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sql {

    List<String> select = new ArrayList<>();
    List<String> tableList = new ArrayList<>();

    // select
    String join = null;
    Map<String, String> aliasMap = new HashMap<>();

    // Select, Update 使用
    String where = null;
    String orderBy = null;
    String groupBy = null;
    String limit = null;

    // Update
    String set = null;

    // Insert
    List<String> columns = new ArrayList<>();
    List<String> values = new ArrayList<>();

}
