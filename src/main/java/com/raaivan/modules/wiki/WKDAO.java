package com.raaivan.modules.wiki;

import com.raaivan.util.dbutil.RVConnection;
import com.raaivan.util.dbutil.RVStructuredParam;
import com.raaivan.modules.users.UsersDAO;
import com.raaivan.modules.rv.beans.Dashboard;
import com.raaivan.modules.rv.beans.MutableUUID;
import com.raaivan.modules.users.beans.User;
import com.raaivan.modules.wiki.beans.Change;
import com.raaivan.modules.wiki.beans.Paragraph;
import com.raaivan.modules.wiki.beans.WikiTitle;
import com.raaivan.modules.wiki.enums.WikiOwnerType;
import com.raaivan.modules.wiki.enums.WikiStatuses;
import com.raaivan.util.PublicMethods;
import io.micrometer.core.instrument.util.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.stream.Collectors;

@Service
@ApplicationScope
public class WKDAO {
    private RVConnection rvConnection;
    private WKParsers parser;
    private PublicMethods publicMethods;
    private UsersDAO usersDao;

    @Autowired
    public void _setDependencies(RVConnection rvConnection, WKParsers parser, PublicMethods publicMethods, UsersDAO usersDao) {
        if (this.rvConnection == null) this.rvConnection = rvConnection;
        if (this.parser == null) this.parser = parser;
        if (this.publicMethods == null) this.publicMethods = publicMethods;
        if (this.usersDao == null) this.usersDao = usersDao;
    }

    private String getQualifiedName(String name){
        return "[dbo].[WK_" + name + "]";
    }

    public boolean addTitle(UUID applicationId, UUID titleId, UUID ownerId, String title, Integer sequenceNumber,
                            WikiOwnerType ownerType, UUID currentUserId, Boolean accept)
    {
        return rvConnection.succeed(getQualifiedName("AddTitle"), applicationId, titleId, ownerId, title, sequenceNumber,
                currentUserId, publicMethods.now(), (ownerType == WikiOwnerType.NotSet ? null : ownerType.toString()), accept);
    }

    public boolean modifyTitle(UUID applicationId, UUID titleId, String title, UUID currentUserId, Boolean accept)
    {
        return rvConnection.succeed(getQualifiedName("ModifyTitle"),
                applicationId, titleId, title, currentUserId, publicMethods.now(), accept);
    }

    public boolean removeTitle(UUID applicationId, UUID titleId, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("ArithmeticDeleteTitle"),
                applicationId, titleId, currentUserId, publicMethods.now());
    }

    public boolean recycleTitle(UUID applicationId, UUID titleId, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("RecycleTitle"),
                applicationId, titleId, currentUserId, publicMethods.now());
    }

    public boolean setTitlesOrder(UUID applicationId, List<UUID> titleIds)
    {
        return rvConnection.succeed(getQualifiedName("SetTitlesOrder"), applicationId,
                titleIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',');
    }

    public List<WikiTitle> getTitles(UUID applicationId, UUID ownerId, Boolean isAdmin, UUID currentUserId, boolean deleted)
    {
        return parser.titles(rvConnection.read(getQualifiedName("GetTitles"), applicationId, ownerId, isAdmin, currentUserId, deleted));
    }

    public List<WikiTitle> getTitles(UUID applicationId, List<UUID> titleIds, UUID currentUserId)
    {
        return parser.titles(rvConnection.read(getQualifiedName("GetTitlesByIDs"), applicationId,
                titleIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',', currentUserId));
    }

    public boolean hasTitle(UUID applicationId, UUID ownerId, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("HasTitle"), applicationId, ownerId, currentUserId);
    }

    public boolean addParagraph(UUID applicationId, UUID paragraphId, UUID titleId, String title, String bodyText,
                                Integer sequenceNumber, UUID currentUserId, Boolean isRichText, Boolean sendToAdmins,
                                Boolean hasAdmin, List<UUID> adminUserIds, List<Dashboard> retDashboards)
    {
        RVStructuredParam adminIdsDT = new RVStructuredParam("GuidTableType")
                .addColumnMetaData("Value", UUID.class);
        adminUserIds.stream().distinct().forEach(adminIdsDT::addRow);

        if(StringUtils.isBlank(bodyText)) bodyText = "";

        return rvConnection.getDashboards(retDashboards, getQualifiedName("AddParagraph"), applicationId,
                paragraphId, titleId, title, bodyText, sequenceNumber, currentUserId, publicMethods.now(),
                isRichText, sendToAdmins, hasAdmin, adminIdsDT) > 0;
    }

    public boolean modifyParagraph(UUID applicationId, UUID paragraphId, String title, String bodyText, UUID currentUserId,
                                   UUID changeId2Accept, Boolean hasAdmin, List<UUID> adminUserIds, Boolean citationNeeded,
                                   Boolean apply, Boolean accept, List<Dashboard> retDashboards)
    {
        RVStructuredParam adminIdsDT = new RVStructuredParam("GuidTableType")
                .addColumnMetaData("Value", UUID.class);
        adminUserIds.stream().distinct().forEach(adminIdsDT::addRow);

        if(StringUtils.isBlank(bodyText)) bodyText = "";

        return rvConnection.getDashboards(retDashboards, getQualifiedName("ModifyParagraph"), applicationId,
                paragraphId, changeId2Accept, title, bodyText, currentUserId, publicMethods.now(), citationNeeded,
                apply, accept, hasAdmin, adminIdsDT) > 0;
    }

    public boolean removeParagraph(UUID applicationId, UUID paragraphId, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("ArithmeticDeleteParagraph"),
                applicationId, paragraphId, currentUserId, publicMethods.now());
    }

    public boolean recycleParagraph(UUID applicationId, UUID paragraphId, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("RecycleParagraph"),
                applicationId, paragraphId, currentUserId, publicMethods.now());
    }

    public boolean setParagraphsOrder(UUID applicationId, List<UUID> paragraphIds)
    {
        return rvConnection.succeed(getQualifiedName("SetParagraphsOrder"), applicationId,
                paragraphIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',');
    }

    public List<Paragraph> getParagraphs(UUID applicationId, List<UUID> paragraphIds, UUID currentUserId)
    {
        return parser.paragraphs(rvConnection.read(getQualifiedName("GetParagraphsByIDs"), applicationId,
                paragraphIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',', currentUserId));
    }

    public List<Paragraph> getTitleParagraphs(UUID applicationId, List<UUID> titleIds,
                                              Boolean isAdmin, UUID currentUserId, boolean removed)
    {
        return parser.paragraphs(rvConnection.read(getQualifiedName("GetParagraphs"), applicationId,
                titleIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',', isAdmin, currentUserId, removed));
    }

    public boolean hasParagraph(UUID applicationId, UUID titleOrOwnerId, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("HasParagraph"), applicationId, titleOrOwnerId, currentUserId);
    }

    public List<Paragraph> getDashboardParagraphs(UUID applicationId, UUID userId)
    {
        return parser.paragraphs(rvConnection.read(getQualifiedName("GetDashboardParagraphs"), applicationId, userId));
    }

    public List<User> getParagraphRelatedUsers(UUID applicationId, UUID paragraphId)
    {
        return usersDao.getUsers(applicationId,
                rvConnection.getUUIDList(getQualifiedName("GetParagraphRelatedUserIDs"), applicationId, paragraphId));
    }

    public boolean rejectChange(UUID applicationId, UUID changeId, UUID evaluatorUserId)
    {
        return rvConnection.succeed(getQualifiedName("RejectChange"),
                applicationId, changeId, evaluatorUserId, publicMethods.now());
    }

    public boolean acceptChange(UUID applicationId, UUID changeId, UUID evaluatorUserId)
    {
        return rvConnection.succeed(getQualifiedName("AcceptChange"),
                applicationId, changeId, evaluatorUserId, publicMethods.now());
    }

    public boolean removeChange(UUID applicationId, UUID changeId)
    {
        return rvConnection.succeed(getQualifiedName("ArithmeticDeleteChange"), applicationId, changeId);
    }

    public List<Change> getChanges(UUID applicationId, List<UUID> changeIds)
    {
        return parser.changes(rvConnection.read(getQualifiedName("GetChangesByIDs"), applicationId,
                changeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ','));
    }

    public List<Change> getChanges(UUID applicationId, List<UUID> paragraphIds,
                                   UUID creatorUserId, WikiStatuses status, Boolean applied)
    {
        return parser.changes(rvConnection.read(getQualifiedName("GetParagraphChanges"), applicationId,
                paragraphIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',',
                creatorUserId, (status == WikiStatuses.None ? null : status.toString()), applied));
    }

    public Change getLastPendingChange(UUID applicationId, UUID paragraphId, UUID userId)
    {
        List<Change> lst = parser.changes(rvConnection.read(getQualifiedName("GetLastPendingChange"),
                applicationId, paragraphId, userId));
        return lst.size() == 0 ? null : lst.get(0);
    }

    public List<UUID> getChangedWikiOwnerIDs(UUID applicationId, List<UUID> ownerIds)
    {
        return rvConnection.getUUIDList(getQualifiedName("GetChangedWikiOwnerIDs"), applicationId,
                ownerIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',');
    }

    public WikiOwnerType getWikiOwner(UUID applicationId, UUID id, MutableUUID retOwnerId)
    {
        return parser.wikiOwner(rvConnection.read(getQualifiedName("GetWikiOwner"), applicationId, id), retOwnerId);
    }

    public String getWikiContent(UUID applicationId, UUID ownerId)
    {
        return rvConnection.getString(getQualifiedName("GetWikiContent"), applicationId, ownerId);
    }

    public int getTitlesCount(UUID applicationId, UUID ownerId, Boolean isAdmin, UUID currentUserId, Boolean removed)
    {
        return rvConnection.getInt(getQualifiedName("GetTitlesCount"),
                applicationId, ownerId, isAdmin, currentUserId, removed);
    }

    public Map<UUID, Integer> getParagraphsCount(UUID applicationId, List<UUID> titleIds,
                                                 Boolean isAdmin, UUID currentUserId, Boolean removed)
    {
        return rvConnection.getItemsCount(getQualifiedName("GetParagraphsCount"), applicationId,
                titleIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',',
                isAdmin, currentUserId, removed);
    }

    public Map<UUID, Integer> getChangesCount(UUID applicationId, List<UUID> paragraphIds, Boolean applied)
    {
        return rvConnection.getItemsCount(getQualifiedName("GetChangesCount"), applicationId,
                paragraphIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',', applied);
    }

    public DateTime lastModificationDate(UUID applicationId, UUID ownerId)
    {
        return rvConnection.getDate(getQualifiedName("LastModificationDate"), applicationId, ownerId);
    }

    public List<SimpleEntry<UUID, Integer>> wikiAuthors(UUID applicationId, UUID ownerId)
    {
        return rvConnection.getItemsCountList(getQualifiedName("WikiAuthors"), applicationId, ownerId);
    }
}
