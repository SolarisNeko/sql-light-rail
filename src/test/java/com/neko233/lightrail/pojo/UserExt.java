package com.neko233.lightrail.pojo;

import lombok.Data;

/**
 * @author SolarisNeko
 * Date on 2022-02-20
 */
@Data
public class UserExt extends User {

    private Integer age;

    public UserExt(Integer id, String name, Integer age) {
        super(id, name);
        this.age = age;
    }

}
