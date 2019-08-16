package com.raaivan.util.rvsettings;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.UUID;

@Component
@ApplicationScope
public class RVSettingsFileContentExtraction {
    private RVSettingsUtil util;

    @Autowired
    public void _setDependencies(RVSettingsUtil util) {
        if(this.util == null) this.util = util;
    }

    public boolean FileContents(UUID applicationId)
    {
        return !util.getValue(applicationId, RVSettingsItem.FileContents).toLowerCase().equals("false");
    }

    public int ExtractionInterval(UUID applicationId)
    {
        return util.getInt(applicationId, RVSettingsItem.FileContentExtractionInterval, 3600000, null);
    }

    public int BatchSize(UUID applicationId)
    {
        return util.getInt(applicationId, RVSettingsItem.FileContentExtractionBatchSize, 10, null);
    }

    public DateTime StartTime(UUID applicationId)
    {
        return util.getTime(applicationId, RVSettingsItem.FileContentExtractionStartTime,
                new DateTime(2000, 1, 1, 0, 0, 0));
    }

    public DateTime EndTime(UUID applicationId)
    {
        return util.getTime(applicationId, RVSettingsItem.FileContentExtractionEndTime,
                new DateTime(2000, 1, 1, 23, 23, 59));
    }
}
