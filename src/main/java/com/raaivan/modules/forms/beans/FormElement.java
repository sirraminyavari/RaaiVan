package com.raaivan.modules.forms.beans;

import com.raaivan.modules.documents.beans.DocFileInfo;
import com.raaivan.modules.forms.enums.FormElementTypes;
import com.raaivan.modules.users.beans.User;
import com.raaivan.util.RVBeanFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@Scope (scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class FormElement {
    private UUID ElementID;
    private UUID FormID;
    private UUID FormInstanceID;
    private UUID RefElementID;
    private Long ChangeID;
    private String Title;
    private String Name;
    private Boolean Necessary;
    private Boolean UniqueValue;
    private Integer SequenceNumber;
    private FormElementTypes Type;
    private String Info;
    private Double Weight;
    private String TextValue;
    private Double FloatValue;
    private Boolean BitValue;
    private DateTime DateValue;
    private List<Map.Entry<UUID, String>> GuidItems;
    private Boolean Filled;
    private User Creator;
    private DateTime CreationDate;
    private UUID LastModifierUserID;
    private DateTime LastModificationDate;
    private List<DocFileInfo> AttachedFiles;
    private List<FormType> TableContent;
    private Integer EditionsCount;

    public FormElement()
    {
        Type = FormElementTypes.None;
        GuidItems = new ArrayList<>();
        Creator = RVBeanFactory.getBean(User.class);
        AttachedFiles = new ArrayList<>();
        TableContent = new ArrayList<>();
    }

    public UUID getElementID() {
        return ElementID;
    }

    public void setElementID(UUID elementID) {
        ElementID = elementID;
    }

    public UUID getFormID() {
        return FormID;
    }

    public void setFormID(UUID formID) {
        FormID = formID;
    }

    public UUID getFormInstanceID() {
        return FormInstanceID;
    }

    public void setFormInstanceID(UUID formInstanceID) {
        FormInstanceID = formInstanceID;
    }

    public UUID getRefElementID() {
        return RefElementID == null ? ElementID : RefElementID;
    }

    public void setRefElementID(UUID refElementID) {
        RefElementID = refElementID;
    }

    public Long getChangeID() {
        return ChangeID;
    }

    public void setChangeID(Long changeID) {
        ChangeID = changeID;
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

    public Boolean getNecessary() {
        return Necessary;
    }

    public void setNecessary(Boolean necessary) {
        Necessary = necessary;
    }

    public Boolean getUniqueValue() {
        return UniqueValue;
    }

    public void setUniqueValue(Boolean uniqueValue) {
        UniqueValue = uniqueValue;
    }

    public Integer getSequenceNumber() {
        return SequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        SequenceNumber = sequenceNumber;
    }

    public FormElementTypes getType() {
        return Type;
    }

    public void setType(FormElementTypes type) {
        Type = type;
    }

    public String getInfo() {
        return Info;
    }

    public void setInfo(String info) {
        Info = info;
    }

    public Double getWeight() {
        return Weight;
    }

    public void setWeight(Double weight) {
        Weight = weight;
    }

    public String getTextValue() {
        return TextValue;
    }

    public void setTextValue(String textValue) {
        TextValue = textValue;
    }

    public Double getFloatValue() {
        return FloatValue;
    }

    public void setFloatValue(Double floatValue) {
        FloatValue = floatValue;
    }

    public Boolean getBitValue() {
        return BitValue;
    }

    public void setBitValue(Boolean bitValue) {
        BitValue = bitValue;
    }

    public DateTime getDateValue() {
        return DateValue;
    }

    public void setDateValue(DateTime dateValue) {
        DateValue = dateValue;
    }

    public List<Map.Entry<UUID, String>> getGuidItems() {
        return GuidItems;
    }

    public void setGuidItems(List<Map.Entry<UUID, String>> guidItems) {
        GuidItems = guidItems;
    }

    public Boolean getFilled() {
        return Filled;
    }

    public void setFilled(Boolean filled) {
        Filled = filled;
    }

    public User getCreator() {
        return Creator;
    }

    public void setCreator(User creator) {
        Creator = creator;
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

    public List<DocFileInfo> getAttachedFiles() {
        return AttachedFiles;
    }

    public void setAttachedFiles(List<DocFileInfo> attachedFiles) {
        AttachedFiles = attachedFiles;
    }

    public List<FormType> getTableContent() {
        return TableContent;
    }

    public void setTableContent(List<FormType> tableContent) {
        TableContent = tableContent;
    }

    public Integer getEditionsCount() {
        return EditionsCount;
    }

    public void setEditionsCount(Integer editionsCount) {
        EditionsCount = editionsCount;
    }
}
