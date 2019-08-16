package com.raaivan.modules.corenetwork.beans;

import org.joda.time.DateTime;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class Tag {
    private UUID TagID;
    private String Text;
    private Boolean Approved;
    private Integer CallsCount;
    private UUID CreatorUserID;
    private DateTime CreationDate;

    public UUID getTagID() {
        return TagID;
    }

    public void setTagID(UUID tagID) {
        TagID = tagID;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public Boolean getApproved() {
        return Approved;
    }

    public void setApproved(Boolean approved) {
        Approved = approved;
    }

    public Integer getCallsCount() {
        return CallsCount;
    }

    public void setCallsCount(Integer callsCount) {
        CallsCount = callsCount;
    }

    public UUID getCreatorUserID() {
        return CreatorUserID;
    }

    public void setCreatorUserID(UUID creatorUserID) {
        CreatorUserID = creatorUserID;
    }

    public DateTime getCreationDate() {
        return CreationDate;
    }

    public void setCreationDate(DateTime creationDate) {
        CreationDate = creationDate;
    }
}
