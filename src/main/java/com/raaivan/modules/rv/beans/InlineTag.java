package com.raaivan.modules.rv.beans;

import com.raaivan.util.PublicMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class InlineTag
{
    private PublicMethods publicMethods;

    @Autowired
    public void _setDependencies(PublicMethods publicMethods) {
        if (this.publicMethods == null) this.publicMethods = publicMethods;
    }

    private String StringID;
    private UUID ID;
    private String Type;
    private String Value;
    private String Info;

    public void setId(String id) {
        StringID = id;
        ID = publicMethods.parseUUID(id);
    }

    public String getId()
    {
        return ID != null ? ID.toString() : StringID;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getInfo() {
        return Info;
    }

    public void setInfo(String info) {
        Info = info;
    }
}
