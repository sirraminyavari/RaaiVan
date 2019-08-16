package com.raaivan.modules.forms.beans;

import com.raaivan.util.Base64;
import com.raaivan.util.PublicMethods;
import com.raaivan.util.RVJSON;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class FormRecord {
    private PublicMethods publicMethods;

    @Autowired
    public void _setDependencies(PublicMethods publicMethods){
        if(this.publicMethods == null) this.publicMethods = publicMethods;
    }

    private UUID InstanceID;
    private UUID OwnerID;
    private DateTime CreationDate;
    private List<RecordCell> Cells;

    public FormRecord()
    {
        Cells = new ArrayList<>();
    }

    public UUID getInstanceID() {
        return InstanceID;
    }

    public void setInstanceID(UUID instanceID) {
        InstanceID = instanceID;
    }

    public UUID getOwnerID() {
        return OwnerID;
    }

    public void setOwnerID(UUID ownerID) {
        OwnerID = ownerID;
    }

    public DateTime getCreationDate() {
        return CreationDate;
    }

    public void setCreationDate(DateTime creationDate) {
        CreationDate = creationDate;
    }

    public List<RecordCell> getCells() {
        return Cells;
    }

    public void setCells(List<RecordCell> cells) {
        Cells = cells;
    }

    public RVJSON toJson() {
        RVJSON ret = new RVJSON()
                .add("InstanceID", InstanceID)
                .add("OwnerID", OwnerID)
                .add("CreationDate", CreationDate)
                .add("CreationDate_Jalali", publicMethods.getPersianDate(CreationDate))
                .add("CreationDate_Local", publicMethods.getPersianDate(CreationDate));

        Cells.stream().filter(x -> x.getElementID() != null)
                .forEach(u -> ret.add(u.getElementID().toString(), Base64.encode(u.getValue())));

        return ret;
    }
}
