package com.raaivan.util.rvsettings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.UUID;

@Component
@ApplicationScope
public class RVSettingsKnowledge {
    private RVSettingsUtil util;

    @Autowired
    public void _setDependencies(RVSettingsUtil util) {
        if(this.util == null) this.util = util;
    }

    public int AlertExpirationInDays(UUID applicationId)
    {
        return util.getInt(applicationId, RVSettingsItem.AlertKnowledgeExpirationInDays, 10, null);
    }
}
