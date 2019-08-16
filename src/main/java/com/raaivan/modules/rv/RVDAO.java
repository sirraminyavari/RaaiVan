package com.raaivan.modules.rv;

import com.raaivan.util.dbutil.RVConnection;
import com.raaivan.util.dbutil.RVStructuredParam;
import com.raaivan.modules.rv.beans.DeletedState;
import com.raaivan.modules.rv.beans.EmailQueueItem;
import com.raaivan.modules.rv.beans.TaggedItem;
import com.raaivan.modules.rv.beans.Variable;
import com.raaivan.util.PublicMethods;
import com.raaivan.util.rvsettings.RVSettingsItem;
import com.raaivan.modules.rv.enums.TaggedItemType;
import com.raaivan.util.RVJSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@ApplicationScope
public class RVDAO {
    private RVConnection rvConnection;
    private RVParsers parser;
    private PublicMethods publicMethods;

    @Autowired
    public void _setDependencies(RVConnection rvConnection, RVParsers parser, PublicMethods publicMethods) {
        if (this.rvConnection == null) this.rvConnection = rvConnection;
        if (this.parser == null) this.parser = parser;
        if (this.publicMethods == null) this.publicMethods = publicMethods;
    }

    private String getQualifiedName(String name){
        return "[dbo].[RV_" + name + "]";
    }

    public String getSystemVersion()
    {
        return rvConnection.getString(getQualifiedName("GetSystemVersion"));
    }

    public List<UUID> getApplicationIDs()
    {
        return rvConnection.getUUIDList(getQualifiedName("GetApplicationIDs"));
    }

    public boolean setApplications(List<SimpleEntry<UUID, String>> applications)
    {
        RVStructuredParam itemsDT = new RVStructuredParam("GuidStringTableType")
                .addColumnMetaData("FirstValue", UUID.class)
                .addColumnMetaData("SecondValue", String.class);
        applications.forEach(u -> { itemsDT.addRow(u.getKey(), u.getValue()); });

        return rvConnection.succeed(getQualifiedName("SetApplications"), itemsDT);
    }

    public boolean setVariable(UUID applicationId, String name, String value, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("SetVariable"), applicationId, name, value, currentUserId, publicMethods.now());
    }

    public String getVariable(UUID applicationId, String name)
    {
        return rvConnection.getString(getQualifiedName("GetVariable"), applicationId, name);
    }

    private Long _setOwnerVariable(UUID applicationId, Long id, UUID ownerId,
                                 String name, String value, UUID currentUserId)
    {
        long ret = rvConnection.getLong(getQualifiedName("SetOwnerVariable"),
                applicationId, id, ownerId, name, value, currentUserId, publicMethods.now());
        return ret <= 0 ? null : ret;
    }

    public Long setOwnerVariable(UUID applicationId, Long id, String name, String value, UUID currentUserId)
    {
        return _setOwnerVariable(applicationId, id, null, name, value, currentUserId);
    }

    public Long setOwnerVariable(UUID applicationId, UUID ownerId, String name, String value, UUID currentUserId)
    {
        return _setOwnerVariable(applicationId, null, ownerId, name, value, currentUserId);
    }

    private List<Variable> _getOwnerVariables(UUID applicationId,
                                            Long id, UUID ownerId, String name, UUID creatorUserId)
    {
        return parser.variables(rvConnection.read(getQualifiedName("GetOwnerVariables"),
                applicationId, id, ownerId, name, creatorUserId));
    }

    public List<Variable> getOwnerVariables(UUID applicationId, UUID ownerId, String name, UUID creatorUserId)
    {
        return _getOwnerVariables(applicationId, null, ownerId, name, creatorUserId);
    }

    public Variable getOwnerVariable(UUID applicationId, Long id)
    {
        List<Variable> lst = _getOwnerVariables(applicationId, id, null, null, null);
        return lst.size() == 0 ? null : lst.get(0);
    }

    public boolean removeOwnerVariable(UUID applicationId, long id, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("RemoveOwnerVariable"),
                applicationId, id, currentUserId, publicMethods.now());
    }

    public boolean addEmailsToQueue(UUID applicationId, List<EmailQueueItem> items)
    {
        RVStructuredParam itemsDT = new RVStructuredParam("EmailQueueItemTableType")
                .addColumnMetaData("ID", long.class)
                .addColumnMetaData("SenderUserID", UUID.class)
                .addColumnMetaData("Action", String.class)
                .addColumnMetaData("Email", String.class)
                .addColumnMetaData("Title", String.class)
                .addColumnMetaData("EmailBody", String.class);
        items.forEach(u -> {
            itemsDT.addRow(u.getID(), u.getSenderUserID(), u.getAction().toString(), u.getEmail(), u.getTitle(), u.getEmailBody());
        });

        return rvConnection.succeed(getQualifiedName("AddEmailsToQueue"), applicationId, itemsDT);
    }

    public List<EmailQueueItem> getEmailQueueItems(UUID applicationId, Integer count)
    {
        return parser.emailQueueItems(rvConnection.read(getQualifiedName("GetEmailQueueItems"), applicationId, count));
    }

    public boolean archiveEmailQueueItems(UUID applicationId, List<Long> itemIds)
    {
        RVStructuredParam idsDT = new RVStructuredParam("BigIntTableType")
                .addColumnMetaData("Value", long.class);
        itemIds.forEach(idsDT::addRow);

        return rvConnection.succeed(getQualifiedName("ArchiveEmailQueueItems"), applicationId, idsDT, publicMethods.now());
    }

    public List<DeletedState> getDeletedStates(UUID applicationId, Integer count, Long lowerBoundary)
    {
        return parser.deletedStates(rvConnection.read(getQualifiedName("GetDeletedStates"), applicationId, count, lowerBoundary));
    }

    public List<SimpleEntry<String, UUID>> getGuids(UUID applicationId,
                                List<String> ids, String type, Boolean exist, Boolean createIfNotExist)
    {
        RVStructuredParam idsDT = new RVStructuredParam("StringTableType")
                .addColumnMetaData("Value", String.class);
        ids.forEach(idsDT::addRow);

        return parser.guids(rvConnection.read(getQualifiedName("GetGuids"), applicationId, idsDT, type, exist, createIfNotExist));
    }

    public boolean saveTaggedItems(UUID applicationId, List<TaggedItem> items,
                                   Boolean removeOldTags, UUID currentUserId)
    {
        if (items == null || items.size() == 0 || currentUserId == null) return false;

        RVStructuredParam taggedDT = new RVStructuredParam("TaggedItemTableType")
                .addColumnMetaData("ContextID", UUID.class)
                .addColumnMetaData("TaggedID", UUID.class)
                .addColumnMetaData("ContextType", String.class)
                .addColumnMetaData("TaggedType", String.class);
        items.forEach(u -> taggedDT.addRow(u.getContextID(), u.getTaggedID(), u.getContextType(), u.getTaggedType()));

        return rvConnection.succeed(getQualifiedName("SaveTaggedItems"), applicationId, taggedDT, removeOldTags, currentUserId);
    }

    public void saveTaggedItemsOffline(UUID applicationId, List<TaggedItem> items,
                                   Boolean removeOldTags, UUID currentUserId)
    {
        publicMethods.setTimeout(() -> {
            saveTaggedItems(applicationId, items, removeOldTags, currentUserId);
        }, 0);
    }

    public List<TaggedItem> getTaggedItems(UUID applicationId, UUID contextId, List<TaggedItemType> taggedTypes)
    {
        RVStructuredParam typesDT = new RVStructuredParam("StringTableType")
                .addColumnMetaData("Value", String.class);
        taggedTypes.forEach(u -> { if(u != TaggedItemType.None) typesDT.addRow(u.toString());});

        return parser.taggedItems(rvConnection.read(getQualifiedName("GetTaggedItems"), applicationId, contextId, typesDT));
    }

    public List<TaggedItem> getTaggedItems(UUID applicationId, UUID contextId, TaggedItemType taggedType) {
        List<TaggedItemType> types = new ArrayList<TaggedItemType>() {{
            add(taggedType);
        }};

        return getTaggedItems(applicationId, contextId, types);
    }

    public boolean isSystemAdmin(UUID applicationId, UUID userId)
    {
        return rvConnection.succeed(getQualifiedName("IsSystemAdmin"), applicationId, userId);
    }

    public String getFileExtension(UUID applicationId, UUID fileId)
    {
        return rvConnection.getString(getQualifiedName("GetFileExtension"), applicationId, fileId);
    }

    private boolean _likeDislikeUnlike(UUID applicationId, UUID userId, UUID likedId, Boolean like)
    {
        return rvConnection.succeed(getQualifiedName("LikeDislikeUnlike"), applicationId, userId, likedId, like, publicMethods.now());
    }

    public boolean like(UUID applicationId, UUID userId, UUID likedId)
    {
        return _likeDislikeUnlike(applicationId, userId, likedId, true);
    }

    public boolean dislike(UUID applicationId, UUID userId, UUID likedId)
    {
        return _likeDislikeUnlike(applicationId, userId, likedId, false);
    }

    public boolean unlike(UUID applicationId, UUID userId, UUID likedId)
    {
        return _likeDislikeUnlike(applicationId, userId, likedId, null);
    }

    public List<UUID> getFanIDs(UUID applicationId, UUID likedId)
    {
        return rvConnection.getUUIDList(getQualifiedName("GetFanIDs"), applicationId, likedId);
    }

    private boolean _followUnfollow(UUID applicationId, UUID userId, UUID followedId, Boolean follow)
    {
        return rvConnection.succeed(getQualifiedName("FollowUnfollow"), applicationId, userId, followedId, follow, publicMethods.now());
    }

    public boolean follow(UUID applicationId, UUID userId, UUID followedId)
    {
        return _followUnfollow(applicationId, userId, followedId, true);
    }

    public boolean unfollow(UUID applicationId, UUID userId, UUID followedId)
    {
        return _followUnfollow(applicationId, userId, followedId, null);
    }

    public boolean setSystemSettings(UUID applicationId, Map<RVSettingsItem, String> items, UUID currentUserId)
    {
        RVStructuredParam itemsDT = new RVStructuredParam("StringPairTableType")
                .addColumnMetaData("FirstValue", String.class)
                .addColumnMetaData("SecondValue", String.class);
        items.forEach((k, v) -> itemsDT.addRow(k.toString(), v));

        return rvConnection.succeed(getQualifiedName("SetSystemSettings"), applicationId, itemsDT, currentUserId, publicMethods.now());
    }

    public Map<RVSettingsItem, String> getSystemSettings(UUID applicationId, List<RVSettingsItem> names)
    {
        return parser.settingItems(rvConnection.read(getQualifiedName("GetSystemSettings"), applicationId,
                names.stream().map(RVSettingsItem::toString).collect(Collectors.joining(",")), ','));
    }

    public List<RVJSON> getLastContentCreators(UUID applicationId, Integer count)
    {
        return parser.lastActiveUsers(rvConnection.read(getQualifiedName("GetLastContentCreators"), applicationId, count));
    }

    public Map<String, Integer> raaivanStatistics(UUID applicationId)
    {
        return parser.raaivanStatistics(rvConnection.read(getQualifiedName("RaaiVanStatistics"), applicationId));
    }
}
