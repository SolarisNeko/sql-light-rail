package com.neko233.sql.lightrail.plugin;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author SolarisNeko
 * Date on 2023-01-07
 */
public class PluginRegistry {

    private static final Map<String, Plugin> GLOBAL_PLUGINS = new ConcurrentHashMap<>();

    public static void addGlobalPlugin(Plugin plugin) {
        GLOBAL_PLUGINS.put(plugin.getPluginName(), plugin);
    }

    public static void removeGlobalPlugin(Plugin plugin) {
        GLOBAL_PLUGINS.remove(plugin.getPluginName());
    }

    public static Collection<Plugin> getAll() {
        return GLOBAL_PLUGINS.values();
    }

    public static void removeAllPlugins() {
        GLOBAL_PLUGINS.clear();
    }

}
