package com.neko233.sql.lightrail.entity;

import com.neko233.sql.lightrail.annotation.Column;
import com.neko233.sql.lightrail.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author SolarisNeko
 * Date on 2023-01-07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("neko233_group_config_template")
public class Neko233GroupConfigTemplate {

    @Column("id")
    private Long id;
    @Column("group_name")
    private String groupName;
    @Column("template_key")
    private String templateKey;
    @Column("template_value")
    private String templateValue;
    @Column("add_dt")
    private LocalDateTime addDt;

    /**
     * 将查询出来的 DAO 结果 ->  groupName : Properties
     * @param neko233GroupConfigTemplates templates
     * @return Map
     */
    public static Map<String, Properties> getGroupName2Properties(List<Neko233GroupConfigTemplate> neko233GroupConfigTemplates) {
        HashMap<String, Properties> map = new HashMap<>();
        for (Neko233GroupConfigTemplate neko233GroupConfigTemplate : neko233GroupConfigTemplates) {
            Properties properties = map.get(neko233GroupConfigTemplate.groupName);
            if (properties == null) {
                Properties prop = new Properties();
                prop.setProperty(neko233GroupConfigTemplate.templateKey, neko233GroupConfigTemplate.templateValue);
                map.put(neko233GroupConfigTemplate.groupName, prop);
                continue;
            }
            properties.setProperty(neko233GroupConfigTemplate.templateKey, neko233GroupConfigTemplate.templateValue);
        }
        return map;
    }
}
