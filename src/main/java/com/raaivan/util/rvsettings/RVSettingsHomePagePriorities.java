package com.raaivan.util.rvsettings;

import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@ApplicationScope
public class RVSettingsHomePagePriorities {
    private RVSettingsUtil util;

    @Autowired
    public void _setDependencies(RVSettingsUtil util) {
        if(this.util == null) this.util = util;
    }

    private List<String> _getArea(UUID applicationId, String pos)
    {
        String _str = util.getValue(applicationId, RVSettingsItem.HomePagePriorities);

        List<String> areas = Arrays.stream(_str.split("\\|"))
                .filter(u -> u.startsWith(pos)).collect(Collectors.toList());
        if(areas.size() == 1 && StringUtils.isBlank(areas.get(0))) areas = new ArrayList<>();

        String str = areas.size() == 0 ? "" : areas.get(areas.size() - 1);

        return Arrays.stream(str.split(":")).filter(v -> !v.equals(pos) && !StringUtils.isBlank(v)).collect(Collectors.toList());
    }

    public List<String> Left(UUID applicationId)
    {
        return _getArea(applicationId, "Left");
    }

    public List<String> Center(UUID applicationId)
    {
        return _getArea(applicationId, "Center");
    }

    public List<String> Right(UUID applicationId)
    {
        return _getArea(applicationId, "Right");
    }
}
