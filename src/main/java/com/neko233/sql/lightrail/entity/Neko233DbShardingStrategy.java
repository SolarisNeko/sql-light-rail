package com.neko233.sql.lightrail.entity;

import com.neko233.sql.lightrail.annotation.Column;
import com.neko233.sql.lightrail.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author SolarisNeko
 * Date on 2023-01-07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("neko233_group_config_template")
public class Neko233DbShardingStrategy {

    @Column("group_name")
    public String groupName;
    @Column(value = "sharding_strategy_name", comment = "database 分库策略")
    public String shardingStrategyName;
    @Column(value = "is_use_default", comment = "是否使用默认的")
    public Boolean isUseDefault;
    @Column("add_dt")
    public LocalDateTime addDt;

}
