package com.raaivan.util.rvsettings;

import com.raaivan.util.PublicMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ApplicationScope
public class RVSettingsOrganization {
    private RVSettingsUtil util;
    private PublicMethods publicMethods;

    @Autowired
    public void _setDependencies(RVSettingsUtil util, PublicMethods publicMethods) {
        if(this.util == null) this.util = util;
        if(this.publicMethods == null) this.publicMethods = publicMethods;
    }

    public String Name(UUID applicationId)
    {
        return util.getValue(applicationId, RVSettingsItem.OrganizationName);
    }

    private static Map<UUID, Boolean> _LogoInited = new ConcurrentHashMap<>();
    private static Map<UUID, Image> _Logo = new ConcurrentHashMap<>();

    public Image Logo(UUID applicationId)
    {
        if (!_LogoInited.containsKey(applicationId) && !_Logo.containsKey(applicationId))
        {
            _LogoInited.put(applicationId, true);

            try
            {
                String address = publicMethods.mapPath("~/" + util.getValue(applicationId, RVSettingsItem.OrganizationLogo));
                if ((new File(address)).exists()) _Logo.put(applicationId, ImageIO.read(new File(address)));
            }
            catch(Exception ex) { }
        }

        return _Logo.get(applicationId);
    }
}
