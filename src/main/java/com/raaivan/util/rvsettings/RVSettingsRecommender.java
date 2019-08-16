package com.raaivan.util.rvsettings;

import com.raaivan.util.RaaiVanConfig;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.UUID;

@Component
@ApplicationScope
public class RVSettingsRecommender {
    private RVSettingsUtil util;

    @Autowired
    public void _setDependencies(RVSettingsUtil util) {
        if(this.util == null) this.util = util;
    }

    public boolean Enabled(UUID applicationId)
    {
        return RaaiVanConfig.Modules.Recommender && !StringUtils.isBlank(URL(applicationId)) &&
                !StringUtils.isBlank(Username(applicationId)) && !StringUtils.isBlank(Password(applicationId)) &&
                !util.getValue(applicationId, RVSettingsItem.Recommender).toLowerCase().equals("false");
    }

    public String URL(UUID applicationId)
    {
        return util.getValue(applicationId, RVSettingsItem.RecommenderURL);
    }

    public String Username(UUID applicationId)
    {
        return util.getValue(applicationId, RVSettingsItem.RecommenderUsername);
    }

    public String Password(UUID applicationId)
    {
        return util.getValue(applicationId, RVSettingsItem.RecommenderPassword);
    }
}
