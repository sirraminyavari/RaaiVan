package com.raaivan.util.rvsettings;

import io.micrometer.core.instrument.util.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@ApplicationScope
public class RVSettingsIndexUpdate {
    private RVSettingsUtil util;
    private RVSettingsFileContentExtraction FileContentExtraction;

    @Autowired
    public void _setDependencies(RVSettingsUtil util, RVSettingsFileContentExtraction fileContentExtraction) {
        if(this.util == null) this.util = util;
        if(this.FileContentExtraction == null) this.FileContentExtraction = fileContentExtraction;
    }

    public boolean Index(UUID applicationId)
    {
        return !util.getValue(applicationId, RVSettingsItem.Index).toLowerCase().equals("false");
    }

    public boolean Ram(UUID applicationId)
    {
        return util.getValue(applicationId, RVSettingsItem.IndexUpdateUseRam).toLowerCase().equals("true");
    }

    public int Interval(UUID applicationId)
    {
        return util.getInt(applicationId, RVSettingsItem.IndexUpdateInterval, 3600000, null);
    }

    public int BatchSize(UUID applicationId)
    {
        return util.getInt(applicationId, RVSettingsItem.IndexUpdateBatchSize, 10, null);
    }

    public DateTime StartTime(UUID applicationId)
    {
        return util.getTime(applicationId, RVSettingsItem.IndexUpdateStartTime, new DateTime(2000, 1, 1, 0, 0, 0));
    }

    public DateTime EndTime(UUID applicationId)
    {
        return util.getTime(applicationId, RVSettingsItem.IndexUpdateEndTime, new DateTime(2000, 1, 1, 23, 59, 59));
    }

    public String[] Priorities(UUID applicationId) {
        String val = util.getValue(applicationId, RVSettingsItem.IndexUpdatePriorities);

        String[] ps = StringUtils.isBlank(val) ? new String[0] : val.split(",");

        List<String> p = new ArrayList<>();

        for (String s : ps) {
            switch (s.toLowerCase()) {
                case "node":
                    p.add("Node");
                    break;
                case "nodetype":
                    p.add("NodeType");
                    break;
                case "question":
                    p.add("Question");
                    break;
                case "file":
                    if (FileContentExtraction.FileContents(applicationId)) p.add("File");
                    break;
                case "user":
                    p.add("User");
                    break;
            }
        }

        if (p.stream().noneMatch(u -> u.equals("Node"))) p.add("Node");
        if (p.stream().noneMatch(u -> u.equals("NodeType"))) p.add("NodeType");
        if (p.stream().noneMatch(u -> u.equals("Question"))) p.add("Question");
        if (FileContentExtraction.FileContents(applicationId) && p.stream().noneMatch(u -> u.equals("File")))
            p.add("File");
        if (p.stream().noneMatch(u -> u.equals("User"))) p.add("User");

        return (String[]) p.toArray();
    }

    public boolean CheckPermissions(UUID applicationId)
    {
        return util.getValue(applicationId, RVSettingsItem.CheckPermissionsForSearchResults).toLowerCase().equals("true");
    }
}
