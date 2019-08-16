package com.raaivan.modules.rv.beans;

import com.raaivan.util.PublicMethods;
import org.joda.time.DateTime;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class DeletedState {
    private Long ID;
    private UUID ObjectID;
    private String ObjectType;
    private DateTime Date;
    private Boolean Deleted;
    private Boolean Bidirectional;
    private Boolean HasReverse;
    private UUID RelSourceID;
    private UUID RelDestinationID;
    private String RelSourceType;
    private String RelDestinationType;
    private UUID RelCreatorID;

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public UUID getObjectID() {
        return ObjectID;
    }

    public void setObjectID(UUID objectID) {
        ObjectID = objectID;
    }

    public String getObjectType() {
        return ObjectType;
    }

    public void setObjectType(String objectType) {
        ObjectType = objectType;
    }

    public DateTime getDate() {
        return Date;
    }

    public void setDate(DateTime date) {
        Date = date;
    }

    public Boolean getDeleted() {
        return Deleted;
    }

    public void setDeleted(Boolean deleted) {
        Deleted = deleted;
    }

    public Boolean getBidirectional() {
        return Bidirectional;
    }

    public void setBidirectional(Boolean bidirectional) {
        Bidirectional = bidirectional;
    }

    public Boolean getHasReverse() {
        return HasReverse;
    }

    public void setHasReverse(Boolean hasReverse) {
        HasReverse = hasReverse;
    }

    public UUID getRelSourceID() {
        return RelSourceID;
    }

    public void setRelSourceID(UUID relSourceID) {
        RelSourceID = relSourceID;
    }

    public UUID getRelDestinationID() {
        return RelDestinationID;
    }

    public void setRelDestinationID(UUID relDestinationID) {
        RelDestinationID = relDestinationID;
    }

    public String getRelSourceType() {
        return RelSourceType;
    }

    public void setRelSourceType(String relSourceType) {
        RelSourceType = relSourceType;
    }

    public String getRelDestinationType() {
        return RelDestinationType;
    }

    public void setRelDestinationType(String relDestinationType) {
        RelDestinationType = relDestinationType;
    }

    public UUID getRelCreatorID() {
        return RelCreatorID;
    }

    public void setRelCreatorID(UUID relCreatorID) {
        RelCreatorID = relCreatorID;
    }
}
