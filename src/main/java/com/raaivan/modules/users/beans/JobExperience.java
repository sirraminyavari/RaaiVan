package com.raaivan.modules.users.beans;

import com.raaivan.util.PublicMethods;
import org.joda.time.DateTime;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class JobExperience {
    private UUID JobID;
    private UUID UserID;
    private String Title;
    private String Employer;
    private DateTime StartDate;
    private DateTime EndDate;

    public UUID getJobID() {
        return JobID;
    }

    public void setJobID(UUID jobID) {
        JobID = jobID;
    }

    public UUID getUserID() {
        return UserID;
    }

    public void setUserID(UUID userID) {
        UserID = userID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getEmployer() {
        return Employer;
    }

    public void setEmployer(String employer) {
        Employer = employer;
    }

    public DateTime getStartDate() {
        return StartDate;
    }

    public void setStartDate(DateTime startDate) {
        StartDate = startDate;
    }

    public DateTime getEndDate() {
        return EndDate;
    }

    public void setEndDate(DateTime endDate) {
        EndDate = endDate;
    }
}
