package com.neko233.sql.lightrail.pojo;

import com.neko233.sql.lightrail.annotation.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author SolarisNeko
 * Date on 2022-02-26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLackFields {

    @Column("name")
    private String name;

    @Column(isUse = false, value = "")
    private Date createTime;

}

