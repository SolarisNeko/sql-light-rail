package com.neko233.sql.lightrail.pojo;

import com.neko233.sql.lightrail.annotation.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SolarisNeko
 * Date on 2022-02-24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginSumDaily {

    @Column("id")
    private Integer id;
    @Column("userSum")
    private Integer userSum;
    @Column("roleSum")
    private Integer roleSum;
    @Column("deviceSum")
    private Integer deviceSum;
    @Column("loginSum")
    private Integer loginSum;

}
