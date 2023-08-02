package com.neko233.sql.lightrail.db;

import com.neko233.sql.lightrail.shardingManager.DbGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author SolarisNeko
 * Date on 2023-01-07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DbConfig {

    private Integer dbId;
    private DbGroup dbGroup;
    private Map<String, String> dbConfigMap;

}
