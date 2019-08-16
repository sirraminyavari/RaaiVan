package com.raaivan.modules.rv.beans;

import com.raaivan.util.Base64;
import com.raaivan.util.RVJSON;
import com.raaivan.util.PublicMethods;
import io.micrometer.core.instrument.util.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class Variable {
    private Long ID;
    private UUID OwnerID;
    private String Name;
    private String Value;
    private UUID CreatorUserID;
    private DateTime CreationDate;

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public UUID getOwnerID() {
        return OwnerID;
    }

    public void setOwnerID(UUID ownerID) {
        OwnerID = ownerID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
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

    public RVJSON toJson(boolean editable)
    {
        return (new RVJSON())
                .add("ID", ID == null ? "" : ID.toString())
                .add("OwnerID", OwnerID == null ? "" : OwnerID.toString())
                .add("Name", StringUtils.isBlank(Name) ? "" : Name)
                .add("Value", Base64.encode(Value))
                .add("Editable", editable);
    }
}
