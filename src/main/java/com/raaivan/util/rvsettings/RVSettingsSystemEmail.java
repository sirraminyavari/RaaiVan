package com.raaivan.util.rvsettings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.UUID;

@Component
@ApplicationScope
public class RVSettingsSystemEmail {
    private RVSettingsUtil util;

    @Autowired
    public void _setDependencies(RVSettingsUtil util) {
        if(this.util == null) this.util = util;
    }

    public String Address(UUID applicationId)
    {
        return util.getValue(applicationId, RVSettingsItem.SystemEmailAddress);
    }

    public String DisplayName(UUID applicationId)
    {
        return util.getValue(applicationId, RVSettingsItem.SystemEmailDisplayName);
    }

    public String Password(UUID applicationId)
    {
        return util.getValue(applicationId, RVSettingsItem.SystemEmailPassword);
    }

    public String SMTPAddress(UUID applicationId)
    {
        return util.getValue(applicationId, RVSettingsItem.SystemEmailSMTPAddress);
    }

    public int SMTPPort(UUID applicationId)
    {
        return util.getInt(applicationId, RVSettingsItem.SystemEmailSMTPPort, 0, null);
    }

    public boolean UseSSL(UUID applicationId)
    {
        return util.getValue(applicationId, RVSettingsItem.SystemEmailUseSSL).toLowerCase().equals("true");
    }

    public int Timeout(UUID applicationId)
    {
        return util.getInt(applicationId, RVSettingsItem.SystemEmailTimeout, 0, null);
    }

    public String EmailSubject(UUID applicationId)
    {
        return util.getValue(applicationId, RVSettingsItem.SystemEmailSubject, "RaaiVan");
    }
}
