package com.neko233.sql.lightrail;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author SolarisNeko
 * Date on 2022-12-16
 */
public class RepositoryGroupManager {

    /**
     * groupName : shardingKey...
     */
    private static final ConcurrentMap<String, List<String>> groupMap = new ConcurrentHashMap<>();

    public static synchronized void addShardingKeyGroup(String groupName, String shardingKey) {
        List<String> shardingKeyList = new ArrayList<>();
        shardingKeyList.add(shardingKey);
        groupMap.merge(groupName, shardingKeyList, (v1, v2) -> {
            v1.addAll(v2);
            return v1;
        });
    }

    public static synchronized void removeGroup(String groupName) {
        groupMap.remove(groupName);
    }

    public static synchronized void removeShardingKeyGroup(String groupName, String shardingKey) {
        List<String> shardingKeyList = groupMap.get(groupName);
        shardingKeyList.remove(shardingKey);
        groupMap.put(groupName, shardingKeyList);
    }

    public static List<String> getShardingKeyList(String groupName) {
        return groupMap.getOrDefault(groupName, new ArrayList<>());
    }


}
