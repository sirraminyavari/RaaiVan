package com.raaivan.modules.corenetwork.beans;

import com.raaivan.modules.documents.util.DocumentUtilities;
import com.raaivan.util.Base64;
import com.raaivan.util.RVJSON;
import io.micrometer.core.instrument.util.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class NodeType {
    private DocumentUtilities documentUtilities;

    @Autowired
    public void _setDependencies(DocumentUtilities documentUtilities){
        if(this.documentUtilities == null) this.documentUtilities = documentUtilities;
    }

    private UUID NodeTypeID;
    private UUID ParentID;
    private String AdditionalID;
    private String Name;
    private String Description;
    private String AdditionalIDPattern;
    private Boolean HasDefaultPattern;
    private UUID CreatorUserID;
    private DateTime CreationDate;
    private UUID LastModifierUserID;
    private DateTime LastModificationDate;
    private Boolean Archive;
    private Boolean HasChild;
    private String IconURL;
    private String HighQualityIconURL;

    public UUID getNodeTypeID() {
        return NodeTypeID;
    }

    public void setNodeTypeID(UUID nodeTypeID) {
        NodeTypeID = nodeTypeID;
    }

    public String getAdditionalID() {
        return AdditionalID;
    }

    public void setAdditionalID(String additionalID) {
        AdditionalID = additionalID;
    }

    public UUID getParentID() {
        return ParentID;
    }

    public void setParentID(UUID parentID) {
        ParentID = parentID;
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

    public String getAdditionalIDPattern() {
        return AdditionalIDPattern;
    }

    public void setAdditionalIDPattern(String additionalIDPattern) {
        AdditionalIDPattern = additionalIDPattern;
    }

    public Boolean getHasDefaultPattern() {
        return HasDefaultPattern;
    }

    public void setHasDefaultPattern(Boolean hasDefaultPattern) {
        HasDefaultPattern = hasDefaultPattern;
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

    public Boolean getArchive() {
        return Archive;
    }

    public void setArchive(Boolean archive) {
        Archive = archive;
    }

    public Boolean getHasChild() {
        return HasChild;
    }

    public void setHasChild(Boolean hasChild) {
        HasChild = hasChild;
    }

    public String getIconURL() {
        return IconURL;
    }

    public void setIconURL(String iconURL) {
        IconURL = iconURL;
    }

    public String getHighQualityIconURL() {
        return HighQualityIconURL;
    }

    public void setHighQualityIconURL(String highQualityIconURL) {
        HighQualityIconURL = highQualityIconURL;
    }

    public RVJSON toJson(UUID applicationId, boolean iconUrl)
    {
        boolean hasDefaultPattern = HasDefaultPattern == null || HasDefaultPattern;

        if (applicationId != null && iconUrl && NodeTypeID != null)
        {
            if(StringUtils.isBlank(IconURL))
                IconURL = documentUtilities.getIconUrl(applicationId, NodeTypeID);
            if(StringUtils.isBlank(HighQualityIconURL))
                HighQualityIconURL = documentUtilities.getIconUrl(applicationId, NodeTypeID, null, true, false);
        }

        return (new RVJSON())
                .add("NodeTypeID", NodeTypeID)
                .add("AdditionalID", Base64.encode(AdditionalID))
                .add("TypeName", Base64.encode(Name))
                .add("ParentID", ParentID)
                .add("AdditionalIDPattern", AdditionalIDPattern)
                .add("IsArchive", Archive != null && Archive)
                .add("HasDefaultPattern", hasDefaultPattern)
                .add("IconURL", IconURL)
                .add("HighQualityIconURL", HighQualityIconURL)
                .add("HasChild", HasChild);
    }

    public RVJSON toJson(){
        return toJson(null, false);
    }
}
