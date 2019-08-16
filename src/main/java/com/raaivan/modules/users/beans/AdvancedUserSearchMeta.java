package com.raaivan.modules.users.beans;

import com.raaivan.util.PublicMethods;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class AdvancedUserSearchMeta {
    private UUID NodeID;
    private Double Rank;
    private Boolean IsMember;
    private Boolean IsExpert;
    private Boolean IsContributor;
    private Boolean HasProperty;

    public UUID getNodeID() {
        return NodeID;
    }

    public void setNodeID(UUID nodeID) {
        NodeID = nodeID;
    }

    public Double getRank() {
        return Rank;
    }

    public void setRank(Double rank) {
        Rank = rank;
    }

    public Boolean getMember() {
        return IsMember;
    }

    public void setMember(Boolean member) {
        IsMember = member;
    }

    public Boolean getExpert() {
        return IsExpert;
    }

    public void setExpert(Boolean expert) {
        IsExpert = expert;
    }

    public Boolean getContributor() {
        return IsContributor;
    }

    public void setContributor(Boolean contributor) {
        IsContributor = contributor;
    }

    public Boolean getHasProperty() {
        return HasProperty;
    }

    public void setHasProperty(Boolean hasProperty) {
        HasProperty = hasProperty;
    }
}
