package com.raaivan.modules.forms.beans;

import org.joda.time.DateTime;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class Poll {
    private UUID PollID;
    private UUID IsCopyOfPollID;
    private UUID OwnerID;
    private String Name;
    private String RefName;
    private String Description;
    private String RefDescription;
    private DateTime BeginDate;
    private DateTime FinishDate;
    private Boolean ShowSummary;
    private Boolean HideContributors;

    public UUID getPollID() {
        return PollID;
    }

    public void setPollID(UUID pollID) {
        PollID = pollID;
    }

    public UUID getIsCopyOfPollID() {
        return IsCopyOfPollID;
    }

    public void setIsCopyOfPollID(UUID isCopyOfPollID) {
        IsCopyOfPollID = isCopyOfPollID;
    }

    public UUID getOwnerID() {
        return OwnerID;
    }

    public void setOwnerID(UUID ownerID) {
        OwnerID = ownerID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getRefName() {
        return RefName;
    }

    public void setRefName(String refName) {
        RefName = refName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getRefDescription() {
        return RefDescription;
    }

    public void setRefDescription(String refDescription) {
        RefDescription = refDescription;
    }

    public DateTime getBeginDate() {
        return BeginDate;
    }

    public void setBeginDate(DateTime beginDate) {
        BeginDate = beginDate;
    }

    public DateTime getFinishDate() {
        return FinishDate;
    }

    public void setFinishDate(DateTime finishDate) {
        FinishDate = finishDate;
    }

    public Boolean getShowSummary() {
        return ShowSummary;
    }

    public void setShowSummary(Boolean showSummary) {
        ShowSummary = showSummary;
    }

    public Boolean getHideContributors() {
        return HideContributors;
    }

    public void setHideContributors(Boolean hideContributors) {
        HideContributors = hideContributors;
    }
}
