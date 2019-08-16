package com.raaivan.modules.corenetwork.beans;

import com.raaivan.modules.users.beans.User;
import com.raaivan.util.RVBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class Expert {
    private Node Node;
    private User User;
    private Boolean Approved;
    private Boolean SocialApproved;
    private Integer ReferralsCount;
    private Double ConfirmsPercentage;

    public Expert()
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

    public Boolean getApproved() {
        return Approved;
    }

    public void setApproved(Boolean approved) {
        Approved = approved;
    }

    public Boolean getSocialApproved() {
        return SocialApproved;
    }

    public void setSocialApproved(Boolean socialApproved) {
        SocialApproved = socialApproved;
    }

    public Integer getReferralsCount() {
        return ReferralsCount;
    }

    public void setReferralsCount(Integer referralsCount) {
        ReferralsCount = referralsCount;
    }

    public Double getConfirmsPercentage() {
        return ConfirmsPercentage;
    }

    public void setConfirmsPercentage(Double confirmsPercentage) {
        ConfirmsPercentage = confirmsPercentage;
    }
}
