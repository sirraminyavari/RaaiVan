package com.raaivan.util.rvsettings;

import com.raaivan.web.config.RVCustomProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.List;

@Component
@ApplicationScope
public class RVSettingsJobs {
    private RVSettingsUtil util;
    private RVCustomProperties rvConfig;

    @Autowired
    public void _setDependencies(RVSettingsUtil util, RVCustomProperties rvCustomProperties) {
        if(this.util == null) this.util = util;
        if(this.rvConfig == null) this.rvConfig = rvCustomProperties;
    }

    public List<String> JobsList()
    {
        return rvConfig.getValues(RVSettingsItem.Job);
    }
}