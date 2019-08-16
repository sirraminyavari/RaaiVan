package com.raaivan.modules.privacy.beans;

import com.raaivan.modules.documents.util.DocumentUtilities;
import com.raaivan.modules.privacy.enums.PermissionType;
import com.raaivan.modules.privacy.enums.PrivacyType;
import com.raaivan.util.RVBeanFactory;
import com.raaivan.util.RVJSON;
import com.raaivan.util.PublicMethods;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class DefaultPermission {
    private PublicMethods publicMethods;

    @Autowired
    public void _setDependencies(PublicMethods publicMethods){
        if(this.publicMethods == null) this.publicMethods = publicMethods;
    }

    private PermissionType Permission;
    private PrivacyType DefaultValue;

    public DefaultPermission() {
        Permission = PermissionType.None;
        DefaultValue = PrivacyType.NotSet;
    }

    public PermissionType getPermission() {
        return Permission;
    }

    public void setPermission(PermissionType permission) {
        Permission = permission;
    }

    public PrivacyType getDefaultValue() {
        return DefaultValue;
    }

    public void setDefaultValue(PrivacyType defaultValue) {
        DefaultValue = defaultValue;
    }

    public RVJSON toJson() {
        return (new RVJSON())
                .add("PermissionType", Permission == PermissionType.None ? null : Permission.toString())
                .add("DefaultValue", DefaultValue == PrivacyType.NotSet ? null : DefaultValue.toString());
    }

    private DefaultPermission _fromJson(JSONObject json)
    {
        if(json == null) return null;

        if (!json.isNull("PermissionType") && json.get("PermissionType") != null)
            this.setPermission(publicMethods.lookupEnum(PermissionType.class, json.get("PermissionType").toString(), PermissionType.None));

        PrivacyType dv = PrivacyType.NotSet;
        if (!json.isNull("DefaultValue") && json.get("DefaultValue") != null)
            this.setDefaultValue(publicMethods.lookupEnum(PrivacyType.class, json.get("DefaultValue").toString(), PrivacyType.NotSet));

        return this.getPermission() == PermissionType.None ? null : this;
    }

    public static DefaultPermission fromJson(JSONObject json){
        return RVBeanFactory.getBean(DefaultPermission.class)._fromJson(json);
    }
}
