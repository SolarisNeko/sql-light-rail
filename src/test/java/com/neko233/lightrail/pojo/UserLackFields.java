package com.neko233.lightrail.pojo;

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

    private String name;

    private Date createTime;

}

