package com.neko233.lightrail.pojo;

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

    private Integer id;
    private Integer userSum;
    private Integer roleSum;
    private Integer deviceSum;
    private Integer loginSum;

}
