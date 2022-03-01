package com.neko233.lightrail.pojo;

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

    private Integer id;

    private String name;

}
