package com.raaivan.modules.corenetwork.beans;

import com.raaivan.util.RVBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class SimilarNode {
    private Node Suggested;
    private Double Rank;
    private Boolean Tags;
    private Boolean Favorites;
    private Boolean Relations;
    private Boolean Experts;

    public SimilarNode() {
        Suggested = RVBeanFactory.getBean(Node.class);
    }

    public Node getSuggested() {
        return Suggested;
    }

    public void setSuggested(Node suggested) {
        Suggested = suggested;
    }

    public Double getRank() {
        return Rank;
    }

    public void setRank(Double rank) {
        Rank = rank;
    }

    public Boolean getTags() {
        return Tags;
    }

    public void setTags(Boolean tags) {
        Tags = tags;
    }

    public Boolean getFavorites() {
        return Favorites;
    }

    public void setFavorites(Boolean favorites) {
        Favorites = favorites;
    }

    public Boolean getRelations() {
        return Relations;
    }

    public void setRelations(Boolean relations) {
        Relations = relations;
    }

    public Boolean getExperts() {
        return Experts;
    }

    public void setExperts(Boolean experts) {
        Experts = experts;
    }
}
