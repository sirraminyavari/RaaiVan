package com.raaivan.modules.users.beans;

import com.raaivan.util.PublicMethods;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class AdvancedUserSearch {
    private UUID UserID;
    private Double Rank;
    private Integer IsMemberCount;
    private Integer IsExpertCount;
    private Integer IsContributorCount;
    private Integer HasPropertyCount;
    private Integer Resume;

    public UUID getUserID() {
        return UserID;
    }

    public void setUserID(UUID userID) {
        UserID = userID;
    }

    public Double getRank() {
        return Rank;
    }

    public void setRank(Double rank) {
        Rank = rank;
    }

    public Integer getIsMemberCount() {
        return IsMemberCount;
    }

    public void setIsMemberCount(Integer isMemberCount) {
        IsMemberCount = isMemberCount;
    }

    public Integer getIsExpertCount() {
        return IsExpertCount;
    }

    public void setIsExpertCount(Integer isExpertCount) {
        IsExpertCount = isExpertCount;
    }

    public Integer getIsContributorCount() {
        return IsContributorCount;
    }

    public void setIsContributorCount(Integer isContributorCount) {
        IsContributorCount = isContributorCount;
    }

    public Integer getHasPropertyCount() {
        return HasPropertyCount;
    }

    public void setHasPropertyCount(Integer hasPropertyCount) {
        HasPropertyCount = hasPropertyCount;
    }

    public Integer getResume() {
        return Resume;
    }

    public void setResume(Integer resume) {
        Resume = resume;
    }
}
