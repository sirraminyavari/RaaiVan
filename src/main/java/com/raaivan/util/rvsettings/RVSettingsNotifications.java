package com.raaivan.util.rvsettings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.UUID;

@Component
@ApplicationScope
public class RVSettingsNotifications {
    private RVSettingsUtil util;

    @Autowired
    public void _setDependencies(RVSettingsUtil util) {
        if(this.util == null) this.util = util;
    }

    public int SeenTimeout(UUID applicationId)
    {
        return util.getInt(applicationId, RVSettingsItem.NotificationsSeenTimeout, 5000, 2000);
    }

    public int UpdateInterval(UUID applicationId)
    {
        return util.getInt(applicationId, RVSettingsItem.NotificationsUpdateInterval, 30000, 5000);
    }
}
