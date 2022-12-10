package com.neko233.sql.lightrail.pojo;

import com.neko233.sql.lightrail.annotation.Column;
import lombok.Data;

/**
 * @author SolarisNeko
 * Date on 2022-02-20
 */
@Data
public class UserExt extends User {

    @Column("age")
    private Integer age;

    public UserExt(Integer id, String name, Integer age) {
        super(id, name);
        this.age = age;
    }

}
