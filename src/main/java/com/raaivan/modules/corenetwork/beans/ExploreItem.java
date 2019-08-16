package com.raaivan.modules.corenetwork.beans;

import org.joda.time.DateTime;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class ExploreItem {
    private UUID BaseID;
    private UUID BaseTypeID;
    private String BaseName;
    private String BaseType;
    private UUID RelatedID;
    private UUID RelatedTypeID;
    private String RelatedName;
    private String RelatedType;
    private DateTime RelatedCreationDate;
    private Boolean IsTag;
    private Boolean IsRelation;
    private Boolean IsRegistrationArea;

    public UUID getBaseID() {
        return BaseID;
    }

    public void setBaseID(UUID baseID) {
        BaseID = baseID;
    }

    public UUID getBaseTypeID() {
        return BaseTypeID;
    }

    public void setBaseTypeID(UUID baseTypeID) {
        BaseTypeID = baseTypeID;
    }

    public String getBaseName() {
        return BaseName;
    }

    public void setBaseName(String baseName) {
        BaseName = baseName;
    }

    public String getBaseType() {
        return BaseType;
    }

    public void setBaseType(String baseType) {
        BaseType = baseType;
    }

    public UUID getRelatedID() {
        return RelatedID;
    }

    public void setRelatedID(UUID relatedID) {
        RelatedID = relatedID;
    }

    public UUID getRelatedTypeID() {
        return RelatedTypeID;
    }

    public void setRelatedTypeID(UUID relatedTypeID) {
        RelatedTypeID = relatedTypeID;
    }

    public String getRelatedName() {
        return RelatedName;
    }

    public void setRelatedName(String relatedName) {
        RelatedName = relatedName;
    }

    public String getRelatedType() {
        return RelatedType;
    }

    public void setRelatedType(String relatedType) {
        RelatedType = relatedType;
    }

    public DateTime getRelatedCreationDate() {
        return RelatedCreationDate;
    }

    public void setRelatedCreationDate(DateTime relatedCreationDate) {
        RelatedCreationDate = relatedCreationDate;
    }

    public Boolean getTag() {
        return IsTag;
    }

    public void setTag(Boolean tag) {
        IsTag = tag;
    }

    public Boolean getRelation() {
        return IsRelation;
    }

    public void setRelation(Boolean relation) {
        IsRelation = relation;
    }

    public Boolean getRegistrationArea() {
        return IsRegistrationArea;
    }

    public void setRegistrationArea(Boolean registrationArea) {
        IsRegistrationArea = registrationArea;
    }
}
