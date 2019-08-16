package com.raaivan.util.rvsettings;

import com.raaivan.modules.rv.RVDAO;
import com.raaivan.modules.rv.beans.MutableUUID;
import com.raaivan.modules.rv.beans.Tenant;
import com.raaivan.util.PublicMethods;
import com.raaivan.util.RaaiVanConfig;
import com.raaivan.web.config.RVCustomProperties;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@ApplicationScope
public class RVSettingsTenants {
    private RVSettingsUtil util;
    private RVCustomProperties rvConfig;
    private PublicMethods publicMethods;
    private RVDAO rvDao;

    @Autowired
    public void _setDependencies(RVSettingsUtil util, RVCustomProperties rvCustomProperties,
                                 PublicMethods publicMethods, RVDAO rvDao) {
        if (this.util == null) this.util = util;
        if (this.rvConfig == null) this.rvConfig = rvCustomProperties;
        if (this.publicMethods == null) this.publicMethods = publicMethods;
        if (this.rvDao == null) this.rvDao = rvDao;
    }

    private static List<Tenant> _Tenants;

    public List<Tenant> get() {
        if (_Tenants == null) {
            _Tenants = new ArrayList<>();

            for(SimpleEntry<String, String> entry : rvConfig.getKeyValues(RVSettingsItem.Tenant)){
                String[] vals = entry.getKey().split(",");

                MutableUUID tenantId = new MutableUUID();

                if (vals.length < 3 || !publicMethods.tryParseUUID(vals[0], tenantId)) continue;

                if (RaaiVanConfig.GlobalSettings.TenantIDs != null && RaaiVanConfig.GlobalSettings.TenantIDs.size() > 0 &&
                        RaaiVanConfig.GlobalSettings.TenantIDs.stream().noneMatch(u -> u == tenantId.getValue())) continue;

                String name = vals[1];
                String domain = vals[2];

                String protocol = domain.indexOf("://") > 0 ? domain.substring(0, domain.indexOf("://")) : "";

                if (!StringUtils.isBlank(protocol)) domain = domain.substring(domain.indexOf("://") + 3);

                if (StringUtils.isBlank(protocol)) protocol = "http";

                _Tenants.add(new Tenant(tenantId.getValue(), name, domain, protocol));
            }

            if(_Tenants.size() == 0)
            {
                List<UUID> lst = rvDao.getApplicationIDs();
                if (lst.size() == 1) _Tenants.add(new Tenant(lst.get(0), "", "", ""));
            }

            if (_Tenants.size() > RaaiVanConfig.GlobalSettings.MaxTenantsCount()) _Tenants = new ArrayList<>();

            rvDao.setApplications(_Tenants.stream()
                    .map(u -> new SimpleEntry<UUID, String>(u.getID(), u.getName()))
                    .collect(Collectors.toList()));
        }

        return _Tenants;
    }

    public Tenant get(UUID applicationId){
        List<Tenant> lst = get().stream().filter(u -> u.getID() == applicationId).collect(Collectors.toList());
        return lst.size() == 0 ? null : lst.get(0);
    }
}
