package com.neko.lightrail.plugin;

import com.neko.lightrail.domain.ExecuteSqlContext;

/**
 * @author SolarisNeko
 * @date 2022-02-26
 */
public abstract class Plugin {

    private String pluginName;

    public Plugin(String pluginName) {
        this.pluginName = pluginName;
    }

    /**
     * 初始化 plugin 阶段
     */
    public abstract void initPlugin();

    /**
     * 开始阶段
     */
    public void begin() {

    }

    /**
     * 执行前
     * @param context
     */
    public void preExecuteSql(ExecuteSqlContext context) {

    }


    /**
     * 执行后
     * @param context
     */
    public void postExecuteSql(ExecuteSqlContext context) {

    }

    /**
     * 完成时
     */
    public void end() {

    }


}
