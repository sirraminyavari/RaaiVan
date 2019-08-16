package com.raaivan.modules.wiki.beans;

import com.raaivan.modules.documents.beans.DocFileInfo;
import com.raaivan.modules.users.beans.User;
import com.raaivan.modules.wiki.enums.WikiStatuses;
import com.raaivan.util.PublicMethods;
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
public class Change {
    private UUID ChangeID;
    private UUID ParagraphID;
    private User Sender;
    private DateTime SendDate;
    private DateTime LastModificationDate;
    private String Title;
    private String BodyText;
    private WikiStatuses Status;
    private Boolean Applied;
    private List<DocFileInfo> AttachedFiles;

    @Autowired
    public Change(User sender)
    {
        this.Sender = sender;
        AttachedFiles = new ArrayList<>();
    }

    public UUID getChangeID() {
        return ChangeID;
    }

    public void setChangeID(UUID changeID) {
        ChangeID = changeID;
    }

    public UUID getParagraphID() {
        return ParagraphID;
    }

    public void setParagraphID(UUID paragraphID) {
        ParagraphID = paragraphID;
    }

    public User getSender() {
        return Sender;
    }

    public void setSender(User sender) {
        Sender = sender;
    }

    public DateTime getSendDate() {
        return SendDate;
    }

    public void setSendDate(DateTime sendDate) {
        SendDate = sendDate;
    }

    public DateTime getLastModificationDate() {
        return LastModificationDate;
    }

    public void setLastModificationDate(DateTime lastModificationDate) {
        LastModificationDate = lastModificationDate;
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

    public WikiStatuses getStatus() {
        return Status;
    }

    public void setStatus(WikiStatuses status) {
        Status = status;
    }

    public Boolean getApplied() {
        return Applied;
    }

    public void setApplied(Boolean applied) {
        Applied = applied;
    }

    public List<DocFileInfo> getAttachedFiles() {
        return AttachedFiles;
    }

    public void setAttachedFiles(List<DocFileInfo> attachedFiles) {
        AttachedFiles = attachedFiles;
    }
}
