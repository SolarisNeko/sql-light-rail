package com.neko233.sql.lightrail.pojo;

import com.neko233.sql.lightrail.annotation.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SolarisNeko
 * Date on 2022-02-20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Column("id")
    private int id;

    @Column("name")
    private String name;

}
