package com.raaivan.modules.corenetwork.beans;

import org.joda.time.DateTime;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class NodeList {
    private UUID ListID;
    private String AdditionalID;
    private UUID NodeTypeID;
    private String NodeType;
    private String Name;
    private String Description;
    private UUID CreatorUserID;
    private DateTime CreationDate;
    private UUID LastModifierUserID;
    private DateTime LastModificationDate;
    private UUID ParentListID;
    private String ParentName;

    public UUID getListID() {
        return ListID;
    }

    public void setListID(UUID listID) {
        ListID = listID;
    }

    public String getAdditionalID() {
        return AdditionalID;
    }

    public void setAdditionalID(String additionalID) {
        AdditionalID = additionalID;
    }

    public UUID getNodeTypeID() {
        return NodeTypeID;
    }

    public void setNodeTypeID(UUID nodeTypeID) {
        NodeTypeID = nodeTypeID;
    }

    public String getNodeType() {
        return NodeType;
    }

    public void setNodeType(String nodeType) {
        NodeType = nodeType;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
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

    public UUID getLastModifierUserID() {
        return LastModifierUserID;
    }

    public void setLastModifierUserID(UUID lastModifierUserID) {
        LastModifierUserID = lastModifierUserID;
    }

    public DateTime getLastModificationDate() {
        return LastModificationDate;
    }

    public void setLastModificationDate(DateTime lastModificationDate) {
        LastModificationDate = lastModificationDate;
    }

    public UUID getParentListID() {
        return ParentListID;
    }

    public void setParentListID(UUID parentListID) {
        ParentListID = parentListID;
    }

    public String getParentName() {
        return ParentName;
    }

    public void setParentName(String parentName) {
        ParentName = parentName;
    }
}
