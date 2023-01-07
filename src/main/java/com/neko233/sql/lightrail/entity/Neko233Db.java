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
public class Neko233Db {

    @Column("id")
    public Long id;
    @Column("group_name")
    public String groupName;
    @Column("db_id")
    public Integer dbId;
    @Column("tag")
    public String tag;
    @Column("add_dt")
    public LocalDateTime addDt;

}
