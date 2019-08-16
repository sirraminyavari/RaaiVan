package com.raaivan.util.rvsettings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.UUID;

@Component
@ApplicationScope
public class RVSettingsPasswordPolicy {
    private RVSettingsUtil util;

    @Autowired
    public void _setDependencies(RVSettingsUtil util) {
        if(this.util == null) this.util = util;
    }

    public int MinLength(UUID applicationId)
    {
        return util.getInt(applicationId, RVSettingsItem.PasswordPolicyMinLength, 8, null);
    }

    public int NewCharacters(UUID applicationId)
    {
        return util.getInt(applicationId, RVSettingsItem.PasswordPolicyNewCharacters, 1, 1);
    }

    public boolean UpperLower(UUID applicationId)
    {
        return !util.getValue(applicationId, RVSettingsItem.PasswordPolicyUpperLower).toLowerCase().equals("false");
    }

    public boolean NonAlphabetic(UUID applicationId)
    {
        return !util.getValue(applicationId, RVSettingsItem.PasswordPolicyNonAlphabetic).toLowerCase().equals("false");
    }

    public boolean Number(UUID applicationId)
    {
        return !util.getValue(applicationId, RVSettingsItem.PasswordPolicyNumber).toLowerCase().equals("false");
    }

    public boolean NonAlphaNumeric(UUID applicationId)
    {
        return !util.getValue(applicationId, RVSettingsItem.PasswordPolicyNonAlphaNumeric).toLowerCase().equals("false");
    }
}
