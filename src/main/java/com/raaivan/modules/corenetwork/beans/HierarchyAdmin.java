package com.raaivan.modules.corenetwork.beans;

import com.raaivan.modules.users.beans.User;
import com.raaivan.util.RVBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class HierarchyAdmin {
    private Node Node;
    private User User;
    private Integer Level;

    public HierarchyAdmin()
    {
        Node = RVBeanFactory.getBean(com.raaivan.modules.corenetwork.beans.Node.class);
        User = RVBeanFactory.getBean(com.raaivan.modules.users.beans.User.class);
    }

    public com.raaivan.modules.corenetwork.beans.Node getNode() {
        return Node;
    }

    public void setNode(com.raaivan.modules.corenetwork.beans.Node node) {
        Node = node;
    }

    public com.raaivan.modules.users.beans.User getUser() {
        return User;
    }

    public void setUser(com.raaivan.modules.users.beans.User user) {
        User = user;
    }

    public Integer getLevel() {
        return Level;
    }

    public void setLevel(Integer level) {
        Level = level;
    }
}
