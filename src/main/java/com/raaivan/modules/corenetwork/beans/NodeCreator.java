package com.raaivan.modules.corenetwork.beans;

import com.raaivan.modules.users.beans.User;
import com.raaivan.util.RVBeanFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class NodeCreator {
    private UUID NodeID;
    private User User;
    private Double CollaborationShare;
    private String Status;
    private UUID CreatorUserID;
    private DateTime CreationDate;
    private UUID LastModifierUserID;
    private DateTime LastModificationDate;

    public NodeCreator(){
        this.User = RVBeanFactory.getBean(User.class);
    }

    public UUID getNodeID() {
        return NodeID;
    }

    public void setNodeID(UUID nodeID) {
        NodeID = nodeID;
    }

    public com.raaivan.modules.users.beans.User getUser() {
        return User;
    }

    public void setUser(User user) {
        User = user;
    }

    public Double getCollaborationShare() {
        return CollaborationShare;
    }

    public void setCollaborationShare(Double collaborationShare) {
        CollaborationShare = collaborationShare;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
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
}
