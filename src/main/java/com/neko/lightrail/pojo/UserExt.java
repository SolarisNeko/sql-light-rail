package com.neko.lightrail.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author SolarisNeko
 * @date 2022-02-20
 */
@Data
public class UserExt extends User {

    private Integer age;

    public UserExt(Integer id, String name, Integer age) {
        super(id, name);
        this.age = age;
    }

}
