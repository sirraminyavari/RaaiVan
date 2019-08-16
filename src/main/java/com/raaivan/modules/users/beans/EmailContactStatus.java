package com.raaivan.modules.users.beans;

import com.raaivan.util.PublicMethods;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class EmailContactStatus {
    private UUID UserID;
    private String Email;
    private Boolean FriendRequestReceived;

    public UUID getUserID() {
        return UserID;
    }

    public void setUserID(UUID userID) {
        UserID = userID;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public Boolean getFriendRequestReceived() {
        return FriendRequestReceived;
    }

    public void setFriendRequestReceived(Boolean friendRequestReceived) {
        FriendRequestReceived = friendRequestReceived;
    }
}
