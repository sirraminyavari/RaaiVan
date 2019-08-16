package com.raaivan.util.rvsettings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.UUID;

@Component
@ApplicationScope
public class RVSettingsSensitivePages {
    private RVSettingsUtil util;
    private RVSettingsSSO SSO;

    @Autowired
    public void _setDependencies(RVSettingsUtil util, RVSettingsSSO sso) {
        if(this.util == null) this.util = util;
        if(this.SSO == null) this.SSO = sso;
    }

    public boolean SettingsAdmin(UUID applicationId)
    {
        return !SSO.Enabled(applicationId) &&
                util.getValue(applicationId, RVSettingsItem.ReauthenticationForSettingsAdminPage).toLowerCase().equals("true");
    }

    public boolean UsersAdmin(UUID applicationId)
    {
        return !SSO.Enabled(applicationId) &&
                util.getValue(applicationId, RVSettingsItem.ReauthenticationForUsersAdminPage).toLowerCase().equals("true");
    }
}
