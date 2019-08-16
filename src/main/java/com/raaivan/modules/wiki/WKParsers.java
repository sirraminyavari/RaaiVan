package com.raaivan.modules.wiki;

import com.raaivan.util.dbutil.RVResultSet;
import com.raaivan.modules.rv.beans.MutableUUID;
import com.raaivan.modules.wiki.beans.Change;
import com.raaivan.modules.wiki.beans.Paragraph;
import com.raaivan.modules.wiki.beans.WikiTitle;
import com.raaivan.modules.wiki.enums.WikiOwnerType;
import com.raaivan.modules.wiki.enums.WikiStatuses;
import com.raaivan.util.PublicMethods;
import com.raaivan.util.RVBeanFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.ArrayList;
import java.util.List;

@Component
@ApplicationScope
public class WKParsers {
    private PublicMethods publicMethods;

    @Autowired
    public void _setDependencies(PublicMethods publicMethods) {
        if (this.publicMethods == null) this.publicMethods = publicMethods;
    }

    public List<WikiTitle> titles(RVResultSet resultSet)
    {
        List<WikiTitle> ret = new ArrayList<>();

        for(int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i){
            try {
                WikiTitle e = RVBeanFactory.getBean(WikiTitle.class);

                e.setTitleID(publicMethods.parseUUID((String) resultSet.getValue(i, "TitleID")));
                e.setOwnerID(publicMethods.parseUUID((String) resultSet.getValue(i, "OwnerID")));
                e.setTitle((String) resultSet.getValue(i, "Title"));
                e.setSequenceNumber((Integer) resultSet.getValue(i, "SequenceNumber"));
                e.setCreatorUserID(publicMethods.parseUUID((String) resultSet.getValue(i, "CreatorUserID")));
                e.setCreationDate((DateTime) resultSet.getValue(i, "CreationDate"));
                e.setLastModificationDate((DateTime) resultSet.getValue(i, "LastModificationDate"));
                e.setStatus(publicMethods.lookupEnum(WikiStatuses.class,
                        (String) resultSet.getValue(i, "Status"), WikiStatuses.None));

                ret.add(e);
            }
            catch (Exception e){
            }
        }

        return ret;
    }

    public List<Paragraph> paragraphs(RVResultSet resultSet)
    {
        List<Paragraph> ret = new ArrayList<>();

        for(int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i){
            try {
                Paragraph e = RVBeanFactory.getBean(Paragraph.class);

                e.setParagraphID(publicMethods.parseUUID((String) resultSet.getValue(i, "ParagraphID")));
                e.setTitleID(publicMethods.parseUUID((String) resultSet.getValue(i, "TitleID")));
                e.setTitle((String) resultSet.getValue(i, "Title"));
                e.setBodyText((String) resultSet.getValue(i, "BodyText"));
                e.setSequenceNumber((Integer) resultSet.getValue(i, "SequenceNumber"));
                e.setRichText((Boolean) resultSet.getValue(i, "IsRichText"));
                e.setCreatorUserID(publicMethods.parseUUID((String) resultSet.getValue(i, "CreatorUserID")));
                e.setCreationDate((DateTime) resultSet.getValue(i, "CreationDate"));
                e.setLastModificationDate((DateTime) resultSet.getValue(i, "LastModificationDate"));
                e.setStatus(publicMethods.lookupEnum(WikiStatuses.class,
                        (String) resultSet.getValue(i, "Status"), WikiStatuses.None));

                ret.add(e);
            }
            catch (Exception e){
            }
        }

        return ret;
    }

    public List<Change> changes(RVResultSet resultSet)
    {
        List<Change> ret = new ArrayList<>();

        for(int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i){
            try {
                Change e = RVBeanFactory.getBean(Change.class);

                e.setChangeID(publicMethods.parseUUID((String) resultSet.getValue(i, "ChangeID")));
                e.setParagraphID(publicMethods.parseUUID((String) resultSet.getValue(i, "ParagraphID")));
                e.setTitle((String) resultSet.getValue(i, "Title"));
                e.setBodyText((String) resultSet.getValue(i, "BodyText"));
                e.setStatus(publicMethods.lookupEnum(WikiStatuses.class,
                        (String) resultSet.getValue(i, "Status"), WikiStatuses.None));
                e.setApplied((Boolean) resultSet.getValue(i, "Applied"));
                e.setSendDate((DateTime) resultSet.getValue(i, "SendDate"));
                e.getSender().setUserID(publicMethods.parseUUID((String) resultSet.getValue(i, "SenderUserID")));
                e.getSender().setUserName((String) resultSet.getValue(i, "SenderUserName"));
                e.getSender().setFirstName((String) resultSet.getValue(i, "SenderFirstName"));
                e.getSender().setLastName((String) resultSet.getValue(i, "SenderLastName"));

                ret.add(e);
            }
            catch (Exception e){
            }
        }

        return ret;
    }

    public WikiOwnerType wikiOwner(RVResultSet resultSet, MutableUUID ownerId) {
        try {
            ownerId.setValue(publicMethods.parseUUID((String) resultSet.getValue(0, "OwnerID")));
            return publicMethods.lookupEnum(WikiOwnerType.class,
                    (String) resultSet.getValue(0, "OwnerType"), WikiOwnerType.NotSet);
        } catch (Exception ex) {
            return WikiOwnerType.NotSet;
        }
    }
}
