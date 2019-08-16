package com.raaivan.modules.corenetwork.beans;

import com.raaivan.modules.corenetwork.enums.ServiceAdminType;
import com.raaivan.util.RVBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class CNService {
    private NodeType NodeType;
    private String Title;
    private String Description;
    private ServiceAdminType AdminType;
    private Node AdminNode;
    private Integer MaxAcceptableAdminLevel;
    private List<String> LimitAttachedFilesTo;
    private Integer MaxAttachedFileSize;
    private Integer MaxAttachedFilesCount;
    private Boolean EnableContribution;
    private Boolean NoContent;
    private Boolean IsDocument;
    private Boolean IsKnowledge;
    private Boolean IsTree;
    private Boolean EditableForAdmin;
    private Boolean EditableForCreator;
    private Boolean EditableForContributors;
    private Boolean EditableForExperts;
    private Boolean EditableForMembers;
    private Boolean EditSuggestion;

    public CNService()
    {
        NodeType = RVBeanFactory.getBean(com.raaivan.modules.corenetwork.beans.NodeType.class);
        AdminType = ServiceAdminType.NotSet;
        AdminNode = RVBeanFactory.getBean(Node.class);
        LimitAttachedFilesTo = new ArrayList<>();
    }

    public com.raaivan.modules.corenetwork.beans.NodeType getNodeType() {
        return NodeType;
    }

    public void setNodeType(com.raaivan.modules.corenetwork.beans.NodeType nodeType) {
        NodeType = nodeType;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public ServiceAdminType getAdminType() {
        return AdminType;
    }

    public void setAdminType(ServiceAdminType adminType) {
        AdminType = adminType;
    }

    public Node getAdminNode() {
        return AdminNode;
    }

    public void setAdminNode(Node adminNode) {
        AdminNode = adminNode;
    }

    public Integer getMaxAcceptableAdminLevel() {
        return MaxAcceptableAdminLevel;
    }

    public void setMaxAcceptableAdminLevel(Integer maxAcceptableAdminLevel) {
        MaxAcceptableAdminLevel = maxAcceptableAdminLevel;
    }

    public List<String> getLimitAttachedFilesTo() {
        return LimitAttachedFilesTo;
    }

    public void setLimitAttachedFilesTo(List<String> limitAttachedFilesTo) {
        LimitAttachedFilesTo = limitAttachedFilesTo;
    }

    public Integer getMaxAttachedFileSize() {
        return MaxAttachedFileSize;
    }

    public void setMaxAttachedFileSize(Integer maxAttachedFileSize) {
        MaxAttachedFileSize = maxAttachedFileSize;
    }

    public Integer getMaxAttachedFilesCount() {
        return MaxAttachedFilesCount;
    }

    public void setMaxAttachedFilesCount(Integer maxAttachedFilesCount) {
        MaxAttachedFilesCount = maxAttachedFilesCount;
    }

    public Boolean getEnableContribution() {
        return EnableContribution;
    }

    public void setEnableContribution(Boolean enableContribution) {
        EnableContribution = enableContribution;
    }

    public Boolean getNoContent() {
        return NoContent;
    }

    public void setNoContent(Boolean noContent) {
        NoContent = noContent;
    }

    public Boolean getDocument() {
        return IsDocument;
    }

    public void setDocument(Boolean document) {
        IsDocument = document;
    }

    public Boolean getKnowledge() {
        return IsKnowledge;
    }

    public void setKnowledge(Boolean knowledge) {
        IsKnowledge = knowledge;
    }

    public Boolean getTree() {
        return IsTree;
    }

    public void setTree(Boolean tree) {
        IsTree = tree;
    }

    public Boolean getEditableForAdmin() {
        return EditableForAdmin;
    }

    public void setEditableForAdmin(Boolean editableForAdmin) {
        EditableForAdmin = editableForAdmin;
    }

    public Boolean getEditableForCreator() {
        return EditableForCreator;
    }

    public void setEditableForCreator(Boolean editableForCreator) {
        EditableForCreator = editableForCreator;
    }

    public Boolean getEditableForContributors() {
        return EditableForContributors;
    }

    public void setEditableForContributors(Boolean editableForContributors) {
        EditableForContributors = editableForContributors;
    }

    public Boolean getEditableForExperts() {
        return EditableForExperts;
    }

    public void setEditableForExperts(Boolean editableForExperts) {
        EditableForExperts = editableForExperts;
    }

    public Boolean getEditableForMembers() {
        return EditableForMembers;
    }

    public void setEditableForMembers(Boolean editableForMembers) {
        EditableForMembers = editableForMembers;
    }

    public Boolean getEditSuggestion() {
        return EditSuggestion;
    }

    public void setEditSuggestion(Boolean editSuggestion) {
        EditSuggestion = editSuggestion;
    }
}
