package com.raaivan.modules.dataexchange.beans;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class ExchangeNode {
    private UUID NodeID;
    private String AdditionalID;
    private String Name;
    private String ParentAdditionalID;
    private String Abstract;
    private String Tags;

    public UUID getNodeID() {
        return NodeID;
    }

    public void setNodeID(UUID nodeID) {
        NodeID = nodeID;
    }

    public String getAdditionalID() {
        return AdditionalID;
    }

    public void setAdditionalID(String additionalID) {
        AdditionalID = additionalID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getParentAdditionalID() {
        return ParentAdditionalID;
    }

    public void setParentAdditionalID(String parentAdditionalID) {
        ParentAdditionalID = parentAdditionalID;
    }

    public String getAbstract() {
        return Abstract;
    }

    public void setAbstract(String anAbstract) {
        Abstract = anAbstract;
    }

    public String getTags() {
        return Tags;
    }

    public void setTags(String tags) {
        Tags = tags;
    }
}
