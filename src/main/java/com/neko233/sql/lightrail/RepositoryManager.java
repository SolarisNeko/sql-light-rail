package com.neko233.sql.lightrail;

import com.neko233.sql.lightrail.manager.DbGroup;
import com.neko233.sql.lightrail.manager.exception.NotFoundInDbManagerException;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @author SolarisNeko on 2022-12-07
 **/
@Slf4j
public class RepositoryManager {

    public static final String DEFAULT_GROUP_NAME = "resource";

    public static final RepositoryManager instance = new RepositoryManager();

    // state
    private final Map<String, DbGroup> dbGroupMap = new ConcurrentHashMap<>();

    public RepositoryManager initByYourself(Consumer<RepositoryManager> consumer) throws Exception {
        consumer.accept(this);
        return this;
    }


    public Map<String, DbGroup> getDbGroupMap() {
        return dbGroupMap;
    }

    public RepositoryManager removeDbGroup(String groupName) {
        DbGroup remove = dbGroupMap.remove(groupName);
        remove.destroy();
        return this;
    }

    public RepositoryManager addDbGroup(DbGroup dbGroup) throws Exception {
        return addDbGroup(Collections.singletonList(dbGroup));
    }
    public RepositoryManager addDbGroup(Collection<DbGroup> dbGroups) throws Exception {
        for (DbGroup dbGroup : dbGroups) {
            if (dbGroup.name == null) {
                throw new IllegalArgumentException("can not null groupName in dbGroup!");
            }
            dbGroupMap.put(dbGroup.name, dbGroup);
            dbGroup.create();
        }
        return this;
    }


    public DbGroup getDbGroup(String name) {
        DbGroup dbGroup = this.dbGroupMap.get(name);
        if (dbGroup == null) {
            throw new NotFoundInDbManagerException(name, null);
        }
        return dbGroup;
    }

    public Collection<DbGroup> getDbGroups() {
        return this.dbGroupMap.values();
    }


}
