package com.raaivan.modules.corenetwork.beans;

import com.raaivan.modules.corenetwork.enums.ExtensionType;
import com.raaivan.util.Base64;
import com.raaivan.util.RVJSON;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class Extension {
    private UUID OwnerID;
    private ExtensionType Type;
    private String Title;
    private Boolean Disabled;
    private Boolean Initialized;

    public Extension()
    {
        Type = ExtensionType.NotSet;
    }

    public UUID getOwnerID() {
        return OwnerID;
    }

    public void setOwnerID(UUID ownerID) {
        OwnerID = ownerID;
    }

    public ExtensionType getType() {
        return Type;
    }

    public void setType(ExtensionType type) {
        Type = type;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public Boolean getDisabled() {
        return Disabled;
    }

    public void setDisabled(Boolean disabled) {
        Disabled = disabled;
    }

    public Boolean getInitialized() {
        return Initialized;
    }

    public void setInitialized(Boolean initialized) {
        Initialized = initialized;
    }

    public RVJSON toJson() {
        return (new RVJSON())
                .add("Extension", Type == ExtensionType.NotSet ? null : Type.toString())
                .add("Title", Base64.encode(Title))
                .add("Disabled", Disabled == null || Disabled)
                .add("Initialized", Initialized != null && Initialized);
    }
}
