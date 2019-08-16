package com.raaivan.modules.rv;

import com.raaivan.util.dbutil.RVResultSet;
import com.raaivan.modules.rv.beans.DeletedState;
import com.raaivan.modules.rv.beans.EmailQueueItem;
import com.raaivan.modules.rv.beans.TaggedItem;
import com.raaivan.modules.rv.beans.Variable;
import com.raaivan.modules.rv.enums.EmailAction;
import com.raaivan.util.PublicMethods;
import com.raaivan.util.RVBeanFactory;
import com.raaivan.util.rvsettings.RVSettingsItem;
import com.raaivan.modules.rv.enums.TaggedItemType;
import com.raaivan.util.RVJSON;
import com.raaivan.util.Base64;
import io.micrometer.core.instrument.util.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;

@Component
@ApplicationScope
public class RVParsers {
    private PublicMethods publicMethods;

    @Autowired
    public void _setDependencies(PublicMethods publicMethods) {
        if (this.publicMethods == null) this.publicMethods = publicMethods;
    }

    public List<Variable> variables(RVResultSet resultSet)
    {
        List<Variable> ret = new ArrayList<>();

        for(int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i){
            try {
                Variable e = RVBeanFactory.getBean(Variable.class);

                e.setID((Long) resultSet.getValue(i, "ID"));
                e.setOwnerID(publicMethods.parseUUID((String) resultSet.getValue(i, "OwnerID")));
                e.setName((String) resultSet.getValue(i, "Name"));
                e.setValue((String) resultSet.getValue(i, "Value"));
                e.setCreatorUserID(publicMethods.parseUUID((String) resultSet.getValue(i, "CreatorUserID")));
                e.setCreationDate((DateTime) resultSet.getValue(i, "CreationDate"));

                ret.add(e);
            }
            catch (Exception e){
            }
        }

        return ret;
    }

    public List<EmailQueueItem> emailQueueItems(RVResultSet resultSet)
    {
        List<EmailQueueItem> ret = new ArrayList<>();

        for(int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i){
            try {
                EmailQueueItem e = RVBeanFactory.getBean(EmailQueueItem.class);

                e.setID((Long) resultSet.getValue(i, "ID"));
                e.setSenderUserID(publicMethods.parseUUID((String) resultSet.getValue(i, "SenderUserID")));
                e.setEmail((String) resultSet.getValue(i, "Email"));
                e.setTitle((String) resultSet.getValue(i, "Title"));
                e.setEmailBody((String) resultSet.getValue(i, "EmailBody"));
                e.setAction(publicMethods.lookupEnum(EmailAction.class,
                        (String) resultSet.getValue(i, "Action"), EmailAction.None));

                ret.add(e);
            }
            catch (Exception e){
            }
        }

        return ret;
    }

    public List<SimpleEntry<String, UUID>> guids(RVResultSet resultSet)
    {
        List<SimpleEntry<String, UUID>> ret = new ArrayList<>();

        for(int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i){
            try {
                String key = (String) resultSet.getValue(i, "ID");
                UUID value = publicMethods.parseUUID((String) resultSet.getValue(i, "Guid"));

                if(!StringUtils.isBlank(key) && value != null)
                    ret.add(new SimpleEntry<String, UUID>(key, value));
            }
            catch (Exception e){
            }
        }

        return ret;
    }

    public List<DeletedState> deletedStates(RVResultSet resultSet)
    {
        List<DeletedState> ret = new ArrayList<>();

        for(int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i){
            try {
                DeletedState e = RVBeanFactory.getBean(DeletedState.class);

                e.setID((Long) resultSet.getValue(i, "ID"));
                e.setObjectID(publicMethods.parseUUID((String) resultSet.getValue(i, "ObjectID")));
                e.setObjectType((String) resultSet.getValue(i, "ObjectType"));
                e.setDate((DateTime) resultSet.getValue(i, "Date"));
                e.setDeleted((Boolean) resultSet.getValue(i, "Deleted"));
                e.setBidirectional((Boolean) resultSet.getValue(i, "Bidirectional"));
                e.setHasReverse((Boolean) resultSet.getValue(i, "HasReverse"));
                e.setRelSourceID(publicMethods.parseUUID((String) resultSet.getValue(i, "RelSourceID")));
                e.setRelDestinationID(publicMethods.parseUUID((String) resultSet.getValue(i, "RelDestinationID")));
                e.setRelSourceType((String) resultSet.getValue(i, "RelSourceType"));
                e.setRelDestinationType((String) resultSet.getValue(i, "RelDestinationType"));
                e.setRelCreatorID(publicMethods.parseUUID((String) resultSet.getValue(i, "RelCreatorID")));

                ret.add(e);
            }
            catch (Exception e){
            }
        }

        return ret;
    }

    public List<TaggedItem> taggedItems(RVResultSet resultSet)
    {
        List<TaggedItem> ret = new ArrayList<>();

        for(int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i){
            try {
                TaggedItem e = RVBeanFactory.getBean(TaggedItem.class);

                e.setContextID(publicMethods.parseUUID((String) resultSet.getValue(i, "ID")));
                e.setTaggedType(publicMethods.lookupEnum(TaggedItemType.class,
                        (String) resultSet.getValue(i, "Type"), TaggedItemType.None));

                if(e.getTaggedType() == TaggedItemType.None) continue;
                else if(e.getTaggedType() == TaggedItemType.Node_Form || e.getTaggedType() == TaggedItemType.Node_Wiki)
                    e.setTaggedType(TaggedItemType.Node);

                ret.add(e);
            }
            catch (Exception e){
            }
        }

        return ret;
    }

    public Map<RVSettingsItem, String> settingItems(RVResultSet resultSet)
    {
        Map<RVSettingsItem, String> ret = new HashMap<>();

        for(int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i){
            try {
                String value = (String) resultSet.getValue(i, "Value");
                RVSettingsItem name = publicMethods.lookupEnum(RVSettingsItem.class,
                        (String) resultSet.getValue(i, "Name"), RVSettingsItem.None);

                if(!StringUtils.isBlank(value) && name != RVSettingsItem.None) ret.put(name, value);
            }
            catch (Exception e){
            }
        }

        return ret;
    }

    public List<RVJSON> lastActiveUsers(RVResultSet resultSet)
    {
        List<RVJSON> ret = new ArrayList<>();

        for(int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i){
            try {
                ret.add((new RVJSON())
                        .add("UserID", publicMethods.parseUUID((String) resultSet.getValue(i, "UserID")))
                        .add("UserName", Base64.encode((String) resultSet.getValue(i, "UserName")))
                        .add("FirstName", Base64.encode((String) resultSet.getValue(i, "FirstName")))
                        .add("LastName", Base64.encode((String) resultSet.getValue(i, "LastName")))
                        .add("Date", (DateTime) resultSet.getValue(i, "Date"))
                        .add("Types", (String) resultSet.getValue(i, "Types")));
            }
            catch (Exception e){
            }
        }

        return ret;
    }

    public Map<String, Integer> raaivanStatistics(RVResultSet resultSet)
    {
        Map<String, Integer> ret = new HashMap<>();

        if (resultSet.getRowsCount() > 0)
        {
            try
            {
                List<String> names = new ArrayList<String>() {{
                    add("NodesCount");
                    add("QuestionsCount");
                    add("AnswersCount");
                    add("WikiChangesCount");
                    add("PostsCount");
                    add("CommentsCount");
                    add("ActiveUsersCount");
                    add("NodePageVisitsCount");
                    add("SearchesCount");
                }};

                for (String n : names) {
                    Integer val = (Integer) resultSet.getValue(0, n);
                    if(val != null && val >= 0) ret.put(n, val);
                }
            }
            catch(Exception ex) { }
        }

        return ret;
    }
}
