package com.raaivan.modules.users.beans;

import com.raaivan.util.PublicMethods;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class EmailAddress {
    private UUID EmailID;
    private UUID UserID;
    private String EmailAddress;

    public UUID getEmailID() {
        return EmailID;
    }

    public void setEmailID(UUID emailID) {
        EmailID = emailID;
    }

    public UUID getUserID() {
        return UserID;
    }

    public void setUserID(UUID userID) {
        UserID = userID;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }
}
