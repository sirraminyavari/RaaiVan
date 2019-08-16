package com.raaivan.modules.corenetwork.beans;

import com.raaivan.modules.corenetwork.enums.NodeMemberStatus;
import com.raaivan.modules.users.beans.User;
import com.raaivan.util.RVBeanFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class NodeMember {
    private Node Node;
    private User Member;
    private DateTime MembershipDate;
    private Boolean IsAdmin;
    private Boolean IsPending;
    private NodeMemberStatus Status;
    private DateTime AcceptionDate;

    public NodeMember()
    {
        Node = RVBeanFactory.getBean(com.raaivan.modules.corenetwork.beans.Node.class);
        Member = RVBeanFactory.getBean(User.class);
        Status = NodeMemberStatus.NotSet;
    }

    public com.raaivan.modules.corenetwork.beans.Node getNode() {
        return Node;
    }

    public void setNode(com.raaivan.modules.corenetwork.beans.Node node) {
        Node = node;
    }

    public User getMember() {
        return Member;
    }

    public void setMember(User member) {
        Member = member;
    }

    public DateTime getMembershipDate() {
        return MembershipDate;
    }

    public void setMembershipDate(DateTime membershipDate) {
        MembershipDate = membershipDate;
    }

    public Boolean getAdmin() {
        return IsAdmin;
    }

    public void setAdmin(Boolean admin) {
        IsAdmin = admin;
    }

    public Boolean getPending() {
        return IsPending;
    }

    public void setPending(Boolean pending) {
        IsPending = pending;
    }

    public NodeMemberStatus getStatus() {
        return Status;
    }

    public void setStatus(NodeMemberStatus status) {
        Status = status;
    }

    public DateTime getAcceptionDate() {
        return AcceptionDate;
    }

    public void setAcceptionDate(DateTime acceptionDate) {
        AcceptionDate = acceptionDate;
    }
}
