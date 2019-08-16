package com.raaivan.modules.corenetwork.beans;

import com.raaivan.modules.users.beans.User;
import com.raaivan.util.RVBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class KnowledgableUser {
    private User User;
    private Double Rank;
    private Boolean Expert;
    private Boolean Contributor;
    private Boolean WikiEditor;
    private Boolean Member;
    private Boolean ExpertOfRelatedNode;
    private Boolean ContributorOfRelatedNode;
    private Boolean MemberOfRelatedNode;

    public KnowledgableUser()
    {
        User = RVBeanFactory.getBean(com.raaivan.modules.users.beans.User.class);
    }

    public com.raaivan.modules.users.beans.User getUser() {
        return User;
    }

    public void setUser(com.raaivan.modules.users.beans.User user) {
        User = user;
    }

    public Double getRank() {
        return Rank;
    }

    public void setRank(Double rank) {
        Rank = rank;
    }

    public Boolean getExpert() {
        return Expert;
    }

    public void setExpert(Boolean expert) {
        Expert = expert;
    }

    public Boolean getContributor() {
        return Contributor;
    }

    public void setContributor(Boolean contributor) {
        Contributor = contributor;
    }

    public Boolean getWikiEditor() {
        return WikiEditor;
    }

    public void setWikiEditor(Boolean wikiEditor) {
        WikiEditor = wikiEditor;
    }

    public Boolean getMember() {
        return Member;
    }

    public void setMember(Boolean member) {
        Member = member;
    }

    public Boolean getExpertOfRelatedNode() {
        return ExpertOfRelatedNode;
    }

    public void setExpertOfRelatedNode(Boolean expertOfRelatedNode) {
        ExpertOfRelatedNode = expertOfRelatedNode;
    }

    public Boolean getContributorOfRelatedNode() {
        return ContributorOfRelatedNode;
    }

    public void setContributorOfRelatedNode(Boolean contributorOfRelatedNode) {
        ContributorOfRelatedNode = contributorOfRelatedNode;
    }

    public Boolean getMemberOfRelatedNode() {
        return MemberOfRelatedNode;
    }

    public void setMemberOfRelatedNode(Boolean memberOfRelatedNode) {
        MemberOfRelatedNode = memberOfRelatedNode;
    }
}
