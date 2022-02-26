package com.neko.lightrail;

import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;

/**
 * @author SolarisNeko
 * @date 2022-02-26
 */
@Slf4j
public class LightRailPlatformFactory {

    public static LightRailPlatform createLightRailPlatform(DataSource dataSource) {
        return LightRailPlatform.createLightRailPlatform(dataSource);
    }

}
