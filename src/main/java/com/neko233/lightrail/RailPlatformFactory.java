package com.neko233.lightrail;

import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;

/**
 * @author SolarisNeko
 * Date on 2022-02-26
 */
@Slf4j
public class RailPlatformFactory {

    public static RailPlatform createLightRailPlatform(DataSource dataSource) {
        return RailPlatform.createLightRailPlatform(dataSource);
    }

}
