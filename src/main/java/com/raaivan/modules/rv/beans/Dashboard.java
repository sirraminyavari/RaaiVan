package com.raaivan.modules.rv.beans;

import com.raaivan.modules.rv.enums.DashboardSubType;
import com.raaivan.modules.rv.enums.DashboardType;
import com.raaivan.util.PublicMethods;
import org.joda.time.DateTime;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class Dashboard {
    private Long DashboardID;
    private UUID UserID;
    private UUID NodeID;
    private String NodeAdditionalID;
    private String NodeName;
    private String NodeType;
    private DashboardType Type;
    private DashboardSubType SubType;
    private String Info;
    private Boolean Removable;
    private UUID SenderUserID;
    private DateTime SendDate;
    private DateTime ExpirationDate;
    private Boolean Seen;
    private DateTime ViewDate;
    private Boolean Done;
    private DateTime ActionDate;
    private Boolean ToBeRemoved;

    public Dashboard()
    {
        Type = DashboardType.NotSet;
        SubType = DashboardSubType.NotSet;
    }

    public Long getDashboardID() {
        return DashboardID;
    }

    public void setDashboardID(Long dashboardID) {
        DashboardID = dashboardID;
    }

    public UUID getUserID() {
        return UserID;
    }

    public void setUserID(UUID userID) {
        UserID = userID;
    }

    public UUID getNodeID() {
        return NodeID;
    }

    public void setNodeID(UUID nodeID) {
        NodeID = nodeID;
    }

    public String getNodeAdditionalID() {
        return NodeAdditionalID;
    }

    public void setNodeAdditionalID(String nodeAdditionalID) {
        NodeAdditionalID = nodeAdditionalID;
    }

    public String getNodeName() {
        return NodeName;
    }

    public void setNodeName(String nodeName) {
        NodeName = nodeName;
    }

    public String getNodeType() {
        return NodeType;
    }

    public void setNodeType(String nodeType) {
        NodeType = nodeType;
    }

    public DashboardType getType() {
        return Type;
    }

    public void setType(DashboardType type) {
        Type = type;
    }

    public DashboardSubType getSubType() {
        return SubType;
    }

    public void setSubType(DashboardSubType subType) {
        SubType = subType;
    }

    public String getInfo() {
        return Info;
    }

    public void setInfo(String info) {
        Info = info;
    }

    public Boolean getRemovable() {
        return Removable;
    }

    public void setRemovable(Boolean removable) {
        Removable = removable;
    }

    public UUID getSenderUserID() {
        return SenderUserID;
    }

    public void setSenderUserID(UUID senderUserID) {
        SenderUserID = senderUserID;
    }

    public DateTime getSendDate() {
        return SendDate;
    }

    public void setSendDate(DateTime sendDate) {
        SendDate = sendDate;
    }

    public DateTime getExpirationDate() {
        return ExpirationDate;
    }

    public void setExpirationDate(DateTime expirationDate) {
        ExpirationDate = expirationDate;
    }

    public Boolean getSeen() {
        return Seen;
    }

    public void setSeen(Boolean seen) {
        Seen = seen;
    }

    public DateTime getViewDate() {
        return ViewDate;
    }

    public void setViewDate(DateTime viewDate) {
        ViewDate = viewDate;
    }

    public Boolean getDone() {
        return Done;
    }

    public void setDone(Boolean done) {
        Done = done;
    }

    public DateTime getActionDate() {
        return ActionDate;
    }

    public void setActionDate(DateTime actionDate) {
        ActionDate = actionDate;
    }

    public Boolean getToBeRemoved() {
        return ToBeRemoved;
    }

    public void setToBeRemoved(Boolean toBeRemoved) {
        ToBeRemoved = toBeRemoved;
    }
}
