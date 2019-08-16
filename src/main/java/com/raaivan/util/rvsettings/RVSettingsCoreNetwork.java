package com.raaivan.util.rvsettings;

import com.raaivan.modules.rv.beans.Expressions;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@ApplicationScope
public class RVSettingsCoreNetwork {
    private RVSettingsUtil util;

    @Autowired
    public void _setDependencies(RVSettingsUtil util) {
        if(this.util == null) this.util = util;
    }

    public List<String> DefaultCNExtensions(UUID applicationId) {
        String strExts = util.getValue(applicationId, RVSettingsItem.DefaultCNExtensions);
        return StringUtils.isBlank(strExts) ? new ArrayList<>() :
                Arrays.stream(strExts.trim().split("\\|")).collect(Collectors.toList());
    }

    public String DefaultAdditionalIDPattern(UUID applicationId){
        String val = util.getValue(applicationId, RVSettingsItem.DefaultAdditionalIDPattern);
        return StringUtils.isBlank(val) || !Expressions.isMatch(val, Expressions.Patterns.AdditionalID) ?
                "~[[PYear]]0~[[NCount]]0~[[NCountSPY]]" : val;
    }
}
