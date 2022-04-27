package com.neko233.lightrail.plugin;

import com.neko233.lightrail.domain.ExecuteSqlContext;

/**
 * @author SolarisNeko
 * Date on 2022-02-26
 */
public abstract class Plugin {

    private String pluginName;

    public Plugin(String pluginName) {
        this.pluginName = pluginName;
    }

    public String getPluginName() {
        return pluginName;
    }

    public abstract void initPlugin();

    public void begin() {

    }

    public void preExecuteSql(ExecuteSqlContext context) {

    }

    public void postExecuteSql(ExecuteSqlContext context) {

    }

    public void end() {

    }


}
