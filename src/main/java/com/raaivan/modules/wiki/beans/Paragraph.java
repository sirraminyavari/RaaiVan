package com.raaivan.modules.wiki.beans;

import com.raaivan.modules.documents.beans.DocFileInfo;
import com.raaivan.modules.wiki.enums.WikiStatuses;
import com.raaivan.util.PublicMethods;
import org.joda.time.DateTime;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class Paragraph {
    private UUID ParagraphID;
    private UUID TitleID;
    private UUID CreatorUserID;
    private DateTime CreationDate;
    private String Title;
    private String BodyText;
    private Integer SequenceNumber;
    private Boolean IsRichText;
    private WikiStatuses Status;
    private UUID LastModifierUserID;
    private DateTime LastModificationDate;
    private List<Change> Changes;
    private List<DocFileInfo> AttachedFiles;
    private Integer AppliedChangesCount;

    public Paragraph()
    {
        Changes = new ArrayList<>();
        AttachedFiles = new ArrayList<>();
    }

    public UUID getParagraphID() {
        return ParagraphID;
    }

    public void setParagraphID(UUID paragraphID) {
        ParagraphID = paragraphID;
    }

    public UUID getTitleID() {
        return TitleID;
    }

    public void setTitleID(UUID titleID) {
        TitleID = titleID;
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

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getBodyText() {
        return BodyText;
    }

    public void setBodyText(String bodyText) {
        BodyText = bodyText;
    }

    public Integer getSequenceNumber() {
        return SequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        SequenceNumber = sequenceNumber;
    }

    public Boolean getRichText() {
        return IsRichText;
    }

    public void setRichText(Boolean richText) {
        IsRichText = richText;
    }

    public WikiStatuses getStatus() {
        return Status;
    }

    public void setStatus(WikiStatuses status) {
        Status = status;
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

    public List<Change> getChanges() {
        return Changes;
    }

    public void setChanges(List<Change> changes) {
        Changes = changes;
    }

    public List<DocFileInfo> getAttachedFiles() {
        return AttachedFiles;
    }

    public void setAttachedFiles(List<DocFileInfo> attachedFiles) {
        AttachedFiles = attachedFiles;
    }

    public Integer getAppliedChangesCount() {
        return AppliedChangesCount;
    }

    public void setAppliedChangesCount(Integer appliedChangesCount) {
        AppliedChangesCount = appliedChangesCount;
    }
}
