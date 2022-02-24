package com.neko.lightrail.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SolarisNeko
 * @date 2022-02-24
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
