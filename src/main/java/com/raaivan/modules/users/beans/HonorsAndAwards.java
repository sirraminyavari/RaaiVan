package com.raaivan.modules.users.beans;

import com.raaivan.util.PublicMethods;
import org.joda.time.DateTime;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class HonorsAndAwards {
    private UUID ID;
    private UUID UserID;
    private String Title;
    private String Issuer;
    private String Occupation;
    private DateTime IssueDate;
    private String Description;

    public UUID getID() {
        return ID;
    }

    public void setID(UUID ID) {
        this.ID = ID;
    }

    public UUID getUserID() {
        return UserID;
    }

    public void setUserID(UUID userID) {
        UserID = userID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getIssuer() {
        return Issuer;
    }

    public void setIssuer(String issuer) {
        Issuer = issuer;
    }

    public String getOccupation() {
        return Occupation;
    }

    public void setOccupation(String occupation) {
        Occupation = occupation;
    }

    public DateTime getIssueDate() {
        return IssueDate;
    }

    public void setIssueDate(DateTime issueDate) {
        IssueDate = issueDate;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
