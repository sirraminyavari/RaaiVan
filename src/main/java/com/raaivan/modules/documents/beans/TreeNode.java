package com.raaivan.modules.documents.beans;

import com.raaivan.modules.users.beans.User;
import com.raaivan.util.Base64;
import com.raaivan.util.RVBeanFactory;
import com.raaivan.util.RVJSON;
import com.raaivan.util.PublicMethods;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class TreeNode {
    private UUID TreeNodeID;
    private UUID TreeID;
    private UUID ParentNodeID;
    private Boolean HasChild;
    private String Name;
    private String Description;
    private DateTime CreationDate;
    private User Creator;
    private DateTime LastModificationDate;
    private UUID LastModifierUserID;

    public TreeNode() {
        Creator = RVBeanFactory.getBean(User.class);
    }

    public UUID getTreeNodeID() {
        return TreeNodeID;
    }

    public void setTreeNodeID(UUID treeNodeID) {
        TreeNodeID = treeNodeID;
    }

    public UUID getTreeID() {
        return TreeID;
    }

    public void setTreeID(UUID treeID) {
        TreeID = treeID;
    }

    public UUID getParentNodeID() {
        return ParentNodeID;
    }

    public void setParentNodeID(UUID parentNodeID) {
        ParentNodeID = parentNodeID;
    }

    public Boolean getHasChild() {
        return HasChild;
    }

    public void setHasChild(Boolean hasChild) {
        HasChild = hasChild;
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

    public DateTime getCreationDate() {
        return CreationDate;
    }

    public void setCreationDate(DateTime creationDate) {
        CreationDate = creationDate;
    }

    public User getCreator() {
        return Creator;
    }

    public void setCreator(User creator) {
        Creator = creator;
    }

    public DateTime getLastModificationDate() {
        return LastModificationDate;
    }

    public void setLastModificationDate(DateTime lastModificationDate) {
        LastModificationDate = lastModificationDate;
    }

    public UUID getLastModifierUserID() {
        return LastModifierUserID;
    }

    public void setLastModifierUserID(UUID lastModifierUserID) {
        LastModifierUserID = lastModifierUserID;
    }

    public RVJSON toJson() {
        return (new RVJSON())
                .add("TreeID", TreeID)
                .add("TreeNodeID", TreeNodeID)
                .add("Title", Base64.encode(Name))
                .add("ParentID", ParentNodeID)
                .add("HasChild", HasChild);
    }
}
