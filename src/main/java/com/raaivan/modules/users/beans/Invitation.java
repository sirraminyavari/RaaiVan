package com.raaivan.modules.users.beans;

import com.raaivan.util.RVBeanFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class Invitation {
    private UUID InvitationID;
    private User SenderUser;
    private User ReceiverUser;
    private DateTime SendDate;
    private String Email;
    private Boolean Activated;

    public Invitation()
    {
        SenderUser = RVBeanFactory.getBean(User.class);
        ReceiverUser = RVBeanFactory.getBean(User.class);
    }

    public UUID getInvitationID() {
        return InvitationID;
    }

    public void setInvitationID(UUID invitationID) {
        InvitationID = invitationID;
    }

    public User getSenderUser() {
        return SenderUser;
    }

    public void setSenderUser(User senderUser) {
        SenderUser = senderUser;
    }

    public User getReceiverUser() {
        return ReceiverUser;
    }

    public void setReceiverUser(User receiverUser) {
        ReceiverUser = receiverUser;
    }

    public DateTime getSendDate() {
        return SendDate;
    }

    public void setSendDate(DateTime sendDate) {
        SendDate = sendDate;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public Boolean getActivated() {
        return Activated;
    }

    public void setActivated(Boolean activated) {
        Activated = activated;
    }
}
