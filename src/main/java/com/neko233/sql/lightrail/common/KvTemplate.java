package com.neko233.sql.lightrail.common;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Key-Value Template = ${xxx} will replace to field 'xxx' value
 *
 * @author SolarisNeko on 2022-10-24
 **/
public class KvTemplate {

    private final String template;
    private final Map<String, String> originalValueKv = new HashMap<>(2, 0.9f);

    public KvTemplate(String template) {
        this.template = template;
    }


    public static KvTemplate builder(String sqlTemplate) {
        if (StringUtils.isBlank(sqlTemplate)) {
            throw new RuntimeException("your kv template is blank !");
        }
        return new KvTemplate(sqlTemplate);
    }


    public KvTemplate merge(String key, String value, String union) {
        originalValueKv.merge(key, value, (v1, v2) -> v1 + union  + v2);
        return this;
    }

    public KvTemplate put(String key, String value) {
        originalValueKv.put(key, value);
        return this;
    }

    public KvTemplate put(Map<String, String> kv) {
        if (kv == null) {
            return this;
        }
        kv.forEach(this::put);
        return this;
    }

    public String build() {
        return this.toString();
    }

    @Override
    public String toString() {
        String tempTemplate = template;
        // replace all ${key}
        for (Map.Entry<String, String> originalKv : originalValueKv.entrySet()) {
            String value = String.valueOf(originalKv.getValue());

            tempTemplate = tempTemplate.replaceAll(
                    "\\$\\{" + originalKv.getKey() + "\\}",
                    value
            );
        }
        return tempTemplate;
    }
}
