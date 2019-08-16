package com.raaivan.modules.rv.beans;

import com.raaivan.util.Base64;
import com.raaivan.util.RVJSON;
import com.raaivan.util.PublicMethods;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class Hierarchy {
    private UUID ID;
    private UUID ParentID;
    private Integer Level;
    private String Name;

    public UUID getID() {
        return ID;
    }

    public void setID(UUID ID) {
        this.ID = ID;
    }

    public UUID getParentID() {
        return ParentID;
    }

    public void setParentID(UUID parentID) {
        ParentID = parentID;
    }

    public Integer getLevel() {
        return Level;
    }

    public void setLevel(Integer level) {
        Level = level;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public RVJSON toJson() {
        return (new RVJSON())
                .add("NodeID", ID == null ? "" : ID.toString())
                .add("Name", Base64.encode(Name))
                .add("ParentID", ParentID == null ? "" : ParentID.toString())
                .add("Level", Level == null ? null : Level.toString());
    }
}
