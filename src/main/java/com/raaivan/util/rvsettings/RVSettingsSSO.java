package com.raaivan.util.rvsettings;

import com.raaivan.util.PublicConsts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.UUID;

@Component
@ApplicationScope
public class RVSettingsSSO {
    private RVSettingsUtil util;

    @Autowired
    public void _setDependencies(RVSettingsUtil util) {
        if(this.util == null) this.util = util;
    }

    public boolean Enabled(UUID applicationId)
    {
        return util.getValue(applicationId, RVSettingsItem.SSOEnabled).toLowerCase().equals("true");
    }

    public String TicketVariableName(UUID applicationId)
    {
        return util.getValue(applicationId, RVSettingsItem.SSOTicketVariableName);
    }

    public String LoginURL(UUID applicationId)
    {
        return util.getValue(applicationId, RVSettingsItem.SSOLoginURL).replace("[return_url]",
                PublicConsts.getCompleteUrl(applicationId, PublicConsts.LoginPage));
    }

    public String ValidateURL(UUID applicationId, String ticket)
    {
        return util.getValue(applicationId, RVSettingsItem.SSOValidateURL).replace("[return_url]",
                PublicConsts.getCompleteUrl(applicationId, PublicConsts.LoginPage)).replace("[ticket]", ticket);
    }

    public String XMLUserNameTag(UUID applicationId)
    {
        return util.getValue(applicationId, RVSettingsItem.SSOXMLUserNameTag);
    }

    public String JSONUserNamePath(UUID applicationId)
    {
        return util.getValue(applicationId, RVSettingsItem.SSOJSONUserNamePath);
    }

    public String InvalidTicketCode(UUID applicationId)
    {
        return util.getValue(applicationId, RVSettingsItem.SSOInvalidTicketCode);
    }
}
