package com.raaivan.modules.privacy;

import com.raaivan.util.dbutil.RVResultSet;
import com.raaivan.modules.privacy.beans.Audience;
import com.raaivan.modules.privacy.beans.ConfidentialityLevel;
import com.raaivan.modules.privacy.beans.DefaultPermission;
import com.raaivan.modules.privacy.beans.Privacy;
import com.raaivan.modules.privacy.enums.PermissionType;
import com.raaivan.modules.privacy.enums.PrivacyType;
import com.raaivan.util.PublicMethods;
import com.raaivan.util.RVBeanFactory;
import org.apache.commons.lang3.mutable.MutableLong;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.*;

@Component
@ApplicationScope
public class PrivacyParsers {
    private PublicMethods publicMethods;

    @Autowired
    public void _setDependencies(PublicMethods publicMethods) {
        if (this.publicMethods == null) this.publicMethods = publicMethods;
    }

    public Map<UUID, List<Audience>> audience(RVResultSet resultSet)
    {
        Map<UUID, List<Audience>> ret = new HashMap<>();

        for(int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i){
            try {
                Audience e = RVBeanFactory.getBean(Audience.class);

                e.setObjectID(publicMethods.parseUUID((String) resultSet.getValue(i, "ObjectID")));
                e.setRoleID(publicMethods.parseUUID((String) resultSet.getValue(i, "RoleID")));
                e.setPermission(publicMethods.lookupEnum(PermissionType.class,
                        (String) resultSet.getValue(i, "PermissionType"), PermissionType.None));
                e.setAllow((Boolean) resultSet.getValue(i, "Allow"));
                e.setExpirationDate((DateTime) resultSet.getValue(i, "ExpirationDate"));
                e.setRoleName((String) resultSet.getValue(i, "Name"));
                e.setRoleType((String) resultSet.getValue(i, "Type"));
                e.setNodeType((String) resultSet.getValue(i, "NodeType"));
                e.setAdditionalID((String) resultSet.getValue(i, "AdditionalID"));

                if(e.getObjectID() == null) continue;

                if(!ret.containsKey(e.getObjectID())) ret.put(e.getObjectID(), new ArrayList<>());

                ret.get(e.getObjectID()).add(e);
            }
            catch (Exception e){
            }
        }

        return ret;
    }

    public Map<UUID, List<PermissionType>> accessCheckedItems(RVResultSet resultSet)
    {
        Map<UUID, List<PermissionType>> ret = new HashMap<>();

        for(int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i){
            try {
                UUID id = publicMethods.parseUUID((String) resultSet.getValue(i, "ID"));
                PermissionType permission = publicMethods.lookupEnum(PermissionType.class,
                        (String) resultSet.getValue(i, "Type"), PermissionType.None);

                if(id != null && permission != PermissionType.None){
                    if (!ret.containsKey(id)) ret.put(id, new ArrayList<>());
                    if (ret.get(id).stream().noneMatch(u -> u == permission)) ret.get(id).add(permission);
                }
            }
            catch (Exception e){
            }
        }

        return ret;
    }

    public Map<UUID, List<DefaultPermission>> defaultPermissions(RVResultSet resultSet)
    {
        Map<UUID, List<DefaultPermission>> ret = new HashMap<>();

        for(int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i){
            try {
                UUID id = publicMethods.parseUUID((String) resultSet.getValue(i, "ID"));
                PermissionType permission = publicMethods.lookupEnum(PermissionType.class,
                        (String) resultSet.getValue(i, "Type"), PermissionType.None);
                PrivacyType defaultValue = publicMethods.lookupEnum(PrivacyType.class,
                        (String) resultSet.getValue(i, "DefaultValue"), PrivacyType.NotSet);

                if(id != null && permission != PermissionType.None && defaultValue != PrivacyType.NotSet){
                    if (!ret.containsKey(id)) ret.put(id, new ArrayList<>());

                    if (ret.get(id).stream().noneMatch(u -> u.getPermission() == permission)) {
                        DefaultPermission dp = RVBeanFactory.getBean(DefaultPermission.class);
                        dp.setPermission(permission);
                        dp.setDefaultValue(defaultValue);

                        ret.get(id).add(dp);
                    }
                }
            }
            catch (Exception e){
            }
        }

        return ret;
    }

    public List<Privacy> settings(RVResultSet resultSet)
    {
        List<Privacy> ret = new ArrayList<>();

        for(int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i){
            try {
                Privacy e = RVBeanFactory.getBean(Privacy.class);

                e.setObjectID(publicMethods.parseUUID((String) resultSet.getValue(i, "ObjectID")));
                e.setCalculateHierarchy((Boolean) resultSet.getValue(i, "CalculateHierarchy"));
                e.getConfidentiality().setID(publicMethods.parseUUID((String) resultSet.getValue(i, "ConfidentialityID")));
                e.getConfidentiality().setLevelID((Integer) resultSet.getValue(i, "LevelID"));
                e.getConfidentiality().setTitle((String) resultSet.getValue(i, "Level"));

                ret.add(e);
            }
            catch (Exception e){
            }
        }

        return ret;
    }

    public List<UUID> userIds(RVResultSet resultSet, MutableLong totalCount)
    {
        totalCount.setValue(0);

        List<UUID> ret = new ArrayList<>();

        for(int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i){
            try {
                UUID userId = publicMethods.parseUUID((String) resultSet.getValue(i, "UserID"));
                if(userId != null) {
                    ret.add(userId);
                    totalCount.setValue((Long) resultSet.getValue(i, "TotalCount"));
                }
            }
            catch (Exception e){
            }
        }

        return ret;
    }

    public List<ConfidentialityLevel> confidentialityLevels(RVResultSet resultSet)
    {
        List<ConfidentialityLevel> ret = new ArrayList<>();

        for(int i = 0, lnt = resultSet.getRowsCount(); i < lnt; ++i){
            try {
                ConfidentialityLevel e = RVBeanFactory.getBean(ConfidentialityLevel.class);

                e.setID(publicMethods.parseUUID((String) resultSet.getValue(i, "ID")));
                e.setLevelID((Integer) resultSet.getValue(i, "LevelID"));
                e.setTitle((String) resultSet.getValue(i, "Title"));

                ret.add(e);
            }
            catch (Exception e){
            }
        }

        return ret;
    }
}
