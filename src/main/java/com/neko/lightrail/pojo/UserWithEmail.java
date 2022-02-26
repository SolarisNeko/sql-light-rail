package com.neko.lightrail.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SolarisNeko
 * @date 2022-02-26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserWithEmail {

    private Integer id;

    private String name;

    private String email;

}

