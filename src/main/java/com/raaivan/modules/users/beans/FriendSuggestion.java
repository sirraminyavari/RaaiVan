package com.raaivan.modules.users.beans;

import com.raaivan.util.RVBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class FriendSuggestion {
    private User User;
    private Integer MutualFriends;

    public FriendSuggestion() {
        User = RVBeanFactory.getBean(User.class);
    }

    public User getUser() {
        return User;
    }

    public void setUser(User user) {
        User = user;
    }

    public Integer getMutualFriends() {
        return MutualFriends;
    }

    public void setMutualFriends(Integer mutualFriends) {
        MutualFriends = mutualFriends;
    }
}