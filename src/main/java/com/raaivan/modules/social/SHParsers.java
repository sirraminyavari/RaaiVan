package com.raaivan.modules.social;

import com.raaivan.util.dbutil.RVResultSet;
import com.raaivan.modules.social.beans.Comment;
import com.raaivan.modules.social.beans.Post;
import com.raaivan.modules.social.enums.PostOwnerType;
import com.raaivan.modules.social.enums.PostPrivacyType;
import com.raaivan.util.PublicMethods;
import com.raaivan.util.RVBeanFactory;
import org.apache.commons.lang3.mutable.MutableLong;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@ApplicationScope
public class SHParsers {
    private PublicMethods publicMethods;

    @Autowired
    public void _setDependencies(PublicMethods publicMethods) {
        if (this.publicMethods == null) this.publicMethods = publicMethods;
    }

    public List<Post> posts(RVResultSet resultSet)
    {
        List<Post> ret = new ArrayList<>();

        for(int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i){
            try {
                Post e = RVBeanFactory.getBean(Post.class);

                e.setPostID(publicMethods.parseUUID((String) resultSet.getValue(i, "PostID")));
                e.setRefPostID(publicMethods.parseUUID((String) resultSet.getValue(i, "RefPostID")));
                e.setDescription((String) resultSet.getValue(i, "Description"));
                e.setOriginalDescription((String) resultSet.getValue(i, "OriginalDescription"));
                e.setSharedObjectID(publicMethods.parseUUID((String) resultSet.getValue(i, "SharedObjectID")));
                e.getSender().setUserID(publicMethods.parseUUID((String) resultSet.getValue(i, "SenderUserID")));
                e.getSender().setFirstName((String) resultSet.getValue(i, "FirstName"));
                e.getSender().setLastName((String) resultSet.getValue(i, "LastName"));
                e.setSendDate((DateTime) resultSet.getValue(i, "SendDate"));
                e.getOriginalSender().setUserID(publicMethods.parseUUID((String) resultSet.getValue(i, "OriginalSenderUserID")));
                e.getOriginalSender().setFirstName((String) resultSet.getValue(i, "OriginalFirstName"));
                e.getOriginalSender().setLastName((String) resultSet.getValue(i, "OriginalLastName"));
                e.setOriginalSendDate((DateTime) resultSet.getValue(i, "OriginalSendDate"));
                e.setLastModificationDate((DateTime) resultSet.getValue(i, "LastModificationDate"));
                e.setOwnerID(publicMethods.parseUUID((String) resultSet.getValue(i, "OwnerID")));
                e.setOwnerType(publicMethods.lookupEnum(PostOwnerType.class,
                        (String) resultSet.getValue(i, "OwnerType"), PostOwnerType.None));
                e.setPrivacy(publicMethods.lookupEnum(PostPrivacyType.class,
                        (String) resultSet.getValue(i, "Privacy"), PostPrivacyType.None));
                e.setCommentsCount((Long) resultSet.getValue(i, "CommentsCount"));
                e.setLikesCount((Long) resultSet.getValue(i, "LikesCount"));
                e.setDislikesCount((Long) resultSet.getValue(i, "DislikesCount"));
                e.setLikeStatus((Boolean) resultSet.getValue(i, "LikeStatus"));
                e.setHasPicture((Boolean) resultSet.getValue(i, "HasPicture"));

                ret.add(e);
            }
            catch (Exception e){
            }
        }

        return ret;
    }

    public List<Comment> comments(RVResultSet resultSet)
    {
        List<Comment> ret = new ArrayList<>();

        for(int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i){
            try {
                Comment e = RVBeanFactory.getBean(Comment.class);

                e.setCommentID(publicMethods.parseUUID((String) resultSet.getValue(i, "CommentID")));
                e.setPostID(publicMethods.parseUUID((String) resultSet.getValue(i, "PostID")));
                e.setDescription((String) resultSet.getValue(i, "Description"));
                e.getSender().setUserID(publicMethods.parseUUID((String) resultSet.getValue(i, "SenderUserID")));
                e.getSender().setFirstName((String) resultSet.getValue(i, "FirstName"));
                e.getSender().setLastName((String) resultSet.getValue(i, "LastName"));
                e.setSendDate((DateTime) resultSet.getValue(i, "SendDate"));
                e.setLikesCount((Long) resultSet.getValue(i, "LikesCount"));
                e.setDislikesCount((Long) resultSet.getValue(i, "DislikesCount"));
                e.setLikeStatus((Boolean) resultSet.getValue(i, "LikeStatus"));

                ret.add(e);
            }
            catch (Exception e){
            }
        }

        return ret;
    }

    public List<UUID> fanUserIDs(RVResultSet resultSet, MutableLong totalCount)
    {
        totalCount.setValue(0);

        List<UUID> ret = new ArrayList<>();

        for(int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i){
            try {
                UUID uId = publicMethods.parseUUID((String) resultSet.getValue(i, "UserID"));
                if(uId != null) ret.add(uId);

                totalCount.setValue((Long) resultSet.getValue(i, "TotalCount"));
            }
            catch (Exception e){
            }
        }

        return ret;
    }
}
