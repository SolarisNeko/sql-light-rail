package com.neko233.lightrail.domain;

import com.neko233.lightrail.plugin.Plugin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 执行一次 SQL 的装配对象
 *
 * @author SolarisNeko
 * Date on 2022-03-06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SqlStatement {

    String shardingKey;

    String sql;

    List<String> sqlList;

    Class<?> returnType;

    Boolean isAutoCommit;

    List<Plugin> addTempPlugins;

    List<String> excludePluginNames;

}
