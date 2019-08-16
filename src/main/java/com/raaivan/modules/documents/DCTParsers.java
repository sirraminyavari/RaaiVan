package com.raaivan.modules.documents;

import com.raaivan.util.dbutil.RVResultSet;
import com.raaivan.modules.documents.beans.DocFileInfo;
import com.raaivan.modules.documents.beans.Tree;
import com.raaivan.modules.documents.beans.TreeNode;
import com.raaivan.modules.documents.enums.FileOwnerTypes;
import com.raaivan.util.PublicMethods;
import com.raaivan.util.RVBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.ArrayList;
import java.util.List;

@Component
@ApplicationScope
public class DCTParsers {
    private PublicMethods publicMethods;

    @Autowired
    public void _setDependencies(PublicMethods publicMethods) {
        if (this.publicMethods == null) this.publicMethods = publicMethods;
    }

    public List<Tree> trees(RVResultSet resultSet)
    {
        List<Tree> ret = new ArrayList<>();

        for(int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i){
            try {
                Tree e = RVBeanFactory.getBean(Tree.class);

                e.setTreeID(publicMethods.parseUUID((String) resultSet.getValue(i, "TreeID")));
                e.setName((String) resultSet.getValue(i, "Name"));
                e.setDescription((String) resultSet.getValue(i, "Description"));

                ret.add(e);
            }
            catch (Exception e){
            }
        }

        return ret;
    }

    public List<TreeNode> treeNodes(RVResultSet resultSet)
    {
        List<TreeNode> ret = new ArrayList<>();

        for(int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i){
            try {
                TreeNode e = RVBeanFactory.getBean(TreeNode.class);

                e.setTreeNodeID(publicMethods.parseUUID((String) resultSet.getValue(i, "TreeNodeID")));
                e.setTreeID(publicMethods.parseUUID((String) resultSet.getValue(i, "TreeID")));
                e.setParentNodeID(publicMethods.parseUUID((String) resultSet.getValue(i, "ParentNodeID")));
                e.setName((String) resultSet.getValue(i, "Name"));
                e.setHasChild((Boolean) resultSet.getValue(i, "HasChild"));

                ret.add(e);
            }
            catch (Exception e){
            }
        }

        return ret;
    }

    public List<DocFileInfo> files(RVResultSet resultSet)
    {
        List<DocFileInfo> ret = new ArrayList<>();

        for(int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i){
            try {
                DocFileInfo e = RVBeanFactory.getBean(DocFileInfo.class);

                e.setOwnerID(publicMethods.parseUUID((String) resultSet.getValue(i, "OwnerID")));
                e.setFileID(publicMethods.parseUUID((String) resultSet.getValue(i, "FileID")));
                e.setFileName((String) resultSet.getValue(i, "FileName"));
                e.setExtension((String) resultSet.getValue(i, "Extension"));
                e.setSize((Long) resultSet.getValue(i, "Size"));
                e.setOwnerType(publicMethods.lookupEnum(FileOwnerTypes.class,
                        (String) resultSet.getValue(i, "OwnerType"), FileOwnerTypes.None));

                ret.add(e);
            }
            catch (Exception e){
            }
        }

        return ret;
    }

    public List<DocFileInfo> fileOwnerNodes(RVResultSet resultSet)
    {
        List<DocFileInfo> ret = new ArrayList<>();

        for(int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i){
            try {
                DocFileInfo e = RVBeanFactory.getBean(DocFileInfo.class);

                e.setFileID(publicMethods.parseUUID((String) resultSet.getValue(i, "FileID")));
                e.getOwnerNode().setNodeID(publicMethods.parseUUID((String) resultSet.getValue(i, "NodeID")));
                e.getOwnerNode().setName((String) resultSet.getValue(i, "Name"));
                e.getOwnerNode().setNodeType((String) resultSet.getValue(i, "NodeType"));

                ret.add(e);
            }
            catch (Exception e){
            }
        }

        return ret;
    }
}
