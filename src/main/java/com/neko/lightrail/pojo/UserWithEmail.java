package com.neko.lightrail.pojo;

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

    private Integer id;

    private String name;

    private String email;

    private Date createTime;

}

