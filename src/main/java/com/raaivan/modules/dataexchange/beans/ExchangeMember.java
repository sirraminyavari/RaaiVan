package com.raaivan.modules.dataexchange.beans;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class ExchangeMember {
    private String NodeTypeAdditionalID;
    private String NodeAdditionalID;
    private UUID NodeID;
    private String UserName;
    private Boolean IsAdmin;

    public String getNodeTypeAdditionalID() {
        return NodeTypeAdditionalID;
    }

    public void setNodeTypeAdditionalID(String nodeTypeAdditionalID) {
        NodeTypeAdditionalID = nodeTypeAdditionalID;
    }

    public String getNodeAdditionalID() {
        return NodeAdditionalID;
    }

    public void setNodeAdditionalID(String nodeAdditionalID) {
        NodeAdditionalID = nodeAdditionalID;
    }

    public UUID getNodeID() {
        return NodeID;
    }

    public void setNodeID(UUID nodeID) {
        NodeID = nodeID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public Boolean getAdmin() {
        return IsAdmin;
    }

    public void setAdmin(Boolean admin) {
        IsAdmin = admin;
    }
}
