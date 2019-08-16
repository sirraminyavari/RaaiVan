package com.raaivan.modules.social;

import com.raaivan.util.dbutil.RVConnection;
import com.raaivan.modules.users.UsersDAO;
import com.raaivan.modules.social.beans.Comment;
import com.raaivan.modules.social.beans.Post;
import com.raaivan.modules.social.enums.PostOwnerType;
import com.raaivan.modules.social.enums.PostPrivacyType;
import com.raaivan.modules.users.beans.User;
import com.raaivan.util.PublicMethods;
import org.apache.commons.lang3.mutable.MutableLong;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@ApplicationScope
public class SHDAO {
    private RVConnection rvConnection;
    private SHParsers parser;
    private PublicMethods publicMethods;
    private UsersDAO usersDao;

    @Autowired
    public void _setDependencies(RVConnection rvConnection, SHParsers parser, PublicMethods publicMethods, UsersDAO usersDao) {
        if (this.rvConnection == null) this.rvConnection = rvConnection;
        if (this.parser == null) this.parser = parser;
        if (this.publicMethods == null) this.publicMethods = publicMethods;
        if (this.usersDao == null) this.usersDao = usersDao;
    }

    private String getQualifiedName(String name) {
        return "[dbo].[SH_" + name + "]";
    }

    public boolean addPost(UUID applicationId, UUID postId, UUID ownerId, String description,
                           UUID senderUserId, PostOwnerType ownerType, Boolean hasPicture, PostPrivacyType privacy) {
        if (ownerType == PostOwnerType.None) return false;
        if (privacy == PostPrivacyType.None) privacy = PostPrivacyType.Public;

        return rvConnection.succeed(getQualifiedName("AddPost"), applicationId, postId, 1, description, null,
                senderUserId, publicMethods.now(), ownerId, ownerType.toString(), hasPicture, privacy.toString());
    }

    public boolean updatePost(UUID applicationId, UUID postId, String description, UUID currentUserId) {
        return rvConnection.succeed(getQualifiedName("UpdatePost"),
                applicationId, postId, description, currentUserId, publicMethods.now());
    }

    public boolean removePost(UUID applicationId, UUID postID) {
        return rvConnection.succeed(getQualifiedName("ArithmeticDeletePost"), applicationId, postID);
    }

    public List<Post> getPosts(UUID applicationId, UUID ownerID, UUID userId,
                               Boolean news, DateTime maxDate, DateTime minDate, Integer count) {
        return parser.posts(rvConnection.read(getQualifiedName("GetPosts"),
                applicationId, ownerID, userId, news, maxDate, minDate, count));
    }

    public List<Post> getPosts(UUID applicationId, List<UUID> postIds, UUID userId) {
        return parser.posts(rvConnection.read(getQualifiedName("GetPostsByIDs"), applicationId,
                postIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',', userId));
    }

    public Post getPost(UUID applicationId, UUID postId, UUID userId) {
        List<UUID> ids = new ArrayList<UUID>() {{
            add(postId);
        }};

        List<Post> posts = getPosts(applicationId, ids, userId);

        return posts.size() == 0 ? null : posts.get(0);
    }

    public UUID getPostOwnerID(UUID applicationId, UUID postIdOrCommentId) {
        return rvConnection.getUUID(getQualifiedName("GetPostOwnerID"), applicationId, postIdOrCommentId);
    }

    public UUID getPostSenderID(UUID applicationId, UUID postIdOrCommentId) {
        return rvConnection.getUUID(getQualifiedName("GetPostSenderID"), applicationId, postIdOrCommentId);
    }

    public boolean Share(UUID applicationId, UUID postId, UUID refPostId, UUID ownerId,
                         String description, UUID senderUserId, PostOwnerType ownerType, PostPrivacyType privacy) {
        if (ownerType == PostOwnerType.None) return false;
        if (privacy == PostPrivacyType.None) privacy = PostPrivacyType.Public;

        return rvConnection.succeed(getQualifiedName("Share"), applicationId, postId, refPostId,
                ownerId, description, senderUserId, publicMethods.now(), ownerType.toString(), privacy.toString());
    }

    public boolean addComment(UUID applicationId, UUID commentId, UUID postId, String description, UUID senderUserId) {
        return rvConnection.succeed(getQualifiedName("AddComment"),
                applicationId, commentId, postId, description, senderUserId, publicMethods.now());
    }

    public boolean updateComment(UUID applicationId, UUID commentId, String description, UUID currentUserId) {
        return rvConnection.succeed(getQualifiedName("UpdateComment"),
                applicationId, commentId, description, currentUserId, publicMethods.now());
    }

    public boolean removeComment(UUID applicationId, UUID commentId) {
        return rvConnection.succeed(getQualifiedName("ArithmeticDeleteComment"), applicationId, commentId);
    }

    public List<Comment> getPostComments(UUID applicationId, List<UUID> postIds, UUID userId) {
        return parser.comments(rvConnection.read(getQualifiedName("GetComments"), applicationId,
                postIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',', userId));
    }

    public List<Comment> getPostComments(UUID applicationId, UUID postId, UUID userId) {
        List<UUID> ids = new ArrayList<UUID>() {{
            add(postId);
        }};

        return getPostComments(applicationId, ids, userId);
    }

    public List<Comment> getComments(UUID applicationId, List<UUID> commentIds, UUID userId) {
        return parser.comments(rvConnection.read(getQualifiedName("GetCommentsByIDs"), applicationId,
                commentIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',', userId));
    }

    public Comment getComment(UUID applicationId, UUID commentId, UUID userId) {
        List<UUID> ids = new ArrayList<UUID>() {{
            add(commentId);
        }};

        List<Comment> comments = getComments(applicationId, ids, userId);

        return comments.size() == 0 ? null : comments.get(0);
    }

    public UUID getCommentSenderID(UUID applicationId, UUID commentId) {
        return rvConnection.getUUID(getQualifiedName("GetCommentSenderID"), applicationId, commentId);
    }

    public List<UUID> getCommentSenderIDs(UUID applicationId, UUID postId) {
        return rvConnection.getUUIDList(getQualifiedName("GetCommentSenderIDs"), applicationId, postId);
    }

    private boolean _likeDislikePost(UUID applicationId, UUID postId, UUID userId, boolean like) {
        return rvConnection.succeed(getQualifiedName("LikeDislikePost"),
                applicationId, postId, userId, like, 0, publicMethods.now());
    }

    public boolean likePost(UUID applicationId, UUID postId, UUID userId) {
        return _likeDislikePost(applicationId, postId, userId, true);
    }

    public boolean dislikePost(UUID applicationId, UUID postId, UUID userId) {
        return _likeDislikePost(applicationId, postId, userId, false);
    }

    public boolean unlikePost(UUID applicationId, UUID postId, UUID userId) {
        return rvConnection.succeed(getQualifiedName("UnlikePost"), applicationId, postId, userId);
    }

    public List<UUID> getPostFanIDs(UUID applicationId, UUID postId, boolean likeStatus,
                                    Integer count, Long lowerBoundary, MutableLong totalCount) {
        return parser.fanUserIDs(rvConnection.read(getQualifiedName("GetPostFanIDs"),
                applicationId, postId, likeStatus, count, lowerBoundary), totalCount);
    }

    public List<UUID> getPostFanIDs(UUID applicationId, UUID postId) {
        MutableLong totalCount = new MutableLong(0);
        return  getPostFanIDs(applicationId, postId, true, null, null, totalCount);
    }

    public List<User> getPostFans(UUID applicationId, UUID postId, boolean likeStatus,
                                  Integer count, Long lowerBoundary, MutableLong totalCount) {
        return usersDao.getUsers(applicationId,
                getPostFanIDs(applicationId, postId, likeStatus, count, lowerBoundary, totalCount));
    }

    public List<User> getPostFans(UUID applicationId, UUID postId) {
        return usersDao.getUsers(applicationId, getPostFanIDs(applicationId, postId));
    }

    public List<UUID> getCommentFanIDs(UUID applicationId, UUID commentId, boolean likeStatus,
                                       Integer count, Long lowerBoundary, MutableLong totalCount) {
        return parser.fanUserIDs(rvConnection.read(getQualifiedName("GetCommentFanIDs"),
                applicationId, commentId, likeStatus, count, lowerBoundary), totalCount);
    }

    public List<UUID> getCommentFanIDs(UUID applicationId, UUID commentId) {
        MutableLong totalCount = new MutableLong(0);
        return getCommentFanIDs(applicationId, commentId, true, null, null, totalCount);
    }

    public List<User> getCommentFans(UUID applicationId, UUID commentId, boolean likeStatus,
                                       Integer count, Long lowerBoundary, MutableLong totalCount) {
        return usersDao.getUsers(applicationId,
                getCommentFanIDs(applicationId, commentId, likeStatus, count, lowerBoundary, totalCount));
    }

    public List<User> getCommentFans(UUID applicationId, UUID commentId) {
        return usersDao.getUsers(applicationId, getCommentFanIDs(applicationId, commentId));
    }

    private boolean _likeDislikeComment(UUID applicationId, UUID commentId, UUID userId, boolean like) {
        return rvConnection.succeed(getQualifiedName("LikeDislikeComment"),
                applicationId, commentId, userId, like, 0, publicMethods.now());
    }

    public boolean likeComment(UUID applicationId, UUID commentId, UUID userId) {
        return _likeDislikeComment(applicationId, commentId, userId, true);
    }

    public boolean dislikeComment(UUID applicationId, UUID commentId, UUID userId) {
        return _likeDislikeComment(applicationId, commentId, userId, false);
    }

    public boolean unlikeComment(UUID applicationId, UUID commentId, UUID userId) {
        return rvConnection.succeed(getQualifiedName("UnlikeComment"), applicationId, commentId, userId);
    }

    public long getPostsCount(UUID applicationId, UUID ownerId, UUID senderUserId) {
        return rvConnection.getLong(getQualifiedName("GetPostsCount"), applicationId, ownerId, senderUserId);
    }

    public long getSharesCount(UUID applicationId, UUID postId) {
        return rvConnection.getLong(getQualifiedName("GetSharesCount"), applicationId, postId);
    }

    public long getCommentsCount(UUID applicationId, UUID postId, UUID senderUserId) {
        return rvConnection.getLong(getQualifiedName("GetCommentsCount"), applicationId, postId, senderUserId);
    }

    public long getUserPostsCount(UUID applicationId, UUID userId) {
        return rvConnection.getLong(getQualifiedName("GetUserPostsCount"), applicationId, userId, 1);
    }

    private long _getPostLikesDislikesCount(UUID applicationId, UUID postId, boolean like) {
        return rvConnection.getLong(getQualifiedName("GetPostLikesDislikesCount"), applicationId, postId, like);
    }

    public long getPostLikesCount(UUID applicationId, UUID postId) {
        return _getPostLikesDislikesCount(applicationId, postId, true);
    }

    public long getPostDislikesCount(UUID applicationId, UUID postId) {
        return _getPostLikesDislikesCount(applicationId, postId, false);
    }

    private long _getCommentLikesDislikesCount(UUID applicationId, UUID commentId, boolean like) {
        return rvConnection.getLong(getQualifiedName("GetCommentLikesDislikesCount"), applicationId, commentId, like);
    }

    public long getCommentLikesCount(UUID applicationId, UUID commentId) {
        return _getCommentLikesDislikesCount(applicationId, commentId, true);
    }

    public long getCommentDislikesCount(UUID applicationId, UUID commentId) {
        return _getCommentLikesDislikesCount(applicationId, commentId, false);
    }
}
