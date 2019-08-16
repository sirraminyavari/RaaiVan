package com.raaivan.modules.privacy.beans;

import com.raaivan.modules.users.beans.User;
import com.raaivan.util.Base64;
import com.raaivan.util.RVBeanFactory;
import com.raaivan.util.RVJSON;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class ConfidentialityLevel {
    private UUID ID;
    private Integer LevelID;
    private String Title;
    private User Creator;
    private User Modifier;

    public ConfidentialityLevel()
    {
        Creator = RVBeanFactory.getBean(User.class);
        Modifier = RVBeanFactory.getBean(User.class);
    }

    public UUID getID() {
        return ID;
    }

    public void setID(UUID ID) {
        this.ID = ID;
    }

    public Integer getLevelID() {
        return LevelID;
    }

    public void setLevelID(Integer levelID) {
        LevelID = levelID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public User getCreator() {
        return Creator;
    }

    public void setCreator(User creator) {
        Creator = creator;
    }

    public User getModifier() {
        return Modifier;
    }

    public void setModifier(User modifier) {
        Modifier = modifier;
    }

    public RVJSON toJson() {
        return (new RVJSON())
                .add("ID", ID)
                .add("LevelID", LevelID)
                .add("Title", Base64.encode(Title));
    }
}
