package com.raaivan.web.config;

import com.raaivan.util.rvsettings.RVSettingsItem;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

@Component
@EnableConfigurationProperties
@ConfigurationProperties("rv.config")
public class RVCustomProperties {
    private String x;

    public String getValue(RVSettingsItem name){
        return "";
    }

    public List<String> getValues(RVSettingsItem keyNameStartWith){
        return new ArrayList<>();
    }

    public List<SimpleEntry<String, String>> getKeyValues(RVSettingsItem keyNameStartWith){
        return new ArrayList<>();
    }
}
