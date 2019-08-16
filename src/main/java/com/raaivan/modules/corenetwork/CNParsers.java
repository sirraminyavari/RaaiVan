package com.raaivan.modules.corenetwork;

import com.raaivan.modules.corenetwork.beans.*;
import com.raaivan.modules.corenetwork.enums.ExtensionType;
import com.raaivan.modules.corenetwork.enums.NodeMemberStatus;
import com.raaivan.modules.corenetwork.enums.NodeStatus;
import com.raaivan.modules.corenetwork.enums.ServiceAdminType;
import com.raaivan.modules.rv.beans.MutableUUID;
import com.raaivan.util.dbutil.RVConnection;
import com.raaivan.util.dbutil.RVResultSet;
import com.raaivan.util.PublicMethods;
import com.raaivan.util.RVBeanFactory;
import com.raaivan.util.rvsettings.RaaiVanSettings;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableLong;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@ApplicationScope
public class CNParsers {
    private PublicMethods publicMethods;
    private RaaiVanSettings raaivaSettings;
    private RVConnection rvConnection;

    @Autowired
    public void _setDependencies(PublicMethods publicMethods, RaaiVanSettings raaivanSettings, RVConnection rvConnection) {
        if (this.publicMethods == null) this.publicMethods = publicMethods;
        if (this.raaivaSettings == null) this.raaivaSettings = raaivanSettings;
        if (this.rvConnection == null) this.rvConnection = rvConnection;
    }

    public List<NodeType> nodeTypes(UUID applicationId, RVResultSet resultSet, MutableLong totalCount) {
        totalCount.setValue(0);

        List<NodeType> ret = new ArrayList<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                NodeType e = RVBeanFactory.getBean(NodeType.class);

                e.setNodeTypeID(publicMethods.parseUUID((String) resultSet.getValue(i, "NodeTypeID")));
                e.setParentID(publicMethods.parseUUID((String) resultSet.getValue(i, "ParentID")));
                e.setName((String) resultSet.getValue(i, "Name"));
                e.setAdditionalID((String) resultSet.getValue(i, "AdditionalID"));
                e.setAdditionalIDPattern((String) resultSet.getValue(i, "AdditionalIDPattern"));

                if (StringUtils.isBlank(e.getAdditionalIDPattern())) {
                    e.setAdditionalIDPattern(raaivaSettings.getCoreNetwork().DefaultAdditionalIDPattern(applicationId));
                    e.setHasDefaultPattern(true);
                } else e.setHasDefaultPattern(false);

                e.setArchive((Boolean) resultSet.getValue(i, "Archive"));

                ret.add(e);
            } catch (Exception e) {
            }
        }

        if (resultSet.getTablesCount() > 1)
            publicMethods.tryParseLong(resultSet.getValue(1, 0, 0, 0).toString(), totalCount);

        return ret;
    }

    public List<NodeType> nodeTypes(UUID applicationId, RVResultSet resultSet) {
        MutableLong totalCount = new MutableLong(0);
        return nodeTypes(applicationId, resultSet, totalCount);
    }

    public List<Node> nodes(RVResultSet resultSet, boolean full, MutableLong totalCount) {
        totalCount.setValue(0);

        List<Node> ret = new ArrayList<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                Node e = RVBeanFactory.getBean(Node.class);

                e.setNodeID(publicMethods.parseUUID((String) resultSet.getValue(i, "NodeID")));
                e.setNodeTypeID(publicMethods.parseUUID((String) resultSet.getValue(i, "NodeTypeID")));
                e.setNodeType((String) resultSet.getValue(i, "NodeType"));
                e.setTypeAdditionalID((String) resultSet.getValue(i, "NodeTypeAdditionalID"));
                e.setName((String) resultSet.getValue(i, "Name"));
                e.setAdditionalID_Main((String) resultSet.getValue(i, "AdditionalID_Main"));
                e.setAdditionalID((String) resultSet.getValue(i, "AdditionalID"));
                e.setParentNodeID(publicMethods.parseUUID((String) resultSet.getValue(i, "ParentNodeID")));
                e.getCreator().setUserID(publicMethods.parseUUID((String) resultSet.getValue(i, "CreatorUserID")));
                e.setCreationDate((DateTime) resultSet.getValue(i, "CreationDate"));
                e.setAdminAreaID(publicMethods.parseUUID((String) resultSet.getValue(i, "AdminAreaID")));
                e.setDocumentTreeNodeID(publicMethods.parseUUID((String) resultSet.getValue(i, "DocumentTreeNodeID")));
                e.setStatus(publicMethods.lookupEnum(NodeStatus.class,
                        (String) resultSet.getValue(i, "Status"), NodeStatus.NotSet));
                e.setWFState((String) resultSet.getValue(i, "WFState"));
                e.setArchive((Boolean) resultSet.getValue(i, "Archive"));

                if (full) {
                    e.setDocumentTreeID(publicMethods.parseUUID((String) resultSet.getValue(i, "DocumentTreeID")));
                    e.setDocumentTreeName((String) resultSet.getValue(i, "DocumentTreeName"));
                    e.setPreviousVersionID(publicMethods.parseUUID((String) resultSet.getValue(i, "PreviousVersionID")));
                    e.setPreviousVersionName((String) resultSet.getValue(i, "PreviousVersionName"));
                    e.setDescription((String) resultSet.getValue(i, "Description"));
                    e.setPublicDescription((String) resultSet.getValue(i, "PublicDescription"));
                    e.setTags(rvConnection.getTagsList((String) resultSet.getValue(i, "Tags")));
                    e.getCreator().setUserName((String) resultSet.getValue(i, "CreatorUserName"));
                    e.getCreator().setFirstName((String) resultSet.getValue(i, "CreatorFirstName"));
                    e.getCreator().setLastName((String) resultSet.getValue(i, "CreatorLastName"));
                    e.setLikesCount((Integer) resultSet.getValue(i, "LikesCount"));
                    e.setLikeStatus((Boolean) resultSet.getValue(i, "LikeStatus"));
                    e.setMembershipStatus((String) resultSet.getValue(i, "MembershipStatus"));
                    e.setVisitsCount((Integer) resultSet.getValue(i, "VisitsCount"));
                    e.setSearchable((Boolean) resultSet.getValue(i, "Searchable"));
                    e.setHideCreators((Boolean) resultSet.getValue(i, "HideCreators"));
                    e.setAdminAreaName((String) resultSet.getValue(i, "AdminAreaName"));
                    e.setAdminAreaType((String) resultSet.getValue(i, "AdminAreaType"));
                    e.getConfidentialityLevel()
                            .setID(publicMethods.parseUUID((String) resultSet.getValue(i, "ConfidentialityLevelID")));
					e.getConfidentialityLevel().setLevelID((Integer) resultSet.getValue(i, "ConfidentialityLevelNum"));
                    e.getConfidentialityLevel().setTitle((String) resultSet.getValue(i, "ConfidentialityLevel"));
                    e.setOwnerID(publicMethods.parseUUID((String) resultSet.getValue(i, "OwnerID")));
                    e.setOwnerName((String) resultSet.getValue(i, "OwnerName"));
                    e.setPublicationDate((DateTime) resultSet.getValue(i, "PublicationDate"));
                    e.setExpirationDate((DateTime) resultSet.getValue(i, "ExpirationDate"));
                    e.setScore((Double) resultSet.getValue(i, "Score"));
                    e.setFreeUser((Boolean) resultSet.getValue(i, "IsFreeUser"));
                    e.setHasWikiContent((Boolean) resultSet.getValue(i, "HasWikiContent"));
                    e.setHasFormContent((Boolean) resultSet.getValue(i, "HasFormContent"));
                }

                ret.add(e);
            } catch (Exception e) {
            }
        }

        if (resultSet.getTablesCount() > 1)
            publicMethods.tryParseLong(resultSet.getValue(1, 0, 0, 0).toString(), totalCount);

        return ret;
    }

    public List<Node> nodes(RVResultSet resultSet, boolean full) {
        MutableLong totalCount = new MutableLong(0);
        return nodes(resultSet, full, totalCount);
    }

    public List<Node> nodes(RVResultSet resultSet) {
        return nodes(resultSet, false);
    }

    public List<Node> popularNodes(RVResultSet resultSet, MutableLong totalCount) {
        totalCount.setValue(0);

        List<Node> ret = new ArrayList<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                Node e = RVBeanFactory.getBean(Node.class);

                e.setNodeID(publicMethods.parseUUID((String) resultSet.getValue(i, "NodeID")));
                e.setNodeTypeID(publicMethods.parseUUID((String) resultSet.getValue(i, "NodeTypeID")));
                e.setName((String) resultSet.getValue(i, "Name"));
                e.setNodeType((String) resultSet.getValue(i, "NodeType"));
                e.setVisitsCount((Integer) resultSet.getValue(i, "VisitsCount"));
                e.setLikesCount((Integer) resultSet.getValue(i, "LikesCount"));

                publicMethods.tryParseLong(resultSet.getValue(i, "TotalCount", 0).toString(), totalCount);

                ret.add(e);
            } catch (Exception e) {
            }
        }

        return ret;
    }

    public List<Relation> relations(RVResultSet resultSet) {
        List<Relation> ret = new ArrayList<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                Relation e = RVBeanFactory.getBean(Relation.class);
                Node relatedNode = RVBeanFactory.getBean(Node.class);

                UUID nodeId = publicMethods.parseUUID((String) resultSet.getValue(i, "NodeID"));
                Boolean isInRelation = (Boolean) resultSet.getValue(i, "IsInRelation");

                relatedNode.setNodeID(publicMethods.parseUUID((String) resultSet.getValue(i, "RelatedNodeID")));
                relatedNode.setNodeTypeID(publicMethods.parseUUID((String) resultSet.getValue(i, "RelatedNodeTypeID")));
                relatedNode.setName((String) resultSet.getValue(i, "RelatedNodeName"));
                relatedNode.setNodeType((String) resultSet.getValue(i, "RelatedNodeTypeName"));
                relatedNode.setTypeAdditionalID((String) resultSet.getValue(i, "RelatedNodeTypeAdditionalID"));

                if (isInRelation) {
                    e.getDestination().setNodeID(nodeId);
                    e.setSource(relatedNode);
                } else {
                    e.getSource().setNodeID(nodeId);
                    e.setDestination(relatedNode);
                }

                Relation x = ret.stream().filter(u -> u.getSource().getNodeID() == e.getDestination().getNodeID() &&
                        u.getDestination().getNodeID() == e.getSource().getNodeID()).findFirst().orElse(null);

                if (x != null) x.setBidirectional(true);
                else ret.add(e);
            } catch (Exception e) {
            }
        }

        return ret;
    }

    public List<NodesCount> nodesCount(RVResultSet resultSet) {
        List<NodesCount> ret = new ArrayList<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                NodesCount e = RVBeanFactory.getBean(NodesCount.class);

                e.setOrder((Integer) resultSet.getValue(i, "Order"));
                e.setReverseOrder((Integer) resultSet.getValue(i, "ReverseOrder"));
                e.setNodeTypeID(publicMethods.parseUUID((String) resultSet.getValue(i, "NodeTypeID")));
                e.setNodeTypeAdditionalID((String) resultSet.getValue(i, "NodeTypeAdditionalID"));
                e.setTypeName((String) resultSet.getValue(i, "TypeName"));
                e.setCount((Integer) resultSet.getValue(i, "NodesCount"));

                ret.add(e);
            } catch (Exception e) {
            }
        }

        return ret;
    }

    public List<NodeMember> nodeMembers(RVResultSet resultSet, boolean parseNode, boolean parseUser, MutableLong totalCount) {
        totalCount.setValue(0);

        List<NodeMember> ret = new ArrayList<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                NodeMember e = RVBeanFactory.getBean(NodeMember.class);

                e.getNode().setNodeID(publicMethods.parseUUID((String) resultSet.getValue(i, "NodeID")));
                e.getMember().setUserID(publicMethods.parseUUID((String) resultSet.getValue(i, "UserID")));
                e.setMembershipDate((DateTime) resultSet.getValue(i, "MembershipDate"));
                e.setAdmin((Boolean) resultSet.getValue(i, "IsAdmin"));
                e.setPending((Boolean) resultSet.getValue(i, "IsPending"));
                e.setStatus(publicMethods.lookupEnum(NodeMemberStatus.class,
                        (String) resultSet.getValue(i, "Status"), NodeMemberStatus.NotSet));
                e.setAcceptionDate((DateTime) resultSet.getValue(i, "AcceptionDate"));

                if (parseNode) {
                    e.getNode().setAdditionalID((String) resultSet.getValue(i, "NodeAdditionalID"));
                    e.getNode().setName((String) resultSet.getValue(i, "NodeName"));
                    e.getNode().setNodeTypeID(publicMethods.parseUUID((String) resultSet.getValue(i, "NodeTypeID")));
                    e.getNode().setNodeType((String) resultSet.getValue(i, "NodeType"));
                }

                if (parseUser) {
                    e.getMember().setUserName((String) resultSet.getValue(i, "UserName"));
                    e.getMember().setFirstName((String) resultSet.getValue(i, "FirstName"));
                    e.getMember().setLastName((String) resultSet.getValue(i, "LastName"));
                }

                ret.add(e);
            } catch (Exception e) {
            }
        }

        if (resultSet.getTablesCount() > 1)
            publicMethods.tryParseLong(resultSet.getValue(1, 0, 0, 0).toString(), totalCount);

        return ret;
    }

    public List<NodeMember> nodeMembers(RVResultSet resultSet, boolean parseNode, boolean parseUser){
        MutableLong totalCount = new MutableLong(0);
        return nodeMembers(resultSet, parseNode, parseUser, totalCount);
    }

    public List<NodeList> lists(RVResultSet resultSet) {
        List<NodeList> ret = new ArrayList<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                NodeList e = RVBeanFactory.getBean(NodeList.class);

                e.setListID(publicMethods.parseUUID((String) resultSet.getValue(i, "ListID")));
                e.setName((String) resultSet.getValue(i, "ListName"));
                e.setAdditionalID((String) resultSet.getValue(i, "AdditionalID"));
                e.setNodeTypeID(publicMethods.parseUUID((String) resultSet.getValue(i, "NodeTypeID")));
                e.setNodeType((String) resultSet.getValue(i, "NodeType"));

                ret.add(e);
            } catch (Exception e) {
            }
        }

        return ret;
    }

    public List<HierarchyAdmin> hierarchyAdmins(RVResultSet resultSet) {
        List<HierarchyAdmin> ret = new ArrayList<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                HierarchyAdmin e = RVBeanFactory.getBean(HierarchyAdmin.class);

                e.getNode().setNodeID(publicMethods.parseUUID((String) resultSet.getValue(i, "NodeID")));
                e.getUser().setUserID(publicMethods.parseUUID((String) resultSet.getValue(i, "UserID")));
                e.setLevel((Integer) resultSet.getValue(i, "Level"));

                ret.add(e);
            } catch (Exception e) {
            }
        }

        return ret;
    }

    public List<Tag> tags(RVResultSet resultSet) {
        List<Tag> ret = new ArrayList<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                Tag e = RVBeanFactory.getBean(Tag.class);

                e.setTagID(publicMethods.parseUUID((String) resultSet.getValue(i, "TagID")));
                e.setText((String) resultSet.getValue(i, "Tag"));
                e.setApproved((Boolean) resultSet.getValue(i, "IsApproved"));
                e.setCallsCount((Integer) resultSet.getValue(i, "CallsCount"));

                ret.add(e);
            } catch (Exception e) {
            }
        }

        return ret;
    }

    public List<NodeCreator> nodeCreators(RVResultSet resultSet, boolean full) {
        List<NodeCreator> ret = new ArrayList<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                NodeCreator e = RVBeanFactory.getBean(NodeCreator.class);

                e.setNodeID(publicMethods.parseUUID((String) resultSet.getValue(i, "NodeID")));
                e.getUser().setUserID(publicMethods.parseUUID((String) resultSet.getValue(i, "UserID")));
                e.setCollaborationShare((Double) resultSet.getValue(i, "CollaborationShare"));
                e.setStatus((String) resultSet.getValue(i, "Status"));

                if (full) {
                    e.getUser().setUserName((String) resultSet.getValue(i, "UserName"));
                    e.getUser().setFirstName((String) resultSet.getValue(i, "FirstName"));
                    e.getUser().setLastName((String) resultSet.getValue(i, "LastName"));
                }

                ret.add(e);
            } catch (Exception e) {
            }
        }

        return ret;
    }

    public List<UUID> fanUserIds(RVResultSet resultSet, MutableLong totalCount) {
        totalCount.setValue(0);

        List<UUID> ret = new ArrayList<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                UUID userId = publicMethods.parseUUID((String) resultSet.getValue(i, "UserID"));
                publicMethods.tryParseLong(resultSet.getValue(i, "TotalCount", 0).toString(), totalCount);

                if (userId != null) ret.add(userId);
            } catch (Exception e) {
            }
        }

        return ret;
    }

    public List<Expert> experts(RVResultSet resultSet, MutableLong totalCount) {
        totalCount.setValue(0);

        List<Expert> ret = new ArrayList<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                Expert e = RVBeanFactory.getBean(Expert.class);

                publicMethods.tryParseLong(resultSet.getValue(i, "TotalCount", 0).toString(), totalCount);

                e.getNode().setNodeID(publicMethods.parseUUID((String) resultSet.getValue(i, "NodeID")));
                e.getNode().setAdditionalID((String) resultSet.getValue(i, "NodeAdditionalID"));
                e.getNode().setName((String) resultSet.getValue(i, "NodeName"));
                e.getNode().setNodeTypeID(publicMethods.parseUUID((String) resultSet.getValue(i, "NodeTypeID")));
                e.getNode().setNodeType((String) resultSet.getValue(i, "NodeType"));
                e.getUser().setUserID(publicMethods.parseUUID((String) resultSet.getValue(i, "ExpertUserID")));
                e.getUser().setUserName((String) resultSet.getValue(i, "ExpertUserName"));
                e.getUser().setFirstName((String) resultSet.getValue(i, "ExpertFirstName"));
                e.getUser().setLastName((String) resultSet.getValue(i, "ExpertLastName"));

                ret.add(e);
            } catch (Exception e) {
            }
        }

        return ret;
    }

    public List<Expert> experts(RVResultSet resultSet){
        MutableLong totalCount = new MutableLong(0);
        return experts(resultSet, totalCount);
    }

    public List<Expert> expertiseSuggestions(RVResultSet resultSet) {
        List<Expert> ret = new ArrayList<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                Expert e = RVBeanFactory.getBean(Expert.class);

                e.getNode().setNodeID(publicMethods.parseUUID((String) resultSet.getValue(i, "NodeID")));
                e.getNode().setName((String) resultSet.getValue(i, "NodeName"));
                e.getNode().setNodeType((String) resultSet.getValue(i, "NodeType"));
                e.getUser().setUserID(publicMethods.parseUUID((String) resultSet.getValue(i, "ExpertUserID")));
                e.getUser().setUserName((String) resultSet.getValue(i, "ExpertUserName"));
                e.getUser().setFirstName((String) resultSet.getValue(i, "ExpertFirstName"));
                e.getUser().setLastName((String) resultSet.getValue(i, "ExpertLastName"));

                ret.add(e);
            } catch (Exception e) {
            }
        }

        return ret;
    }

    public List<NodeInfo> nodeInfo(RVResultSet resultSet) {
        List<NodeInfo> ret = new ArrayList<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                NodeInfo e = RVBeanFactory.getBean(NodeInfo.class);

                e.setNodeID(publicMethods.parseUUID((String) resultSet.getValue(i, "NodeID")));
                e.setNodeTypeID(publicMethods.parseUUID((String) resultSet.getValue(i, "NodeTypeID")));
                e.setDescription((String) resultSet.getValue(i, "Description"));
                e.getCreator().setUserID(publicMethods.parseUUID((String) resultSet.getValue(i, "CreatorUserID")));
                e.getCreator().setUserName((String) resultSet.getValue(i, "CreatorUserName"));
                e.getCreator().setFirstName((String) resultSet.getValue(i, "CreatorFirstName"));
                e.getCreator().setLastName((String) resultSet.getValue(i, "CreatorLastName"));
                e.setContributorsCount((Integer) resultSet.getValue(i, "ContributorsCount"));
                e.setLikesCount((Integer) resultSet.getValue(i, "LikesCount"));
                e.setVisitsCount((Integer) resultSet.getValue(i, "VisitsCount"));
                e.setExpertsCount((Integer) resultSet.getValue(i, "ExpertsCount"));
                e.setMembersCount((Integer) resultSet.getValue(i, "MembersCount"));
                e.setChildsCount((Integer) resultSet.getValue(i, "ChildsCount"));
                e.setRelatedNodesCount((Integer) resultSet.getValue(i, "RelatedNodesCount"));
                e.setLikeStatus((Boolean) resultSet.getValue(i, "LikeStatus"));
                e.setTags(rvConnection.getTagsList((String) resultSet.getValue(i, "Tags")));

                ret.add(e);
            } catch (Exception e) {
            }
        }

        return ret;
    }

    public List<Extension> extensions(RVResultSet resultSet) {
        List<Extension> ret = new ArrayList<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                Extension e = RVBeanFactory.getBean(Extension.class);

                e.setOwnerID(publicMethods.parseUUID((String) resultSet.getValue(i, "OwnerID")));
                e.setTitle((String) resultSet.getValue(i, "Title"));
                e.setDisabled((Boolean) resultSet.getValue(i, "Disabled"));
                e.setType(publicMethods.lookupEnum(ExtensionType.class,
                        (String) resultSet.getValue(i, "Extension"), ExtensionType.NotSet));
                e.setInitialized(true);

                if (e.getType() != ExtensionType.NotSet) ret.add(e);
            } catch (Exception e) {
            }
        }

        return ret;
    }

    public List<CNService> services(RVResultSet resultSet) {
        List<CNService> ret = new ArrayList<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                CNService e = RVBeanFactory.getBean(CNService.class);

                e.getNodeType().setNodeTypeID(publicMethods.parseUUID((String) resultSet.getValue(i, "NodeTypeID")));
                e.getNodeType().setName((String) resultSet.getValue(i, "NodeType"));
                e.setTitle((String) resultSet.getValue(i, "ServiceTitle"));
                e.setDescription((String) resultSet.getValue(i, "ServiceDescription"));
                e.setEnableContribution((Boolean) resultSet.getValue(i, "EnableContribution"));
                e.setNoContent((Boolean) resultSet.getValue(i, "NoContent"));
                e.setDocument((Boolean) resultSet.getValue(i, "IsDocument"));
                e.setKnowledge((Boolean) resultSet.getValue(i, "IsKnowledge"));
                e.setTree((Boolean) resultSet.getValue(i, "IsTree"));
                e.getAdminNode().setNodeID(publicMethods.parseUUID((String) resultSet.getValue(i, "AdminNodeID")));
                e.setMaxAcceptableAdminLevel((Integer) resultSet.getValue(i, "MaxAcceptableAdminLevel"));

                String exts = (String) resultSet.getValue(i, "LimitAttachedFilesTo");
                if (!StringUtils.isBlank(exts)) {
                    e.setLimitAttachedFilesTo(Arrays.stream(exts.split(",")).map(String::trim)
                            .filter(u -> !StringUtils.isBlank(u)).collect(Collectors.toList()));
                }

                e.setMaxAttachedFileSize((Integer) resultSet.getValue(i, "MaxAttachedFileSize"));
                e.setMaxAttachedFilesCount((Integer) resultSet.getValue(i, "MaxAttachedFilesCount"));
                e.setEditableForAdmin((Boolean) resultSet.getValue(i, "EditableForAdmin"));
                e.setEditableForCreator((Boolean) resultSet.getValue(i, "EditableForCreator"));
                e.setEditableForContributors((Boolean) resultSet.getValue(i, "EditableForOwners"));
                e.setEditableForExperts((Boolean) resultSet.getValue(i, "EditableForExperts"));
                e.setEditableForMembers((Boolean) resultSet.getValue(i, "EditableForMembers"));
                e.setEditSuggestion((Boolean) resultSet.getValue(i, "EditSuggestion"));
                e.setAdminType(publicMethods.lookupEnum(ServiceAdminType.class,
                        (String) resultSet.getValue(i, "AdminType"), ServiceAdminType.NotSet));

                ret.add(e);
            } catch (Exception e) {
            }
        }

        return ret;
    }

    public boolean user2nodeStatus(RVResultSet resultSet, MutableUUID nodeTypeId, MutableUUID areaId,
                                   MutableBoolean isCreator, MutableBoolean isContributor, MutableBoolean isExpert,
                                   MutableBoolean isMember, MutableBoolean isAdminMember, MutableBoolean isServiceAdmin) {
        boolean result = true;

        try {
            nodeTypeId.setValue(publicMethods.parseUUID((String) resultSet.getValue(0, "NodeTypeID")));
            areaId.setValue(publicMethods.parseUUID((String) resultSet.getValue(0, "AreaID")));

            publicMethods.tryParseBoolean(resultSet.getValue(0, "IsCreator").toString(), isCreator);
            publicMethods.tryParseBoolean(resultSet.getValue(0, "IsContributor").toString(), isContributor);
            publicMethods.tryParseBoolean(resultSet.getValue(0, "IsExpert").toString(), isExpert);
            publicMethods.tryParseBoolean(resultSet.getValue(0, "IsMember").toString(), isMember);
            publicMethods.tryParseBoolean(resultSet.getValue(0, "IsAdminMember").toString(), isAdminMember);
            publicMethods.tryParseBoolean(resultSet.getValue(0, "IsServiceAdmin").toString(), isServiceAdmin);
        } catch (Exception ex) {
            result = false;
        }

        return result;
    }

    public List<ExploreItem> exploreItems(RVResultSet resultSet, MutableLong totalCount) {
        totalCount.setValue(0);

        List<ExploreItem> ret = new ArrayList<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                ExploreItem e = RVBeanFactory.getBean(ExploreItem.class);

                publicMethods.tryParseLong(resultSet.getValue(i, "TotalCount", 0).toString(), totalCount);

                e.setBaseID(publicMethods.parseUUID((String) resultSet.getValue(i, "BaseID")));
                e.setBaseTypeID(publicMethods.parseUUID((String) resultSet.getValue(i, "BaseTypeID")));
                e.setBaseName((String) resultSet.getValue(i, "BaseName"));
                e.setBaseType((String) resultSet.getValue(i, "BaseType"));
                e.setRelatedID(publicMethods.parseUUID((String) resultSet.getValue(i, "RelatedID")));
                e.setRelatedTypeID(publicMethods.parseUUID((String) resultSet.getValue(i, "RelatedTypeID")));
                e.setRelatedName((String) resultSet.getValue(i, "RelatedName"));
                e.setRelatedType((String) resultSet.getValue(i, "RelatedType"));
                e.setRelatedCreationDate((DateTime) resultSet.getValue(i, "RelatedCreationDate"));
                e.setTag((Boolean) resultSet.getValue(i, "IsTag"));
                e.setRelation((Boolean) resultSet.getValue(i, "IsRelation"));
                e.setRegistrationArea((Boolean) resultSet.getValue(i, "IsRegistrationArea"));

                ret.add(e);
            } catch (Exception e) {
            }
        }

        return ret;
    }

    public List<SimilarNode> similarNodes(RVResultSet resultSet) {
        List<SimilarNode> ret = new ArrayList<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                SimilarNode e = RVBeanFactory.getBean(SimilarNode.class);

                e.getSuggested().setNodeID(publicMethods.parseUUID((String) resultSet.getValue(i, "NodeID")));
                e.setRank((Double) resultSet.getValue(i, "Rank"));
                e.setTags((Boolean) resultSet.getValue(i, "Tags"));
                e.setFavorites((Boolean) resultSet.getValue(i, "Favorites"));
                e.setRelations((Boolean) resultSet.getValue(i, "Relations"));
                e.setExperts((Boolean) resultSet.getValue(i, "Experts"));

                ret.add(e);
            } catch (Exception e) {
            }
        }

        return ret;
    }

    public List<KnowledgableUser> knowledgableUsers(RVResultSet resultSet) {
        List<KnowledgableUser> ret = new ArrayList<>();

        for (int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i) {
            try {
                KnowledgableUser e = RVBeanFactory.getBean(KnowledgableUser.class);

                e.getUser().setUserID(publicMethods.parseUUID((String) resultSet.getValue(i, "UserID")));
                e.setRank((Double) resultSet.getValue(i, "Rank"));
                e.setExpert((Boolean) resultSet.getValue(i, "Expert"));
                e.setContributor((Boolean) resultSet.getValue(i, "Contributor"));
                e.setWikiEditor((Boolean) resultSet.getValue(i, "WikiEditor"));
                e.setMember((Boolean) resultSet.getValue(i, "Member"));
                e.setExpertOfRelatedNode((Boolean) resultSet.getValue(i, "ExpertOfRelatedNode"));
                e.setContributorOfRelatedNode((Boolean) resultSet.getValue(i, "ContributorOfRelatedNode"));
                e.setMemberOfRelatedNode((Boolean) resultSet.getValue(i, "MemberOfRelatedNode"));

                ret.add(e);
            } catch (Exception e) {
            }
        }

        return ret;
    }
}