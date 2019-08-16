package com.raaivan.modules.corenetwork;

import com.raaivan.modules.corenetwork.beans.*;
import com.raaivan.modules.corenetwork.enums.ExtensionType;
import com.raaivan.modules.corenetwork.enums.NodeMemberStatus;
import com.raaivan.modules.corenetwork.enums.ServiceAdminType;
import com.raaivan.modules.corenetwork.util.CNUtilities;
import com.raaivan.modules.forms.beans.FormFilter;
import com.raaivan.modules.privacy.PrivacyDAO;
import com.raaivan.modules.privacy.enums.PermissionType;
import com.raaivan.modules.privacy.enums.PrivacyObjectType;
import com.raaivan.modules.rv.beans.Dashboard;
import com.raaivan.modules.rv.beans.Hierarchy;
import com.raaivan.modules.rv.beans.MutableUUID;
import com.raaivan.modules.users.UsersDAO;
import com.raaivan.modules.users.beans.User;
import com.raaivan.util.PublicMethods;
import com.raaivan.util.RVBeanFactory;
import com.raaivan.util.RaaiVanConfig;
import com.raaivan.util.dbutil.RVConnection;
import com.raaivan.util.dbutil.RVStructuredParam;
import com.raaivan.util.rvsettings.RaaiVanSettings;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableLong;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.*;
import java.util.stream.Collectors;

@Service
@ApplicationScope
public class CNDAO {
    private RVConnection rvConnection;
    private CNParsers parser;
    private PublicMethods publicMethods;
    private CNUtilities cnUtilities;
    private RaaiVanSettings raaivanSettings;
    private UsersDAO usersDAO;
    private PrivacyDAO privacyDAO;

    @Autowired
    public void _setDependencies(RVConnection rvConnection, CNParsers parser, PublicMethods publicMethods,
                                 CNUtilities cnUtilities, RaaiVanSettings raaivanSettings, UsersDAO usersDAO,
                                 PrivacyDAO privacyDAO) {
        if (this.rvConnection == null) this.rvConnection = rvConnection;
        if (this.parser == null) this.parser = parser;
        if (this.publicMethods == null) this.publicMethods = publicMethods;
        if (this.raaivanSettings == null) this.raaivanSettings = raaivanSettings;
        if (this.cnUtilities == null) this.cnUtilities = cnUtilities;
        if (this.usersDAO == null) this.usersDAO = usersDAO;
        if (this.privacyDAO == null) this.privacyDAO = privacyDAO;
    }

    private String getQualifiedName(String name){
        return "[dbo].[CN_" + name + "]";
    }

    public boolean initialize(UUID applicationId)
    {
        return rvConnection.succeed(getQualifiedName("Initialize"), applicationId);
    }

    public boolean addNodeType(UUID applicationId, UUID nodeTypeId, String additionalId, String name, UUID parentId, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("AddNodeType"),
                applicationId, nodeTypeId, additionalId, name, parentId, currentUserId, publicMethods.now());
    }

    public boolean renameNodeType(UUID applicationId, UUID nodeTypeId, String name, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("RenameNodeType"),
                applicationId, nodeTypeId, name, currentUserId, publicMethods.now());
    }

    public boolean setNodeTypeAdditionalID(UUID applicationId, UUID nodeTypeId,
                                           String additionalId, UUID currentUserId, StringBuilder errorMessage)
    {
        return rvConnection.succeed(errorMessage, getQualifiedName("SetNodeTypeAdditionalID"),
                applicationId, nodeTypeId, additionalId, currentUserId, publicMethods.now());
    }

    public boolean setAdditionalIDPattern(UUID applicationId, UUID nodeTypeId, String additionalIdPattern, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("SetAdditionalIDPattern"),
                applicationId, nodeTypeId, additionalIdPattern, currentUserId, publicMethods.now());
    }

    public boolean moveNodeType(UUID applicationId, List<UUID> nodeTypeIDs, UUID parentID, UUID currentUserID)
    {
        return rvConnection.succeed(getQualifiedName("MoveNodeType"), applicationId,
                nodeTypeIDs.stream().map(UUID::toString).collect(Collectors.joining(",")), ',',
                parentID, currentUserID, publicMethods.now());
    }

    public List<NodeType> getNodeTypes(UUID applicationId, List<UUID> nodeTypeIds)
    {
        return parser.nodeTypes(applicationId, rvConnection.read(getQualifiedName("GetNodeTypesByIDs"),
                applicationId, nodeTypeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ','));
    }

    public List<NodeType> getNodeTypes(UUID applicationId,  String searchText, Boolean isKnowledge, Boolean isDocument,
                                       Boolean archive, Integer count, Long lowerBoundary, MutableLong totalCount)
    {
        return parser.nodeTypes(applicationId, rvConnection.read(getQualifiedName("GetNodeTypes"), applicationId,
                rvConnection.getSearchText(searchText), isKnowledge, isDocument, archive, count, lowerBoundary), totalCount);
    }

    public List<NodeType> getNodeTypes(UUID applicationId,  String searchText, Boolean archive)
    {
        MutableLong totalCount = new MutableLong(0);
        return getNodeTypes(applicationId, searchText, null, null, archive, null, null, totalCount);
    }

    public List<NodeType> getNodeTypes(UUID applicationId,  String searchText)
    {
        return getNodeTypes(applicationId, searchText, false);
    }

    private NodeType _getNodeType(UUID applicationId, UUID nodeTypeId, String nodeTypeAdditionalId)
    {
        return parser.nodeTypes(applicationId, rvConnection.read(getQualifiedName("GetNodeType"),
                applicationId, nodeTypeId, nodeTypeAdditionalId, null)).stream().findFirst().orElse(null);
    }

    public NodeType getNodeType(UUID applicationId, UUID nodeTypeIdOrNodeId)
    {
        return _getNodeType(applicationId, nodeTypeIdOrNodeId, null);
    }

    public NodeType getNodeType(UUID applicationId, String nodeTypeAdditionalId)
    {
        return _getNodeType(applicationId, null, nodeTypeAdditionalId);
    }

    public List<UUID> haveChildNodeTypes(UUID applicationId, List<UUID> nodeTypeIds)
    {
        return rvConnection.getUUIDList(getQualifiedName("HaveChildNodeTypes"), applicationId,
                nodeTypeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',');
    }

    public List<NodeType> getChildNodeTypes(UUID applicationId, UUID parentId, Boolean archive)
    {
        return parser.nodeTypes(applicationId,
                rvConnection.read(getQualifiedName("GetChildNodeTypes"), applicationId, parentId, archive));
    }

    public List<NodeType> getChildNodeTypes(UUID applicationId, UUID parentId){
        return getChildNodeTypes(applicationId, parentId, false);
    }

    public boolean removeNodeTypes(UUID applicationId, List<UUID> nodeTypeIds, boolean removeHierarchy, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("ArithmeticDeleteNodeTypes"), applicationId,
                nodeTypeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',',
                removeHierarchy, currentUserId, publicMethods.now());
    }

    public boolean removeNodeType(UUID applicationId, UUID nodeTypeId, boolean removeHierarchy, UUID currentUserId) {
        List<UUID> ids = new ArrayList<UUID>() {{
            add(nodeTypeId);
        }};

        return removeNodeTypes(applicationId, ids, removeHierarchy, currentUserId);
    }

    public boolean recoverNodeType(UUID applicationId, UUID nodeTypeId, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("RecoverNodeType"),
                applicationId, nodeTypeId, currentUserId, publicMethods.now());
    }

    public boolean addRelations(UUID applicationId, List<Relation> relations, UUID currentUserId)
    {

        String strRelations = relations.stream()
                .filter(x -> x.getSource().getNodeID() != null && x.getDestination().getNodeID() != null)
                .map(u -> u.getSource().getNodeID().toString() + "|" +
                        u.getDestination().getNodeID().toString() + "|" + "00000000-0000-0000-0000-000000000000")
                .collect(Collectors.joining(","));

        return rvConnection.succeed(getQualifiedName("AddRelation"),
                applicationId, strRelations, '|', ',', currentUserId, publicMethods.now());
    }

    public boolean addRelation(UUID applicationId, Relation relation, UUID currentUserId) {
        List<Relation> lst = new ArrayList<Relation>() {{
            add(relation);
        }};

        return addRelations(applicationId, lst, currentUserId);
    }

    public boolean saveRelations(UUID applicationId, UUID nodeId, List<UUID> relatedNodeIds, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("SaveRelations"), applicationId, nodeId,
                relatedNodeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',',
                currentUserId, publicMethods.now());
    }

    public boolean makeParent(UUID applicationId, List<Relation> relations, UUID currentUserId)
    {
        String strRelations = relations.stream()
                .filter(x -> x.getSource().getNodeID() != null && x.getDestination().getNodeID() != null)
                .map(u -> u.getSource().getNodeID().toString() + "|" + u.getDestination().getNodeID().toString())
                .collect(Collectors.joining(","));

        return rvConnection.succeed(getQualifiedName("MakeParent"),
                applicationId, strRelations, '|', ',', currentUserId, publicMethods.now());
    }

    public boolean makeParent(UUID applicationId, Relation relation, UUID currentUserId){
        List<Relation> lst = new ArrayList<Relation>() {{
            add(relation);
        }};

        return makeParent(applicationId, lst, currentUserId);
    }

    public boolean makeCorrelation(UUID applicationId, List<Relation> relations, UUID currentUserId)
    {
        String strRelations = relations.stream()
                .filter(x -> x.getSource().getNodeID() != null && x.getDestination().getNodeID() != null)
                .map(u -> u.getSource().getNodeID().toString() + "|" + u.getDestination().getNodeID().toString())
                .collect(Collectors.joining(","));

        return rvConnection.succeed(getQualifiedName("MakeCorrelation"),
                applicationId, strRelations, '|', ',', currentUserId, publicMethods.now());
    }

    public boolean makeCorrelation(UUID applicationId, Relation relation, UUID currentUserId){
        List<Relation> lst = new ArrayList<Relation>() {{
            add(relation);
        }};

        return makeCorrelation(applicationId, lst, currentUserId);
    }

    public boolean removeRelations(UUID applicationId, List<Relation> relations, UUID currentUserId, boolean reverseAlso)
    {
        String strRelations = relations.stream()
                .filter(x -> x.getSource().getNodeID() != null && x.getDestination().getNodeID() != null)
                .map(u -> u.getSource().getNodeID().toString() + "|" + u.getDestination().getNodeID().toString())
                .collect(Collectors.joining(","));

        return rvConnection.succeed(getQualifiedName("ArithmeticDeleteRelations"),
                applicationId, strRelations, '|', ',', null, currentUserId, publicMethods.now(), reverseAlso);
    }

    public boolean removeRelations(UUID applicationId, List<Relation> relations, UUID currentUserId){
        return removeRelations(applicationId, relations, currentUserId, false);
    }

    public boolean removeRelation(UUID applicationId, Relation relation, UUID currentUserId, boolean reverseAlso){
        List<Relation> lst = new ArrayList<Relation>() {{
            add(relation);
        }};

        return removeRelations(applicationId, lst, currentUserId, reverseAlso);
    }

    public boolean removeRelation(UUID applicationId, Relation relation, UUID currentUserId) {
        return removeRelation(applicationId, relation, currentUserId, false);
    }

    public boolean removeCorrelations(UUID applicationId, List<Relation> relations, UUID currentUserId)
    {
        String strRelations = relations.stream()
                .filter(x -> x.getSource().getNodeID() != null && x.getDestination().getNodeID() != null)
                .map(u -> u.getSource().getNodeID().toString() + "|" + u.getDestination().getNodeID().toString())
                .collect(Collectors.joining(","));

        return rvConnection.succeed(getQualifiedName("ArithmeticDeleteCorrelations"),
                applicationId, strRelations, '|', ',', currentUserId, publicMethods.now());
    }

    public boolean removeCorrelation(UUID applicationId, Relation relation, UUID currentUserId){
        List<Relation> lst = new ArrayList<Relation>() {{
            add(relation);
        }};

        return removeCorrelations(applicationId, lst, currentUserId);
    }

    public boolean unparent(UUID applicationId, List<Relation> relations, UUID currentUserId)
    {
        String strRelations = relations.stream()
                .filter(x -> x.getSource().getNodeID() != null && x.getDestination().getNodeID() != null)
                .map(u -> u.getSource().getNodeID().toString() + "|" + u.getDestination().getNodeID().toString())
                .collect(Collectors.joining(","));

        return rvConnection.succeed(getQualifiedName("Unparent"),
                applicationId, strRelations, '|', ',', currentUserId, publicMethods.now());
    }

    public boolean unparent(UUID applicationId, Relation relation, UUID currentUserId){
        List<Relation> lst = new ArrayList<Relation>() {{
            add(relation);
        }};

        return unparent(applicationId, lst, currentUserId);
    }

    private boolean _addNode(UUID applicationId, UUID nodeId, String additionalId_Main, String additionalId,
                           UUID nodeTypeId, String nodeTypeAdditionalId, UUID parentNodeId, UUID documentTreeNodeId,
                           String name, String description, List<String> tags, boolean searchable, UUID currentUserId, boolean addMember)
    {
        if(StringUtils.isBlank(additionalId_Main)) additionalId_Main = null;
        if(StringUtils.isBlank(additionalId)) additionalId = null;

        return rvConnection.succeed(getQualifiedName("AddNode"), applicationId, nodeId, additionalId_Main, additionalId,
                nodeTypeId, nodeTypeAdditionalId, documentTreeNodeId, name, description, rvConnection.getTagsText(tags),
                searchable, currentUserId, publicMethods.now(), parentNodeId, addMember, true);
    }

    public boolean addNode(UUID applicationId, UUID nodeId, String additionalId_Main, String additionalId,
                           UUID nodeTypeId, UUID parentNodeId, UUID documentTreeNodeId, String name, String description,
                           List<String> tags, boolean searchable, UUID currentUserId, boolean addMember){
        return _addNode(applicationId, nodeId, additionalId_Main, additionalId, nodeTypeId, null, parentNodeId,
                documentTreeNodeId, name, description, tags, searchable, currentUserId, addMember);
    }

    public boolean addNode(UUID applicationId, UUID nodeId, String additionalId_Main, String additionalId,
                           String nodeTypeAdditionalId, UUID parentNodeId, UUID documentTreeNodeId, String name, String description,
                           List<String> tags, boolean searchable, UUID currentUserId, boolean addMember){
        return _addNode(applicationId, nodeId, additionalId_Main, additionalId, null, nodeTypeAdditionalId, parentNodeId,
                documentTreeNodeId, name, description, tags, searchable, currentUserId, addMember);
    }

    public boolean setAdditionalID(UUID applicationId, UUID id, String additionalId_main, String additionalId, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("SetAdditionalID"),
                applicationId, id, additionalId_main, additionalId, currentUserId, publicMethods.now());
    }

    public boolean modifyNode(UUID applicationId, UUID nodeId, String name,
                              String description, List<String> tags, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("ModifyNode"),
                applicationId, nodeId, name, description, rvConnection.getTagsText(tags), currentUserId, publicMethods.now());
    }

    public boolean changeNodeType(UUID applicationId, UUID nodeId, UUID nodeTypeId, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("ChangeNodeType"),
                applicationId, nodeId, nodeTypeId, currentUserId, publicMethods.now());
    }

    public boolean setDocumentTreeNodeID(UUID applicationId, List<UUID> nodeIds, UUID documentTreeNodeId, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("SetDocumentTreeNodeID"), applicationId,
                nodeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',',
                documentTreeNodeId, currentUserId, publicMethods.now());
    }

    public boolean setDocumentTreeNodeID(UUID applicationId, UUID nodeId, UUID documentTreeNodeId, UUID currentUserId) {
        List<UUID> ids = new ArrayList<UUID>() {{
            add(nodeId);
        }};

        return setDocumentTreeNodeID(applicationId, ids, documentTreeNodeId, currentUserId);
    }

    public boolean modifyNodeName(UUID applicationId, UUID nodeId, String name, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("ModifyNodeName"),
                applicationId, nodeId, name, currentUserId, publicMethods.now());
    }

    public boolean modifyNodeDescription(UUID applicationId, UUID nodeId, String description, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("ModifyNodeDescription"),
                applicationId, nodeId, description, currentUserId, publicMethods.now());
    }

    public boolean modifyNodePublicDescription(UUID applicationId, UUID nodeId, String description)
    {
        return rvConnection.succeed(getQualifiedName("ModifyNodePublicDescription"), applicationId, nodeId, description);
    }

    public boolean setNodeExpirationDate(UUID applicationId, UUID nodeId, DateTime expirationDate)
    {
        return rvConnection.succeed(getQualifiedName("SetNodeExpirationDate"), applicationId, nodeId, expirationDate);
    }

    public boolean setExpiredNodesAsNotSearchable(UUID applicationId)
    {
        return rvConnection.succeed(getQualifiedName("SetExpiredNodesAsNotSearchable"), applicationId, publicMethods.now());
    }

    public List<UUID> getNodeIDsThatWillBeExpiredSoon(UUID applicationId)
    {
        return rvConnection.getUUIDList(getQualifiedName("GetNodeIDsThatWillBeExpiredSoon"),
                applicationId, publicMethods.now().plusDays(raaivanSettings.getKnowledge().AlertExpirationInDays(applicationId)));
    }

    public boolean notifyNodeExpiration(UUID applicationId, UUID nodeId, UUID userId, List<Dashboard> retDashboards)
    {
        return rvConnection.getDashboards(retDashboards, getQualifiedName("NotifyNodeExpiration"),
                applicationId, nodeId, userId, publicMethods.now()) > 0;
    }

    public boolean setPreviousVersion(UUID applicationId, UUID nodeId, UUID previousVersionId, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("SetPreviousVersion"),
                applicationId, nodeId, previousVersionId, currentUserId, publicMethods.now());
    }

    public List<Node> getPreviousVersions(UUID applicationId, UUID nodeId, UUID currentUserId, boolean checkPrivacy)
    {
        return parser.nodes(rvConnection.read(getQualifiedName("GetPreviousVersions"), applicationId,
                nodeId, currentUserId, checkPrivacy, publicMethods.now(), raaivanSettings.DefaultPrivacy(applicationId)));
    }

    public List<Node> getNewVersions(UUID applicationId, UUID nodeId)
    {
        return parser.nodes(rvConnection.read(getQualifiedName("GetNewVersions"), applicationId, nodeId));
    }

    public boolean modifyNodeTags(UUID applicationId, UUID nodeId, List<String> tags, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("ModifyNodeTags"),
                applicationId, nodeId, rvConnection.getTagsText(tags), currentUserId, publicMethods.now());
    }

    public String getNodeDescription(UUID applicationId, UUID nodeId)
    {
        return rvConnection.getString(getQualifiedName("GetNodeDescription"), applicationId, nodeId);
    }

    public boolean setNodesSearchability(UUID applicationId, List<UUID> nodeIds, boolean searchable, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("SetNodesSearchability"), applicationId,
                nodeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',',
                searchable, currentUserId, publicMethods.now());
    }

    public boolean setNodeSearchability(UUID applicationId, UUID nodeId, boolean searchable, UUID currentUserId){
        List<UUID> ids = new ArrayList<UUID>() {{
            add(nodeId);
        }};

        return setNodesSearchability(applicationId, ids, searchable, currentUserId);
    }

    public boolean removeNodes(UUID applicationId, List<UUID> nodeIds, boolean removeHierarchy, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("ArithmeticDeleteNodes"), applicationId,
                nodeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',',
                removeHierarchy, currentUserId, publicMethods.now());
    }

    public boolean removeNode(UUID applicationId, UUID nodeId, boolean removeHierarchy, UUID currentUserId){
        List<UUID> ids = new ArrayList<UUID>() {{
            add(nodeId);
        }};

        return removeNodes(applicationId, ids, removeHierarchy, currentUserId);
    }

    public boolean recycleNodes(UUID applicationId, List<UUID> nodeIds, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("RecycleNodes"), applicationId,
                nodeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',', currentUserId, publicMethods.now());
    }

    public boolean setNodeTypesOrder(UUID applicationId, List<UUID> nodeTypeIds)
    {
        return rvConnection.succeed(getQualifiedName("SetNodeTypesOrder"), applicationId,
                nodeTypeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',');
    }

    public boolean setNodesOrder(UUID applicationId, List<UUID> nodeIds)
    {
        return rvConnection.succeed(getQualifiedName("SetNodesOrder"), applicationId,
                nodeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',');
    }

    private List<NodesCount> _getNodesCount(UUID applicationId, List<UUID> nodeTypeIds, String nodeTypeAdditionalId,
                                          DateTime lowerCreationDateLimit, DateTime upperCreationDateLimit, Boolean root, Boolean archive)
    {
        return parser.nodesCount(rvConnection.read(getQualifiedName("GetNodesCount"), applicationId,
                nodeTypeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',',
                nodeTypeAdditionalId, lowerCreationDateLimit, upperCreationDateLimit, root, archive));
    }

    public List<NodesCount> getNodesCount(UUID applicationId, List<UUID> nodeTypeIds, DateTime lowerCreationDateLimit,
                                          DateTime upperCreationDateLimit, Boolean root, Boolean archive){
        return _getNodesCount(applicationId, nodeTypeIds, null,
                lowerCreationDateLimit, upperCreationDateLimit, root, archive);
    }

    public NodesCount getNodesCount(UUID applicationId, UUID nodeTypeId, DateTime lowerCreationDateLimit,
                                          DateTime upperCreationDateLimit, Boolean root, Boolean archive) {
        List<UUID> lst = new ArrayList<UUID>() {{
            add(nodeTypeId);
        }};

        return  _getNodesCount(applicationId, lst, null,
                lowerCreationDateLimit, upperCreationDateLimit, root, archive).stream().findFirst().orElse(null);
    }

    public List<NodesCount> getNodesCount(UUID applicationId, String nodeTypeAdditionalId, DateTime lowerCreationDateLimit,
                                          DateTime upperCreationDateLimit, Boolean root, Boolean archive){
        return _getNodesCount(applicationId, new ArrayList<>(), nodeTypeAdditionalId,
                lowerCreationDateLimit, upperCreationDateLimit, root, archive);
    }

    public NodesCount getNodesCount(UUID applicationId, UUID nodeTypeId, DateTime lowerCreationDateLimit,
                                    DateTime upperCreationDateLimit, Boolean archive){
        List<UUID> lst = new ArrayList<UUID>() {{
            add(nodeTypeId);
        }};

        return _getNodesCount(applicationId, lst, null,
                lowerCreationDateLimit, upperCreationDateLimit, null, archive).stream().findFirst().orElse(null);
    }

    public NodesCount getNodesCount(UUID applicationId, UUID nodeTypeId, Boolean archive){
        return getNodesCount(applicationId, nodeTypeId, null, null, archive);
    }

    public List<NodesCount> getNodesCount(UUID applicationId, DateTime lowerCreationDateLimit,
                                          DateTime upperCreationDateLimit, Boolean archive) {
        return _getNodesCount(applicationId, new ArrayList<>(), null,
                lowerCreationDateLimit, upperCreationDateLimit, null, archive);
    }

    public List<NodesCount> getNodesCount(UUID applicationId, Boolean archive){
        return _getNodesCount(applicationId, new ArrayList<>(), null,
                null, null, null, archive);
    }

    public List<NodesCount> getNodesCount(UUID applicationId){
        return _getNodesCount(applicationId, new ArrayList<>(), null,
                null, null, null, false);
    }

    public List<NodesCount> getMostPopulatedNodeTypes(UUID applicationId, Integer count, Integer lowerBoundary)
    {
        return parser.nodesCount(rvConnection.read(getQualifiedName("GetMostPopulatedNodeTypes"), applicationId, count, lowerBoundary));
    }

    public int getNodeRecordsCount(UUID applicationId)
    {
        return rvConnection.getInt(getQualifiedName("GetNodeRecordsCount"), applicationId);
    }

    private List<UUID> _getNodeIds(UUID applicationId, List<String> nodeAdditionalIds, UUID nodeTypeId, String nodeTypeAdditionalId)
    {
        if (nodeTypeId == null && StringUtils.isBlank(nodeTypeAdditionalId)) return new ArrayList<>();

        RVStructuredParam idsDT = new RVStructuredParam("StringTableType").addColumnMetaData("Value", String.class);
        nodeAdditionalIds.forEach(idsDT::addRow);

        return rvConnection.getUUIDList(getQualifiedName("GetNodeIDs"), applicationId, idsDT, nodeTypeId, nodeTypeAdditionalId);
    }

    private UUID _getNodeId(UUID applicationId, String nodeAdditionalId, UUID nodeTypeId, String nodeTypeAdditionalId) {
        List<String> ids = new ArrayList<String>() {{
            add(nodeAdditionalId);
        }};

        return _getNodeIds(applicationId, ids, nodeTypeId, nodeTypeAdditionalId).stream().findFirst().orElse(null);
    }

    public List<UUID> getNodeIds(UUID applicationId, List<String> nodeAdditionalIds, UUID nodeTypeId){
        return _getNodeIds(applicationId, nodeAdditionalIds, nodeTypeId, null);
    }

    public List<UUID> getNodeIds(UUID applicationId, List<String> nodeAdditionalIds, String nodeTypeAdditionalId){
        return _getNodeIds(applicationId, nodeAdditionalIds, null, nodeTypeAdditionalId);
    }

    public UUID getNodeId(UUID applicationId, String nodeAdditionalId, UUID nodeTypeId){
        return _getNodeId(applicationId, nodeAdditionalId, nodeTypeId, null);
    }

    public UUID getNodeId(UUID applicationId, String nodeAdditionalId, String nodeTypeAdditionalId){
        return _getNodeId(applicationId, nodeAdditionalId, null, nodeTypeAdditionalId);
    }

    public List<UUID> getNodeIds(UUID applicationId, List<Node> nodes)
    {
        RVStructuredParam idsDT = new RVStructuredParam("StringPairTableType")
                .addColumnMetaData("FirstValue", String.class)
                .addColumnMetaData("SecondValue", String.class);
        nodes.forEach(u -> idsDT.addRow(u.getAdditionalID(), u.getTypeAdditionalID()));

        return rvConnection.getUUIDList(getQualifiedName("GetNodeIDsByAdditionalIDs"), applicationId, idsDT);
    }

    public List<Node> getNodes(UUID applicationId, List<UUID> nodeIds, Boolean full, UUID currentUserId)
    {
        return parser.nodes(rvConnection.read(getQualifiedName("GetNodesByIDs"), applicationId,
                nodeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',', full, currentUserId));
    }

    public List<Node> getNodes(UUID applicationId, List<UUID> nodeIds) {
        return getNodes(applicationId, nodeIds, null, null);
    }

    public Node getNode(UUID applicationId, UUID nodeId, Boolean full, UUID currentUserId) {
        List<UUID> ids = new ArrayList<UUID>() {{
            add(nodeId);
        }};

        return getNodes(applicationId, ids, full, currentUserId).stream().findFirst().orElse(null);
    }

    public Node getNode(UUID applicationId, UUID nodeId){
        return getNode(applicationId, nodeId, null, null);
    }

    private List<Node> _getNodes(UUID applicationId, List<UUID> nodeTypeIds, String nodeTypeAdditionalId,
                               Boolean useNodeTypeHierarchy, String searchText, Boolean isDocument, Boolean isKnowledge,
                               DateTime lowerCreationDateLimit, DateTime upperCreationDateLimit, int count, Long lowerBoundary,
                               Boolean searchable, Boolean archive, Boolean grabNoContentServices, List<FormFilter> filters,
                               Boolean matchAllFilters, UUID currentUserId, Boolean isMine, MutableLong totalCount)
    {
        if(filters == null) filters = new ArrayList<>();

        RVStructuredParam filtersDT = new RVStructuredParam("FormFilterTableType")
                .addColumnMetaData("ElementID", UUID.class)
                .addColumnMetaData("OwnerID", UUID.class)
                .addColumnMetaData("Text", String.class)
                .addColumnMetaData("TextItems", String.class)
                .addColumnMetaData("Or", Boolean.class)
                .addColumnMetaData("Exact", Boolean.class)
                .addColumnMetaData("DateFrom", DateTime.class)
                .addColumnMetaData("DateTo", DateTime.class)
                .addColumnMetaData("FloatFrom", Double.class)
                .addColumnMetaData("FloatTo", Double.class)
                .addColumnMetaData("Bit", Boolean.class)
                .addColumnMetaData("Guid", UUID.class)
                .addColumnMetaData("GuidItems", String.class);
        filters.forEach(u -> filtersDT.addRow(u.getElementID(), u.getOwnerID(), u.getText(),
                String.join(",", u.getTextItems()), u.getOr(), u.getExact(), u.getDateFrom(), u.getDateTo(),
                u.getFloatFrom(), u.getFloatTo(), u.getBit(), u.getGuid(),
                u.getGuidItems().stream().map(UUID::toString).collect(Collectors.joining(","))));

        return parser.nodes(rvConnection.read(getQualifiedName("GetNodes"), applicationId, currentUserId,
                nodeTypeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',',
                nodeTypeAdditionalId, useNodeTypeHierarchy, rvConnection.getSearchText(searchText),
                isDocument, isKnowledge, isMine, searchable, archive, grabNoContentServices, lowerCreationDateLimit,
                upperCreationDateLimit, count, lowerBoundary, filtersDT, matchAllFilters), false, totalCount);
    }

    public List<Node> getNodes(UUID applicationId, List<UUID> nodeTypeIds,
                               Boolean useNodeTypeHierarchy, String searchText, Boolean isDocument, Boolean isKnowledge,
                               DateTime lowerCreationDateLimit, DateTime upperCreationDateLimit, int count, Long lowerBoundary,
                               Boolean searchable, Boolean archive, Boolean grabNoContentServices, List<FormFilter> filters,
                               Boolean matchAllFilters, UUID currentUserId, Boolean isMine, MutableLong totalCount){
        return _getNodes(applicationId, nodeTypeIds, null, useNodeTypeHierarchy, searchText, isDocument,
                isKnowledge, lowerCreationDateLimit, upperCreationDateLimit, count, lowerBoundary, searchable,
                archive, grabNoContentServices, filters, matchAllFilters, currentUserId, isMine, totalCount);
    }

    public List<Node> getNodes(UUID applicationId, String nodeTypeAdditionalId,
                               Boolean useNodeTypeHierarchy, String searchText, Boolean isDocument, Boolean isKnowledge,
                               DateTime lowerCreationDateLimit, DateTime upperCreationDateLimit, int count, Long lowerBoundary,
                               Boolean searchable, Boolean archive, Boolean grabNoContentServices, List<FormFilter> filters,
                               Boolean matchAllFilters, UUID currentUserId, Boolean isMine, MutableLong totalCount){
        return _getNodes(applicationId, new ArrayList<>(), nodeTypeAdditionalId, useNodeTypeHierarchy, searchText, isDocument,
                isKnowledge, lowerCreationDateLimit, upperCreationDateLimit, count, lowerBoundary, searchable,
                archive, grabNoContentServices, filters, matchAllFilters, currentUserId, isMine, totalCount);
    }

    public List<Node> getMostPopularNodes(UUID applicationId, List<UUID> nodeTypeIds,
                                           UUID parentNodeId, int count, Long lowerBoundary, MutableLong totalCount)
    {
        return parser.popularNodes(rvConnection.read(getQualifiedName("GetMostPopularNodes"), applicationId,
                nodeTypeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',',
                parentNodeId, count, lowerBoundary), totalCount);
    }

    public List<Node> getMostPopularNodes(UUID applicationId, UUID nodeTypeId,
                                          UUID parentNodeId, int count, Long lowerBoundary, MutableLong totalCount) {
        List<UUID> ids = new ArrayList<UUID>() {{
            if (nodeTypeId != null) add(nodeTypeId);
        }};

        return getMostPopularNodes(applicationId, ids, parentNodeId, count, lowerBoundary, totalCount);
    }

    public List<Node> getParentNodes(UUID applicationId, UUID nodeId)
    {
        return parser.nodes(rvConnection.read(getQualifiedName("GetParentNodes"), applicationId, nodeId));
    }

    public List<Node> getChildNodes(UUID applicationId, UUID nodeId)
    {
        return parser.nodes(rvConnection.read(getQualifiedName("GetChildNodes"), applicationId, nodeId));
    }

    public List<Node> getDefaultRelatedNodes(UUID applicationId, UUID nodeId)
    {
        return parser.nodes(rvConnection.read(getQualifiedName("GetDefaultRelatedNodes"), applicationId, nodeId));
    }

    public List<Node> getDefaultConnectedNodes(UUID applicationId, UUID nodeId)
    {
        return parser.nodes(rvConnection.read(getQualifiedName("GetDefaultConnectedNodes"), applicationId, nodeId));
    }

    public List<Node> getBrotherNodes(UUID applicationId, UUID nodeId)
    {
        return parser.nodes(rvConnection.read(getQualifiedName("GetBrotherNodes"), applicationId, nodeId));
    }

    public List<Node> getDirectChilds(UUID applicationId, UUID nodeId, UUID nodeTypeId, String nodeTypeAdditionalId,
                                      Boolean searchable, Double lowerBoundary, Integer count, String orderBy, Boolean orderByDesc,
                                      String searchText, Boolean checkAccess, UUID currentUserId, MutableLong totalCount)
    {
        return parser.nodes(rvConnection.read(getQualifiedName("GetDirectChilds"), applicationId, nodeId, nodeTypeId,
                nodeTypeAdditionalId, searchable, lowerBoundary, count, orderBy, orderByDesc,
                rvConnection.getSearchText(searchText), checkAccess, currentUserId, publicMethods.now(),
                raaivanSettings.DefaultPrivacy(applicationId)), false, totalCount);
    }

    public List<Node> getDirectChilds(UUID applicationId, UUID nodeId) {
        MutableLong totalCount = new MutableLong(0);
        return getDirectChilds(applicationId, nodeId, null, null, true,
                null, null, null, null, null, null,
                null, totalCount);
    }

    public Node getDirectParent(UUID applicationId, UUID nodeId)
    {
        return parser.nodes(rvConnection.read(getQualifiedName("GetDirectParent"), applicationId, nodeId))
                .stream().findFirst().orElse(null);
    }

    public boolean setDirectParent(UUID applicationId, List<UUID> nodeIds, UUID parentNodeId,
                                   UUID currentUserId, StringBuilder errorMessage)
    {
        return rvConnection.succeed(errorMessage, getQualifiedName("SetDirectParent"), applicationId,
                nodeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',',
                parentNodeId, currentUserId, publicMethods.now());
    }

    public boolean setDirectParent(UUID applicationId, UUID nodeId, UUID parentNodeId,
                                   UUID currentUserId, StringBuilder errorMessage){
        List<UUID> ids = new ArrayList<UUID>() {{
            add(nodeId);
        }};

        return setDirectParent(applicationId, ids, parentNodeId, currentUserId, errorMessage);
    }

    public List<UUID> haveChilds(UUID applicationId, List<UUID> nodeIds)
    {
        return rvConnection.getUUIDList(getQualifiedName("HaveChilds"), applicationId,
                nodeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',');
    }

    public boolean hasChilds(UUID applicationId, UUID nodeId) {
        List<UUID> ids = new ArrayList<UUID>() {{
            add(nodeId);
        }};

        return haveChilds(applicationId, ids).size() > 0;
    }

    public List<UUID> getRelatedNodeIDs(UUID applicationId, UUID nodeId,
                                         UUID nodeTypeId, String searchText, Boolean inRelations, Boolean outRelations,
                                         Boolean inTagRelations, Boolean outTagRelations, Integer count, Integer lowerBoundary)
    {
        return rvConnection.getUUIDList(getQualifiedName("GetRelatedNodeIDs"), applicationId,
                nodeId, nodeTypeId, rvConnection.getSearchText(searchText), inRelations, outRelations,
                inTagRelations, outTagRelations, count, lowerBoundary);
    }

    public List<Node> getRelatedNodes(UUID applicationId, UUID nodeId, UUID nodeTypeId, String searchText,
                                      Boolean inRelations, Boolean outRelations, Boolean inTagRelations, Boolean outTagRelations,
                                      Integer count, Integer lowerBoundary)
    {
        return parser.nodes(rvConnection.read(getQualifiedName("GetRelatedNodes"), applicationId, nodeId, nodeTypeId,
                rvConnection.getSearchText(searchText), inRelations, outRelations, inTagRelations, outTagRelations, count, lowerBoundary));
    }

    public List<NodesCount> getRelatedNodesCount(UUID applicationId, UUID nodeId, UUID nodeTypeId, String searchText,
                                                 Boolean inRelations, Boolean outRelations, Boolean inTagRelations,
                                                 Boolean outTagRelations, Integer count, Integer lowerBoundary)
    {
        return parser.nodesCount(rvConnection.read(getQualifiedName("GetRelatedNodesCount"), applicationId, nodeId,
                nodeTypeId, rvConnection.getSearchText(searchText), inRelations, outRelations, inTagRelations,
                outTagRelations, count, lowerBoundary));
    }

    public boolean relationExists(UUID applicationId, UUID sourceNodeId, UUID destinationNodeId, Boolean reverseAlso)
    {
        return rvConnection.succeed(getQualifiedName("RelationExists"), applicationId, sourceNodeId, destinationNodeId, reverseAlso);
    }

    public boolean relationExists(UUID applicationId, UUID sourceNodeId, UUID destinationNodeId){
        return relationExists(applicationId, sourceNodeId, destinationNodeId, false);
    }

    public boolean addMember(UUID applicationId, UUID nodeId, UUID userId,
                             Boolean isAdmin, NodeMemberStatus status, List<Dashboard> retDashboards)
    {
        boolean isPending = status == NodeMemberStatus.Pending;
        DateTime membershipDate = publicMethods.now();
        DateTime acceptionDate = isPending ? null : membershipDate;

        return rvConnection.succeed(retDashboards, getQualifiedName("AddMember"),
                applicationId, nodeId, userId, membershipDate, isAdmin, isPending, acceptionDate, null, true);
    }

    public boolean removeMember(UUID applicationId, UUID nodeId, UUID userId)
    {
        return rvConnection.succeed(getQualifiedName("ArithmeticDeleteMember"), applicationId, nodeId, userId);
    }

    public boolean acceptMember(UUID applicationId, UUID nodeId, UUID userId)
    {
        return rvConnection.succeed(getQualifiedName("AcceptMember"), applicationId, nodeId, userId, publicMethods.now());
    }

    private boolean _isNodeCreator(UUID applicationId, UUID nodeId, UUID nodeTypeId, String additionalId, UUID userId)
    {
        if(StringUtils.isBlank(additionalId)) additionalId = null;

        return rvConnection.succeed(getQualifiedName("IsNodeCreator"), applicationId, nodeId, nodeTypeId, additionalId, userId);
    }

    public boolean isNodeCreator(UUID applicationId, UUID nodeId, UUID userId){
        return _isNodeCreator(applicationId, nodeId, null, null, userId);
    }

    public boolean isNodeCreator(UUID applicationId, UUID nodeTypeId, String nodeAdditionalId, UUID userId){
        return _isNodeCreator(applicationId, null, nodeTypeId, nodeAdditionalId, userId);
    }

    public boolean isNodeMember(UUID applicationId, UUID nodeId, UUID userId, Boolean isAdmin, NodeMemberStatus status)
    {
        return rvConnection.succeed(getQualifiedName("IsNodeMember"), applicationId, nodeId, userId, isAdmin,
                (status == NodeMemberStatus.NotSet ? null : status.toString()));
    }

    public boolean hasAdmin(UUID applicationId, UUID nodeId)
    {
        return rvConnection.succeed(getQualifiedName("HasAdmin"), applicationId, nodeId);
    }

    private boolean _setUnsetNodeAdmin(UUID applicationId, UUID nodeId, UUID userId, boolean admin, boolean unique)
    {
        return rvConnection.succeed(getQualifiedName("SetUnsetNodeAdmin"), applicationId, nodeId, userId, admin, unique);
    }

    public boolean setNodeAdmin(UUID applicationId, UUID nodeId, UUID userId, boolean unique)
    {
        return _setUnsetNodeAdmin(applicationId, nodeId, userId, true, unique);
    }

    public boolean unsetNodeAdmin(UUID applicationId, UUID nodeId, UUID userId, boolean unique)
    {
        return _setUnsetNodeAdmin(applicationId, nodeId, userId, false, unique);
    }

    public boolean isAdminMember(UUID applicationId, UUID nodeId, UUID userId)
    {
        return rvConnection.succeed(getQualifiedName("IsNodeAdmin"), applicationId, nodeId, userId);
    }

    public List<HierarchyAdmin> getAreaAdmins(UUID applicationId, UUID nodeId, Node node, CNService service)
    {
        if (service == null) service = getService(applicationId,
                node != null && node.getNodeTypeID() != null ? node.getNodeTypeID() : nodeId);
        if (service == null) return new ArrayList<>();

        switch (service.getAdminType())
        {
            case AreaAdmin:
            case ComplexAdmin:
                if (node == null) node = getNode(applicationId, nodeId);

                if (node == null || node.getAdminAreaID() == null)
                    return new ArrayList<>();
                else if (service.getAdminType() == ServiceAdminType.ComplexAdmin) {
                    return getComplexAdmins(applicationId, node.getAdminAreaID()).stream().map(u -> {
                        HierarchyAdmin h = RVBeanFactory.getBean(HierarchyAdmin.class);
                        h.getUser().setUserID(u);
                        h.setLevel(0);

                        return h;
                    }).collect(Collectors.toList());
                }
                else {
                    final CNService s = service;

                    return getHierarchyAdmins(applicationId, node.getAdminAreaID(), true).stream()
                            .filter(u -> s.getMaxAcceptableAdminLevel() == null || s.getMaxAcceptableAdminLevel() < 0 ||
                                    u.getLevel() <= s.getMaxAcceptableAdminLevel())
                            .map(u -> {
                                HierarchyAdmin h = RVBeanFactory.getBean(HierarchyAdmin.class);
                                h.getUser().setUserID(u.getUser().getUserID());
                                h.setLevel(u.getLevel());

                                return h;
                            }).collect(Collectors.toList());
                }
            case SpecificNode: {
                return service.getAdminNode().getNodeID() == null ? new ArrayList<>() :
                        getMembers(applicationId, service.getAdminNode().getNodeID(), false, true)
                                .stream().map(u -> {
                            HierarchyAdmin h = RVBeanFactory.getBean(HierarchyAdmin.class);
                            h.getUser().setUserID(u.getMember().getUserID());
                            h.setLevel(0);

                            return h;
                        }).collect(Collectors.toList());
            }
            case Registerer:
                if (node == null) node = getNode(applicationId, nodeId);
                List<UUID> retList = new ArrayList<>();
                if (node != null && node.getCreator().getUserID() != null) retList.add(node.getCreator().getUserID());

                return retList.stream().map(u -> {
                    HierarchyAdmin h = RVBeanFactory.getBean(HierarchyAdmin.class);
                    h.getUser().setUserID(u);
                    h.setLevel(0);

                    return h;
                }).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    public List<HierarchyAdmin> getNodeAdmins(UUID applicationId, UUID nodeId, Node nodeObject, CNService service)
    {
        if (nodeObject == null && (nodeObject = getNode(applicationId, nodeId)) == null)
            nodeObject = RVBeanFactory.getBean(Node.class);
        if (service == null && (service = getService(applicationId,
                nodeObject != null && nodeObject.getNodeTypeID() != null ? nodeObject.getNodeTypeID() : nodeId)) == null)
            service = RVBeanFactory.getBean(CNService.class);

        List<HierarchyAdmin> admins = new ArrayList<>();

        switch (service.getAdminType())
        {
            case AreaAdmin:
                if (nodeObject.getAdminAreaID() != null)
                    admins = getAreaAdmins(applicationId, nodeId, nodeObject, null);
                break;
            case ComplexAdmin:
                if (nodeObject.getAdminAreaID() != null) {
                    admins = getComplexAdmins(applicationId, nodeObject.getAdminAreaID()).stream().map(u -> {
                        HierarchyAdmin h = RVBeanFactory.getBean(HierarchyAdmin.class);
                        h.getUser().setUserID(u);
                        h.setLevel(0);

                        return h;
                    }).collect(Collectors.toList());
                }
                break;
            case SpecificNode:
                if (service.getAdminNode().getNodeID() != null) {
                    admins = getMembers(applicationId, service.getAdminNode().getNodeID(), false, true).stream()
                            .map(u -> {
                                HierarchyAdmin h = RVBeanFactory.getBean(HierarchyAdmin.class);
                                h.getUser().setUserID(u.getMember().getUserID());
                                h.setLevel(0);

                                return h;
                            }).collect(Collectors.toList());
                }
                break;
            case Registerer:
                if (nodeObject.getCreator().getUserID() != null){
                    HierarchyAdmin h = RVBeanFactory.getBean(HierarchyAdmin.class);
                    h.getUser().setUserID(nodeObject.getCreator().getUserID());
                    h.setLevel(0);

                    admins.add(h);
                }
                break;
        }

        return admins;
    }

    public boolean isNodeAdmin(UUID applicationId, UUID userId, UUID nodeId, UUID nodeTypeId, UUID areaId,
                               Boolean isCreator, CNService service, List<NodeCreator> contributors)
    {
        if (service == null || (service = getService(applicationId, nodeTypeId != null ? nodeTypeId : nodeId)) == null)
            service = RVBeanFactory.getBean(CNService.class);

        if (areaId == null &&
                (service.getAdminType() == ServiceAdminType.AreaAdmin || service.getAdminType() == ServiceAdminType.ComplexAdmin))
        {
            Node nd = getNode(applicationId, nodeId);
            if (nd != null) areaId = nd.getAdminAreaID();
        }

        switch (service.getAdminType())
        {
            case AreaAdmin:
                if (areaId == null) return false;
                else
                {
                    if (contributors == null) contributors = getNodeCreators(applicationId, nodeId);

                    return service.getMaxAcceptableAdminLevel() != null ?
                            isHierarchyAdmin(applicationId, areaId, userId,
                                    contributors.stream().map(u -> u.getUser().getUserID()).collect(Collectors.toList()),
                                    service.getMaxAcceptableAdminLevel()) :
                            isHierarchyAdmin(applicationId, areaId, userId,
                                    contributors.stream().map(u -> u.getUser().getUserID()).collect(Collectors.toList()));
                }
            case ComplexAdmin:
                return areaId != null && isComplexAdmin(applicationId, areaId, userId);
            case SpecificNode:
                return service.getAdminNode().getNodeID() != null &&
                        isAdminMember(applicationId, service.getAdminNode().getNodeID(), userId);
            case Registerer:
                return isCreator != null ? isCreator : isNodeCreator(applicationId, nodeId, userId);
        }

        return false;
    }

    private boolean _getUser2NodeStatus(UUID applicationId, UUID userId, UUID nodeId,
                                      MutableUUID nodeTypeId, MutableUUID areaId, MutableBoolean isCreator,
                                      MutableBoolean isContributor, MutableBoolean isExpert, MutableBoolean isMember,
                                      MutableBoolean isAdminMember, MutableBoolean isServiceAdmin)
    {
        return parser.user2nodeStatus(rvConnection.read(getQualifiedName("GetUser2NodeStatus"), applicationId, userId, nodeId),
                nodeTypeId, areaId, isCreator, isContributor, isExpert, isMember, isAdminMember, isServiceAdmin);
    }

    public boolean getUser2NodeStatus(UUID applicationId, UUID userId, UUID nodeId,
                                      MutableBoolean isCreator, MutableBoolean isContributor, MutableBoolean isExpert,
                                      MutableBoolean isMember, MutableBoolean isAdminMember, MutableBoolean isServiceAdmin,
                                      MutableBoolean isAreaAdmin, MutableBoolean editable, MutableBoolean noOptionSet,
                                      MutableBoolean editSuggestion, CNService service, List<NodeCreator> contributors)
    {
        //noOptionSet: All edit permissions are not set

        MutableUUID nodeTypeId = new MutableUUID(null);
        MutableUUID areaId = new MutableUUID(null);

        if (!_getUser2NodeStatus(applicationId, userId, nodeId, nodeTypeId, areaId,
                isCreator, isContributor, isExpert, isMember, isAdminMember, isServiceAdmin))
        {
            editSuggestion.setValue(false);
            return false;
        }

        if (service == null || (service = getService(applicationId, nodeTypeId.getValue())) == null)
            service = RVBeanFactory.getBean(CNService.class);

        isAreaAdmin.setValue(isNodeAdmin(applicationId, userId, nodeId, nodeTypeId.getValue(),
                areaId.getValue(), isCreator.getValue(), service, contributors));

        noOptionSet.setValue( service.getEditableForAdmin() == null && service.getEditableForCreator() == null &&
                service.getEditableForContributors() == null && service.getEditableForExperts() == null &&
                service.getEditableForMembers() == null);

        if (service.getEditableForAdmin() == null) service.setEditableForAdmin(false);
        if (service.getEditableForCreator() == null) service.setEditableForCreator(false);
        if (service.getEditableForContributors() == null) service.setEditableForContributors(false);
        if (service.getEditableForExperts() == null) service.setEditableForExperts(false);
        if (service.getEditableForMembers() == null) service.setEditableForMembers(false);

        editable.setValue(publicMethods.isSystemAdmin(applicationId, userId) || isServiceAdmin.getValue() ||
                (isAreaAdmin.getValue() && service.getEditableForAdmin()) ||
                (isCreator.getValue() && service.getEditableForCreator()) ||
                (isContributor.getValue() && service.getEditableForContributors()) ||
                (isExpert.getValue() && service.getEditableForExperts()) ||
                (isMember.getValue() && service.getEditableForMembers()));

        editSuggestion.setValue(service.getEditSuggestion() == null || service.getEditSuggestion());

        return true;
    }

    public boolean getUser2NodeStatus(UUID applicationId, UUID userId, UUID nodeId,
                                      MutableBoolean isCreator, MutableBoolean isContributor, MutableBoolean isExpert,
                                      MutableBoolean isMember, MutableBoolean isAdminMember, MutableBoolean isServiceAdmin,
                                      MutableBoolean isAreaAdmin, MutableBoolean editable,
                                      CNService service, List<NodeCreator> contributors){
        MutableBoolean noOptionSet = new MutableBoolean(false);
        MutableBoolean editSuggestion = new MutableBoolean(false);

        return getUser2NodeStatus(applicationId, userId, nodeId, isCreator, isContributor, isExpert, isMember,
                isAdminMember, isServiceAdmin, isAreaAdmin, editable, noOptionSet, editSuggestion, service, contributors);
    }

    public boolean hasEditPermission(UUID applicationId, UUID userId, UUID nodeId,
                                     boolean defaultPermissionForExperts, MutableBoolean editSuggestion)
    {
        MutableBoolean isCreator = new MutableBoolean(false), isContributor = new MutableBoolean(false),
                isExpert = new MutableBoolean(false), isMember = new MutableBoolean(false),
                isAdminMember = new MutableBoolean(false), isServiceAdmin = new MutableBoolean(false),
                isAreaAdmin = new MutableBoolean(false), editable = new MutableBoolean(false),
                noOptionSet = new MutableBoolean(false);

        getUser2NodeStatus(applicationId, userId, nodeId, isCreator, isContributor, isExpert, isMember,
                isAdminMember, isServiceAdmin, isAreaAdmin, editable, noOptionSet, editSuggestion, null, null);

        return editable.getValue() || (noOptionSet.getValue() && defaultPermissionForExperts && isExpert.getValue()) ||
                privacyDAO.checkAccess(applicationId, userId, nodeId, PrivacyObjectType.Node, PermissionType.Modify);
    }

    public boolean hasEditPermission(UUID applicationId, UUID userId, UUID nodeId, boolean defaultPermissionForExperts)
    {
        MutableBoolean editSuggestion = new MutableBoolean(false);
        return hasEditPermission(applicationId, userId, nodeId, defaultPermissionForExperts, editSuggestion);
    }

    public boolean hasEditPermission(UUID applicationId, UUID userId, UUID nodeId)
    {
        return hasEditPermission(applicationId, userId, nodeId, false);
    }

    public List<UUID> getUsersWithEditPermission(UUID applicationId, UUID nodeId,
                                                 boolean defaultPermissionForExperts, MutableBoolean editSuggestion) {
        List<UUID> retList = new ArrayList<>();

        CNService service = getService(applicationId, nodeId);

        editSuggestion.setValue(service == null || service.getEditSuggestion() == null || service.getEditSuggestion());

        if (defaultPermissionForExperts && (
                service == null || (
                        service.getEditableForAdmin() == null && service.getEditableForCreator() == null &&
                                service.getEditableForContributors() == null && service.getEditableForExperts() == null &&
                                service.getEditableForMembers() == null
                ))) {
            return getExperts(applicationId, nodeId).stream().map(u -> u.getUser().getUserID()).collect(Collectors.toList());
        }
        else if (service == null) return new ArrayList<>();

        Node node = null;

        if (service.getEditableForAdmin() != null && service.getEditableForAdmin())
            getAreaAdmins(applicationId, nodeId, node, service);

        if (service.getEditableForContributors() != null && service.getEditableForContributors())
            retList.addAll(getNodeCreators(applicationId, nodeId).stream().map(u -> u.getUser().getUserID()).collect(Collectors.toList()));

        if (service.getEditableForCreator() != null && service.getEditableForCreator())
            retList.add(getNode(applicationId, nodeId).getCreator().getUserID());

        if (service.getEditableForExperts() != null && service.getEditableForExperts())
            retList.addAll(getExperts(applicationId, nodeId).stream().map(u -> u.getUser().getUserID()).collect(Collectors.toList()));

        if (service.getEditableForMembers() != null && service.getEditableForMembers())
            retList.addAll(getMembers(applicationId, nodeId, false, null)
                    .stream().map(u -> u.getMember().getUserID()).collect(Collectors.toList()));

        return retList.stream().distinct().collect(Collectors.toList());
    }

    public List<UUID> getUsersWithEditPermission(UUID applicationId, UUID nodeId, boolean defaultPermissionForExperts)
    {
        MutableBoolean editSuggestion = new MutableBoolean(false);
        return getUsersWithEditPermission(applicationId, nodeId, defaultPermissionForExperts, editSuggestion);
    }

    public List<UUID> getUsersWithEditPermission(UUID applicationId, UUID nodeId)
    {
        return getUsersWithEditPermission(applicationId, nodeId, false);
    }

    public List<UUID> getComplexAdmins(UUID applicationId, UUID listIdOrNodeId)
    {
        return rvConnection.getUUIDList(getQualifiedName("GetComplexAdmins"), applicationId, listIdOrNodeId);
    }

    public boolean isComplexAdmin(UUID applicationId, UUID nodeId, UUID userId)
    {
        return rvConnection.succeed(getQualifiedName("IsComplexAdmin"), applicationId, nodeId, userId);
    }

    public UUID getComplexTypeID(UUID applicationId, UUID listId)
    {
        return rvConnection.getUUID(getQualifiedName("GetComplexTypeID"), applicationId, listId);
    }

    public List<Hierarchy> getNodeHierarchy(UUID applicationId, UUID nodeId, Boolean sameType)
    {
        return rvConnection.getHierarchy(getQualifiedName("GetNodeHierarchy"), applicationId, nodeId, sameType);
    }

    public List<Hierarchy> getNodeHierarchy(UUID applicationId, UUID nodeId)
    {
        return getNodeHierarchy(applicationId, nodeId, true);
    }

    public List<Hierarchy> getNodeTypesHierarchy(UUID applicationId, List<UUID> nodeTypeIds)
    {
        return rvConnection.getHierarchy(getQualifiedName("GetNodeTypesHierarchy"), applicationId,
                nodeTypeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',');
    }

    public List<Hierarchy> getNodeTypeHierarchy(UUID applicationId, UUID nodeTypeId) {
        List<UUID> ids = new ArrayList<UUID>() {{
            add(nodeTypeId);
        }};

        return getNodeTypesHierarchy(applicationId, ids);
    }

    public int getTreeDepth(UUID applicationId, UUID nodeTypeId)
    {
        return rvConnection.getInt(getQualifiedName("GetTreeDepth"), applicationId, nodeTypeId);
    }

    public List<HierarchyAdmin> getHierarchyAdmins(UUID applicationId, UUID nodeId, Boolean sameType)
    {
        return parser.hierarchyAdmins(rvConnection.read(getQualifiedName("GetNodeHierarchyAdminIDs"), applicationId, nodeId, sameType));
    }

    public List<HierarchyAdmin> getHierarchyAdmins(UUID applicationId, UUID nodeId){
        return getHierarchyAdmins(applicationId, nodeId, true);
    }

    public boolean isHierarchyAdmin(UUID applicationId, UUID nodeId, UUID userId, List<UUID> contributorUserIds, int maxLevel)
    {
        List<HierarchyAdmin> admins = getHierarchyAdmins(applicationId, nodeId).stream().filter(
                u -> maxLevel < 0 || u.getLevel() <= maxLevel)
                .sorted(Comparator.comparing(HierarchyAdmin::getLevel))
                .collect(Collectors.toList());

        if (admins.size() == 0) return false;

        if (admins.stream().anyMatch(u -> u.getUser().getUserID() == userId) &&
                contributorUserIds.stream().anyMatch(u -> u == userId) &&
                admins.stream().anyMatch(u -> u.getUser().getUserID() != userId &&
                        contributorUserIds.stream().noneMatch(x -> x == u.getUser().getUserID()))){
            return false;
        }

        if(admins.stream().anyMatch(u -> u.getUser().getUserID() == userId &&
                contributorUserIds.stream().noneMatch(x -> x == userId))) return true;

        return admins.stream().anyMatch(u -> u.getUser().getUserID() == userId);
    }

    public boolean isHierarchyAdmin(UUID applicationId, UUID nodeId, UUID userId, List<UUID> contributorUserIds){
        return isHierarchyAdmin(applicationId, nodeId, userId, contributorUserIds, 2);
    }

    public boolean isHierarchyAdmin(UUID applicationId, UUID nodeId, UUID userId, int maxLevel)
    {
        return isHierarchyAdmin(applicationId, nodeId, userId, maxLevel);
    }

    public boolean isHierarchyAdmin(UUID applicationId, UUID nodeId, UUID userId){
        return isHierarchyAdmin(applicationId, nodeId, userId, 2);
    }

    public List<NodeMember> getMembers(UUID applicationId, List<UUID> nodeIds, Boolean pending, Boolean admin,
                                       Integer count, Long lowerBoundary, MutableLong totalCount)
    {
        if(nodeIds == null || nodeIds.size() == 0) return new ArrayList<>();

        return parser.nodeMembers(rvConnection.read(getQualifiedName("GetMembers"), applicationId,
                nodeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',',
                pending, admin, count, lowerBoundary), false, true, totalCount);
    }

    public List<NodeMember> getMembers(UUID applicationId, UUID nodeId, Boolean pending, Boolean admin,
                                       Integer count, Long lowerBoundary, MutableLong totalCount) {
        List<UUID> ids = new ArrayList<UUID>() {{
            add(nodeId);
        }};

        return getMembers(applicationId, ids, pending, admin, count, lowerBoundary, totalCount);
    }

    public List<NodeMember> getMembers(UUID applicationId, List<UUID> nodeIds, Boolean pending, Boolean admin){
        MutableLong totalCount = new MutableLong(0);
        return getMembers(applicationId, nodeIds, pending, admin, null, null, totalCount);
    }

    public List<NodeMember> getMembers(UUID applicationId, UUID nodeId, Boolean pending, Boolean admin){
        MutableLong totalCount = new MutableLong(0);
        return getMembers(applicationId, nodeId, pending, admin, null, null, totalCount);
    }

    public NodeMember getMember(UUID applicationId, UUID nodeId, UUID userId)
    {
        return parser.nodeMembers(rvConnection.read(getQualifiedName("GetMember"), applicationId, nodeId, userId),
                false, true).stream().findFirst().orElse(null);
    }

    public List<UUID> getMemberUserIDs(UUID applicationId, List<UUID> nodeIds, NodeMemberStatus status, Boolean admin)
    {
        return rvConnection.getUUIDList(getQualifiedName("GetMemberUserIDs"), applicationId,
                nodeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',',
                (status == NodeMemberStatus.NotSet ? null : status.toString()), admin);
    }

    public List<UUID> getMemberUserIDs(UUID applicationId, UUID nodeId, NodeMemberStatus status, Boolean admin) {
        List<UUID> ids = new ArrayList<UUID>() {{
            add(nodeId);
        }};

        return getMemberUserIDs(applicationId, ids, status, admin);
    }

    public int getMembersCount(UUID applicationId, UUID nodeId, NodeMemberStatus status, Boolean admin)
    {
        return rvConnection.getInt(getQualifiedName("GetMembersCount"), applicationId, nodeId,
                (status == NodeMemberStatus.NotSet ? null : status.toString()), admin);
    }

    public List<NodeMember> getMembershipRequestsDashboards(UUID applicationId, UUID userId)
    {
        return parser.nodeMembers(rvConnection.read(getQualifiedName("GetMembershipRequestsDashboards"), applicationId, userId),
                true, true);
    }

    public int getMembershipRequestsDashboardsCount(UUID applicationId, UUID userId)
    {
        return rvConnection.getInt(getQualifiedName("GetMembershipRequestsDashboards"), applicationId, userId);
    }

    public List<NodesCount> getMembershipDomainsCount(UUID applicationId, UUID userId, UUID nodeTypeId, UUID nodeId,
                                                      String additionalId, DateTime lowerDateLimit, DateTime upperDateLimit)
    {
        return parser.nodesCount(rvConnection.read(getQualifiedName("GetMembershipDomainsCount"),
                applicationId, userId, nodeTypeId, nodeId, additionalId, lowerDateLimit, upperDateLimit));
    }

    public NodesCount getMembershipDomainsCount(UUID applicationId, UUID userId, UUID nodeTypeId,
                                                      DateTime lowerDateLimit, DateTime upperDateLimit)
    {
        return getMembershipDomainsCount(applicationId, userId, nodeTypeId,
                null, null, lowerDateLimit, upperDateLimit).stream().findFirst().orElse(null);
    }

    public List<Node> getMembershipDomains(UUID applicationId, UUID userId, UUID nodeTypeId, UUID nodeId,
                                           String additionalId, String searchText, DateTime lowerDateLimit,
                                           DateTime upperDateLimit, Integer lowerBoundary, Integer count)
    {
        return parser.nodes(rvConnection.read(getQualifiedName("GetMembershipDomains"), applicationId, userId, nodeTypeId,
                nodeId, additionalId, rvConnection.getSearchText(searchText), lowerDateLimit, upperDateLimit, lowerBoundary, count));
    }

    public List<NodeMember> getMemberNodes(UUID applicationId, List<UUID> userIds, List<UUID> nodeTypeIds, Boolean admin)
    {
        return parser.nodeMembers(rvConnection.read(getQualifiedName("GetMemberNodes"), applicationId,
                userIds.stream().map(UUID::toString).collect(Collectors.joining(",")),
                nodeTypeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',',
                null, admin), true, false);
    }

    public List<NodeMember> getMemberNodes(UUID applicationId, UUID userId, List<UUID> nodeTypeIds, Boolean admin) {
        List<UUID> userIds = new ArrayList<UUID>() {{
            if(userId != null) add(userId);
        }};

        return getMemberNodes(applicationId, userIds, nodeTypeIds, admin);
    }

    public List<NodeMember> getMemberNodes(UUID applicationId, UUID userId, UUID nodeTypeId, Boolean admin)
    {
        List<UUID> nodeTypeIds = new ArrayList<UUID>() {{
            if(nodeTypeId != null) add(nodeTypeId);
        }};

        return getMemberNodes(applicationId, userId, nodeTypeIds, admin);
    }

    public List<NodeMember> getMemberNodes(UUID applicationId, UUID userId, Boolean admin) {
        return getMemberNodes(applicationId, userId, (UUID) null, admin);
    }

    public List<UUID> getChildHierarchyMemberIDs(UUID applicationId, UUID nodeId,
                                                 Integer count, Long lowerBoundary, MutableLong totalCount)
    {
        return rvConnection.getUUIDList(totalCount, getQualifiedName("GetChildHierarchyMemberIDs"),
                applicationId, nodeId, count, lowerBoundary);
    }

    public List<UUID> getChildHierarchyExpertIDs(UUID applicationId, UUID nodeId,
                                                 Integer count, Long lowerBoundary, MutableLong totalCount)
    {
        return rvConnection.getUUIDList(totalCount, getQualifiedName("GetChildHierarchyExpertIDs"),
                applicationId, nodeId, count, lowerBoundary);
    }

    public boolean likeNodes(UUID applicationId, List<UUID> nodeIds, UUID userId)
    {
        return rvConnection.succeed(getQualifiedName("LikeNodes"), applicationId,
                nodeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',', userId, publicMethods.now());
    }

    public boolean likeNode(UUID applicationId, UUID nodeId, UUID userId){
        List<UUID> ids = new ArrayList<UUID>(){{
           if(nodeId != null) add(nodeId);
        }};

        return likeNodes(applicationId, ids, userId);
    }

    public boolean unlikeNodes(UUID applicationId, List<UUID> nodeIds, UUID userId)
    {
        return rvConnection.succeed(getQualifiedName("UnlikeNodes"), applicationId,
                nodeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',', userId, publicMethods.now());
    }

    public boolean unlikeNode(UUID applicationId, UUID nodeId, UUID userId){
        List<UUID> ids = new ArrayList<UUID>(){{
            if(nodeId != null) add(nodeId);
        }};

        return unlikeNodes(applicationId, ids, userId);
    }

    public List<UUID> isFan(UUID applicationId, List<UUID> nodeIds, UUID userId)
    {
        return rvConnection.getUUIDList(getQualifiedName("UnlikeNodes"), applicationId,
                nodeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',', userId);
    }

    public boolean isFan(UUID applicationId, UUID nodeId, UUID userId){
        List<UUID> ids = new ArrayList<UUID>(){{
            if(nodeId != null) add(nodeId);
        }};

        return isFan(applicationId, ids, userId).stream().findFirst().orElse(null) == nodeId;
    }

    public List<UUID> getNodeFansUserIDs(UUID applicationId, UUID nodeId,
                                         Integer count, Long lowerBoundary, MutableLong totalCount)
    {
        return parser.fanUserIds(rvConnection.read(getQualifiedName("GetNodeFansUserIDs"),
                applicationId, nodeId, count, lowerBoundary), totalCount);
    }

    public List<UUID> getNodeFansUserIDs(UUID applicationId, UUID nodeId, Integer count, Long lowerBoundary){
        return getNodeFansUserIDs(applicationId, nodeId, count, lowerBoundary, new MutableLong(0));
    }

    public List<User> getNodeFansUsers(UUID applicationId, UUID nodeId,
                                         Integer count, Long lowerBoundary, MutableLong totalCount){
        return usersDAO.getUsers(applicationId, getNodeFansUserIDs(applicationId, nodeId, count, lowerBoundary, totalCount));
    }

    public List<NodesCount> getFavoriteNodesCount(UUID applicationId, UUID userId, UUID nodeTypeId, UUID nodeId,
                                                  String additionalId, DateTime lowerDateLimit, DateTime upperDateLimit)
    {
        return parser.nodesCount(rvConnection.read(getQualifiedName("GetFavoriteNodesCount"),
                applicationId, userId, nodeTypeId, nodeId, additionalId, lowerDateLimit, upperDateLimit));
    }

    public NodesCount getFavoriteNodesCount(UUID applicationId, UUID userId, UUID nodeTypeId,
                                                  DateTime lowerDateLimit, DateTime upperDateLimit){
        return getFavoriteNodesCount(applicationId, userId, nodeTypeId,
                null, null, lowerDateLimit, upperDateLimit).stream().findFirst().orElse(null);
    }

    public List<Node> getFavoriteNodes(UUID applicationId, UUID userId, UUID nodeTypeId, UUID nodeId,
                                       String additionalId, String searchText, DateTime lowerDateLimit,
                                       DateTime upperDateLimit, Integer lowerBoundary, Integer count)
    {
        return parser.nodes(rvConnection.read(getQualifiedName("GetFavoriteNodes"), applicationId, userId, nodeTypeId,
                nodeId, additionalId, rvConnection.getSearchText(searchText), lowerDateLimit, upperDateLimit, lowerBoundary, count));
    }

    public boolean addComplex(UUID applicationId, UUID listId, UUID nodeTypeId, String name, UUID parentListId, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("AddComplex"),
                applicationId, listId, nodeTypeId, name, null, currentUserId, publicMethods.now(), parentListId, null, null);
    }

    public boolean modifyComplex(UUID applicationId, UUID listId, String name, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("ModifyComplex"),
                applicationId, listId, name, null, currentUserId, publicMethods.now());
    }

    public boolean removeComplexes(UUID applicationId, List<UUID> listIds, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("ArithmeticDeleteComplexes"), applicationId,
                listIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',',
                currentUserId, publicMethods.now());
    }

    public boolean removeComplex(UUID applicationId, UUID listId, UUID currentUserId){
        List<UUID> ids = new ArrayList<UUID>(){{
            if(listId != null) add(listId);
        }};

        return removeComplexes(applicationId, ids, currentUserId);
    }

    public boolean addComplexAdmin(UUID applicationId, UUID listId, UUID userId, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("AddComplexAdmin"),
                applicationId, listId, userId, currentUserId, publicMethods.now());
    }

    public boolean removeComplexAdmin(UUID applicationId, UUID listId, UUID userId, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("RemoveComplexAdmin"),
                applicationId, listId, userId, currentUserId, publicMethods.now());
    }

    public List<NodeList> getLists(UUID applicationId, List<UUID> listIds)
    {
        return parser.lists(rvConnection.read(getQualifiedName("GetListsByIDs"), applicationId,
                listIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ','));
    }

    public NodeList getLists(UUID applicationId, UUID listId){
        List<UUID> ids = new ArrayList<UUID>(){{
            if(listId != null) add(listId);
        }};

        return getLists(applicationId, ids).stream().findFirst().orElse(null);
    }

    public List<NodeList> getLists(UUID applicationId, UUID nodeTypeId, String searchText, Integer count, UUID minId)
    {
        if(count == null) count = 1000;

        return parser.lists(rvConnection.read(getQualifiedName("GetLists"), applicationId, nodeTypeId,
                null, rvConnection.getSearchText(searchText), count, minId));
    }

    public boolean addNodesToComplex(UUID applicationId, UUID listId, List<UUID> nodeIds, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("AddNodesToComplex"), applicationId, listId,
                nodeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',',
                currentUserId, publicMethods.now());
    }

    public boolean addNodeToComplex(UUID applicationId, UUID listId, UUID nodeId, UUID currentUserId){
        List<UUID> ids = new ArrayList<UUID>(){{
            if(nodeId != null) add(nodeId);
        }};

        return addNodesToComplex(applicationId, listId, ids, currentUserId);
    }

    public boolean removeComplexNodes(UUID applicationId, UUID listId, List<UUID> nodeIds, UUID currentUserId) {
        return rvConnection.succeed(getQualifiedName("ArithmeticDeleteComplexNodes"), applicationId, listId,
                nodeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',',
                currentUserId, publicMethods.now());
    }

    public boolean removeComplexNode(UUID applicationId, UUID listId, UUID nodeId, UUID currentUserId){
        List<UUID> ids = new ArrayList<UUID>(){{
            if(nodeId != null) add(nodeId);
        }};

        return removeComplexNodes(applicationId, listId, ids, currentUserId);
    }

    public List<Node> getListNodes(UUID applicationId, UUID listId, UUID nodeTypeId)
    {
        return parser.nodes(rvConnection.read(getQualifiedName("GetListNodes"), applicationId, listId, nodeTypeId, null));
    }

    public List<Node> getListNodes(UUID applicationId, UUID listId){
        return getListNodes(applicationId, listId, null);
    }

    public UUID addTags(UUID applicationId, List<String> tags, UUID currentUserId)
    {
        RVStructuredParam tagsDT = new RVStructuredParam("StringTableType").addColumnMetaData("Value", String.class);
        tags.forEach(tagsDT::addRow);

        return rvConnection.getUUID(getQualifiedName("AddTags"), applicationId, tagsDT, currentUserId, publicMethods.now());
    }

    public UUID addTag(UUID applicationId, String tag, UUID currentUserId){
        if(StringUtils.isBlank(tag)) return null;

        List<String> tags = new ArrayList<String>(){{
            add(tag);
        }};

        return addTags(applicationId, tags, currentUserId);
    }

    public void saveTagsOffline(UUID applicationId, List<String> tags, UUID currentUserId) {
        publicMethods.setTimeout(() -> {
            addTags(applicationId, tags, currentUserId);
        }, 0);
    }

    public List<Tag> searchTags(UUID applicationId, String searchText, Integer count, Integer lowerBoundary)
    {
        return parser.tags(rvConnection.read(getQualifiedName("SearchTags"),
                applicationId, rvConnection.getSearchText(searchText), count, lowerBoundary));
    }

    public List<NodeCreator> getNodeCreators(UUID applicationId, UUID nodeId, Boolean full)
    {
        return parser.nodeCreators(rvConnection.read(getQualifiedName("GetNodeCreators"), applicationId, nodeId, full), full);
    }

    public List<NodeCreator> getNodeCreators(UUID applicationId, UUID nodeId){
        return getNodeCreators(applicationId, nodeId, null);
    }

    public List<Node> getCreatorNodes(UUID applicationId, UUID userId, UUID nodeTypeId)
    {
        return parser.nodes(rvConnection.read(getQualifiedName("GetCreatorNodes"), applicationId, userId, nodeTypeId));
    }

    public List<Node> getCreatorNodes(UUID applicationId, UUID userId){
        return getCreatorNodes(applicationId, userId, null);
    }

    public boolean addExperts(UUID applicationId, UUID nodeId, List<UUID> userIds)
    {
        if (userIds == null || userIds.size() == 0) return false;

        return rvConnection.succeed(getQualifiedName("AddExperts"), applicationId, nodeId,
                userIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',');
    }

    public boolean addExpert(UUID applicationId, UUID nodeId, UUID userId){
        List<UUID> ids = new ArrayList<UUID>(){{
           if(userId != null) add(userId);
        }};

        return addExperts(applicationId, nodeId, ids);
    }

    public boolean removeExperts(UUID applicationId, UUID nodeId, List<UUID> userIds)
    {
        return rvConnection.succeed(getQualifiedName("ArithmeticDeleteExperts"), applicationId, nodeId,
                userIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',');
    }

    public boolean removeExpert(UUID applicationId, UUID nodeId, UUID userId){
        List<UUID> ids = new ArrayList<UUID>(){{
            if(userId != null) add(userId);
        }};

        return removeExperts(applicationId, nodeId, ids);
    }

    public List<Expert> getExperts(UUID applicationId, List<UUID> nodeIds,
                                  Integer count, Long lowerBoundary, MutableLong totalCount)
    {
        return parser.experts(rvConnection.read(getQualifiedName("GetExperts"), applicationId,
                nodeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',', count, lowerBoundary), totalCount);
    }

    public List<Expert> getExperts(UUID applicationId, List<UUID> nodeIds)
    {
        MutableLong totalCount = new MutableLong(0);
        return getExperts(applicationId, nodeIds, null, null, totalCount);
    }

    public List<Expert> getExperts(UUID applicationId, UUID nodeId, Integer count, Long lowerBoundary, MutableLong totalCount) {
        List<UUID> _nIds = new ArrayList<UUID>() {{
            add(nodeId);
        }};

        return getExperts(applicationId, _nIds, count, lowerBoundary, totalCount);
    }

    public List<Expert> getExperts(UUID applicationId, UUID nodeId)
    {
        MutableLong totalCount = new MutableLong(0);
        return getExperts(applicationId, nodeId, null, null, totalCount);
    }

    public List<NodesCount> getExpertiseDomainsCount(UUID applicationId, UUID userId, UUID nodeTypeId, UUID nodeId,
                                                     String additionalId, DateTime lowerDateLimit, DateTime upperDateLimit)
    {
        return parser.nodesCount(rvConnection.read(getQualifiedName("GetExpertiseDomainsCount"),
                applicationId, userId, nodeTypeId, nodeId, additionalId, lowerDateLimit, upperDateLimit));
    }

    public NodesCount getExpertiseDomainsCount(UUID applicationId, UUID userId, UUID nodeTypeId,
                                                     DateTime lowerDateLimit, DateTime upperDateLimit){
        return getExpertiseDomainsCount(applicationId, userId, nodeTypeId,
                null, null, lowerDateLimit, upperDateLimit).stream().findFirst().orElse(null);
    }

    public List<Node> getExpertiseDomains(UUID applicationId, UUID userId, UUID nodeTypeId, UUID nodeId,
                                          String additionalId, String searchText, DateTime lowerDateLimit,
                                          DateTime upperDateLimit, Integer lowerBoundary, Integer count)
    {
        return parser.nodes(rvConnection.read(getQualifiedName("GetExpertiseDomains"), applicationId, userId, nodeTypeId,
                nodeId, additionalId, rvConnection.getSearchText(searchText), lowerDateLimit, upperDateLimit, lowerBoundary, count));
    }

    public List<Expert> getExpertiseDomains(UUID applicationId, List<UUID> userIds, UUID nodeTypeId)
    {
        return parser.experts(rvConnection.read(getQualifiedName("GetUsersExpertiseDomains"), applicationId,
                userIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',',
                nodeTypeId, true, false, false));
    }

    public List<Expert> getExpertiseDomains(UUID applicationId, UUID userId, UUID nodeTypeId){
        List<UUID> ids = new ArrayList<UUID>(){{
            if(userId != null) add(userId);
        }};

        return getExpertiseDomains(applicationId, ids, nodeTypeId);
    }

    public List<UUID> getExpertiseDomainIDs(UUID applicationId, List<UUID> userIds)
    {
        return rvConnection.getUUIDList(getQualifiedName("GetUsersExpertiseDomainIDs"), applicationId,
                userIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',', true, false);
    }

    public List<UUID> getExpertiseDomainIDs(UUID applicationId, UUID userId){
        List<UUID> ids = new ArrayList<UUID>(){{
            if(userId != null) add(userId);
        }};

        return getExpertiseDomainIDs(applicationId, ids);
    }

    public boolean isExpert(UUID applicationId, UUID userId, UUID nodeId)
    {
        return rvConnection.succeed(getQualifiedName("IsExpert"), applicationId, userId, nodeId, true, false);
    }

    public int getExpertsCount(UUID applicationId, UUID nodeId, Boolean distinctUsers)
    {
        return rvConnection.getInt(getQualifiedName("GetExpertsCount"), applicationId, nodeId, distinctUsers, true, false);
    }

    public List<Node> suggestNodeRelations(UUID applicationId, UUID userId, UUID relatedNodeTypeId, Integer count)
    {
        if(count == null) count = 20;

        return parser.nodes(rvConnection.read(getQualifiedName("SuggestNodeRelations"),
                applicationId, userId, null, relatedNodeTypeId, count, publicMethods.now()));
    }

    public List<NodeType> suggestNodeTypesForRelations(UUID applicationId, UUID userId, Integer count) {
        if (count == null) count = 10;

        return parser.nodeTypes(applicationId, rvConnection.read(getQualifiedName("SuggestNodeTypesForRelations"),
                applicationId, userId, null, (count == null ? 10 : count), publicMethods.now()));
    }

    public List<SimilarNode> suggestSimilarNodes(UUID applicationId, UUID nodeId, Integer count)
    {
        return parser.similarNodes(rvConnection.read(getQualifiedName("SuggestSimilarNodes"), applicationId, nodeId, count));
    }

    public List<KnowledgableUser> suggestKnowledgableUsers(UUID applicationId, UUID nodeId, Integer count)
    {
        return parser.knowledgableUsers(rvConnection.read(getQualifiedName("SuggestKnowledgableUsers"), applicationId, nodeId, count));
    }

    public List<UUID> getExistingNodeIDs(UUID applicationId, List<UUID> nodeIds, Boolean searchable, Boolean noContent)
    {
        return rvConnection.getUUIDList(getQualifiedName("GetExistingNodeIDs"), applicationId,
                nodeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',', searchable, noContent);
    }

    public List<UUID> getExistingNodeTypeIDs(UUID applicationId, List<UUID> nodeTypeIds, Boolean noContent)
    {
        return rvConnection.getUUIDList(getQualifiedName("GetExistingNodeTypeIDs"), applicationId,
                nodeTypeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',', noContent);
    }

    public List<NodeInfo> getNodeInfo(UUID applicationId, List<UUID> nodeIds, UUID currentUserId,
                                      Boolean tags, Boolean description, Boolean creator, Boolean contributorsCount,
                                      Boolean likesCount, Boolean visitsCount, Boolean expertsCount, Boolean membersCount,
                                      Boolean childsCount, Boolean relatedNodesCount, Boolean likeStatus)
    {
        if(currentUserId == null) likeStatus = null;

        return parser.nodeInfo(rvConnection.read(getQualifiedName("GetNodeInfo"), applicationId,
                nodeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',', currentUserId,
                tags, description, creator, contributorsCount, likesCount, visitsCount, expertsCount, membersCount,
                childsCount, relatedNodesCount, likeStatus));
    }

    public NodeInfo getNodeInfo(UUID applicationId, UUID nodeId, UUID currentUserId,
                                      Boolean tags, Boolean description, Boolean creator, Boolean contributorsCount,
                                      Boolean likesCount, Boolean visitsCount, Boolean expertsCount, Boolean membersCount,
                                      Boolean childsCount, Boolean relatedNodesCount, Boolean likeStatus){
        List<UUID> ids = new ArrayList<UUID>(){{
           if(nodeId != null) add(nodeId);
        }};

        return getNodeInfo(applicationId, ids, currentUserId, tags, description, creator, contributorsCount,
                likesCount, visitsCount, expertsCount, membersCount, childsCount, relatedNodesCount, likeStatus)
                .stream().findFirst().orElse(null);
    }

    public boolean initializeExtensions(UUID applicationId, UUID ownerId, UUID currentUserId)
    {
        List<Extension> lst = cnUtilities.extendExtensions(applicationId, new ArrayList<>());

        List<String> enabledExtensions = lst.stream().filter(u -> u.getDisabled() == null || !u.getDisabled())
                .map(Extension::getType).map(ExtensionType::toString).collect(Collectors.toList());

        List<String> disabledExtensions = lst.stream().filter(u -> u.getDisabled() != null && u.getDisabled())
                .map(Extension::getType).map(ExtensionType::toString).collect(Collectors.toList());

        return rvConnection.succeed(getQualifiedName("InitializeExtensions"), applicationId, ownerId,
                String.join(",", enabledExtensions), String.join(",", disabledExtensions), ',',
                currentUserId, publicMethods.now());
    }

    private boolean _enableDisableExtension(UUID applicationId, UUID ownerId,
                                          ExtensionType extensionType, boolean disable, UUID currentUserId)
    {
        if (extensionType == ExtensionType.NotSet) return false;

        return rvConnection.succeed(getQualifiedName("EnableDisableExtension"), applicationId, ownerId,
                extensionType.toString(), disable, currentUserId, publicMethods.now());
    }

    public boolean enableExtension(UUID applicationId, UUID ownerId, ExtensionType extensionType, UUID currentUserId)
    {
        return _enableDisableExtension(applicationId, ownerId, extensionType, false, currentUserId);
    }

    public boolean disableExtension(UUID applicationId, UUID ownerId, ExtensionType extensionType, UUID currentUserId)
    {
        return _enableDisableExtension(applicationId, ownerId, extensionType, true, currentUserId);
    }

    public boolean setExtensionTitle(UUID applicationId, UUID ownerId, ExtensionType extensionType, String title, UUID currentUserId)
    {
        if (extensionType == ExtensionType.NotSet) return false;

        return rvConnection.succeed(getQualifiedName("SetExtensionTitle"),
                applicationId, ownerId, extensionType.toString(), title, currentUserId, publicMethods.now());
    }

    public boolean moveExtension(UUID applicationId, UUID ownerId, ExtensionType extensionType, boolean moveDown)
    {
        if (extensionType == ExtensionType.NotSet) return false;
        return rvConnection.succeed(getQualifiedName("MoveExtension"), applicationId, ownerId, extensionType.toString(), moveDown);
    }

    public List<Extension> getExtensions(UUID applicationId, UUID ownerId)
    {
        return parser.extensions(rvConnection.read(getQualifiedName("GetExtensions"), applicationId, ownerId));
    }

    public boolean hasExtension(UUID applicationId, UUID ownerId, ExtensionType extensionType)
    {
        if (extensionType == ExtensionType.NotSet) return false;
        return rvConnection.succeed(getQualifiedName("HasExtension"), applicationId, ownerId, extensionType.toString());
    }

    public List<NodeType> getNodeTypesWithExtension(UUID applicationId, List<ExtensionType> exts)
    {
        String strExts = exts == null ? null :
                exts.stream().filter(u -> u != ExtensionType.NotSet).map(ExtensionType::toString).collect(Collectors.joining(","));

        return parser.nodeTypes(applicationId,
                rvConnection.read(getQualifiedName("GetNodeTypesWithExtension"), applicationId, strExts, ','));
    }

    public List<NodeType> getNodeTypesWithExtension(UUID applicationId, ExtensionType ext) {
        List<ExtensionType> lst = new ArrayList<ExtensionType>() {{
            add(ext);
        }};

        return getNodeTypesWithExtension(applicationId, lst);
    }

    public List<NodesCount> getIntellectualPropertiesCount(UUID applicationId, UUID userId, UUID nodeTypeId, UUID nodeId,
                                                           String additionalId, UUID currentUserId,
                                                           DateTime lowerDateLimit, DateTime upperDateLimit)
    {
        return parser.nodesCount(rvConnection.read(getQualifiedName("GetIntellectualPropertiesCount"),
                applicationId, userId, nodeTypeId, nodeId, additionalId, currentUserId, lowerDateLimit, upperDateLimit));
    }

    public NodesCount getIntellectualPropertiesCount(UUID applicationId, UUID userId, UUID nodeTypeId,
                                                           UUID currentUserId, DateTime lowerDateLimit, DateTime upperDateLimit){
        return getIntellectualPropertiesCount(applicationId, userId, nodeTypeId, null, null,
                currentUserId, lowerDateLimit, upperDateLimit).stream().findFirst().orElse(null);
    }

    public List<Node> getIntellectualProperties(UUID applicationId, UUID userId, UUID nodeTypeId, UUID nodeId,
                                                String additionalId, UUID currentUserId, String searchText, DateTime lowerDateLimit,
                                                DateTime upperDateLimit, Integer lowerBoundary, Integer count)
    {
        return parser.nodes(rvConnection.read(getQualifiedName("GetIntellectualProperties"),
                applicationId, userId, nodeTypeId, nodeId, additionalId, currentUserId,
                rvConnection.getSearchText(searchText), lowerDateLimit, upperDateLimit, lowerBoundary, count));
    }

    public List<Node> getIntellectualPropertiesOfFriends(UUID applicationId, UUID userId, UUID nodeTypeId,
                                                         Integer lowerBoundary, Integer count)
    {
        return parser.nodes(rvConnection.read(getQualifiedName("GetIntellectualPropertiesOfFriends"),
                applicationId, userId, nodeTypeId, lowerBoundary, count));
    }

    public List<Node> getDocumentTreeNodeItems(UUID applicationId, UUID documentTreeNodeId, UUID currenrUserId,
                                               Boolean checkPrivacy, Integer count, Integer lowerBoundary)
    {
        return parser.nodes(rvConnection.read(getQualifiedName("GetDocumentTreeNodeItems"), applicationId, documentTreeNodeId,
                currenrUserId, checkPrivacy, publicMethods.now(), raaivanSettings.DefaultPrivacy(applicationId), count, lowerBoundary));
    }

    public List<Node> getDocumentTreeNodeContents(UUID applicationId, UUID documentTreeNodeId, UUID currenrUserId,
                                                  Boolean checkPrivacy, Integer count, Integer lowerBoundary,
                                                  String searchText, MutableLong totalCount)
    {
        return parser.nodes(rvConnection.read(getQualifiedName("GetDocumentTreeNodeContents"), applicationId, documentTreeNodeId,
                currenrUserId, checkPrivacy, publicMethods.now(), raaivanSettings.DefaultPrivacy(applicationId),
                count, lowerBoundary, rvConnection.getSearchText(searchText)), false, totalCount);
    }

    public List<UUID> isNodeType(UUID applicationId, List<UUID> ids)
    {
        return rvConnection.getUUIDList(getQualifiedName("IsNodeType"), applicationId,
                ids.stream().map(UUID::toString).collect(Collectors.joining(",")), ',');
    }

    public boolean isNodeType(UUID applicationId, UUID id){
        List<UUID> ids = new ArrayList<UUID>(){{
            if(id != null) add(id);
        }};

        return isNodeType(applicationId, ids).stream().findFirst().orElse(null) == id;
    }

    public List<UUID> isNode(UUID applicationId, List<UUID> ids)
    {
        return rvConnection.getUUIDList(getQualifiedName("IsNode"), applicationId,
                ids.stream().map(UUID::toString).collect(Collectors.joining(",")), ',');
    }

    public boolean isNode(UUID applicationId, UUID id){
        List<UUID> ids = new ArrayList<UUID>(){{
            if(id != null) add(id);
        }};

        return isNode(applicationId, ids).stream().findFirst().orElse(null) == id;
    }

    public List<ExploreItem> explore(UUID applicationId, UUID baseId, UUID relatedId, List<UUID> baseTypeIds,
                                     List<UUID> relatedTypeIds, Boolean registrationArea, Boolean tags, Boolean relations,
                                     Integer lowerBoundary, Integer count, String orderBy, Boolean orderByDesc,
                                     String searchText, Boolean checkAccess, UUID currentUserId, MutableLong totalCount)
    {
        return parser.exploreItems(rvConnection.read(getQualifiedName("Explore"), applicationId, baseId, relatedId,
                baseTypeIds.stream().map(UUID::toString).collect(Collectors.joining(",")),
                relatedTypeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',',
                registrationArea, tags, relations, lowerBoundary, count, orderBy, orderByDesc,
                rvConnection.getSearchText(searchText), checkAccess, currentUserId, publicMethods.now(),
                raaivanSettings.DefaultPrivacy(applicationId)), totalCount);
    }

    private boolean _updateFormAndWikiTags(UUID applicationId, List<UUID> nodeIds, UUID currentUserId, Integer count)
    {
        if((nodeIds == null || nodeIds.size() == 0 || currentUserId == null) && (count == null || count <= 0)) return false;

        boolean form = RaaiVanConfig.Modules.FormGenerator;
        boolean wiki = true;

        return rvConnection.succeed(getQualifiedName("UpdateFormAndWikiTags"), applicationId,
                nodeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',', currentUserId, count, form, wiki);
    }

    public boolean updateFormAndWikiTags(UUID applicationId, Integer count){
        return _updateFormAndWikiTags(applicationId, new ArrayList<>(), null, count);
    }

    public boolean updateFormAndWikiTags(UUID applicationId, List<UUID> nodeIds, UUID currentUserId){
        return _updateFormAndWikiTags(applicationId, nodeIds, currentUserId, null);
    }

    /* Service */

    public boolean initializeService(UUID applicationId, UUID nodeTypeId)
    {
        return rvConnection.succeed(getQualifiedName("InitializeService"), applicationId, nodeTypeId);
    }

    public List<CNService> getServices(UUID applicationId, List<UUID> nodeTypeIds)
    {
        return parser.services(rvConnection.read(getQualifiedName("GetServicesByIDs"), applicationId,
                nodeTypeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ','));
    }

    private List<CNService> _getServices(UUID applicationId, UUID nodeTypeIdOrNodeId, UUID currentUserId,
                                         Boolean isDocument, Boolean isKnowledge, Boolean checkPrivacy)
    {
        return parser.services(rvConnection.read(getQualifiedName("GetServices"), applicationId, nodeTypeIdOrNodeId, currentUserId,
                isDocument, isKnowledge, checkPrivacy, publicMethods.now(), raaivanSettings.DefaultPrivacy(applicationId)));
    }

    public List<CNService> getServices(UUID applicationId, UUID currentUserId,
                                       Boolean isDocument, Boolean isKnowledge, Boolean checkPrivacy){
        return _getServices(applicationId, null, currentUserId, isDocument, isKnowledge, checkPrivacy);
    }

    public CNService getService(UUID applicationId, UUID nodeTypeIdOrNodeId){
        return _getServices(applicationId, nodeTypeIdOrNodeId,
                null, null, null, false).stream().findFirst().orElse(null);
    }

    public boolean setServiceTitle(UUID applicationId, UUID nodeTypeId, String title)
    {
        return rvConnection.succeed(getQualifiedName("SetServiceTitle"), applicationId, nodeTypeId, title);
    }

    public boolean setServiceDescription(UUID applicationId, UUID nodeTypeId, String description)
    {
        return rvConnection.succeed(getQualifiedName("SetServiceDescription"), applicationId, nodeTypeId, description);
    }

    public boolean setServiceSuccessMessage(UUID applicationId, UUID nodeTypeId, String message)
    {
        return rvConnection.succeed(getQualifiedName("SetServiceSuccessMessage"), applicationId, nodeTypeId, message);
    }

    public String getServiceSuccessMessage(UUID applicationId, UUID nodeTypeId)
    {
        return rvConnection.getString(getQualifiedName("GetServiceSuccessMessage"), applicationId, nodeTypeId);
    }

    public boolean setServiceAdminType(UUID applicationId, UUID nodeTypeId, ServiceAdminType adminType,
                                       UUID adminNodeId, List<UUID> limitNodeTypeIds, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("SetServiceAdminType"), applicationId, nodeTypeId,
                (adminType == ServiceAdminType.NotSet ? null : adminType.toString()), adminNodeId,
                limitNodeTypeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',',
                currentUserId, publicMethods.now());
    }

    public List<NodeType> getAdminAreaLimits(UUID applicationId, UUID nodeTypeIdOrnodeId)
    {
        return parser.nodeTypes(applicationId,
                rvConnection.read(getQualifiedName("GetAdminAreaLimits"), applicationId, nodeTypeIdOrnodeId));
    }

    public boolean setMaxAcceptableAdminLevel(UUID applicationId, UUID nodeTypeId, Integer maxAcceptableAdminLevel)
    {
        return rvConnection.succeed(getQualifiedName("SetMaxAcceptableAdminLevel"), applicationId, nodeTypeId, maxAcceptableAdminLevel);
    }

    public boolean setContributionLimits(UUID applicationId, UUID nodeTypeId, List<UUID> limitNodeTypeIds, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("SetContributionLimits"), applicationId, nodeTypeId,
                limitNodeTypeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',',
                currentUserId, publicMethods.now());
    }

    public List<NodeType> getContributionLimits(UUID applicationId, UUID nodeTypeIdOrnodeId)
    {
        return parser.nodeTypes(applicationId,
                rvConnection.read(getQualifiedName("GetContributionLimits"), applicationId, nodeTypeIdOrnodeId));
    }

    public boolean enableContribution(UUID applicationId, UUID nodeTypeId, boolean enable)
    {
        return rvConnection.succeed(getQualifiedName("EnableContribution"), applicationId, nodeTypeId, enable);
    }

    public boolean noContentService(UUID applicationId, UUID nodeTypeIdOrNodeId, Boolean value)
    {
        return rvConnection.succeed(getQualifiedName("NoContentService"), applicationId, nodeTypeIdOrNodeId, value);
    }

    public boolean noContentService(UUID applicationId, UUID nodeTypeIdOrNodeId){
        return noContentService(applicationId, nodeTypeIdOrNodeId, null);
    }

    public boolean isDocument(UUID applicationId, UUID nodeTypeIdOrNodeId, Boolean isDocument)
    {
        return rvConnection.succeed(getQualifiedName("IsDocument"), applicationId, nodeTypeIdOrNodeId, isDocument);
    }

    public boolean isDocument(UUID applicationId, UUID nodeTypeIdOrNodeId){
        return isDocument(applicationId, nodeTypeIdOrNodeId, null);
    }

    public boolean isKnowledge(UUID applicationId, UUID nodeTypeIdOrNodeId, Boolean isKnowledge)
    {
        return rvConnection.succeed(getQualifiedName("IsKnowledge"), applicationId, nodeTypeIdOrNodeId, isKnowledge);
    }

    public boolean isKnowledge(UUID applicationId, UUID nodeTypeIdOrNodeId){
        return isKnowledge(applicationId, nodeTypeIdOrNodeId, null);
    }

    private List<UUID> _isTree(UUID applicationId, List<UUID> nodeTypeOrNodeIds, Boolean isTree, MutableBoolean retValue)
    {
        if(isTree != null){
            retValue.setValue(rvConnection.succeed(getQualifiedName("IsTree"), applicationId,
                    nodeTypeOrNodeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',', isTree));
            return new ArrayList<>();
        }
        else {
            return rvConnection.getUUIDList(getQualifiedName("IsTree"), applicationId,
                    nodeTypeOrNodeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',', null);
        }
    }

    public List<UUID> isTree(UUID applicationId, List<UUID> nodeTypeOrNodeIds){
        return _isTree(applicationId, nodeTypeOrNodeIds, null, (new MutableBoolean(false)));
    }

    public boolean isTree(UUID applicationId, UUID nodeTypeOrNodeId){
        List<UUID> ids = new ArrayList<UUID>() {{
            add(nodeTypeOrNodeId);
        }};

        return isTree(applicationId, ids).size() > 0;
    }

    public boolean isTree(UUID applicationId, List<UUID> nodeTypeOrNodeIds, boolean isTree) {
        MutableBoolean retValue = new MutableBoolean(false);
        _isTree(applicationId, nodeTypeOrNodeIds, isTree, retValue);
        return retValue.getValue() != null && retValue.getValue();
    }

    public boolean isTree(UUID applicationId, UUID nodeTypeOrNodeId, boolean isTree) {
        List<UUID> ids = new ArrayList<UUID>() {{
            add(nodeTypeOrNodeId);
        }};

        return isTree(applicationId, ids, isTree);
    }

    public boolean editableForAdmin(UUID applicationId, UUID nodeTypeId, boolean editable)
    {
        return rvConnection.succeed(getQualifiedName("EditableForAdmin"), applicationId, nodeTypeId, editable);
    }

    public boolean editableForCreator(UUID applicationId, UUID nodeTypeId, boolean editable)
    {
        return rvConnection.succeed(getQualifiedName("EditableForCreator"), applicationId, nodeTypeId, editable);
    }

    public boolean editableForOwners(UUID applicationId, UUID nodeTypeId, boolean editable)
    {
        return rvConnection.succeed(getQualifiedName("EditableForOwners"), applicationId, nodeTypeId, editable);
    }

    public boolean editableForExperts(UUID applicationId, UUID nodeTypeId, boolean editable)
    {
        return rvConnection.succeed(getQualifiedName("EditableForExperts"), applicationId, nodeTypeId, editable);
    }

    public boolean editableForMembers(UUID applicationId, UUID nodeTypeId, boolean editable)
    {
        return rvConnection.succeed(getQualifiedName("EditableForMembers"), applicationId, nodeTypeId, editable);
    }

    public boolean editSuggestion(UUID applicationId, UUID nodeTypeId, boolean enable)
    {
        return rvConnection.succeed(getQualifiedName("EditSuggestion"), applicationId, nodeTypeId, enable);
    }

    public boolean addFreeUser(UUID applicationId, UUID nodeTypeId, UUID userId, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("AddFreeUser"),
                applicationId, nodeTypeId, userId, currentUserId, publicMethods.now());
    }

    public boolean removeFreeUser(UUID applicationId, UUID nodeTypeId, UUID userId, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("ArithmeticDeleteFreeUser"),
                applicationId, nodeTypeId, userId, currentUserId, publicMethods.now());
    }

    public List<User> getFreeUsers(UUID applicationId, UUID nodeTypeId)
    {
        return usersDAO.getUsers(applicationId, rvConnection.getUUIDList(getQualifiedName("GetFreeUserIDs"), applicationId, nodeTypeId));
    }

    public boolean isFreeUser(UUID applicationId, UUID nodeTypeIdOrNodeId, UUID userId)
    {
        return rvConnection.succeed(getQualifiedName("IsFreeUser"), applicationId, nodeTypeIdOrNodeId, userId);
    }

    public boolean addServiceAdmin(UUID applicationId, UUID nodeTypeId, UUID userId, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("AddServiceAdmin"),
                applicationId, nodeTypeId, userId, currentUserId, publicMethods.now());
    }

    public boolean removeServiceAdmin(UUID applicationId, UUID nodeTypeId, UUID userId, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("ArithmeticDeleteServiceAdmin"),
                applicationId, nodeTypeId, userId, currentUserId, publicMethods.now());
    }

    public List<User> getServiceAdmins(UUID applicationId, UUID nodeTypeId)
    {
        return usersDAO.getUsers(applicationId,
                rvConnection.getUUIDList(getQualifiedName("GetServiceAdminIDs"), applicationId, nodeTypeId));
    }

    public List<UUID> isServiceAdmin(UUID applicationId, List<UUID> nodeTypeIdOrNodeIds, UUID userId)
    {
        return rvConnection.getUUIDList(getQualifiedName("IsServiceAdmin"), applicationId,
                nodeTypeIdOrNodeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',', userId);
    }

    public boolean isServiceAdmin(UUID applicationId, UUID nodeTypeIdOrNodeId, UUID userId){
        List<UUID> ids = new ArrayList<UUID>(){{
           if(nodeTypeIdOrNodeId != null) add(nodeTypeIdOrNodeId);
        }};

        return isServiceAdmin(applicationId, ids, userId).size() > 0;
    }

    public boolean registerNewNode(UUID applicationId, UUID nodeId, UUID nodeTypeId, String additionalId_main,
                                   String additionalId, UUID parentNodeId, UUID documentTreeNodeId,
                                   UUID previousVersionId, String name, String description, List<String> tags,
                                   UUID currentUserId, UUID ownerId, UUID adminAreaId, UUID workflowId,
                                   UUID formInstanceId, UUID wfDirectorNodeId, UUID wfDirectorUserId,
                                   List<NodeCreator> contributors, List<Dashboard> retDashboards, StringBuilder errorMessage) {
        if (tags == null) tags = new ArrayList<>();
        if (contributors == null) contributors = new ArrayList<>();

        RVStructuredParam creatorsDT = new RVStructuredParam("GuidFloatTableType")
                .addColumnMetaData("FirstValue", UUID.class)
                .addColumnMetaData("SecondValue", Double.class);
        contributors.forEach(u -> creatorsDT.addRow(u.getUser().getUserID(), u.getCollaborationShare()));

        return rvConnection.getDashboards(errorMessage, retDashboards, getQualifiedName("RegisterNewNode"),
                applicationId, nodeId, nodeTypeId, additionalId_main, additionalId, parentNodeId, documentTreeNodeId,
                previousVersionId, name, description, (tags.size() == 0 ? null : rvConnection.getTagsText(tags)),
                currentUserId, publicMethods.now(), creatorsDT, ownerId,
                workflowId, adminAreaId, formInstanceId, wfDirectorNodeId, wfDirectorUserId) > 0;
    }

    public boolean registerNewNode(UUID applicationId, UUID nodeId, UUID nodeTypeId, String additionalId_main,
                                   String additionalId, UUID parentNodeId, UUID documentTreeNodeId,
                                   UUID previousVersionId, String name, String description, List<String> tags,
                                   UUID currentUserId, UUID ownerId, UUID adminAreaId, UUID workflowId,
                                   UUID formInstanceId, UUID wfDirectorNodeId, UUID wfDirectorUserId,
                                   List<NodeCreator> contributors, StringBuilder errorMessage){
        return registerNewNode(applicationId, nodeId, nodeTypeId, additionalId_main, additionalId, parentNodeId,
                documentTreeNodeId, previousVersionId, name, description, tags, currentUserId, ownerId,
                adminAreaId, workflowId, formInstanceId, wfDirectorNodeId, wfDirectorUserId, contributors,
                new ArrayList<>(), errorMessage);
    }

    public boolean setAdminArea(UUID applicationId, UUID nodeId, UUID areaId)
    {
        return rvConnection.succeed(getQualifiedName("SetAdminArea"), applicationId, nodeId, areaId);
    }

    public boolean setContributors(UUID applicationId, UUID nodeId, List<NodeCreator> contributors,
                                   UUID ownerId, UUID currentUserId, StringBuilder errorMessage)
    {
        if (contributors == null) contributors = new ArrayList<>();

        RVStructuredParam creatorsDT = new RVStructuredParam("GuidFloatTableType")
                .addColumnMetaData("FirstValue", UUID.class)
                .addColumnMetaData("SecondValue", Double.class);
        contributors.forEach(u -> creatorsDT.addRow(u.getUser().getUserID(), u.getCollaborationShare()));

        return rvConnection.succeed(errorMessage, getQualifiedName("SetContributors"),
                applicationId, nodeId, creatorsDT, ownerId, currentUserId, publicMethods.now());
    }

    /* end of Service */
}