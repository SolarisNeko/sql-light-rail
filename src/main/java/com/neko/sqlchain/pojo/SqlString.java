package com.neko.sqlchain.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SolarisNeko
 * @date 2022-02-20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SqlString {

    String select = null;
    String columns = null;
    String table = null;
    // select, update 使用
    String where = null;
    String orderBy = null;
    String groupBy = null;
    String limit = null;

}
