package com.raaivan.util.rvsettings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.UUID;

@Component
@ApplicationScope
public class RVSettingsUsers {
    private RVSettingsUtil util;
    private RVSettingsPasswordPolicy PasswordPolicy;

    @Autowired
    public void _setDependencies(RVSettingsUtil util, RVSettingsPasswordPolicy passwordPolicy) {
        if(this.util == null) this.util = util;
        if(this.PasswordPolicy == null) this.PasswordPolicy = passwordPolicy;
    }

    public RVSettingsPasswordPolicy getPasswordPolicy() {
        return PasswordPolicy;
    }

    public boolean RestrictPasswordsToActiveDirectory(UUID applicationId)
    {
        return util.getValue(applicationId, RVSettingsItem.RestrictPasswordsToActiveDirectory).toLowerCase().equals("true");
    }

    public boolean EnableTwoStepAuthenticationViaEmail(UUID applicationId)
    {
        return util.getValue(applicationId, RVSettingsItem.EnableTwoStepAuthenticationViaEmail).toLowerCase().equals("true");
    }

    public boolean EnableTwoStepAuthenticationViaSMS(UUID applicationId)
    {
        return util.getValue(applicationId, RVSettingsItem.EnableTwoStepAuthenticationViaSMS).toLowerCase().equals("true");
    }

    public int TwoStepAuthenticationTimeout(UUID applicationId)
    {
        return util.getInt(applicationId, RVSettingsItem.TwoStepAuthenticationTimeoutInSeconds, 60, 60);
    }

    public boolean ForceChangeFirstPassword(UUID applicationId)
    {
        return util.getValue(applicationId, RVSettingsItem.ForceChangeFirstPassword).toLowerCase().equals("true");
    }

    public int PasswordLifeTimeInDays(UUID applicationId)
    {
        return util.getInt(applicationId, RVSettingsItem.PasswordLifeTimeInDays, 0, 0);
    }

    public int NotAvailablePreviousPasswordsCount(UUID applicationId)
    {
        return util.getInt(applicationId, RVSettingsItem.NotAvailablePreviousPasswordsCount, 0, 0);
    }

    public String UserNamePattern(UUID applicationId)
    {
        return util.getValue(applicationId, RVSettingsItem.UserNamePattern).trim();
    }
}
