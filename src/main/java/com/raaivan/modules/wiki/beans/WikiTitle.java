package com.raaivan.modules.wiki.beans;

import com.raaivan.modules.wiki.enums.WikiOwnerType;
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
public class WikiTitle {
    private UUID TitleID;
    private UUID OwnerID;
    private UUID CreatorUserID;
    private DateTime CreationDate;
    private Integer SequenceNumber;
    private String Title;
    private WikiStatuses Status;
    private WikiOwnerType OwnerType;
    private UUID LastModifierUserID;
    private DateTime LastModificationDate;
    private List<Paragraph> Paragraphs;
    private Integer RemovedParagraphsCount;

    public WikiTitle()
    {
        Paragraphs = new ArrayList<>();
    }

    public UUID getTitleID() {
        return TitleID;
    }

    public void setTitleID(UUID titleID) {
        TitleID = titleID;
    }

    public UUID getOwnerID() {
        return OwnerID;
    }

    public void setOwnerID(UUID ownerID) {
        OwnerID = ownerID;
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

    public Integer getSequenceNumber() {
        return SequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        SequenceNumber = sequenceNumber;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public WikiStatuses getStatus() {
        return Status;
    }

    public void setStatus(WikiStatuses status) {
        Status = status;
    }

    public WikiOwnerType getOwnerType() {
        return OwnerType;
    }

    public void setOwnerType(WikiOwnerType ownerType) {
        OwnerType = ownerType;
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

    public List<Paragraph> getParagraphs() {
        return Paragraphs;
    }

    public void setParagraphs(List<Paragraph> paragraphs) {
        Paragraphs = paragraphs;
    }

    public Integer getRemovedParagraphsCount() {
        return RemovedParagraphsCount;
    }

    public void setRemovedParagraphsCount(Integer removedParagraphsCount) {
        RemovedParagraphsCount = removedParagraphsCount;
    }
}
