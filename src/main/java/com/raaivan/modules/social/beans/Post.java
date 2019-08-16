package com.raaivan.modules.social.beans;

import com.raaivan.modules.social.enums.PostOwnerType;
import com.raaivan.modules.social.enums.PostPrivacyType;
import com.raaivan.modules.users.beans.User;
import com.raaivan.util.PublicMethods;
import org.joda.time.DateTime;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class Post {
    private UUID PostID;
    private UUID RefPostID;
    private String Description;
    private String OriginalDescription;
    private UUID SharedObjectID;
    private User Sender;
    private DateTime SendDate;
    private User OriginalSender;
    private DateTime OriginalSendDate;
    private UUID LastModifierUserID;
    private DateTime LastModificationDate;
    private UUID OwnerID;
    private PostOwnerType OwnerType;
    private String OwnerTitle;
    private PostPrivacyType Privacy;
    private Long CommentsCount;
    private Long LikesCount;
    private Long DislikesCount;
    private Boolean LikeStatus; //if true, current user likes the post, if false, current user dislikes the post and if null, it's null!
    private Boolean HasPicture;
    private List<Comment> Comments;

    public Post(){
        OwnerType = PostOwnerType.None;
        Privacy = PostPrivacyType.None;
        Comments = new ArrayList<>();
    }

    public UUID getPostID() {
        return PostID;
    }

    public void setPostID(UUID postID) {
        PostID = postID;
    }

    public UUID getRefPostID() {
        return RefPostID;
    }

    public void setRefPostID(UUID refPostID) {
        RefPostID = refPostID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getOriginalDescription() {
        return OriginalDescription;
    }

    public void setOriginalDescription(String originalDescription) {
        OriginalDescription = originalDescription;
    }

    public UUID getSharedObjectID() {
        return SharedObjectID;
    }

    public void setSharedObjectID(UUID sharedObjectID) {
        SharedObjectID = sharedObjectID;
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

    public User getOriginalSender() {
        return OriginalSender;
    }

    public void setOriginalSender(User originalSender) {
        OriginalSender = originalSender;
    }

    public DateTime getOriginalSendDate() {
        return OriginalSendDate;
    }

    public void setOriginalSendDate(DateTime originalSendDate) {
        OriginalSendDate = originalSendDate;
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

    public UUID getOwnerID() {
        return OwnerID;
    }

    public void setOwnerID(UUID ownerID) {
        OwnerID = ownerID;
    }

    public PostOwnerType getOwnerType() {
        return OwnerType;
    }

    public void setOwnerType(PostOwnerType ownerType) {
        OwnerType = ownerType;
    }

    public String getOwnerTitle() {
        return OwnerTitle;
    }

    public void setOwnerTitle(String ownerTitle) {
        OwnerTitle = ownerTitle;
    }

    public PostPrivacyType getPrivacy() {
        return Privacy;
    }

    public void setPrivacy(PostPrivacyType privacy) {
        Privacy = privacy;
    }

    public Long getCommentsCount() {
        return CommentsCount;
    }

    public void setCommentsCount(Long commentsCount) {
        CommentsCount = commentsCount;
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

    public Boolean getLikeStatus() {
        return LikeStatus;
    }

    public void setLikeStatus(Boolean likeStatus) {
        LikeStatus = likeStatus;
    }

    public Boolean getHasPicture() {
        return HasPicture;
    }

    public void setHasPicture(Boolean hasPicture) {
        HasPicture = hasPicture;
    }

    public List<Comment> getComments() {
        return Comments;
    }

    public void setComments(List<Comment> comments) {
        Comments = comments;
    }
}
