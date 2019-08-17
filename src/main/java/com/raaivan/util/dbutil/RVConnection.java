package com.raaivan.util.dbutil;

import com.microsoft.sqlserver.jdbc.SQLServerConnection;
import com.microsoft.sqlserver.jdbc.SQLServerPreparedStatement;
import com.raaivan.modules.rv.beans.Dashboard;
import com.raaivan.modules.rv.beans.Hierarchy;
import com.raaivan.modules.rv.enums.DashboardSubType;
import com.raaivan.modules.rv.enums.DashboardType;
import com.raaivan.util.PublicMethods;
import com.raaivan.web.aspects.annotations.RVDBCall;
import io.micrometer.core.instrument.util.StringUtils;
import org.apache.commons.lang3.mutable.MutableLong;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.sql.DataSource;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.stream.Collectors;

@Component
@ApplicationScope
public class RVConnection {
    private DataSource dataSource;
    private PublicMethods publicMethods;

    @Autowired
    public void _setDependencies(DataSource dataSource, PublicMethods publicMethods) {
        if (this.publicMethods == null) this.publicMethods = publicMethods;
        if (this.dataSource == null) this.dataSource = dataSource;
    }

    public String getSearchText(String searchText, boolean startWith)
    {
        if (StringUtils.isBlank(searchText)) return searchText;

        searchText = publicMethods.convertNumbersFromPersian(searchText)
                .replaceAll("\"", " ").replaceAll("'", " ");

        String[] words = searchText.split(" ");
        List<String> lstWords = new ArrayList<>();

        for (int i = 0; i < words.length; ++i)
            if (!StringUtils.isBlank(words[i].trim())) lstWords.add(words[i].trim());

        searchText = "ISABOUT(";
        for (int i = 0, _count = lstWords.size(); i < _count; ++i)
            searchText += (i == 0 ? "" : ",") + "\"" + lstWords.get(i) + (startWith ? "*" : "") +
                    "\" WEIGHT(" + (i > 4 ? 0.1 : (i == 0 ? 0.99 : 1.0) - (i * 0.2)) + ")";
        searchText += ")";

        return searchText;
    }

    public String getSearchText(String searchText){
        return getSearchText(searchText, true);
    }

    public String getTagsText(List<String> tags)
    {
        return tags == null || tags.size() == 0 ? "" : String.join(" ~ ", tags);
    }

    public List<String> getTagsList(String strTags, char delimiter)
    {
        if (StringUtils.isBlank(strTags)) return new ArrayList<>();

        List<String> retList = new ArrayList<>();

        return Arrays.stream(strTags.split(Character.toString(delimiter)))
                .map(String::trim)
                .filter(u -> !StringUtils.isBlank(u))
                .collect(Collectors.toList());
    }

    public List<String> getTagsList(String strTags){
        return getTagsList(strTags, '~');
    }

    private boolean addParameter(CallableStatement st, int index, Object value) {
        ++index;

        try {
            if (value == null) st.setNull(index, Types.NULL);
            else if (value instanceof Integer) st.setInt(index, (Integer) value);
            else if (value instanceof DateTime) st.setDate(index, new Date(((DateTime) value).toDateTime(DateTimeZone.UTC).getMillis()));
            else if (value instanceof Long) st.setLong(index, (Long) value);
            else if (value instanceof Float) st.setFloat(index, (Float) value);
            else if (value instanceof Double) st.setDouble(index, (Double) value);
            else if (value instanceof String) st.setString(index, (String) value);
            else st.setString(index, value.toString());

            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private boolean addParameter(SQLServerPreparedStatement st, int index, Object value) {
        ++index;

        try {
            if (value == null) st.setNull(index, Types.NULL);
            else if (value instanceof RVStructuredParam) {
                if(!((RVStructuredParam) value).setParameter(index, st)) return false;
            }
            else if (value instanceof Integer) st.setInt(index, (Integer) value);
            else if (value instanceof DateTime) st.setDate(index, new Date(((DateTime) value).toDateTime(DateTimeZone.UTC).getMillis()));
            else if (value instanceof Long) st.setLong(index, (Long) value);
            else if (value instanceof Float) st.setFloat(index, (Float) value);
            else if (value instanceof Double) st.setDouble(index, (Double) value);
            else if (value instanceof String) st.setString(index, (String) value);
            else st.setString(index, value.toString());

            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @RVDBCall
    public RVResultSet read(String procedureName, Object... params) {
        if(Arrays.stream(params).anyMatch(u -> u instanceof RVStructuredParam))
            return readStructured(procedureName, params);

        Connection connection = null;

        RVResultSet ret = new RVResultSet();

        try {
            connection = dataSource.getConnection();

            String[] questionMarks = new String[params.length];
            for (int i = 0; i < params.length; ++i) questionMarks[i] = "?";

            String query = "{call " + procedureName + "(" + String.join(",", questionMarks) + ")}";

            CallableStatement callableStatement = connection.prepareCall(query);

            for (int i = 0; i < params.length; ++i)
                if(!addParameter(callableStatement, i, params[i])) return new RVResultSet();

            boolean result = callableStatement.execute();

            while (result) {
                ResultSet resultSet = callableStatement.getResultSet();
                ResultSetMetaData rsmd = resultSet.getMetaData();

                List<String> colNames = new ArrayList<>();
                for (Integer i = 1; i <= rsmd.getColumnCount(); ++i)
                    colNames.add(StringUtils.isBlank(rsmd.getColumnName(i)) ? i.toString() : rsmd.getColumnName(i));

                ret.addTable(colNames);

                while (resultSet.next()) {
                    ret.addRow();

                    for (int i = 1; i <= rsmd.getColumnCount(); ++i)
                        ret.addValue(colNames.get(i - 1), resultSet.getObject(i));
                }

                resultSet.close();

                result = callableStatement.getMoreResults();
            }

            return ret;
        } catch (Exception ex) {
            return new RVResultSet();
        } finally {
            try {
                if (connection != null && !connection.isClosed()) connection.close();
            } catch (Exception e) {
            }
        }
    }

    private RVResultSet readStructured(String procedureName, Object... params){
        SQLServerConnection connection = null;

        RVResultSet ret = new RVResultSet();

        try{
            connection = dataSource.getConnection().unwrap(SQLServerConnection.class);

            String[] questionMarks = new String[params.length];
            for (int i = 0; i < params.length; ++i) questionMarks[i] = "?";

            String proc = "exec " + procedureName +
                    (questionMarks.length > 0 ? " " : "") + String.join(",", questionMarks);
            SQLServerPreparedStatement st = (SQLServerPreparedStatement)connection.prepareStatement(proc);

            for (int i = 0; i < params.length; ++i)
                if(!addParameter(st, i, params[i])) return new RVResultSet();

            boolean result = st.execute();

            while (result) {
                ResultSet resultSet = st.getResultSet();
                ResultSetMetaData rsmd = resultSet.getMetaData();

                List<String> colNames = new ArrayList<>();
                for (Integer i = 1; i <= rsmd.getColumnCount(); ++i)
                    colNames.add(StringUtils.isBlank(rsmd.getColumnName(i)) ? i.toString() : rsmd.getColumnName(i));

                ret.addTable(colNames);

                while (resultSet.next()) {
                    ret.addRow();

                    for (int i = 1; i <= rsmd.getColumnCount(); ++i)
                        ret.addValue(colNames.get(i - 1), resultSet.getObject(i));
                }

                resultSet.close();

                result = st.getMoreResults();
            }

            return ret;
        }
        catch (Exception ex) {
            return new RVResultSet();
        } finally {
            try {
                if (connection != null && !connection.isClosed()) connection.close();
            } catch (Exception e) {
            }
        }
    }

    public boolean succeed(StringBuilder errorMessage, List<Dashboard> retDashboards, String procedureName, Object... params) {
        RVResultSet result = read(procedureName, params);

        try {
            Object value = result.getValue(0, 0);
            Object error = result.getValue(0, 1);

            if (error instanceof String) {
                errorMessage.setLength(0);
                errorMessage.append((String) error);
            }

            if (result.getTablesCount() > 0) _parseDashboards(result, retDashboards, 1);

            if (value == null) return false;

            return (value instanceof Boolean && ((Boolean) value)) ||
                    (value instanceof Integer && ((Integer) value) > 0);
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean succeed(StringBuilder errorMessage, String procedureName, Object... params){
        return succeed(errorMessage, new ArrayList<>(), procedureName, params);
    }

    public boolean succeed(List<Dashboard> retDashboards, String procedureName, Object... params){
        StringBuilder errorMessage = new StringBuilder();
        return succeed(errorMessage, retDashboards, procedureName, params);
    }

    public boolean succeed(String procedureName, Object... params){
        StringBuilder errorMessage = new StringBuilder();
        return succeed(errorMessage, new ArrayList<>(), procedureName, params);
    }

    public int getInt(String procedureName, Object... params) {
        RVResultSet result = read(procedureName, params);

        try {
            Object value = result.getValue(0, 0);
            return value == null ? 0 : (int) value;
        } catch (Exception ex) {
            return 0;
        }
    }

    public long getLong(String procedureName, Object... params) {
        RVResultSet result = read(procedureName, params);

        try {
            Object value = result.getValue(0, 0);
            return value == null ? 0 : (long) value;
        } catch (Exception ex) {
            return 0;
        }
    }

    public DateTime getDate(String procedureName, Object... params) {
        RVResultSet result = read(procedureName, params);

        try {
            return (DateTime) result.getValue(0, 0);
        } catch (Exception ex) {
            return null;
        }
    }

    public UUID getUUID(String procedureName, Object... params) {
        RVResultSet result = read(procedureName, params);

        try {
            return UUID.fromString((String) result.getValue(0, 0));
        } catch (Exception ex) {
            return null;
        }
    }

    public String getString(String procedureName, Object... params) {
        RVResultSet result = read(procedureName, params);

        try {
            return (String) result.getValue(0, 0);
        } catch (Exception ex) {
            return null;
        }
    }

    public List<String> getStringList(String procedureName, Object... params) {
        RVResultSet result = read(procedureName, params);

        List<String> lst = new ArrayList<>();

        try {
            for(int i  = 0, lnt = result.getRowsCount(); i < lnt; ++i)
                lst.add((String) result.getValue(0, 0));
            return lst;
        } catch (Exception ex) {
            return lst;
        }
    }

    public List<UUID> getUUIDList(MutableLong totalCount, StringBuilder errorMessage, String procedureName, Object... params) {
        RVResultSet result = read(procedureName, params);

        List<UUID> lst = new ArrayList<>();
        errorMessage.setLength(0);

        try {
            for(int i  = 0, lnt = result.getRowsCount(); i < lnt; ++i)
                lst.add(UUID.fromString((String) result.getValue(0, 0)));

            if(result.getTablesCount() > 1) {
                try {
                    totalCount.setValue(Long.parseLong(result.getValue(1, 0, 0).toString()));
                } catch (Exception e) {
                }

                try {
                    errorMessage.append((String) result.getValue(1, 0, 1));
                } catch (Exception e) {
                }
            }

            return lst;
        } catch (Exception ex) {
            return lst;
        }
    }

    public List<UUID> getUUIDList(String procedureName, Object... params) {
        StringBuilder errorMessage = new StringBuilder("");
        MutableLong totalCount = new MutableLong(0);
        return getUUIDList(totalCount, errorMessage, procedureName, params);
    }

    public List<UUID> getUUIDList(MutableLong totalCount, String procedureName, Object... params) {
        StringBuilder errorMessage = new StringBuilder("");
        return getUUIDList(totalCount, errorMessage, procedureName, params);
    }

    public List<UUID> getUUIDList(StringBuilder errorMessage, String procedureName, Object... params) {
        MutableLong totalCount = new MutableLong(0);
        return getUUIDList(totalCount, errorMessage, procedureName, params);
    }

    public List<Hierarchy> getHierarchy(String procedureName, Object... params) {
        RVResultSet result = read(procedureName, params);

        List<Hierarchy> ret = new ArrayList<>();

        for(int i = 0, lnt = result.getRowsCount(); i < lnt; ++i){
            try {
                Hierarchy e = new Hierarchy();

                e.setID(publicMethods.parseUUID((String) result.getValue(i, "ID")));
                e.setParentID(publicMethods.parseUUID((String) result.getValue(i, "ParentID")));
                e.setLevel((Integer) result.getValue(i, "Level"));
                e.setName((String) result.getValue(i, "Name"));

                ret.add(e);
            }
            catch (Exception e){
            }
        }

        return ret;
    }

    public void _parseDashboards(RVResultSet result, List<Dashboard> retDashboards, int tableIndex) {
        for (int i = 0, lnt = result.getRowsCount(tableIndex); i < lnt; ++i) {
            try {
                Dashboard e = new Dashboard();

                e.setDashboardID((Long) result.getValue(tableIndex, i, "ID"));
                e.setUserID(publicMethods.parseUUID((String) result.getValue(tableIndex, i, "UserID")));
                e.setNodeID(publicMethods.parseUUID((String) result.getValue(tableIndex, i, "NodeID")));
                e.setNodeAdditionalID((String) result.getValue(tableIndex, i, "NodeAdditionalID"));
                e.setNodeName((String) result.getValue(tableIndex, i, "NodeName"));
                e.setNodeType((String) result.getValue(tableIndex, i, "NodeType"));
                e.setType(publicMethods.lookupEnum(DashboardType.class,
                        (String) result.getValue(tableIndex, i, "Type"), DashboardType.NotSet));
                e.setSubType(publicMethods.lookupEnum(DashboardSubType.class,
                        (String) result.getValue(tableIndex, i, "SubType"), DashboardSubType.NotSet));
                e.setInfo((String) result.getValue(tableIndex, i, "Info"));
                e.setRemovable((Boolean) result.getValue(tableIndex, i, "Removable"));
                e.setSenderUserID(publicMethods.parseUUID((String) result.getValue(tableIndex, i, "SenderUserID")));
                e.setSendDate((DateTime) result.getValue(tableIndex, i, "SendDate"));
                e.setExpirationDate((DateTime) result.getValue(tableIndex, i, "ExpirationDate"));
                e.setSeen((Boolean) result.getValue(tableIndex, i, "Seen"));
                e.setViewDate((DateTime) result.getValue(tableIndex, i, "ViewDate"));
                e.setDone((Boolean) result.getValue(tableIndex, i, "Done"));
                e.setActionDate((DateTime) result.getValue(tableIndex, i, "ActionDate"));
                e.setToBeRemoved((Boolean) result.getValue(tableIndex, i, "ToBeRemoved"));

                if(e.getDashboardID() == null) return;

                retDashboards.add(e);
            } catch (Exception e) {
            }
        }
    }

    public int getDashboards(StringBuilder errorMessage, List<Dashboard> retDashboards,
                               String procedureName, Object... params) {
        RVResultSet result = read(procedureName, params);

        errorMessage.setLength(0);

        if (result.getColumnsCount() <= 2) {
            try {
                String value = result.getValue(0, 1).toString();

                try {
                    if (result.getColumnsCount() > 1) errorMessage.append(value);
                } catch (Exception ex) {
                }

                return Integer.parseInt(value);
            } catch (Exception ex2) {
                return 0;
            }
        }

        _parseDashboards(result, retDashboards, 0);

        return 1;
    }

    public int getDashboards(List<Dashboard> retDashboards, String procedureName, Object... params)
    {
        StringBuilder msg = new StringBuilder();
        return getDashboards(msg, retDashboards, procedureName, params);
    }

    public List<SimpleEntry<UUID, Integer>> getItemsCountList(String procedureName, Object... params)
    {
        RVResultSet result = read(procedureName, params);

        List<SimpleEntry<UUID, Integer>> retDic = new ArrayList<>();

        for (int i = 0, lnt = result.getRowsCount(); i < lnt; ++i) {
            try {
                UUID id = publicMethods.parseUUID((String) result.getValue(i, "ID"));
                Integer count = (Integer) result.getValue(i, "Count");

                if(id != null && count != null) retDic.add(new SimpleEntry<UUID, Integer>(id, count));
            } catch (Exception e) {
            }
        }

        return retDic;
    }

    public Map<UUID, Integer> getItemsCount(String procedureName, Object... params)
    {
        List<SimpleEntry<UUID, Integer>> lst = getItemsCountList(procedureName, params);

        Map<UUID, Integer> retDic = new HashMap<>();

        for(SimpleEntry<UUID, Integer> e : lst)
            retDic.put(e.getKey(), e.getValue());

        return retDic;
    }

    public Map<UUID, Boolean> getItemsStatusBool(String procedureName, Object... params)
    {
        RVResultSet result = read(procedureName, params);

        Map<UUID, Boolean> retDic = new HashMap<>();

        for (int i = 0, lnt = result.getRowsCount(); i < lnt; ++i) {
            try {
                UUID id = publicMethods.parseUUID((String) result.getValue(i, "ID"));
                Boolean val = (Boolean) result.getValue(i, "Value");

                if(id != null && val != null) retDic.put(id, val);
            } catch (Exception e) {
            }
        }

        return retDic;
    }
}
