package com.raaivan.modules.privacy.beans;

import com.raaivan.modules.documents.util.DocumentUtilities;
import com.raaivan.modules.privacy.enums.PermissionType;
import com.raaivan.util.Base64;
import com.raaivan.util.RVBeanFactory;
import com.raaivan.util.RVJSON;
import com.raaivan.util.PublicMethods;
import org.joda.time.DateTime;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class Audience {
    private PublicMethods publicMethods;

    @Autowired
    public void _setDependencies(PublicMethods publicMethods){
        if(this.publicMethods == null) this.publicMethods = publicMethods;
    }

    private UUID ObjectID;
    private UUID RoleID;
    private String RoleName;
    private String RoleType;
    private String NodeType;
    private String AdditionalID;
    private PermissionType Permission;
    private Boolean Allow;
    private DateTime ExpirationDate;
    private UUID CreatorUserID;
    private DateTime CreationDate;
    private UUID LastModifierUserID;
    private DateTime LastModificationDate;

    public Audience() {
        Permission = PermissionType.None;
    }

    public UUID getObjectID() {
        return ObjectID;
    }

    public void setObjectID(UUID objectID) {
        ObjectID = objectID;
    }

    public UUID getRoleID() {
        return RoleID;
    }

    public void setRoleID(UUID roleID) {
        RoleID = roleID;
    }

    public String getRoleName() {
        return RoleName;
    }

    public void setRoleName(String roleName) {
        RoleName = roleName;
    }

    public String getRoleType() {
        return RoleType;
    }

    public void setRoleType(String roleType) {
        RoleType = roleType;
    }

    public String getNodeType() {
        return NodeType;
    }

    public void setNodeType(String nodeType) {
        NodeType = nodeType;
    }

    public String getAdditionalID() {
        return AdditionalID;
    }

    public void setAdditionalID(String additionalID) {
        AdditionalID = additionalID;
    }

    public PermissionType getPermission() {
        return Permission;
    }

    public void setPermission(PermissionType permission) {
        Permission = permission;
    }

    public Boolean getAllow() {
        return Allow;
    }

    public void setAllow(Boolean allow) {
        Allow = allow;
    }

    public DateTime getExpirationDate() {
        return ExpirationDate;
    }

    public void setExpirationDate(DateTime expirationDate) {
        ExpirationDate = expirationDate;
    }

    public UUID getCreatorUserID() {
        return CreatorUserID;
    }

    public void setCreatorUserID(UUID creatorUserID) {
        CreatorUserID = creatorUserID;
    }

    public DateTime getCreationDate() {
        return CreationDate;
    }

    public void setCreationDate(DateTime creationDate) {
        CreationDate = creationDate;
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

    public RVJSON toJson()
    {
        return (new RVJSON())
                .add("ObjectID", ObjectID)
                .add("RoleID", RoleID)
                .add("RoleName", Base64.encode(RoleName))
                .add("RoleType", Base64.encode(RoleType))
                .add("NodeType", Base64.encode(NodeType))
                .add("AdditionalID", Base64.encode(AdditionalID))
                .add("Allow", Allow)
                .add("ExpirationDate", ExpirationDate)
                .add("ExpirationDate_Locale", publicMethods.getPersianDate(ExpirationDate))
                .add("PermissionType", Permission == PermissionType.None ? null : Permission.toString());
    }

    private Audience _fromJson(JSONObject json)
    {
        if(json == null) return null;

        if (!json.isNull("RoleID") && json.get("RoleID") != null)
            this.setRoleID(publicMethods.parseUUID(json.get("RoleID").toString()));
        if (!json.isNull("Allow") && json.get("Allow") != null)
            this.setAllow(publicMethods.parseBoolean(json.get("Allow").toString()));
        if (!json.isNull("ExpirationDate") && json.get("ExpirationDate") != null)
            this.setExpirationDate(publicMethods.parseDate(json.get("ExpirationDate").toString()));

        if (!json.isNull("PermissionType") && json.get("PermissionType") != null)
            this.setPermission(publicMethods.lookupEnum(PermissionType.class, json.get("PermissionType").toString(), PermissionType.None));

        return this.getPermission() != PermissionType.None && this.RoleID != null && this.Allow != null ? this : null;
    }

    public static Audience fromJson(JSONObject json){
        return RVBeanFactory.getBean(Audience.class)._fromJson(json);
    }
}
