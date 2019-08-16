package com.raaivan.util.rvsettings;

import com.raaivan.util.RaaiVanConfig;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.UUID;

@Component
@ApplicationScope
public class RVSettingsEmailQueue {
    private RVSettingsUtil util;

    @Autowired
    public void _setDependencies(RVSettingsUtil util) {
        if(this.util == null) this.util = util;
    }

    public boolean EnableEmailQueue(UUID applicationId)
    {
        return RaaiVanConfig.Modules.SMSEMailNotifier &&
                util.getValue(applicationId, RVSettingsItem.EmailQueue).toLowerCase().equals("true");
    }

    public int Interval(UUID applicationId)
    {
        return util.getInt(applicationId, RVSettingsItem.EmailQueueInterval, 3600000, null);
    }

    public int BatchSize(UUID applicationId)
    {
        return util.getInt(applicationId, RVSettingsItem.EmailQueueBatchSize, 100, 1);
    }

    public DateTime StartTime(UUID applicationId)
    {
        return util.getTime(applicationId, RVSettingsItem.EmailQueueStartTime, new DateTime(2000, 1, 1, 0, 0, 0));
    }

    public DateTime EndTime(UUID applicationId)
    {
        return util.getTime(applicationId, RVSettingsItem.EmailQueueEndTime, new DateTime(2000, 1, 1, 23, 59, 59));
    }
}
