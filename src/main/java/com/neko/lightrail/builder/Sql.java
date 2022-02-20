package com.neko.lightrail.builder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SolarisNeko
 * @date 2022-02-20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sql {

    String select = null;
    String table = null;

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
