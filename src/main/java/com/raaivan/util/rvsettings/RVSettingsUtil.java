package com.raaivan.util.rvsettings;

import com.raaivan.modules.rv.RVDAO;
import com.raaivan.util.PublicMethods;
import com.raaivan.web.config.RVCustomProperties;
import io.micrometer.core.instrument.util.StringUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@ApplicationScope
public class RVSettingsUtil {
    private PublicMethods publicMethods;
    private RVDAO rvDao;
    private RVCustomProperties rvConfig;

    @Autowired
    public void _setDependencies(PublicMethods publicMethods, RVDAO rvDao, RVCustomProperties rvCustomProperties) {
        if (this.publicMethods == null) this.publicMethods = publicMethods;
        if (this.rvDao == null) this.rvDao = rvDao;
        if (this.rvConfig == null) this.rvConfig = rvCustomProperties;
    }

    private Map<UUID, Map<RVSettingsItem, String>> SettingsDic = new ConcurrentHashMap<>();

    public String getValue(UUID applicationId, RVSettingsItem name, String defaultValue) {
        if (SettingsDic.containsKey(applicationId) && SettingsDic.get(applicationId).containsKey(name)) {
            return StringUtils.isBlank(SettingsDic.get(applicationId).get(name)) ?
                    defaultValue : SettingsDic.get(applicationId).get(name);
        } else {
            List<RVSettingsItem> lst = new ArrayList<RVSettingsItem>() {{
                add(name);
            }};

            Map<RVSettingsItem, String> dic = getValue(applicationId, lst);
            String value = dic.getOrDefault(name, "");
            return StringUtils.isBlank(value) ? defaultValue : value;
        }
    }

    public String getValue(UUID applicationId, RVSettingsItem name){
        return getValue(applicationId, name, "");
    }

    private Map<RVSettingsItem, String> getValue(UUID applicationId, List<RVSettingsItem> names) {
        Map<RVSettingsItem, String> items = rvDao.getSystemSettings(applicationId, names);

        List<RVSettingsItem> theNames = names.size() > 0 ? names :
                Arrays.stream(RVSettingsItem.values()).collect(Collectors.toList());

        for (RVSettingsItem n : theNames)
            if (!items.containsKey(n) || StringUtils.isBlank(items.get(n))) items.put(n, rvConfig.getValue(n));

        for (RVSettingsItem n : items.keySet()) saveValue(applicationId, n, items.get(n));
        return items;
    }

    private void saveValue(UUID applicationId, RVSettingsItem name, String value) {
        if (!SettingsDic.containsKey(applicationId)) SettingsDic.put(applicationId, new ConcurrentHashMap<>());

        SettingsDic.get(applicationId).put(name, StringUtils.isBlank(value) ? "" : value);
    }

    public boolean setValue(UUID applicationId, Map<RVSettingsItem, String> items, UUID currentUserId)
    {
        boolean result = rvDao.setSystemSettings(applicationId, items, currentUserId);
        if (!result) return false;
        for (RVSettingsItem itm : items.keySet())
        saveValue(applicationId, itm, items.get(itm));
        return true;
    }

    public boolean setValue(UUID applicationId, RVSettingsItem name, String value, UUID currentUserId)
    {
        Map<RVSettingsItem, String> items = new ConcurrentHashMap<>();
        items.put(name, value);
        return setValue(applicationId, items, currentUserId);
    }

    public DateTime getTime(UUID applicationId, RVSettingsItem name, DateTime defaultValue) {
        String val = getValue(applicationId, name);
        DateTime tm = defaultValue;

        if (!StringUtils.isBlank(val))
        {
            String[] t = val.split(":");
            MutableInt n = new MutableInt(0);
            if (t.length >= 2 && publicMethods.tryParseInt(t[0], n) && n.getValue() >= 0 &&
                    n.getValue() <= 23 && publicMethods.tryParseInt(t[1], n) && n.getValue() >= 0 && n.getValue() <= 59)
            {
                tm = new DateTime(defaultValue.getYear(), defaultValue.getMonthOfYear(), defaultValue.getDayOfMonth(),
                        Integer.parseInt(t[0]), Integer.parseInt(t[1]), 0);
            }
        }

        return tm;
    }

    public int getInt(UUID applicationId, RVSettingsItem name, int defaultValue, Integer minValue)
    {
        MutableInt val = new MutableInt(0);
        if (!publicMethods.tryParseInt(getValue(applicationId, name), val)) val.setValue(defaultValue);
        return minValue != null ? Math.max(val.getValue(), minValue) : val.getValue();
    }

    public int getDurationInSeconds(UUID applicationId, RVSettingsItem name, int defaultDuration, int maxDuration)
    {
        int val = 0;

        MutableInt hours = new MutableInt(0), minutes = new MutableInt(0), seconds = new MutableInt(0);
        String[] timeElements = (getValue(applicationId, name)).split(":");

        if (timeElements.length == 3 && publicMethods.tryParseInt(timeElements[0], hours) &&
                publicMethods.tryParseInt(timeElements[1], minutes) && publicMethods.tryParseInt(timeElements[2], seconds))
            val = (hours.getValue() * 60 * 60) + (minutes.getValue() * 60) + seconds.getValue();
        else
            val = defaultDuration;

        val = val <= 0 ? defaultDuration : val;

        return maxDuration > 0 && val > maxDuration ? maxDuration : val;
    }
}
