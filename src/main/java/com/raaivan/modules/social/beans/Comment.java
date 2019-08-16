package com.raaivan.modules.social.beans;

import com.raaivan.modules.users.beans.User;
import com.raaivan.util.PublicMethods;
import org.joda.time.DateTime;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class Comment {
    private UUID CommentID;
    private UUID PostID;
    private String Description;
    private User Sender;
    private DateTime SendDate;
    private Long LikesCount;
    private Long DislikesCount;
    private UUID LastModifierUserID;
    private DateTime LastModificationDate;
    private Boolean LikeStatus; //if true, current user likes the comment, if false, current user dislikes the comment and if null, it's null!

    public UUID getCommentID() {
        return CommentID;
    }

    public void setCommentID(UUID commentID) {
        CommentID = commentID;
    }

    public UUID getPostID() {
        return PostID;
    }

    public void setPostID(UUID postID) {
        PostID = postID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public User getSender() {
        return Sender;
    }

    public void setSender(User sender) {
        Sender = sender;
    }

    public DateTime getSendDate() {
        return SendDate;
    }

    public void setSendDate(DateTime sendDate) {
        SendDate = sendDate;
    }

    public Long getLikesCount() {
        return LikesCount;
    }

    public void setLikesCount(Long likesCount) {
        LikesCount = likesCount;
    }

    public Long getDislikesCount() {
        return DislikesCount;
    }

    public void setDislikesCount(Long dislikesCount) {
        DislikesCount = dislikesCount;
    }

    public UUID getLastModifierUserID() {
        return LastModifierUserID;
    }

    public void setLastModifierUserID(UUID lastModifierUserID) {
        LastModifierUserID = lastModifierUserID;
    }

    public DateTime getLastModificationDate() {
        return LastModificationDate;
    }

    public void setLastModificationDate(DateTime lastModificationDate) {
        LastModificationDate = lastModificationDate;
    }

    public Boolean getLikeStatus() {
        return LikeStatus;
    }

    public void setLikeStatus(Boolean likeStatus) {
        LikeStatus = likeStatus;
    }
}
