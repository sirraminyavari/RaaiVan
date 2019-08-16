package com.raaivan.modules.corenetwork.beans;

import com.raaivan.modules.corenetwork.enums.NodeStatus;
import com.raaivan.modules.documents.util.DocumentUtilities;
import com.raaivan.modules.documents.enums.DefaultIconTypes;
import com.raaivan.modules.privacy.beans.ConfidentialityLevel;
import com.raaivan.modules.users.beans.User;
import com.raaivan.util.Base64;
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
public class Node {
    private DocumentUtilities documentUtilities;

    @Autowired
    public void _setDependencies(DocumentUtilities documentUtilities){
        if(this.documentUtilities == null) this.documentUtilities = documentUtilities;
    }

    private UUID NodeID;
    private String AdditionalID_Main;
    private String AdditionalID;
    private UUID NodeTypeID;
    private String TypeAdditionalID;
    private UUID DocumentTreeNodeID;
    private UUID DocumentTreeID;
    private String DocumentTreeName;
    private UUID PreviousVersionID;
    private String PreviousVersionName;
    private String NodeType;
    private String Name;
    private String Description;
    private String PublicDescription;
    private User Creator;
    private DateTime CreationDate;
    private UUID LastModifierUserID;
    private DateTime LastModificationDate;
    private UUID ParentNodeID;
    private String ParentName;
    private Integer LikesCount;
    private Boolean LikeStatus;
    private String MembershipStatus;
    private Integer VisitsCount;
    private List<String> Tags;
    private List<NodeCreator> Contributors;
    private UUID OwnerID;
    private String OwnerName;
    private UUID AdminAreaID;
    private String AdminAreaName;
    private String AdminAreaType;
    private ConfidentialityLevel ConfidentialityLevel;
    private Boolean Searchable;
    private Boolean HideCreators;
    private DateTime PublicationDate;
    private DateTime ExpirationDate;
    private NodeStatus Status;
    private String WFState;
    private Double Score;
    private Boolean IsFreeUser;
    private Boolean Archive;
    private Boolean HasWikiContent;
    private Boolean HasFormContent;
    private Boolean HasChild;

    public Node()
    {
        Creator = RVBeanFactory.getBean(User.class);
        Tags = new ArrayList<>();
        Contributors = new ArrayList<>();
        ConfidentialityLevel = RVBeanFactory.getBean(ConfidentialityLevel.class);
        Status = NodeStatus.NotSet;
    }

    public UUID getNodeID() {
        return NodeID;
    }

    public void setNodeID(UUID nodeID) {
        NodeID = nodeID;
    }

    public String getAdditionalID_Main() {
        return AdditionalID_Main;
    }

    public void setAdditionalID_Main(String additionalID_Main) {
        AdditionalID_Main = additionalID_Main;
    }

    public String getAdditionalID() {
        return AdditionalID;
    }

    public void setAdditionalID(String additionalID) {
        AdditionalID = additionalID;
    }

    public UUID getNodeTypeID() {
        return NodeTypeID;
    }

    public void setNodeTypeID(UUID nodeTypeID) {
        NodeTypeID = nodeTypeID;
    }

    public String getTypeAdditionalID() {
        return TypeAdditionalID;
    }

    public void setTypeAdditionalID(String typeAdditionalID) {
        TypeAdditionalID = typeAdditionalID;
    }

    public UUID getDocumentTreeNodeID() {
        return DocumentTreeNodeID;
    }

    public void setDocumentTreeNodeID(UUID documentTreeNodeID) {
        DocumentTreeNodeID = documentTreeNodeID;
    }

    public UUID getDocumentTreeID() {
        return DocumentTreeID;
    }

    public void setDocumentTreeID(UUID documentTreeID) {
        DocumentTreeID = documentTreeID;
    }

    public String getDocumentTreeName() {
        return DocumentTreeName;
    }

    public void setDocumentTreeName(String documentTreeName) {
        DocumentTreeName = documentTreeName;
    }

    public UUID getPreviousVersionID() {
        return PreviousVersionID;
    }

    public void setPreviousVersionID(UUID previousVersionID) {
        PreviousVersionID = previousVersionID;
    }

    public String getPreviousVersionName() {
        return PreviousVersionName;
    }

    public void setPreviousVersionName(String previousVersionName) {
        PreviousVersionName = previousVersionName;
    }

    public String getNodeType() {
        return NodeType;
    }

    public void setNodeType(String nodeType) {
        NodeType = nodeType;
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

    public String getPublicDescription() {
        return PublicDescription;
    }

    public void setPublicDescription(String publicDescription) {
        PublicDescription = publicDescription;
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

    public UUID getParentNodeID() {
        return ParentNodeID;
    }

    public void setParentNodeID(UUID parentNodeID) {
        ParentNodeID = parentNodeID;
    }

    public String getParentName() {
        return ParentName;
    }

    public void setParentName(String parentName) {
        ParentName = parentName;
    }

    public Integer getLikesCount() {
        return LikesCount;
    }

    public void setLikesCount(Integer likesCount) {
        LikesCount = likesCount;
    }

    public Boolean getLikeStatus() {
        return LikeStatus;
    }

    public void setLikeStatus(Boolean likeStatus) {
        LikeStatus = likeStatus;
    }

    public String getMembershipStatus() {
        return MembershipStatus;
    }

    public void setMembershipStatus(String membershipStatus) {
        MembershipStatus = membershipStatus;
    }

    public Integer getVisitsCount() {
        return VisitsCount;
    }

    public void setVisitsCount(Integer visitsCount) {
        VisitsCount = visitsCount;
    }

    public List<String> getTags() {
        return Tags;
    }

    public void setTags(List<String> tags) {
        Tags = tags;
    }

    public List<NodeCreator> getContributors() {
        return Contributors;
    }

    public void setContributors(List<NodeCreator> contributors) {
        Contributors = contributors;
    }

    public UUID getOwnerID() {
        return OwnerID;
    }

    public void setOwnerID(UUID ownerID) {
        OwnerID = ownerID;
    }

    public String getOwnerName() {
        return OwnerName;
    }

    public void setOwnerName(String ownerName) {
        OwnerName = ownerName;
    }

    public UUID getAdminAreaID() {
        return AdminAreaID;
    }

    public void setAdminAreaID(UUID adminAreaID) {
        AdminAreaID = adminAreaID;
    }

    public String getAdminAreaName() {
        return AdminAreaName;
    }

    public void setAdminAreaName(String adminAreaName) {
        AdminAreaName = adminAreaName;
    }

    public String getAdminAreaType() {
        return AdminAreaType;
    }

    public void setAdminAreaType(String adminAreaType) {
        AdminAreaType = adminAreaType;
    }

    public ConfidentialityLevel getConfidentialityLevel() {
        return ConfidentialityLevel;
    }

    public void setConfidentialityLevel(ConfidentialityLevel confidentialityLevel) {
        ConfidentialityLevel = confidentialityLevel;
    }

    public Boolean getSearchable() {
        return Searchable;
    }

    public void setSearchable(Boolean searchable) {
        Searchable = searchable;
    }

    public Boolean getHideCreators() {
        return HideCreators;
    }

    public void setHideCreators(Boolean hideCreators) {
        HideCreators = hideCreators;
    }

    public DateTime getPublicationDate() {
        return PublicationDate;
    }

    public void setPublicationDate(DateTime publicationDate) {
        PublicationDate = publicationDate;
    }

    public DateTime getExpirationDate() {
        return ExpirationDate;
    }

    public void setExpirationDate(DateTime expirationDate) {
        ExpirationDate = expirationDate;
    }

    public NodeStatus getStatus() {
        return Status;
    }

    public void setStatus(NodeStatus status) {
        Status = status;
    }

    public String getWFState() {
        return WFState;
    }

    public void setWFState(String WFState) {
        this.WFState = WFState;
    }

    public Double getScore() {
        return Score;
    }

    public void setScore(Double score) {
        Score = score;
    }

    public Boolean getFreeUser() {
        return IsFreeUser;
    }

    public void setFreeUser(Boolean freeUser) {
        IsFreeUser = freeUser;
    }

    public Boolean getArchive() {
        return Archive;
    }

    public void setArchive(Boolean archive) {
        Archive = archive;
    }

    public Boolean getHasWikiContent() {
        return HasWikiContent;
    }

    public void setHasWikiContent(Boolean hasWikiContent) {
        HasWikiContent = hasWikiContent;
    }

    public Boolean getHasFormContent() {
        return HasFormContent;
    }

    public void setHasFormContent(Boolean hasFormContent) {
        HasFormContent = hasFormContent;
    }

    public Boolean getHasChild() {
        return HasChild;
    }

    public void setHasChild(Boolean hasChild) {
        HasChild = hasChild;
    }

    public boolean isPersonal(UUID currentUserId)
    {
        return Creator.getUserID() == currentUserId && (this.Status == NodeStatus.NotSet ||
                Status == NodeStatus.SentBackForRevision || Status == NodeStatus.Personal || Status == NodeStatus.Rejected);
    }

    public RVJSON toJson(UUID applicationId, boolean iconUrl)
    {
        return (new RVJSON())
                .add("NodeID", NodeID)
                .add("AdditionalID", Base64.encode(AdditionalID))
                .add("NodeTypeID", NodeTypeID)
                .add("Name", Base64.encode(Name))
                .add("NodeType", Base64.encode(NodeType))
                .add("CreationDate", CreationDate)
                .add("Status", Status == NodeStatus.NotSet ? null : Status.toString())
                .add("WFState", Base64.encode(WFState))
                .add("VisitsCount", VisitsCount)
                .add("LikesCount", LikesCount)
                .add("HasChild", HasChild)
                .add("IconURL", applicationId == null || !iconUrl ? null :
                        documentUtilities.getIconUrl(applicationId, NodeID, DefaultIconTypes.Node, NodeTypeID, false))
                .add("Creator", Creator == null ? null : Creator.toJson(applicationId, true))
                .add("Archived", Archive);
    }

    public RVJSON toJson(){
        return toJson(null, false);
    }
}
