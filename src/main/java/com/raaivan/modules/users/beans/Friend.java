package com.raaivan.modules.users.beans;

import com.raaivan.util.RVBeanFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class Friend {
    private User User;
    private DateTime RequestDate;
    private DateTime AcceptionDate;
    private Boolean AreFriends;
    private Boolean IsSender;
    private Integer MutualFriendsCount;

    public Friend(){
        User = RVBeanFactory.getBean(User.class);
    }

    public User getUser() {
        return User;
    }

    public void setUser(User user) {
        User = user;
    }

    public DateTime getRequestDate() {
        return RequestDate;
    }

    public void setRequestDate(DateTime requestDate) {
        RequestDate = requestDate;
    }

    public DateTime getAcceptionDate() {
        return AcceptionDate;
    }

    public void setAcceptionDate(DateTime acceptionDate) {
        AcceptionDate = acceptionDate;
    }

    public Boolean getAreFriends() {
        return AreFriends;
    }

    public void setAreFriends(Boolean areFriends) {
        AreFriends = areFriends;
    }

    public Boolean getSender() {
        return IsSender;
    }

    public void setSender(Boolean sender) {
        IsSender = sender;
    }

    public Integer getMutualFriendsCount() {
        return MutualFriendsCount;
    }

    public void setMutualFriendsCount(Integer mutualFriendsCount) {
        MutualFriendsCount = mutualFriendsCount;
    }
}
