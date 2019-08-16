package com.raaivan.modules.rv.beans;

import com.raaivan.modules.rv.enums.EmailAction;
import com.raaivan.util.PublicMethods;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class EmailQueueItem {
    private Long ID;
    private UUID SenderUserID;
    private EmailAction Action;
    private String Email;
    private String Title;
    private String EmailBody;

    public EmailQueueItem(){
        Action = EmailAction.None;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public UUID getSenderUserID() {
        return SenderUserID;
    }

    public void setSenderUserID(UUID senderUserID) {
        SenderUserID = senderUserID;
    }

    public EmailAction getAction() {
        return Action;
    }

    public void setAction(EmailAction action) {
        Action = action;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getEmailBody() {
        return EmailBody;
    }

    public void setEmailBody(String emailBody) {
        EmailBody = emailBody;
    }
}
