package com.raaivan.modules.corenetwork.beans;

import com.raaivan.modules.users.beans.User;
import com.raaivan.util.RVBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class NodeInfo {
    private UUID NodeID;
    private UUID NodeTypeID;
    private List<String> Tags;
    private String Description;
    private User Creator;
    private Integer ContributorsCount;
    private Integer LikesCount;
    private Integer VisitsCount;
    private Integer ExpertsCount;
    private Integer MembersCount;
    private Integer ChildsCount;
    private Integer RelatedNodesCount;
    private Boolean LikeStatus;

    public NodeInfo()
    {
        Tags = new ArrayList<>();
        Creator = RVBeanFactory.getBean(User.class);
    }

    public UUID getNodeID() {
        return NodeID;
    }

    public void setNodeID(UUID nodeID) {
        NodeID = nodeID;
    }

    public UUID getNodeTypeID() {
        return NodeTypeID;
    }

    public void setNodeTypeID(UUID nodeTypeID) {
        NodeTypeID = nodeTypeID;
    }

    public List<String> getTags() {
        return Tags;
    }

    public void setTags(List<String> tags) {
        Tags = tags;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public User getCreator() {
        return Creator;
    }

    public void setCreator(User creator) {
        Creator = creator;
    }

    public Integer getContributorsCount() {
        return ContributorsCount;
    }

    public void setContributorsCount(Integer contributorsCount) {
        ContributorsCount = contributorsCount;
    }

    public Integer getLikesCount() {
        return LikesCount;
    }

    public void setLikesCount(Integer likesCount) {
        LikesCount = likesCount;
    }

    public Integer getVisitsCount() {
        return VisitsCount;
    }

    public void setVisitsCount(Integer visitsCount) {
        VisitsCount = visitsCount;
    }

    public Integer getExpertsCount() {
        return ExpertsCount;
    }

    public void setExpertsCount(Integer expertsCount) {
        ExpertsCount = expertsCount;
    }

    public Integer getMembersCount() {
        return MembersCount;
    }

    public void setMembersCount(Integer membersCount) {
        MembersCount = membersCount;
    }

    public Integer getChildsCount() {
        return ChildsCount;
    }

    public void setChildsCount(Integer childsCount) {
        ChildsCount = childsCount;
    }

    public Integer getRelatedNodesCount() {
        return RelatedNodesCount;
    }

    public void setRelatedNodesCount(Integer relatedNodesCount) {
        RelatedNodesCount = relatedNodesCount;
    }

    public Boolean getLikeStatus() {
        return LikeStatus;
    }

    public void setLikeStatus(Boolean likeStatus) {
        LikeStatus = likeStatus;
    }
}
