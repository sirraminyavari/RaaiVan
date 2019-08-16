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
public class Tree {
    private UUID TreeID;
    private Boolean IsPrivate;
    private UUID OwnerID;
    private String Name;
    private String Description;
    private DateTime CreationDate;
    private User Creator;
    private DateTime LastModificationDate;
    private UUID LastModifierUserID;

    public Tree(){
        Creator = RVBeanFactory.getBean(User.class);
    }

    public UUID getTreeID() {
        return TreeID;
    }

    public void setTreeID(UUID treeID) {
        TreeID = treeID;
    }

    public Boolean getPrivate() {
        return IsPrivate;
    }

    public void setPrivate(Boolean aPrivate) {
        IsPrivate = aPrivate;
    }

    public UUID getOwnerID() {
        return OwnerID;
    }

    public void setOwnerID(UUID ownerID) {
        OwnerID = ownerID;
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

    public RVJSON toJson()
    {
        return (new RVJSON()).add("ID", TreeID).add("Title", Base64.encode(Name));
    }
}
