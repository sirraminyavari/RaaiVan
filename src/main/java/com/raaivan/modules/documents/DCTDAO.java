package com.raaivan.modules.documents;

import com.raaivan.util.dbutil.RVConnection;
import com.raaivan.util.dbutil.RVStructuredParam;
import com.raaivan.modules.documents.beans.DocFileInfo;
import com.raaivan.modules.documents.beans.Tree;
import com.raaivan.modules.documents.beans.TreeNode;
import com.raaivan.modules.documents.enums.FileOwnerTypes;
import com.raaivan.modules.rv.beans.Hierarchy;
import com.raaivan.util.PublicMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@ApplicationScope
public class DCTDAO {
    private RVConnection rvConnection;
    private DCTParsers parser;
    private PublicMethods publicMethods;

    @Autowired
    public void _setDependencies( RVConnection rvConnection, DCTParsers parser,PublicMethods publicMethods) {
        if (this.rvConnection == null) this.rvConnection = rvConnection;
        if (this.parser == null) this.parser = parser;
        if (this.publicMethods == null) this.publicMethods = publicMethods;
    }

    private String getQualifiedName(String name){
        return "[dbo].[DCT_" + name + "]";
    }

    public boolean createTree(UUID applicationId, UUID treeId, Boolean isPrivate, UUID ownerId,
                              String name, String description, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("CreateTree"), applicationId, treeId, isPrivate, ownerId,
                name, description, currentUserId, publicMethods.now(), null, null);
    }

    public boolean modifyTree(UUID applicationId, UUID treeId, String name, String description, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("ChangeTree"),
                applicationId, treeId, name, description, currentUserId, publicMethods.now(), null);
    }

    public boolean removeTrees(UUID applicationId, List<UUID> treeIds, UUID ownerId, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("ArithmeticDeleteTree"), applicationId,
                treeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',',
                ownerId, currentUserId, publicMethods.now());
    }

    public boolean removeTree(UUID applicationId, UUID treeId, UUID ownerId, UUID currentUserId) {
        List<UUID> ids = new ArrayList<UUID>() {{
            add(treeId);
        }};

        return removeTrees(applicationId, ids, ownerId, currentUserId);
    }

    public boolean recycleTree(UUID applicationId, UUID treeId, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("RecycleTree"), applicationId, treeId, currentUserId, publicMethods.now());
    }

    public List<Tree> getTrees(UUID applicationId, UUID ownerId, Boolean archive)
    {
        return parser.trees(rvConnection.read(getQualifiedName("GetTrees"), applicationId, ownerId, archive));
    }

    public List<Tree> getTrees(UUID applicationId, UUID ownerId)
    {
        return getTrees(applicationId, ownerId, null);
    }

    public List<Tree> getTrees(UUID applicationId, Boolean archive)
    {
        return getTrees(applicationId, null, archive);
    }

    public List<Tree> getTrees(UUID applicationId)
    {
        return getTrees(applicationId, null, null);
    }

    public List<Tree> getTrees(UUID applicationId, List<UUID> treeIds)
    {
        return parser.trees(rvConnection.read(getQualifiedName("GetTreesByIDs"), applicationId,
                treeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ','));
    }

    public Tree getTree(UUID applicationId, UUID treeId) {
        List<UUID> ids = new ArrayList<UUID>() {{
            add(treeId);
        }};

        List<Tree> lst = getTrees(applicationId, ids);

        return lst.size() == 0 ? null : lst.get(0);
    }

    public boolean addTreeNode(UUID applicationId, UUID treeNodeId, UUID treeId, UUID parentNodeId,
                               String name, String description, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("AddTreeNode"), applicationId, treeNodeId, treeId,
                parentNodeId, name, description, currentUserId, publicMethods.now(), null);
    }

    public boolean modifyTreeNode(UUID applicationId, UUID treeNodeId, String name, String description, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("ChangeTreeNode"),
                applicationId, treeNodeId, name, description, currentUserId, publicMethods.now());
    }

    public List<UUID> copyTreesOrTreeNodes(UUID applicationId, UUID treeIdOrTreeNodeId, List<UUID> copiedIds, UUID currentUserId)
    {
        return rvConnection.getUUIDList(getQualifiedName("CopyTreesOrTreeNodes"), applicationId, treeIdOrTreeNodeId,
                copiedIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',',
                currentUserId, publicMethods.now());
    }

    public List<UUID> moveTreesOrTreeNodes(UUID applicationId, UUID treeIdOrTreeNodeId,
                                            List<UUID> movedIds, UUID currentUserId, StringBuilder errorMessage)
    {
        return rvConnection.getUUIDList(errorMessage, getQualifiedName("MoveTreesOrTreeNodes"), applicationId,
                treeIdOrTreeNodeId, movedIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',',
                currentUserId, publicMethods.now());
    }

    public boolean moveTreeNodes(UUID applicationId, List<UUID> treeNodeIds, UUID parentTreeNodeId,
                                UUID currentUserId, StringBuilder errorMessage)
    {
        return rvConnection.succeed(errorMessage, getQualifiedName("MoveTreeNode"), applicationId,
                treeNodeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',',
                parentTreeNodeId, currentUserId, publicMethods.now());
    }

    public boolean removeTreeNodes(UUID applicationId, List<UUID> treeNodeIds,
                                   UUID treeOwnerId, Boolean removeHierarchy, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("ArithmeticDeleteTreeNode"), applicationId,
                treeNodeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',',
                treeOwnerId, removeHierarchy, currentUserId, publicMethods.now());
    }

    public List<TreeNode> getTreeNodes(UUID applicationId, UUID treeId)
    {
        return parser.treeNodes(rvConnection.read(getQualifiedName("GetTreeNodes"), applicationId, treeId));
    }

    public List<TreeNode> getTreeNodes(UUID applicationId,  List<UUID> treeNodeIds)
    {
        return parser.treeNodes(rvConnection.read(getQualifiedName("GetTreeNodesByIDs"), applicationId,
                treeNodeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ','));
    }

    public TreeNode getTreeNode(UUID applicationId,  UUID treeNodeId) {
        List<UUID> ids = new ArrayList<UUID>() {{
            add(treeNodeId);
        }};

        List<TreeNode> lst = getTreeNodes(applicationId, ids);

        return lst.size() == 0 ? null : lst.get(0);
    }

    public List<TreeNode> getRootNodes(UUID applicationId, UUID treeId)
    {
        return parser.treeNodes(rvConnection.read(getQualifiedName("GetRootNodes"), applicationId, treeId));
    }

    public List<TreeNode> getChildNodes(UUID applicationId, UUID parentNodeId, UUID treeId, String searchText)
    {
        if(parentNodeId == null && treeId == null) return new ArrayList<>();

        return parser.treeNodes(rvConnection.read(getQualifiedName("GetChildNodes"),
                applicationId, parentNodeId, treeId, rvConnection.getSearchText(searchText)));
    }

    public List<TreeNode> getChildNodes(UUID applicationId, UUID parentNodeId, UUID treeId){
        return getChildNodes(applicationId, parentNodeId, treeId, null);
    }

    public List<TreeNode> getChildNodes(UUID applicationId, UUID parentNodeId, String searchText){
        return getChildNodes(applicationId, parentNodeId, null, searchText);
    }

    public List<TreeNode> getChildNodes(UUID applicationId, UUID parentNodeId){
        return getChildNodes(applicationId, parentNodeId, null, null);
    }

    public TreeNode getParentNode(UUID applicationId, UUID treeNodeId)
    {
        List<TreeNode> lst = parser.treeNodes(rvConnection.read(getQualifiedName("GetParentNode"), applicationId, treeNodeId));
        return lst.size() == 0 ? null : lst.get(0);
    }

    public boolean addFiles(UUID applicationId, UUID ownerId, FileOwnerTypes ownerType,
                            List<DocFileInfo> attachments, UUID currentUserId) {
        RVStructuredParam attachmentsDT = new RVStructuredParam("DocFileInfoTableType")
                .addColumnMetaData("FileID", UUID.class)
                .addColumnMetaData("FileName", String.class)
                .addColumnMetaData("Extension", String.class)
                .addColumnMetaData("MIME", String.class)
                .addColumnMetaData("Size", Long.class)
                .addColumnMetaData("OwnerID", UUID.class)
                .addColumnMetaData("OwnerType", String.class);
        attachments.forEach(u -> {
            String strOt = u.getOwnerType() == FileOwnerTypes.None ? null : u.getOwnerType().toString();
            attachmentsDT.addRow(u.getFileID(), u.getFileName(), u.getExtension(), u.MIME(), u.getSize(), u.getOwnerID(), strOt);
        });

        return rvConnection.succeed(getQualifiedName("AddFiles"), applicationId, ownerId,
                (ownerType == FileOwnerTypes.None ? null : ownerType.toString()), attachmentsDT, currentUserId, publicMethods.now());
    }

    public boolean addFiles(UUID applicationId, UUID ownerId,
                            FileOwnerTypes ownerType, DocFileInfo attachment, UUID currentUserId) {
        List<DocFileInfo> lst = new ArrayList<DocFileInfo>() {{
            add(attachment);
        }};

        return addFiles(applicationId, ownerId, ownerType, lst, currentUserId);
    }

    public List<DocFileInfo> getOwnerFiles(UUID applicationId, List<UUID> ownerIds, FileOwnerTypes ownerType)
    {
        return parser.files(rvConnection.read(getQualifiedName("GetOwnerFiles"), applicationId,
                ownerIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',',
                (ownerType == FileOwnerTypes.None ? null : ownerType.toString())));
    }

    public List<DocFileInfo> getOwnerFiles(UUID applicationId, List<UUID> ownerIds)
    {
        return getOwnerFiles(applicationId, ownerIds, FileOwnerTypes.None);
    }

    public List<DocFileInfo> getOwnerFiles(UUID applicationId, UUID ownerId, FileOwnerTypes ownerType) {
        List<UUID> ids = new ArrayList<UUID>() {{
            add(ownerId);
        }};

        return getOwnerFiles(applicationId, ids, ownerType);
    }

    public List<DocFileInfo> getOwnerFiles(UUID applicationId, UUID ownerId) {
        return getOwnerFiles(applicationId, ownerId, FileOwnerTypes.None);
    }

    public List<DocFileInfo> getFiles(UUID applicationId, List<UUID> fileIds)
    {
        return parser.files(rvConnection.read(getQualifiedName("GetFilesByIDs"), applicationId,
                fileIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ','));
    }

    public DocFileInfo getFile(UUID applicationId, UUID fileId) {
        List<UUID> ids = new ArrayList<UUID>() {{
            add(fileId);
        }};

        List<DocFileInfo> lst = getFiles(applicationId, ids);

        return lst.size() == 0 ? null : lst.get(0);
    }

    public List<DocFileInfo> getFileOwnerNodes(UUID applicationId, List<UUID> fileIds)
    {
        return parser.fileOwnerNodes(rvConnection.read(getQualifiedName("GetFileOwnerNodes"), applicationId,
                fileIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ','));
    }

    public boolean removeFiles(UUID applicationId, UUID ownerId, List<UUID> fileIds)
    {
        return rvConnection.succeed(getQualifiedName("ArithmeticDeleteFiles"), applicationId, ownerId,
                fileIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',');
    }

    public boolean removeFiles(UUID applicationId, List<UUID> fileIds)
    {
        return removeFiles(applicationId, null, fileIds);
    }

    public boolean removeFile(UUID applicationId, UUID ownerId, UUID fileId)
    {
        List<UUID> ids = new ArrayList<UUID>(){{add(fileId);}};
        return removeFiles(applicationId, ownerId, ids);
    }

    public boolean removeFile(UUID applicationId, UUID fileId)
    {
        return removeFile(applicationId, null, fileId);
    }

    public boolean copyFile(UUID applicationId, UUID ownerId, UUID fileId, FileOwnerTypes ownerType, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("CopyFile"), applicationId, ownerId, fileId,
                (ownerType == FileOwnerTypes.None ? null : ownerType.toString()), currentUserId, publicMethods.now());
    }

    public List<Hierarchy> getTreeNodeHierarchy(UUID applicationId, UUID treeNodeId)
    {
        return rvConnection.getHierarchy(getQualifiedName("GetTreeNodeHierarchy"), applicationId, treeNodeId);
    }

    //get not extracted files from DB
    public List<DocFileInfo> getNotExtractedFiles(UUID applicationId, String allowedExtractions, Character delimiter, Integer count)
    {
        return parser.files(rvConnection.read(getQualifiedName("GetNotExtractedFiles"),
                applicationId, allowedExtractions, delimiter, count));
    }

    //save extracted file content in DB
    public boolean saveFileContent(UUID applicationId, UUID fileId, String content,
                                   boolean notExtractable, boolean fileNotFound, double duration, String errorText)
    {
        return rvConnection.succeed(getQualifiedName("SaveFileContent"),
                applicationId, fileId, content, notExtractable, fileNotFound, duration, publicMethods.now(), errorText);
    }

    public boolean setTreeNodesOrder(UUID applicationId, List<UUID> treeNodeIds)
    {
        return rvConnection.succeed(getQualifiedName("SetTreeNodesOrder"), applicationId,
                treeNodeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',');
    }

    public boolean isPrivateTree(UUID applicationId, UUID treeIdOrTreeNodeId)
    {
        return rvConnection.succeed(getQualifiedName("IsPrivateTree"), applicationId, treeIdOrTreeNodeId);
    }

    public boolean addOwnerTree(UUID applicationId, UUID ownerId, UUID treeId, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("AddOwnerTree"),
                applicationId, ownerId, treeId, currentUserId, publicMethods.now());
    }

    public boolean removeOwnerTree(UUID applicationId, UUID ownerId, UUID treeId, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("ArithmeticDeleteOwnerTree"),
                applicationId, ownerId, treeId, currentUserId, publicMethods.now());
    }

    public UUID getTreeOwnerID(UUID applicationId, UUID treeIdOrTreeNodeId)
    {
        return rvConnection.getUUID(getQualifiedName("GetTreeOwnerID"), applicationId, treeIdOrTreeNodeId);
    }

    public List<Tree> getOwnerTrees(UUID applicationId, UUID ownerId)
    {
        return parser.trees(rvConnection.read(getQualifiedName("GetOwnerTrees"), applicationId, ownerId));
    }

    public List<Tree> cloneTrees(UUID applicationId, List<UUID> treeIds, UUID ownerId, Boolean allowMultiple, UUID currentUserId)
    {
        return parser.trees(rvConnection.read(getQualifiedName("CloneTrees"), applicationId,
                treeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',',
                ownerId, allowMultiple, currentUserId, publicMethods.now()));
    }

    public boolean addTreeNodeContents(UUID applicationId, UUID treeNodeId, List<UUID> nodeIds, UUID removeFrom, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("AddTreeNodeContents"), applicationId, treeNodeId,
                nodeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',',
                removeFrom, currentUserId, publicMethods.now());
    }

    public boolean removeTreeNodeContents(UUID applicationId, UUID treeNodeId, List<UUID> nodeIds, UUID currentUserId)
    {
        return rvConnection.succeed(getQualifiedName("RemoveTreeNodeContents"), applicationId, treeNodeId,
                nodeIds.stream().map(UUID::toString).collect(Collectors.joining(",")), ',',
                currentUserId, publicMethods.now());
    }
}
