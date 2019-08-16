package com.raaivan.modules.dataexchange;

import com.raaivan.util.dbutil.RVConnection;
import com.raaivan.util.dbutil.RVStructuredParam;
import com.raaivan.modules.dataexchange.beans.ExchangeMember;
import com.raaivan.modules.dataexchange.beans.ExchangeNode;
import com.raaivan.modules.dataexchange.beans.ExchangeRelation;
import com.raaivan.modules.dataexchange.beans.ExchangeUser;
import com.raaivan.util.PublicMethods;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.List;
import java.util.UUID;

@Service
@ApplicationScope
public class DEDAO {
    private RVConnection rvConnection;
    private DEParsers parser;
    private PublicMethods publicMethods;

    @Autowired
    public void _setDependencies(RVConnection rvConnection, DEParsers parser, PublicMethods publicMethods) {
        if (this.rvConnection == null) this.rvConnection = rvConnection;
        if (this.parser == null) this.parser = parser;
        if (this.publicMethods == null) this.publicMethods = publicMethods;
    }

    private String getQualifiedName(String name){
        return "[dbo].[DE_" + name + "]";
    }

    public boolean updateNodes(UUID applicationId, List<ExchangeNode> nodes,
                               UUID nodeTypeId, String nodeTypeAdditionalId, UUID currentUserId, List<UUID> newNodeIds)
    {
        RVStructuredParam nodesDT = new RVStructuredParam("ExchangeNodeTableType")
                .addColumnMetaData("NodeID", UUID.class)
                .addColumnMetaData("NodeAdditionalID", String.class)
                .addColumnMetaData("Name", String.class)
                .addColumnMetaData("ParentAdditionalID", String.class)
                .addColumnMetaData("Abstract", String.class)
                .addColumnMetaData("Tags", String.class);
        nodes.forEach(u -> {
            if(StringUtils.isBlank(u.getAdditionalID())) u.setAdditionalID(null);
            if(StringUtils.isBlank(u.getName())) u.setName("");
            if(StringUtils.isBlank(u.getParentAdditionalID())) u.setParentAdditionalID(null);
            if(StringUtils.isBlank(u.getAbstract())) u.setAbstract("");
            if(StringUtils.isBlank(u.getTags())) u.setTags("");

            nodesDT.addRow(u.getNodeID(), u.getAdditionalID(), u.getName().substring(0, Math.min(250, u.getName().length())),
                    u.getParentAdditionalID(), u.getAbstract(), u.getTags());
        });

        return parser.updateNodesResults(rvConnection.read(getQualifiedName("UpdateNodes"), applicationId, nodeTypeId,
                nodeTypeAdditionalId, nodesDT, currentUserId, publicMethods.now()), newNodeIds);
    }

    public boolean updateUsers(UUID applicationId, List<ExchangeUser> users)
    {
        RVStructuredParam usersDT = new RVStructuredParam("ExchangeUserTableType")
                .addColumnMetaData("UserID", UUID.class)
                .addColumnMetaData("UserName", String.class)
                .addColumnMetaData("NewUserName", String.class)
                .addColumnMetaData("FirstName", String.class)
                .addColumnMetaData("LastName", String.class)
                .addColumnMetaData("EmploymentType", String.class)
                .addColumnMetaData("DepartmentID", String.class)
                .addColumnMetaData("IsManager", Boolean.class)
                .addColumnMetaData("Email", String.class)
                .addColumnMetaData("Password", String.class)
                .addColumnMetaData("PasswordSalt", String.class)
                .addColumnMetaData("EncryptedPassword", String.class);
        users.forEach(u -> usersDT.addRow(null, u.getUserName(), u.getNewUserName(), u.getFirstName(), u.getLastName(),
                null, u.getDepartmentID(), u.getManager(), u.getEmail(), u.getPassword().getSalted(),
                u.getPassword().getSalt(), u.getPassword().getEncrypted()));

        return rvConnection.succeed(getQualifiedName("UpdateUsers"), applicationId, usersDT, publicMethods.now());
    }

    public boolean updateMembers(UUID applicationId, List<ExchangeMember> members)
    {
        RVStructuredParam membersDT = new RVStructuredParam("ExchangeMemberTableType")
                .addColumnMetaData("NodeTypeAdditionalID", String.class)
                .addColumnMetaData("NodeAdditionalID", String.class)
                .addColumnMetaData("NodeID", UUID.class)
                .addColumnMetaData("UserName", String.class)
                .addColumnMetaData("IsAdmin", Boolean.class);
        members.forEach(u -> membersDT.addRow(u.getNodeTypeAdditionalID(), u.getNodeAdditionalID(),
                u.getNodeID(), u.getUserName(), u.getAdmin()));

        return rvConnection.succeed(getQualifiedName("UpdateMembers"), applicationId, membersDT, publicMethods.now(), true);
    }

    public boolean updateExperts(UUID applicationId, List<ExchangeMember> experts)
    {
        RVStructuredParam expertsDT = new RVStructuredParam("ExchangeMemberTableType")
                .addColumnMetaData("NodeTypeAdditionalID", String.class)
                .addColumnMetaData("NodeAdditionalID", String.class)
                .addColumnMetaData("NodeID", UUID.class)
                .addColumnMetaData("UserName", String.class)
                .addColumnMetaData("IsAdmin", Boolean.class);
        experts.forEach(u -> expertsDT.addRow(u.getNodeTypeAdditionalID(), u.getNodeAdditionalID(),
                u.getNodeID(), u.getUserName(), false));

        return rvConnection.succeed(getQualifiedName("UpdateExperts"), applicationId, expertsDT, publicMethods.now());
    }

    public boolean updateRelations(UUID applicationId, UUID currentUserId, List<ExchangeRelation> relations)
    {
        RVStructuredParam relationsDT = new RVStructuredParam("ExchangeRelationTableType")
                .addColumnMetaData("SourceTypeAdditionalID", String.class)
                .addColumnMetaData("SourceAdditionalID", String.class)
                .addColumnMetaData("SourceID", UUID.class)
                .addColumnMetaData("DestinationTypeAdditionalID", String.class)
                .addColumnMetaData("DestinationAdditionalID", String.class)
                .addColumnMetaData("DestinationID", UUID.class)
                .addColumnMetaData("Bidirectional", Boolean.class);
        relations.forEach(u -> relationsDT.addRow(u.getSourceTypeAdditionalID(), u.getSourceAdditionalID(),
                u.getSourceID(), u.getDestinationTypeAdditionalID(), u.getDestinationAdditionalID(),
                u.getDestinationID(), u.getBidirectional()));

        return rvConnection.succeed(getQualifiedName("UpdateRelations"), applicationId, currentUserId, relationsDT, publicMethods.now());
    }
}
