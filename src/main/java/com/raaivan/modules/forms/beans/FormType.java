package com.raaivan.modules.forms.beans;

import com.raaivan.modules.users.beans.User;
import com.raaivan.util.PublicMethods;
import com.raaivan.util.RVBeanFactory;
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
public class FormType {
    private PublicMethods publicMethods;

    @Autowired
    public void _setDependencies(PublicMethods publicMethods){
        if(this.publicMethods == null) this.publicMethods = publicMethods;
    }

    private UUID FormID;
    private UUID InstanceID;
    private UUID OwnerID;
    private UUID DirectorID;
    private Boolean Admin;
    private String Title;
    private String Name;
    private String Description;
    private User Creator;
    private Boolean Filled;
    private Boolean IsTemporary;
    private DateTime FillingDate;
    private DateTime CreationDate;
    private UUID LastModifierUserID;
    private DateTime LastModificationDate;
    private List<FormElement> Elements;

    public FormType()
    {
        Elements = new ArrayList<>();
        Creator = RVBeanFactory.getBean(User.class);
    }

    public PublicMethods getPublicMethods() {
        return publicMethods;
    }

    public void setPublicMethods(PublicMethods publicMethods) {
        this.publicMethods = publicMethods;
    }

    public UUID getFormID() {
        return FormID;
    }

    public void setFormID(UUID formID) {
        FormID = formID;
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

    public UUID getDirectorID() {
        return DirectorID;
    }

    public void setDirectorID(UUID directorID) {
        DirectorID = directorID;
    }

    public Boolean getAdmin() {
        return Admin;
    }

    public void setAdmin(Boolean admin) {
        Admin = admin;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public User getCreator() {
        return Creator;
    }

    public void setCreator(User creator) {
        Creator = creator;
    }

    public Boolean getFilled() {
        return Filled;
    }

    public void setFilled(Boolean filled) {
        Filled = filled;
    }

    public Boolean getTemporary() {
        return IsTemporary;
    }

    public void setTemporary(Boolean temporary) {
        IsTemporary = temporary;
    }

    public DateTime getFillingDate() {
        return FillingDate;
    }

    public void setFillingDate(DateTime fillingDate) {
        FillingDate = fillingDate;
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

    public List<FormElement> getElements() {
        return Elements;
    }

    public void setElements(List<FormElement> elements) {
        Elements = elements;
    }

    public RVJSON toJson() {
        return (new RVJSON())
                .add("InstanceID", InstanceID)
                .add("FormID", FormID)
                .add("OwnerID", OwnerID)
                .add("DirectorID", DirectorID)
                .add("IsTemporary", IsTemporary != null && IsTemporary)
                .add("CreationDate", CreationDate)
                .add("CreationDate_Jalali", publicMethods.getPersianDate(CreationDate))
                .add("CreationDate_Local", publicMethods.getPersianDate(CreationDate));
    }
}
