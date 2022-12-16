package com.neko233.sql.lightrail;

import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;

/**
 * @author SolarisNeko
 * Date on 2022-02-26
 */
@Slf4j
public class RepositoryManagerFactory {

    public static RepositoryManager create(DataSource defaultDataSource) {
        if (RepositoryManager.getDataSourceSize() > 0) {
            log.debug("[RepositoryManager] You can't create default DataSource again.");
            return RepositoryManager.getInstance();
        }
        return RepositoryManager.getInstance()
                .addDataSource("default", defaultDataSource);
    }


}
