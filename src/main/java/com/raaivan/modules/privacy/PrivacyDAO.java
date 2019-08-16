package com.raaivan.modules.privacy;

import com.raaivan.util.dbutil.RVConnection;
import com.raaivan.util.dbutil.RVStructuredParam;
import com.raaivan.modules.privacy.beans.Audience;
import com.raaivan.modules.privacy.beans.ConfidentialityLevel;
import com.raaivan.modules.privacy.beans.DefaultPermission;
import com.raaivan.modules.privacy.beans.Privacy;
import com.raaivan.modules.privacy.enums.PermissionType;
import com.raaivan.modules.privacy.enums.PrivacyObjectType;
import com.raaivan.modules.privacy.enums.PrivacyType;
import com.raaivan.util.rvsettings.RaaiVanSettings;
import com.raaivan.util.PublicMethods;
import org.apache.commons.lang3.mutable.MutableLong;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.*;
import java.util.stream.Collectors;

@Service
@ApplicationScope
public class PrivacyDAO {
    private RVConnection rvConnection;
    private PrivacyParsers parser;
    private PublicMethods publicMethods;
    private RaaiVanSettings raaivanSettings;

    @Autowired
    public void _setDependencies(RVConnection rvConnection, PrivacyParsers parser,
                                 PublicMethods publicMethods, RaaiVanSettings raaiVanSettings) {
        if (this.rvConnection == null) this.rvConnection = rvConnection;
        if (this.parser == null) this.parser = parser;
        if (this.publicMethods == null) this.publicMethods = publicMethods;
        if (this.raaivanSettings == null) this.raaivanSettings = raaiVanSettings;
    }

    private String getQualifiedName(String name){
        return "[dbo].[PRVC_" + name + "]";
    }

    public boolean initializeConfidentialityLevels(UUID applicationId)
    {
        return rvConnection.succeed(getQualifiedName("InitializeConfidentialityLevels"), applicationId);
    }

    public boolean refineAccessRoles(UUID applicationId)
    {
        return rvConnection.succeed(getQualifiedName("RefineAccessRoles"), applicationId);
    }

    public boolean setAudience(UUID applicationId, List<Privacy> items, UUID currentUserId)
    {
        RVStructuredParam idsDT = new RVStructuredParam("GuidTableType").addColumnMetaData("Value", UUID.class);
        items.stream().filter(u -> u.getObjectID() != null).map(Privacy::getObjectID).distinct().forEach(idsDT::addRow);

        RVStructuredParam defaultsDT = new RVStructuredParam("GuidStringPairTableType")
                .addColumnMetaData("GuidValue", UUID.class)
                .addColumnMetaData("FirstValue", String.class)
                .addColumnMetaData("SecondValue", String.class);
        items.stream().filter(u -> u.getObjectID() != null).forEach(u -> {
             u.getDefaultPermissions().stream()
                     .filter(x -> x.getPermission() != PermissionType.None && x.getDefaultValue() != PrivacyType.NotSet)
                     .forEach(x -> defaultsDT.addRow(u.getObjectID(), x.getPermission().toString(), x.getDefaultValue().toString()));
        });

        RVStructuredParam audienceDT = new RVStructuredParam("PrivacyAudienceTableType")
                .addColumnMetaData("ObjectID", UUID.class)
                .addColumnMetaData("RoleID", UUID.class)
                .addColumnMetaData("PermissionType", String.class)
                .addColumnMetaData("Allow", Boolean.class)
                .addColumnMetaData("ExpirationDate", DateTime.class);
        items.stream().filter(u -> u.getObjectID() != null).forEach(u -> {
            u.getAudienceList().stream()
                    .filter(x -> x.getRoleID() != null && x.getAllow() != null && x.getPermission() != PermissionType.None)
                    .forEach(x -> audienceDT.addRow(u.getObjectID(), x.getRoleID(),
                            x.getPermission().toString(), x.getAllow(), x.getExpirationDate()));
        });

        RVStructuredParam settingsDT = new RVStructuredParam("GuidPairBitTableType")
                .addColumnMetaData("FirstValue", UUID.class)
                .addColumnMetaData("SecondValue", UUID.class)
                .addColumnMetaData("BitValue", Boolean.class);
        items.stream().filter(u -> u.getObjectID() != null &&
                (u.getConfidentiality().getLevelID() != null || u.getCalculateHierarchy() != null))
                .forEach(u -> settingsDT.addRow(u.getObjectID(), u.getConfidentiality().getID(), u.getCalculateHierarchy()));

        return rvConnection.succeed(getQualifiedName("SetAudience"),
                applicationId, idsDT, defaultsDT, audienceDT, settingsDT, currentUserId, publicMethods.now());
    }

    public Map<UUID, List<PermissionType>> checkAccess(UUID applicationId, UUID userId, List<UUID> objectIds,
                                                       PrivacyObjectType objectType, List<PermissionType> permissions)
    {
        if(objectIds.size() == 0) return new HashMap<>();

        if(userId == null) userId = UUID.randomUUID();

        if(permissions.size() == 0)
            permissions = Arrays.stream(PermissionType.values()).filter(u -> u != PermissionType.None).collect(Collectors.toList());

        RVStructuredParam idsDT = new RVStructuredParam("GuidTableType").addColumnMetaData("Value", UUID.class);
        objectIds.forEach(idsDT::addRow);

        RVStructuredParam permissionsDT = new RVStructuredParam("StringPairTableType")
                .addColumnMetaData("FirstValue", String.class)
                .addColumnMetaData("SecondValue", String.class);
        permissions.stream().filter(u -> u != PermissionType.None).forEach(u -> {
            String defaultPrivacy = u == PermissionType.Create || u == PermissionType.View ||
                    u == PermissionType.ViewAbstract ? raaivanSettings.DefaultPrivacy(applicationId) :
                    (u == PermissionType.Download ? raaivanSettings.DefaultPrivacyForReadableFiles(applicationId) : "");

            permissionsDT.addRow(u.toString(), defaultPrivacy);
        });

        return parser.accessCheckedItems(rvConnection.read(getQualifiedName("CheckAccess"), applicationId, userId,
                (objectType == PrivacyObjectType.None ? null : objectType.toString()), idsDT, permissionsDT, publicMethods.now()));
    }

    public Map<UUID, List<PermissionType>> checkAccess(UUID applicationId, UUID userId,
                                                       List<UUID> objectIds, PrivacyObjectType objectType){
        return checkAccess(applicationId, userId, objectIds, objectType, new ArrayList<>());
    }

    public List<UUID> checkAccess(UUID applicationId, UUID userId, List<UUID> objectIds,
                                  PrivacyObjectType objectType, PermissionType permission){
        if(permission == PermissionType.None) return new ArrayList<>();

        List<PermissionType> p = new ArrayList<PermissionType>(){{
            add(permission);
        }};

        Map<UUID, List<PermissionType>> dic = checkAccess(applicationId, userId, objectIds, objectType, p);

        return new ArrayList<UUID>(dic.keySet());
    }

    public List<PermissionType> checkAccess(UUID applicationId, UUID userId, UUID objectId,
                                            PrivacyObjectType objectType, List<PermissionType> permissions){
        List<UUID> ids = new ArrayList<UUID>(){{
            add(objectId);
        }};

        Map<UUID, List<PermissionType>> dic = checkAccess(applicationId, userId, ids, objectType, permissions);

        return dic.getOrDefault(objectId, new ArrayList<>());
    }

    public List<PermissionType> checkAccess(UUID applicationId, UUID userId, UUID objectId, PrivacyObjectType objectType){
        return checkAccess(applicationId, userId, objectId, objectType, new ArrayList<>());
    }

    public boolean checkAccess(UUID applicationId, UUID userId, UUID objectId,
                               PrivacyObjectType objectType, PermissionType permission) {
        if(permission == PermissionType.None) return false;

        List<PermissionType> p = new ArrayList<PermissionType>() {{
            add(permission);
        }};

        List<PermissionType> granted = checkAccess(applicationId, userId, objectId, objectType, p);

        return granted.size() > 0;
    }

    public Map<UUID, List<Audience>> getAudience(UUID applicationId, List<UUID> objectIds)
    {
        return parser.audience(rvConnection.read(getQualifiedName("GetAudience"), applicationId,
                objectIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ','));
    }

    public List<Audience> getAudience(UUID applicationId, UUID objectId){
        List<UUID> ids = new ArrayList<UUID>(){{
            add(objectId);
        }};

        Map<UUID, List<Audience>> dic = getAudience(applicationId, ids);

        return dic.getOrDefault(objectId, new ArrayList<>());
    }

    public Map<UUID, List<DefaultPermission>> getDefaultPermissions(UUID applicationId, List<UUID> objectIds)
    {
        return parser.defaultPermissions(rvConnection.read(getQualifiedName("GetDefaultPermissions"), applicationId,
                objectIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ','));
    }

    public List<DefaultPermission> getDefaultPermissions(UUID applicationId, UUID objectId){
        List<UUID> ids = new ArrayList<UUID>(){{
            add(objectId);
        }};

        Map<UUID, List<DefaultPermission>> dic = getDefaultPermissions(applicationId, ids);

        return dic.getOrDefault(objectId, new ArrayList<>());
    }

    public List<Privacy> getSettings(UUID applicationId, List<UUID> objectIds)
    {
        return parser.settings(rvConnection.read(getQualifiedName("GetSettings"), applicationId,
                objectIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ','));
    }

    public Privacy getSettings(UUID applicationId, UUID objectId)
    {
        List<UUID> ids = new ArrayList<UUID>(){{
            add(objectId);
        }};

        List<Privacy> lst = getSettings(applicationId, ids);

        return lst.size() == 0 ? null : lst.get(0);
    }

    public boolean addConfidentialityLevel(UUID applicationId, UUID id, int levelId,
                                           String title, UUID currentUserId, StringBuilder errorMessage)
    {
        return rvConnection.succeed(errorMessage, getQualifiedName("AddConfidentialityLevel"),
                applicationId, id, levelId, title, currentUserId, publicMethods.now());
    }

    public boolean modifyConfidentialityLevel(UUID applicationId, UUID id, int newLevelId,
                                              String newTitle, UUID currentUserId, StringBuilder errorMessage)
    {
        return rvConnection.succeed(errorMessage, getQualifiedName("ModifyConfidentialityLevel"),
                applicationId, id, newLevelId, newTitle, currentUserId, publicMethods.now());
    }

    public boolean removeConfidentialityLevel(UUID applicationId, UUID id, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("RemoveConfidentialityLevel"),
                applicationId, id, currentUserId, publicMethods.now());
    }

    public List<ConfidentialityLevel> getConfidentialityLevels(UUID applicationId)
    {
        return parser.confidentialityLevels(rvConnection.read(getQualifiedName("GetConfidentialityLevels"), applicationId));
    }

    public boolean setConfidentialityLevel(UUID applicationId, UUID itemId, UUID levelId, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("SetConfidentialityLevel"),
                applicationId, itemId, levelId, currentUserId, publicMethods.now());
    }

    public boolean unsetConfidentialityLevel(UUID applicationId, UUID itemId, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("UnsetConfidentialityLevel"),
                applicationId, itemId, currentUserId, publicMethods.now());
    }

    public List<UUID> getConfidentialityLevelUserIDs(UUID applicationId, UUID confidentialityId,
                                                     Integer count, Long lowerBoundary, MutableLong totalCount)
    {
        return parser.userIds(rvConnection.read(getQualifiedName("GetConfidentialityLevelUserIDs"),
                applicationId, confidentialityId, count, lowerBoundary), totalCount);
    }

    public List<UUID> getConfidentialityLevelUserIDs(UUID applicationId, UUID confidentialityId, Integer count, Long lowerBoundary)
    {
        MutableLong totalCount = new MutableLong(0);
        return getConfidentialityLevelUserIDs(applicationId, confidentialityId, count, lowerBoundary, totalCount);
    }

    public List<UUID> getConfidentialityLevelUserIDs(UUID applicationId, UUID confidentialityId)
    {
        return getConfidentialityLevelUserIDs(applicationId, confidentialityId, null, null);
    }
}
