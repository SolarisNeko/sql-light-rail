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
public class UserWithEmail {

    @Column("id")
    private Integer id;

    @Column("name")
    private String name;

    @Column("email")
    private String email;

    @Column("createTime")
    private Date createTime;

}

