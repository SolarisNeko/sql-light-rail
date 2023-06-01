package com.neko233.sql.lightrail.entity;

import com.neko233.sql.lightrail.annotation.Column;
import com.neko233.sql.lightrail.annotation.IgnoreColumn;
import com.neko233.sql.lightrail.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author SolarisNeko
 * Date on 2023-01-07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("neko233_group_config_template")
public class Neko233ConfigTagKv {

    @Column("id")
    private Long id;
    @Column("tag")
    private String tag;
    @Column("config_key")
    private String config_key;
    @Column("config_value")
    private String config_value;
    @Column("add_dt")
    private LocalDateTime addDt;

    @IgnoreColumn
    private boolean testIgnoreUse;

    public static Map<String, String> translateToKvMap(Collection<Neko233ConfigTagKv> kvList) {
        Map<String, String> kvMap = new HashMap<>();
        for (Neko233ConfigTagKv neko233ConfigTagKv : kvList) {
            kvMap.put(neko233ConfigTagKv.config_key, neko233ConfigTagKv.config_value);
        }
        return kvMap;
    }
}
