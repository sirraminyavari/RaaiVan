package com.raaivan.modules.rv.beans;

import org.joda.time.DateTime;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class RVJob {
    private Thread ThreadObject;
    private Integer Interval;
    private DateTime StartTime;
    private DateTime EndTime;
    private DateTime LastActivityDate;
    private Long LastActivityDuration;
    private String ErrorMessage;
    private UUID TenantID;

    public RVJob(UUID tenantId)
    {
        TenantID = tenantId;
    }

    public boolean checkTime()
    {
        try {
            if (StartTime == null || EndTime == null || TenantID == null) return false;

            DateTime now = DateTime.now();
            now = new DateTime(2000, 1, 1,
                    now.getHourOfDay(), now.getMinuteOfHour(), now.getSecondOfMinute(), now.getMillisOfSecond());
            StartTime = new DateTime(2000, 1, 1, StartTime.getHourOfDay(), StartTime.getMinuteOfHour(), 0);
            EndTime = new DateTime(2000, 1, 1, EndTime.getHourOfDay(), EndTime.getMinuteOfHour(), 59);

            if (LastActivityDate == null && now.compareTo(StartTime) < 0)
                ThreadObject.sleep(StartTime.getMillis() - now.getMillis() + 1000);

            return EndTime.compareTo(StartTime) > 0 ? (now.compareTo(StartTime) >= 0 && now.compareTo(EndTime) <= 0) :
                    (now.compareTo(StartTime) >= 0 || now.compareTo(EndTime) <= 0);
        }catch (Exception ex)
        {
            return false;
        }
    }

    public Thread getThreadObject() {
        return ThreadObject;
    }

    public void setThreadObject(Thread threadObject) {
        ThreadObject = threadObject;
    }

    public Integer getInterval() {
        return Interval;
    }

    public void setInterval(Integer interval) {
        Interval = interval;
    }

    public DateTime getStartTime() {
        return StartTime;
    }

    public void setStartTime(DateTime startTime) {
        StartTime = startTime;
    }

    public DateTime getEndTime() {
        return EndTime;
    }

    public void setEndTime(DateTime endTime) {
        EndTime = endTime;
    }

    public DateTime getLastActivityDate() {
        return LastActivityDate;
    }

    public void setLastActivityDate(DateTime lastActivityDate) {
        LastActivityDate = lastActivityDate;
    }

    public Long getLastActivityDuration() {
        return LastActivityDuration;
    }

    public void setLastActivityDuration(Long lastActivityDuration) {
        LastActivityDuration = lastActivityDuration;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }

    public UUID getTenantID() {
        return TenantID;
    }

    public void setTenantID(UUID tenantID) {
        TenantID = tenantID;
    }
}
